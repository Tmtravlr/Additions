package com.tmtravlr.additions.gui.view.edit.item;

import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedThrowable;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputSoundEvent;
import com.tmtravlr.additions.gui.view.components.input.effect.GuiComponentEffectInput;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;

/**
 * Page for adding a new throwable or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2018
 */
public class GuiEditItemThrowable extends GuiEditItemBaseProjectile<ItemAddedThrowable> {
	
	private GuiComponentFloatInput itemVelocityInput;
	private GuiComponentFloatInput itemInaccuracyInput;
	private GuiComponentDropdownInputSoundEvent itemThrowSoundInput;
	private GuiComponentListInput<GuiComponentEffectInput> itemThrowEffectsInput;
    
	public GuiEditItemThrowable(GuiScreen parentScreen, String title, Addon addon, ItemAddedThrowable item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedThrowable();
		} else {
			this.item = item;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.itemVelocityInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.throwable.velocity.label"), this, false);
		this.itemVelocityInput.setMinimum(0);
		this.itemVelocityInput.setMaximum(100);
		if (!this.isNew) {
			this.itemVelocityInput.setDefaultFloat(this.item.velocity);
		} else {
			this.itemVelocityInput.setDefaultFloat(1.5f);
		}
		
		this.itemInaccuracyInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.throwable.inaccuracy.label"), this, false);
		this.itemInaccuracyInput.setMinimum(0);
		if (!this.isNew) {
			this.itemInaccuracyInput.setDefaultFloat(this.item.inaccuracy);
		}
		
		this.itemThrowSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.item.throwable.throwSound.label"), this.addon, this);
		if (!this.isNew) {
			this.itemThrowSoundInput.setDefaultSelected(this.item.throwSound);
		} else {
			this.itemThrowSoundInput.setDefaultSelected(SoundEvents.ENTITY_SNOWBALL_THROW);
		}
		
		this.itemThrowEffectsInput = new GuiComponentListInput<GuiComponentEffectInput>(I18n.format("gui.edit.item.throwable.throwEffects.label"), this) {

			@Override
			public GuiComponentEffectInput createBlankComponent() {
				return new GuiComponentEffectInput("", GuiEditItemThrowable.this.addon, this.editScreen);
			}
			
		};
		if (!this.isNew) {
			this.item.throwEffects.forEach(toAdd -> {
				GuiComponentEffectInput input = this.itemThrowEffectsInput.createBlankComponent();
				input.setDefaultEffect(toAdd);
				this.itemThrowEffectsInput.addDefaultComponent(input);
			});
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.itemIdInput);
		this.components.add(this.itemNameInput);
		this.components.add(this.itemShinesInput);
		this.components.add(this.itemBaseDamageInput);
		this.components.add(this.itemBasePunchInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemGravityInput);
		this.advancedComponents.add(this.itemVelocityInput);
		this.advancedComponents.add(this.itemInaccuracyInput);
		this.advancedComponents.add(this.itemWorksWithInfinityInput);
		this.advancedComponents.add(this.itemSticksInGroundInput);
		this.advancedComponents.add(this.itemPiercesEntitiesInput);
		this.advancedComponents.add(this.itemWorksInWaterInput);
		this.advancedComponents.add(this.itemThrowEffectsInput);
		this.advancedComponents.add(this.itemHitEffectsInput);
		this.advancedComponents.add(this.itemHasPotionEffectsInput);
		this.advancedComponents.add(this.itemRenders3DInput);
		this.advancedComponents.add(this.itemDamageIgnoresSpeedInput);
		this.advancedComponents.add(this.itemThrowSoundInput);
		this.advancedComponents.add(this.itemHitSoundInput);
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemIsBeaconPaymentInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
	
	@Override
	public void saveObject() {
		this.item.velocity = this.itemVelocityInput.getFloat();
		this.item.inaccuracy = this.itemInaccuracyInput.getFloat();
		this.item.throwSound = this.itemThrowSoundInput.getSelected();
		
		if (!this.itemThrowEffectsInput.getComponents().isEmpty()) {
			this.item.throwEffects = this.itemThrowEffectsInput.getComponents().stream().filter(input -> input.getEffect() != null).map(GuiComponentEffectInput::getEffect).collect(Collectors.toList());
		}
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.itemVelocityInput.setDefaultFloat(this.copyFrom.velocity);
		this.itemInaccuracyInput.setDefaultFloat(this.copyFrom.inaccuracy);
		this.itemThrowSoundInput.setDefaultSelected(this.copyFrom.throwSound);
		
		this.copyFrom.throwEffects.forEach(toAdd -> {
			GuiComponentEffectInput input = this.itemThrowEffectsInput.createBlankComponent();
			input.setDefaultEffect(toAdd);
			this.itemThrowEffectsInput.addDefaultComponent(input);
		});
		
		super.copyFromOther();
	}
}
