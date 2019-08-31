package com.tmtravlr.additions.gui;

import com.tmtravlr.additions.ConfigLoader;
import com.tmtravlr.additions.util.ProblemNotifier;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class GuiAdditionsButton extends GuiButton {

	private GuiScreen parentScreen;
	private int problemBounceTicks;
	
	public GuiAdditionsButton(int buttonId, GuiScreen screen) {
		super(buttonId, screen.width / 2 + ConfigLoader.additionsMainMenuButtonX.getInt(104), screen.height / 4 + ConfigLoader.additionsMainMenuButtonY.getInt(132), 20, 20, "");
		this.parentScreen = screen;
	}
	
    @Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    	super.drawButton(mc, mouseX, mouseY, partialTicks);
    	
    	this.parentScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
	    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
	    GlStateManager.enableAlpha();
	    
		this.parentScreen.drawTexturedModalRect(this.x + 2, this.y + 3, 138, 64, 16, 14);
		
		if (ProblemNotifier.hasProblems()) {
			int yOffset = MathHelper.ceil(Math.abs(Math.sin(++this.problemBounceTicks / 6.0)) * 5);
			this.parentScreen.drawTexturedModalRect(this.x + 14, this.y + 1 - yOffset, 154, 74, 6, 13);
		}
    }

    @Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
        	this.playPressSound(this.parentScreen.mc.getSoundHandler());
        	this.parentScreen.mc.displayGuiScreen(new GuiAdditionsMainMenu(this.parentScreen));
        }
        
        return false;
    }

}
