package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemLeftClick;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates components and handles saving the left click effect cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class GuiEditHandlerEffectCauseItemLeftClick implements IGuiEffectCauseEditHandler {
	
	private GuiEdit editScreen;
	private GuiComponentDisplayText description;
	private GuiComponentItemStackInput stackInput;

	@Override
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause) {
		this.editScreen = editScreen;
		EffectCauseItemLeftClick specificCause = (cause instanceof EffectCauseItemLeftClick) ? (EffectCauseItemLeftClick) cause : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effectCause.itemLeftClick.description"));
		
		this.stackInput = new GuiComponentItemStackInput(I18n.format("gui.edit.effectCause.item.item.label"), editScreen);
		this.stackInput.setRequired();
		this.stackInput.enableAnyDamage();
		this.stackInput.disableCount();
		if (specificCause != null) {
			this.stackInput.setDefaultItemStack(specificCause.itemStack);
		}
		
		return Arrays.asList(this.description, this.stackInput);
	}

	@Override
	public EffectCause createEffectCause() {
		if (this.editScreen == null || this.stackInput == null) {
			return null;
		}
		
		if (this.stackInput.getItemStack().isEmpty()) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.item.problem.noItem.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		EffectCauseItemLeftClick cause = new EffectCauseItemLeftClick();
		cause.itemStack = this.stackInput.getItemStack();
		
		return cause;
	}

}
