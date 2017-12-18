package com.tmtravlr.additions.gui.view.components.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import com.tmtravlr.additions.gui.GuiScrollingListAnyHeight;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;

/**
 * Shows a scrolling list of input components, which can be added or removed.
 * Useful for item lore and lists like that.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiScrollingComponentList<T extends IGuiViewComponent> extends GuiScrollingListAnyHeight {
	
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
    private static final int REMOVE_COMPONENT_WIDTH = 20;
    
	private GuiComponentListInput<T> parentInput;
	public ArrayList<IGuiViewComponent> displayComponents = new ArrayList<>();
	
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
		IGuiViewComponent component = displayComponents.get(slot);
		if (component == this.parentInput.addNewComponent) {
			//Add new component
			this.parentInput.addNewComponent();
		} else if (component == this.parentInput.insertNewComponent) {
			//Insert a component here
			int index = slot / 2;
			this.parentInput.insertNewComponent(index);
		} else {
			if (this.mouseX >= this.left && this.mouseX < this.left + REMOVE_COMPONENT_WIDTH) {
				//Remove component
				int index = slot / 2;
				this.parentInput.removeComponent(index);
			}
		}
	}

	@Override
	protected boolean isSelected(int slot) {
		return false;
	}

	@Override
	protected int getSlotHeight(int slot, int entryRight) {
		return this.displayComponents.get(slot).getHeight(this.left, entryRight);
	}

	@Override
	protected void drawSlot(int slot, int right, int top, int buffer, Tessellator tess) {
		IGuiViewComponent component = this.displayComponents.get(slot);
		int height = component.getHeight(this.left, right);
		
		if (component == this.parentInput.addNewComponent) {
			
			if (this.mouseX >= this.left && this.mouseX <= right && this.mouseY >= top && this.mouseY < top + height ) {
				this.parentInput.editScreen.drawRect(this.left + 1, top, right - 1, top + height, 0xFF102123);
				this.parentInput.editScreen.drawRect(this.left + 3, top + 2, right - 3, top + height - 2, 0xFF1C393C);
				this.parentInput.editScreen.drawRect(this.left + 5, top + 4, right - 5, top + height - 4, 0xFF2D5C60);
			}
			
			this.parentInput.editScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			GlStateManager.enableAlpha();
		    
			this.parentInput.editScreen.drawTexturedModalRect(this.left + (right - this.left)/2 - 7, top + height/2 - 7, 73, 64, 13, 13);
		} else if (component == this.parentInput.insertNewComponent) {
			
			if (this.mouseX >= this.left && this.mouseX <= right && this.mouseY >= top && this.mouseY < top + height ) {
				this.parentInput.editScreen.drawRect(this.left + 1, top, right - 1, top + height, 0xFF102123);
				this.parentInput.editScreen.drawRect(this.left + 2, top + 1, right - 2, top + height - 1, 0xFF1C393C);
				this.parentInput.editScreen.drawRect(this.left + 3, top + 2, right - 3, top + height - 2, 0xFF2D5C60);
				
				this.parentInput.editScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
			    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
				GlStateManager.enableAlpha();
			    
				this.parentInput.editScreen.drawTexturedModalRect(this.left + (right - this.left)/2 - 7, top + height/2 - 7, 73, 64, 13, 13);
			}
		} else {
			int leftStart = this.left + REMOVE_COMPONENT_WIDTH;
		
			component.drawInList(leftStart, top, right, this.mouseX, this.mouseY);
			
			if (this.mouseX >= this.left && this.mouseX <= this.left + REMOVE_COMPONENT_WIDTH && this.mouseY >= top && this.mouseY < top + height ) {
				this.parentInput.editScreen.drawRect(this.left + 1, top, this.left + REMOVE_COMPONENT_WIDTH - 2, top + height, 0xFF210F0F);
				this.parentInput.editScreen.drawRect(this.left + 2, top + 1, this.left + REMOVE_COMPONENT_WIDTH - 3, top + height - 1, 0xFF351919);
				this.parentInput.editScreen.drawRect(this.left + 3, top + 2, this.left + REMOVE_COMPONENT_WIDTH - 4, top + height - 2, 0xFF602D2D);
			}
			
			
			this.parentInput.editScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			GlStateManager.enableAlpha();
		    
			this.parentInput.editScreen.drawTexturedModalRect(this.left + REMOVE_COMPONENT_WIDTH/2 - 7, top + height/2 - 7, 60, 64, 13, 13);
		}
	}
	
	@Override 
	public void handleMouseInput(int mouseX, int mouseY) throws IOException {
		boolean scrollList = true;
		for (IGuiViewComponent component : this.displayComponents) {
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
		
		Gui.drawRect(this.left - 1, this.top - 1, this.left + this.listWidth + 1, this.top + this.listHeight + 1, 0xFFA0A0A0);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (IGuiViewComponent component : this.displayComponents) {
			component.onMouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {
		for (IGuiViewComponent component : this.displayComponents) {
			component.onKeyTyped(keyTyped, keyCode);
		}
	}

	public void addComponent(IGuiViewComponent component) {
		this.displayComponents.add(component);
	}
	
	public void clearComponents() {
		this.displayComponents.clear();
	}

	public void removeComponent(IGuiViewComponent component) {
		this.displayComponents.remove(component);
	}
	
	public int getListHeight() {
		int entryRight = this.left + this.listWidth - 7;
		return this.getContentHeight(entryRight);
	}
}
