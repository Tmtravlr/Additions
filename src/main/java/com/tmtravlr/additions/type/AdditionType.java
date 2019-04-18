package com.tmtravlr.additions.type;

import java.util.List;

import com.tmtravlr.additions.addon.Addon;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Represents something that can be added (like items, or creative tabs).
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017
 * @param <T> The type that is being added, for instance IItemAdded for items or CreativeTabAdded for creative tabs.
 */
public abstract class AdditionType<T> {
	
	/**
	 * Do the loading (from json files and such) during the pre init event.
	 */
	public void loadPreInit(List<Addon> addons, FMLPreInitializationEvent event) {};
	
	/**
	 * Do the loading (from json files and such) during the init event.
	 */
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {};
	
	/**
	 * Do the loading (from json files and such) during the post init event.
	 */
	public void loadPostInit(List<Addon> addons, FMLPostInitializationEvent event) {};
	
	/**
	 * Do the loading (from json files and such) during the server starting event.
	 */
	public void loadServerStarting(List<Addon> addons, FMLServerStartingEvent event) {};
	
	/**
	 * Do any necessary setup for a new addon being created.
	 */
	public void setupNewAddon(Addon addon) {};

	/**
	 * Returns everything of this type added for the addon.
	 */
	public abstract List<T> getAllAdditions(Addon addon);
	
	/**
	 * Saves the addition of this type for the addon to a json file.
	 */
	@SideOnly(Side.CLIENT)
	public abstract void saveAddition(Addon addon, T addition);
	
	/**
	 * Deletes the json file for this addition and removes it from the addon.
	 */
	@SideOnly(Side.CLIENT)
	public abstract void deleteAddition(Addon addon, T addition);
	
}
