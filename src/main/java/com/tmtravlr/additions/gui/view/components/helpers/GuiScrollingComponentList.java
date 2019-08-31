package com.tmtravlr.additions.gui.view.components.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.gui.GuiScrollingListAnyHeight;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

/**
 * Shows a scrolling list of input components, which can be added or removed.
 * Useful for item lore and lists like that.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiScrollingComponentList<T extends IGuiViewComponent> extends GuiScrollingListAnyHeight {
	private static final int MODIFY_COMPONENT_WIDTH = 20;
    private static final int MIN_COMPONENT_HEIGHT = 40;
    
	private GuiComponentListInput<T> parentInput;
	public List<ComponentDisplay> displayComponents = new ArrayList<>();
	public List<IGuiViewComponent> visibleComponents = new ArrayList<>();
	
	public GuiScrollingComponentList(GuiComponentListInput parentInput, int x, int y, int width, int height) {
		super(parentInput.editScreen.mc, width, height, y, x, parentInput.editScreen.width, parentInput.editScreen.height);
		this.parentInput = parentInput;
	}

	@Override
	protected int getSize() {
		return this.displayComponents.size();
	}

	@Override
	protected void elementClicked(int slot, boolean doubleClick) {
		ComponentDisplay componentDisplay = displayComponents.get(slot);
		if (componentDisplay.component == this.parentInput.addNewComponent) {
			//Add new component
			this.parentInput.addNewComponent();
		} else if (componentDisplay.component == this.parentInput.insertNewComponent) {
			//Insert a component here
			int index = slot / 2;
			this.parentInput.insertNewComponent(index);
		} else {
			boolean firstComponent = slot == 1;
			boolean lastComponent = slot == this.displayComponents.size() - 2;
			int entryRight = this.left + this.listWidth - 7;
			int height = this.getSlotHeight(slot, entryRight);
			int index = slot / 2;
			
			if (!firstComponent && CommonGuiUtils.isMouseWithin(this.mouseX, this.mouseY, this.left, componentDisplay.top, MODIFY_COMPONENT_WIDTH, 12)) {
				this.parentInput.moveComponentUp(index);
			}
			
			if (CommonGuiUtils.isMouseWithin(this.mouseX, this.mouseY, this.left, componentDisplay.top + 12, MODIFY_COMPONENT_WIDTH, height - 24)) {
				this.parentInput.removeComponent(index);
			}
			
			if (!lastComponent && CommonGuiUtils.isMouseWithin(this.mouseX, this.mouseY, this.left, componentDisplay.top + height - 12, MODIFY_COMPONENT_WIDTH, 12)) {
				this.parentInput.moveComponentDown(index);
			}
		}
	}

	@Override
	protected boolean isSelected(int slot) {
		return false;
	}

	@Override
	protected int getSlotHeight(int slot, int entryRight) {
		IGuiViewComponent component = this.displayComponents.get(slot).component;
		
		if (component == this.parentInput.addNewComponent || component == this.parentInput.insertNewComponent) {
			return component.getHeight(this.left, entryRight);
		} else {
			return Math.max(component.getHeight(this.left, entryRight), MIN_COMPONENT_HEIGHT);
		}
	}

	@Override
	protected void drawSlot(int slot, int right, int top, int buffer, Tessellator tess) {
		ComponentDisplay componentDisplay = this.displayComponents.get(slot);
		this.visibleComponents.add(componentDisplay.component);
		int height = this.getSlotHeight(slot, right);
		componentDisplay.top = top;
		
		if (componentDisplay.component == this.parentInput.addNewComponent) {
			
			if (CommonGuiUtils.isMouseWithin(this.mouseX, this.mouseY, this.left, top, right - this.left, height - 1)) {
				this.parentInput.editScreen.drawRect(this.left + 1, top, right - 1, top + height - 1, 0x442D5C60);
				this.parentInput.editScreen.drawRect(this.left + 3, top + 2, right - 3, top + height - 3, 0x992D5C60);
				this.parentInput.editScreen.drawRect(this.left + 5, top + 4, right - 5, top + height - 5, 0xFF2D5C60);
			}
			
			this.parentInput.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			GlStateManager.enableAlpha();
		    
			this.parentInput.editScreen.drawTexturedModalRect(this.left + (right - this.left)/2 - 7, top + height/2 - 7, 73, 64, 13, 13);
		} else if (componentDisplay.component == this.parentInput.insertNewComponent) {
			
			if (CommonGuiUtils.isMouseWithin(this.mouseX, this.mouseY, this.left, top, right - this.left, height - 1)) {
				this.parentInput.editScreen.drawRect(this.left + 1, top, right - 1, top + height, 0x442D5C60);
				this.parentInput.editScreen.drawRect(this.left + 2, top + 1, right - 2, top + height - 1, 0x992D5C60);
				this.parentInput.editScreen.drawRect(this.left + 3, top + 2, right - 3, top + height - 2, 0xFF2D5C60);
				
				this.parentInput.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
			    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
				GlStateManager.enableAlpha();
			    
				this.parentInput.editScreen.drawTexturedModalRect(this.left + (right - this.left)/2 - 7, top + height/2 - 7, 73, 64, 13, 13);
			}
		} else {
			int leftStart = this.left + MODIFY_COMPONENT_WIDTH;
			boolean firstComponent = slot == 1;
			boolean lastComponent = slot == this.displayComponents.size() - 2;
		
			componentDisplay.component.drawInList(leftStart, top, right, this.mouseX, this.mouseY);
			
			if (!firstComponent && CommonGuiUtils.isMouseWithin(this.mouseX, this.mouseY, this.left, top, MODIFY_COMPONENT_WIDTH, 12)) {
				this.parentInput.editScreen.drawRect(this.left + 1, top, this.left + MODIFY_COMPONENT_WIDTH - 2, top + 12, 0x442D5C60);
				this.parentInput.editScreen.drawRect(this.left + 2, top + 1, this.left + MODIFY_COMPONENT_WIDTH - 3, top + 11, 0x992D5C60);
				this.parentInput.editScreen.drawRect(this.left + 3, top + 2, this.left + MODIFY_COMPONENT_WIDTH - 4, top + 10, 0xFF2D5C60);
			}
			
			if (CommonGuiUtils.isMouseWithin(this.mouseX, this.mouseY, this.left, top + 14, MODIFY_COMPONENT_WIDTH, height - 24)) {
				this.parentInput.editScreen.drawRect(this.left + 1, top + 11, this.left + MODIFY_COMPONENT_WIDTH - 2, top + height - 12, 0x44602D2D);
				this.parentInput.editScreen.drawRect(this.left + 2, top + 12, this.left + MODIFY_COMPONENT_WIDTH - 3, top + height - 13, 0x99602D2D);
				this.parentInput.editScreen.drawRect(this.left + 3, top + 13, this.left + MODIFY_COMPONENT_WIDTH - 4, top + height - 14, 0xFF602D2D);
			}
			
			if (!lastComponent && CommonGuiUtils.isMouseWithin(this.mouseX, this.mouseY, this.left, top + height - 12, MODIFY_COMPONENT_WIDTH, 11)) {
				this.parentInput.editScreen.drawRect(this.left + 1, top + height - 12, this.left + MODIFY_COMPONENT_WIDTH - 2, top + height, 0x442D5C60);
				this.parentInput.editScreen.drawRect(this.left + 2, top + height - 11, this.left + MODIFY_COMPONENT_WIDTH - 3, top + height - 1, 0x992D5C60);
				this.parentInput.editScreen.drawRect(this.left + 3, top + height - 10, this.left + MODIFY_COMPONENT_WIDTH - 4, top + height - 2, 0xFF2D5C60);
			}
			
			this.parentInput.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			GlStateManager.enableAlpha();

			if (!firstComponent) {
				this.parentInput.editScreen.drawTexturedModalRect(this.left + MODIFY_COMPONENT_WIDTH/2 - 7, top + 2, 21, 77, 13, 8);
			}
			
			this.parentInput.editScreen.drawTexturedModalRect(this.left + MODIFY_COMPONENT_WIDTH/2 - 7, top + height/2 - 7, 60, 64, 13, 13);
			
			if (!lastComponent) {
				this.parentInput.editScreen.drawTexturedModalRect(this.left + MODIFY_COMPONENT_WIDTH/2 - 7, top + height - 10, 34, 77, 13, 8);
			}
		}
	}
	
	@Override 
	public void handleMouseInput(int mouseX, int mouseY) throws IOException {
		boolean scrollList = true;
		for (IGuiViewComponent component : this.visibleComponents) {
			if (component.onHandleMouseInput(mouseX, mouseY)) {
				scrollList = false;
			}
		}
		if (scrollList) {
			super.handleMouseInput(mouseX, mouseY);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		CommonGuiUtils.drawOutline(this.left - 1, this.top - 1, this.listWidth + 2, this.listHeight + 2, 0xFFA0A0A0);
		this.visibleComponents.clear();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseX >= this.left && mouseX <= this.left + this.listWidth && mouseY >= this.top && mouseY <= this.top + this.listHeight) {
			for (IGuiViewComponent component : this.visibleComponents) {
				component.onMouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {
		for (IGuiViewComponent component : this.visibleComponents) {
			component.onKeyTyped(keyTyped, keyCode);
		}
	}

	public void addComponent(IGuiViewComponent component) {
		this.displayComponents.add(new ComponentDisplay(component));
	}
	
	public void clearComponents() {
		this.displayComponents.clear();
	}

	public void removeComponent(IGuiViewComponent component) {
		this.displayComponents.stream().filter(display -> display.component != component).collect(Collectors.toCollection(() -> new ArrayList<>()));
	}
	
	public int getListHeight() {
		int entryRight = this.left + this.listWidth - 7;
		return this.getContentHeight(entryRight);
	}
	
	public static class ComponentDisplay {
		public IGuiViewComponent component;
		public int top = 0;
		
		public ComponentDisplay(IGuiViewComponent component) {
			this.component = component;
		}
	}
}
