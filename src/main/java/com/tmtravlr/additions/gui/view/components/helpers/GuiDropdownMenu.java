package com.tmtravlr.additions.gui.view.components.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.gui.GuiScreenOverlay;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.GuiScrollingList;

public abstract class GuiDropdownMenu extends GuiScreenOverlay {
	
	protected GuiDropdownMenuScrolling dropdown;
	
	protected int x;
	protected int y;
	protected int dropdownWidth;
	protected int dropdownHeight;
	protected boolean hasInitialized;
	protected boolean closeOnMouseUp = false;
	protected List<MenuOption> options = new ArrayList<>();
	
	public GuiDropdownMenu(GuiScreen parentScreen, int x, int inputTop, int inputBottom, int width, List<MenuOption> options) {
		super(parentScreen);
		this.options = options;
		this.dropdownHeight = Math.min(options.size() * this.getMenuOptionHeight(), this.getMenuOptionHeight() * 6) + 5;
		this.dropdownWidth = width;
		this.x = x;
		
		boolean above = this.parentScreen.height - 40 - inputBottom < dropdownHeight;
		this.y = above ? inputTop - this.dropdownHeight : inputBottom;
		
		
		if (options.isEmpty()) {
			this.removeDropdown();
		}
	}
	
	@Override
	public void initGui() {
		if (this.hasInitialized) { 
			this.removeDropdown();
		} else {
			this.dropdown = new GuiDropdownMenuScrolling();
			this.hasInitialized = true;
		}
	}
	
	@Override
	public void drawScreenOverlay(int mouseX, int mouseY, float partialTicks) {
		if (!Mouse.isButtonDown(0) && this.closeOnMouseUp) {
			this.removeDropdown();
			return;
		}
		
		this.dropdown.drawScreen(mouseX, mouseY, 0);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (!CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x, this.y, this.dropdownWidth, this.dropdownHeight)) {
			this.removeDropdown();
			if (this.parentScreen instanceof GuiView) {
				((GuiView)this.parentScreen).mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}
	
	@Override
    public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int scroll = Mouse.getEventDWheel();
        
        if (scroll != 0) {
	        if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x, this.y, this.dropdownWidth, this.dropdownHeight)) {
	        	this.dropdown.handleMouseInput(mouseX, mouseY);
			} else {
				this.removeDropdown();
				if (this.parentScreen instanceof GuiView) {
					((GuiView)this.parentScreen).handleMouseInput();
				}
			}
        }
	}

	public void removeDropdown() {
		this.mc.displayGuiScreen(this.parentScreen);
	}
	
	public void closeOnMouseUp() {
		this.closeOnMouseUp = true;
	}
	
	protected abstract void onOptionSelected(MenuOption option);
	
	protected void drawOption(MenuOption option, int index, int left, int right, int top) {
		this.fontRenderer.drawString(option.getLabel().getFormattedText(), left + 5, top + 3, 0xFFFFFF);
	}
	
	protected int getMenuOptionHeight() {
		return 18;
	}
	
	protected class GuiDropdownMenuScrolling extends GuiScrollingList {
		
		List<String> optionKeys;
		
		public GuiDropdownMenuScrolling() {
			super(GuiDropdownMenu.this.mc, GuiDropdownMenu.this.dropdownWidth, GuiDropdownMenu.this.dropdownHeight, GuiDropdownMenu.this.y, GuiDropdownMenu.this.y + GuiDropdownMenu.this.dropdownHeight, 
					GuiDropdownMenu.this.x, GuiDropdownMenu.this.getMenuOptionHeight(), GuiDropdownMenu.this.parentScreen.width, GuiDropdownMenu.this.parentScreen.height);
		}

		@Override
		protected int getSize() {
			return GuiDropdownMenu.this.options.size();
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			MenuOption option = GuiDropdownMenu.this.options.get(index);
			if (option != null) {
				GuiDropdownMenu.this.onOptionSelected(option);
			}
		}

		@Override
		protected boolean isSelected(int index) {
			return false;
		}

		@Override
		protected void drawBackground() {}

		@Override
		protected void drawSlot(int index, int right, int top, int slotBuffer, Tessellator tess) {
			MenuOption option = GuiDropdownMenu.this.options.get(index);
			if (option != null) {
				GuiDropdownMenu.this.drawOption(option, index, this.left, right, top);
			}
		}
		
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			CommonGuiUtils.drawOutline(this.left - 1, this.top - 1, this.listWidth + 2, this.listHeight + 2, 0xFFA0A0A0);
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
	}
	
	public static class MenuOption {
		private ITextComponent label;
		
		public MenuOption(ITextComponent label) {
			this.label = label;
		}
		
		public ITextComponent getLabel() {
			return this.label;
		}
	}

}
