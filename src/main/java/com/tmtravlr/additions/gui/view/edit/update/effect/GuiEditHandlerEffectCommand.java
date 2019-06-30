package com.tmtravlr.additions.gui.view.edit.update.effect;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectCommand;
import com.tmtravlr.additions.api.gui.IGuiEffectEditHandler;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates or updates a command effect
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class GuiEditHandlerEffectCommand implements IGuiEffectEditHandler {
	private GuiEdit editScreen;

	private GuiComponentDisplayText description;
	private GuiComponentStringInput commandInput;
	private GuiComponentStringInput commandNameInput;
	private GuiComponentBooleanInput hideFeedbackInput;
	private GuiComponentFloatInput chanceInput;

	@Override
	public List<IGuiViewComponent> getViewComponents(GuiEdit editScreen, Effect effect) {
		this.editScreen = editScreen;
		EffectCommand effectCommand = (effect instanceof EffectCommand) ? (EffectCommand) effect : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effect.command.description"));
		
		this.commandInput = new GuiComponentStringInput(I18n.format("gui.edit.effect.command.command.label"), editScreen);
		this.commandInput.setMaxStringLength(Integer.MAX_VALUE);
		this.commandInput.setRequired();
		if (effectCommand != null) {
			this.commandInput.setDefaultText(effectCommand.command);
		}
		
		this.commandNameInput = new GuiComponentStringInput(I18n.format("gui.edit.effect.command.commandSenderName.label"), editScreen);
		this.commandNameInput.setMaxStringLength(256);
		this.commandNameInput.setInfo(new TextComponentTranslation("gui.edit.effect.command.commandSenderName.info"));
		this.commandNameInput.setDefaultText(effectCommand != null ? effectCommand.commandSenderName : "@");
		
		this.hideFeedbackInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effect.command.hideFeedback.label"), editScreen);
		this.hideFeedbackInput.setDefaultBoolean(effectCommand != null ? effectCommand.hideFeedback : true);
		
		this.chanceInput = new GuiComponentFloatInput(I18n.format("gui.edit.effect.chance.label"), editScreen, false);
		this.chanceInput.setInfo(new TextComponentTranslation("gui.edit.effect.chance.info"));
		this.chanceInput.setMinimum(0);
		this.chanceInput.setMaximum(1);
		this.chanceInput.setDefaultFloat(effectCommand != null ? effectCommand.chance : 1);
		
		return Arrays.asList(this.description, this.commandInput, this.commandNameInput, this.hideFeedbackInput, this.chanceInput);
	}

	@Override
	public Effect createEffect() {
		if (this.editScreen == null || this.commandInput == null || this.commandNameInput == null || this.hideFeedbackInput  == null || this.chanceInput == null) {
			return null;
		}
		
		if (this.commandInput.getText().isEmpty()) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effect.problem.title"), new TextComponentTranslation("gui.edit.effect.command.problem.noCommand.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		EffectCommand effect = new EffectCommand();
		
		effect.command = this.commandInput.getText();
		effect.commandSenderName = this.commandNameInput.getText();
		effect.hideFeedback = this.hideFeedbackInput.getBoolean();
		effect.chance = this.chanceInput.getFloat();
		
		return effect;
	}

}
