package com.tmtravlr.additions.gui.type.card.recipe;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingDyeItem;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class GuiRecipeCardDisplayCraftingDyeItem extends GuiRecipeCardDisplayCrafting {
	
	private RecipeAddedCraftingDyeItem recipe;
	
	public GuiRecipeCardDisplayCraftingDyeItem(RecipeAddedCraftingDyeItem recipe) {
		this.recipe = recipe;
		
		if (this.recipe.itemToDye != null) {
			IngredientOreNBT ingredientToDye = new IngredientOreNBT();
			IngredientOreNBT dyeList = new IngredientOreNBT();
			ingredientToDye.setStackList(NonNullList.withSize(1, new ItemStack(this.recipe.itemToDye)));
			dyeList.setOreName("dye");
			
			this.displayIngredients.set(0, ingredientToDye);
			this.displayIngredients.set(1, dyeList);
		}
	}

	@Override
	protected ItemStack getOutput(NonNullList<ItemStack> inputs) {
		if (this.recipe.itemToDye != null) {
			return this.recipe.getCraftingResult(this.mockInventoryCrafting(inputs));
		} else {
			return ItemStack.EMPTY;
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
