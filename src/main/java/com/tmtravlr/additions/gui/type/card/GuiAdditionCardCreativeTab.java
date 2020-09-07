package com.tmtravlr.additions.gui.type.card;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonCreativeTab;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEditCreativeTab;
import com.tmtravlr.additions.type.AdditionTypeCreativeTab;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

/**
 * Displays info about an added creative tab.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public class GuiAdditionCardCreativeTab extends GuiAdditionCardColored {

	private CreativeTabAdded addition;
	private String textCount;
	private String textId;
	private String textName;
	private String filterId;
	private String filterName;
	
	public GuiAdditionCardCreativeTab(GuiView viewScreen, Addon addon, CreativeTabAdded addition) {
		super(viewScreen, addon);
		
		this.addition = addition;
		
		this.textCount = TextFormatting.GRAY + I18n.format("gui.view.additionType.items", TextFormatting.RESET + String.valueOf(this.addition.getItemCount()) + TextFormatting.GRAY);
		this.textId = TextFormatting.GRAY + I18n.format("gui.view.additionType.id", TextFormatting.RESET + this.addition.id + TextFormatting.GRAY);
		this.textName = TextFormatting.GRAY + I18n.format("gui.view.additionType.name", TextFormatting.RESET + this.addition.getTabLabel() + TextFormatting.GRAY);
		
		this.filterId = this.addition.id.toLowerCase();
		this.filterName = this.addition.getTabLabel().toLowerCase();
		
		this.setColors(GuiAdditionTypeButtonCreativeTab.BUTTON_COLOR_DARK, GuiAdditionTypeButtonCreativeTab.BUTTON_COLOR_HOVER);
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
		ItemStack itemStack = this.addition.getIconItemStack();
		
		Gui.drawRect(this.x + 9, itemDisplayTop - 1, this.x + 31, itemDisplayTop + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + 10, itemDisplayTop, this.x + 30, itemDisplayTop + 20, 0xFF000000);

		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textName, columnWidth*2 - 60, this.x + 45, this.y + 8, 0xFFFFFF);
    	
    	if (this.needs3Lines()) {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth*2 - 10, this.x + 45, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textCount, columnWidth*2 - 5, this.x + 45, this.y + 42, 0xFFFFFF);
    	} else {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth - 5, this.x + 45, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textCount, columnWidth - 5, this.x + 45 + columnWidth, this.y + 25, 0xFFFFFF);
    	}

		this.viewScreen.renderItemStack(itemStack, this.x + 12, itemDisplayTop + 2, mouseX, mouseY, true);
	}

	@Override
	protected void editAddition() {
		this.viewScreen.mc.displayGuiScreen(new GuiEditCreativeTab(this.viewScreen, I18n.format("gui.edit.editing", I18n.format(this.addition.getTabLabel())), this.addon, this.addition));
	}

	@Override
	protected void duplicateAddition() {
		GuiEditCreativeTab editScreen = new GuiEditCreativeTab(this.viewScreen, I18n.format("gui.edit.creativeTab.title"), this.addon, null);
		editScreen.copyFrom(this.addition);
		this.viewScreen.mc.displayGuiScreen(editScreen);		
	}

	@Override
	protected void deleteAddition() {
		AdditionTypeCreativeTab.INSTANCE.deleteAddition(this.addon, this.addition);
	}
	
	@Override
	public boolean filterApplies(String filter) {
		return filter == null || filter.isEmpty() || this.filterId.contains(filter.toLowerCase()) || this.filterName.contains(filter.toLowerCase());
	}
	
	private boolean needs3Lines() {
		return this.viewScreen.getFontRenderer().getStringWidth(this.textId) > this.getColumnWidth() || this.viewScreen.getFontRenderer().getStringWidth(this.textCount) > this.getColumnWidth() || this.width < 240;
	}
	
	private int getColumnWidth() {
		return (this.width - 45) / 2; 
	}

}
