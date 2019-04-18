package com.tmtravlr.additions.gui.type.card;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.advancements.AdvancementAdded;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonAdvancement;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.type.AdditionTypeAdvancement;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

/**
 * Displays info about an added advancement.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class GuiAdditionCardAdvancement extends GuiAdditionCardColored {

	private AdvancementAdded addition;
	
	private String textId;
	private String textName;
	private String textDescription;
	
	private String filterId;
	private String filterName;
	private String filterDescription;
	
	public GuiAdditionCardAdvancement(GuiView viewScreen, Addon addon, AdvancementAdded addition) {
		super(viewScreen, addon);
		
		this.addition = addition;
		
		this.textId = TextFormatting.GRAY + I18n.format("gui.view.additionType.id", TextFormatting.RESET + this.addition.id.toString() + TextFormatting.GRAY);
		this.filterId = this.textId.toLowerCase();
		
		this.textName = TextFormatting.GRAY + I18n.format("gui.view.additionType.name", TextFormatting.RESET + this.addition.display.getTitle().getFormattedText() + TextFormatting.GRAY);
		this.filterName = this.addition.display.getTitle().getUnformattedText().toLowerCase();
		
		this.textDescription = TextFormatting.GRAY + I18n.format("gui.view.additionType.description", TextFormatting.RESET + this.addition.display.getDescription().getFormattedText() + TextFormatting.GRAY);
		this.filterDescription = this.addition.display.getDescription().getUnformattedText().toLowerCase();
		
		this.allowEdit = false;
		
		this.setColors(GuiAdditionTypeButtonAdvancement.BUTTON_COLOR_DARK, GuiAdditionTypeButtonAdvancement.BUTTON_COLOR_HOVER);
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
		ItemStack itemStack = this.addition.display.getIcon();
		
		Gui.drawRect(this.x + 9, itemDisplayTop - 1, this.x + 31, itemDisplayTop + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + 10, itemDisplayTop, this.x + 30, itemDisplayTop + 20, 0xFF000000);

		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textName, columnWidth*2 - 20, this.x + 45, this.y + 8, 0xFFFFFF);
    	
    	if (this.needs3Lines()) {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth*2 - 5, this.x + 45, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textDescription, columnWidth*2 - 5, this.x + 45, this.y + 42, 0xFFFFFF);
    	} else {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth - 5, this.x + 45, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textDescription, columnWidth - 5, this.x + 45 + columnWidth, this.y + 25, 0xFFFFFF);
    	}

		this.viewScreen.renderItemStack(itemStack, this.x + 12, itemDisplayTop + 2, mouseX, mouseY, true);
	}

	@Override
	protected void deleteAddition() {
		AdditionTypeAdvancement.INSTANCE.deleteAddition(this.addon, this.addition);
	}
	
	@Override
	public boolean filterApplies(String filter) {
		return filter == null || filter.isEmpty() || this.filterId.contains(filter.toLowerCase()) || this.filterName.contains(filter.toLowerCase()) || this.filterDescription.contains(filter.toLowerCase());
	}
	
	private boolean needs3Lines() {
		return this.viewScreen.getFontRenderer().getStringWidth(this.textId) > this.getColumnWidth() || this.viewScreen.getFontRenderer().getStringWidth(this.textDescription) > this.getColumnWidth() || this.width < 240;
	}
	
	private int getColumnWidth() {
		return (this.width - 45) / 2; 
	}

}
