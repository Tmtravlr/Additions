package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditIngredientOreNBTInput;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.client.ItemStackDisplay;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Lets you build an item stack.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017 
 */
public class GuiComponentIngredientOreNBTInput implements IGuiViewComponent {

	protected GuiEdit editScreen;
	protected GuiTextField selectedText;
	
	protected int x;
	protected int y;
	protected int width;
	protected boolean hidden = false;
	protected String label = "";
	protected boolean required = false;
	protected IngredientOreNBT ingredient = new IngredientOreNBT();
	
	protected ItemStackDisplay stackDisplay = new ItemStackDisplay();
	
	public GuiComponentIngredientOreNBTInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
	}
	
	@Override
	public boolean isRequired() {
		return this.required;
	}

	@Override
	public boolean isHidden() {
		return this.hidden;
	}
	
	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public int getHeight(int left, int right) {
		return 40;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = right - x;

		int itemDisplayTop = this.y + 10;
		
		Gui.drawRect(this.x, itemDisplayTop - 1, this.x + 22, itemDisplayTop + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + 1, itemDisplayTop, this.x + 21, itemDisplayTop + 20, 0xFF000000);
		
		this.selectedText.x = x + 30;
		this.selectedText.y = y + 10;
		this.selectedText.width = right - 90 - x;
		
		this.stackDisplay.updateDisplay(this.ingredient.getMatchingStacks());
		
		String displayText = this.getDisplayText();
		if (!this.selectedText.getText().equals(displayText)) {
			this.selectedText.setText(displayText);
			this.selectedText.setCursorPositionZero();
		}
		
		this.selectedText.drawTextBox();
		
		if (!this.selectedText.getText().isEmpty()) {
			this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}

		this.editScreen.renderItemStack(this.stackDisplay.getDisplayStack(), this.x + 3, itemDisplayTop + 2, mouseX, mouseY, true);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (!this.selectedText.getText().isEmpty()) {
			
			if(mouseX >= deleteX && mouseX < this.selectedText.x + this.selectedText.width && mouseY >= deleteY && mouseY < deleteY + 13) {
				this.setIngredient(null);
			}
		}
		
		if (mouseX >= this.selectedText.x - 30 && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			this.editScreen.mc.displayGuiScreen(this.getUpdateScreen());
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void setDefaultIngredient(IngredientOreNBT group) {
		if (group == null) {
			this.ingredient.clear();
		} else {
			this.ingredient = group;
		}
		this.stackDisplay = new ItemStackDisplay();
	}
	
	public void setIngredient(IngredientOreNBT group) {
		this.setDefaultIngredient(group);
		this.editScreen.notifyHasChanges();
	}
	
	public IngredientOreNBT getIngredient() {
		return this.ingredient;
	}
	
	protected String getDisplayText() {
		return this.stackDisplay.getDisplayText();
	}
	
	protected GuiScreen getUpdateScreen() {
		return new GuiEditIngredientOreNBTInput(this.editScreen, this);
	}
}
