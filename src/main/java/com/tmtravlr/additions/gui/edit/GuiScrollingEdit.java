package com.tmtravlr.additions.gui.edit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tmtravlr.additions.gui.GuiScrollingListAnyHeight;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiScrollingEdit extends GuiScrollingListAnyHeight {
	
	private GuiEdit parent;
	private ArrayList<IGuiEditComponent> components = new ArrayList<>();
	private int listSize = 0;

	public GuiScrollingEdit(GuiEdit parent) {
		super(parent.mc, parent.width, parent.height - 70, 30, 0, parent.width, parent.height);
		this.parent = parent;
	}

	@Override
	protected int getSize() {
		return components.size();
	}

	@Override
	protected int getSlotHeight(int slotId, int entryRight) {
		IGuiEditComponent component = this.components.get(slotId);
		return component == null ? 0 : component.getHeight(this.left, entryRight);
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {}

	@Override
	protected boolean isSelected(int index) {
		return false;
	}

	@Override
	protected void drawSlot(int slot, int right, int top, int buffer, Tessellator tess) {
		IGuiEditComponent component = components.get(slot);
		if (component != null) {
			int leftStart = this.left;
			int componentHeight = component.getHeight(this.left, right);
			
			if (component.getLabel() != null && !component.getLabel().isEmpty()) {
				this.parent.drawString(this.parent.getFontRenderer(), I18n.format(component.getLabel()), this.left + 10, top + componentHeight / 2 - 5, 0xFFFFFF);
				leftStart += GuiEdit.LABEL_OFFSET + 10;
			}
		
			if (component.isRequired()) {
				this.parent.drawString(this.parent.getFontRenderer(), "*", leftStart - 10, top + componentHeight / 2 - 5, 0xFFFFFF);
			}
		
		
			component.drawInList(leftStart, top, right, this.mouseX, this.mouseY);
		}
	}

	public void addComponent(IGuiEditComponent component) {
		this.components.add(component);
	}

	public void removeComponent(IGuiEditComponent component) {
		this.components.remove(component);
	}
	
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (IGuiEditComponent component : components) {
			component.onMouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {
		for (IGuiEditComponent component : components) {
			component.onKeyTyped(keyTyped, keyCode);
		}
	}
	
	@Override 
	public void handleMouseInput(int mouseX, int mouseY) throws IOException {
		boolean scrollEditArea = true;
		for (IGuiEditComponent component : components) {
			if (component.onHandleMouseInput(mouseX, mouseY)) {
				scrollEditArea = false;
			}
		}
		if (scrollEditArea) {
			super.handleMouseInput(mouseX, mouseY);
		}
	}
}
