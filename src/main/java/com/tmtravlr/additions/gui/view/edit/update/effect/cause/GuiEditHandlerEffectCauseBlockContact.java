package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockContact;
import com.tmtravlr.additions.addon.effects.cause.HandType;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBlockStateInfoInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentNBTInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates components and handles saving the block contact effect cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public class GuiEditHandlerEffectCauseBlockContact implements IGuiEffectCauseEditHandler {
	
	private GuiEdit editScreen;
	private GuiComponentDisplayText description;
	private GuiComponentBlockStateInfoInput stateInput;
	private GuiComponentNBTInput nbtInput;
	private GuiComponentBooleanInput targetSelfInput;
	private GuiComponentDropdownInput<EffectCauseBlockContact.ContactType> contactTypeInput;

	@Override
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause) {
		this.editScreen = editScreen;
		EffectCauseBlockContact specificCause = (cause instanceof EffectCauseBlockContact) ? (EffectCauseBlockContact) cause : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effectCause.BlockContact.description"));
		
		this.stateInput = new GuiComponentBlockStateInfoInput(I18n.format("gui.edit.effectCause.block.blockState.label"), editScreen);
		this.stateInput.setRequired();
		if (specificCause != null) {
			this.stateInput.setDefaultBlockStateInfo(specificCause.blockState);
		}
		
		this.nbtInput = new GuiComponentNBTInput(I18n.format("gui.edit.effectCause.block.blockTag.label"), editScreen);
		if (specificCause != null) {
			this.nbtInput.setDefaultText(specificCause.blockTag != null ? specificCause.blockTag.toString() : "{}");
		}
		
		this.contactTypeInput = new GuiComponentDropdownInput<EffectCauseBlockContact.ContactType>(I18n.format("gui.edit.effectCause.blockContact.standing.label"), editScreen) {
			
			@Override
			public String getSelectionName(EffectCauseBlockContact.ContactType selected) {
				return I18n.format("gui.edit.effectCause.blockContact.standing." + selected.toString().toLowerCase() + ".label");
			}
			
		};
		this.contactTypeInput.setSelections(EffectCauseBlockContact.ContactType.values());
		this.contactTypeInput.setRequired();
		this.contactTypeInput.disallowDelete();
		if (specificCause != null) {
			this.contactTypeInput.setDefaultSelected(specificCause.contactType);
		} else {
			this.contactTypeInput.setDefaultSelected(EffectCauseBlockContact.ContactType.BOTH);
		}
		
		this.targetSelfInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.targetSelf.label"), editScreen);
		if (specificCause != null) {
			this.targetSelfInput.setDefaultBoolean(specificCause.targetSelf);
		}
		
		return Arrays.asList(this.description, this.stateInput, this.nbtInput, this.contactTypeInput, this.targetSelfInput);
	}

	@Override
	public EffectCause createEffectCause() {
		if (this.editScreen == null || this.stateInput == null || this.contactTypeInput == null || this.targetSelfInput == null) {
			return null;
		}
		
		if (this.stateInput.getBlockStateInfo() == null) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.block.problem.noBlockState.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		if (this.contactTypeInput.getSelected() == null) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.blockContact.problem.noStanding.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		EffectCauseBlockContact cause = new EffectCauseBlockContact();
		cause.blockState = this.stateInput.getBlockStateInfo();
		cause.blockTag = this.nbtInput.getTag();
		cause.targetSelf = this.targetSelfInput.getBoolean();
		
		return cause;
	}

}
