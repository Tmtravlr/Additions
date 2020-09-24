package com.tmtravlr.additions.gui.view.edit.item.material;

import java.util.ArrayList;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.materials.ArmorMaterialAdded;
import com.tmtravlr.additions.addon.items.materials.ItemMaterialAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputSoundEvent;
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
 * Page for adding or editing an armor material.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017
 */
public class GuiEditArmorMaterial extends GuiEditItemMaterial<ArmorMaterialAdded> {
	
	private GuiComponentIntegerInput baseArmorDurabilityInput;
	private GuiComponentFloatInput toughnessInput;
	private GuiComponentIntegerInput helmetArmorInput;
	private GuiComponentIntegerInput chestplateArmorInput;
	private GuiComponentIntegerInput leggingsArmorInput;
	private GuiComponentIntegerInput bootsArmorInput;
    private GuiComponentDropdownInputSoundEvent equipSoundInput;
    
	public GuiEditArmorMaterial(GuiScreen parentScreen, String title, Addon addon, ArmorMaterialAdded material) {
		super(parentScreen, title, addon, material);
	}

	@Override
	public void initComponents() {
		
		super.initComponents();
		
		this.baseArmorDurabilityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.itemMaterial.baseArmorDurability.label"), this, false);
		this.baseArmorDurabilityInput.setRequired();
		this.baseArmorDurabilityInput.setMinimum(0);
		this.baseArmorDurabilityInput.setInfo(new TextComponentTranslation("gui.edit.itemMaterial.baseArmorDurability.info"));
		if (!this.isNew) {
			this.baseArmorDurabilityInput.setDefaultInteger(this.oldMaterial.getBaseArmorDurability());
		}
		
		this.toughnessInput = new GuiComponentFloatInput(I18n.format("gui.edit.itemMaterial.toughness.label"), this, true);
		this.toughnessInput.setRequired();
		this.toughnessInput.setMinimum(0);
		if (!this.isNew) {
			this.toughnessInput.setDefaultFloat(this.oldMaterial.getToughness());
		}
		
		this.helmetArmorInput = new GuiComponentIntegerInput(I18n.format("gui.edit.itemMaterial.helmetArmor.label"), this, false);
		this.helmetArmorInput.setRequired();
		this.helmetArmorInput.setMinimum(0);
		if (!this.isNew) {
			this.helmetArmorInput.setDefaultInteger(this.oldMaterial.getHelmetArmor());
		}
		
		this.chestplateArmorInput = new GuiComponentIntegerInput(I18n.format("gui.edit.itemMaterial.chestplateArmor.label"), this, false);
		this.chestplateArmorInput.setRequired();
		this.chestplateArmorInput.setMinimum(0);
		if (!this.isNew) {
			this.chestplateArmorInput.setDefaultInteger(this.oldMaterial.getChestplateArmor());
		}
		
		this.leggingsArmorInput = new GuiComponentIntegerInput(I18n.format("gui.edit.itemMaterial.leggingsArmor.label"), this, false);
		this.leggingsArmorInput.setRequired();
		this.leggingsArmorInput.setMinimum(0);
		if (!this.isNew) {
			this.leggingsArmorInput.setDefaultInteger(this.oldMaterial.getLeggingsArmor());
		}
		
		this.bootsArmorInput = new GuiComponentIntegerInput(I18n.format("gui.edit.itemMaterial.bootsArmor.label"), this, false);
		this.bootsArmorInput.setRequired();
		this.bootsArmorInput.setMinimum(0);
		if (!this.isNew) {
			this.bootsArmorInput.setDefaultInteger(this.oldMaterial.getBootsArmor());
		}
		
		this.equipSoundInput = new GuiComponentDropdownInputSoundEvent(I18n.format("gui.edit.itemMaterial.equipSound.label"), this.addon, this);
		this.equipSoundInput.setRequired();
		if (!this.isNew) {
			this.equipSoundInput.setDefaultSelected(this.oldMaterial.getEquipSound());
		} else {
			this.equipSoundInput.setDefaultSelected(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC);
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.idInput);
		this.components.add(this.enchantabilityInput);
		this.components.add(this.repairStackInput);
		this.components.add(this.baseArmorDurabilityInput);
		this.components.add(this.toughnessInput);
		this.components.add(this.helmetArmorInput);
		this.components.add(this.chestplateArmorInput);
		this.components.add(this.leggingsArmorInput);
		this.components.add(this.bootsArmorInput);
		this.components.add(this.equipSoundInput);
		
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
        
        if (this.equipSoundInput.getSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.itemMaterial.problem.title"), new TextComponentTranslation("gui.edit.itemMaterial.problem.noEquipSound", name), I18n.format("gui.buttons.back")));
			return;
        }
        
        int enchantability = this.enchantabilityInput.getInteger();
		ItemStack repairStack = this.repairStackInput.getItemStack();
        int baseArmorDurability = this.baseArmorDurabilityInput.getInteger();
        float toughness = this.toughnessInput.getFloat();
        int helmetArmor = this.helmetArmorInput.getInteger();
        int chestplateArmor = this.chestplateArmorInput.getInteger();
        int leggingsArmor = this.leggingsArmorInput.getInteger();
        int bootsArmor = this.bootsArmorInput.getInteger();
        SoundEvent equipSound = this.equipSoundInput.getSelected();
        
        ArmorMaterialAdded armorMaterial;
        
        if (this.isNew) {
		    armorMaterial = new ArmorMaterialAdded(EnumHelper.addArmorMaterial(name, "", baseArmorDurability, new int[]{bootsArmor, leggingsArmor, chestplateArmor, helmetArmor}, enchantability, equipSound, toughness));
        } else {
        	armorMaterial = this.oldMaterial;
        	
        	armorMaterial.setEnchantability(enchantability);
        	armorMaterial.setRepairStack(repairStack);
        	armorMaterial.setBaseArmorDurability(baseArmorDurability);
        	armorMaterial.setToughness(toughness);
        	armorMaterial.setArmor(helmetArmor, chestplateArmor, leggingsArmor, bootsArmor);
        	armorMaterial.setEquipSound(equipSound);
        }
        
        armorMaterial.setRepairStack(this.repairStackInput.getItemStack());
		
        AdditionTypeItemMaterial.INSTANCE.saveAddition(addon, armorMaterial);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.edit.itemMaterial.success.title"), new TextComponentTranslation("gui.edit.itemMaterial.success.message"), I18n.format("gui.buttons.continue")));
	}
	
	@Override
	public void refreshView() {
		this.equipSoundInput.refreshSelections();
	}
	
	public void copyFromOther() {
		this.baseArmorDurabilityInput.setDefaultInteger(this.copyFrom.getBaseArmorDurability());
		this.toughnessInput.setDefaultFloat(this.copyFrom.getToughness());
		this.helmetArmorInput.setDefaultInteger(this.copyFrom.getHelmetArmor());
		this.chestplateArmorInput.setDefaultInteger(this.copyFrom.getChestplateArmor());
		this.leggingsArmorInput.setDefaultInteger(this.copyFrom.getLeggingsArmor());
		this.bootsArmorInput.setDefaultInteger(this.copyFrom.getBootsArmor());
		this.equipSoundInput.setDefaultSelected(this.copyFrom.getEquipSound());
		
		super.copyFromOther();
	}

}
