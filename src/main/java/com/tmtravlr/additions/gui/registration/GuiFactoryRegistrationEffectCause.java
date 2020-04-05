package com.tmtravlr.additions.gui.registration;

import javax.annotation.Nullable;

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
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseFactory;
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

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class GuiFactoryRegistrationEffectCause {
	
	public static void registerGuiFactories() {
		EffectCauseManager.registerGuiFactory(EffectCauseItemInHand.TYPE, new IGuiEffectCauseFactory<EffectCauseItemInHand>() {

			@Override
			public String getTitle(@Nullable EffectCauseItemInHand cause) {
				return I18n.format("type.effectCause.held.title");
			}

			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemInHand cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}

			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemInHand();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemInInventory.TYPE, new IGuiEffectCauseFactory<EffectCauseItemInInventory>() {

			@Override
			public String getTitle(@Nullable EffectCauseItemInInventory cause) {
				return I18n.format("type.effectCause.inInventory.title");
			}

			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemInInventory cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}

			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemInInventory();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemEquipped.TYPE, new IGuiEffectCauseFactory<EffectCauseItemEquipped>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseItemEquipped cause) {
				boolean plural = cause == null || cause.equipment.size() > 1;
				return I18n.format("type.effectCause.equipped.title." + (plural ? "plural" : "singular"));
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemEquipped cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				if (cause.equipment.size() > 0) {
					displayStacks.addAll(cause.equipment.values());
				}
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemEquipped();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemRightClick.TYPE, new IGuiEffectCauseFactory<EffectCauseItemRightClick>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseItemRightClick cause) {
				return I18n.format("type.effectCause.itemRightClick.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemRightClick cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemRightClick();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemRightClickBlock.TYPE, new IGuiEffectCauseFactory<EffectCauseItemRightClickBlock>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseItemRightClickBlock cause) {
				return I18n.format("type.effectCause.itemRightClickBlock.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemRightClickBlock cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemRightClickBlock();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemRightClickEntity.TYPE, new IGuiEffectCauseFactory<EffectCauseItemRightClickEntity>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseItemRightClickEntity cause) {
				return I18n.format("type.effectCause.itemRightClickEntity.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemRightClickEntity cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemRightClickEntity();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemUsing.TYPE, new IGuiEffectCauseFactory<EffectCauseItemUsing>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseItemUsing cause) {
				return I18n.format("type.effectCause.itemUsing.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemUsing cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemUsing();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemLeftClick.TYPE, new IGuiEffectCauseFactory<EffectCauseItemLeftClick>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseItemLeftClick cause) {
				return I18n.format("type.effectCause.itemLeftClick.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemLeftClick cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemLeftClick();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemDiggingBlock.TYPE, new IGuiEffectCauseFactory<EffectCauseItemDiggingBlock>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseItemDiggingBlock cause) {
				return I18n.format("type.effectCause.itemDigging.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemDiggingBlock cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemDiggingBlock();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemBreakBlock.TYPE, new IGuiEffectCauseFactory<EffectCauseItemBreakBlock>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseItemBreakBlock cause) {
				return I18n.format("type.effectCause.itemBreakBlock.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemBreakBlock cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemBreakBlock();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemAttack.TYPE, new IGuiEffectCauseFactory<EffectCauseItemAttack>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseItemAttack cause) {
				return I18n.format("type.effectCause.itemAttack.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemAttack cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemAttack();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseItemKill.TYPE, new IGuiEffectCauseFactory<EffectCauseItemKill>() {

			@Override
			public String getTitle(@Nullable EffectCauseItemKill cause) {
				return I18n.format("type.effectCause.itemKill.title");
			}

			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseItemKill cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				displayStacks.add(cause.itemStack);
				return displayStacks;
			}

			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseItemKill();
			}
			
		});
	}

}
