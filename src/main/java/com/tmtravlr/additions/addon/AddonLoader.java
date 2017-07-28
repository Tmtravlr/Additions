package com.tmtravlr.additions.addon;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.toposort.ModSortingException;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedManager;

/**
 * Loads addons from folders in the folder addons/Additions.
 * 
 * @author Rebeca Rey
 * @Date July 2017 
 */
public class AddonLoader {
	
    private static final Gson GSON = (new GsonBuilder())
    		.registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer())
    		.registerTypeAdapter(AddonInfo.class, new AddonInfo.Serializer())
    		.registerTypeAdapter(CreativeTabAdded.class, new CreativeTabAdded.Serializer())
    		.registerTypeHierarchyAdapter(IItemAdded.class, new ItemAddedManager.Serializer())
    		.create();
	
	public static final String ADDON_LOCATION = "/addons/Additions";
	public static final Map<String, AddonInfo> ADDONS_NAMED = new TreeMap<>();
	
	public static List<AddonInfo> addonsLoaded = new ArrayList<>();
	public static File addonFolder;
	
	public static Multimap<String, Item> oreDictsToRegister = HashMultimap.create();
	public static List<Item> colorItemsToRegister = new ArrayList<>();
	
	public static void loadAddons() {
		
		//Load the addon folder
		try {
			addonFolder = new File(net.minecraft.client.Minecraft.getMinecraft().mcDataDir, ADDON_LOCATION);
		} catch (NoClassDefFoundError e) {
			//Must be on a dedicated server
			addonFolder = new File(new File("."), ADDON_LOCATION);
		}
		
		AdditionsMod.logger.info("Loading addon files from location " + addonFolder);
		
		if (!addonFolder.exists()) {
			addonFolder.mkdirs();
		}

		//Get the files in the folder
		File[] addonFiles = addonFolder.listFiles();
		AdditionsMod.logger.info("Found " + addonFiles.length + " addon(s) to load.");
		
		if (addonFiles != null && addonFiles.length > 0) {
			
			for (File addonFile : addonFiles) {
				File infoFile = new File(addonFile, "addon_info.json");
				if (infoFile.exists()) {
					try {
						
						String fileString = FileUtils.readFileToString(infoFile, StandardCharsets.UTF_8);
						AddonInfo addon = GSON.fromJson(fileString, AddonInfo.class);
						addon.setAddonFile(addonFile);
						
						addonsLoaded.add(addon);
						ADDONS_NAMED.put(addon.id, addon);
						AdditionsMod.proxy.registerAsResourcePack(addonFile);

						AdditionsMod.logger.info("Loaded addon '" + addon.id + "'");
					} catch (IOException e) {
						AdditionsMod.logger.error("Error loading addon " + addonFile + ". The addon will not load.", e);
					} catch (JsonSyntaxException e) {
						AdditionsMod.logger.error("Error loading addon_info.json file in addon " + addonFile + ". The addon will not load.", e);
					}
				} else {
					AdditionsMod.logger.warn("Non-addon file or folder found in Additions addons: " + addonFile.getName());
				}
			}
		}
		
		//Re-organize the addons if needed
		for (AddonInfo addon : addonsLoaded) {
			
			if (!addon.requiredMods.isEmpty()) {
				
				for (String modID : addon.requiredMods) {
					if (!Loader.isModLoaded(modID)) {
						AdditionsMod.proxy.throwAddonLoadingException("gui.loadingError.modNotFound", addon.id, modID);
					}
				}
			}
				
			if (!addon.requiredAddons.isEmpty()) {	
				for (String addonId : addon.requiredAddons) {
					if (!ADDONS_NAMED.containsKey(addonId)) {
						AdditionsMod.proxy.throwAddonLoadingException("gui.loadingError.addonNotFound", addon.id, addonId);
					}
				}
			}
		}
		
		AddonSorter sorter = new AddonSorter();
		
		try {
			addonsLoaded = sorter.sort();
		} catch (ModSortingException e) {
			AdditionsMod.proxy.throwAddonLoadingException("gui.loadingError.loop", ((AddonInfo)e.getExceptionData().getFirstBadNode()).id);
		}
	}
	
	public void saveAddonInfo(AddonInfo addon) {
		File infoFile = new File(addon.addonFile, "addon_info.json");
		
		try {
			String fileContents = GSON.toJson(addon);
			FileUtils.write(infoFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException e) {
			AdditionsMod.logger.warn("Error saving addon " + addon.name, e);
		} catch (IllegalArgumentException e) {
			AdditionsMod.logger.error("Error saving addon " + addon.name, e);
		}
	}
	
	public static void loadAddonItems() {
		AdditionsMod.logger.info("Loading addon items.");
		
		for (AddonInfo addon : addonsLoaded) {
			File itemFolder = new File(addon.addonFile, "items");
			
			if (itemFolder.isDirectory()) {
				for (File file : itemFolder.listFiles()) {
					try {
						String fileString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
						IItemAdded itemAdded = GSON.fromJson(fileString, IItemAdded.class).getAsItem();
						addon.itemsAdded.add(itemAdded);
						Item itemAddedItem = itemAdded.getAsItem();
						
						String itemName = file.getName();
						
						if (itemName.endsWith(".json")) {
							itemName = itemName.substring(0, itemName.length()-5);
						}
						
						itemName = addon.id + "." + itemName;
						ResourceLocation itemRegistryName = new ResourceLocation(AdditionsMod.MOD_ID, itemName);
						
						ForgeRegistries.ITEMS.register(itemAddedItem.setUnlocalizedName(itemName).setRegistryName(itemRegistryName));
						
						AdditionsMod.logger.info("Loaded item '" + itemRegistryName + "'");
					} catch (IOException e) {
						AdditionsMod.logger.error("Error loading item " + file + ". The item will not load.", e);
					} catch (JsonSyntaxException e) {
						AdditionsMod.logger.error("Error loading item " + file + ". The item will not load.", e);
					}
				}
			}
		}
		
		if (!oreDictsToRegister.isEmpty()) {
			for (String oreName : oreDictsToRegister.keySet()) {
				for (Item item : oreDictsToRegister.get(oreName)) {
					OreDictionary.registerOre(oreName, item);
				}
			}
			oreDictsToRegister.clear();
		}
	}
	
	public static void loadAddonItemModels() {
		AdditionsMod.logger.info("Loading addon item models.");
		
		for (AddonInfo addon : addonsLoaded) {
			for (IItemAdded item : addon.itemsAdded) {
				item.registerModels();
				colorItemsToRegister.add(item.getAsItem());
			}
		}
		
		if(!colorItemsToRegister.isEmpty()) {
			AdditionsMod.proxy.registerItemColors(colorItemsToRegister.toArray(new Item[0]));
			colorItemsToRegister.clear();
		}
	}
	
	public static void loadAddonCreativeTabs() {
		AdditionsMod.logger.info("Loading addon creative tabs.");
		
		for (AddonInfo addon : addonsLoaded) {
			File itemFolder = new File(addon.addonFile, "creative_tabs");
			
			if (itemFolder.isDirectory()) {
				for (File file : itemFolder.listFiles()) {
					try {
						String fileString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
						CreativeTabAdded tabAdded = GSON.fromJson(fileString, CreativeTabAdded.class);
						
						addon.creativeTabsAdded.add(tabAdded);
						
						AdditionsMod.logger.info("Loaded creative tab '" + tabAdded.getTabLabel() + "'");
					} catch (IOException e) {
						AdditionsMod.logger.error("Error loading creative tab " + file + ". The creative tab will not load.", e);
					} catch (JsonSyntaxException e) {
						AdditionsMod.logger.error("Error loading creative tab " + file + ". The creative tab will not load.", e);
					}
				}
			}
		}
		
		if (!oreDictsToRegister.isEmpty()) {
			for (String oreName : oreDictsToRegister.keySet()) {
				for (Item item : oreDictsToRegister.get(oreName)) {
					OreDictionary.registerOre(oreName, item);
				}
			}
		}
	}
	
}
