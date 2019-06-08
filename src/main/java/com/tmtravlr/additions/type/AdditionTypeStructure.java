package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;

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
public class AdditionTypeStructure extends AdditionType<ResourceLocation> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "structure");
	public static final String FOLDER_NAME = "data" + File.separator + "structures";
	public static final String FILE_POSTFIX = ".nbt";
	public static final AdditionTypeStructure INSTANCE = new AdditionTypeStructure();
	
	private final Multimap<Addon, ResourceLocation> structureLocations = HashMultimap.create();

	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		this.reloadAllStructures(addons);
	}

	@Override
	public List<ResourceLocation> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.structureLocations.get(addon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, ResourceLocation location) {
		throw new IllegalStateException("Saving structures isn't supported!");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, ResourceLocation location) {
		if (this.structureLocations.containsEntry(addon, location)) {
			this.structureLocations.remove(addon, location);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);

		File additionFile = new File(additionFolder, location.getResourceDomain() + File.separator + location.getResourcePath() + FILE_POSTFIX);
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
	}
	
	public boolean hasLocation(ResourceLocation location) {
		return this.structureLocations.containsValue(location);
	}
	
	public void reloadAllStructures(List<Addon> addons) {
		AdditionsMod.logger.info("Loading addon structures.");
		this.structureLocations.clear();
		
		for (Addon addon : addons) {
			List<String> filePaths = new ArrayList<>();
			
			try {
				filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading structure files for addon " + addon.id + ". The structures will not load.", e);
			}
			
			for (String filePath : filePaths) {
				String fileName = filePath;
				
				int structuresFolderIndex = 0;
				if (fileName.startsWith(FOLDER_NAME + File.separator)) {
					structuresFolderIndex = FOLDER_NAME.length() + 1;
				}
				
				fileName = fileName.substring(structuresFolderIndex, fileName.length() - FILE_POSTFIX.length());
				
				if (fileName.contains(File.separator)) {
					String[] locationStrings = fileName.split(Pattern.quote(File.separator), 2);
					String locationPath = locationStrings[1];
					
					if (!"/".equals(File.separator)) {
						locationPath = locationPath.replace(File.separatorChar, '/');
					}
					
					ResourceLocation location = new ResourceLocation(locationStrings[0], locationPath);
					
					this.structureLocations.put(addon, location);
				} else {
					AdditionsMod.logger.error("Addon structure " + filePath + " can't be directly in the structures folder. It must be inside another folder.");
				}
			}
		}
	}

	public File getStructureFolder(Addon addon) {
		return new File(addon.addonFolder, FOLDER_NAME + File.separator + "additions");
	}

	public void saveStructureFile(Addon addon, File structureFile) {
		File structureFolder = this.getStructureFolder(addon);
		
		String structureName = structureFile.getName();
		if (!structureName.startsWith(addon.id + "-")) {
			structureName = addon.id + "-" + structureName;
		}
		
		File addonStructureFile = new File(structureFolder, structureName);

		try {
			FileUtils.copyFile(structureFile, addonStructureFile);
		} catch (IOException e) {
			AdditionsMod.logger.error("Unable to copy structure file into addon", e);
		}
	}
}
