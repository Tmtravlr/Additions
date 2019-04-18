package com.tmtravlr.additions.gui.view.components.input;

import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditIngredientModifierInput;

import net.minecraft.client.gui.GuiScreen;

/**
 * Lets you build an ingredient that has a modifier amount.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since April 2019 
 */
public class GuiComponentIngredientModifierInput extends GuiComponentIngredientOreNBTInput {
	
	private int modifier = 0;
	
	public GuiComponentIngredientModifierInput(String label, GuiEdit editScreen) {
		super(label, editScreen);
	}
	
	@Override
	protected String getDisplayText() {
		return (this.modifier < 0 ? "" : "+") + this.modifier + ": " + super.getDisplayText();
	}

	@Override
	protected GuiScreen getUpdateScreen() {
		return new GuiEditIngredientModifierInput(this.editScreen, this);
	}
	
	public void setDefaultModifier(int modifier) {
		this.modifier = modifier;
	}
	
	public void setModifier(int modifier) {
		this.setDefaultModifier(modifier);
		this.editScreen.notifyHasChanges();
	}
	
	public int getModifier() {
		return this.modifier;
	}
}
