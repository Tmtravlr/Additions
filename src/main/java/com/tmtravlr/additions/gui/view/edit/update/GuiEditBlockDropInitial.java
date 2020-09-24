package com.tmtravlr.additions.gui.view.edit.update;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.loottables.LootTableAdded;
import com.tmtravlr.additions.addon.loottables.LootTablePreset;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.AdditionTypeBlock;
import com.tmtravlr.additions.type.AdditionTypeLootTable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Edit screen for block drops, used when first creating the block
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class GuiEditBlockDropInitial extends GuiEditBlockDrop {
	
	protected final GuiScreen nextScreen;
	protected LootTablePreset defaultPreset;
	
	public GuiEditBlockDropInitial(GuiEdit parentScreen, GuiScreen nextScreen, LootTableAdded lootTable, IBlockAdded block, Addon addon) {
		super(parentScreen, lootTable, block, addon);
		this.nextScreen = nextScreen;
		this.defaultPreset = lootTable.preset;
	}
	
	@Override
	protected void handleSaved() {
		AdditionTypeLootTable.INSTANCE.saveAddition(this.addon, this.lootTable);
		AdditionTypeBlock.INSTANCE.saveAddition(this.addon, this.block);
		
    	this.mc.displayGuiScreen(this.nextScreen);
	}
	
	@Override
	public void cancelEdit() {
		this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this, this.nextScreen, I18n.format("gui.edit.blockDrop.cancel.title"), new TextComponentTranslation("gui.edit.blockDrop.cancel.message"), I18n.format("gui.buttons.back"), I18n.format("gui.edit.blockDrop.popup.useDefault")) {
			
			@Override
			public void onSecondButtonClicked() {
				GuiEditBlockDropInitial.this.lootTable.preset = GuiEditBlockDropInitial.this.defaultPreset;
				GuiEditBlockDropInitial.this.handleSaved();
			}
			
		});
	}
	
}
