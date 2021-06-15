package com.tmtravlr.additions.gui.registration;

import java.util.Random;

import javax.annotation.Nullable;

import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockBroken;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockContact;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockDigging;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockPlaced;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockRandom;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockRightClicked;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityAttacked;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityDeath;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityRightClicked;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntitySpawned;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityUpdate;
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
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseBlockBroken;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseBlockContact;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseBlockDigging;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseBlockPlaced;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseBlockRandom;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseBlockRightClicked;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseEntityAttacked;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseEntityDeath;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseEntityRightClicked;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseEntitySpawned;
import com.tmtravlr.additions.gui.view.edit.update.effect.cause.GuiEditHandlerEffectCauseEntityUpdate;
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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
				return I18n.format("type.effectCause.itemRightClicked.title");
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
		
		EffectCauseManager.registerGuiFactory(EffectCauseBlockPlaced.TYPE, new IGuiEffectCauseFactory<EffectCauseBlockPlaced>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseBlockPlaced cause) {
				return I18n.format("type.effectCause.blockPlaced.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseBlockPlaced cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				ItemStack displayStack = cause.blockState.getDisplayStack();
				
				if (!displayStack.isEmpty()) {
					displayStacks.add(displayStack);
				}
				
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseBlockPlaced();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseBlockDigging.TYPE, new IGuiEffectCauseFactory<EffectCauseBlockDigging>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseBlockDigging cause) {
				return I18n.format("type.effectCause.blockDigging.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseBlockDigging cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				ItemStack displayStack = cause.blockState.getDisplayStack();
				
				if (!displayStack.isEmpty()) {
					displayStacks.add(displayStack);
				}
				
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseBlockDigging();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseBlockBroken.TYPE, new IGuiEffectCauseFactory<EffectCauseBlockBroken>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseBlockBroken cause) {
				return I18n.format("type.effectCause.blockBroken.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseBlockBroken cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				ItemStack displayStack = cause.blockState.getDisplayStack();
				
				if (!displayStack.isEmpty()) {
					displayStacks.add(displayStack);
				}
				
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseBlockBroken();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseBlockRightClicked.TYPE, new IGuiEffectCauseFactory<EffectCauseBlockRightClicked>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseBlockRightClicked cause) {
				return I18n.format("type.effectCause.blockRightClicked.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseBlockRightClicked cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				ItemStack displayStack = cause.blockState.getDisplayStack();
				
				if (!displayStack.isEmpty()) {
					displayStacks.add(displayStack);
				}
				
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseBlockRightClicked();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseBlockRandom.TYPE, new IGuiEffectCauseFactory<EffectCauseBlockRandom>() {

			@Override
			public String getTitle(@Nullable EffectCauseBlockRandom cause) {
				return I18n.format("type.effectCause.blockRandom.title");
			}

			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseBlockRandom cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				ItemStack displayStack = cause.blockState.getDisplayStack();
				
				if (!displayStack.isEmpty()) {
					displayStacks.add(displayStack);
				}
				
				return displayStacks;
			}

			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseBlockRandom();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseBlockContact.TYPE, new IGuiEffectCauseFactory<EffectCauseBlockContact>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseBlockContact cause) {
				return I18n.format("type.effectCause.blockContact.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseBlockContact cause) {
				NonNullList<ItemStack> displayStacks = NonNullList.create();
				ItemStack displayStack = cause.blockState.getDisplayStack();
				
				if (!displayStack.isEmpty()) {
					displayStacks.add(displayStack);
				}
				
				return displayStacks;
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseBlockContact();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseEntitySpawned.TYPE, new IGuiEffectCauseFactory<EffectCauseEntitySpawned>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseEntitySpawned cause) {
				return I18n.format("type.effectCause.entitySpawned.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseEntitySpawned cause) {
				return NonNullList.create();
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseEntitySpawned();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseEntityUpdate.TYPE, new IGuiEffectCauseFactory<EffectCauseEntityUpdate>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseEntityUpdate cause) {
				return I18n.format("type.effectCause.entityUpdate.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseEntityUpdate cause) {
				return NonNullList.create();
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseEntityUpdate();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseEntityRightClicked.TYPE, new IGuiEffectCauseFactory<EffectCauseEntityRightClicked>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseEntityRightClicked cause) {
				return I18n.format("type.effectCause.entityRightClicked.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseEntityRightClicked cause) {
				return NonNullList.create();
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseEntityRightClicked();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseEntityAttacked.TYPE, new IGuiEffectCauseFactory<EffectCauseEntityAttacked>() {
			
			@Override
			public String getTitle(@Nullable EffectCauseEntityAttacked cause) {
				return I18n.format("type.effectCause.entityAttacked.title");
			}
			
			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseEntityAttacked cause) {
				return NonNullList.create();
			}
			
			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseEntityAttacked();
			}
			
		});
		
		EffectCauseManager.registerGuiFactory(EffectCauseEntityDeath.TYPE, new IGuiEffectCauseFactory<EffectCauseEntityDeath>() {

			@Override
			public String getTitle(@Nullable EffectCauseEntityDeath cause) {
				return I18n.format("type.effectCause.entityDeath.title");
			}

			@Override
			public NonNullList<ItemStack> getDisplayStacks(EffectCauseEntityDeath cause) {
				return NonNullList.create();
			}

			@Override
			public IGuiEffectCauseEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCauseEntityDeath();
			}
			
		});
	}

}
