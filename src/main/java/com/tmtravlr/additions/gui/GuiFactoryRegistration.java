package com.tmtravlr.additions.gui;

import java.util.Collections;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedManager;
import com.tmtravlr.additions.addon.blocks.BlockAddedSimple;
import com.tmtravlr.additions.addon.effects.EffectManager;
import com.tmtravlr.additions.addon.effects.EffectPotion;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemInHand;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseManager;
import com.tmtravlr.additions.addon.items.ItemAddedArmor;
import com.tmtravlr.additions.addon.items.ItemAddedArrow;
import com.tmtravlr.additions.addon.items.ItemAddedAxe;
import com.tmtravlr.additions.addon.items.ItemAddedBow;
import com.tmtravlr.additions.addon.items.ItemAddedClub;
import com.tmtravlr.additions.addon.items.ItemAddedFirestarter;
import com.tmtravlr.additions.addon.items.ItemAddedFood;
import com.tmtravlr.additions.addon.items.ItemAddedGun;
import com.tmtravlr.additions.addon.items.ItemAddedHat;
import com.tmtravlr.additions.addon.items.ItemAddedHoe;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.addon.items.ItemAddedMultiTool;
import com.tmtravlr.additions.addon.items.ItemAddedPickaxe;
import com.tmtravlr.additions.addon.items.ItemAddedProjectile;
import com.tmtravlr.additions.addon.items.ItemAddedRecord;
import com.tmtravlr.additions.addon.items.ItemAddedShears;
import com.tmtravlr.additions.addon.items.ItemAddedShield;
import com.tmtravlr.additions.addon.items.ItemAddedShovel;
import com.tmtravlr.additions.addon.items.ItemAddedSimple;
import com.tmtravlr.additions.addon.items.ItemAddedSword;
import com.tmtravlr.additions.addon.items.ItemAddedThrowable;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItemDrop;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItself;
import com.tmtravlr.additions.addon.loottables.LootTablePresetEmpty;
import com.tmtravlr.additions.addon.loottables.LootTablePresetManager;
import com.tmtravlr.additions.addon.loottables.LootTablePresetOtherLootTable;
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
import com.tmtravlr.additions.api.gui.IGuiBlockAddedFactory;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseFactory;
import com.tmtravlr.additions.api.gui.IGuiEffectEditHandler;
import com.tmtravlr.additions.api.gui.IGuiEffectFactory;
import com.tmtravlr.additions.api.gui.IGuiItemAddedFactory;
import com.tmtravlr.additions.api.gui.IGuiLootTablePresetFactory;
import com.tmtravlr.additions.api.gui.IGuiRecipeAddedFactory;
import com.tmtravlr.additions.api.gui.IGuiRecipeCardDisplay;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonAdvancement;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonBlock;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonCreativeTab;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonEffect;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonFunction;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonItem;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonItemMaterial;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonLootTable;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonRecipe;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonSoundEvent;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonStructure;
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
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockSimple;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemArmor;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemArrow;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemAxe;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemBow;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemClub;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemFirestarter;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemFood;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemGun;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemHat;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemHoe;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemMultiTool;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemPickaxe;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemProjectile;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemRecord;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemShears;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemShield;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemShovel;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemSimple;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemSword;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemThrowable;
import com.tmtravlr.additions.gui.view.edit.loottable.GuiEditLootTablePresetBlockItem;
import com.tmtravlr.additions.gui.view.edit.loottable.GuiEditLootTablePresetBlockItself;
import com.tmtravlr.additions.gui.view.edit.loottable.GuiEditLootTablePresetEmpty;
import com.tmtravlr.additions.gui.view.edit.loottable.GuiEditLootTablePresetOtherLootTable;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeBrewing;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeBrewingComplete;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingDyeItem;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingModifyDamage;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingModifyNBT;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingPotionTipping;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingShaped;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeCraftingShapeless;
import com.tmtravlr.additions.gui.view.edit.recipe.GuiEditRecipeSmelting;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectCauseItemInHand;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectPotion;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

public class GuiFactoryRegistration {

