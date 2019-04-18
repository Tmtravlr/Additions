package com.tmtravlr.additions.gui.view.edit.loottable;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.loottables.LootTablePresetOtherLootTable;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputLootTable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a loot table that drops another loot table or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since Febuary 2019
 */
public class GuiEditLootTablePresetOtherLootTable extends GuiEditLootTablePreset<LootTablePresetOtherLootTable> {
	
	private GuiComponentSuggestionInputLootTable lootTableOtherLootTableInput;
    
	public GuiEditLootTablePresetOtherLootTable(GuiScreen parentScreen, String title, Addon addon, LootTablePresetOtherLootTable preset) {
		super(parentScreen, title, addon);
		
		this.isNew = preset == null;
		
		if (this.isNew) {
			this.preset = new LootTablePresetOtherLootTable();
		} else {
			this.preset = preset;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.lootTableOtherLootTableInput = new GuiComponentSuggestionInputLootTable(I18n.format("gui.edit.lootTable.preset.otherLootTable.lootTable.label"), this);
		this.lootTableOtherLootTableInput.setRequired();
		if (!this.isNew) {
			this.lootTableOtherLootTableInput.setDefaultText(this.preset.otherLootTable.toString());
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.lootTableIdInput);
		this.components.add(this.lootTableOtherLootTableInput);
	}
	
	@Override
	public void saveObject() {
		if (this.lootTableOtherLootTableInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.lootTable.problem.title"), new TextComponentTranslation("gui.edit.lootTable.preset.otherLootTable.problem.noLootTable"), I18n.format("gui.buttons.back")));
			return;
		}
		
        this.preset.otherLootTable = new ResourceLocation(this.lootTableOtherLootTableInput.getText());
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.lootTableOtherLootTableInput.setDefaultText(this.copyFrom.otherLootTable.toString());
		
		super.copyFromOther();
	}
}
