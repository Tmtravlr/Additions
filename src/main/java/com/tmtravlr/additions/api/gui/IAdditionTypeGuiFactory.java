package com.tmtravlr.additions.api.gui;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButton;
import com.tmtravlr.additions.gui.view.GuiView;

/**
 * Info for rendering an AdditionType. Used by the GUI to add a button in the addon's screen and to add/edit the additions.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2017
 */
public interface IAdditionTypeGuiFactory {
	
	/**
	 * Create the button for this addition type that shows up on the addon page.
	 * When clicked on, it should display a screen showing the additions for the given addon.
	 */
	public GuiAdditionTypeButton createButton(GuiView parent, Addon addon);
	
}
