package com.tmtravlr.additions.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiAdditionsButton extends GuiButton {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");

	private GuiScreen parentScreen;
	
	public GuiAdditionsButton(int buttonId, GuiScreen screen) {
		super(buttonId, screen.width / 2 + 104, screen.height / 4 + 132, 20, 20, "");
		this.parentScreen = screen;
	}
	
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    	super.drawButton(mc, mouseX, mouseY, partialTicks);
    	
    	this.parentScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
	    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
	    GlStateManager.enableAlpha();
	    
		this.parentScreen.drawTexturedModalRect(this.x + 2, this.y + 3, 138, 64, 16, 14);
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
        	this.playPressSound(this.parentScreen.mc.getSoundHandler());
        	this.parentScreen.mc.displayGuiScreen(new GuiAdditionsMainMenu(this.parentScreen));
        }
        
        return false;
    }

}
