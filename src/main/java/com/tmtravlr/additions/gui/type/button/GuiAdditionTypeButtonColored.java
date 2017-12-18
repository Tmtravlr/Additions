package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionTypeList;

/**
 * Implementation of the GuiAdditionTypeButton with simple colors.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public abstract class GuiAdditionTypeButtonColored extends GuiAdditionTypeButton {
	
	private static final int MAX_WIDTH = 300;
	
	protected int backgroundColor;
	protected int hoverColor;
	protected int lightColor;
	protected int shadowColor;
	protected String label;
	
	public GuiAdditionTypeButtonColored(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setColors(int backgroundColor, int hoverColor, int lightColor, int shadowColor) {
		this.backgroundColor = backgroundColor;
		this.hoverColor = hoverColor;
		this.lightColor = lightColor;
		this.shadowColor = shadowColor;
	}

	@Override
	public void drawButton(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y + 10;
		this.width = right - x;
		int height = 20;
		
		if (this.width > MAX_WIDTH) {
			this.x = this.width/2 - MAX_WIDTH/2;
			this.width = MAX_WIDTH;
		}
		
		this.viewScreen.drawRect(this.x - 1, this.y - 1, this.x + this.width, this.y + height, this.lightColor);
		this.viewScreen.drawRect(this.x, this.y, this.x + this.width + 1, this.y + height + 1, this.shadowColor);
	    if (this.isHovering(mouseX, mouseY)) {
			this.viewScreen.drawRect(this.x, this.y, this.x + this.width, this.y + height, this.hoverColor);
	    } else {
			this.viewScreen.drawRect(this.x, this.y, this.x + this.width, this.y + height, this.backgroundColor);
	    }
		this.viewScreen.drawCenteredString(this.viewScreen.getFontRenderer(), this.label, this.x + this.width/2, this.y + 7, 0xFFFFFF);
	}

	@Override
	public boolean isHovering(int mouseX, int mouseY) {
		return mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + 20;
	}

}
