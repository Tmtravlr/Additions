package com.tmtravlr.additions.gui.view.edit.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.items.ItemAddedFood;
import com.tmtravlr.additions.addon.items.ItemAddedSimple;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentButton;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentAttributeModifierInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemTexture;
import com.tmtravlr.additions.type.AdditionTypeItem;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Page for adding a new simple item or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017
 */
public class GuiEditItemFood extends GuiEditItem<ItemAddedFood> {
	
	private GuiComponentIntegerInput itemHungerInput;
	private GuiComponentFloatInput itemSaturationInput;
	private GuiComponentIntegerInput itemEatTimeInput;
	private GuiComponentBooleanInput itemAlwaysEdibleInput;
	private GuiComponentBooleanInput itemWolvesEatInput;
	private GuiComponentBooleanInput itemIsDrinkInput;
	private GuiComponentBooleanInput itemHasPotionEffectsInput;
    
	public GuiEditItemFood(GuiScreen parentScreen, String title, Addon addon, ItemAddedFood item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedFood();
		} else {
			this.item = item;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.itemHungerInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.food.hunger.label"), this, false);
		this.itemHungerInput.setMinimum(0);
		this.itemHungerInput.setMaximum(20);
		this.itemHungerInput.setRequired();
		this.itemHungerInput.setInfo(new TextComponentTranslation("gui.edit.item.food.hunger.info"));
		if (!this.isNew) {
			this.itemHungerInput.setDefaultInteger(this.item.getHungerRestored());
		}

		this.itemSaturationInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.food.saturation.label"), this, false);
		this.itemSaturationInput.setMinimum(0);
		this.itemSaturationInput.setMaximum(20);
		this.itemSaturationInput.setRequired();
		this.itemSaturationInput.setInfo(new TextComponentTranslation("gui.edit.item.food.saturation.info"));
		if (!this.isNew) {
			this.itemSaturationInput.setDefaultFloat(this.item.getSaturationRestored() * (2*this.item.getHungerRestored()));
		}
		
		this.itemEatTimeInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.food.eatTime.label"), this, false);
		this.itemEatTimeInput.setMinimum(0);
		this.itemEatTimeInput.setMaximum(1024);
		this.itemEatTimeInput.setDefaultInteger(32);
		this.itemEatTimeInput.setRequired();
		this.itemEatTimeInput.setInfo(new TextComponentTranslation("gui.edit.item.food.eatTime.info"));
		if (!this.isNew) {
			this.itemEatTimeInput.setDefaultInteger(this.item.eatTime);
		}
		
		this.itemAlwaysEdibleInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.food.alwaysEdible.label"), this);
		if (!this.isNew) {
			this.itemAlwaysEdibleInput.setDefaultBoolean(this.item.isAlwaysEdible());
		}
		
		this.itemWolvesEatInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.food.wolvesEat.label"), this);
		if (!this.isNew) {
			this.itemWolvesEatInput.setDefaultBoolean(this.item.doWolvesEat());
		}
		
		this.itemIsDrinkInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.food.isDrink.label"), this);
		if (!this.isNew) {
			this.itemIsDrinkInput.setDefaultBoolean(this.item.isDrink);
		}
		
		this.itemHasPotionEffectsInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.hasPotionEffects.label"), this);
		if (!this.isNew) {
			this.itemHasPotionEffectsInput.setDefaultBoolean(this.item.hasPotionEffects);
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.itemIdInput);
		this.components.add(this.itemNameInput);
		this.components.add(this.itemStackSizeInput);
		this.components.add(this.itemShinesInput);
		this.components.add(this.itemHungerInput);
		this.components.add(this.itemSaturationInput);
		this.components.add(this.itemEatTimeInput);
		this.components.add(this.itemAlwaysEdibleInput);
		this.components.add(this.itemIsDrinkInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemWolvesEatInput);
		this.advancedComponents.add(this.itemHasPotionEffectsInput);
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
	
	@Override
	public void saveObject() {
		
		int hunger = this.itemHungerInput.getInteger();
		float saturation = this.itemSaturationInput.getFloat() / (2*hunger);
		boolean wolvesEat = this.itemWolvesEatInput.getBoolean();
		boolean alwaysEdible = this.itemAlwaysEdibleInput.getBoolean();
		
		this.item.setFoodStats(hunger, saturation, wolvesEat, alwaysEdible);
		this.item.eatTime = this.itemEatTimeInput.getInteger();
		this.item.isDrink = this.itemIsDrinkInput.getBoolean();
		this.item.hasPotionEffects = this.itemHasPotionEffectsInput.getBoolean();
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.itemHungerInput.setDefaultInteger(this.copyFrom.getHungerRestored());
		this.itemSaturationInput.setDefaultFloat(this.copyFrom.getSaturationRestored() * (2*this.copyFrom.getHungerRestored()));
		this.itemWolvesEatInput.setDefaultBoolean(this.copyFrom.doWolvesEat());
		this.itemAlwaysEdibleInput.setDefaultBoolean(this.copyFrom.isAlwaysEdible());
		this.itemEatTimeInput.setDefaultInteger(this.copyFrom.eatTime);
		this.itemIsDrinkInput.setDefaultBoolean(this.copyFrom.isDrink);
		this.itemHasPotionEffectsInput.setDefaultBoolean(this.copyFrom.hasPotionEffects);
		
		super.copyFromOther();
	}
}
