package com.tmtravlr.additions.gui.type.card;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;

/**
 * Card with info about an addition (like an item, or a creative tab)
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public abstract class GuiAdditionCard {

	protected int x;
	protected int y;
	protected int width;
	protected GuiView viewScreen;
	protected Addon addon;

	public GuiAdditionCard(GuiView viewScreen, Addon addon) {
		this.viewScreen = viewScreen;
		this.addon = addon;
	}
	
	/**
	 * @return Height of the button that should render in the addon view screen.
	 */
	public int getCardHeight() {
		return 20;
	}
	
	/**
	 * @return True if the mouse is hovering over this button.
	 */
	public boolean isHovering(int mouseX, int mouseY) {
		return mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.getCardHeight();
	}
	
	/**
	 * Called to draw the button
	 */
	public abstract void drawCard(int x, int y, int right, int mouseX, int mouseY);
	
	/**
	 * Called when the button is clicked.
	 * 
	 * You should create the screen here for viewing the additions.
	 * This should normally be a GuiViewAdditionType, but you can create a custom screen if you want.
	 * 
	 * If you do create a custom screen, make sure to add a way for people to add/edit your additions!
	 */
	public abstract void onClick(int mouseX, int mouseY);
	
	/**
	 * @return True if the filter should include this card.
	 */
	public abstract boolean filterApplies(String filter);
	
}
