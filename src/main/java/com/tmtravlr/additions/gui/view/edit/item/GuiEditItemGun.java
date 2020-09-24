package com.tmtravlr.additions.gui.view.edit.item;

import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedGun;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIngredientOreNBTInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputSoundEvent;
import com.tmtravlr.additions.gui.view.components.input.effect.GuiComponentEffectInput;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemTexture;
import com.tmtravlr.additions.util.models.ItemModelManager;

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
public class GuiEditItemGun extends GuiEditItem<ItemAddedGun> {
	
	private GuiComponentIntegerInput itemDurabilityInput;
	private GuiComponentIntegerInput itemEnchantabilityInput;
	private GuiComponentIngredientOreNBTInput itemRepairStacksInput;
	private GuiComponentFloatInput itemBaseDamageInput;
	private GuiComponentFloatInput itemShotVelocityInput;
	private GuiComponentFloatInput itemScatteringInput;
	private GuiComponentIntegerInput itemReloadTimeInput;
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

	public GuiEditItemGun(GuiScreen parentScreen, String title, Addon addon, ItemAddedGun item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedGun();
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

		this.itemBaseDamageInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.shooter.damage.label"), this, false);
		this.itemBaseDamageInput.setMinimum(0f);
		if (!this.isNew) {
			this.itemBaseDamageInput.setDefaultFloat(this.item.baseDamage);
		}

		this.itemShotVelocityInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.gun.shotVelocity.label"), this, false);
		this.itemShotVelocityInput.setMinimum(0f);
		if (!this.isNew) {
			this.itemShotVelocityInput.setDefaultFloat(this.item.shotVelocity);
		} else {
			this.itemShotVelocityInput.setDefaultFloat(1);			
		}

		this.itemScatteringInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.shooter.scattering.label"), this, false);
		this.itemScatteringInput.setMinimum(0f);
		if (!this.isNew) {
			this.itemScatteringInput.setDefaultFloat(this.item.scattering);
		}

		this.itemReloadTimeInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.gun.reloadTime.label"), this, false);
		this.itemReloadTimeInput.setMinimum(1);
		this.itemReloadTimeInput.setMaximum(72000);
		if (!this.isNew) {
			this.itemReloadTimeInput.setDefaultInteger(this.item.reloadTime);
		} else {
			this.itemReloadTimeInput.setDefaultInteger(20);
		}

		this.itemShotCountInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.shooter.shotCount.label"), this, false);
		this.itemShotCountInput.setMinimum(1);
		this.itemShotCountInput.setMaximum(100);
		if (!this.isNew) {
			this.itemShotCountInput.setDefaultInteger(this.item.shotCount);
		}

		this.itemFiresVanillaArrowsInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.shooter.firesVanillaArrows.label"), this);
		this.itemFiresVanillaArrowsInput.setDefaultBoolean(this.isNew ? true : this.item.firesVanillaArrows);

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
				GuiEditItemGun.this.itemEfficiencyMultiplierInput.setHidden(!input);
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
				return new GuiComponentEffectInput("", GuiEditItemGun.this.addon, this.editScreen);
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
		this.components.add(this.itemBaseDamageInput);
		this.components.add(this.itemReloadTimeInput);
		this.components.add(this.itemShotCountInput);
		this.components.add(this.itemAlwaysInfiniteInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemShotVelocityInput);
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
		this.item.baseDamage = this.itemBaseDamageInput.getFloat();
		this.item.shotVelocity = this.itemShotVelocityInput.getFloat();
		this.item.scattering = this.itemScatteringInput.getFloat();
		this.item.reloadTime = this.itemReloadTimeInput.getInteger();
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
		this.itemBaseDamageInput.setDefaultFloat(this.copyFrom.baseDamage);
		this.itemShotVelocityInput.setDefaultFloat(this.copyFrom.shotVelocity);
		this.itemScatteringInput.setDefaultFloat(this.copyFrom.scattering);
		this.itemReloadTimeInput.setDefaultInteger(this.copyFrom.reloadTime);
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
    	this.mc.displayGuiScreen(new GuiEditItemTexture(nextScreen, this.addon, this.item, this.isNew, ItemModelManager.ItemModelType.GUN));
    }
	
	@Override
	protected void onToggleShowAdvanced(boolean showAdvanced) {
		if (showAdvanced) {
			this.itemUseEfficiencyInput.setDefaultBoolean(this.itemUseEfficiencyInput.getBoolean());
		}
	}
}
