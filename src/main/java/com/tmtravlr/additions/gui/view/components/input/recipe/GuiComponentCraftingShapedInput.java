package com.tmtravlr.additions.gui.view.components.input.recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.message.edit.GuiMessageBoxEditItemStack;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.helpers.GuiDropdownMenu;
import com.tmtravlr.additions.gui.view.components.helpers.GuiDropdownMenu.MenuOption;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditIngredientOreNBT;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Lets you edit a shaped crafting recipe
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date April 2019
 */
public class GuiComponentCraftingShapedInput extends GuiComponentCraftingRecipeInput {
	
	public GuiComponentCraftingShapedInput(String label, GuiEdit editScreen) {
		super(label, editScreen);
	}
	
	public void setRecipe(ShapedOreRecipe recipe) {
		this.setDefaultRecipe(recipe);
		this.editScreen.notifyHasChanges();
	}
	
	public void setDefaultRecipe(ShapedOreRecipe recipe) {
		if (recipe == null) {
			this.ingredients = NonNullList.withSize(MAX_INGREDIENTS, IngredientOreNBT.EMPTY);
			this.recreateDisplayStacks();
			this.output = ItemStack.EMPTY;
		} else {
			this.setDefaultShapedIngredients(recipe.getIngredients(), recipe.getRecipeWidth(), recipe.getRecipeHeight());
			this.recreateDisplayStacks();
			this.output = recipe.getRecipeOutput();
		}
	}
	
	public ShapedOreRecipe getRecipe() {
		ShapedPrimer primer = new ShapedPrimer();
		primer.input = NonNullList.create();
		primer.height = 0;
		primer.width = 0;
		
		int minX = -1;
		int maxX = -1;
		int minY = -1;
		int maxY = -1;
		
		for (int i = 0; i < MAX_INGREDIENTS; i++) {
			IngredientOreNBT ingredient = this.ingredients.get(i);
			
			if (!ingredient.isEmpty()) {
				int x = i % 3;
				int y = i / 3;
				
				if (minX < 0 || minX > x) {
					minX = x;
				}
				
				if (minY < 0 || minY > y) {
					minY = y;
				}
				
				if (maxX < 0 || maxX < x) {
					maxX = x;
				}
				
				if (maxY < 0 || maxY < y) {
					maxY = y;
				}
			}
		}
		
		if (minX >= 0 && maxX >= 0 && minY >= 0 && maxY >= 0) {
			primer.width = maxX - minX + 1;
			primer.height = maxY - minY + 1;
			
			for (int y = minY; y < maxY + 1; y++) {
				for (int x = minX; x < maxX + 1; x++) {
					int index = y * 3 + x;
					IngredientOreNBT ingredient = this.ingredients.get(index);
					
					primer.input.add(ingredient.isEmpty() ? Ingredient.EMPTY : ingredient);
				}
			}
		}
		
		return new ShapedOreRecipe(null, this.output, primer);
	}
	
	protected void setDefaultShapedIngredients(NonNullList<Ingredient> ingredients, int width, int height) {
		this.ingredients = NonNullList.withSize(MAX_INGREDIENTS, IngredientOreNBT.EMPTY);
		
		int ingredientsIndex = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				int index = y * 3 + x;
				
				if (x < width && y < height && ingredientsIndex < ingredients.size()) {
					Ingredient ingredient = ingredients.get(ingredientsIndex);
					this.ingredients.set(index, ingredient instanceof IngredientOreNBT ? (IngredientOreNBT) ingredient : IngredientOreNBT.EMPTY);
					ingredientsIndex++;
				}
			}
		}
	}
}
