package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemEquipped;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentEquipmentItemInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates components and handles saving the equipped item effect cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class GuiEditHandlerEffectCauseItemEquipped implements IGuiEffectCauseEditHandler {
	private static final String HELMET_LABEL = I18n.format("gui.edit.effectCause.equipped.slot.helmet.label");
	private static final String CHESTPLATE_LABEL = I18n.format("gui.edit.effectCause.equipped.slot.chestplate.label");
	private static final String LEGGINGS_LABEL = I18n.format("gui.edit.effectCause.equipped.slot.leggings.label");
	private static final String BOOTS_LABEL = I18n.format("gui.edit.effectCause.equipped.slot.boots.label");
	private static final String MAINHAND_LABEL = I18n.format("gui.edit.effectCause.equipped.slot.mainhand.label");
	private static final String OFFHAND_LABEL = I18n.format("gui.edit.effectCause.equipped.slot.offhand.label");
	
	private GuiEdit editScreen;
	private GuiComponentDisplayText description;
	private GuiComponentBooleanInput allRequiredInput;
	private GuiComponentIntegerInput requiredInput;
	private GuiComponentListInput<GuiComponentEquipmentItemInput> equipmentInput;

	@Override
	public List<IGuiViewComponent> createViewComponents(GuiEdit editScreen, EffectCause cause) {
		this.editScreen = editScreen;
		EffectCauseItemEquipped specificCause = (cause instanceof EffectCauseItemEquipped) ? (EffectCauseItemEquipped) cause : null;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effectCause.equipped.description"));
		
		this.requiredInput = new GuiComponentIntegerInput(I18n.format("gui.edit.effectCause.equipped.required.label"), editScreen, false);
		this.requiredInput.setInfo(new TextComponentTranslation("gui.edit.effectCause.equipped.required.info"));
		this.requiredInput.setMinimum(1);
		this.requiredInput.setHidden(true);
		if (specificCause != null) {
			this.requiredInput.setDefaultInteger(specificCause.piecesRequired);
		}
		
		this.allRequiredInput = new GuiComponentBooleanInput(I18n.format("gui.edit.effectCause.equipped.allRequired.label"), editScreen) {
			
			@Override
			public void setDefaultBoolean(boolean input) {
				GuiEditHandlerEffectCauseItemEquipped.this.requiredInput.setHidden(input);
				super.setDefaultBoolean(input);
			}
			
		};
		this.allRequiredInput.setDefaultBoolean(specificCause != null ? specificCause.equipment.size() == specificCause.piecesRequired : true);
		
		this.equipmentInput = new GuiComponentListInput<GuiComponentEquipmentItemInput>(I18n.format("gui.edit.effectCause.equipped.items.label"), editScreen) {
			
			@Override
			public GuiComponentEquipmentItemInput createBlankComponent() {
				return new GuiComponentEquipmentItemInput("", this.editScreen);
			}
			
		};
		this.equipmentInput.setRequired();
		if (specificCause != null) {
			specificCause.equipment.forEach((slot, stack) -> {
				GuiComponentEquipmentItemInput input = this.equipmentInput.createBlankComponent();
				input.setDefaultEquipmentStack(stack, slot);
				this.equipmentInput.addDefaultComponent(input);
			});
		}
		
		return Arrays.asList(this.description, this.equipmentInput, this.allRequiredInput, this.requiredInput);
	}

	@Override
	public EffectCause createEffectCause() {
		if (this.editScreen == null || this.requiredInput == null || this.equipmentInput == null) {
			return null;
		}
		
		if (this.equipmentInput.getComponents().isEmpty()) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBox(this.editScreen, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.equipped.problem.noItems.message"), I18n.format("gui.buttons.back")));
			return null;
		}
		
		EffectCauseItemEquipped cause = new EffectCauseItemEquipped();
		this.equipmentInput.getComponents().forEach(input -> {
			if (input.getSlot() != null && !input.getEquipmentStack().isEmpty()) {
				cause.equipment.put(input.getSlot(), input.getEquipmentStack());
			}
		});
		cause.piecesRequired = this.allRequiredInput.getBoolean() ? cause.equipment.size() : this.requiredInput.getInteger();
		
		return cause;
	}

}
