package com.tmtravlr.additions.gui.type.card.recipe;

import java.util.Arrays;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingModifyDamage;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class GuiRecipeCardDisplayCraftingModifyDamage extends GuiRecipeCardDisplayCrafting {
	
	private RecipeAddedCraftingModifyDamage recipe;
	
	public GuiRecipeCardDisplayCraftingModifyDamage(RecipeAddedCraftingModifyDamage recipe) {
		this.recipe = recipe;
		
		if (this.recipe.itemToModify != null && !this.recipe.modifyAmounts.isEmpty()) {
			IngredientOreNBT ingredientToModify = new IngredientOreNBT();
			IngredientOreNBT modifierList = new IngredientOreNBT();
			ingredientToModify.setStackList(NonNullList.withSize(1, new ItemStack(this.recipe.itemToModify)));
			NonNullList<ItemStack> modifierStacks = NonNullList.create();
			for (IngredientOreNBT ingredient : this.recipe.modifyAmounts.keySet()) {
				modifierStacks.addAll(Arrays.asList(ingredient.getMatchingStacks()));
			}
			modifierList.setStackList(modifierStacks);
			
			this.displayIngredients.set(0, ingredientToModify);
			this.displayIngredients.set(1, modifierList);
		}
	}

	@Override
	protected ItemStack getOutput(NonNullList<ItemStack> inputs) {
		if (this.recipe.output.isEmpty()) {
			if (this.recipe.itemToModify != null && !this.recipe.modifyAmounts.isEmpty()) {
				return this.recipe.getCraftingResult(this.mockInventoryCrafting(inputs));
			} else {
				return ItemStack.EMPTY;
			}
		} else {
			return this.recipe.output;
		}
	}
	
	private InventoryCrafting mockInventoryCrafting(NonNullList<ItemStack> inputs) {
		return new InventoryCrafting(null, 0, 0) {
			@Override
			public int getSizeInventory() {
				return inputs.size();
			}
			
			@Override
			public ItemStack getStackInSlot(int index) {
		        return index >= this.getSizeInventory() ? ItemStack.EMPTY : inputs.get(index);
		    }
		};
	}

}
