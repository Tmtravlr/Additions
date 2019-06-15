package com.tmtravlr.additions.gui.view.edit.update.effect;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectConsumeItem;
import com.tmtravlr.additions.api.gui.IGuiEffectEditHandler;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates or updates a consume item effect
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class GuiEditHandlerEffectConsumeItem implements IGuiEffectEditHandler {
	private GuiEdit editScreen;

	private GuiComponentDisplayText description;
	private GuiComponentIntegerInput amountInput;
	private GuiComponentFloatInput chanceInput;

	@Override
	public List<IGuiViewComponent> getViewComponents(GuiEdit editScreen, Effect effect) {
		this.editScreen = editScreen;
		EffectConsumeItem effectConsumeItem = (effect instanceof EffectConsumeItem) ? (EffectConsumeItem) effect : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effect.command.description"));
		
		this.amountInput = new GuiComponentIntegerInput(I18n.format("gui.edit.effect.consumeItem.amount.label"), editScreen, false);
		this.amountInput.setInfo(new TextComponentTranslation("gui.edit.effect.consumeItem.amount.info"));
		this.amountInput.setMinimum(1);
		this.amountInput.setMaximum(64);
		this.amountInput.setDefaultInteger(effectConsumeItem != null ? effectConsumeItem.amount : 1);
		
		this.chanceInput = new GuiComponentFloatInput(I18n.format("gui.edit.effect.chance.label"), editScreen, false);
		this.chanceInput.setInfo(new TextComponentTranslation("gui.edit.effect.chance.info"));
		this.chanceInput.setMinimum(0);
		this.chanceInput.setMaximum(1);
		this.chanceInput.setDefaultFloat(effectConsumeItem != null ? effectConsumeItem.chance : 1);
		
		return Arrays.asList(this.description, this.amountInput, this.chanceInput);
	}

	@Override
	public Effect createEffect() {
		if (this.editScreen == null || this.amountInput == null || this.chanceInput == null) {
			return null;
		}
		
		EffectConsumeItem effect = new EffectConsumeItem();
		
		effect.amount = this.amountInput.getInteger();
		effect.chance = this.chanceInput.getFloat();
		
		return effect;
	}

}
