package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionTypeButton;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

/**
 * Button for an addition type which displays in the addon's view screen.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public abstract class GuiAdditionTypeButton {
	
	protected int x;
	protected int y;
	protected int width;
	protected GuiView viewScreen;
	protected Addon addon;

	public GuiAdditionTypeButton(GuiView viewScreen, Addon addon) {
		this.viewScreen = viewScreen;
		this.addon = addon;
	}
	
	/**
	 * @return Height of the button that should render in the addon view screen.
	 */
	public int getButtonHeight() {
		return 40;
	}
	
	/**
	 * @return True if the mouse is hovering over this button.
	 */
	public boolean isHovering(int mouseX, int mouseY) {
		return CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x, this.y, this.width, this.getButtonHeight());
	}
	
	/**
	 * @return True if this button should be hidden by default and shown with the advanced buttons. 
	 * Generally this should be true if the addition type is more complicated, and won't be used
	 * by most addon makers.
	 */
	public boolean isAdvanced() {
		return false;
	}
	
	/**
	 * Called to draw the button
	 */
	public abstract void drawButton(int x, int y, int right, int mouseX, int mouseY);
	
	/**
	 * Called when the button is clicked.
	 * 
	 * You should create the screen here for viewing the additions.
	 * This should normally be a GuiViewAdditionType, but you can create a custom screen if you want.
	 * 
	 * If you do create a custom screen, make sure to add a way for people to add/edit your additions!
	 */
	public abstract void onClick();
}
