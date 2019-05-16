package com.tmtravlr.additions.gui.type.card;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.BlockAddedManager;
import com.tmtravlr.additions.addon.effects.EffectList;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseManager;
import com.tmtravlr.additions.api.gui.IGuiBlockAddedFactory;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseFactory;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonBlock;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonEffect;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEditEffectList;
import com.tmtravlr.additions.type.AdditionTypeEffect;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Displays info about an added effect.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019 
 */
public class GuiAdditionCardEffect extends GuiAdditionCardColored {

	private final EffectList addition;
	private ItemStack displayStack = ItemStack.EMPTY;
	private int xOffset = 10;
	
	private String textId;
	private String textCause;
	private String textEffectCount;
	private String filterId;
	private String filterCause;
	
	public GuiAdditionCardEffect(GuiView viewScreen, Addon addon, EffectList addition) {
		super(viewScreen, addon);
		
		this.addition = addition;
		
		ResourceLocation causeType = EffectCauseManager.getTypeFor(this.addition.cause);
		IGuiEffectCauseFactory factory = EffectCauseManager.getGuiFactoryFor(causeType);
		
		this.displayStack = factory != null ? factory.getDisplayStack(this.addition.cause) : ItemStack.EMPTY;
		
		if (!this.displayStack.isEmpty()) {
			this.xOffset = 45;
		}
		
		this.textId = TextFormatting.GRAY + I18n.format("gui.view.additionType.id", TextFormatting.RESET + this.addition.id.toString() + TextFormatting.GRAY);
		this.textCause = TextFormatting.GRAY + I18n.format("gui.view.additionType.cause", TextFormatting.RESET + (factory != null ? factory.getTitle() : causeType.toString()) + TextFormatting.GRAY);
		this.textEffectCount = TextFormatting.GRAY + I18n.format("gui.view.additionType.effects." + (this.addition.effects.size() == 1 ? "singular" : "plural"), TextFormatting.RESET + String.valueOf(this.addition.effects.size()) + TextFormatting.GRAY);
		
		this.filterId = this.addition.id.toString().toLowerCase();
		this.filterCause = (factory != null ? factory.getTitle() : causeType.toString()).toLowerCase();
		
		this.setColors(GuiAdditionTypeButtonEffect.BUTTON_COLOR_DARK, GuiAdditionTypeButtonEffect.BUTTON_COLOR_HOVER);
	}
	
	@Override
	public int getCardHeight() {
		if (this.needs3Lines()) {
			return 80;
		} else {
			return 60;
		}
	}

	@Override
	protected void drawCardInfo(int mouseX, int mouseY) {
		int itemDisplayTop = this.y + this.height/2 - 10;
		int columnWidth = this.getColumnWidth();
		
		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth*2 - 60, this.x + this.xOffset, this.y + 8, 0xFFFFFF);
    	
    	if (this.needs3Lines()) {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textCause, columnWidth*2 - 10, this.x + this.xOffset, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textEffectCount, columnWidth*2 - 10, this.x + this.xOffset, this.y + 42, 0xFFFFFF);
    	} else {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textCause, columnWidth - 10, this.x + this.xOffset, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textEffectCount, columnWidth - 10, this.x + this.xOffset + columnWidth, this.y + 25, 0xFFFFFF);
    	}
    	
    	if (!this.displayStack.isEmpty()) {
			Gui.drawRect(this.x + 9, itemDisplayTop - 1, this.x + 31, itemDisplayTop + 21, 0xFFA0A0A0);
			Gui.drawRect(this.x + 10, itemDisplayTop, this.x + 30, itemDisplayTop + 20, 0xFF000000);
			
    		this.viewScreen.renderItemStack(this.displayStack, this.x + 12, itemDisplayTop + 2, mouseX, mouseY, true);
    	}
	}

	@Override
	protected void editAddition() {
		this.viewScreen.mc.displayGuiScreen(new GuiEditEffectList(this.viewScreen, I18n.format("gui.edit.editing", this.addition.id), this.addon, this.addition));
	}

	@Override
	protected void duplicateAddition() {
		GuiEditEffectList editScreen = new GuiEditEffectList(this.viewScreen, I18n.format("gui.edit.effectList.title"), this.addon, null);
		editScreen.copyFrom(this.addition);
		this.viewScreen.mc.displayGuiScreen(editScreen);
	}

	@Override
	protected void deleteAddition() {
		AdditionTypeEffect.INSTANCE.deleteAddition(this.addon, this.addition);
	}
	
	@Override
	public boolean filterApplies(String filter) {
		return filter == null || filter.isEmpty() || this.filterId.contains(filter.toLowerCase()) || this.filterCause.contains(filter.toLowerCase());
	}
	
	private boolean needs3Lines() {
		return this.viewScreen.getFontRenderer().getStringWidth(this.textId) > this.getColumnWidth() || this.viewScreen.getFontRenderer().getStringWidth(this.textCause) > this.getColumnWidth() || this.width < 195 + this.xOffset;
	}
	
	private int getColumnWidth() {
		return (this.width - this.xOffset) / 2;
	}

}
