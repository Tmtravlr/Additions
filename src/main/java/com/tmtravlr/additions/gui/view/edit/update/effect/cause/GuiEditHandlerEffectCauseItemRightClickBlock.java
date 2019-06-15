package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClickBlock;
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
 * Creates components and handles saving the right click block effect cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class GuiEditHandlerEffectCauseItemRightClickBlock implements IGuiEffectCauseEditHandler {
	
	private GuiEdit editScreen;
	private GuiComponentDisplayText description;
	private GuiComponentItemStackInput stackInput;
	private GuiComponentDropdownInput<HandType> handInput;
	private GuiComponentBooleanInput targetSelfInput;
	private GuiComponentBooleanInput againstBlockInput;
	private GuiComponentBooleanInput exceptReplaceableInput;

	@Override
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause) {
		this.editScreen = editScreen;
		EffectCauseItemRightClickBlock specificCause = (cause instanceof EffectCauseItemRightClickBlock) ? (EffectCauseItemRightClickBlock) cause : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effectCause.itemRightClickBlock.description"));
		
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
		
		this.exceptReplaceableInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.block.exceptReplaceable.label"), editScreen);
		this.exceptReplaceableInput.setInfo(new TextComponentTranslation("gui.edit.effectCause.block.exceptReplaceable.info"));
		this.exceptReplaceableInput.setHidden(true);
		if (specificCause != null) {
			this.exceptReplaceableInput.setDefaultBoolean(specificCause.exceptReplaceable);
		}
		
		this.againstBlockInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.block.againstBlock.label"), editScreen) {
			
			@Override
			public void setDefaultBoolean(boolean input) {
				GuiEditHandlerEffectCauseItemRightClickBlock.this.exceptReplaceableInput.setHidden(!input);
				super.setDefaultBoolean(input);
			}
			
		};
		this.againstBlockInput.setInfo(new TextComponentTranslation("gui.edit.effectCause.block.againstBlock.info"));
		if (specificCause != null) {
			this.againstBlockInput.setDefaultBoolean(specificCause.againstBlock);
		}
		
		return Arrays.asList(this.description, this.stackInput, this.handInput, this.targetSelfInput, this.againstBlockInput, this.exceptReplaceableInput);
	}

	@Override
	public EffectCause createEffectCause() {
		if (this.editScreen == null || this.stackInput == null || this.handInput == null || this.targetSelfInput == null || this.againstBlockInput == null || this.exceptReplaceableInput == null) {
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
		
		EffectCauseItemRightClickBlock cause = new EffectCauseItemRightClickBlock();
		cause.itemStack = this.stackInput.getItemStack();
		cause.handType = this.handInput.getSelected();
		cause.targetSelf = this.targetSelfInput.getBoolean();
		cause.againstBlock = this.againstBlockInput.getBoolean();
		cause.exceptReplaceable = this.exceptReplaceableInput.getBoolean();
		
		return cause;
	}

}
