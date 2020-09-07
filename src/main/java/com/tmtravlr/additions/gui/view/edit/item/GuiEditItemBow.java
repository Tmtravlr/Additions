package com.tmtravlr.additions.gui.view.edit.item;

import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedBow;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIngredientOreNBTInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputSoundEvent;
import com.tmtravlr.additions.gui.view.components.input.effect.GuiComponentEffectInput;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemBowTexture;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a new simple item or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017
 */
public class GuiEditItemBow extends GuiEditItem<ItemAddedBow> {
	
	private GuiComponentIntegerInput itemDurabilityInput;
	private GuiComponentIntegerInput itemEnchantabilityInput;
	private GuiComponentIngredientOreNBTInput itemRepairStacksInput;
	private GuiComponentFloatInput itemExtraDamageInput;
	private GuiComponentFloatInput itemScatteringInput;
	private GuiComponentIntegerInput itemDrawTimeInput;
	private GuiComponentIntegerInput itemShotCountInput;
	private GuiComponentBooleanInput itemFiresVanillaArrowsInput;
	private GuiComponentBooleanInput itemAlwaysInfiniteInput;
	private GuiComponentBooleanInput itemInfinityRequiresAmmo;
	private GuiComponentBooleanInput itemConsumesOneAmmoInput;
	private GuiComponentBooleanInput itemUseEfficiencyInput;
	private GuiComponentFloatInput itemEfficiencyMultiplierInput;
	private GuiComponentDropdownInputSoundEvent itemShotSoundInput;
	private GuiComponentListInput<GuiComponentDropdownInputItem> itemAmmoItemsInput;
	private GuiComponentListInput<GuiComponentEffectInput> itemShotEffectsInput;

	public GuiEditItemBow(GuiScreen parentScreen, String title, Addon addon, ItemAddedBow item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedBow();
		} else {
			this.item = item;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.itemDurabilityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.itemMaterial.baseToolDurability.label"), this, false);
		this.itemDurabilityInput.setMinimum(0);
		this.itemDurabilityInput.setRequired();
		this.itemDurabilityInput.setInfo(new TextComponentTranslation("gui.edit.itemMaterial.baseToolDurability.info"));
		if (!this.isNew) {
			this.itemDurabilityInput.setDefaultInteger(this.item.getMaxDamage());
		}
		
		this.itemEnchantabilityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.enchantability.label"), this, false);
		this.itemEnchantabilityInput.setMinimum(0);
		this.itemEnchantabilityInput.setInfo(new TextComponentTranslation("gui.edit.item.enchantability.info"));
		if (!this.isNew) {
			this.itemEnchantabilityInput.setDefaultInteger(this.item.enchantability);
		}
		
		this.itemRepairStacksInput = new GuiComponentIngredientOreNBTInput(I18n.format("gui.edit.item.repairStacks.label"), this);
		if (!this.isNew) {
			this.itemRepairStacksInput.setDefaultIngredient(this.item.repairStacks);
		}

		this.itemExtraDamageInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.shooter.damage.label"), this, false);
		this.itemExtraDamageInput.setMinimum(0f);
		if (!this.isNew) {
			this.itemExtraDamageInput.setDefaultFloat(this.item.extraDamage);
		}

		this.itemScatteringInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.shooter.scattering.label"), this, false);
		this.itemScatteringInput.setMinimum(0f);
		if (!this.isNew) {
			this.itemScatteringInput.setDefaultFloat(this.item.scattering);
		}

		this.itemDrawTimeInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.bow.drawTime.label"), this, false);
		this.itemDrawTimeInput.setMinimum(1);
		this.itemDrawTimeInput.setMaximum(72000);
		if (!this.isNew) {
			this.itemDrawTimeInput.setDefaultInteger(this.item.drawTime);
		} else {
			this.itemDrawTimeInput.setDefaultInteger(20);
		}

		this.itemShotCountInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.shooter.shotCount.label"), this, false);
		this.itemShotCountInput.setMinimum(1);
		this.itemShotCountInput.setMaximum(100);
		if (!this.isNew) {
			this.itemShotCountInput.setDefaultInteger(this.item.shotCount);
		}

		this.itemFiresVanillaArrowsInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.shooter.firesVanillaArrows.label"), this);
		if (!this.isNew) {
			this.itemFiresVanillaArrowsInput.setDefaultBoolean(this.item.firesVanillaArrows);
		} else {
			this.itemFiresVanillaArrowsInput.setDefaultBoolean(true);
		}

		this.itemAlwaysInfiniteInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.shooter.alwaysInfinite.label"), this);
		if (!this.isNew) {
			this.itemAlwaysInfiniteInput.setDefaultBoolean(this.item.alwaysInfinite);
		}

		this.itemInfinityRequiresAmmo = new GuiComponentBooleanInput(I18n.format("gui.edit.item.shooter.infinityRequiresAmmo.label"), this);
		this.itemInfinityRequiresAmmo.setInfo(new TextComponentTranslation("gui.edit.item.shooter.infinityRequiresAmmo.info"));
		if (!this.isNew) {
			this.itemInfinityRequiresAmmo.setDefaultBoolean(this.item.infinityRequresAmmo);
		}
		
		this.itemConsumesOneAmmoInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.shooter.consumesOneAmmo.label"), this);
		this.itemConsumesOneAmmoInput.setInfo(new TextComponentTranslation("gui.edit.item.shooter.consumesOneAmmo.info"));
		if (!this.isNew) {
			this.itemConsumesOneAmmoInput.setDefaultBoolean(this.item.consumesOneAmmo);
		}
		
		this.itemEfficiencyMultiplierInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.efficiencyMultiplier.label"), this, false);
		this.itemEfficiencyMultiplierInput.setHidden(true);
		this.itemEfficiencyMultiplierInput.setMaximum(1);
		this.itemEfficiencyMultiplierInput.setMinimum(0);
		if (!this.isNew) {
			this.itemEfficiencyMultiplierInput.setDefaultFloat(this.item.efficiencyMultiplier == 0 ? 0.5f : this.item.efficiencyMultiplier);
		} else {
			this.itemEfficiencyMultiplierInput.setDefaultFloat(0.5f);
		}
		
		this.itemUseEfficiencyInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.useEfficiency.label"), this) {
			
			@Override
			public void setDefaultBoolean(boolean input) {
				GuiEditItemBow.this.itemEfficiencyMultiplierInput.setHidden(!input);
				super.setDefaultBoolean(input);
			}
			
		};
		this.itemUseEfficiencyInput.setInfo(new TextComponentTranslation("gui.edit.item.useEfficiency.info"));
		this.itemUseEfficiencyInput.setDefaultBoolean(this.item.efficiencyMultiplier != 0);
		
