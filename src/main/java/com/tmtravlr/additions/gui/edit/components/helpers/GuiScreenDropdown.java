package com.tmtravlr.additions.gui.edit.components.helpers;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.gui.edit.components.GuiDropdownListInput;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiScreenDropdown<T> extends GuiScreen {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
	
	private GuiDropdownListInput<T> parent;
	private int x;
	private int y;
	private int dropdownWidth;
	private int dropdownHeight;
	private boolean hasInitialized;
	private boolean close = false;
	
	public GuiScreenDropdown(GuiDropdownListInput<T> parent, int x, int y, int width, int height) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.dropdownWidth = width;
		this.dropdownHeight = height;
	}
	
	@Override
	public void initGui() {
		if (this.hasInitialized) { 
			this.removeDropdown();
		} else {
			this.hasInitialized = true;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (this.close) {
			this.removeDropdown();
			return;
		}
		
		this.parent.dropdown.drawScreen(mouseX, mouseY, 0);
		this.parent.filter.drawTextBox();

		this.parent.parent.mc.getTextureManager().bindTexture(GUI_TEXTURES);
	    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
		
	    int searchX = this.parent.filter.x + this.parent.filter.width - 16;
		int searchY = this.parent.filter.y + (this.parent.filter.height / 2 - 6);
		this.parent.drawTexturedModalRect(searchX, searchY, 47, 64, 13, 13);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseX >= this.x && mouseX <= this.x + this.dropdownWidth && mouseY >= this.y && mouseY <= this.y + this.dropdownHeight) {
			this.parent.filter.mouseClicked(mouseX, mouseY, mouseButton);
		} else {
			this.removeDropdown();
			this.parent.parent.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char keyTyped, int keyCode) throws IOException {
		this.parent.filter.textboxKeyTyped(keyTyped, keyCode);
	}
	
	@Override
    public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int scroll = Mouse.getEventDWheel();
        
        if (scroll != 0) {
	        if (mouseX >= this.x && mouseX <= this.x + this.dropdownWidth && mouseY >= this.y && mouseY <= this.y + this.dropdownHeight) {
	        	this.parent.dropdown.handleMouseInput(mouseX, mouseY);
			} else {
				this.removeDropdown();
				this.parent.parent.handleMouseInput();
			}
        }
	}

	public void removeDropdown() {
		this.parent.parent.mc.displayGuiScreen(this.parent.parent);
		this.parent.dropdown = null;
		this.parent.filter.setFocused(false);
		this.parent.dropdownScreen = null;
	}
}
