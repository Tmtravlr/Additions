package com.tmtravlr.additions.gui.type.card;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.sounds.SoundEventAdded;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonSoundEvent;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEditSoundEvent;
import com.tmtravlr.additions.type.AdditionTypeSoundEvent;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

/**
 * Displays info about an added sound event.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date October 2018
 */
public class GuiAdditionCardSoundEvent extends GuiAdditionCardColored {

	private SoundEventAdded addition;
	private String textCount;
	private String textId;
	private String filterId;
	
	public GuiAdditionCardSoundEvent(GuiView viewScreen, Addon addon, SoundEventAdded addition) {
		super(viewScreen, addon);
		
		this.addition = addition;
		
		this.textCount = TextFormatting.GRAY + I18n.format("gui.view.additionType.sounds", TextFormatting.RESET + String.valueOf(this.addition.getSoundList().getSounds().size()) + TextFormatting.GRAY);
		this.textId = TextFormatting.GRAY + I18n.format("gui.view.additionType.id", TextFormatting.RESET + this.addition.getRegistryName().toString() + TextFormatting.GRAY);
		
		this.filterId = this.textId.toLowerCase();
		
		this.setColors(GuiAdditionTypeButtonSoundEvent.BUTTON_COLOR_DARK, GuiAdditionTypeButtonSoundEvent.BUTTON_COLOR_LIGHT);
	}
	
	@Override
	public int getCardHeight() {
		return 60;
	}

	@Override
	protected void drawCardInfo(int mouseX, int mouseY) {
		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, this.width - 60, this.x + 10, this.y + 8, 0xFFFFFF);
		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textCount, this.width - 10, this.x + 10, this.y + 25, 0xFFFFFF);
	}

	@Override
	protected void editAddition() {
		this.viewScreen.mc.displayGuiScreen(new GuiEditSoundEvent(this.viewScreen, I18n.format("gui.edit.editing", this.addition.getRegistryName()), this.addon, this.addition));
	}

	@Override
	protected void duplicateAddition() {
		GuiEditSoundEvent editScreen = new GuiEditSoundEvent(this.viewScreen, I18n.format("gui.edit.function.title"), this.addon, null);
		editScreen.copyFrom(this.addition);
		this.viewScreen.mc.displayGuiScreen(editScreen);		
	}

	@Override
	protected void deleteAddition() {
		AdditionTypeSoundEvent.INSTANCE.deleteAddition(this.addon, this.addition);
	}
	
	@Override
	public boolean filterApplies(String filter) {
		return filter == null || filter.isEmpty() || this.filterId.contains(filter.toLowerCase());
	}

}
