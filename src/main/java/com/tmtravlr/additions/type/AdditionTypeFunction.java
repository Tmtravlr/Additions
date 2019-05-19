package com.tmtravlr.additions.type;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.ConfigLoader;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.functions.FunctionAdded;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Added functions
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class AdditionTypeFunction extends AdditionType<FunctionAdded> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "function");
	public static final String FOLDER_NAME = "data" + File.separator + "functions";
	public static final String FILE_POSTFIX = ".mcfunction";
	public static final AdditionTypeFunction INSTANCE = new AdditionTypeFunction();
	
	private final Multimap<Addon, FunctionAdded> loadedFunctions = HashMultimap.create();

	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		this.reloadAllFunctions(addons);
	}
	
	@Override
	public List<FunctionAdded> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.loadedFunctions.get(addon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, FunctionAdded addition) {
		if (!this.loadedFunctions.containsEntry(addon, addition)) {
			File additionFolder = new File(addon.addonFolder, FOLDER_NAME + File.separator + addition.id.getResourceDomain());
			
			if (!additionFolder.isDirectory()) {
				additionFolder.mkdirs();
			}

			File additionFile = new File(additionFolder, addition.id.getResourcePath() + FILE_POSTFIX);
			
			try {
				final PrintStream functionWriteStream = new PrintStream(additionFile);

				addition.commands.forEach(functionWriteStream::println);
				
				functionWriteStream.close();
			}
			catch (FileNotFoundException e) {
				AdditionsMod.logger.warn("Error saving function " + addition.id + " in addon " + addon.name, e);
			}
			
			this.loadedFunctions.put(addon, addition);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, FunctionAdded addition) {
		if (this.loadedFunctions.get(addon).contains(addition)) {
			this.loadedFunctions.get(addon).remove(addition);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);

		File additionFile = new File(additionFolder, addition.id.getResourceDomain() + File.separator + addition.id.getResourcePath() + FILE_POSTFIX);
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
	}
	
	public void reloadAllFunctions(List<Addon> addons) {
		if (ConfigLoader.replaceManagers.getBoolean(true)) {
			AdditionsMod.logger.info("Loading addon functions.");
			this.loadedFunctions.clear();
			
			for (Addon addon : addons) {
				List<String> filePaths = new ArrayList<>();
				
				try {
					filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
				} catch (IOException e) {
					AdditionsMod.logger.error("Error loading function files for addon " + addon.id + ". The functions will not load.", e);
				}
				
				for (String filePath : filePaths) {
					String functionName = filePath;
								
					int functionsFolderIndex = 0;
					if (functionName.startsWith(FOLDER_NAME + File.separator)) {
						functionsFolderIndex = FOLDER_NAME.length() + 1;
					}
					
					functionName = functionName.substring(functionsFolderIndex, functionName.length() - FILE_POSTFIX.length());
					
					if (functionName.contains(File.separator)) {
						String[] locationStrings = functionName.split(Pattern.quote(File.separator), 2);
						ResourceLocation location = new ResourceLocation(locationStrings[0], locationStrings[1]);
						
						try {
							this.loadedFunctions.put(addon, new FunctionAdded(location, AddonLoader.readAddonFileLines(addon.addonFolder, filePath)));
						} catch (IOException e) {
							AdditionsMod.logger.error("Error loading function " + location + "  for addon " + addon.id + ". It will be skipped.", e);
						}
					} else {
						AdditionsMod.logger.error("Addon function " + filePath + " can't be directly in the functions folder. It must be inside another folder (the domain).");
					}
				}
			}
		}
	}
	
	public void setAddonLoopFunction(Addon addon, ResourceLocation loopFunction) {
		addon.setLoopFunction(loopFunction);
	}
	
	public boolean hasFunctionWithId(Addon addon, ResourceLocation id) {
		for (FunctionAdded function : this.getAllAdditions(addon)) {
			if (function.id.equals(id)) {
				return true;
			}
		}
		return false;
	}
}
