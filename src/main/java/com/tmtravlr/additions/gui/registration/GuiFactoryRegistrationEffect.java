package com.tmtravlr.additions.gui.registration;

import java.util.Collections;

import javax.annotation.Nullable;

import com.tmtravlr.additions.addon.effects.EffectCancelNormal;
import com.tmtravlr.additions.addon.effects.EffectCommand;
import com.tmtravlr.additions.addon.effects.EffectConsumeItem;
import com.tmtravlr.additions.addon.effects.EffectDamageItem;
import com.tmtravlr.additions.addon.effects.EffectLootTableAt;
import com.tmtravlr.additions.addon.effects.EffectLootTableInside;
import com.tmtravlr.additions.addon.effects.EffectManager;
import com.tmtravlr.additions.addon.effects.EffectPotion;
import com.tmtravlr.additions.api.gui.IGuiEffectEditHandler;
import com.tmtravlr.additions.api.gui.IGuiEffectFactory;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectCancelNormal;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectCommand;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectConsumeItem;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectDamageItem;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectLootTableAt;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectLootTableInside;
import com.tmtravlr.additions.gui.view.edit.update.effect.GuiEditHandlerEffectPotion;

import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;

public class GuiFactoryRegistrationEffect {
	
	public static void registerGuiFactories() {
		EffectManager.registerGuiFactory(EffectPotion.TYPE, new IGuiEffectFactory<EffectPotion>() {

			@Override
			public String getTitle(@Nullable EffectPotion effect) {
				if (effect == null) {
					return I18n.format("type.effect.potion.title");
				} else {
					String effectName = "";
					
					if (effect.potion != null) {
						ResourceLocation potionName = Potion.REGISTRY.getNameForObject(effect.potion.getPotion());
						
						if (potionName != null) {
							effectName = potionName.toString();
						}
					}
					
					if (effect.potionType != null) {
						effectName = I18n.format(effect.potionType.getNamePrefixed("potion.effect."));
					}
					
					return effectName.isEmpty() ? I18n.format("type.effect.potion.title") : I18n.format("type.effect.potion.title.withName", effectName);
				}
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
		
		EffectManager.registerGuiFactory(EffectCommand.TYPE, new IGuiEffectFactory<EffectCommand>() {

			@Override
			public String getTitle(@Nullable EffectCommand effect) {
				return effect == null ? I18n.format("type.effect.command.title") : I18n.format("type.effect.command.title.withName", effect.command);
			}

			@Override
			public ItemStack getDisplayStack(EffectCommand effect) {
				return ItemStack.EMPTY;
			}

			@Override
			public IGuiEffectEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCommand();
			}
			
		});
		
		EffectManager.registerGuiFactory(EffectLootTableInside.TYPE, new IGuiEffectFactory<EffectLootTableInside>() {

			@Override
			public String getTitle(@Nullable EffectLootTableInside effect) {
				return effect == null ? I18n.format("type.effect.lootTableInside.title") : I18n.format("type.effect.lootTableInside.title.withName", effect.lootTable.toString());
			}

			@Override
			public ItemStack getDisplayStack(EffectLootTableInside effect) {
				return ItemStack.EMPTY;
			}

			@Override
			public IGuiEffectEditHandler getEditHandler() {
				return new GuiEditHandlerEffectLootTableInside();
			}
			
		});
		
		EffectManager.registerGuiFactory(EffectLootTableAt.TYPE, new IGuiEffectFactory<EffectLootTableAt>() {

			@Override
			public String getTitle(@Nullable EffectLootTableAt effect) {
				return effect == null ? I18n.format("type.effect.lootTableAt.title") : I18n.format("type.effect.lootTableAt.title.withName", effect.lootTable.toString());
			}

			@Override
			public ItemStack getDisplayStack(EffectLootTableAt effect) {
				return ItemStack.EMPTY;
			}

			@Override
			public IGuiEffectEditHandler getEditHandler() {
				return new GuiEditHandlerEffectLootTableAt();
			}
			
		});
		
		EffectManager.registerGuiFactory(EffectCancelNormal.TYPE, new IGuiEffectFactory<EffectCancelNormal>() {

			@Override
			public String getTitle(@Nullable EffectCancelNormal effect) {
				return I18n.format("type.effect.cancelNormal.title");
			}

			@Override
			public ItemStack getDisplayStack(EffectCancelNormal effect) {
				return ItemStack.EMPTY;
			}

			@Override
			public IGuiEffectEditHandler getEditHandler() {
				return new GuiEditHandlerEffectCancelNormal();
			}
			
		});
		
		EffectManager.registerGuiFactory(EffectConsumeItem.TYPE, new IGuiEffectFactory<EffectConsumeItem>() {

			@Override
			public String getTitle(@Nullable EffectConsumeItem effect) {
				return (effect == null || effect.amount == 1) ? I18n.format("type.effect.consumeItem.title") : I18n.format("type.effect.consumeItem.title.withAmount", effect.amount);
			}

			@Override
			public ItemStack getDisplayStack(EffectConsumeItem effect) {
				return ItemStack.EMPTY;
			}

			@Override
			public IGuiEffectEditHandler getEditHandler() {
				return new GuiEditHandlerEffectConsumeItem();
			}
			
		});
		
		EffectManager.registerGuiFactory(EffectDamageItem.TYPE, new IGuiEffectFactory<EffectDamageItem>() {

			@Override
			public String getTitle(@Nullable EffectDamageItem effect) {
				return effect == null ? I18n.format("type.effect.damageItem.title") : I18n.format("type.effect.damageItem.title.withAmount", effect.amount);
			}

			@Override
			public ItemStack getDisplayStack(EffectDamageItem effect) {
				return ItemStack.EMPTY;
			}

			@Override
			public IGuiEffectEditHandler getEditHandler() {
				return new GuiEditHandlerEffectDamageItem();
			}
			
		});
	}

}