		this.itemShotSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.item.shooter.shotSound.label"), this.addon, this);
		if (!this.isNew) {
			this.itemShotSoundInput.setDefaultSelected(this.item.shotSound);
		} else {
			this.itemShotSoundInput.setDefaultSelected(SoundEvents.ENTITY_ARROW_SHOOT);
		}
		
		this.itemAmmoItemsInput = new GuiComponentListInput<GuiComponentDropdownInputItem>(I18n.format("gui.edit.item.shooter.ammoItems.label"), this) {

			@Override
			public GuiComponentDropdownInputItem createBlankComponent() {
				GuiComponentDropdownInputItem input = new GuiComponentDropdownInputItem("", this.editScreen);
				return input;
			}
		};
		if (!this.isNew) {
			List<GuiComponentDropdownInputItem> ammoItemInputs = this.item.ammoItems.stream().map(ammoItem -> {
				GuiComponentDropdownInputItem input = this.itemAmmoItemsInput.createBlankComponent();
				input.setDefaultSelected(ammoItem);
				return input;
			}).collect(Collectors.toList());
			
			this.itemAmmoItemsInput.setDefaultComponents(ammoItemInputs);
		}
		
		this.itemShotEffectsInput = new GuiComponentListInput<GuiComponentEffectInput>(I18n.format("gui.edit.item.shooter.shotEffects.label"), this) {

			@Override
			public GuiComponentEffectInput createBlankComponent() {
				return new GuiComponentEffectInput("", GuiEditItemBow.this.addon, this.editScreen);
			}
			
		};
		if (!this.isNew) {
			this.item.shotEffects.forEach(toAdd -> {
				GuiComponentEffectInput input = this.itemShotEffectsInput.createBlankComponent();
				input.setDefaultEffect(toAdd);
				this.itemShotEffectsInput.addDefaultComponent(input);
			});
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.itemIdInput);
		this.components.add(this.itemNameInput);
		this.components.add(this.itemShinesInput);
		this.components.add(this.itemDurabilityInput);
		this.components.add(this.itemEnchantabilityInput);
		this.components.add(this.itemRepairStacksInput);
		this.components.add(this.itemExtraDamageInput);
		this.components.add(this.itemDrawTimeInput);
		this.components.add(this.itemShotCountInput);
		this.components.add(this.itemAlwaysInfiniteInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}

