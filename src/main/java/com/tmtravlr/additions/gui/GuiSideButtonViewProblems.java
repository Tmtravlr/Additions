package com.tmtravlr.additions.gui;

import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class GuiSideButtonViewProblems extends GuiButton {

	private GuiScreen parentScreen;
	private int problemBounceTicks;
	
	public GuiSideButtonViewProblems(int buttonId, int x, int y, GuiScreen screen) {
		super(buttonId, x, y, 30, 20, "");
		this.parentScreen = screen;
	}
	
    @Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
    	super.drawButton(mc, mouseX, mouseY, partialTicks);
    	
    	this.parentScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
	    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
	    GlStateManager.enableAlpha();
		
		int yOffset = MathHelper.ceil(Math.abs(Math.sin(++this.problemBounceTicks / 6.0)) * 4);
		this.parentScreen.drawTexturedModalRect(this.x + 7, this.y + 6 - yOffset, 154, 74, 6, 13);
    }

}
