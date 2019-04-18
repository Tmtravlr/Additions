package com.tmtravlr.additions.gui.type.card.recipe;

import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingShapeless;

import net.minecraft.item.ItemStack;

public class GuiRecipeCardDisplayCraftingShapeless extends GuiRecipeCardDisplayCrafting {
	
	private RecipeAddedCraftingShapeless recipe;
	
	public GuiRecipeCardDisplayCraftingShapeless(RecipeAddedCraftingShapeless recipe) {
		this.recipe = recipe;
		if (this.recipe.recipe != null) {
			this.createDisplayStacks(this.recipe.recipe.getIngredients().stream().map(ingredient -> ingredient instanceof IngredientOreNBT ? (IngredientOreNBT) ingredient : new IngredientOreNBT()).collect(Collectors.toList()));
		}
	}

	@Override
	protected ItemStack getOutput(List<ItemStack> inputs) {
		if (this.recipe.recipe != null) {
			return this.recipe.recipe.getRecipeOutput();
		} else {
			return ItemStack.EMPTY;
		}
	}

}
