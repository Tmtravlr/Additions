package com.tmtravlr.additions.gui.view.edit.item;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedArmor;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputArmorMaterial;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemArmorTexture;
import com.tmtravlr.additions.type.AdditionTypeItemMaterial;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a new piece of armor or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017
 */
public class GuiEditItemArmor extends GuiEditItem<ItemAddedArmor> {
	
	private GuiComponentDropdownInputArmorMaterial itemMaterialInput;
	private GuiComponentDropdownInput<EntityEquipmentSlot> itemSlotInput;
	private GuiComponentBooleanInput itemColoredInput;
	private GuiComponentBooleanInput itemApplyVanillaAttributesInput;
    
	public GuiEditItemArmor(GuiScreen parentScreen, String title, Addon addon, ItemAddedArmor item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedArmor();
		} else {
			this.item = item;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.itemMaterialInput = new GuiComponentDropdownInputArmorMaterial(I18n.format("gui.edit.item.armor.material.label"), this, this.addon);
		this.itemMaterialInput.setRequired();
		if (!this.isNew) {
			this.itemMaterialInput.setDefaultSelected(AdditionTypeItemMaterial.INSTANCE.getMaterialWithArmorMaterial(this.item.getArmorMaterial()));
		}
		
		this.itemSlotInput = new GuiComponentDropdownInput<EntityEquipmentSlot>(I18n.format("gui.edit.item.armor.slot.label"), this) {
			
			@Override
			public String getSelectionName(EntityEquipmentSlot selected) {
				return selected == null ? "" : selected.getName();
			}
			
		};
		this.itemSlotInput.setRequired();
		this.itemSlotInput.addSelection(EntityEquipmentSlot.HEAD);
		this.itemSlotInput.addSelection(EntityEquipmentSlot.CHEST);
		this.itemSlotInput.addSelection(EntityEquipmentSlot.LEGS);
		this.itemSlotInput.addSelection(EntityEquipmentSlot.FEET);
		if (!this.isNew) {
			this.itemSlotInput.setDefaultSelected(this.item.armorType);
		}
		
		this.itemColoredInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.armor.colored.label"), this);
		if (!this.isNew) {
			this.itemColoredInput.setDefaultBoolean(this.item.colored);
		}
		
		this.itemApplyVanillaAttributesInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.applyVanillaAttributes.label"), this);
		if (!this.isNew) {
			this.itemApplyVanillaAttributesInput.setDefaultBoolean(this.item.applyVanillaAttributes);
		} else {
			this.itemApplyVanillaAttributesInput.setDefaultBoolean(true);
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.itemIdInput);
		this.components.add(this.itemNameInput);
		this.components.add(this.itemShinesInput);
		this.components.add(this.itemMaterialInput);
		this.components.add(this.itemSlotInput);
		this.components.add(this.itemColoredInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemIsBeaconPaymentInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemApplyVanillaAttributesInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
	
	@Override
	public void saveObject() {
		
		if (this.itemMaterialInput.getSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.armor.problem.noMaterial"), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.itemSlotInput.getSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.armor.problem.noSlot"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.item.setArmorMaterial(this.itemMaterialInput.getSelected().getArmorMaterial());
		this.item.setEquipmentSlot(this.itemSlotInput.getSelected());
		this.item.colored = this.itemColoredInput.getBoolean();
		this.item.applyVanillaAttributes = this.itemApplyVanillaAttributesInput.getBoolean();
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.itemMaterialInput.setDefaultSelected(AdditionTypeItemMaterial.INSTANCE.getMaterialWithArmorMaterial(this.copyFrom.getArmorMaterial()));
		this.itemSlotInput.setDefaultSelected(this.copyFrom.armorType);
		this.itemColoredInput.setDefaultBoolean(this.copyFrom.colored);
		this.itemApplyVanillaAttributesInput.setDefaultBoolean(this.copyFrom.applyVanillaAttributes);
		
		super.copyFromOther();
	}
	
	@Override
	public void refreshView() {
		super.refreshView();
		
		this.itemMaterialInput.refreshSelections();
	}
    
	@Override
    protected void openTextureDialogue(GuiScreen nextScreen) {
    	this.mc.displayGuiScreen(new GuiEditItemArmorTexture(nextScreen, this.addon, this.item, this.isNew, this.itemColoredInput.getBoolean()));
    }
}
