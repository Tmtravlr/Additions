package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClickEntity;
import com.tmtravlr.additions.addon.effects.cause.HandType;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates components and handles saving the right click entity effect cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class GuiEditHandlerEffectCauseItemRightClickEntity implements IGuiEffectCauseEditHandler {
	
	private GuiEdit editScreen;
	private GuiComponentDisplayText description;
	private GuiComponentItemStackInput stackInput;
	private GuiComponentBooleanInput targetSelfInput;
	private GuiComponentDropdownInput<HandType> handInput;

	@Override
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause) {
		this.editScreen = editScreen;
		EffectCauseItemRightClickEntity specificCause = (cause instanceof EffectCauseItemRightClickEntity) ? (EffectCauseItemRightClickEntity) cause : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effectCause.itemRightClickEntity.description"));
		
		this.stackInput = new GuiComponentItemStackInput(I18n.format("gui.edit.effectCause.item.item.label"), editScreen);
		this.stackInput.setRequired();
		this.stackInput.enableAnyDamage();
		this.stackInput.disableCount();
		if (specificCause != null) {
			this.stackInput.setDefaultItemStack(specificCause.itemStack);
		}
		
		this.handInput = new GuiComponentDropdownInput<HandType>(I18n.format("gui.edit.effectCause.held.hand.label"), editScreen) {
			
			@Override
			public String getSelectionName(HandType selected) {
				switch(selected) {
				case MAINHAND:
					return HandTypeLabel.MAINHAND_LABEL.getLabel();
				case OFFHAND:
					return HandTypeLabel.OFFHAND_LABEL.getLabel();
				case BOTH:
					return HandTypeLabel.BOTH_HANDS_LABEL.getLabel();
				default:
					return "";	
				}
			}
			
		};
		this.handInput.setSelections(HandType.values());
		this.handInput.setRequired();
		this.handInput.disallowDelete();
		if (specificCause != null) {
			this.handInput.setDefaultSelected(specificCause.handType);
		} else {
			this.handInput.setDefaultSelected(HandType.BOTH);
		}
		
		this.targetSelfInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.targetSelf.label"), editScreen);
		if (specificCause != null) {
			this.targetSelfInput.setDefaultBoolean(specificCause.targetSelf);
		}
		
		return Arrays.asList(this.description, this.stackInput, this.handInput, this.targetSelfInput);
	}

	@Override
	public EffectCause createEffectCause() {
		if (this.editScreen == null || this.stackInput == null || this.handInput == null || this.targetSelfInput == null) {
			return null;
		}
		
		if (this.stackInput.getItemStack().isEmpty()) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.item.problem.noItem.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		if (this.handInput.getSelected() == null) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.held.problem.noHand.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		EffectCauseItemRightClickEntity cause = new EffectCauseItemRightClickEntity();
		cause.itemStack = this.stackInput.getItemStack();
		cause.handType = this.handInput.getSelected();
		cause.targetSelf = this.targetSelfInput.getBoolean();
		
		return cause;
	}

}
