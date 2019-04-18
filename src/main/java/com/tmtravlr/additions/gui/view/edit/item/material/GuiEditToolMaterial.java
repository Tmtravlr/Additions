package com.tmtravlr.additions.gui.view.edit.item.material;

import java.util.ArrayList;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.materials.ItemMaterialAdded;
import com.tmtravlr.additions.addon.items.materials.ToolMaterialAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInput;
import com.tmtravlr.additions.type.AdditionTypeItemMaterial;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding or editing a tool material.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017
 */
public class GuiEditToolMaterial extends GuiEditItemMaterial<ToolMaterialAdded> {
	
	private GuiComponentIntegerInput baseToolDurabilityInput;
	private GuiComponentIntegerInput harvestLevelInput;
	private GuiComponentFloatInput efficiencyInput;
	private GuiComponentFloatInput baseAttackDamageInput;
    
	public GuiEditToolMaterial(GuiScreen parentScreen, String title, Addon addon, ToolMaterialAdded material) {
		super(parentScreen, title, addon, material);
	}

	@Override
	public void initComponents() {
		
		super.initComponents();
		
		this.baseToolDurabilityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.itemMaterial.baseToolDurability.label"), this, false);
		this.baseToolDurabilityInput.setRequired();
		this.baseToolDurabilityInput.setMinimum(0);
		this.baseToolDurabilityInput.setInfo(new TextComponentTranslation("gui.edit.itemMaterial.baseToolDurability.info"));
		if (!this.isNew) {
			this.baseToolDurabilityInput.setDefaultInteger(this.oldMaterial.getBaseToolDurability());
		}
		
		this.harvestLevelInput = new GuiComponentIntegerInput(I18n.format("gui.edit.itemMaterial.harvestLevel.label"), this, false);
		this.harvestLevelInput.setRequired();
		this.harvestLevelInput.setMinimum(0);
		this.harvestLevelInput.setInfo(new TextComponentTranslation("gui.edit.itemMaterial.harvestLevel.info"));
		if (!this.isNew) {
			this.harvestLevelInput.setDefaultInteger(this.oldMaterial.getHarvestLevel());
		}
		
		this.efficiencyInput = new GuiComponentFloatInput(I18n.format("gui.edit.itemMaterial.efficiency.label"), this, false);
		this.efficiencyInput.setRequired();
		this.efficiencyInput.setMinimum(0f);
		this.efficiencyInput.setInfo(new TextComponentTranslation("gui.edit.itemMaterial.efficiency.info"));
		if (!this.isNew) {
			this.efficiencyInput.setDefaultFloat(this.oldMaterial.getEfficiency());
		}
		
		this.baseAttackDamageInput = new GuiComponentFloatInput(I18n.format("gui.edit.itemMaterial.baseAttackDamage.label"), this, true);
		this.baseAttackDamageInput.setRequired();
		this.baseAttackDamageInput.setInfo(new TextComponentTranslation("gui.edit.itemMaterial.baseAttackDamage.info"));
		if (!this.isNew) {
			this.baseAttackDamageInput.setDefaultFloat(this.oldMaterial.getBaseAttackDamage());
		}
		
		this.components.add(this.idInput);
		this.components.add(this.enchantabilityInput);
		this.components.add(this.repairStackInput);
		this.components.add(this.baseToolDurabilityInput);
		this.components.add(this.harvestLevelInput);
		this.components.add(this.efficiencyInput);
		this.components.add(this.baseAttackDamageInput);
		
	}
	
	@Override
	public void saveObject() {
		String name = this.isNew ? AdditionsMod.MOD_ID + "-" + this.addon.id + "-" + this.idInput.getText() : this.oldMaterial.getId();
		
		if (this.idInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.itemMaterial.problem.title"), new TextComponentTranslation("gui.edit.problem.noId", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.isNew && AdditionTypeItemMaterial.INSTANCE.hasItemMaterialWithId(this.addon, name)) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.itemMaterial.problem.title"), new TextComponentTranslation("gui.edit.itemMaterial.problem.duplicate", name), I18n.format("gui.buttons.back")));
			return;
		}
        
        int enchantability = this.enchantabilityInput.getInteger();
		ItemStack repairStack = this.repairStackInput.getItemStack();
        int harvestLevel = this.harvestLevelInput.getInteger();
        int baseToolDurability = this.baseToolDurabilityInput.getInteger();
        float efficiency = this.efficiencyInput.getFloat();
        float baseAttackDamage = this.baseAttackDamageInput.getFloat();
        
        ToolMaterialAdded toolMaterial;
        
        if (this.isNew) {
		    toolMaterial = new ToolMaterialAdded(EnumHelper.addToolMaterial(name.toUpperCase(), harvestLevel, baseToolDurability, efficiency, baseAttackDamage, enchantability));
        } else {
        	toolMaterial = this.oldMaterial;
        	
        	toolMaterial.setEnchantability(enchantability);
        	toolMaterial.setRepairStack(repairStack);
        	toolMaterial.setHarvestLevel(harvestLevel);
        	toolMaterial.setBaseToolDurability(baseToolDurability);
        	toolMaterial.setEfficiency(efficiency);
        	toolMaterial.setBaseAttackDamage(baseAttackDamage);
        }
        
        toolMaterial.setRepairStack(this.repairStackInput.getItemStack());
		
        AdditionTypeItemMaterial.INSTANCE.saveAddition(addon, toolMaterial);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.edit.itemMaterial.success.title"), new TextComponentTranslation("gui.edit.itemMaterial.success.message"), I18n.format("gui.buttons.continue")));
	}
	
	public void copyFromOther() {
		
		this.baseToolDurabilityInput.setDefaultInteger(this.copyFrom.getBaseToolDurability());
		this.harvestLevelInput.setDefaultInteger(this.copyFrom.getHarvestLevel());
		this.efficiencyInput.setDefaultFloat(this.copyFrom.getEfficiency());
		this.baseAttackDamageInput.setDefaultFloat(this.copyFrom.getBaseAttackDamage());
		
		super.copyFromOther();
	}

}
