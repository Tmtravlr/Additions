package com.tmtravlr.additions.api;

import java.io.File;

import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.blocks.BlockAddedManager;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.addon.loottables.LootTablePreset;
import com.tmtravlr.additions.addon.loottables.LootTablePresetManager;
import com.tmtravlr.additions.addon.recipes.IRecipeAdded;
import com.tmtravlr.additions.addon.recipes.RecipeAddedManager;
import com.tmtravlr.additions.api.gui.IAdditionTypeGuiFactory;
import com.tmtravlr.additions.api.gui.IGuiBlockAddedFactory;
import com.tmtravlr.additions.api.gui.IGuiItemAddedFactory;
import com.tmtravlr.additions.api.gui.IGuiLootTablePresetFactory;
import com.tmtravlr.additions.api.gui.IGuiRecipeAddedFactory;
import com.tmtravlr.additions.type.AdditionType;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.util.ResourceLocation;

/**
 * Contains convenient methods for registering things
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since March 2019
 */
public class AdditionsModRegistry {
	
	/**
	 * Register a custom folder to load addons from.
	 * 
	 * @param addonFolder The custom folder containing addons to load
	 * @param loadResources True if Additions should load the resource packs for addons
	 *                      in this folder. Set to false for things like the lucky block
	 *                      mod that already load the resource packs
	 */
	public void registerAddonFolder(File addonFolder, boolean loadResources) {
		AddonLoader.ADDON_FOLDERS.put(addonFolder, loadResources);
	}

	/**
	 * Register a type of addition (like items, creative tabs, etc). A GUI for it will
	 * have to be registered separately.
	 * 
	 * @param name A name for the addition type
	 * @param toRegister The addition type instance
	 */
	public void registerAdditionType(ResourceLocation name, AdditionType toRegister) {
		AdditionTypeManager.registerAdditionType(name, toRegister);
	}

	/**
	 * Register the GUIs for a custom addition type. Well, right now it just registers
	 * the button that shows up in the list of addition types for an addon, that
	 * will create the GUI for the custom addition type.
	 * 
	 * @param factory A factory for registering custom GUIs
	 */
	public void registerAdditionTypeGui(IAdditionTypeGuiFactory factory) {
		AdditionTypeManager.registerAdditionTypeGuiFactory(factory);
	}

	/**
	 * Register a new type of added item.
	 * 
	 * @param itemAddedSerializer The serializer for the new item added type
	 */
	public void registerItemAddedType(IItemAdded.Serializer itemAddedSerializer) {
		ItemAddedManager.registerItemType(itemAddedSerializer);
	}

	/**
	 * Registers a factory for the GUIs for a new type of added item.
	 * 
	 * @param type The id of the new item added type
	 * @param factory The factory to create the GUIs for the item added type
	 */
	public void registerItemAddedGui(ResourceLocation type, IGuiItemAddedFactory factory) {
		ItemAddedManager.registerGuiFactory(type, factory);
	}

	/**
	 * Register a new type of added block.
	 * 
	 * @param blockAddedSerializer The serializer for the new block added type
	 */
	public void registerBlockAddedType(IBlockAdded.Serializer blockAddedSerializer) {
		BlockAddedManager.registerBlockType(blockAddedSerializer);
	}

	/**
	 * Registers a factory for the GUIs for a new type of added block.
	 * 
	 * @param type The id of the new block added type
	 * @param factory The factory to create the GUIs for the block added type
	 */
	public void registerBlockAddedGui(ResourceLocation type, IGuiBlockAddedFactory factory) {
		BlockAddedManager.registerGuiFactory(type, factory);
	}

	/**
	 * Register a new type of added recipe.
	 * 
	 * @param recipeAddedSerializer The serializer for the new recipe added type
	 */
	public void registerRecipeAddedType(IRecipeAdded.Serializer recipeAddedSerializer) {
		RecipeAddedManager.registerRecipeType(recipeAddedSerializer);
	}

	/**
	 * Registers a factory for the GUIs for a new type of added recipe.
	 * 
	 * @param type The id of the new recipe added type
	 * @param factory The factory to create the GUIs for the recipe added type
	 */
	public void registerRecipeAddedGui(ResourceLocation type, IGuiRecipeAddedFactory factory) {
		RecipeAddedManager.registerGuiFactory(type, factory);
	}

	/**
	 * Register a new type of loot table preset.
	 * 
	 * @param lootTablePresetSerializer The serializer for the new loot table preset type
	 */
	public void registerLootTablePresetType(LootTablePreset.Serializer lootTablePresetSerializer) {
		LootTablePresetManager.registerLootTablePresetType(lootTablePresetSerializer);
	}

	/**
	 * Registers a factory for the GUIs for a new type of loot table preset.
	 * 
	 * @param type The id of the new loot table preset type
	 * @param factory The factory to create the GUIs for the loot table preset type
	 */
	public void registerLootTablePresetGui(ResourceLocation type, IGuiLootTablePresetFactory factory) {
		LootTablePresetManager.registerGuiFactory(type, factory);
	}
	
}
