package com.tmtravlr.additions.gui.view.edit.update.effect;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectLootTableAt;
import com.tmtravlr.additions.api.gui.IGuiEffectEditHandler;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputLootTable;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates or updates a loot table effect
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class GuiEditHandlerEffectLootTableAt implements IGuiEffectEditHandler {
	private GuiEdit editScreen;

	private GuiComponentDisplayText description;
	private GuiComponentSuggestionInputLootTable lootTableInput;
	private GuiComponentBooleanInput useLootTableSeedInput;
	private GuiComponentIntegerInput lootTableSeedInput;
	private GuiComponentFloatInput chanceInput;

	@Override
	public List<IGuiViewComponent> getViewComponents(GuiEdit editScreen, Effect effect) {
		this.editScreen = editScreen;
		EffectLootTableAt effectLootTable = (effect instanceof EffectLootTableAt) ? (EffectLootTableAt) effect : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effect.lootTableAt.description"));
		
		this.lootTableInput = new GuiComponentSuggestionInputLootTable(I18n.format("gui.edit.effect.lootTable.lootTable.label"), editScreen);
		this.lootTableInput.setRequired();
		if (effectLootTable != null) {
			this.lootTableInput.setDefaultText(effectLootTable.lootTable.toString());
		}
		
		this.lootTableSeedInput = new GuiComponentIntegerInput(I18n.format("gui.edit.effect.lootTable.lootTableSeed.label"), editScreen, true);
		this.lootTableSeedInput.setHidden(true);
		if (effectLootTable != null) {
			this.lootTableSeedInput.setDefaultInteger(effectLootTable.lootSeed != null ? effectLootTable.lootSeed : 0);
		}
		
		this.useLootTableSeedInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effect.lootTable.useLootTableSeed.label"), editScreen) {
			@Override
			public void setDefaultBoolean(boolean input) {
				GuiEditHandlerEffectLootTableAt.this.lootTableSeedInput.setHidden(!input);
				super.setDefaultBoolean(input);
			}
		};
		if (effectLootTable != null) {
			this.useLootTableSeedInput.setDefaultBoolean(effectLootTable.lootSeed != null);
		}
		
		this.chanceInput = new GuiComponentFloatInput(I18n.format("gui.edit.effect.chance.label"), editScreen, false);
		this.chanceInput.setInfo(new TextComponentTranslation("gui.edit.effect.chance.info"));
		this.chanceInput.setMinimum(0);
		this.chanceInput.setMaximum(1);
		this.chanceInput.setDefaultFloat(effectLootTable != null ? effectLootTable.chance : 1);
		
		return Arrays.asList(this.description, this.lootTableInput, this.useLootTableSeedInput, this.lootTableSeedInput, this.chanceInput);
	}

	@Override
	public Effect createEffect() {
		if (this.editScreen == null || this.lootTableInput == null || this.useLootTableSeedInput == null || this.lootTableSeedInput == null || this.chanceInput == null) {
			return null;
		}
		
		if (this.lootTableInput.getText().isEmpty()) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effect.problem.title"), new TextComponentTranslation("gui.edit.effect.lootTable.problem.noLootTable.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		EffectLootTableAt effect = new EffectLootTableAt();
		
		effect.lootTable = new ResourceLocation(this.lootTableInput.getText());
		effect.lootSeed = !this.useLootTableSeedInput.getBoolean() ? null : this.lootTableSeedInput.getInteger();
		effect.chance = this.chanceInput.getFloat();
		
		return effect;
	}

}
