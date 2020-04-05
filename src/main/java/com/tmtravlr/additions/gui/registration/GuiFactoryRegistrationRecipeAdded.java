package com.tmtravlr.additions.gui.registration;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.recipes.RecipeAddedBrewing;
import com.tmtravlr.additions.addon.recipes.RecipeAddedBrewingComplete;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingDyeItem;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingModifyDamage;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingModifyNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingPotionTipping;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingShaped;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingShapeless;
import com.tmtravlr.additions.addon.recipes.RecipeAddedManager;
import com.tmtravlr.additions.addon.recipes.RecipeAddedSmelting;
import com.tmtravlr.additions.api.gui.IGuiRecipeAddedFactory;
import com.tmtravlr.additions.api.gui.IGuiRecipeCardDisplay;
import com.tmtravlr.additions.gui.type.card.recipe.GuiRecipeCardDisplayBrewing;
import com.tmtravlr.additions.gui.type.card.recipe.GuiRecipeCardDisplayBrewingComplete;
import com.tmtravlr.additions.gui.type.card.recipe.GuiRecipeCardDisplayCraftingDyeItem;
import com.tmtravlr.additions.gui.type.card.recipe.GuiRecipeCardDisplayCraftingModifyDamage;
import com.tmtravlr.additions.gui.type.card.recipe.GuiRecipeCardDisplayCraftingModifyNBT;
import com.tmtravlr.additions.gui.type.card.recipe.GuiRecipeCardDisplayCraftingPotionTipping;
import com.tmtravlr.additions.gui.type.card.recipe.GuiRecipeCardDisplayCraftingShaped;
import com.tmtravlr.additions.gui.type.card.recipe.GuiRecipeCardDisplayCraftingShapeless;
import com.tmtravlr.additions.gui.type.card.recipe.GuiRecipeCardDisplaySmelting;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeBrewing;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeBrewingComplete;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingDyeItem;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingModifyDamage;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingModifyNBT;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingPotionTipping;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingShaped;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingShapeless;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeSmelting;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiFactoryRegistrationRecipeAdded {

	public static void registerGuiFactories() {
		RecipeAddedManager.registerGuiFactory(RecipeAddedCraftingShaped.TYPE, new IGuiRecipeAddedFactory<RecipeAddedCraftingShaped>() {
			@Override
			public String getTitle() {
				return I18n.format("type.recipe.crafting.shaped.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.recipe.crafting.shaped.description");
			}
			
			@Override
			public IGuiRecipeCardDisplay getRecipeCardDisplay(RecipeAddedCraftingShaped recipe) {
				return new GuiRecipeCardDisplayCraftingShaped(recipe);
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingShaped recipe) {
				return new GuiEditRecipeCraftingShaped(parent, recipe == null ? I18n.format("gui.edit.recipe.crafting.shaped.title") : I18n.format("gui.edit.editing", recipe.getId()), addon, recipe);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingShaped recipe) {
				GuiEditRecipeCraftingShaped editScreen = new GuiEditRecipeCraftingShaped(parent, I18n.format("gui.edit.recipe.crafting.shaped.title"), addon, null);
				editScreen.copyFrom(recipe);
				return editScreen;
			}
		});
		
		RecipeAddedManager.registerGuiFactory(RecipeAddedCraftingShapeless.TYPE, new IGuiRecipeAddedFactory<RecipeAddedCraftingShapeless>() {
			@Override
			public String getTitle() {
				return I18n.format("type.recipe.crafting.shapeless.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.recipe.crafting.shapeless.description");
			}
			
			@Override
			public IGuiRecipeCardDisplay getRecipeCardDisplay(RecipeAddedCraftingShapeless recipe) {
				return new GuiRecipeCardDisplayCraftingShapeless(recipe);
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingShapeless recipe) {
				return new GuiEditRecipeCraftingShapeless(parent, recipe == null ? I18n.format("gui.edit.recipe.crafting.shapeless.title") : I18n.format("gui.edit.editing", recipe.getId()), addon, recipe);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingShapeless recipe) {
				GuiEditRecipeCraftingShapeless editScreen = new GuiEditRecipeCraftingShapeless(parent, I18n.format("gui.edit.recipe.crafting.shapeless.title"), addon, null);
				editScreen.copyFrom(recipe);
				return editScreen;
			}
		});
		
		RecipeAddedManager.registerGuiFactory(RecipeAddedCraftingPotionTipping.TYPE, new IGuiRecipeAddedFactory<RecipeAddedCraftingPotionTipping>() {
			@Override
			public String getTitle() {
				return I18n.format("type.recipe.crafting.tipProjectile.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.recipe.crafting.tipProjectile.description");
			}
			
			@Override
			public IGuiRecipeCardDisplay getRecipeCardDisplay(RecipeAddedCraftingPotionTipping recipe) {
				return new GuiRecipeCardDisplayCraftingPotionTipping(recipe);
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingPotionTipping recipe) {
				return new GuiEditRecipeCraftingPotionTipping(parent, recipe == null ? I18n.format("gui.edit.recipe.crafting.tipProjectile.title") : I18n.format("gui.edit.editing", recipe.getId()), addon, recipe);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingPotionTipping recipe) {
				GuiEditRecipeCraftingPotionTipping editScreen = new GuiEditRecipeCraftingPotionTipping(parent, I18n.format("gui.edit.recipe.crafting.tipProjectile.title"), addon, null);
				editScreen.copyFrom(recipe);
				return editScreen;
			}
		});
			
		RecipeAddedManager.registerGuiFactory(RecipeAddedCraftingDyeItem.TYPE, new IGuiRecipeAddedFactory<RecipeAddedCraftingDyeItem>() {
			@Override
			public String getTitle() {
				return I18n.format("type.recipe.crafting.dyeItem.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.recipe.crafting.dyeItem.description");
			}
			
			@Override
			public IGuiRecipeCardDisplay getRecipeCardDisplay(RecipeAddedCraftingDyeItem recipe) {
				return new GuiRecipeCardDisplayCraftingDyeItem(recipe);
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingDyeItem recipe) {
				return new GuiEditRecipeCraftingDyeItem(parent, recipe == null ? I18n.format("gui.edit.recipe.crafting.dyeItem.title") : I18n.format("gui.edit.editing", recipe.getId()), addon, recipe);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingDyeItem recipe) {
				GuiEditRecipeCraftingDyeItem editScreen = new GuiEditRecipeCraftingDyeItem(parent, I18n.format("gui.edit.recipe.crafting.dyeItem.title"), addon, null);
				editScreen.copyFrom(recipe);
				return editScreen;
			}
		});
		
		RecipeAddedManager.registerGuiFactory(RecipeAddedCraftingModifyDamage.TYPE, new IGuiRecipeAddedFactory<RecipeAddedCraftingModifyDamage>() {
			@Override
			public String getTitle() {
				return I18n.format("type.recipe.crafting.modifyDamage.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.recipe.crafting.modifyDamage.description");
			}
			
			@Override
			public IGuiRecipeCardDisplay getRecipeCardDisplay(RecipeAddedCraftingModifyDamage recipe) {
				return new GuiRecipeCardDisplayCraftingModifyDamage(recipe);
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingModifyDamage recipe) {
				return new GuiEditRecipeCraftingModifyDamage(parent, recipe == null ? I18n.format("gui.edit.recipe.crafting.modifyDamage.title") : I18n.format("gui.edit.editing", recipe.getId()), addon, recipe);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingModifyDamage recipe) {
				GuiEditRecipeCraftingModifyDamage editScreen = new GuiEditRecipeCraftingModifyDamage(parent, I18n.format("gui.edit.recipe.crafting.modifyDamage.title"), addon, null);
				editScreen.copyFrom(recipe);
				return editScreen;
			}
		});
		
		RecipeAddedManager.registerGuiFactory(RecipeAddedCraftingModifyNBT.TYPE, new IGuiRecipeAddedFactory<RecipeAddedCraftingModifyNBT>() {
			@Override
			public String getTitle() {
				return I18n.format("type.recipe.crafting.modifyNbt.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.recipe.crafting.modifyNbt.description");
			}
			
			@Override
			public IGuiRecipeCardDisplay getRecipeCardDisplay(RecipeAddedCraftingModifyNBT recipe) {
				return new GuiRecipeCardDisplayCraftingModifyNBT(recipe);
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingModifyNBT recipe) {
				return new GuiEditRecipeCraftingModifyNBT(parent, recipe == null ? I18n.format("gui.edit.recipe.crafting.modifyNbt.title") : I18n.format("gui.edit.editing", recipe.getId()), addon, recipe);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, RecipeAddedCraftingModifyNBT recipe) {
				GuiEditRecipeCraftingModifyNBT editScreen = new GuiEditRecipeCraftingModifyNBT(parent, I18n.format("gui.edit.recipe.crafting.modifyNbt.title"), addon, null);
				editScreen.copyFrom(recipe);
				return editScreen;
			}
		});
		
		RecipeAddedManager.registerGuiFactory(RecipeAddedSmelting.TYPE, new IGuiRecipeAddedFactory<RecipeAddedSmelting>() {
			@Override
			public String getTitle() {
				return I18n.format("type.recipe.smelting.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.recipe.smelting.description");
			}
			
			@Override
			public IGuiRecipeCardDisplay getRecipeCardDisplay(RecipeAddedSmelting recipe) {
				return new GuiRecipeCardDisplaySmelting(recipe);
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, RecipeAddedSmelting recipe) {
				return new GuiEditRecipeSmelting(parent, recipe == null ? I18n.format("gui.edit.recipe.smelting.title") : I18n.format("gui.edit.editing", recipe.getId()), addon, recipe);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, RecipeAddedSmelting recipe) {
				GuiEditRecipeSmelting editScreen = new GuiEditRecipeSmelting(parent, I18n.format("gui.edit.recipe.smelting.title"), addon, null);
				editScreen.copyFrom(recipe);
				return editScreen;
			}
		});
		
		RecipeAddedManager.registerGuiFactory(RecipeAddedBrewing.TYPE, new IGuiRecipeAddedFactory<RecipeAddedBrewing>() {
			@Override
			public String getTitle() {
				return I18n.format("type.recipe.brewing.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.recipe.brewing.description");
			}
			
			@Override
			public IGuiRecipeCardDisplay getRecipeCardDisplay(RecipeAddedBrewing recipe) {
				return new GuiRecipeCardDisplayBrewing(recipe);
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, RecipeAddedBrewing recipe) {
				return new GuiEditRecipeBrewing(parent, recipe == null ? I18n.format("gui.edit.recipe.brewing.title") : I18n.format("gui.edit.editing", recipe.getId()), addon, recipe);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, RecipeAddedBrewing recipe) {
				GuiEditRecipeBrewing editScreen = new GuiEditRecipeBrewing(parent, I18n.format("gui.edit.recipe.brewing.title"), addon, null);
				editScreen.copyFrom(recipe);
				return editScreen;
			}
		});
		
		RecipeAddedManager.registerGuiFactory(RecipeAddedBrewingComplete.TYPE, new IGuiRecipeAddedFactory<RecipeAddedBrewingComplete>() {
			@Override
			public String getTitle() {
				return I18n.format("type.recipe.brewing.complete.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.recipe.brewing.complete.description");
			}
			
			@Override
			public IGuiRecipeCardDisplay getRecipeCardDisplay(RecipeAddedBrewingComplete recipe) {
				return new GuiRecipeCardDisplayBrewingComplete(recipe);
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, RecipeAddedBrewingComplete recipe) {
				return new GuiEditRecipeBrewingComplete(parent, recipe == null ? I18n.format("gui.edit.recipe.brewing.complete.title") : I18n.format("gui.edit.editing", recipe.getId()), addon, recipe);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, RecipeAddedBrewingComplete recipe) {
				GuiEditRecipeBrewingComplete editScreen = new GuiEditRecipeBrewingComplete(parent, I18n.format("gui.edit.recipe.brewing.complete.title"), addon, null);
				editScreen.copyFrom(recipe);
				return editScreen;
			}
		});
	}
	
}
