package com.tmtravlr.additions.gui.inputs;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import com.tmtravlr.additions.gui.GuiEditObject;

/**
 * Extension of the vanilla text field, 
 * @author Rebeca Rey (Tmtravlr)
 *
 */
public class GuiStringInput extends GuiTextField {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");

	private GuiEditObject parent;
	
	public String info = null;
	public String error = null;
	public boolean editable = true;
	public boolean hasColorSelect = false;
	
	public GuiStringInput(int id, FontRenderer font, int x, int y, int width, int height) {
		super(id, font, x, y, width, height);
		
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public void setError(String error) {
		this.error = error;
	}

    /**
     * Draws the textbox
     */
	@Override
    public void drawTextBox() {
		super.drawTextBox();
		
		if (this.getVisible()) {
			
		}
    }
	
}
