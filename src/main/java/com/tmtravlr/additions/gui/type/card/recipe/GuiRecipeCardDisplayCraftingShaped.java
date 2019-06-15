package com.tmtravlr.additions.gui.type.card.recipe;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingShaped;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class GuiRecipeCardDisplayCraftingShaped extends GuiRecipeCardDisplayCrafting {
	
	private RecipeAddedCraftingShaped recipe;
	
	public GuiRecipeCardDisplayCraftingShaped(RecipeAddedCraftingShaped recipe) {
		this.recipe = recipe;
		if (this.recipe.recipe != null) {
			
			int ingredientsIndex = 0;
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < 3; x++) {
					int index = y * 3 + x;
					
					if (x < this.recipe.recipe.getRecipeWidth() && y < this.recipe.recipe.getRecipeHeight() && ingredientsIndex < this.recipe.recipe.getIngredients().size()) {
						Ingredient ingredient = this.recipe.recipe.getIngredients().get(ingredientsIndex);
						this.displayIngredients.set(index, ingredient instanceof IngredientOreNBT ? (IngredientOreNBT) ingredient : IngredientOreNBT.EMPTY);
						ingredientsIndex++;
					}
				}
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
