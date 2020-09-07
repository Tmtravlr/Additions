package com.tmtravlr.additions.type;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.loottables.ExtendedLootTableManager;
import com.tmtravlr.additions.addon.loottables.LootTableAdded;
import com.tmtravlr.additions.addon.loottables.LootTablePreset;
import com.tmtravlr.additions.addon.loottables.LootTablePresetManager;
import com.tmtravlr.additions.util.GeneralUtils;
import com.tmtravlr.additions.util.ProblemNotifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Added structures
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2018
 */
public class AdditionTypeLootTable extends AdditionType<LootTableAdded> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "loot_table");
	public static final String FOLDER_NAME = "data" + File.separator + "loot_tables";
	public static final String FILE_POSTFIX = JSON_POSTFIX;
	public static final AdditionTypeLootTable INSTANCE = new AdditionTypeLootTable();
	
	public static final Gson GSON = GeneralUtils.newBuilder()
			.registerTypeHierarchyAdapter(LootTablePreset.class, new LootTablePresetManager.Serializer())
			.create();
	
	private final Multimap<Addon, LootTableAdded> lootTableLocations = HashMultimap.create();

	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		this.reloadAllLootTables(addons);
	}

	@Override
	public List<LootTableAdded> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.lootTableLocations.get(addon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, LootTableAdded addition) {
		if (addition.preset != null) {
			if (!this.lootTableLocations.containsEntry(addon, addition)) {
				this.lootTableLocations.put(addon, addition);
			}
			
			File lootTableFolder = new File(addon.addonFolder, FOLDER_NAME);
			
			if (!lootTableFolder.isDirectory()) {
				lootTableFolder.mkdir();
			}
			
			File additionFile = new File(lootTableFolder, addition.location.getResourceDomain() + File.separator + addition.location.getResourcePath() + FILE_POSTFIX);
			
			try {
				String fileContents = GSON.toJson(addition.preset);
				FileUtils.write(additionFile, fileContents, StandardCharsets.UTF_8);
			} catch (IOException | IllegalArgumentException e) {
				AdditionsMod.logger.error("Error saving addon " + addon.name, e);
			}
		} else {
			throw new IllegalArgumentException("Saving loot tables without a preset isn't supported!");
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, LootTableAdded addition) {
		if (this.lootTableLocations.containsEntry(addon, addition)) {
			this.lootTableLocations.remove(addon, addition);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);

		File additionFile = new File(additionFolder, addition.location.getResourceDomain() + File.separator + addition.location.getResourcePath() + FILE_POSTFIX);
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
	}
	
	public void reloadAllLootTables(List<Addon> addons) {
		AdditionsMod.logger.info("Loading addon loot tables.");
		this.lootTableLocations.clear();
		
		for (Addon addon : addons) {
			List<String> filePaths = new ArrayList<>();
			
			try {
				filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading loot table files for addon " + addon.id + ". The loot tables will not load.", e);
				ProblemNotifier.addProblemNotification(new TextComponentTranslation("gui.view.addon.lootTables.title", addon.id), new TextComponentString(e.getMessage()));
			}
			
			for (String filePath : filePaths) {
				String fileName = filePath;
							
				int lootTableFolderIndex = 0;
				if (fileName.startsWith(FOLDER_NAME + File.separator)) {
					lootTableFolderIndex = FOLDER_NAME.length() + 1;
				}
				
				fileName = fileName.substring(lootTableFolderIndex, fileName.length() - FILE_POSTFIX.length());
				
				if (fileName.contains(File.separator)) {
					String[] locationStrings = fileName.split(Pattern.quote(File.separator), 2);
					String locationPath = locationStrings[1];
					
					if (!"/".equals(File.separator)) {
						locationPath = locationPath.replace(File.separatorChar, '/');
					}
					
					ResourceLocation location = new ResourceLocation(locationStrings[0], locationPath);
					LootTablePreset preset = null;
					
					try {
						String fileString = AddonLoader.readAddonFile(addon.addonFolder, filePath);
						preset = GSON.fromJson(fileString, LootTablePreset.class);
						
						if (preset != null) {
							preset.id = location;
						}
					} catch (IOException | JsonParseException e) {
						AdditionsMod.logger.error("Error loading preset loot table " + filePath + " for addon " + addon.id + ". The loot table preset will not load", e);
						ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentString(e.getMessage()));
					}
					
					LootTableAdded lootTable = new LootTableAdded(location, preset);
					
					this.lootTableLocations.put(addon, lootTable);
				} else {
					AdditionsMod.logger.error("Addon loot table " + filePath + " can't be directly in the loot_tables folder. It must be inside another folder.");
					ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentTranslation("gui.notification.problem.directlyInFolder", "loot_tables"));
				}
			}
		}
	}

	public File getLootTableFolder(Addon addon) {
		return new File(addon.addonFolder, FOLDER_NAME + File.separator + "additions");
	}
	
	public boolean doesLootTableExistForAddon(Addon addon, ResourceLocation location) {
		return this.getAllAdditions(addon).stream().anyMatch(addition -> addition.location.equals(location));
	}
	
	public Optional<LootTableAdded> getLootTableForLocation(Addon addon, ResourceLocation location) {
		return this.getAllAdditions(addon).stream().filter(addition -> addition.location.equals(location)).findAny();
	}
	
	public Set<ResourceLocation> getAllLootTablesAdded() {
		return this.lootTableLocations.values().stream().map(addition -> addition.location).collect(Collectors.toSet());
	}
	
	public List<LootTable> getLootTableExtras(ResourceLocation lootTableLocation, LootTableManager lootTableManager) {
		ResourceLocation extrasLocation = new ResourceLocation(lootTableLocation.getResourceDomain(), lootTableLocation.getResourcePath() + "_extra");
		List<LootTable> extraLootTables = new ArrayList<>();
		
		for (Addon addon : AddonLoader.addonsLoaded) {
			if (this.lootTableLocations.get(addon).stream().anyMatch(lootTableAdded -> lootTableAdded.location.equals(extrasLocation))) {
				String filePath = FOLDER_NAME + File.separator + extrasLocation.getResourceDomain() + File.separator + extrasLocation.getResourcePath().replace('/', File.separatorChar) + FILE_POSTFIX;
				
				try {
					if (AddonLoader.addonFileExists(addon.addonFolder, filePath)) {
						String data = AddonLoader.readAddonFile(addon.addonFolder, filePath);
						
						extraLootTables.add(ForgeHooks.loadLootTable(ExtendedLootTableManager.LOOT_TABLE_GSON, extrasLocation, data, true, lootTableManager));
					}
	            } catch (JsonParseException e) {
	            	AdditionsMod.logger.warn("Parsing error loading extra loot table {} for addon {}", extrasLocation, addon.id, e);
					ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentString(e.getMessage()));
	            } catch (IOException ioexception) {
	            	AdditionsMod.logger.error("Couldn't load addon extra loot table {} from {} for addon {}", extrasLocation, filePath, addon.id, ioexception);
					ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentString(ioexception.getMessage()));
				}
			}
		}
		
		return extraLootTables;
	}
}
