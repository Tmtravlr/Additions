package com.tmtravlr.additions.gui.view.components.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.gui.view.components.input.GuiComponentDropdownInput;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;

/**
 * Shows a scrolling list, displaying the given type of object.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiScrollingDropdown<T> extends GuiScrollingList {

	public GuiComponentDropdownInput parentInput;
	public List<T> selections;
	public List<T> selectionDisplay;
	public String prevFilter = "";
	
	public GuiScrollingDropdown(GuiComponentDropdownInput parentInput, List<T> selections, int height, boolean above) {
		super(parentInput.editScreen.mc, parentInput.selectedText.width, height,
				above ? parentInput.selectedText.y - height - 20 : parentInput.selectedText.y + parentInput.selectedText.height + 20, 
				above ? parentInput.selectedText.y - 20 : parentInput.selectedText.y + parentInput.selectedText.height + height + 20, 
				parentInput.selectedText.x, 18, parentInput.editScreen.width, parentInput.editScreen.height);
		
		this.parentInput = parentInput;
		this.selections = selections;
		this.selectionDisplay = this.selections;
	}

	@Override
	protected int getSize() {
		return Math.max(1, this.selectionDisplay.size());
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		if (!this.selectionDisplay.isEmpty()) {
			this.parentInput.setSelected(this.selectionDisplay.get(index));
			this.parentInput.dropdownScreen.closeOnMouseUp();
		}
	}

	@Override
	protected boolean isSelected(int index) {
		if (!this.selectionDisplay.isEmpty()) {
			return this.selectionDisplay.get(index).equals(this.parentInput.getSelected());
		}
		return false;
	}

	@Override
	protected void drawBackground() {}

	@Override
	protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
		if (!this.selectionDisplay.isEmpty()) {
			T toDisplay = this.selectionDisplay.get(slot);
			if (toDisplay != null) {
				this.parentInput.editScreen.getFontRenderer().drawString(this.parentInput.getSelectionName(toDisplay), this.left + 5, top + 2, 0xFFFFFF);
			}
		} else {
			this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.dropdown.nothingToShow"), this.left + 5, top + 2, 0x888888);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Gui.drawRect(this.left - 1, this.top - 1, this.left + this.listWidth + 1, this.top + this.listHeight + 1, 0xFFA0A0A0);
		
		
		if (!this.parentInput.filter.getText().equals(prevFilter)) {
			String filter = this.parentInput.filter.getText();
			this.selectionDisplay = selections.stream().filter(s -> s == null ? false : this.parentInput.getSelectionName(s).toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());
			this.prevFilter = filter;
		}
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public boolean isHovering() {
		return this.mouseX >= this.left && this.mouseX <= this.left + this.listWidth && this.mouseY >= this.top && this.mouseY <= this.bottom;
	}
}
