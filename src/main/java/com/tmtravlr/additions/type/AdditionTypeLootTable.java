package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.loottables.LootTableAdded;
import com.tmtravlr.additions.addon.loottables.LootTablePreset;
import com.tmtravlr.additions.addon.loottables.LootTablePresetManager;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Added structures
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class AdditionTypeLootTable extends AdditionType<LootTableAdded> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "loot_table");
	public static final String FOLDER_NAME = "data" + File.separator + "loot_tables";
	public static final String FILE_POSTFIX = ".json";
	public static final AdditionTypeLootTable INSTANCE = new AdditionTypeLootTable();
	
	public static final Gson GSON = new GsonBuilder()
			.registerTypeHierarchyAdapter(LootTablePreset.class, new LootTablePresetManager.Serializer())
			.setPrettyPrinting()
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
					ResourceLocation location = new ResourceLocation(locationStrings[0], locationStrings[1]);
					LootTablePreset preset = null;
					
					try {
						String fileString = AddonLoader.readAddonFile(addon.addonFolder, filePath);
						preset = GSON.fromJson(fileString, LootTablePreset.class);
					} catch (IOException | JsonParseException e) {
						AdditionsMod.logger.error("Error loading preset loot table " + filePath + " for addon " + addon.id + ". The loot table preset will not load", e);
					}
					
					LootTableAdded lootTable = new LootTableAdded(location, preset);
					
					this.lootTableLocations.put(addon, lootTable);
				} else {
					AdditionsMod.logger.error("Addon loot table " + filePath + " can't be directly in the loot_tables folder. It must be inside another folder.");
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
}
