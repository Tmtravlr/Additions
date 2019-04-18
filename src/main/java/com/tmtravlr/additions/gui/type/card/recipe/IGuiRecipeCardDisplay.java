package com.tmtravlr.additions.gui.type.card.recipe;

import com.tmtravlr.additions.addon.recipes.IRecipeAdded;
import com.tmtravlr.additions.gui.view.GuiView;

/**
 * Displays a visual of a recipe in the recipe view card
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since March 2019
 */
public interface IGuiRecipeCardDisplay {

	public int getWidth();
	
	public int getHeight();
	
	public void renderDisplay(GuiView viewScreen, int x, int y, int mouseX, int mouseY);
	
}
