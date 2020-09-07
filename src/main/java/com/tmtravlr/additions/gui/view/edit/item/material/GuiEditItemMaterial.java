package com.tmtravlr.additions.gui.view.edit.item.material;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.materials.ItemMaterialAdded;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding or editing a tool/armor material, mimicking how the vanilla tool/armor materials works.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017
 */
public abstract class GuiEditItemMaterial<T extends ItemMaterialAdded> extends GuiEdit {
	
	protected Addon addon;
	
	protected boolean isNew;
	protected T oldMaterial;
	protected T copyFrom;

	protected GuiComponentStringInput idInput;
	protected GuiComponentIntegerInput enchantabilityInput;
	protected GuiComponentItemStackInput repairStackInput;
    
	public GuiEditItemMaterial(GuiScreen parentScreen, String title, Addon addon, T material) {
		super(parentScreen, title);
		this.addon = addon;
		this.isNew = material == null;
		this.oldMaterial = material;
	}

	@Override
	public void initComponents() {
		
		this.idInput = new GuiComponentStringInput(I18n.format("gui.edit.itemMaterial.id.label"), this);
		if (this.isNew) {
			this.idInput.setRequired();
			this.idInput.setMaxStringLength(32);
			this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
			this.idInput.setValidator(input -> input.matches("[a-z0-9\\_]*"));
		} else {
			this.idInput.setEnabled(false);
			this.idInput.setMaxStringLength(1024);
			this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
			this.idInput.setDefaultText(this.oldMaterial.getId());
		}
		
		this.enchantabilityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.enchantability.label"), this, false);
		this.enchantabilityInput.setRequired();
		this.enchantabilityInput.setMinimum(0);
		this.enchantabilityInput.setInfo(new TextComponentTranslation("gui.edit.item.enchantability.info"));
		if (!this.isNew) {
			this.enchantabilityInput.setDefaultInteger(this.oldMaterial.getEnchantability());
		}
		
		this.repairStackInput = new GuiComponentItemStackInput(I18n.format("gui.edit.itemMaterial.repairStack.label"), this);
		this.repairStackInput.disableCount();
		this.repairStackInput.enableAnyDamage();
		this.repairStackInput.disableTag();
		if (!this.isNew) {
			this.repairStackInput.setDefaultItemStack(this.oldMaterial.getRepairStack());
		}
		
	}
	
	public void copyFrom(T material) {
		this.copyFrom = material;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	public void copyFromOther() {
		this.enchantabilityInput.setDefaultInteger(this.copyFrom.getEnchantability());
		this.repairStackInput.setDefaultItemStack(this.copyFrom.getRepairStack());
		
		this.copyFrom = null;
	}

}
