package com.tmtravlr.additions.api.gui;

import com.tmtravlr.additions.addon.effects.Effect;

import net.minecraft.item.ItemStack;

/**
 * Creates the edit screen for the effect.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019
 */
public interface IGuiEffectFactory<T extends Effect> {
	
	/**
	 * Returns a friendlier title for the effect type
	 */
	public String getTitle();
	
	/**
	 * Item stack to render to represent the effect
	 */
	public ItemStack getDisplayStack(T effect);

	/**
	 * Creates a list of components to display on the edit screen for this effect
	 */
	public IGuiEffectEditHandler getEditHandler();
	
}
