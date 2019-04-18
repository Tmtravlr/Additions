package com.tmtravlr.additions.gui.view.edit.item;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedShield;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIngredientOreNBTInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputSoundEvent;
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
 * @since November 2017
 */
public class GuiEditItemShield extends GuiEditItem<ItemAddedShield> {
	
	private GuiComponentIntegerInput itemDurabilityInput;
	private GuiComponentIntegerInput itemEnchantabilityInput;
	private GuiComponentIngredientOreNBTInput itemRepairStacksInput;
	private GuiComponentFloatInput itemBashDamageInput;
	private GuiComponentIntegerInput itemBashCooldownInput;
	private GuiComponentFloatInput itemEfficiencyMultiplierInput;
    private GuiComponentDropdownInputSoundEvent itemBashSwingSoundInput;
    private GuiComponentDropdownInputSoundEvent itemBashHitSoundInput;

	public GuiEditItemShield(GuiScreen parentScreen, String title, Addon addon, ItemAddedShield item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedShield();
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

		this.itemBashDamageInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.shield.bashDamage.label"), this, false);
		this.itemBashDamageInput.setMinimum(0f);
		if (!this.isNew) {
			this.itemBashDamageInput.setDefaultFloat(this.item.bashDamage);
		}

		this.itemBashCooldownInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.shield.bashCooldown.label"), this, false);
		this.itemBashCooldownInput.setMinimum(0);
		if (!this.isNew) {
			this.itemBashCooldownInput.setDefaultInteger(this.item.bashCooldown);
		} else {
			this.itemBashCooldownInput.setDefaultInteger(60);
		}
		
		this.itemEfficiencyMultiplierInput = new GuiComponentFloatInput(I18n.format("gui.edit.item.efficiencyMultiplier.label"), this, false);
		this.itemEfficiencyMultiplierInput.setInfo(new TextComponentTranslation("gui.edit.item.efficiencyMultiplier.info"));
		this.itemEfficiencyMultiplierInput.setMaximum(1);
		this.itemEfficiencyMultiplierInput.setMinimum(0);
		if (!this.isNew) {
			this.itemEfficiencyMultiplierInput.setDefaultFloat(this.item.efficiencyMultiplier);
		} else {
			this.itemEfficiencyMultiplierInput.setDefaultFloat(0.5f);
		}

		this.itemBashSwingSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.item.shield.bashSwingSound.label"), this, this.addon);
		if (!this.isNew) {
			this.itemBashSwingSoundInput.setDefaultSelected(this.item.bashSwingSound);
		} else {
			this.itemBashSwingSoundInput.setDefaultSelected(SoundEvents.ITEM_ARMOR_EQUIP_IRON);
		}

		this.itemBashHitSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.item.shield.bashHitSound.label"), this, this.addon);
		if (!this.isNew) {
			this.itemBashHitSoundInput.setDefaultSelected(this.item.bashHitSound);
		} else {
			this.itemBashHitSoundInput.setDefaultSelected(SoundEvents.ITEM_SHIELD_BLOCK);
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
		this.components.add(this.itemBashDamageInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemBashCooldownInput);
		this.advancedComponents.add(this.itemEfficiencyMultiplierInput);
		this.advancedComponents.add(this.itemBashSwingSoundInput);
		this.advancedComponents.add(this.itemBashHitSoundInput);
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
	
	@Override
	public void saveObject() {
		
		this.item.setMaxDamage(this.itemDurabilityInput.getInteger());
		this.item.enchantability = this.itemEnchantabilityInput.getInteger();
		this.item.repairStacks = this.itemRepairStacksInput.getIngredient();
		this.item.bashDamage = this.itemBashDamageInput.getFloat();
		this.item.bashCooldown = this.itemBashCooldownInput.getInteger();
		this.item.efficiencyMultiplier = this.itemEfficiencyMultiplierInput.getFloat();
		this.item.bashSwingSound = this.itemBashSwingSoundInput.getSelected();
		this.item.bashHitSound = this.itemBashHitSoundInput.getSelected();
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.itemDurabilityInput.setDefaultInteger(this.copyFrom.getMaxDamage());
		this.itemEnchantabilityInput.setDefaultInteger(this.copyFrom.enchantability);
		this.itemRepairStacksInput.setDefaultIngredient(this.copyFrom.repairStacks);
		this.itemBashDamageInput.setDefaultFloat(this.copyFrom.bashDamage);
		this.itemBashCooldownInput.setDefaultInteger(this.copyFrom.bashCooldown);
		this.itemEfficiencyMultiplierInput.setDefaultFloat(this.copyFrom.efficiencyMultiplier);
		this.itemBashSwingSoundInput.setDefaultSelected(this.copyFrom.bashSwingSound);
		this.itemBashHitSoundInput.setDefaultSelected(this.copyFrom.bashHitSound);
		
		super.copyFromOther();
	}
    
	@Override
	protected void openTextureDialogue() {
    	GuiScreen nextScreen;
    	if (this.isNew) {
    		 nextScreen = getItemCreatedPopup();
    	} else {
    		nextScreen = this;
    	}
    	
    	this.mc.displayGuiScreen(new GuiEditItemTexture(nextScreen, this.addon, this.item, this.isNew, ItemModelManager.ItemModelType.SHIELD));
    }
}
