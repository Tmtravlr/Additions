package com.tmtravlr.additions;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigLoader {
	
	public static Configuration config;
	
	public static Property defaultFileDialogueFolder;
	public static Property renderAdditionsButtonInMainMenu;
	
	public static void loadConfigFile(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		defaultFileDialogueFolder = config.get("file_dialogue", "Default Folder", "", "Default folder that file choosers will start in. Updates automatically from the last folder you pick.");
		
		renderAdditionsButtonInMainMenu = config.get("gui", "Render Additions Button in Main Menu", true, "Set to false to remove the button rendered in the main menu. You can still get to the Additions\ngui by going to Mod Options and clicking Config for Additions.");
		
		config.save();
	}
	
	public static File getDefaultFileDialogueFolder() {
		File folder = new File(defaultFileDialogueFolder.getString());
		
		if (folder.isDirectory()) {
			return folder;
		} else {
			return null;
		}
	}
	
	public static void setDefaultFileDialogueFolder(File folder) {
		if (folder == null) {
			defaultFileDialogueFolder.set("");
		} else {
			defaultFileDialogueFolder.set(folder.getAbsolutePath());
		}
		
		config.save();
	}

}
