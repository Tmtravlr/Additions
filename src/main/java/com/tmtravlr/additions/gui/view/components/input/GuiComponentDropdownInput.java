package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.helpers.GuiScreenDropdown;
import com.tmtravlr.additions.gui.view.components.helpers.GuiScrollingDropdown;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Dropdown list with a text field displaying the selection.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017
 */
public class GuiComponentDropdownInput<T> extends Gui implements IGuiViewComponent {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");

	public GuiEdit editScreen;
	public GuiTextField selectedText;
	public GuiTextField filter;
	public GuiScrollingDropdown dropdown;
	public GuiScreenDropdown dropdownScreen;
	public List<T> selections = new ArrayList<>();
	
	private String label = "";
	private boolean required = false;
	private T selected;
	
	public GuiComponentDropdownInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
		this.filter = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 19);
		this.filter.setMaxStringLength(1024);
	}
	
	public void setSelections(Collection<T> selections) {
		this.selections = new ArrayList<>(selections);
	}
	
	public void addSelection(T selection) {
		this.selections.add(selection);
	}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void setDefaultSelected(T selected) {
		this.selected = selected;
		if (this.selected == null) {
			this.selectedText.setText("");
		} else {
			this.selectedText.setText(this.getSelectionName(selected));
		}
		this.selectedText.setCursorPositionZero();
	}
	
	public void setSelected(T selected) {
		this.setDefaultSelected(selected);
		this.selectedText.drawTextBox();
		this.editScreen.notifyHasChanges();
	}
	
	public String getSelectionName(T selected) {
		return selected == null ? "" : selected.toString();
	}
	
	public T getSelected() {
		return this.selected;
	}
	
	@Override
	public int getHeight(int left, int right) {
		return 40;
	}
	
	@Override
	public String getLabel() {
		return this.label;
	}
	
	@Override
	public boolean isRequired() {
		return this.required;
	}
	
	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		
		this.selectedText.x = x;
		this.selectedText.y = y + 10;
		this.selectedText.width = right - 60 - x;
		
		this.selectedText.drawTextBox();
		
		if (!this.selectedText.getText().isEmpty()) {
			this.editScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}
	}
	
	@Override
	public void onKeyTyped(char keyTyped, int keyCode) {
		this.selectedText.textboxKeyTyped(keyTyped, keyCode);
	}
	
	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	    int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (!this.selectedText.getText().isEmpty()) {
			
			if(mouseX >= deleteX && mouseX < this.selectedText.x + this.selectedText.width && mouseY >= deleteY && mouseY < deleteY + 13) {
				this.setSelected(null);
			}
		}
		
		if (mouseX >= this.selectedText.x && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			this.createDropdown();
			this.filter.setFocused(true);
		}
	}
	
	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}
	
	protected void createDropdown() {
		int dropdownHeight = Math.max(22, Math.min(this.selections.size() * 20, 20 * 6));
		int dropdownWidth = this.selectedText.width;
		
		boolean above = this.editScreen.height - 40 - (this.selectedText.y + this.selectedText.height) < dropdownHeight;
		
		int dropdownX = this.selectedText.x;
		int dropdownY = above ? this.selectedText.y - this.filter.height - dropdownHeight : this.selectedText.y + this.selectedText.height;
		int filterY = above ? this.selectedText.y - this.filter.height : this.selectedText.y + this.selectedText.height;
		
		this.dropdown = this.createScrollingDropdown(dropdownHeight, above);
		
		this.filter.x = dropdownX;
		this.filter.y = filterY;
		this.filter.width = dropdownWidth;
		
		this.dropdownScreen = new GuiScreenDropdown(this, this.editScreen.mc.currentScreen, dropdownX, dropdownY, dropdownWidth, dropdownHeight + this.filter.height);
		this.editScreen.mc.displayGuiScreen(this.dropdownScreen);
	}
	
	protected GuiScrollingDropdown<T> createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown<T>(this, this.selections, dropdownHeight, above);
	}
}
