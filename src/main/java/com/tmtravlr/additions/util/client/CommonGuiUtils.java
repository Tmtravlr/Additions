package com.tmtravlr.additions.util.client;

import java.awt.Desktop;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class CommonGuiUtils {

	public static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
	
	private static final Minecraft MC = Minecraft.getMinecraft();
	private static final String DOTS = "...";
	
	public static final int ADDITION_BUTTON_COLOR = 0xbb2a2a2a;
	
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

}
