package com.tmtravlr.additions.gui.view.components.helpers;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.gui.view.components.input.GuiComponentDropdownInput;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Screen for the dropdown list input. Has a search bar, and a scrolling list of whatever the dropdown displays.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiScreenDropdown<T> extends GuiScreen {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
	
	private GuiComponentDropdownInput<T> parentInput;
	private GuiScreen parentScreen;
	private int x;
	private int y;
	private int dropdownWidth;
	private int dropdownHeight;
	private boolean hasInitialized;
	private boolean closeOnMouseUp = false;
	
	public GuiScreenDropdown(GuiComponentDropdownInput<T> parentInput, GuiScreen parentScreen, int x, int y, int width, int height) {
		this.parentInput = parentInput;
		this.parentScreen = parentScreen;
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
		if (!Mouse.isButtonDown(0) && this.closeOnMouseUp) {
			this.removeDropdown();
			return;
		}
		
		this.parentInput.dropdown.drawScreen(mouseX, mouseY, 0);
		this.parentInput.filter.drawTextBox();

		this.parentInput.editScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
	    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
		
	    int searchX = this.parentInput.filter.x + this.parentInput.filter.width - 16;
		int searchY = this.parentInput.filter.y + (this.parentInput.filter.height / 2 - 6);
		this.parentInput.drawTexturedModalRect(searchX, searchY, 47, 64, 13, 13);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseX >= this.x && mouseX <= this.x + this.dropdownWidth && mouseY >= this.y && mouseY <= this.y + this.dropdownHeight) {
			this.parentInput.filter.mouseClicked(mouseX, mouseY, mouseButton);
		} else {
			this.removeDropdown();
			this.parentInput.editScreen.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char keyTyped, int keyCode) throws IOException {
		this.parentInput.filter.textboxKeyTyped(keyTyped, keyCode);
	}
	
	@Override
    public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int scroll = Mouse.getEventDWheel();
        
        if (scroll != 0) {
	        if (mouseX >= this.x && mouseX <= this.x + this.dropdownWidth && mouseY >= this.y && mouseY <= this.y + this.dropdownHeight) {
	        	this.parentInput.dropdown.handleMouseInput(mouseX, mouseY);
			} else {
				this.removeDropdown();
				this.parentInput.editScreen.handleMouseInput();
			}
        }
	}

	public void removeDropdown() {
		this.parentInput.editScreen.mc.displayGuiScreen(this.parentScreen);
		this.parentInput.dropdown = null;
		this.parentInput.filter.setFocused(false);
		this.parentInput.dropdownScreen = null;
	}
	
	public void closeOnMouseUp() {
		this.closeOnMouseUp = true;
	}
}
