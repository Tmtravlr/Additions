package com.tmtravlr.additions.gui.view.edit.update.effect;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemInHand;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentItemStackInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates components and handles saving the in hand effect.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class GuiEditHandlerEffectCauseItemInHand implements IGuiEffectCauseEditHandler {
	private static final String MAINHAND_LABEL = I18n.format("gui.edit.effectCause.held.hand.mainhand.label");
	private static final String OFFHAND_LABEL = I18n.format("gui.edit.effectCause.held.hand.offhand.label");
	private static final String BOTH_HANDS_LABEL = I18n.format("gui.edit.effectCause.held.hand.both.label");
	
	private GuiEdit editScreen;
	private GuiComponentDisplayText description;
	private GuiComponentItemStackInput stackInput;
	private GuiComponentDropdownInput<EffectCauseItemInHand.HandType> handInput;

	@Override
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause) {
		this.editScreen = editScreen;
		EffectCauseItemInHand causeHeld = (cause instanceof EffectCauseItemInHand) ? (EffectCauseItemInHand) cause : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effectCause.held.description"));
		
		this.stackInput = new GuiComponentItemStackInput(I18n.format("gui.edit.effectCause.held.itemStack.label"), editScreen);
		this.stackInput.setRequired();
		this.stackInput.disableCount();
		if (causeHeld != null) {
			this.stackInput.setDefaultItemStack(causeHeld.itemStack);
		}
		
		this.handInput = new GuiComponentDropdownInput<EffectCauseItemInHand.HandType>(I18n.format("gui.edit.effectCause.held.hand.label"), editScreen) {
			
			@Override
			public String getSelectionName(EffectCauseItemInHand.HandType selected) {
				switch(selected) {
				case MAINHAND:
					return MAINHAND_LABEL;
				case OFFHAND:
					return OFFHAND_LABEL;
				case BOTH:
					return BOTH_HANDS_LABEL;
				default:
					return "";	
				}
			}
			
		};
		this.handInput.setSelections(EffectCauseItemInHand.HandType.values());
		this.handInput.setRequired();
		this.handInput.disallowDelete();
		if (causeHeld != null) {
			this.handInput.setDefaultSelected(causeHeld.handType);
		} else {
			this.handInput.setDefaultSelected(EffectCauseItemInHand.HandType.BOTH);
		}
		
		return Arrays.asList(this.description, this.stackInput, this.handInput);
	}

	@Override
	public EffectCause createEffectCause() {
		if (this.editScreen == null || this.stackInput == null || this.handInput == null) {
			return null;
		}
		
		if (this.stackInput.getItemStack().isEmpty()) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.held.problem.noItem.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		if (this.handInput.getSelected() == null) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.held.problem.noHand.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		EffectCauseItemInHand cause = new EffectCauseItemInHand();
		cause.itemStack = this.stackInput.getItemStack();
		cause.handType = this.handInput.getSelected();
		
		return cause;
	}

}
