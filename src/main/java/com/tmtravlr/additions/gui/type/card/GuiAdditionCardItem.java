package com.tmtravlr.additions.gui.type.card;

import java.util.Collections;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.gui.GuiMessagePopup;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.item.IGuiEditItemFactory;
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
public class GuiAdditionCardItem extends GuiAdditionCardColored {

	private IItemAdded addedItem;
	private String typeString;
	private String idString;
	private String nameString;
	private String filterType;
	private String filterId;
	private String filterName;
	
	public GuiAdditionCardItem(GuiView viewScreen, Addon addon, IItemAdded addedItem) {
		super(viewScreen, addon);
		
		this.addedItem = addedItem;
		
		this.typeString = I18n.format("gui.view.additionType.type", ItemAddedManager.getTypeFor(this.addedItem));
		this.idString = I18n.format("gui.view.additionType.id", this.addedItem.getId());
		this.nameString = I18n.format("gui.view.additionType.name", this.addedItem.getDisplayName());
		
		this.filterType = String.valueOf(ItemAddedManager.getTypeFor(this.addedItem)).toLowerCase();
		this.filterId = this.addedItem.getId().toLowerCase();
		this.filterName = this.addedItem.getDisplayName().toLowerCase();
		
		this.setColors(0xff08212b, 0xff14495c);
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
		ItemStack itemStack = new ItemStack(this.addedItem.getAsItem());
		
		Gui.drawRect(this.x + 9, itemDisplayTop - 1, this.x + 31, itemDisplayTop + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + 10, itemDisplayTop, this.x + 30, itemDisplayTop + 20, 0xFF000000);

    	this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.nameString, columnWidth*2 - 60), this.x + 45, this.y + 8, 0xFFFFFF);
    	
    	if (this.needs3Lines()) {
    		this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.idString, columnWidth*2 - 10), this.x + 45, this.y + 25, 0xFFFFFF);
    		this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.typeString, columnWidth*2 - 10), this.x + 45, this.y + 42, 0xFFFFFF);
    	} else {
    		this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.idString, columnWidth - 10), this.x + 45, this.y + 25, 0xFFFFFF);
    		this.viewScreen.drawString(this.viewScreen.getFontRenderer(), trimWithDots(this.typeString, columnWidth - 10), this.x + 45 + columnWidth, this.y + 25, 0xFFFFFF);
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
		IGuiEditItemFactory editFactory = ItemAddedManager.getGuiFactoryFor(ItemAddedManager.getTypeFor(this.addedItem));
		
		if (editFactory != null) {
			this.viewScreen.mc.displayGuiScreen(editFactory.getEditScreen(this.viewScreen, this.addon, this.addedItem));
		} else {
			this.viewScreen.mc.displayGuiScreen(new GuiMessagePopup(this.viewScreen, I18n.format("gui.warnDialogue.problem.title"), new TextComponentTranslation("gui.warnDialogue.noTypeGui.message", ItemAddedManager.getTypeFor(this.addedItem)), I18n.format("gui.buttons.close")));
		}
	}

	@Override
	protected void duplicateAddition() {
		IGuiEditItemFactory editFactory = ItemAddedManager.getGuiFactoryFor(ItemAddedManager.getTypeFor(this.addedItem));
		
		if (editFactory != null) {
			this.viewScreen.mc.displayGuiScreen(editFactory.getDuplicateScreen(this.viewScreen, this.addon, this.addedItem));
		} else {
			this.viewScreen.mc.displayGuiScreen(new GuiMessagePopup(this.viewScreen, I18n.format("gui.warnDialogue.problem.title"), new TextComponentTranslation("gui.warnDialogue.noTypeGui.message", ItemAddedManager.getTypeFor(this.addedItem)), I18n.format("gui.buttons.close")));
		}
	}

	@Override
	protected void deleteAddition() {
		AdditionTypeItem.INSTANCE.deleteAddition(this.addon, this.addedItem);
	}
	
	@Override
	public boolean filterApplies(String filter) {
		return filter == null || filter.isEmpty() || this.filterType.contains(filter.toLowerCase()) || this.filterId.contains(filter.toLowerCase()) || this.filterName.contains(filter.toLowerCase());
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
