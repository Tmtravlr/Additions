package com.tmtravlr.additions.gui.view.edit.item;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.gui.GuiScreen;

/**
 * Creates the edit screen for the item type.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017
 */
public interface IGuiEditItemFactory<T> {

	public GuiEdit getEditScreen(GuiScreen parent, Addon addon, T item);

	public GuiEdit getDuplicateScreen(GuiView viewScreen, Addon addon, T item);
	
}
