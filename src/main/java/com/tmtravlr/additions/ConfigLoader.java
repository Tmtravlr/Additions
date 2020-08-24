package com.tmtravlr.additions;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Some config options.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class ConfigLoader {
	
	public static Configuration config;
	
	public static Property replaceManagers;
	
	public static Property renderAdditionsButtonInMainMenu;
	public static Property additionsMainMenuButtonX;
	public static Property additionsMainMenuButtonY;
	
	public static Property renderItemsInLists;
	public static Property skipReloadingResources;
	public static Property prettyPrintGeneratedFiles;
	
	public static Property showProblemNotificationsMainMenu;
	public static Property showProblemNotificationsIngame;
	
	public static void loadConfigFile(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		replaceManagers = config.get("managers", "Replace the Structure, Function, Advancement, and Loot Table Managers", true, "Set to false to keep this mod from replacing the vanilla structure, function, advancement, and\nloot table managers. Will make addon structures, functions, advancements, and loot tables not work.");
		
		renderAdditionsButtonInMainMenu = config.get("gui", "Render Additions Button in Main Menu", true, "Set to false to remove the button rendered in the main menu. You can still get to the Additions\ngui by going to Mod Options and clicking Config for Additions.");
		additionsMainMenuButtonX = config.get("gui", "Additions Main Menu Button X Offset", 104, "X offset from the center of the screen for the additions main menu button. You can change it\nto change the button's position (in case it conflicts).");
		additionsMainMenuButtonY = config.get("gui", "Additions Main Menu Button Y Offset", 132, "Y offset from the center of the screen for the additions main menu button. You can change it\nto change the button's position (in case it conflicts).");
		
		skipReloadingResources = config.get("editor", "Skip Reloading Resources", false, "If true, skips any resource reloading. Good if you want to add/edit a bunch of things quickly,\nwithout interruption. You can still reload the resources by restarting the game or by hitting F3+T ingame.");
		renderItemsInLists = config.get("editor", "Render Items in Item Selectors?", true, "If false, won't render items in item/block/ore dictionary selectors. Set to false if you are\ngetting crashes when looking at those selectors.");
		prettyPrintGeneratedFiles = config.get("editor", "Pretty-Print Generated Json Files", false, "If true, will generate JSON files that are easier to understand.");
		
		showProblemNotificationsMainMenu = config.get("problems", "Show Problem Notifications in Main Menu", true, "Set this to false to hide the problem notifications in the main menu and the additions main menu.");
		showProblemNotificationsIngame = config.get("problems", "Show Problem Notifications Ingame", true, "Set this to false to hide the problem notifications in chat.");
		
		config.save();
	}
}
