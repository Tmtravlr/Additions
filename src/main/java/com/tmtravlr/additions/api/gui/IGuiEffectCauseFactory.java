package com.tmtravlr.additions.api.gui;

import javax.annotation.Nullable;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

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
	public String getTitle(@Nullable T cause);
	
	/**
	 * Item stack to render to represent the effect cause
	 */
	public NonNullList<ItemStack> getDisplayStacks(T cause);

	/**
	 * Creates a list of components to display on the edit screen for this effect cause
	 */
	public IGuiEffectCauseEditHandler getEditHandler();
	
}
