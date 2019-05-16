package com.tmtravlr.additions.gui.view.edit.loottable;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItemDrop;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentNBTInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlock;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a loot table that drops a block or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since Febuary 2019
 */
public class GuiEditLootTablePresetBlockItem extends GuiEditLootTableWithPreset<LootTablePresetBlockItemDrop> {
	
	private GuiComponentDropdownInputBlock lootTableBlockInput;
	private GuiComponentItemStackInput lootTableItemInput;
	private GuiComponentIntegerInput lootTableItemMinInput;
	private GuiComponentIntegerInput lootTableItemMaxInput;
	private GuiComponentBooleanInput lootTableFortunableInput;
	private GuiComponentBooleanInput lootTableSilkTouchableInput;
    
	public GuiEditLootTablePresetBlockItem(GuiScreen parentScreen, String title, Addon addon, LootTablePresetBlockItemDrop preset) {
		super(parentScreen, title, addon);
		
		this.isNew = preset == null;
		
		if (this.isNew) {
			this.lootTable = new LootTablePresetBlockItemDrop();
		} else {
			this.lootTable = preset;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.lootTableBlockInput = new GuiComponentDropdownInputBlock(I18n.format("gui.edit.lootTable.preset.blockItem.block.label"), this);
		this.lootTableBlockInput.setRequired();
		if (!this.isNew) {
			this.lootTableBlockInput.setDefaultSelected(this.lootTable.block);
		}
		
		this.lootTableItemInput = new GuiComponentItemStackInput(I18n.format("gui.edit.lootTable.preset.blockItem.item.label"), this);
		this.lootTableItemInput.setRequired();
		this.lootTableItemInput.disableCount();
		if (!this.isNew) {
			this.lootTableItemInput.setDefaultItemStack(this.lootTable.dropStack);
		}
		
		this.lootTableItemMinInput = new GuiComponentIntegerInput(I18n.format("gui.edit.lootTable.preset.blockItem.itemMin.label"), this, true) {
			
			@Override
			protected void setInteger(int integer) {
				if (GuiEditLootTablePresetBlockItem.this.lootTableItemMaxInput != null && integer > GuiEditLootTablePresetBlockItem.this.lootTableItemMaxInput.getInteger()) {
					integer = GuiEditLootTablePresetBlockItem.this.lootTableItemMaxInput.getInteger();
				}
				super.setInteger(integer);
			}
			
		};
		this.lootTableItemMinInput.setDefaultInteger(this.lootTable.itemMin);
		
		this.lootTableItemMaxInput = new GuiComponentIntegerInput(I18n.format("gui.edit.lootTable.preset.blockItem.itemMax.label"), this, false) {
			
			@Override
			protected void setInteger(int integer) {
				if (GuiEditLootTablePresetBlockItem.this.lootTableItemMinInput != null && integer < GuiEditLootTablePresetBlockItem.this.lootTableItemMinInput.getInteger()) {
					integer = GuiEditLootTablePresetBlockItem.this.lootTableItemMinInput.getInteger();
				}
				super.setInteger(integer);
			}
			
		};
		this.lootTableItemMaxInput.setMinimum(0);
		this.lootTableItemMaxInput.setDefaultInteger(this.lootTable.itemMax);
		
		this.lootTableFortunableInput = new GuiComponentBooleanInput(I18n.format("gui.edit.lootTable.preset.blockItem.fortunable.label"), this);
		if (this.lootTable.fortunable) {
			this.lootTableFortunableInput.setDefaultBoolean(true);
		}
		
		this.lootTableSilkTouchableInput = new GuiComponentBooleanInput(I18n.format("gui.edit.lootTable.preset.blockItem.silkTouchable.label"), this);
		if (this.lootTable.silkTouchable) {
			this.lootTableSilkTouchableInput.setDefaultBoolean(true);
		}
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.lootTableIdInput);
		this.components.add(this.lootTableBlockInput);
		this.components.add(this.lootTableItemInput);
		this.components.add(this.lootTableItemMinInput);
		this.components.add(this.lootTableItemMaxInput);
		this.components.add(this.lootTableFortunableInput);
		this.components.add(this.lootTableSilkTouchableInput);
	}
	
	@Override
	public void saveObject() {
		if (this.lootTableBlockInput.getSelected() == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.lootTable.problem.title"), new TextComponentTranslation("gui.edit.lootTable.preset.blockItem.problem.noBlock"), I18n.format("gui.buttons.back")));
			return;
		}

		if (this.lootTableItemInput.getItemStack().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.lootTable.problem.title"), new TextComponentTranslation("gui.edit.lootTable.preset.blockItem.problem.noItem"), I18n.format("gui.buttons.back")));
			return;
		}
		
        this.lootTable.block = this.lootTableBlockInput.getSelected();
        this.lootTable.dropStack = this.lootTableItemInput.getItemStack();
        this.lootTable.itemMin = this.lootTableItemMinInput.getInteger();
        this.lootTable.itemMax = this.lootTableItemMaxInput.getInteger();
        this.lootTable.fortunable = this.lootTableFortunableInput.getBoolean();
        this.lootTable.silkTouchable = this.lootTableSilkTouchableInput.getBoolean();
		
		super.saveObject();
	}
	
	@Override
	protected void copyFromOther() {
		this.lootTableBlockInput.setDefaultSelected(this.copyFrom.block);
        this.lootTableItemInput.setDefaultItemStack(this.copyFrom.dropStack);
        this.lootTableItemMinInput.setDefaultInteger(this.copyFrom.itemMin);
        this.lootTableItemMaxInput.setDefaultInteger(this.copyFrom.itemMax);
        this.lootTableFortunableInput.setDefaultBoolean(this.copyFrom.fortunable);
        this.lootTableSilkTouchableInput.setDefaultBoolean(this.copyFrom.silkTouchable);
		
		super.copyFromOther();
	}
}
