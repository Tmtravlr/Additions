package com.tmtravlr.additions.api.gui;

import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

/**
 * Creates the edit screen for the effect cause.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019
 */
public interface IGuiEffectCauseFactory<T extends EffectCause> {
	
	/**
	 * Returns a friendlier title for the effect cause type
	 */
	public String getTitle();
	
	/**
	 * Item stack to render to represent the effect cause
	 */
	public ItemStack getDisplayStack(T cause);

	/**
	 * Creates a list of components to display on the edit screen for this effect cause
	 */
	public IGuiEffectCauseEditHandler getEditHandler();
	
}
