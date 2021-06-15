package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockBroken;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBlockStateInfoInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentNBTInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates components and handles saving the block broken effect cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public class GuiEditHandlerEffectCauseBlockBroken implements IGuiEffectCauseEditHandler {
	
	private GuiEdit editScreen;
	private GuiComponentDisplayText description;
	private GuiComponentBlockStateInfoInput stateInput;
	private GuiComponentNBTInput nbtInput;
	private GuiComponentBooleanInput targetSelfInput;

	@Override
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause) {
		this.editScreen = editScreen;
		EffectCauseBlockBroken specificCause = (cause instanceof EffectCauseBlockBroken) ? (EffectCauseBlockBroken) cause : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effectCause.blockBroken.description"));
		
		this.stateInput = new GuiComponentBlockStateInfoInput(I18n.format("gui.edit.effectCause.block.blockState.label"), editScreen);
		this.stateInput.setRequired();
		if (specificCause != null) {
			this.stateInput.setDefaultBlockStateInfo(specificCause.blockState);
		}
		
		this.nbtInput = new GuiComponentNBTInput(I18n.format("gui.edit.effectCause.block.blockTag.label"), editScreen);
		if (specificCause != null) {
			this.nbtInput.setDefaultText(specificCause.blockTag != null ? specificCause.blockTag.toString() : "{}");
		}
		
		this.targetSelfInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.targetSelf.label"), editScreen);
		if (specificCause != null) {
			this.targetSelfInput.setDefaultBoolean(specificCause.targetSelf);
		}
		
		return Arrays.asList(this.description, this.stateInput, this.nbtInput, this.targetSelfInput);
	}

	@Override
	public EffectCause createEffectCause() {
		if (this.editScreen == null || this.stateInput == null || this.targetSelfInput == null) {
			return null;
		}
		
		if (this.stateInput.getBlockStateInfo() == null) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.block.problem.noBlockState.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		EffectCauseBlockBroken cause = new EffectCauseBlockBroken();
		cause.blockState = this.stateInput.getBlockStateInfo();
		cause.blockTag = this.nbtInput.getTag();
		cause.targetSelf = this.targetSelfInput.getBoolean();
		
		return cause;
	}

}
