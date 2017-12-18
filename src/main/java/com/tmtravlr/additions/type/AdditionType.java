package com.tmtravlr.additions.type;

import java.util.List;

import com.tmtravlr.additions.addon.Addon;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

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
	public abstract void loadPreInit(List<Addon> addons, FMLPreInitializationEvent event);
	
	/**
	 * Do the loading (from json files and such) during the init event.
	 */
	public abstract void loadInit(List<Addon> addons, FMLInitializationEvent event);
	
	/**
	 * Do the loading (from json files and such) during the post init event.
	 */
	public abstract void loadPostInit(List<Addon> addons, FMLPostInitializationEvent event);
	
	/**
	 * Do the loading (from json files and such) during the server starting event.
	 */
	public abstract void loadServerStarting(List<Addon> addons, FMLServerStartingEvent event);
	
	/**
	 * Do any necessary setup for a new addon being created.
	 */
	public abstract void setupNewAddon(Addon addon);

	/**
	 * Returns everything of this type added for the addon.
	 */
	public abstract List<T> getAllAdditions(Addon addon);
	
	/**
	 * Saves the addition of this type for the addon to a json file.
	 */
	public abstract void saveAddition(Addon addon, T addition);
	
	/**
	 * Deletes the json file for this addition and removes it from the addon.
	 */
	public abstract void deleteAddition(Addon addon, T addition);
	
}
