package com.tmtravlr.additions;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientConfigLoader {
	private static final String INTERNAL_CONFIG_FILE_NAME = "../additions_client_data.dat";
	private static File internalConfigFile;

	private static final String TAG_KEY_FOLDER_TEXTURES = "file_dialogue_folder_textures";
	private static final String TAG_KEY_FOLDER_STRUCTURES = "file_dialogue_folder_structures";
	private static final String TAG_KEY_FOLDER_SOUNDS = "file_dialogue_folder_sounds";
	private static String fileDialogueFolderTextures = null;
	private static String fileDialogueFolderStructures = null;
	private static String fileDialogueFolderSounds = null;

	public static void loadInternalConfigFile(FMLPreInitializationEvent event) {
		internalConfigFile = new File(event.getSuggestedConfigurationFile().getParentFile(), INTERNAL_CONFIG_FILE_NAME);
		
		if (internalConfigFile.exists()) {
			DataInputStream inputStream = null;
			
			try {
				inputStream = new DataInputStream(new FileInputStream(internalConfigFile));
				NBTTagCompound tag = CompressedStreamTools.readCompressed(inputStream);
				
				if (tag.hasKey(TAG_KEY_FOLDER_TEXTURES)) {
					fileDialogueFolderTextures = tag.getString(TAG_KEY_FOLDER_TEXTURES);
				}
				
				if (tag.hasKey(TAG_KEY_FOLDER_STRUCTURES)) {
					fileDialogueFolderStructures = tag.getString(TAG_KEY_FOLDER_STRUCTURES);
				}
				
				if (tag.hasKey(TAG_KEY_FOLDER_SOUNDS)) {
					fileDialogueFolderSounds = tag.getString(TAG_KEY_FOLDER_SOUNDS);
				}
			} catch (IOException e) {
				AdditionsMod.logger.error("Unable to load client config file!", e);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						AdditionsMod.logger.error("Unable to close client config file input stream!", e);
					}
				}
			}
		}
	}
	
	public static void saveInternalConfigFile() {
		NBTTagCompound tag = new NBTTagCompound();
		
		if (fileDialogueFolderTextures != null) {
			tag.setString(TAG_KEY_FOLDER_TEXTURES, fileDialogueFolderTextures);
		}
		
		if (fileDialogueFolderStructures != null) {
			tag.setString(TAG_KEY_FOLDER_STRUCTURES, fileDialogueFolderStructures);
		}
		
		if (fileDialogueFolderSounds != null) {
			tag.setString(TAG_KEY_FOLDER_SOUNDS, fileDialogueFolderSounds);
		}
		
		FileOutputStream outputStream = null;
		
		try {
			outputStream = new FileOutputStream(internalConfigFile);
			CompressedStreamTools.writeCompressed(tag, outputStream);
		} catch (IOException e) {
			AdditionsMod.logger.error("Unable to save client config file!", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					AdditionsMod.logger.error("Unable to close client config file output stream!", e);
				}
			}
		}
	}
	
	public static File getFileDialogueFolderTextures() {
		File folder = null;
		
		if (fileDialogueFolderTextures != null) {
			folder = new File(fileDialogueFolderTextures);
		}
		
		return folder;
	}
	
	public static void setFileDialogueFolderTextures(File folder) {
		if (folder == null) {
			fileDialogueFolderTextures = null;
		} else {
			fileDialogueFolderTextures = folder.getAbsolutePath();
		}
		
		saveInternalConfigFile();
	}
	
	public static File getFileDialogueFolderStructures() {
		File folder = null;
		
		if (fileDialogueFolderStructures != null) {
			folder = new File(fileDialogueFolderStructures);
		}
		
		return folder;
	}
	
	public static void setFileDialogueFolderStructures(File folder) {
		if (folder == null) {
			fileDialogueFolderStructures = null;
		} else {
			fileDialogueFolderStructures = folder.getAbsolutePath();
		}
		
		saveInternalConfigFile();
	}
	
	public static File getFileDialogueFolderSounds() {
		File folder = null;
		
		if (fileDialogueFolderSounds != null) {
			folder = new File(fileDialogueFolderSounds);
		}
		
		return folder;
	}
	
	public static void setFileDialogueFolderSounds(File folder) {
		if (folder == null) {
			fileDialogueFolderSounds = null;
		} else {
			fileDialogueFolderSounds = folder.getAbsolutePath();
		}
		
		saveInternalConfigFile();
	}

}
