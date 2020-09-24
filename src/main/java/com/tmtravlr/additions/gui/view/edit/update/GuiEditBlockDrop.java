package com.tmtravlr.additions.gui.view.edit.update;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.loottables.LootTableAdded;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItemDrop;
import com.tmtravlr.additions.addon.loottables.LootTablePresetBlockItself;
import com.tmtravlr.additions.addon.loottables.LootTablePresetEmpty;
import com.tmtravlr.additions.addon.loottables.LootTablePresetOtherLootTable;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputLootTable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Edit screen for block drops
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public abstract class GuiEditBlockDrop extends GuiEditUpdate {
	
	protected final Addon addon;
	protected final IBlockAdded block;
	protected final LootTableAdded lootTable;
	
	protected GuiComponentDropdownInput<DropTypeSelection> dropTypeInput;
	protected GuiComponentDisplayText dropMessageNothing;
	protected GuiComponentDisplayText dropMessageItself;
	protected GuiComponentDisplayText dropMessageCustom;
	protected GuiComponentSuggestionInputLootTable dropOtherLootTableInput;
	protected GuiComponentItemStackInput dropItemInput;
	protected GuiComponentIntegerInput dropItemMinInput;
	protected GuiComponentIntegerInput dropItemMaxInput;
	protected GuiComponentBooleanInput dropFortunableInput;
	protected GuiComponentBooleanInput dropSilkTouchableInput;
	protected GuiComponentIntegerInput dropXpMinInput;
	protected GuiComponentIntegerInput dropXpMaxInput;
	protected GuiComponentDropdownInput<ExplosionDropTypeSelection> explosionDropTypeInput;

	public GuiEditBlockDrop(GuiScreen parentScreen, LootTableAdded lootTable, IBlockAdded block, Addon addon) {
		super(parentScreen, I18n.format("gui.edit.blockDrop.title"));
		this.lootTable = lootTable;
		this.block = block;
		this.addon = addon;
	}
	
	@Override
	public void initComponents() {
		this.dropMessageNothing = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.blockDrop.nothing.message"));
		this.dropMessageNothing.setHidden(true);
		
		this.dropMessageItself = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.blockDrop.itself.message"));
		this.dropMessageItself.setHidden(true);
		
		this.dropMessageCustom = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.blockDrop.custom.message"));
		this.dropMessageCustom.setHidden(true);
		
		this.dropOtherLootTableInput = new GuiComponentSuggestionInputLootTable(I18n.format("gui.edit.lootTable.preset.otherLootTable.lootTable.label"), this);
		this.dropOtherLootTableInput.removeSuggestion(this.lootTable.location.toString());
		this.dropOtherLootTableInput.setRequired();
		this.dropOtherLootTableInput.setHidden(true);
		
		this.dropItemInput = new GuiComponentItemStackInput(I18n.format("gui.edit.lootTable.preset.blockItem.item.label"), this);
		this.dropItemInput.disableCount();
		this.dropItemInput.setRequired();
		this.dropItemInput.setHidden(true);
		
		this.dropItemMinInput = new GuiComponentIntegerInput(I18n.format("gui.edit.lootTable.preset.blockItem.itemMin.label"), this, false);
		this.dropItemMinInput.setHidden(true);
		this.dropItemMinInput.setDefaultInteger(1);
		
		this.dropItemMaxInput = new GuiComponentIntegerInput(I18n.format("gui.edit.lootTable.preset.blockItem.itemMax.label"), this, false);
		this.dropItemMaxInput.setHidden(true);
		this.dropItemMaxInput.setMinimum(1);
		this.dropItemMaxInput.setDefaultInteger(1);
		
		this.dropFortunableInput = new GuiComponentBooleanInput(I18n.format("gui.edit.lootTable.preset.blockItem.fortunable.label"), this);
		this.dropFortunableInput.setHidden(true);
		this.dropFortunableInput.setDefaultBoolean(true);
		
		this.dropSilkTouchableInput = new GuiComponentBooleanInput(I18n.format("gui.edit.lootTable.preset.blockItem.silkTouchable.label"), this);
		this.dropSilkTouchableInput.setHidden(true);
		this.dropSilkTouchableInput.setDefaultBoolean(true);
		
		this.dropXpMinInput = new GuiComponentIntegerInput(I18n.format("gui.edit.blockDrop.xp.min.label"), this, false);
		this.dropXpMinInput.setInfo(new TextComponentTranslation("gui.edit.blockDrop.xp.min.info"));
		this.dropXpMinInput.setDefaultInteger(this.block.getXpDroppedMin());
		this.dropXpMinInput.setHidden(true);
		
		this.dropXpMaxInput = new GuiComponentIntegerInput(I18n.format("gui.edit.blockDrop.xp.max.label"), this, false);
		this.dropXpMaxInput.setInfo(new TextComponentTranslation("gui.edit.blockDrop.xp.max.info"));
		this.dropXpMaxInput.setDefaultInteger(this.block.getXpDroppedMax());
		this.dropXpMaxInput.setHidden(true);
		
		this.explosionDropTypeInput = new GuiComponentDropdownInput<ExplosionDropTypeSelection>(I18n.format("gui.edit.block.drop.explosion.label"), this);
		this.explosionDropTypeInput.setSelections(ExplosionDropTypeSelection.values());
		this.explosionDropTypeInput.setDefaultSelected(ExplosionDropTypeSelection.getFromBlock(this.block));
		this.explosionDropTypeInput.disallowDelete();
		
		this.dropTypeInput = new GuiComponentDropdownInput<DropTypeSelection>(I18n.format("gui.edit.blockDrop.type.label"), this) {
			
			@Override
			public String getSelectionName(DropTypeSelection selected) {
				return selected == null ? "" : selected.getLabel();
			}
			
			@Override
			public void setDefaultSelected(DropTypeSelection selected) {
				if (this.getSelected() != selected) {
					GuiEditBlockDrop.this.handleDropTypeSelected(this.getSelected(), selected);
				}
				super.setDefaultSelected(selected);
			}
			
		};
		this.dropTypeInput.setRequired();
		this.dropTypeInput.setSelections(DropTypeSelection.DROP_TYPE_NOTHING, DropTypeSelection.DROP_TYPE_ITSELF, DropTypeSelection.DROP_TYPE_LOOT_TABLE, DropTypeSelection.DROP_TYPE_ITEM);
		this.initializeDropTypeSelection();
		
		this.components.add(this.dropTypeInput);
		this.components.add(this.dropMessageNothing);
		this.components.add(this.dropMessageItself);
		this.components.add(this.dropMessageCustom);
		this.components.add(this.dropOtherLootTableInput);
		this.components.add(this.dropItemInput);
		this.components.add(this.dropItemMinInput);
		this.components.add(this.dropItemMaxInput);
		this.components.add(this.dropFortunableInput);
		this.components.add(this.dropSilkTouchableInput);
		this.components.add(this.dropXpMinInput);
		this.components.add(this.dropXpMaxInput);
		this.components.add(this.explosionDropTypeInput);
	}

	@Override
    public void saveObject() {
		if (!this.validateInput()) {
			return;
		}
		
		this.updatePresetInLootTable();
		this.block.setXpDroppedMin(this.dropXpMinInput.getInteger());
		this.block.setXpDroppedMax(this.dropXpMaxInput.getInteger());
		this.explosionDropTypeInput.getSelected().setToBlock(this.block);
        
        if (this.parentScreen instanceof GuiView) {
        	((GuiView) this.parentScreen).refreshView();
        }
        
        this.handleSaved();
    }
	
	protected void handleSaved() {
    	this.mc.displayGuiScreen(this.parentScreen);
	}
	
	protected void handleDropTypeSelected(DropTypeSelection oldSelection, DropTypeSelection newSelection) {
		if (oldSelection == DropTypeSelection.DROP_TYPE_NOTHING) {
			this.dropMessageNothing.setHidden(true);
		} else if (oldSelection == DropTypeSelection.DROP_TYPE_CUSTOM) {
			this.dropMessageCustom.setHidden(true);
		} else if (oldSelection == DropTypeSelection.DROP_TYPE_ITSELF) {
			this.dropMessageItself.setHidden(true);
			this.dropXpMinInput.setHidden(true);
			this.dropXpMaxInput.setHidden(true);
			this.explosionDropTypeInput.setHidden(true);
		} else if (oldSelection == DropTypeSelection.DROP_TYPE_LOOT_TABLE) {
			this.dropOtherLootTableInput.setHidden(true);
			this.dropXpMinInput.setHidden(true);
			this.dropXpMaxInput.setHidden(true);
			this.explosionDropTypeInput.setHidden(true);
		} else if (oldSelection == DropTypeSelection.DROP_TYPE_ITEM) {
			this.dropItemInput.setHidden(true);
			this.dropItemMinInput.setHidden(true);
			this.dropItemMaxInput.setHidden(true);
			this.dropFortunableInput.setHidden(true);
			this.dropSilkTouchableInput.setHidden(true);
			this.dropXpMinInput.setHidden(true);
			this.dropXpMaxInput.setHidden(true);
			this.explosionDropTypeInput.setHidden(true);
		}
		
		if (newSelection == DropTypeSelection.DROP_TYPE_NOTHING) {
			this.dropMessageNothing.setHidden(false);
		} else if (newSelection == DropTypeSelection.DROP_TYPE_CUSTOM) {
			this.dropMessageCustom.setHidden(false);
			this.dropXpMinInput.setHidden(false);
			this.dropXpMaxInput.setHidden(false);
			this.explosionDropTypeInput.setHidden(false);
		} else if (newSelection == DropTypeSelection.DROP_TYPE_ITSELF) {
			this.dropMessageItself.setHidden(false);
			this.explosionDropTypeInput.setHidden(false);
		} else if (newSelection == DropTypeSelection.DROP_TYPE_LOOT_TABLE) {
			this.dropOtherLootTableInput.setHidden(false);
			this.dropXpMinInput.setHidden(false);
			this.dropXpMaxInput.setHidden(false);
			this.explosionDropTypeInput.setHidden(false);
		} else if (newSelection == DropTypeSelection.DROP_TYPE_ITEM) {
			this.dropItemInput.setHidden(false);
			this.dropItemMinInput.setHidden(false);
			this.dropItemMaxInput.setHidden(false);
			this.dropFortunableInput.setHidden(false);
			this.dropSilkTouchableInput.setHidden(false);
			this.dropXpMinInput.setHidden(false);
			this.dropXpMaxInput.setHidden(false);
			this.explosionDropTypeInput.setHidden(false);
		}
	}
	
	protected void initializeDropTypeSelection() {
		if (this.lootTable.preset instanceof LootTablePresetEmpty) {
			this.dropTypeInput.setDefaultSelected(DropTypeSelection.DROP_TYPE_NOTHING);
		} else if (this.lootTable.preset instanceof LootTablePresetBlockItself && ((LootTablePresetBlockItself)this.lootTable.preset).block == this.block) {
			this.dropTypeInput.setDefaultSelected(DropTypeSelection.DROP_TYPE_ITSELF);
		} else if (this.lootTable.preset instanceof LootTablePresetOtherLootTable) {
			this.dropTypeInput.setDefaultSelected(DropTypeSelection.DROP_TYPE_LOOT_TABLE);
			this.dropOtherLootTableInput.setDefaultText(((LootTablePresetOtherLootTable)this.lootTable.preset).otherLootTable.toString());
		} else if (this.lootTable.preset instanceof LootTablePresetBlockItemDrop && ((LootTablePresetBlockItemDrop)this.lootTable.preset).block == this.block) {
			LootTablePresetBlockItemDrop preset = (LootTablePresetBlockItemDrop) this.lootTable.preset;
			this.dropTypeInput.setDefaultSelected(DropTypeSelection.DROP_TYPE_ITEM);
			this.dropItemInput.setDefaultItemStack(preset.dropStack);
			this.dropItemMinInput.setDefaultInteger(preset.itemMin);
			this.dropItemMaxInput.setDefaultInteger(preset.itemMax);
			this.dropFortunableInput.setDefaultBoolean(preset.fortunable);
			this.dropSilkTouchableInput.setDefaultBoolean(preset.silkTouchable);
		} else {
			this.dropTypeInput.setDefaultSelected(DropTypeSelection.DROP_TYPE_CUSTOM);
		}
	}
	
	protected boolean validateInput() {
		DropTypeSelection type = this.dropTypeInput.getSelected();
		
		if (type == DropTypeSelection.DROP_TYPE_LOOT_TABLE && this.dropOtherLootTableInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.lootTable.problem.title"), new TextComponentTranslation("gui.edit.lootTable.preset.otherLootTable.problem.noLootTable"), I18n.format("gui.buttons.back")));
			return false;	
		} else if (type == DropTypeSelection.DROP_TYPE_ITEM && this.dropItemInput.getItemStack().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.lootTable.problem.title"), new TextComponentTranslation("gui.edit.lootTable.preset.blockItem.problem.noItem"), I18n.format("gui.buttons.back")));
			return false;
		}
		
		return true;
	}
	
	protected void updatePresetInLootTable() {
		DropTypeSelection type = this.dropTypeInput.getSelected();
		ResourceLocation presetName = this.lootTable.location;
		
		if (type == DropTypeSelection.DROP_TYPE_NOTHING) {
			LootTablePresetEmpty preset = new LootTablePresetEmpty();
			preset.id = presetName;
			this.lootTable.preset = preset;
		} else if (type == DropTypeSelection.DROP_TYPE_LOOT_TABLE) {
			LootTablePresetOtherLootTable preset = new LootTablePresetOtherLootTable();
			preset.id = presetName;
			preset.otherLootTable = new ResourceLocation(this.dropOtherLootTableInput.getText());
			this.lootTable.preset = preset;
		} else if (type == DropTypeSelection.DROP_TYPE_ITSELF) {
			LootTablePresetBlockItself preset = new LootTablePresetBlockItself();
			preset.id = presetName;
			preset.block = this.block.getAsBlock();
			this.lootTable.preset = preset;
		} else if (type == DropTypeSelection.DROP_TYPE_ITEM) {
			LootTablePresetBlockItemDrop preset = new LootTablePresetBlockItemDrop();
			preset.id = presetName;
			preset.block = this.block.getAsBlock();
			preset.dropStack = this.dropItemInput.getItemStack();
			preset.itemMin = this.dropItemMinInput.getInteger();
			preset.itemMax = this.dropItemMaxInput.getInteger();
			preset.fortunable = this.dropFortunableInput.getBoolean();
			preset.silkTouchable = this.dropSilkTouchableInput.getBoolean();
			this.lootTable.preset = preset;
		}
	}
	
	protected enum DropTypeSelection {
		DROP_TYPE_NOTHING(I18n.format("gui.edit.blockDrop.type.nothing")),
		DROP_TYPE_ITSELF(I18n.format("gui.edit.blockDrop.type.itself")),
		DROP_TYPE_ITEM(I18n.format("gui.edit.blockDrop.type.item")),
		DROP_TYPE_LOOT_TABLE(I18n.format("gui.edit.blockDrop.type.lootTable")),
		DROP_TYPE_CUSTOM(I18n.format("gui.edit.blockDrop.type.custom"));
		
		private String label;
		
		private DropTypeSelection(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return this.label;
		}
	}
	
	protected enum ExplosionDropTypeSelection {
		EXPLOSION_DROP_TYPE_NORMAL(I18n.format("gui.edit.block.drop.explosion.option.normal")),
		EXPLOSION_DROP_TYPE_NEVER(I18n.format("gui.edit.block.drop.explosion.option.never")),
		EXPLOSION_DROP_TYPE_ALWAYS(I18n.format("gui.edit.block.drop.explosion.option.always"));
		
		private String label;
		
		private ExplosionDropTypeSelection(String label) {
			this.label = label;
		}
		
		@Override
		public String toString() {
			return this.label;
		}
		
		public void setToBlock(IBlockAdded block) {
			block.setDroppedFromExplosions(this == EXPLOSION_DROP_TYPE_NORMAL ? null : this == EXPLOSION_DROP_TYPE_ALWAYS ? true : false);
		}
		
		public static ExplosionDropTypeSelection getFromBlock(IBlockAdded block) {
			return block.getDroppedFromExplosions() == null ? EXPLOSION_DROP_TYPE_NORMAL : block.getDroppedFromExplosions() ? EXPLOSION_DROP_TYPE_ALWAYS : EXPLOSION_DROP_TYPE_NEVER;
		}
	}

}
