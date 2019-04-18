package com.tmtravlr.additions.gui.type.card.recipe;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingDyeItem;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingModifyDamage;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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
			
			NonNullList<IngredientOreNBT> displayIngredients = NonNullList.withSize(9, IngredientOreNBT.EMPTY);
			displayIngredients.set(0, ingredientToModify);
			displayIngredients.set(1, modifierList);
			
			this.createDisplayStacks(displayIngredients);
		}
	}

	@Override
	protected ItemStack getOutput(List<ItemStack> inputs) {
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
	
	private InventoryCrafting mockInventoryCrafting(List<ItemStack> inputs) {
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
