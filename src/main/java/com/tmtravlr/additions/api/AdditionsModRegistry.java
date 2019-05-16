package com.tmtravlr.additions.api;

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

	public void registerAdditionType(ResourceLocation name, AdditionType toRegister) {
		AdditionTypeManager.registerAdditionType(name, toRegister);
	}
	
	public void registerAdditionTypeGui(IAdditionTypeGuiFactory factory) {
		AdditionTypeManager.registerAdditionTypeGuiFactory(factory);
	}

	public void registerItemAddedType(IItemAdded.Serializer itemAddedSerializer) {
		ItemAddedManager.registerItemType(itemAddedSerializer);
	}
	
	public void registerItemAddedGui(ResourceLocation type, IGuiItemAddedFactory factory) {
		ItemAddedManager.registerGuiFactory(type, factory);
	}

	public void registerBlockAddedType(IBlockAdded.Serializer blockAddedSerializer) {
		BlockAddedManager.registerBlockType(blockAddedSerializer);
	}
	
	public void registerBlockAddedGui(ResourceLocation type, IGuiBlockAddedFactory factory) {
		BlockAddedManager.registerGuiFactory(type, factory);
	}

	public void registerRecipeAddedType(IRecipeAdded.Serializer recipeAddedSerializer) {
		RecipeAddedManager.registerRecipeType(recipeAddedSerializer);
	}
	
	public void registerRecipeAddedGui(ResourceLocation type, IGuiRecipeAddedFactory factory) {
		RecipeAddedManager.registerGuiFactory(type, factory);
	}
	
	public void registerLootTablePresetType(LootTablePreset.Serializer lootTablePresetSerializer) {
		LootTablePresetManager.registerLootTablePresetType(lootTablePresetSerializer);
	}
	
	public void registerLootTablePresetGui(ResourceLocation type, IGuiLootTablePresetFactory factory) {
		LootTablePresetManager.registerGuiFactory(type, factory);
	}
	
}