	public static void registerGuiFactories() {
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonBlock(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonItem(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonCreativeTab(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonRecipe(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonItemMaterial(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonEffect(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonAdvancement(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonFunction(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonLootTable(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonStructure(parent, addon));
		AdditionTypeManager.registerAdditionTypeGuiFactory((parent, addon) -> new GuiAdditionTypeButtonSoundEvent(parent, addon));
		
		ItemAddedManager.registerGuiFactory(ItemAddedSimple.TYPE, new IGuiItemAddedFactory<ItemAddedSimple>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.simple.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.simple.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedSimple item) {
				return new GuiEditItemSimple(parent, item == null ? I18n.format("gui.edit.item.simple.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedSimple item) {
				GuiEditItemSimple editScreen = new GuiEditItemSimple(parent, I18n.format("gui.edit.item.simple.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedFood.TYPE, new IGuiItemAddedFactory<ItemAddedFood>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.food.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.food.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedFood item) {
				return new GuiEditItemFood(parent, item == null ? I18n.format("gui.edit.item.food.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedFood item) {
				GuiEditItemFood editScreen = new GuiEditItemFood(parent, I18n.format("gui.edit.item.food.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedRecord.TYPE, new IGuiItemAddedFactory<ItemAddedRecord>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.record.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item..description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedRecord item) {
				return new GuiEditItemRecord(parent, item == null ? I18n.format("gui.edit.item.record.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedRecord item) {
				GuiEditItemRecord editScreen = new GuiEditItemRecord(parent, I18n.format("gui.edit.item.record.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedArmor.TYPE, new IGuiItemAddedFactory<ItemAddedArmor>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.armor.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.armor.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedArmor item) {
				return new GuiEditItemArmor(parent, item == null ? I18n.format("gui.edit.item.armor.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedArmor item) {
				GuiEditItemArmor editScreen = new GuiEditItemArmor(parent, I18n.format("gui.edit.item.armor.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedHat.TYPE, new IGuiItemAddedFactory<ItemAddedHat>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.hat.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.hat.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedHat item) {
				return new GuiEditItemHat(parent, item == null ? I18n.format("gui.edit.item.hat.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedHat item) {
				GuiEditItemHat editScreen = new GuiEditItemHat(parent, I18n.format("gui.edit.item.hat.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedSword.TYPE, new IGuiItemAddedFactory<ItemAddedSword>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.sword.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.sword.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedSword item) {
				return new GuiEditItemSword(parent, item == null ? I18n.format("gui.edit.item.sword.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedSword item) {
				GuiEditItemSword editScreen = new GuiEditItemSword(parent, I18n.format("gui.edit.item.sword.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedClub.TYPE, new IGuiItemAddedFactory<ItemAddedClub>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.club.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.club.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedClub item) {
				return new GuiEditItemClub(parent, item == null ? I18n.format("gui.edit.item.club.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedClub item) {
				GuiEditItemClub editScreen = new GuiEditItemClub(parent, I18n.format("gui.edit.item.club.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedShears.TYPE, new IGuiItemAddedFactory<ItemAddedShears>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.shears.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.shears.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedShears item) {
				return new GuiEditItemShears(parent, item == null ? I18n.format("gui.edit.item.shears.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedShears item) {
				GuiEditItemShears editScreen = new GuiEditItemShears(parent, I18n.format("gui.edit.item.shears.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedFirestarter.TYPE, new IGuiItemAddedFactory<ItemAddedFirestarter>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.firestarter.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.firestarter.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedFirestarter item) {
				return new GuiEditItemFirestarter(parent, item == null ? I18n.format("gui.edit.item.firestarter.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedFirestarter item) {
				GuiEditItemFirestarter editScreen = new GuiEditItemFirestarter(parent, I18n.format("gui.edit.item.firestarter.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedPickaxe.TYPE, new IGuiItemAddedFactory<ItemAddedPickaxe>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.pickaxe.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.pickaxe.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedPickaxe item) {
				return new GuiEditItemPickaxe(parent, item == null ? I18n.format("gui.edit.item.pickaxe.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedPickaxe item) {
				GuiEditItemPickaxe editScreen = new GuiEditItemPickaxe(parent, I18n.format("gui.edit.item.pickaxe.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedAxe.TYPE, new IGuiItemAddedFactory<ItemAddedAxe>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.axe.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.axe.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedAxe item) {
				return new GuiEditItemAxe(parent, item == null ? I18n.format("gui.edit.item.axe.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedAxe item) {
				GuiEditItemAxe editScreen = new GuiEditItemAxe(parent, I18n.format("gui.edit.item.axe.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedShovel.TYPE, new IGuiItemAddedFactory<ItemAddedShovel>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.shovel.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.shovel.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedShovel item) {
				return new GuiEditItemShovel(parent, item == null ? I18n.format("gui.edit.item.shovel.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedShovel item) {
				GuiEditItemShovel editScreen = new GuiEditItemShovel(parent, I18n.format("gui.edit.item.shovel.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedHoe.TYPE, new IGuiItemAddedFactory<ItemAddedHoe>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.hoe.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.hoe.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedHoe item) {
				return new GuiEditItemHoe(parent, item == null ? I18n.format("gui.edit.item.hoe.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedHoe item) {
				GuiEditItemHoe editScreen = new GuiEditItemHoe(parent, I18n.format("gui.edit.item.hoe.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedMultiTool.TYPE, new IGuiItemAddedFactory<ItemAddedMultiTool>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.multitool.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.multitool.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedMultiTool item) {
				return new GuiEditItemMultiTool(parent, item == null ? I18n.format("gui.edit.item.multitool.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedMultiTool item) {
				GuiEditItemMultiTool editScreen = new GuiEditItemMultiTool(parent, I18n.format("gui.edit.item.multitool.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedShield.TYPE, new IGuiItemAddedFactory<ItemAddedShield>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.shield.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.shield.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedShield item) {
				return new GuiEditItemShield(parent, item == null ? I18n.format("gui.edit.item.shield.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedShield item) {
				GuiEditItemShield editScreen = new GuiEditItemShield(parent, I18n.format("gui.edit.item.shield.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedBow.TYPE, new IGuiItemAddedFactory<ItemAddedBow>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.bow.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.bow.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedBow item) {
				return new GuiEditItemBow(parent, item == null ? I18n.format("gui.edit.item.bow.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedBow item) {
				GuiEditItemBow editScreen = new GuiEditItemBow(parent, I18n.format("gui.edit.item.bow.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedGun.TYPE, new IGuiItemAddedFactory<ItemAddedGun>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.gun.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.gun.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedGun item) {
				return new GuiEditItemGun(parent, item == null ? I18n.format("gui.edit.item.gun.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedGun item) {
				GuiEditItemGun editScreen = new GuiEditItemGun(parent, I18n.format("gui.edit.item.gun.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedThrowable.TYPE, new IGuiItemAddedFactory<ItemAddedThrowable>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.throwable.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.throwable.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedThrowable item) {
				return new GuiEditItemThrowable(parent, item == null ? I18n.format("gui.edit.item.throwable.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedThrowable item) {
				GuiEditItemThrowable editScreen = new GuiEditItemThrowable(parent, I18n.format("gui.edit.item.throwable.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedArrow.TYPE, new IGuiItemAddedFactory<ItemAddedArrow>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.arrow.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.arrow.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedArrow item) {
				return new GuiEditItemArrow(parent, item == null ? I18n.format("gui.edit.item.arrow.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedArrow item) {
				GuiEditItemArrow editScreen = new GuiEditItemArrow(parent, I18n.format("gui.edit.item.arrow.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		ItemAddedManager.registerGuiFactory(ItemAddedProjectile.TYPE, new IGuiItemAddedFactory<ItemAddedProjectile>() {
			@Override
			public String getTitle() {
				return I18n.format("type.item.projectile.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.item.projectile.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, ItemAddedProjectile item) {
				return new GuiEditItemProjectile(parent, item == null ? I18n.format("gui.edit.item.projectile.title") : I18n.format("gui.edit.editing", item.getDisplayName()), addon, item);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, ItemAddedProjectile item) {
				GuiEditItemProjectile editScreen = new GuiEditItemProjectile(parent, I18n.format("gui.edit.item.projectile.title"), addon, null);
				editScreen.copyFrom(item);
				return editScreen;
			}
		});
		
		BlockAddedManager.registerGuiFactory(BlockAddedSimple.TYPE, new IGuiBlockAddedFactory<BlockAddedSimple>() {
			@Override
			public String getTitle() {
				return I18n.format("type.block.simple.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.block.simple.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, BlockAddedSimple block) {
				return new GuiEditBlockSimple(parent, block == null ? I18n.format("gui.edit.block.simple.title") : I18n.format("gui.edit.editing", block.getDisplayName()), addon, block);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, BlockAddedSimple block) {
				GuiEditBlockSimple editScreen = new GuiEditBlockSimple(parent, I18n.format("gui.edit.block.simple.title"), addon, null);
				editScreen.copyFrom(block);
				return editScreen;
			}
		});
		
		LootTablePresetManager.registerGuiFactory(LootTablePresetEmpty.TYPE, new IGuiLootTablePresetFactory<LootTablePresetEmpty>() {
			@Override
			public String getTitle() {
				return I18n.format("type.lootTable.preset.empty.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.lootTable.preset.empty.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, LootTablePresetEmpty preset) {
				return new GuiEditLootTablePresetEmpty(parent, preset == null ? I18n.format("gui.edit.lootTable.preset.empty.title") : I18n.format("gui.edit.editing", preset.id), addon, preset);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, LootTablePresetEmpty preset) {
				GuiEditLootTablePresetEmpty editScreen = new GuiEditLootTablePresetEmpty(parent, I18n.format("gui.edit.lootTable.preset.empty.title"), addon, null);
				editScreen.copyFrom(preset);
				return editScreen;
			}
		});
		
		LootTablePresetManager.registerGuiFactory(LootTablePresetOtherLootTable.TYPE, new IGuiLootTablePresetFactory<LootTablePresetOtherLootTable>() {
			@Override
			public String getTitle() {
				return I18n.format("type.lootTable.preset.otherLootTable.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.lootTable.preset.otherLootTable.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, LootTablePresetOtherLootTable preset) {
				return new GuiEditLootTablePresetOtherLootTable(parent, preset == null ? I18n.format("gui.edit.lootTable.preset.otherLootTable.title") : I18n.format("gui.edit.editing", preset.id), addon, preset);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, LootTablePresetOtherLootTable preset) {
				GuiEditLootTablePresetOtherLootTable editScreen = new GuiEditLootTablePresetOtherLootTable(parent, I18n.format("gui.edit.lootTable.preset.otherLootTable.title"), addon, null);
				editScreen.copyFrom(preset);
				return editScreen;
			}
		});
		
		LootTablePresetManager.registerGuiFactory(LootTablePresetBlockItself.TYPE, new IGuiLootTablePresetFactory<LootTablePresetBlockItself>() {
			@Override
			public String getTitle() {
				return I18n.format("type.lootTable.preset.blockItself.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.lootTable.preset.blockItself.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, LootTablePresetBlockItself preset) {
				return new GuiEditLootTablePresetBlockItself(parent, preset == null ? I18n.format("gui.edit.lootTable.preset.blockItself.title") : I18n.format("gui.edit.editing", preset.id), addon, preset);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, LootTablePresetBlockItself preset) {
				GuiEditLootTablePresetBlockItself editScreen = new GuiEditLootTablePresetBlockItself(parent, I18n.format("gui.edit.lootTable.preset.blockItself.title"), addon, null);
				editScreen.copyFrom(preset);
				return editScreen;
			}
		});
		
		LootTablePresetManager.registerGuiFactory(LootTablePresetBlockItemDrop.TYPE, new IGuiLootTablePresetFactory<LootTablePresetBlockItemDrop>() {
			@Override
			public String getTitle() {
				return I18n.format("type.lootTable.preset.blockItem.title");
			}
			
			@Override
			public String getDescription() {
				return I18n.format("type.lootTable.preset.blockItem.description");
			}
			
			@Override
			public GuiEdit getEditScreen(GuiScreen parent, Addon addon, LootTablePresetBlockItemDrop preset) {
				return new GuiEditLootTablePresetBlockItem(parent, preset == null ? I18n.format("gui.edit.lootTable.preset.blockItem.title") : I18n.format("gui.edit.editing", preset.id), addon, preset);
			}
			
			@Override
			public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, LootTablePresetBlockItemDrop preset) {
				GuiEditLootTablePresetBlockItem editScreen = new GuiEditLootTablePresetBlockItem(parent, I18n.format("gui.edit.lootTable.preset.blockItem.title"), addon, null);
				editScreen.copyFrom(preset);
				return editScreen;
			}
		});
		
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
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemInHand.TYPE, new IGuiEffectCauseFactory<EffectCauseItemInHand>() {

			@Override
			public String getTitle() {
				return I18n.format("type.effectCause.held.title");
			}

			@Override
			public ItemStack getDisplayStack(EffectCauseItemInHand cause) {
				return cause.itemStack;
			}

			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemInHand();
			}
			
		});
		
		EffectManager.registerGuiFactory(EffectPotion.TYPE, new IGuiEffectFactory<EffectPotion>() {

			@Override
			public String getTitle() {
				return I18n.format("type.effect.potion.title");
			}

			@Override
			public ItemStack getDisplayStack(EffectPotion effect) {
				ItemStack displayStack = new ItemStack(Items.POTIONITEM);
				
				if (effect.potionType != null) {
					PotionUtils.addPotionToItemStack(displayStack, effect.potionType);
				}
				
				if (effect.potion != null) {
					PotionUtils.appendEffects(displayStack, Collections.singleton(effect.potion));
					PotionEffect effectWithParticles = new PotionEffect(effect.potion.getPotion(), effect.potion.getDuration(), effect.potion.getAmplifier());
					displayStack.getTagCompound().setInteger("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(Collections.singleton(effectWithParticles)));
				}
				
				return displayStack;
			}

			@Override
			public IGuiEffectEditHandler getEditHandler() {
				
				return new GuiEditHandlerEffectPotion();
			}
			
		});
	}
	
}
