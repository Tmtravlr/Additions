package com.tmtravlr.additions.util;

import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedBrewingComplete;
import com.tmtravlr.additions.util.client.ItemStackDisplay;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

/**
 * Utility for generating potions to render for a complete brewing recipe (for the GUI and for JEI).
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since May 2021
 */
public class BrewingCompleteDisplayRecipes {
	
	public static List<DisplayRecipe> createDisplayRecipes(RecipeAddedBrewingComplete recipe) {
		List<DisplayRecipe> displayRecipes = new ArrayList<>();
		
		displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithTag(Items.POTIONITEM, recipe.inputTag), stackWithTag(Items.POTIONITEM, recipe.outputTag)));
		
		if (recipe.allowMundane) {
			displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithPotion(Items.POTIONITEM, PotionTypes.WATER), stackWithPotion(Items.POTIONITEM, PotionTypes.MUNDANE)));
		}
		
		if (recipe.outputExtendedTag != null && !recipe.outputExtendedTag.hasNoTags()) {
			displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE, stackWithTag(Items.POTIONITEM, recipe.outputTag), stackWithTag(Items.POTIONITEM, recipe.outputExtendedTag)));
			
			if (recipe.outputPoweredTag != null && !recipe.outputPoweredTag.hasNoTags()) {
				displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE, stackWithTag(Items.POTIONITEM, recipe.outputPoweredTag), stackWithTag(Items.POTIONITEM, recipe.outputExtendedTag)));
			}
			
			if (recipe.inputExtendedTag != null && !recipe.inputExtendedTag.hasNoTags()) {
				displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithTag(Items.POTIONITEM, recipe.inputExtendedTag), stackWithTag(Items.POTIONITEM, recipe.outputExtendedTag)));
			}
		}
		
		if (recipe.outputPoweredTag != null && !recipe.outputPoweredTag.hasNoTags()) {
			displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE, stackWithTag(Items.POTIONITEM, recipe.outputTag), stackWithTag(Items.POTIONITEM, recipe.outputPoweredTag)));
			
			if (recipe.outputExtendedTag != null && !recipe.outputExtendedTag.hasNoTags()) {
				displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE, stackWithTag(Items.POTIONITEM, recipe.outputExtendedTag), stackWithTag(Items.POTIONITEM, recipe.outputPoweredTag)));
			}
			
			if (recipe.inputPoweredTag != null && !recipe.inputPoweredTag.hasNoTags()) {
				displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithTag(Items.POTIONITEM, recipe.inputPoweredTag), stackWithTag(Items.POTIONITEM, recipe.outputPoweredTag)));
			}
		}
		
		if (recipe.allowSplash) {
			displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithTag(Items.SPLASH_POTION, recipe.inputTag), stackWithTag(Items.SPLASH_POTION, recipe.outputTag)));
			displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_GUNPOWDER, stackWithTag(Items.POTIONITEM, recipe.outputTag), stackWithTag(Items.SPLASH_POTION, recipe.outputTag)));
			
			if (recipe.allowMundane) {
				displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithPotion(Items.SPLASH_POTION, PotionTypes.WATER), stackWithPotion(Items.SPLASH_POTION, PotionTypes.MUNDANE)));
			}
			
			if (recipe.outputExtendedTag != null && !recipe.outputExtendedTag.hasNoTags()) {
				displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE, stackWithTag(Items.SPLASH_POTION, recipe.outputTag), stackWithTag(Items.SPLASH_POTION, recipe.outputExtendedTag)));
				displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_GUNPOWDER, stackWithTag(Items.POTIONITEM, recipe.outputExtendedTag), stackWithTag(Items.SPLASH_POTION, recipe.outputExtendedTag)));
				
				if (recipe.outputPoweredTag != null && !recipe.outputPoweredTag.hasNoTags()) {
					displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE, stackWithTag(Items.SPLASH_POTION, recipe.outputPoweredTag), stackWithTag(Items.SPLASH_POTION, recipe.outputExtendedTag)));
				}
				
				if (recipe.inputExtendedTag != null && !recipe.inputExtendedTag.hasNoTags()) {
					displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithTag(Items.SPLASH_POTION, recipe.inputExtendedTag), stackWithTag(Items.SPLASH_POTION, recipe.outputExtendedTag)));
				}
			}
			
			if (recipe.outputPoweredTag != null && !recipe.outputPoweredTag.hasNoTags()) {
				displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE, stackWithTag(Items.SPLASH_POTION, recipe.outputTag), stackWithTag(Items.SPLASH_POTION, recipe.outputPoweredTag)));
				displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_GUNPOWDER, stackWithTag(Items.POTIONITEM, recipe.outputPoweredTag), stackWithTag(Items.SPLASH_POTION, recipe.outputPoweredTag)));
				
				if (recipe.outputExtendedTag != null && !recipe.outputExtendedTag.hasNoTags()) {
					displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE, stackWithTag(Items.SPLASH_POTION, recipe.outputExtendedTag), stackWithTag(Items.SPLASH_POTION, recipe.outputPoweredTag)));
				}
				
				if (recipe.inputPoweredTag != null && !recipe.inputPoweredTag.hasNoTags()) {
					displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithTag(Items.SPLASH_POTION, recipe.inputPoweredTag), stackWithTag(Items.SPLASH_POTION, recipe.outputPoweredTag)));
				}
			}
		}
		
		if (recipe.allowLingering) {
			displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithTag(Items.LINGERING_POTION, recipe.inputTag), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputTag))));
			displayRecipes.add(new DisplayRecipe(new IngredientOreNBT(new ItemStack(Items.DRAGON_BREATH)), stackWithTag(Items.SPLASH_POTION, recipe.outputTag), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputTag))));
			
			if (recipe.allowMundane) {
				displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithPotion(Items.LINGERING_POTION, PotionTypes.WATER), stackWithPotion(Items.LINGERING_POTION, PotionTypes.MUNDANE)));
			}
			
			if (recipe.outputExtendedTag != null && !recipe.outputExtendedTag.hasNoTags()) {
				displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE, stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputTag)), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputExtendedTag))));
				displayRecipes.add(new DisplayRecipe(new IngredientOreNBT(new ItemStack(Items.DRAGON_BREATH)), stackWithTag(Items.SPLASH_POTION, recipe.outputExtendedTag), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputExtendedTag))));
				
				if (recipe.outputPoweredTag != null && !recipe.outputPoweredTag.hasNoTags()) {
					displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_REDSTONE, stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputPoweredTag)), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputExtendedTag))));
				}
				
				if (recipe.inputExtendedTag != null && !recipe.inputExtendedTag.hasNoTags()) {
					displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.inputExtendedTag)), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputExtendedTag))));
				}
			}
			
			if (recipe.outputPoweredTag != null && !recipe.outputPoweredTag.hasNoTags()) {
				displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE, stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputTag)), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputPoweredTag))));
				displayRecipes.add(new DisplayRecipe(new IngredientOreNBT(new ItemStack(Items.DRAGON_BREATH)), stackWithTag(Items.SPLASH_POTION, recipe.outputPoweredTag), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputPoweredTag))));
				
				if (recipe.outputExtendedTag != null && !recipe.outputExtendedTag.hasNoTags()) {
					displayRecipes.add(new DisplayRecipe(RecipeAddedBrewingComplete.INGREDIENT_GLOWSTONE, stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputExtendedTag)), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputPoweredTag))));
				}
				
				if (recipe.inputPoweredTag != null && !recipe.inputPoweredTag.hasNoTags()) {
					displayRecipes.add(new DisplayRecipe(recipe.ingredient, stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.inputPoweredTag)), stackWithTag(Items.LINGERING_POTION, recipe.getLingeringTag(recipe.outputPoweredTag))));
				}
			}
		}
		
		return displayRecipes;
	}
	
	private static ItemStack stackWithPotion(Item item, PotionType potionType) {
		return PotionUtils.addPotionToItemStack(new ItemStack(item), potionType);
	}
	
	private static ItemStack stackWithTag(Item item, NBTTagCompound tag) {
		ItemStack stack = new ItemStack(item);
		stack.setTagCompound(tag);
		return stack;
	}
	
	public static class DisplayRecipe {
		public IngredientOreNBT ingredient;
		public ItemStack input;
		public ItemStack output;
		
		public DisplayRecipe(IngredientOreNBT ingredient, ItemStack input, ItemStack output) {
			this.ingredient = ingredient;
			this.input = input;
			this.output = output;
		}
	}

}
