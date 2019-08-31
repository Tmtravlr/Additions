package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.sounds.SoundEventAdded;
import com.tmtravlr.additions.util.ProblemNotifier;
import com.tmtravlr.additions.util.client.SoundsJsonSerializer;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundList;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Added sound events
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since October 2018 
 */
public class AdditionTypeSoundEvent extends AdditionType<SoundEventAdded> {

	@SideOnly(Side.CLIENT)
	private static Gson SOUNDS_JSON_GSON;
	@SideOnly(Side.CLIENT)
	private static ParameterizedType SOUNDS_JSON_TYPE;
	
	static {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			SOUNDS_JSON_GSON = ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, null, "field_147699_c", "GSON");
			SOUNDS_JSON_TYPE = ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, null, "field_147696_d", "TYPE");
		}
	}
	
	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "sound");
	public static final String EVENT_LIST_FILE = "data" + File.separator + "sound_events" + File.separator + "sound_event_list.txt";
	public static final String SOUNDS_JSON_FILE = "assets" + File.separator + AdditionsMod.MOD_ID + File.separator + "sounds.json";
	public static final String SOUND_FOLDER_NAME = "assets" + File.separator + AdditionsMod.MOD_ID + File.separator + "sounds";
	public static final String SOUND_FILE_POSTFIX = ".ogg";
	public static final AdditionTypeSoundEvent INSTANCE = new AdditionTypeSoundEvent();
	
	private final Multimap<Addon, SoundEventAdded> loadedSoundEvents = HashMultimap.create();

	@Override
	public void loadPreInit(List<Addon> addons, FMLPreInitializationEvent event) {
		AdditionsMod.logger.info("Loading addon sound events.");
		this.loadedSoundEvents.clear();
		
		for (Addon addon : addons) {
			try {
				if (AddonLoader.addonFileExists(addon.addonFolder, EVENT_LIST_FILE)) {
					List<String> eventList = AddonLoader.readAddonFileLines(addon.addonFolder, EVENT_LIST_FILE);
					
					if (!eventList.isEmpty()) {
						for (String eventName : eventList) {
							if (!StringUtils.isNullOrEmpty(eventName)) {
								ResourceLocation name = new ResourceLocation(eventName);
								SoundEventAdded soundEventAdded = new SoundEventAdded(name);
								ForgeRegistries.SOUND_EVENTS.register(soundEventAdded);
								this.loadedSoundEvents.put(addon, soundEventAdded);
							}
						}
					}
				}
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading sound events for addon " + addon.id + ". The sound events will not load.", e);
				ProblemNotifier.addProblemNotification(new TextComponentTranslation("gui.view.addon.soundEvents.title", addon.id), new TextComponentString(e.getMessage()));
			}
		}
	}
	
	@Override
	public List<SoundEventAdded> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.loadedSoundEvents.get(addon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, SoundEventAdded addition) {
		if (!this.loadedSoundEvents.containsEntry(addon, addition)) {
			this.loadedSoundEvents.put(addon, addition);
			
			this.saveSoundFiles(addon);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, SoundEventAdded addition) {
		if (this.loadedSoundEvents.containsEntry(addon, addition)) {
			this.loadedSoundEvents.remove(addon, addition);
			
			this.saveSoundFiles(addon);
		}
	}
	
	public boolean hasSoundEventWithId(Addon addon, ResourceLocation location) {
		return this.loadedSoundEvents.get(addon).stream().anyMatch(soundEventAdded -> soundEventAdded.getRegistryName().equals(location));
	}

	@SideOnly(Side.CLIENT)
	public void loadSoundList(SoundEventAdded eventAdded) {
		Entry<Addon, SoundEventAdded> eventEntry = this.loadedSoundEvents.entries().stream().filter(entry -> entry.getValue() == eventAdded).findFirst().orElse(null);
		
		if (eventEntry != null) {
			this.loadSoundListFromMap(loadSoundsJson(eventEntry.getKey()), eventAdded, eventEntry.getKey());
		} else {
			AdditionsMod.logger.warn("Couldn't find sound event " + eventAdded.getRegistryName() + " loaded ingame. Things are probably messed up.");
		}
	}

	@SideOnly(Side.CLIENT)
	public void loadAllSoundListsForAddon(Addon addon) {
		Map<ResourceLocation, SoundList> soundListMap = loadSoundsJson(addon);
		
		for (SoundEventAdded eventAdded : this.loadedSoundEvents.get(addon)) {
			this.loadSoundListFromMap(soundListMap, eventAdded, addon);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public List<ResourceLocation> getAllSoundFilesForAddon(Addon addon) {
		List<String> filePaths = new ArrayList<>();
		List<ResourceLocation> soundFiles = new ArrayList<>();
		
		try {
			filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, SOUND_FOLDER_NAME);
		} catch (IOException e) {
			AdditionsMod.logger.error("Error loading sound files for addon " + addon.id + ".", e);
		}
		
		for (String filePath : filePaths) {
			String fileName = filePath;
			
			int functionsFolderIndex = 0;
			if (fileName.startsWith(SOUND_FOLDER_NAME + File.separator)) {
				functionsFolderIndex = SOUND_FOLDER_NAME.length() + 1;
			}
			
			fileName = fileName.substring(functionsFolderIndex, fileName.length() - SOUND_FILE_POSTFIX.length());
			
			if (!"/".equals(File.separator)) {
				fileName = fileName.replace(File.separatorChar, '/');
			}
			
			soundFiles.add(new ResourceLocation(AdditionsMod.MOD_ID, fileName));
		}
		
		return soundFiles;
	}
	
	@SideOnly(Side.CLIENT)
	public void saveSoundFile(Addon addon, File fileChosen, ResourceLocation location) throws IOException {
		File soundFile = new File(addon.addonFolder, SOUND_FOLDER_NAME + File.separator + location.getResourcePath() + SOUND_FILE_POSTFIX);
		FileUtils.copyFile(fileChosen, soundFile);
	}
	
	@SideOnly(Side.CLIENT)
	private void loadSoundListFromMap(Map<ResourceLocation, SoundList> soundListMap, SoundEventAdded eventAdded, Addon addon) {
		SoundList soundList = soundListMap.get(eventAdded.getRegistryName());
		
		if (soundList == null) {
			AdditionsMod.logger.warn("Couldn't find sound list " + eventAdded.getRegistryName() + " for the addon " + addon.id + ". Is it missing from the sounds.json file?");
			soundList = new SoundList(new ArrayList<>(), false, "");
		}
		
		eventAdded.setSoundList(soundList);
	}
	
	@SideOnly(Side.CLIENT)
	private Map<ResourceLocation, SoundList> loadSoundsJson(Addon addon) {
		Map<ResourceLocation, SoundList> soundListMap = new HashMap<>();
		File soundsJsonFile = new File(addon.addonFolder, SOUNDS_JSON_FILE);
		
		try {
			if (AddonLoader.addonFileExists(addon.addonFolder, SOUNDS_JSON_FILE)) {
				Map<String, SoundList> soundsJsonMap = JsonUtils.gsonDeserialize(SOUNDS_JSON_GSON, AddonLoader.readAddonFile(addon.addonFolder, SOUNDS_JSON_FILE), SOUNDS_JSON_TYPE);
				soundListMap = soundsJsonMap.entrySet().stream().collect(Collectors.toMap(entry -> new ResourceLocation(AdditionsMod.MOD_ID, entry.getKey()), entry -> entry.getValue()));
			}
		} catch (IOException | JsonParseException e) {
			AdditionsMod.logger.warn("Error loading sounds.json for addon " + addon.id + ".", e);
			ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromFile(soundsJsonFile), new TextComponentString(e.getMessage()));
        }
		
		return soundListMap;
	}

	@SideOnly(Side.CLIENT)
	private void saveSoundFiles(Addon addon) {
		File eventListFile = new File(addon.addonFolder, EVENT_LIST_FILE);
		
		if (!eventListFile.getParentFile().exists()) {
			eventListFile.getParentFile().mkdirs();
		}
		
		try {
			Files.write(eventListFile.toPath(), this.loadedSoundEvents.get(addon).stream().map(event -> event.getRegistryName().toString()).collect(Collectors.toList()), StandardCharsets.UTF_8);
			
			this.saveSoundsJson(addon);
		} catch (IOException e) {
			AdditionsMod.logger.error("Error saving sound events for addon " + addon.id + ". The sound events will not save.", e);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void saveSoundsJson(Addon addon) {
		File soundsJsonFile = new File(addon.addonFolder, SOUNDS_JSON_FILE);
		
		if (!soundsJsonFile.getParentFile().exists()) {
			soundsJsonFile.getParentFile().mkdirs();
		}
		
		try {
			String fileContents = SoundsJsonSerializer.GSON.toJson(this.loadedSoundEvents.get(addon));
			FileUtils.write(soundsJsonFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException | IllegalArgumentException e) {
			AdditionsMod.logger.warn("Error saving sounds.json file for addon " + addon.id + ". The sounds.json file will not save.", e);
		}
	}
}
