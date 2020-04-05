package com.tmtravlr.additions.gui.registration;

import java.util.Collections;

import javax.annotation.Nullable;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedCarpet;
import com.tmtravlr.additions.addon.blocks.BlockAddedFacing;
import com.tmtravlr.additions.addon.blocks.BlockAddedFalling;
import com.tmtravlr.additions.addon.blocks.BlockAddedManager;
import com.tmtravlr.additions.addon.blocks.BlockAddedPillar;
import com.tmtravlr.additions.addon.blocks.BlockAddedSimple;
import com.tmtravlr.additions.addon.blocks.BlockAddedSlab;
import com.tmtravlr.additions.addon.blocks.BlockAddedStairs;
import com.tmtravlr.additions.addon.effects.EffectCancelNormal;
import com.tmtravlr.additions.addon.effects.EffectCommand;
import com.tmtravlr.additions.addon.effects.EffectConsumeItem;
import com.tmtravlr.additions.addon.effects.EffectDamageItem;
import com.tmtravlr.additions.addon.effects.EffectLootTableAt;
import com.tmtravlr.additions.addon.effects.EffectLootTableInside;
import com.tmtravlr.additions.addon.effects.EffectManager;
import com.tmtravlr.additions.addon.effects.EffectPotion;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemAttack;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemBreakBlock;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemDiggingBlock;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemEquipped;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemInHand;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemInInventory;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemKill;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemLeftClick;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClick;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClickBlock;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClickEntity;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemUsing;
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
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockCarpet;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockFacing;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockFalling;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockPillar;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockSimple;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockSlab;
import com.tmtravlr.additions.gui.view.edit.block.GuiEditBlockStairs;
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
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectCancelNormal;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectCommand;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectConsumeItem;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectDamageItem;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectLootTableAt;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectLootTableInside;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectPotion;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemAttack;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemBreakBlock;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemDiggingBlock;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemEquipped;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemInHand;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemInInventory;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemKill;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemLeftClick;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemRightClick;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemRightClickBlock;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemRightClickEntity;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseItemUsing;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

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
		
		GuiFactoryRegistrationItemAdded.registerGuiFactories();
		GuiFactoryRegistrationBlockAdded.registerGuiFactories();
		GuiFactoryRegistrationLootTablePreset.registerGuiFactories();
		GuiFactoryRegistrationRecipeAdded.registerGuiFactories();
		GuiFactoryRegistrationEffectCause.registerGuiFactories();
		GuiFactoryRegistrationEffect.registerGuiFactories();
	}
	
}
