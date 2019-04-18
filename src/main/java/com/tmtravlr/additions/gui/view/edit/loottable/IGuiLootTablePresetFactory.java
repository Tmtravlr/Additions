package com.tmtravlr.additions.gui.view.edit.loottable;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.gui.GuiScreen;

/**
 * Creates the edit screen for the loot table preset type.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since February 2019
 */
public interface IGuiLootTablePresetFactory<T> {
	
	/**
	 * Returns a friendlier title for the loot table preset type
	 */
	public String getTitle();
	
	/**
	 * Returns a short description about the loot table preset to show in the preset type select screen
	 */
	public String getDescription();

	/**
	 * Creates a screen to edit the loot table preset, or to add it if preset is passed in as null.
	 */
	public GuiEdit getEditScreen(GuiScreen parent, Addon addon, T lootTablePreset);

	/**
	 * Creates a screen to create a copy of the loot table preset.
	 */
	public GuiEdit getDuplicateScreen(GuiScreen parent, Addon addon, T lootTablePreset);
	
}
