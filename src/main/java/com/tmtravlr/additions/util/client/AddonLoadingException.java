package com.tmtravlr.additions.util.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT) 
public class AddonLoadingException extends CustomModLoadingErrorDisplayException {
	private String message;
	
	public AddonLoadingException(String message) {
		this.message = message;
	}
	
	@Override
	public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {}

	@Override
	public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime) {
		errorScreen.drawDefaultBackground();
		
		int middleX = errorScreen.width / 2;
		int middleY = errorScreen.height / 2;
		
		errorScreen.drawCenteredString(fontRenderer, I18n.translateToLocal("gui.loadingError.title"), middleX, middleY - 10, 0xbbbbbb);
		
		int messageWidth = fontRenderer.getStringWidth(message);
		int fontX = (messageWidth > errorScreen.width - 80) ? 40 : middleX - messageWidth / 2;
		int fontY =  middleY + 10;
		
		for (String line : fontRenderer.listFormattedStringToWidth(message, errorScreen.width - 80)) {
			fontRenderer.drawString(line, fontX, fontY, 0xffffff);
			fontY += fontRenderer.FONT_HEIGHT + 5;
		}
	}
}