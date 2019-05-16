package com.tmtravlr.additions.gui.view.edit.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedMultiTool;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIngredientOreNBTInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputToolType;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemTexture;
import com.tmtravlr.additions.util.models.ItemModelManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a new multitool or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017
 */
public class GuiEditItemMultiTool extends GuiEditItem<ItemAddedMultiTool> {
	
	private GuiComponentIntegerInput itemDurabilityInput;
	private GuiComponentFloatInput itemEfficientyInput;
	private GuiComponentIntegerInput itemEnchantabilityInput;
	private GuiComponentIntegerInput itemHarvestLevelInput;
	private GuiComponentIntegerInput itemHarvestRadiusInput;
	private GuiComponentIntegerInput itemHarvestDepthInput;
	private GuiComponentIngredientOreNBTInput itemRepairStacksInput;
	private GuiComponentListInput<GuiComponentSuggestionInputToolType> itemToolTypesInput;
    
	public GuiEditItemMultiTool(GuiScreen parentScreen, String title, Addon addon, ItemAddedMultiTool item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedMultiTool();
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

		this.itemEfficientyInput = new GuiComponentFloatInput(I18n.format("gui.edit.itemMaterial.efficiency.label"), this, false);
		this.itemEfficientyInput.setMinimum(0f);
		this.itemEfficientyInput.setRequired();
		this.itemEfficientyInput.setInfo(new TextComponentTranslation("gui.edit.itemMaterial.efficiency.info"));
		if (!this.isNew) {
			this.itemEfficientyInput.setDefaultFloat(this.item.getEfficiency());
		}
		
		this.itemEnchantabilityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.enchantability.label"), this, false);
		this.itemEnchantabilityInput.setMinimum(0);
		this.itemEnchantabilityInput.setInfo(new TextComponentTranslation("gui.edit.item.enchantability.info"));
		if (!this.isNew) {
			this.itemEnchantabilityInput.setDefaultInteger(this.item.enchantability);
		}
		
		this.itemHarvestLevelInput = new GuiComponentIntegerInput(I18n.format("gui.edit.itemMaterial.harvestLevel.label"), this, false);
		this.itemHarvestLevelInput.setMinimum(0);
		this.itemHarvestLevelInput.setInfo(new TextComponentTranslation("gui.edit.itemMaterial.harvestLevel.info"));
		if (!this.isNew) {
			this.itemHarvestLevelInput.setDefaultInteger(this.item.harvestLevel);
		}
		
		this.itemHarvestRadiusInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.multitool.harvestRadius.label"), this, false);
		this.itemHarvestRadiusInput.setMinimum(0);
		this.itemHarvestRadiusInput.setInfo(new TextComponentTranslation("gui.edit.item.multitool.harvestRadius.info"));
		if (!this.isNew) {
			this.itemHarvestRadiusInput.setDefaultInteger(this.item.harvestRadiusWidth);
		}
		
		this.itemHarvestDepthInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.multitool.harvestDepth.label"), this, false);
		this.itemHarvestDepthInput.setMinimum(0);
		this.itemHarvestDepthInput.setInfo(new TextComponentTranslation("gui.edit.item.multitool.harvestDepth.info"));
		if (!this.isNew) {
			this.itemHarvestDepthInput.setDefaultInteger(this.item.harvestDepth);
		}
		
		this.itemRepairStacksInput = new GuiComponentIngredientOreNBTInput(I18n.format("gui.edit.item.repairStacks.label"), this);
		if (!this.isNew) {
			this.itemRepairStacksInput.setDefaultIngredient(this.item.repairStacks);
		}
		
		this.itemToolTypesInput = new GuiComponentListInput<GuiComponentSuggestionInputToolType>(I18n.format("gui.edit.item.multitool.toolTypes.label"), this) {
			
			@Override
			public GuiComponentSuggestionInputToolType createBlankComponent() {
				GuiComponentSuggestionInputToolType suggestionInput = new GuiComponentSuggestionInputToolType("", editScreen);
				return suggestionInput;
			}
			
		};
		if (!this.isNew) {
			this.item.toolClasses.forEach(type -> {
				GuiComponentSuggestionInputToolType suggestionInput = this.itemToolTypesInput.createBlankComponent();
				suggestionInput.setDefaultText(type);
				this.itemToolTypesInput.addDefaultComponent(suggestionInput);
			});
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.itemIdInput);
		this.components.add(this.itemNameInput);
		this.components.add(this.itemShinesInput);
		this.components.add(this.itemDurabilityInput);
		this.components.add(this.itemEfficientyInput);
		this.components.add(this.itemEnchantabilityInput);
		this.components.add(this.itemHarvestLevelInput);
		this.components.add(this.itemRepairStacksInput);
		this.components.add(this.itemToolTypesInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemHarvestRadiusInput);
		this.advancedComponents.add(this.itemHarvestDepthInput);
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
	
	@Override
	public void saveObject() {
		
		this.item.setMaxDamage(this.itemDurabilityInput.getInteger());
		this.item.setEfficiency(this.itemEfficientyInput.getFloat());
		this.item.enchantability = this.itemEnchantabilityInput.getInteger();
		this.item.harvestLevel = this.itemHarvestLevelInput.getInteger();
		this.item.harvestRadiusWidth = this.itemHarvestRadiusInput.getInteger();
		this.item.harvestDepth = this.itemHarvestDepthInput.getInteger();
		this.item.repairStacks = this.itemRepairStacksInput.getIngredient();
		
		List<String> toolTypes = new ArrayList<>();
		this.itemToolTypesInput.getComponents().forEach(input -> {
			if (!input.getText().isEmpty()) {
				toolTypes.add(input.getText());
			}
		});
		this.item.setToolClasses(toolTypes);
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.itemDurabilityInput.setDefaultInteger(this.copyFrom.getMaxDamage());
		this.itemEfficientyInput.setDefaultFloat(this.copyFrom.getEfficiency());
		this.itemEnchantabilityInput.setDefaultInteger(this.copyFrom.enchantability);
		this.itemHarvestLevelInput.setDefaultInteger(this.copyFrom.harvestLevel);
		this.itemHarvestRadiusInput.setDefaultInteger(this.copyFrom.harvestRadiusWidth);
		this.itemHarvestDepthInput.setDefaultInteger(this.copyFrom.harvestDepth);
		this.itemRepairStacksInput.setDefaultIngredient(this.copyFrom.repairStacks);
		
		this.itemToolTypesInput.removeAllComponents();
		this.copyFrom.toolClasses.forEach(type -> {
			GuiComponentSuggestionInputToolType suggestionInput = this.itemToolTypesInput.createBlankComponent();
			suggestionInput.setDefaultText(type);
			this.itemToolTypesInput.addDefaultComponent(suggestionInput);
		});
		
		super.copyFromOther();
	}
    
	@Override
	protected void openTextureDialogue(GuiScreen nextScreen) {
    	this.mc.displayGuiScreen(new GuiEditItemTexture(nextScreen, this.addon, this.item, this.isNew, ItemModelManager.ItemModelType.TOOL));
    }
}
