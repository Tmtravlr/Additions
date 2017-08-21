package com.tmtravlr.additions.gui.edit.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import com.tmtravlr.additions.gui.edit.GuiEdit;
import com.tmtravlr.additions.gui.edit.IGuiEditComponent;
import com.tmtravlr.additions.gui.edit.components.helpers.GuiScreenDropdown;
import com.tmtravlr.additions.gui.edit.components.helpers.GuiScrollingDropdown;

/**
 * Dropdown list with a text field displaying the selection.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date August 2017
 */
public class GuiDropdownListInput<T> extends Gui implements IGuiEditComponent {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");

	public GuiEdit parent;
	public GuiTextField selectedText;
	public GuiTextField filter;
	public GuiScrollingDropdown dropdown;
	public GuiScreenDropdown dropdownScreen;
	public List<T> selections = new ArrayList<>();
	
	private String label = "";
	private boolean required = false;
	private T selected;
	
	public GuiDropdownListInput(String label, GuiEdit parentScreen) {
		this.parent = parentScreen;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.parent.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
		this.filter = new GuiTextField(0, this.parent.getFontRenderer(), 0, 0, 0, 19);
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
	
	public void setSelected(T selected) {
		this.selected = selected;
		this.selectedText.setText(this.getSelectionName(selected));
		this.selectedText.drawTextBox();
		this.parent.setHasUnsavedChanges();
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
			this.parent.mc.getTextureManager().bindTexture(GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.parent.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
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
				this.selectedText.setText("");
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
		int dropdownHeight = Math.max(22, Math.min(this.selections.size() * 15, 15 * 6));
		int dropdownWidth = this.selectedText.width;
		
		boolean above = this.parent.height - 40 - (this.selectedText.y + this.selectedText.height) < dropdownHeight;
		
		int dropdownX = this.selectedText.x;
		int dropdownY = above ? this.selectedText.y - this.filter.height - dropdownHeight : this.selectedText.y + this.selectedText.height;
		int filterY = above ? this.selectedText.y - this.filter.height : this.selectedText.y + this.selectedText.height;
		
		this.dropdown = this.createScrollingDropdown(dropdownHeight, above);
		
		this.filter.x = dropdownX;
		this.filter.y = filterY;
		this.filter.width = dropdownWidth;
		
		this.dropdownScreen = new GuiScreenDropdown(this, dropdownX, dropdownY, dropdownWidth, dropdownHeight + this.filter.height);
		this.parent.mc.displayGuiScreen(this.dropdownScreen);
	}
	
	protected GuiScrollingDropdown<T> createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown<T>(this, this.selections, dropdownHeight, above);
	}
}
