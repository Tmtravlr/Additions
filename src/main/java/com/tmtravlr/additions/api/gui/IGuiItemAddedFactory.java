package com.tmtravlr.additions.api.gui;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.gui.GuiScreen;

/**
 * Creates the edit screen for the item type.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public interface IGuiItemAddedFactory<T extends IItemAdded> {
	
	/**
	 * Returns a friendlier title for the item type
	 */
	public String getTitle();
	
	/**
	 * Returns a short description about the item to show in the item type select screen
	 */
	public String getDescription();

	/**
	 * Creates a screen to edit the item, or to add it if item is passed in as null.
	 */
	public GuiEdit getEditScreen(GuiScreen parent, Addon addon, T item);

	/**
	 * Creates a screen to create a copy of the item.
	 */
	public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, T item);
	
}