		this.advancedComponents.add(this.itemFiresVanillaArrowsInput);
		this.advancedComponents.add(this.itemInfinityRequiresAmmo);
		this.advancedComponents.add(this.itemConsumesOneAmmoInput);
		this.advancedComponents.add(this.itemAmmoItemsInput);
		this.advancedComponents.add(this.itemShotEffectsInput);
		this.advancedComponents.add(this.itemScatteringInput);
		this.advancedComponents.add(this.itemUseEfficiencyInput);
		this.advancedComponents.add(this.itemEfficiencyMultiplierInput);
		this.advancedComponents.add(this.itemShotSoundInput);
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemIsBeaconPaymentInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
	
	@Override
	public void saveObject() {
		
		this.item.setMaxDamage(this.itemDurabilityInput.getInteger());
		this.item.enchantability = this.itemEnchantabilityInput.getInteger();
		this.item.repairStacks = this.itemRepairStacksInput.getIngredient();
		this.item.extraDamage = this.itemExtraDamageInput.getFloat();
		this.item.scattering = this.itemScatteringInput.getFloat();
		this.item.drawTime = this.itemDrawTimeInput.getInteger();
		this.item.shotCount = this.itemShotCountInput.getInteger();
		this.item.firesVanillaArrows = this.itemFiresVanillaArrowsInput.getBoolean();
		this.item.alwaysInfinite = this.itemAlwaysInfiniteInput.getBoolean();
		this.item.infinityRequresAmmo = this.itemInfinityRequiresAmmo.getBoolean();
		this.item.consumesOneAmmo = this.itemConsumesOneAmmoInput.getBoolean();
		this.item.efficiencyMultiplier = this.itemUseEfficiencyInput.getBoolean() ? this.itemEfficiencyMultiplierInput.getFloat() : 0;
		this.item.shotSound = this.itemShotSoundInput.getSelected();
		this.item.ammoItems = this.itemAmmoItemsInput.getComponents().stream().map(GuiComponentDropdownInputItem::getSelected).collect(Collectors.toList());
		
		if (!this.itemShotEffectsInput.getComponents().isEmpty()) {
			this.item.shotEffects = this.itemShotEffectsInput.getComponents().stream().filter(input -> input.getEffect() != null).map(GuiComponentEffectInput::getEffect).collect(Collectors.toList());
		}
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.itemDurabilityInput.setDefaultInteger(this.copyFrom.getMaxDamage());
		this.itemEnchantabilityInput.setDefaultInteger(this.copyFrom.enchantability);
		this.itemRepairStacksInput.setDefaultIngredient(this.copyFrom.repairStacks);
		this.itemExtraDamageInput.setDefaultFloat(this.copyFrom.extraDamage);
		this.itemScatteringInput.setDefaultFloat(this.copyFrom.scattering);
		this.itemDrawTimeInput.setDefaultInteger(this.copyFrom.drawTime);
		this.itemShotCountInput.setDefaultInteger(this.copyFrom.shotCount);
		this.itemFiresVanillaArrowsInput.setDefaultBoolean(this.copyFrom.firesVanillaArrows);
		this.itemAlwaysInfiniteInput.setDefaultBoolean(this.copyFrom.alwaysInfinite);
		this.itemInfinityRequiresAmmo.setDefaultBoolean(this.copyFrom.infinityRequresAmmo);
		this.itemConsumesOneAmmoInput.setDefaultBoolean(this.copyFrom.consumesOneAmmo);
		this.itemUseEfficiencyInput.setDefaultBoolean(this.copyFrom.efficiencyMultiplier == 0);
		this.itemEfficiencyMultiplierInput.setDefaultFloat(this.copyFrom.efficiencyMultiplier == 0 ? 0.5f : this.copyFrom.efficiencyMultiplier);
		this.itemShotSoundInput.setDefaultSelected(this.copyFrom.shotSound);
		
		this.copyFrom.ammoItems.forEach(toAdd -> {
			GuiComponentDropdownInputItem input = this.itemAmmoItemsInput.createBlankComponent();
			input.setDefaultSelected(toAdd);
			this.itemAmmoItemsInput.addDefaultComponent(input);
		});
		
		this.copyFrom.shotEffects.forEach(toAdd -> {
			GuiComponentEffectInput input = this.itemShotEffectsInput.createBlankComponent();
			input.setDefaultEffect(toAdd);
			this.itemShotEffectsInput.addDefaultComponent(input);
		});
		
		super.copyFromOther();
	}
    
	@Override
	protected void openTextureDialogue(GuiScreen nextScreen) {
    	this.mc.displayGuiScreen(new GuiEditItemBowTexture(nextScreen, this.addon, this.item, this.isNew));
    }
	
	@Override
	protected void onToggleShowAdvanced(boolean showAdvanced) {
		if (showAdvanced) {
			this.itemUseEfficiencyInput.setDefaultBoolean(this.itemUseEfficiencyInput.getBoolean());
		}
	}
}
