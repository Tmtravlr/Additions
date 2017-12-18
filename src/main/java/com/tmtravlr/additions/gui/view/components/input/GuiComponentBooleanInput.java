package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;

import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Simple boolean input, with a clickable boolean selector like in the new minecraft launcher.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiComponentBooleanInput extends Gui implements IGuiViewComponent {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");

	private GuiEdit editScreen;
	private String label = "";
	private boolean input = false;
	private int x;
	private int y;
	
	public GuiComponentBooleanInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
	}
	
	public void setDefaultBoolean(boolean input) {
		this.input = input;
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
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y + 10;
		
		this.editScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        
        this.editScreen.drawTexturedModalRect(this.x, this.y, 192, 64, 32, 16);
        
        if (input) {
            this.editScreen.drawTexturedModalRect(this.x + 16, this.y, 240, 64, 16, 16);
        } else {
            this.editScreen.drawTexturedModalRect(this.x, this.y, 224, 64, 16, 16);
        }
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseX >= this.x && mouseX <= this.x + 32 && mouseY >= this.y && mouseY <= this.y + 16) {
			this.input = !this.input;
			this.editScreen.notifyHasChanges();
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public boolean getBoolean() {
		return input;
	}

}
