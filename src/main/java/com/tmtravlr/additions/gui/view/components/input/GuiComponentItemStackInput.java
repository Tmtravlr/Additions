package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;

import com.tmtravlr.additions.gui.message.edit.GuiMessageBoxEditItemStack;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import com.tmtravlr.additions.util.client.ItemStackDisplay;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

/**
 * Lets you build an item stack.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public class GuiComponentItemStackInput implements IGuiViewComponent {

	public GuiScreen parentScreen;
	public GuiEdit editScreen;
	public GuiTextField selectedText;
	
	private int x;
	private int y;
	private int width;
	private String label = "";
	private boolean required = false;
	private boolean hidden = false;
	private ItemStack itemStack = ItemStack.EMPTY;
	private ItemStackDisplay stackDisplay = new ItemStackDisplay();
	private boolean hasMeta = true;
	private boolean hasAnyDamage = false;
	private boolean hasCount = true;
	private boolean hasTag = true;
	
	public GuiComponentItemStackInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.parentScreen = editScreen;
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
		this.selectedText.width = right - 60 - x;
		
		this.stackDisplay.updateDisplay(this.itemStack);
		
		String displayText = this.itemStack.isEmpty() ? "" : this.stackDisplay.getDisplayText();
		
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

		this.editScreen.renderItemStack(this.stackDisplay.getDisplayStack(), this.x + 3, itemDisplayTop + 2, mouseX, mouseY, true, true);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (!this.selectedText.getText().isEmpty()) {
			
			if(mouseX >= deleteX && mouseX < this.selectedText.x + this.selectedText.width && mouseY >= deleteY && mouseY < deleteY + 13) {
				this.clearItemStack();
			}
		}
		
		if (mouseX >= this.selectedText.x - 30 && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBoxEditItemStack(this.parentScreen, this.editScreen, this.itemStack, this.hasMeta, this.hasCount, this.hasTag, this.hasAnyDamage) {

				@Override
				protected void removeItemStack() {
					GuiComponentItemStackInput.this.clearItemStack();
				}

				@Override
				protected void saveItemStack(ItemStack stack) {
					GuiComponentItemStackInput.this.setItemStack(stack);
				}
				
			});
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void disableMetadata() {
		this.hasMeta = false;
	}
	
	public void enableAnyDamage() {
		this.hasAnyDamage = true;
	}
	
	public void disableCount() {
		this.hasCount = false;
	}
	
	public void disableTag() {
		this.hasTag = false;
	}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void setOtherParentScreen(GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}
	
	public void setDefaultItemStack(ItemStack itemStack) {
		this.itemStack = itemStack == null ? ItemStack.EMPTY : itemStack;
		this.stackDisplay = new ItemStackDisplay();
	}

	private void clearItemStack() {
		this.itemStack = ItemStack.EMPTY;
		this.editScreen.notifyHasChanges();
	}
	
	public ItemStack getItemStack() {
		return this.itemStack;
	}
	
	private void setItemStack(ItemStack stack) {
		this.setDefaultItemStack(stack);
		this.editScreen.notifyHasChanges();
	}
}
