package com.tmtravlr.additions.gui.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.gui.GuiScrollingListAnyHeight;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;

import net.minecraft.client.renderer.Tessellator;

/**
 * Scrolling list for all the View screens.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiScrollingView extends GuiScrollingListAnyHeight {
	
	private GuiView parent;
	private List<IGuiViewComponent> components = new ArrayList<>();
	private List<IGuiViewComponent> visibleComponents = new ArrayList<>();
	private int listSize = 0;

	public GuiScrollingView(GuiView parent) {
		super(parent.mc, parent.width, parent.height - 70, 30, 0, parent.width, parent.height);
		this.parent = parent;
	}

	@Override
	protected int getSize() {
		return components.size();
	}

	@Override
	protected int getSlotHeight(int slotId, int right) {
		IGuiViewComponent component = this.components.get(slotId);
		int drawLeft = this.left;
		int drawRight = right;
		
		if (drawRight - drawLeft > GuiView.MAX_WIDTH) {
			int drawMiddle = drawLeft + (drawRight - drawLeft)/2;
			drawLeft = drawMiddle - GuiView.MAX_WIDTH/2;
			drawRight = drawMiddle + GuiView.MAX_WIDTH/2;
		}
		
		if (component.getLabel() != null && !component.getLabel().isEmpty()) {
			drawLeft += GuiView.LABEL_OFFSET + 10;
		}
		
		return (component == null || component.isHidden()) ? 0 : component.getHeight(drawLeft, drawRight);
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {}

	@Override
	protected boolean isSelected(int index) {
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.visibleComponents.clear();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawSlot(int slot, int right, int top, int buffer, Tessellator tess) {
		IGuiViewComponent component = components.get(slot);
		
		if (component != null && !component.isHidden()) {
			this.visibleComponents.add(component);
			int drawLeft = this.left;
			int drawRight = right;
			int componentHeight = component.getHeight(drawLeft, drawRight);
			
			if (drawRight - drawLeft > GuiView.MAX_WIDTH) {
				int drawMiddle = drawLeft + (drawRight - drawLeft)/2;
				drawLeft = drawMiddle - GuiView.MAX_WIDTH/2;
				drawRight = drawMiddle + GuiView.MAX_WIDTH/2;
			}
			
			if (component.getLabel() != null && !component.getLabel().isEmpty()) {
				this.parent.drawString(this.parent.getFontRenderer(), component.getLabel(), drawLeft + 10, top + componentHeight / 2 - 5, 0xFFFFFF);
				drawLeft += GuiView.LABEL_OFFSET + 10;
			}
		
			if (component.isRequired()) {
				this.parent.drawString(this.parent.getFontRenderer(), "*", drawLeft - 10, top + componentHeight / 2 - 5, 0xFFFFFF);
			}
		
			component.drawInList(drawLeft, top, drawRight, this.mouseX, this.mouseY);
		}
	}

	public void addComponent(IGuiViewComponent component) {
		this.components.add(component);
	}

	public void removeComponent(IGuiViewComponent component) {
		this.components.remove(component);
	}
	
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (IGuiViewComponent component : this.visibleComponents) {
			component.onMouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {
		for (IGuiViewComponent component : this.visibleComponents) {
			component.onKeyTyped(keyTyped, keyCode);
		}
	}
	
	@Override 
	public void handleMouseInput(int mouseX, int mouseY) throws IOException {
		boolean scrollEditArea = true;
		for (IGuiViewComponent component : this.visibleComponents) {
			if (component.onHandleMouseInput(mouseX, mouseY)) {
				scrollEditArea = false;
			}
		}
		if (scrollEditArea) {
			super.handleMouseInput(mouseX, mouseY);
		}
	}
}
