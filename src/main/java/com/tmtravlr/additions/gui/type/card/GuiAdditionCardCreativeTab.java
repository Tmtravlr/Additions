package com.tmtravlr.additions.gui.type.card;

import java.util.Collections;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.gui.GuiMessagePopup;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEditCreativeTab;
import com.tmtravlr.additions.gui.view.edit.item.IGuiEditItemFactory;
import com.tmtravlr.additions.type.AdditionTypeCreativeTab;
import com.tmtravlr.additions.type.AdditionTypeItem;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * Displays info about an added item.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiAdditionCardCreativeTab extends GuiAdditionCardColored {

	private CreativeTabAdded addition;
	private String countString;
	private String idString;
	private String nameString;
	private String filterId;
	private String filterName;
	
	public GuiAdditionCardCreativeTab(GuiView viewScreen, Addon addon, CreativeTabAdded addition) {
		super(viewScreen, addon);
		
		this.addition = addition;
		
		this.countString = I18n.format("gui.view.additionType.items", this.addition.getItemCount());
		this.idString = I18n.format("gui.view.additionType.id", this.addition.id);
		this.nameString = I18n.format("gui.view.additionType.name", this.addition.getTabLabel());
		
		this.filterId = this.addition.id.toLowerCase();
		this.filterName = this.addition.getTabLabel().toLowerCase();
		
		this.setColors(0xff08082b, 0xff161d69);
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

    	this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.nameString, columnWidth*2 - 60), this.x + 45, this.y + 8, 0xFFFFFF);
    	
    	if (this.needs3Lines()) {
    		this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.idString, columnWidth*2 - 10), this.x + 45, this.y + 25, 0xFFFFFF);
    		this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.countString, columnWidth*2 - 10), this.x + 45, this.y + 42, 0xFFFFFF);
    	} else {
    		this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.idString, columnWidth - 10), this.x + 45, this.y + 25, 0xFFFFFF);
    		this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.countString, columnWidth - 10), this.x + 45 + columnWidth, this.y + 25, 0xFFFFFF);
    	}
		
		RenderHelper.enableStandardItemLighting();
    	this.viewScreen.mc.getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)null, itemStack, this.x + 12, itemDisplayTop + 2);
    	if (mouseX >= this.x + 12 && mouseX < this.x + 28 && mouseY >= itemDisplayTop + 2 && mouseY < itemDisplayTop + 18) {
    		this.viewScreen.renderItemStackTooltip(itemStack, mouseX, mouseY);
    	}
		RenderHelper.disableStandardItemLighting();
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
		return this.viewScreen.getFontRenderer().getStringWidth(this.idString) > this.getColumnWidth() || this.width < 240;
	}
	
	private int getColumnWidth() {
		return (this.width - 45) / 2; 
	}
	
	private String trimWithDots(String toTrim, int width) {
		if (this.viewScreen.getFontRenderer().getStringWidth(toTrim) < width) {
			return toTrim;
		}
		
		return this.viewScreen.getFontRenderer().trimStringToWidth(toTrim, width) + "...";
	}

}
