package com.tmtravlr.additions.api.gui;

import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

/**
 * Handles creating the components and saving the effect cause.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date May 2019
 */
public interface IGuiEffectCauseEditHandler {

	/**
	 * Creates a list of components to display on the edit screen for this effect cause.
	 * The cause could be any type, and a null cause means this is creating a new one
	 */
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause);
	
	/**
	 * Save the effect cause
	 */
	public EffectCause createEffectCause();
}
