package com.tmtravlr.additions.gui.view.edit.item;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.IItemAddedTool;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputToolMaterial;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemTexture;
import com.tmtravlr.additions.type.AdditionTypeItemMaterial;
import com.tmtravlr.additions.util.models.ItemModelManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a tool.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017
 */
public abstract class GuiEditItemTool<T extends IItemAdded & IItemAddedTool> extends GuiEditItem<T> {
	
	protected GuiComponentDropdownInputToolMaterial itemMaterialInput;
	protected GuiComponentBooleanInput itemApplyVanillaAttributesInput;
    
	public GuiEditItemTool(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title, addon);
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.itemMaterialInput = new GuiComponentDropdownInputToolMaterial(I18n.format("gui.edit.item.tool.material.label"), this, this.addon);
		this.itemMaterialInput.setRequired();
		if (!this.isNew) {
			this.itemMaterialInput.setDefaultSelected(AdditionTypeItemMaterial.INSTANCE.getMaterialWithToolMaterial(this.item.getToolMaterial()));
		}
		
		this.itemApplyVanillaAttributesInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.applyVanillaAttributes.label"), this);
		if (!this.isNew) {
			this.itemApplyVanillaAttributesInput.setDefaultBoolean(this.item.shouldApplyVanillaAttributes());
		} else {
			this.itemApplyVanillaAttributesInput.setDefaultBoolean(true);
		}
	}
	
	@Override
	public void saveObject() {
		
		if (this.itemMaterialInput.getSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.tool.problem.noMaterial"), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.item.setToolMaterial(this.itemMaterialInput.getSelected().getToolMaterial());
		this.item.setApplyVanillaAttributes(this.itemApplyVanillaAttributesInput.getBoolean());
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.itemMaterialInput.setDefaultSelected(AdditionTypeItemMaterial.INSTANCE.getMaterialWithToolMaterial(this.copyFrom.getToolMaterial()));
		this.itemApplyVanillaAttributesInput.setDefaultBoolean(this.copyFrom.shouldApplyVanillaAttributes());
		
		super.copyFromOther();
	}
	
	@Override
	public void refreshView() {
		super.refreshView();
		
		this.itemMaterialInput.refreshSelections();
	}
    
	@Override
	protected void openTextureDialogue(GuiScreen nextScreen) {
    	this.mc.displayGuiScreen(new GuiEditItemTexture(nextScreen, this.addon, this.item, this.isNew, ItemModelManager.ItemModelType.TOOL));
    }
}
