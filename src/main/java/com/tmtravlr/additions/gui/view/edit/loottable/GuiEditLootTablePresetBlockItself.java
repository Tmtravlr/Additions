package com.tmtravlr.additions.gui.view.edit.loottable;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedSimple;
import com.tmtravlr.additions.addon.items.blocks.ItemAddedBlockSimple;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItself;
import com.tmtravlr.additions.addon.loottables.LootTablePresetEmpty;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlock;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding a loot table that drops a block or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since Febuary 2019
 */
public class GuiEditLootTablePresetBlockItself extends GuiEditLootTableWithPreset<LootTablePresetBlockItself> {
	
	private GuiComponentDropdownInputBlock lootTableBlockInput;
    
	public GuiEditLootTablePresetBlockItself(GuiScreen parentScreen, String title, Addon addon, LootTablePresetBlockItself preset) {
		super(parentScreen, title, addon);
		
		this.isNew = preset == null;
		
		if (this.isNew) {
			this.lootTable = new LootTablePresetBlockItself();
		} else {
			this.lootTable = preset;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.lootTableBlockInput = new GuiComponentDropdownInputBlock(I18n.format("gui.edit.lootTable.preset.blockItself.block.label"), this);
		this.lootTableBlockInput.setRequired();
		if (!this.isNew) {
			this.lootTableBlockInput.setDefaultSelected(this.lootTable.block);
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.lootTableIdInput);
		this.components.add(this.lootTableBlockInput);
	}
	
	@Override
	public void saveObject() {
		
		if (this.lootTableBlockInput.getSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.lootTable.problem.title"), new TextComponentTranslation("gui.edit.lootTable.preset.blockItself.problem.noBlock"), I18n.format("gui.buttons.back")));
			return;
		}
		
        this.lootTable.block = this.lootTableBlockInput.getSelected();
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.lootTableBlockInput.setDefaultSelected(this.copyFrom.block);
		
		super.copyFromOther();
	}
}
