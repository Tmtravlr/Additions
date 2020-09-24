package com.tmtravlr.additions.gui.view.edit.update;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.loottables.LootTableAdded;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBlockDropInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

/**
 * Edit screen for block drops, used from the block drop input
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class GuiEditBlockDropInput extends GuiEditBlockDrop {
	
	protected GuiComponentBlockDropInput input;
	
	public GuiEditBlockDropInput(GuiEdit parentScreen, LootTableAdded lootTable, IBlockAdded block, Addon addon, GuiComponentBlockDropInput input) {
		super(parentScreen, lootTable, block, addon);
		this.input = input;
	}
	
	@Override
	protected void handleSaved() {
		input.setBlockDropPreset(this.lootTable.preset);
		
    	super.handleSaved();
	}
	
}
