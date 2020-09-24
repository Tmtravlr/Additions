package com.tmtravlr.additions.gui.view.components;

import java.io.IOException;

/**
 * Input field of some sort to go in the scrolling list for GuiEditObject
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2017
 */
public interface IGuiViewComponent {

	/**
	 * @return The height of this component. It shouldn't change after the component is set up.
	 */
	public int getHeight(int left, int right);
	
	/**
	 * @return This component's label if it has one. Should be an empty string if it doesn't.
	 */
	public default String getLabel() {
		return "";
	}
	
	/**
	 * @return True if this component is required to be filled out. Draws a star next to inputs.
	 */
	public default boolean isRequired() {
		return false;
	}
	
	/**
	 * Called to draw this component in the list.
	 * @param x The leftmost start of the component's slot
	 * @param y The topmost start of the component's slot
	 * @param right The right side of the list
	 * @param mouseX The x location of the mouse
	 * @param mouseY The y location of the mouse
	 */
	public void drawInList(int x, int y, int right, int mouseX, int mouseY);
	
	/**
	 * Called when the mouse is clicked
	 * @param mouseX
	 * @param mouseY
	 * @param mouseButton
	 * @throws IOException
	 */
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
	
	/**
	 * Handles other mouse input like scrolling
	 * @param mouseX
	 * @param mouseY
	 * @return True if this component is handling the mouse input; 
	 * needed so when you scroll in a dropdown, the background doesn't scroll too.
	 * @throws IOException
	 */
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException;
	
	/**
	 * Called when a key is typed
	 * @param keyTyped
	 * @param keyCode
	 * @throws IOException
	 */
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException;
	
	/**
	 * Called when initGui is called
	 */
	public default void onInitGui() {}
	
	/**
	 * @return True if this component is hidden from view, and should not be interacted with.
	 */
	public boolean isHidden();
	
	/**
	 * @param hidden True if the component should be hidden.
	 */
	public void setHidden(boolean hidden);
	
}
