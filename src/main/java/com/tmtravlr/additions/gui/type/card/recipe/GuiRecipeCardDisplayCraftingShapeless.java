package com.tmtravlr.additions.gui.type.card.recipe;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingShapeless;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class GuiRecipeCardDisplayCraftingShapeless extends GuiRecipeCardDisplayCrafting {
	
	private RecipeAddedCraftingShapeless recipe;
	
	public GuiRecipeCardDisplayCraftingShapeless(RecipeAddedCraftingShapeless recipe) {
		this.recipe = recipe;
		if (this.recipe.recipe != null) {
			for (int i = 0; i < MAX_INGREDIENTS && i < this.recipe.recipe.getIngredients().size(); i++) {
				Ingredient ingredient = this.recipe.recipe.getIngredients().get(i);
				this.displayIngredients.set(i, ingredient instanceof IngredientOreNBT ? (IngredientOreNBT) ingredient : new IngredientOreNBT());
			}
		}
	}

	@Override
	protected ItemStack getOutput(NonNullList<ItemStack> inputs) {
		if (this.recipe.recipe != null) {
			return this.recipe.recipe.getRecipeOutput();
		} else {
			return ItemStack.EMPTY;
		}
	}

}
