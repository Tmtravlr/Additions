package com.tmtravlr.additions.type;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;
import com.tmtravlr.additions.util.GeneralUtils;
import com.tmtravlr.additions.util.ProblemNotifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Added creative tabs
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class AdditionTypeCreativeTab extends AdditionType<CreativeTabAdded> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "creative_tab");
	public static final String FOLDER_NAME = "data" + File.separator + "creative_tabs";
	public static final String FILE_POSTFIX = JSON_POSTFIX;
	public static final AdditionTypeCreativeTab INSTANCE = new AdditionTypeCreativeTab();
	
	private static final Gson GSON = GeneralUtils.newBuilder()
			.registerTypeHierarchyAdapter(CreativeTabAdded.class, new CreativeTabAdded.Serializer())
			.create();
	
	private Multimap<Addon, CreativeTabAdded> loadedCreativeTabs = HashMultimap.create();

	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		AdditionsMod.logger.info("Loading addon creative tabs.");
		for (Addon addon : addons) {
			List<String> filePaths = new ArrayList<>();
			
			try {
				filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading creative tab files for addon " + addon.id + ". The creative tabs will not load.", e);
				ProblemNotifier.addProblemNotification(new TextComponentTranslation("gui.view.addon.creativeTabs.title", addon.id), new TextComponentString(e.getMessage()));
			}
			
			for (String filePath : filePaths) {
				try {
					String fileString = AddonLoader.readAddonFile(addon.addonFolder, filePath);
					CreativeTabAdded tabAdded = GSON.fromJson(fileString, CreativeTabAdded.class);

					String tabId = filePath;
					if (tabId.contains(File.separator)) {
						tabId = tabId.substring(tabId.lastIndexOf(File.separator) + 1);
					}
					
					if (tabId.endsWith(FILE_POSTFIX)) {
						tabId = tabId.substring(0, tabId.length() - FILE_POSTFIX.length());

						tabAdded.setId(tabId);

						this.loadedCreativeTabs.put(addon, tabAdded);
					}
				} catch (IOException | JsonParseException e) {
					AdditionsMod.logger.error("Error loading creative tab " + filePath + " for addon " + addon.id + ". The creative tab will not load.", e);
					ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentString(e.getMessage()));
				}
			}
		}
	}
	
	@Override
	public List<CreativeTabAdded> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.loadedCreativeTabs.get(addon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, CreativeTabAdded addition) {
		if (!this.loadedCreativeTabs.containsEntry(addon, addition)) {
			this.loadedCreativeTabs.put(addon, addition);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);
		
		if (!additionFolder.isDirectory()) {
			additionFolder.mkdirs();
		}
		
		File additionFile = new File(additionFolder, addition.id + FILE_POSTFIX);
		
		try {
			String fileContents = GSON.toJson(addition);
			FileUtils.write(additionFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException | IllegalArgumentException e) {
			AdditionsMod.logger.warn("Error saving creative tab " + addition.id, e);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, CreativeTabAdded addition) {
		if (this.loadedCreativeTabs.containsEntry(addon, addition)) {
			this.loadedCreativeTabs.remove(addon, addition);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);

		File additionFile = new File(additionFolder, addition.id + FILE_POSTFIX);
		
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
