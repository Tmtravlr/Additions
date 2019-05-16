package com.tmtravlr.additions.gui.view.edit.update.effect;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectPotion;
import com.tmtravlr.additions.api.gui.IGuiEffectEditHandler;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentPotionEffectInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputPotionType;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiEditHandlerEffectPotion implements IGuiEffectEditHandler {
	private GuiEdit editScreen;

	private GuiComponentDisplayText description;
	private GuiComponentDropdownInput<PotionSelection> potionSelectionInput;
	private GuiComponentDropdownInputPotionType potionTypeInput;
	private GuiComponentPotionEffectInput potionEffectInput;
	private GuiComponentIntegerInput delayInput;
	private GuiComponentFloatInput chanceInput;

	@Override
	public List<IGuiViewComponent> getViewComponents(GuiEdit editScreen, Effect effect) {
		this.editScreen = editScreen;
		EffectPotion effectPotion = (effect instanceof EffectPotion) ? (EffectPotion) effect : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effect.potion.description"));
		
		this.potionTypeInput = new GuiComponentDropdownInputPotionType(I18n.format("gui.edit.effect.potion.type.preset.label"), editScreen);
		this.potionTypeInput.setRequired();
		this.potionTypeInput.setHidden(true);
		if (effectPotion != null) {
			this.potionTypeInput.setDefaultSelected(effectPotion.potionType);
		}
		
		this.potionEffectInput = new GuiComponentPotionEffectInput(I18n.format("gui.edit.effect.potion.type.custom.label"), editScreen);
		this.potionEffectInput.setRequired();
		this.potionEffectInput.setHidden(true);
		if (effectPotion != null) {
			this.potionEffectInput.setDefaultPotionEffect(effectPotion.potion);
		}
		
		this.potionSelectionInput = new GuiComponentDropdownInput<PotionSelection>(I18n.format("gui.edit.effect.potion.type.label"), editScreen) {
			
			@Override
			public String getSelectionName(PotionSelection selection) {
				return selection.getLabel();
			}
			
			@Override
			public void setDefaultSelected(PotionSelection selection) {
				if (selection != this.getSelected()) {
					GuiEditHandlerEffectPotion.this.potionTypeInput.setHidden(true);
					GuiEditHandlerEffectPotion.this.potionEffectInput.setHidden(true);
					
					if (selection == PotionSelection.PRESET) {
						GuiEditHandlerEffectPotion.this.potionTypeInput.setHidden(false);
					}
					
					if (selection == PotionSelection.CUSTOM) {
						GuiEditHandlerEffectPotion.this.potionEffectInput.setHidden(false);
					}
				}
				super.setDefaultSelected(selection);
			}
			
		};
		this.potionSelectionInput.setRequired();
		this.potionSelectionInput.setSelections(PotionSelection.values());
		this.potionSelectionInput.setDefaultSelected((effectPotion != null && effectPotion.potionType != null) ? PotionSelection.PRESET : PotionSelection.CUSTOM);
		
		this.delayInput = new GuiComponentIntegerInput(I18n.format("gui.edit.effect.potion.delay.label"), editScreen, false);
		this.delayInput.setInfo(new TextComponentTranslation("gui.edit.effect.potion.delay.info"));
		this.delayInput.setMinimum(0);
		this.delayInput.setDefaultInteger(effectPotion != null ? effectPotion.reapplicationDelay : 0);
		
		this.chanceInput = new GuiComponentFloatInput(I18n.format("gui.edit.effect.chance.label"), editScreen, false);
		this.chanceInput.setInfo(new TextComponentTranslation("gui.edit.effect.chance.info"));
		this.chanceInput.setMinimum(0);
		this.chanceInput.setMaximum(1);
		this.chanceInput.setDefaultFloat(effectPotion != null ? effectPotion.chance : 1);
		
		return Arrays.asList(this.description, this.potionSelectionInput, this.potionTypeInput, this.potionEffectInput, this.delayInput, this.chanceInput);
	}

	@Override
	public Effect createEffect() {
		if (this.editScreen == null || this.potionSelectionInput == null || this.potionEffectInput == null || this.potionTypeInput == null) {
			return null;
		}
		
		if (this.potionSelectionInput.getSelected() == null) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effect.problem.title"), new TextComponentTranslation("gui.edit.effect.potion.problem.noType.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		if (this.potionSelectionInput.getSelected() == PotionSelection.PRESET && this.potionTypeInput.getSelected() == null) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effect.problem.title"), new TextComponentTranslation("gui.edit.effect.potion.problem.noPreset.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		if (this.potionSelectionInput.getSelected() == PotionSelection.CUSTOM && this.potionEffectInput.getPotionEffect() == null) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effect.problem.title"), new TextComponentTranslation("gui.edit.effect.potion.problem.noCustom.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		EffectPotion effect = new EffectPotion();
		
		if (this.potionSelectionInput.getSelected() == PotionSelection.PRESET) {
			effect.potionType = this.potionTypeInput.getSelected();
		}
		
		if (this.potionSelectionInput.getSelected() == PotionSelection.CUSTOM) {
			effect.potion = this.potionEffectInput.getPotionEffect();
		}
		
		effect.reapplicationDelay = this.delayInput.getInteger();
		effect.chance = this.chanceInput.getFloat();
		
		return effect;
	}
	
	private enum PotionSelection {
		CUSTOM(I18n.format("gui.edit.effect.potion.type.custom.label")),
		PRESET(I18n.format("gui.edit.effect.potion.type.preset.label"));
		
		private String label;
		
		PotionSelection(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return this.label;
		}
	}

}
