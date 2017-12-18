package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Added creative tabs
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class AdditionTypeCreativeTab extends AdditionType<CreativeTabAdded> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "creative_tab");
	public static final AdditionTypeCreativeTab INSTANCE = new AdditionTypeCreativeTab();
	
	private static final Gson GSON = new GsonBuilder()
			.registerTypeHierarchyAdapter(CreativeTabAdded.class, new CreativeTabAdded.Serializer())
			.setPrettyPrinting()
			.create();
	
	private HashMap<String, List<CreativeTabAdded>> loadedCreativeTabs = new HashMap<>();

	@Override
	public void loadPreInit(List<Addon> addons, FMLPreInitializationEvent event) {}

	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		AdditionsMod.logger.info("Loading addon creative tabs.");
		for (Addon addon : addons) {
			this.loadedCreativeTabs.put(addon.id, new ArrayList<>());
			
			File tabFolder = new File(addon.addonFolder, "creative_tabs");
			
			if (tabFolder.isDirectory()) {
				for (File file : tabFolder.listFiles()) {
					try {
						String fileString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
						CreativeTabAdded tabAdded = GSON.fromJson(fileString, CreativeTabAdded.class);

						String tabId = file.getName();
						
						if (tabId.endsWith(".json")) {
							tabId = tabId.substring(0, tabId.length()-5);
						}
						
						tabAdded.setId(tabId);
						
						this.loadedCreativeTabs.get(addon.id).add(tabAdded);
						
						AdditionsMod.logger.info("Loaded creative tab '" + tabAdded.getTabLabel() + "'");
					} catch (IOException e) {
						AdditionsMod.logger.error("Error loading creative tab " + file + ". The creative tab will not load.", e);
					} catch (JsonSyntaxException e) {
						AdditionsMod.logger.error("Error loading creative tab " + file + ". The creative tab will not load.", e);
					}
				}
			}
		}
	}

	@Override
	public void loadPostInit(List<Addon> addons, FMLPostInitializationEvent event) {}

	@Override
	public void loadServerStarting(List<Addon> addons, FMLServerStartingEvent event) {}

	@Override
	public void setupNewAddon(Addon addon) {
		this.loadedCreativeTabs.put(addon.id, new ArrayList<>());
	}
	
	@Override
	public List<CreativeTabAdded> getAllAdditions(Addon addon) {
		return this.loadedCreativeTabs.get(addon.id);
	}

	@Override
	public void saveAddition(Addon addon, CreativeTabAdded addition) {
		if (!this.loadedCreativeTabs.get(addon.id).contains(addition)) {
			this.loadedCreativeTabs.get(addon.id).add(addition);
		}
		
		File additionFolder = new File(addon.addonFolder, "creative_tabs");
		
		if (!additionFolder.isDirectory()) {
			additionFolder.mkdirs();
		}
		
		File additionFile = new File(additionFolder, addition.id + ".json");
		
		try {
			String fileContents = GSON.toJson(addition);
			FileUtils.write(additionFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException e) {
			AdditionsMod.logger.warn("Error saving addon " + addon.name, e);
		} catch (IllegalArgumentException e) {
			AdditionsMod.logger.error("Error saving addon " + addon.name, e);
		}
	}
	
	@Override
	public void deleteAddition(Addon addon, CreativeTabAdded addition) {
		if (this.loadedCreativeTabs.get(addon.id).contains(addition)) {
			this.loadedCreativeTabs.get(addon.id).remove(addition);
		}

		File additionFile = new File("creative_tabs", addition.id + ".json");
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
	}
	
	public boolean hasCreativeTabWithId(Addon addon, String id) {
		for (CreativeTabAdded creativeTab : this.getAllAdditions(addon)) {
			if (creativeTab.id.equals(id)) {
				return true;
			}
		}
		return false;
	}

}
