package com.tmtravlr.additions.api.gui;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.IRecipeAdded;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.gui.GuiScreen;

/**
 * Creates the edit screen for the recipe type.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since March 2019
 */
public interface IGuiRecipeAddedFactory<T extends IRecipeAdded> {
	
	/**
	 * Returns a friendlier title for the recipe type
	 */
	public String getTitle();
	
	/**
	 * Returns a short description about the recipe to show in the recipe type select screen
	 */
	public String getDescription();
	
	/**
	 * Renders a visual of the recipe in the card view
	 */
	public IGuiRecipeCardDisplay getRecipeCardDisplay(T recipe);

	/**
	 * Creates a screen to edit the recipe, or to add it if recipe is passed in as null.
	 */
	public GuiEdit getEditScreen(GuiScreen parent, Addon addon, T recipe);

	/**
	 * Creates a screen to create a copy of the recipe.
	 */
	public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, T recipe);
	
}
