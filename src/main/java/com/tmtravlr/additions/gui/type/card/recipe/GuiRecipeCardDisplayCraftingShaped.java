package com.tmtravlr.additions.gui.type.card.recipe;

import java.util.List;

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
			NonNullList<IngredientOreNBT> displayIngredients = NonNullList.withSize(9, IngredientOreNBT.EMPTY);
			
			int ingredientsIndex = 0;
			for (int x = 2; x >= 0; x--) {
				for (int y = 2; y >= 0; y--) {
					int index = y * 3 + x;
					
					if (x < this.recipe.recipe.getRecipeWidth() && y < this.recipe.recipe.getRecipeHeight() && ingredientsIndex < this.recipe.recipe.getIngredients().size()) {
						Ingredient ingredient = this.recipe.recipe.getIngredients().get(ingredientsIndex);
						displayIngredients.set(index, ingredient instanceof IngredientOreNBT ? (IngredientOreNBT) ingredient : IngredientOreNBT.EMPTY);
						ingredientsIndex++;
					}
				}
			}
			
			this.createDisplayStacks(displayIngredients);
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
