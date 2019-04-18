package com.tmtravlr.additions.gui.view.edit.update;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIngredientModifierInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiEditIngredientModifierInput extends GuiEditIngredientOreNBT {
	private GuiComponentIngredientModifierInput parent;
	private GuiComponentIntegerInput modifierInput;
	
	public GuiEditIngredientModifierInput(GuiScreen parentScreen, GuiComponentIngredientModifierInput parent) {
		super(parentScreen);
		this.parent = parent;
	}
	
	@Override
	public void initComponents() {
		super.initComponents();
		
		this.oreDictInput.setDefaultText(parent.getIngredient().getOreName());
		
		if (!this.parent.getIngredient().getStackList().isEmpty()) {
			this.parent.getIngredient().getStackList().forEach(toAdd -> {
				GuiComponentItemStackInput input = this.itemGroupInput.createBlankComponent();
				input.setDefaultItemStack(toAdd);
				this.itemGroupInput.addDefaultComponent(input);
			});
		}
		
		this.modifierInput = new GuiComponentIntegerInput(I18n.format("gui.edit.recipe.crafting.modify.label"), this, true);
		this.modifierInput.setDefaultInteger(this.parent.getModifier());
		
		this.components.add(modifierInput);
	}

	@Override
    public void saveObject() {
		super.saveObject();
		
		this.parent.setModifier(this.modifierInput.getInteger());
    	this.mc.displayGuiScreen(this.parentScreen);
	}
	
	@Override
	protected void handleIngredientSaved(IngredientOreNBT group) {
        this.parent.setItemStackGroup(group);
	}

}
