package com.tmtravlr.additions.gui.type.card.recipe;

import java.util.List;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingDyeItem;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingPotionTipping;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class GuiRecipeCardDisplayCraftingPotionTipping extends GuiRecipeCardDisplayCrafting {
	
	private RecipeAddedCraftingPotionTipping recipe;
	
	public GuiRecipeCardDisplayCraftingPotionTipping(RecipeAddedCraftingPotionTipping recipe) {
		this.recipe = recipe;
		
		if (this.recipe.untippedProjectile != null && this.recipe.tippedProjectile != null) {
			IngredientOreNBT ingredientToTip = new IngredientOreNBT();
			IngredientOreNBT potionList = new IngredientOreNBT();
			ingredientToTip.setStackList(NonNullList.withSize(1, new ItemStack(this.recipe.untippedProjectile)));
			NonNullList<ItemStack> potionItems = NonNullList.create();
			Items.LINGERING_POTION.getSubItems(CreativeTabs.SEARCH, potionItems);
			potionList.setStackList(potionItems);
			
			NonNullList<IngredientOreNBT> displayIngredients = NonNullList.withSize(9, ingredientToTip);
			displayIngredients.set(4, potionList);
			
			this.createDisplayStacks(displayIngredients);
		}
	}

	@Override
	protected ItemStack getOutput(List<ItemStack> inputs) {
		if (this.recipe.untippedProjectile != null && this.recipe.tippedProjectile != null) {
			return this.recipe.getCraftingResult(this.mockInventoryCrafting(inputs));
		} else {
			return ItemStack.EMPTY;
		}
	}
	
	private InventoryCrafting mockInventoryCrafting(List<ItemStack> inputs) {
		return new InventoryCrafting(null, 3, 3) {
			@Override
			public ItemStack getStackInRowAndColumn(int x, int y) {
				int index = y * 3 + x;
				return inputs.get(index);
			}
		};
	}

}
