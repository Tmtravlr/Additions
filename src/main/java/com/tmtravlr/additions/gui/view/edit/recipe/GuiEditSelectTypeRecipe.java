package com.tmtravlr.additions.gui.view.edit.recipe;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.RecipeAddedManager;
import com.tmtravlr.additions.api.gui.IGuiRecipeAddedFactory;
import com.tmtravlr.additions.gui.view.edit.GuiEditSelectType;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

/**
 * Page for selecting a recipe type to add.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since March 2019
 */
public class GuiEditSelectTypeRecipe extends GuiEditSelectType<IGuiRecipeAddedFactory> {
	
	public GuiEditSelectTypeRecipe(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon);
	}

	@Override
	public Collection<IGuiRecipeAddedFactory> getTypes() {
		return RecipeAddedManager.getAllGuiFactories().values();
	}

	@Override
	public String getDescription(IGuiRecipeAddedFactory editFactory) {
		return editFactory.getDescription();
	}
	
	@Override
	public String getTitle(IGuiRecipeAddedFactory editFactory) {
		return editFactory.getTitle();
	}

	@Override
	public void createObject() {
		this.mc.displayGuiScreen(this.getSelectedType().getEditScreen(this.parentScreen, this.addon, null));
	}

}
