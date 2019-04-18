package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionTypeButton;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.util.math.MathHelper;

/**
 * Implementation of the GuiAdditionTypeButton with simple colors.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public abstract class GuiAdditionTypeButtonColored extends GuiAdditionTypeButton {
	
	private static final int MAX_WIDTH = 300;
	private static final int MAX_HOVER_TIME = 5;
	
	protected int backgroundColor;
	protected int hoverColor;
	protected int lightColor;
	protected int shadowColor;
	protected String label;
	protected int hoverTime = 0;
	
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
		int middleX = this.x + this.width/2;
		int height = 20;
		
		if (this.width > MAX_WIDTH) {
			this.x = middleX - MAX_WIDTH/2;
			this.width = MAX_WIDTH;
		}
		
		this.viewScreen.drawRect(this.x - 1, this.y - 1, this.x + this.width, this.y + height, this.lightColor);
		this.viewScreen.drawRect(this.x, this.y, this.x + this.width + 1, this.y + height + 1, this.shadowColor);
		this.viewScreen.drawRect(this.x, this.y, this.x + this.width, this.y + height, this.backgroundColor);
		
	    if (this.isHovering(mouseX, mouseY)) {
			if (hoverTime < MAX_HOVER_TIME) {
				this.hoverTime++;
			}
	    } else {
	    	if (hoverTime > 0) {
	    		this.hoverTime--;
	    	}
	    }
	    hoverTime = MathHelper.clamp(hoverTime, 0, MAX_HOVER_TIME);
	    
		double highlightWidthPercent = Math.sqrt((double)this.hoverTime / MAX_HOVER_TIME);
		int highlightWidth = MathHelper.ceil(this.width * highlightWidthPercent);
		
		this.viewScreen.drawRect(middleX - highlightWidth/2, this.y, middleX + highlightWidth/2, this.y + height, this.hoverColor);
		this.viewScreen.drawCenteredString(this.viewScreen.getFontRenderer(), this.label, middleX, this.y + 7, 0xFFFFFF);
	}

	@Override
	public boolean isHovering(int mouseX, int mouseY) {
		return CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x, this.y, this.width, 20);
	}

}
