package com.tmtravlr.additions.util.client;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class CommonGuiUtils {

	//TODO color generator (R, G, B -> single RGBA hex)

	public static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
	public static final int ADDITION_BUTTON_COLOR = 0xBB2A2A2A;
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	private static final String DOTS = "...";
	
	public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public static void playClickSound() {
		MC.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	public static String trimWithDots(FontRenderer fontRenderer, String toTrim, int width) {
		if (fontRenderer.getStringWidth(toTrim) < width) {
			return toTrim;
		}
		
		return fontRenderer.trimStringToWidth(toTrim, width - fontRenderer.getStringWidth(DOTS)) + DOTS;
	}
	
	public static void drawStringWithDots(FontRenderer fontRenderer, String text, int width, int x, int y, int color) {
		fontRenderer.drawStringWithShadow(trimWithDots(fontRenderer, text, width), x, y, color);
	}
    
    public static void openFolder(File folder) {
        try {
        	Desktop.getDesktop().open(folder);
        } catch (Throwable cause) {
            AdditionsMod.logger.error("Unable to open folder: " + folder, cause);
        }
    }
    
    public static JFileChooser createFileChooser(FileNameExtensionFilter filter, File folder) {
    	try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) {
			AdditionsMod.logger.warn("Couldn't set system look and feel for file chooser.", e);
		}
    	
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(folder);
		
		return chooser;
    }
    
    public static void drawOutline(int x, int y, int width, int height, int color) {
		Gui.drawRect(x, y, x + width, y + 1, color);
		Gui.drawRect(x, y, x + 1, y + height, color);
		Gui.drawRect(x + width - 1, y, x + width, y + height, color);
		Gui.drawRect(x, y + height - 1, x + width, y + height, color);
    }

	/**
	 * Require a String input to not be empty.
	 * @param input the input to test
	 * @param parentScreen use {@code this} as the parent parentScreen
	 * @param titleI18n the unlocalized title
	 * @param reason the TextComponent message
	 * @return the text, or {@code null} if the text was empty
	 * @since July 2020
	 * @author sschr15
	 */
	public static String requireStringField(GuiComponentStringInput input, GuiScreen parentScreen, String titleI18n, TextComponentBase reason) {
		if (input.getText().isEmpty()) {
			parentScreen.mc.displayGuiScreen(new GuiMessageBox(parentScreen, I18n.format(titleI18n), reason, I18n.format("gui.buttons.back")));
			return null;
		}
		return input.getText();
	}

}
