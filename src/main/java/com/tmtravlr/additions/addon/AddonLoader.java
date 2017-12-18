package com.tmtravlr.additions.addon;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.toposort.ModSortingException;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Loads addons from folders in the folder addons/Additions.
 * Also calls the addition type manager to load each type of addition.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class AddonLoader {
	
    private static final Gson GSON = new GsonBuilder()
    		.registerTypeAdapter(Addon.class, new Addon.Serializer())
    		.setPrettyPrinting()
    		.create();
	
	public static final String ADDON_LOCATION = "/addons/Additions";
	public static final Map<String, Addon> ADDONS_NAMED = new TreeMap<>();
	
	public static List<Addon> addonsLoaded = new ArrayList<>();
	public static File additionsFolder;
	
	public static boolean loadAddons() {
		
		//Load the addon folder
		try {
			additionsFolder = new File(net.minecraft.client.Minecraft.getMinecraft().mcDataDir, ADDON_LOCATION);
		} catch (NoClassDefFoundError e) {
			//Must be on a dedicated server
			additionsFolder = new File(new File("."), ADDON_LOCATION);
		}
		
		AdditionsMod.logger.info("Loading addon files from location " + additionsFolder);
		
		if (!additionsFolder.exists()) {
			additionsFolder.mkdirs();
		}

		//Get the files in the folder
		File[] addonFiles = additionsFolder.listFiles();
		AdditionsMod.logger.info("Found " + addonFiles.length + " addon(s) to load.");
		
		if (addonFiles != null && addonFiles.length > 0) {
			
			for (File addonFile : addonFiles) {
				File infoFile = new File(addonFile, "addon.json");
				if (infoFile.exists()) {
					try {
						
						String fileString = FileUtils.readFileToString(infoFile, StandardCharsets.UTF_8);
						Addon addon = GSON.fromJson(fileString, Addon.class);
						addon.setAddonFolder(addonFile);
						
						addonsLoaded.add(addon);
						ADDONS_NAMED.put(addon.id, addon);
						AdditionsMod.proxy.registerAsResourcePack(addonFile);

						AdditionsMod.logger.info("Loaded addon '" + addon.id + "'");
					} catch (IOException e) {
						AdditionsMod.logger.error("Error loading addon " + addonFile + ". The addon will not load.", e);
					} catch (JsonSyntaxException e) {
						AdditionsMod.logger.error("Error loading addon.json file in addon " + addonFile + ". The addon will not load.", e);
					}
				} else {
					AdditionsMod.logger.warn("Non-addon file or folder found in Additions addons: " + addonFile.getName());
				}
			}
		}
		
		//Re-organize the addons if needed
		for (Addon addon : addonsLoaded) {
			
			if (!addon.requiredMods.isEmpty()) {
				
				for (String modID : addon.requiredMods) {
					if (!Loader.isModLoaded(modID)) {
						AdditionsMod.proxy.throwAddonLoadingException("gui.loadingError.modNotFound", addon, modID);
						return false;
					}
				}
			}
				
			if (!addon.requiredAddons.isEmpty()) {	
				for (String addonId : addon.requiredAddons) {
					if (!ADDONS_NAMED.containsKey(addonId)) {
						AdditionsMod.proxy.throwAddonLoadingException("gui.loadingError.addonNotFound", addon, addonId);
						return false;
					}
				}
			}
		}
		
		AddonSorter sorter = new AddonSorter();
		
		try {
			addonsLoaded = sorter.sort();
		} catch (ModSortingException e) {
			AdditionsMod.proxy.throwAddonLoadingException("gui.loadingError.dependancyCycle", (Addon)e.getExceptionData().getFirstBadNode());
			return false;
		}
		
		return true;
	}
	
	public static void saveAddon(Addon addon) {
		File infoFile = new File(addon.addonFolder, "addon.json");
		
		try {
			String fileContents = GSON.toJson(addon);
			FileUtils.write(infoFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException e) {
			AdditionsMod.logger.warn("Error saving addon " + addon.name, e);
		} catch (IllegalArgumentException e) {
			AdditionsMod.logger.error("Error saving addon " + addon.name, e);
		}
	}
	
	public static void setupNewAddon(Addon addon) {
		addonsLoaded.add(addon);
		ADDONS_NAMED.put(addon.id, addon);
		AdditionTypeManager.setupNewAddon(addon);
		saveResourcePackFile(addon);
	}
	
	public static void loadAdditionsPreInit(FMLPreInitializationEvent event) {
		AdditionsMod.logger.info("Preinitializing addons.");
		AdditionTypeManager.loadPreInit(addonsLoaded, event);
	}
	
	public static void loadAdditionsInit(FMLInitializationEvent event) {
		AdditionsMod.logger.info("Initializing addons.");
		AdditionTypeManager.loadInit(addonsLoaded, event);
	}
	
	public static void loadAdditionsPostInit(FMLPostInitializationEvent event) {
		AdditionsMod.logger.info("Postinitializing addons.");
		AdditionTypeManager.loadPostInit(addonsLoaded, event);
	}
	
	public static void loadAdditionsServerStarting(FMLServerStartingEvent event) {
		AdditionsMod.logger.info("Loading addons as server starts.");
		AdditionTypeManager.loadServerStarting(addonsLoaded, event);
	}
	
	private static void saveResourcePackFile(Addon addon) {
		File packMeta = new File(addon.addonFolder, "pack.mcmeta");
		
		if (!packMeta.exists()) {
			String packMetaString = "{\n" +
									"  \"pack\": {\n" +
									"    \"pack_format\": 3,\n" +
									"    \"description\": \"Additions Addon: " + addon.id + "\"\n" +
									"  }\n" +
									"}";
			
			try {
				FileUtils.write(packMeta, packMetaString, StandardCharsets.UTF_8);
			} catch (IOException e) {
				AdditionsMod.logger.warn("Error creating pack meta for " + addon.name, e);
			} catch (IllegalArgumentException e) {
				AdditionsMod.logger.error("Error creating pack meta for " + addon.name, e);
			}
		}
	}
	
}
