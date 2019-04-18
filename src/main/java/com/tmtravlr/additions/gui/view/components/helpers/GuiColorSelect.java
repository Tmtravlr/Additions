package com.tmtravlr.additions.gui.view.components.helpers;

import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

/**
 * Simple color selector for a text input field
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiColorSelect extends Gui {
	
	private GuiEdit editScreen;
	
    public TextFormatting color = TextFormatting.RESET;
    public boolean obfuscated;
    public boolean bold;
    public boolean italic;
    public boolean underline;
    public boolean strikethrough;
    
    public boolean visible = false;
    public int x;
    public int y;
    public int width = 64;
    public int height = 43;
    
    public GuiColorSelect(GuiEdit editScreen) {
    	this.editScreen = editScreen;
    }
    
    public String stripFormatting(String text) {
    	int lastFormatIndex = this.parseFormat(text);
    	return text.substring(lastFormatIndex);
    }
    
    public String addFormatting(String text) {
    	return this.getFormatting() + text;
    }
    
    public String getFormatting() {
    	String format = "";
    	
    	if (this.color != TextFormatting.RESET) {
    		format += this.color;
    	}
    	if (this.obfuscated) {
    		format += TextFormatting.OBFUSCATED;
    	}
    	if (this.bold) {
    		format += TextFormatting.BOLD;
    	}
    	if (this.underline) {
    		format += TextFormatting.UNDERLINE;
    	}
    	if (this.strikethrough) {
    		format += TextFormatting.STRIKETHROUGH;
    	}
    	if (this.italic) {
    		format += TextFormatting.ITALIC;
    	}
    	
    	return format;
    }
	
    @SuppressWarnings("incomplete-switch")
	public void drawColorSelect() {
		this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        GlStateManager.enableAlpha();
        
        this.editScreen.drawTexturedModalRect(this.x, this.y, 0, 85, this.width, this.height);
        switch (this.color) {
		case WHITE:
			this.editScreen.drawTexturedModalRect(this.x + 4, this.y + 4, 64, 85, 8, 8);
			break;
		case GRAY:
			this.editScreen.drawTexturedModalRect(this.x + 13, this.y + 4, 64, 85, 8, 8);
			break;
		case DARK_GRAY:
			this.editScreen.drawTexturedModalRect(this.x + 22, this.y + 4, 64, 85, 8, 8);
			break;
		case BLACK:
			this.editScreen.drawTexturedModalRect(this.x + 31, this.y + 4, 64, 85, 8, 8);
			break;
		case YELLOW:
			this.editScreen.drawTexturedModalRect(this.x + 4, this.y + 13, 64, 85, 8, 8);
			break;
		case GOLD:
			this.editScreen.drawTexturedModalRect(this.x + 13, this.y + 13, 64, 85, 8, 8);
			break;
		case RED:
			this.editScreen.drawTexturedModalRect(this.x + 22, this.y + 13, 64, 85, 8, 8);
			break;
		case DARK_RED:
			this.editScreen.drawTexturedModalRect(this.x + 31, this.y + 13, 64, 85, 8, 8);
			break;
		case GREEN:
			this.editScreen.drawTexturedModalRect(this.x + 4, this.y + 22, 64, 85, 8, 8);
			break;
		case DARK_GREEN:
			this.editScreen.drawTexturedModalRect(this.x + 13, this.y + 22, 64, 85, 8, 8);
			break;
		case LIGHT_PURPLE:
			this.editScreen.drawTexturedModalRect(this.x + 22, this.y + 22, 64, 85, 8, 8);
			break;
		case DARK_PURPLE:
			this.editScreen.drawTexturedModalRect(this.x + 31, this.y + 22, 64, 85, 8, 8);
			break;
		case AQUA:
			this.editScreen.drawTexturedModalRect(this.x + 4, this.y + 31, 64, 85, 8, 8);
			break;
		case DARK_AQUA:
			this.editScreen.drawTexturedModalRect(this.x + 13, this.y + 31, 64, 85, 8, 8);
			break;
		case BLUE:
			this.editScreen.drawTexturedModalRect(this.x + 22, this.y + 31, 64, 85, 8, 8);
			break;
		case DARK_BLUE:
			this.editScreen.drawTexturedModalRect(this.x + 31, this.y + 31, 64, 85, 8, 8);
			break;
        }
        
        if (this.strikethrough) {
			this.editScreen.drawTexturedModalRect(this.x + 41, this.y + 4, 72, 85, 9, 11);
        }
        
        if (this.obfuscated) {
			this.editScreen.drawTexturedModalRect(this.x + 51, this.y + 4, 72, 85, 9, 11);
        }
        
        if (this.bold) {
			this.editScreen.drawTexturedModalRect(this.x + 41, this.y + 16, 72, 85, 9, 11);
        }
        
        if (this.italic) {
			this.editScreen.drawTexturedModalRect(this.x + 51, this.y + 16, 72, 85, 9, 11);
        }
        
        if (this.underline) {
			this.editScreen.drawTexturedModalRect(this.x + 51, this.y + 28, 72, 85, 9, 11);
        }
    }
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    	if (this.visible) {
    		boolean clickedSomething = true;
    		
    		if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 4, this.y + 4, 8, 8)) {
    			this.color = this.color == TextFormatting.WHITE ? TextFormatting.RESET : TextFormatting.WHITE;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 13, this.y + 4, 8, 8)) {
    			this.color = this.color == TextFormatting.GRAY ? TextFormatting.RESET : TextFormatting.GRAY;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 22, this.y + 4, 8, 8)) {
    			this.color = this.color == TextFormatting.DARK_GRAY ? TextFormatting.RESET : TextFormatting.DARK_GRAY;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 31, this.y + 4, 8, 8)) {
    			this.color = this.color == TextFormatting.BLACK ? TextFormatting.RESET : TextFormatting.BLACK;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 4, this.y + 13, 8, 8)) {
    			this.color = this.color == TextFormatting.YELLOW ? TextFormatting.RESET : TextFormatting.YELLOW;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 13, this.y + 13, 8, 8)) {
    			this.color = this.color == TextFormatting.GOLD ? TextFormatting.RESET : TextFormatting.GOLD;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 22, this.y + 13, 8, 8)) {
    			this.color = this.color == TextFormatting.RED ? TextFormatting.RESET : TextFormatting.RED;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 31, this.y + 13, 8, 8)) {
    			this.color = this.color == TextFormatting.DARK_RED ? TextFormatting.RESET : TextFormatting.DARK_RED;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 4, this.y + 22, 8, 8)) {
    			this.color = this.color == TextFormatting.GREEN ? TextFormatting.RESET : TextFormatting.GREEN;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 13, this.y + 22, 8, 8)) {
    			this.color = this.color == TextFormatting.DARK_GREEN ? TextFormatting.RESET : TextFormatting.DARK_GREEN;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 22, this.y + 22, 8, 8)) {
    			this.color = this.color == TextFormatting.LIGHT_PURPLE ? TextFormatting.RESET : TextFormatting.LIGHT_PURPLE;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 31, this.y + 22, 8, 8)) {
    			this.color = this.color == TextFormatting.DARK_PURPLE ? TextFormatting.RESET : TextFormatting.DARK_PURPLE;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 4, this.y + 31, 8, 8)) {
    			this.color = this.color == TextFormatting.AQUA ? TextFormatting.RESET : TextFormatting.AQUA;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 13, this.y + 31, 8, 8)) {
    			this.color = this.color == TextFormatting.DARK_AQUA ? TextFormatting.RESET : TextFormatting.DARK_AQUA;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 22, this.y + 31, 8, 8)) {
    			this.color = this.color == TextFormatting.BLUE ? TextFormatting.RESET : TextFormatting.BLUE;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 31, this.y + 31, 8, 8)) {
    			this.color = this.color == TextFormatting.DARK_BLUE ? TextFormatting.RESET : TextFormatting.DARK_BLUE;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 41, this.y + 4, 9, 11)) {
    			this.strikethrough = !this.strikethrough;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 51, this.y + 4, 9, 11)) {
    			this.obfuscated = !this.obfuscated;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 41, this.y + 16, 9, 11)) {
    			this.bold = !this.bold;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 51, this.y + 16, 9, 11)) {
    			this.italic = !this.italic;
    		} else if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + 51, this.y + 28, 9, 11)) {
    			this.underline = !this.underline;
    		} else {
    			clickedSomething = false;
    			
    			if (!CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
    				this.visible = false;
    			}
    		}
    		
    		if (clickedSomething) {
    			this.editScreen.notifyHasChanges();
    		}
    	}
    }
    
    private int parseFormat(String text) {
    	int index = 0;
    	
		while (index + 1 < text.length() && text.charAt(index) == '\u00a7') {
			parseFormatChar(text.charAt(index + 1));
			index += 2;
		}
		
		return index;
    }
    
    private void parseFormatChar(char format) {
    	if ( (format >= '0' && format <= '9') || (format >= 'a' && format <= 'f') || (format >= 'A' && format <= 'F')) {
    		this.resetFormatting();
    		
    		switch (format) {
    		case '0':
    			this.color = TextFormatting.BLACK;
    			break;
    		case '1':
    			this.color = TextFormatting.DARK_BLUE;
    			break;
    		case '2':
    			this.color = TextFormatting.DARK_GREEN;
    			break;
    		case '3':
    			this.color = TextFormatting.DARK_AQUA;
    			break;
    		case '4':
    			this.color = TextFormatting.DARK_RED;
    			break;
    		case '5':
    			this.color = TextFormatting.DARK_PURPLE;
    			break;
    		case '6':
    			this.color = TextFormatting.GOLD;
    			break;
    		case '7':
    			this.color = TextFormatting.GRAY;
    			break;
    		case '8':
    			this.color = TextFormatting.DARK_GRAY;
    			break;
    		case '9':
    			this.color = TextFormatting.BLUE;
    			break;
    		case 'a':
    		case 'A':
    			this.color = TextFormatting.GREEN;
    			break;
    		case 'b':
    		case 'B':
    			this.color = TextFormatting.AQUA;
    			break;
    		case 'c':
    		case 'C':
    			this.color = TextFormatting.RED;
    			break;
    		case 'd':
    		case 'D':
    			this.color = TextFormatting.LIGHT_PURPLE;
    			break;
    		case 'e':
    		case 'E':
    			this.color = TextFormatting.YELLOW;
    			break;
    		case 'f':
    		case 'F':
    			this.color = TextFormatting.WHITE;
    			break;
    		}
    	} else if (format == 'k') {
    		this.obfuscated = true;
    	} else if (format == 'l') {
    		this.bold = true;
    	} else if (format == 'm') {
    		this.strikethrough = true;
    	} else if (format == 'n') {
    		this.underline = true;
    	} else if (format == 'o') {
    		this.italic = true;
    	} else if (format == 'r') {
    		this.resetFormatting();
    	}
    }
    
    private void resetFormatting() {
    	this.color = TextFormatting.RESET;
    	this.obfuscated = false;
    	this.bold = false;
    	this.italic = false;
    	this.underline = false;
    	this.strikethrough = false;
    }
	
}
