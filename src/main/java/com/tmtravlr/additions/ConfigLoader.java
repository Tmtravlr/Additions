package com.tmtravlr.additions;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigLoader {
	
	public static Configuration config;
	
	public static Property replaceManagers;
	public static Property renderAdditionsButtonInMainMenu;
	public static Property additionsMainMenuButtonX;
	public static Property additionsMainMenuButtonY;
	
	public static void loadConfigFile(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		replaceManagers = config.get("managers", "Replace the Structure, Function, Advancement, and Loot Table Managers", true, "Set to false to keep this mod from replacing the vanilla structure, function, advancement, and\nloot table managers. Will make addon structures, functions, advancements, and loot tables not work.");
		
		renderAdditionsButtonInMainMenu = config.get("gui", "Render Additions Button in Main Menu", true, "Set to false to remove the button rendered in the main menu. You can still get to the Additions\ngui by going to Mod Options and clicking Config for Additions.");
		additionsMainMenuButtonX = config.get("gui", "Additions Main Menu Button X Offset", 104, "X offset from the center of the screen for the additions main menu button. You can change it\nto change the button's position (in case it conflicts).");
		additionsMainMenuButtonY = config.get("gui", "Additions Main Menu Button Y Offset", 132, "Y offset from the center of the screen for the additions main menu button. You can change it\nto change the button's position (in case it conflicts).");
		
		config.save();
	}
}
