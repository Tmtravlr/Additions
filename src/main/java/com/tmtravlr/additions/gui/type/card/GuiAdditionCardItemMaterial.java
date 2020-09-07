package com.tmtravlr.additions.gui.type.card;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.IItemAddedTool;
import com.tmtravlr.additions.addon.items.ItemAddedArmor;
import com.tmtravlr.additions.addon.items.materials.ItemMaterialAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonItemMaterial;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.item.material.GuiEditArmorMaterial;
import com.tmtravlr.additions.gui.view.edit.item.material.GuiEditItemMaterial;
import com.tmtravlr.additions.gui.view.edit.item.material.GuiEditToolMaterial;
import com.tmtravlr.additions.type.AdditionTypeItem;
import com.tmtravlr.additions.type.AdditionTypeItemMaterial;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * Displays info about an added item.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017
 */
public class GuiAdditionCardItemMaterial extends GuiAdditionCardColored {

	private ItemMaterialAdded addition;
	private String textId;
	private String textRepairMaterial;
	private String textType;
	private String filterId;
	private String filterRepairMaterial;
	private String filterType;
	
	public GuiAdditionCardItemMaterial(GuiView viewScreen, Addon addon, ItemMaterialAdded addition) {
		super(viewScreen, addon);
		
		this.addition = addition;
		
		this.textId = TextFormatting.GRAY + I18n.format("gui.view.additionType.id", TextFormatting.RESET + this.addition.getId() + TextFormatting.GRAY);
		this.textRepairMaterial = TextFormatting.GRAY + I18n.format("gui.view.additionType.material", TextFormatting.RESET + this.addition.getRepairStack().getDisplayName() + TextFormatting.GRAY);
		this.textType = TextFormatting.GRAY + I18n.format("gui.view.additionType.type", TextFormatting.RESET + (addition.isArmorMaterial() ? "armor" : "tool") + TextFormatting.GRAY);
		
		this.filterId = this.addition.getId().toLowerCase();
		this.filterRepairMaterial = this.addition.getRepairStack().getDisplayName().toLowerCase();
		this.filterType = addition.isArmorMaterial() ? "armor" : "tool";
		
		this.setColors(GuiAdditionTypeButtonItemMaterial.BUTTON_COLOR_DARK, GuiAdditionTypeButtonItemMaterial.BUTTON_COLOR_HOVER);
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
		ItemStack itemStack = this.addition.getRepairStack();
		
		Gui.drawRect(this.x + 9, itemDisplayTop - 1, this.x + 31, itemDisplayTop + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + 10, itemDisplayTop, this.x + 30, itemDisplayTop + 20, 0xFF000000);

		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textType, columnWidth*2 - 60, this.x + 45, this.y + 8, 0xFFFFFF);
    	
    	if (this.needs3Lines()) {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth*2 - 10, this.x + 45, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textRepairMaterial, columnWidth*2 - 10, this.x + 45, this.y + 42, 0xFFFFFF);
    	} else {
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth - 10, this.x + 45, this.y + 25, 0xFFFFFF);
    		CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textRepairMaterial, columnWidth - 10, this.x + 45 + columnWidth, this.y + 25, 0xFFFFFF);
    	}

		this.viewScreen.renderItemStack(itemStack, this.x + 12, itemDisplayTop + 2, mouseX, mouseY, true);
	}

	@Override
	protected void editAddition() {
		if (this.addition.isArmorMaterial()) {
			this.viewScreen.mc.displayGuiScreen(new GuiEditArmorMaterial(this.viewScreen, I18n.format("gui.edit.editing", this.addition.getId()), this.addon, this.addition.getArmorMaterialAdded()));
		} else if (this.addition.isToolMaterial()) {
			this.viewScreen.mc.displayGuiScreen(new GuiEditToolMaterial(this.viewScreen, I18n.format("gui.edit.editing", this.addition.getId()), this.addon, this.addition.getToolMaterialAdded()));
		} else {
			throw new IllegalArgumentException("Unknown item materal type");
		}
	}

	@Override
	protected void duplicateAddition() {
		GuiEditItemMaterial editScreen;
		if (this.addition.isArmorMaterial()) {
			editScreen = new GuiEditArmorMaterial(this.viewScreen, I18n.format("gui.edit.itemMaterial.armor.title"), this.addon, null);
		} else if (this.addition.isToolMaterial()) {
			editScreen = new GuiEditToolMaterial(this.viewScreen, I18n.format("gui.edit.itemMaterial.tool.title"), this.addon, null);
		} else {
			throw new IllegalArgumentException("Unknown item materal type");
		}
		editScreen.copyFrom(this.addition);
		this.viewScreen.mc.displayGuiScreen(editScreen);	
	}
	
	@Override
	protected void confirmDelete() {
		final GuiAdditionCardColored card = this;
		
		if (this.addition.isArmorMaterial()) {
			List<IItemAdded> armorWithMaterial = AdditionTypeItem.INSTANCE.getAllAdditions().stream().filter(item -> (item instanceof ItemAddedArmor) && ((ItemAddedArmor)item).getArmorMaterial() == this.addition.getArmorMaterial()).collect(Collectors.toList());
			if (!armorWithMaterial.isEmpty()) {
				//Armor warning
				this.viewScreen.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this.viewScreen, this.viewScreen, I18n.format("gui.warnDialogue.delete.title"), new TextComponentTranslation("gui.popup.itemMaterial.armor.delete.message"), I18n.format("gui.buttons.cancel"), I18n.format("gui.buttons.delete")) {

					protected void actionPerformed(GuiButton button) throws IOException {
				        if (button.id == SECOND_BUTTON) {
				        	for (IItemAdded itemAdded : armorWithMaterial) {
				        		if (itemAdded instanceof ItemAddedArmor) {
				        			((ItemAddedArmor)itemAdded).setArmorMaterial(ItemArmor.ArmorMaterial.LEATHER);
				        		}
				        	}
				        	card.deleteAddition();
				        	card.viewScreen.refreshView();
				        }
				        super.actionPerformed(button);
				    }
				});
			}
		} else {
			List<IItemAdded> toolsWithMaterial = AdditionTypeItem.INSTANCE.getAllAdditions().stream().filter(item -> (item instanceof IItemAddedTool) && ((IItemAddedTool)item).getToolMaterial() == this.addition.getToolMaterial()).collect(Collectors.toList());
			if (!toolsWithMaterial.isEmpty()) {
				//Tool warning
				this.viewScreen.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this.viewScreen, this.viewScreen, I18n.format("gui.warnDialogue.delete.title"), new TextComponentTranslation("gui.popup.itemMaterial.tool.delete.message"), I18n.format("gui.buttons.cancel"), I18n.format("gui.buttons.delete")) {

					protected void actionPerformed(GuiButton button) throws IOException {
				        if (button.id == SECOND_BUTTON) {
				        	for (IItemAdded itemAdded : toolsWithMaterial) {
				        		if (itemAdded instanceof IItemAddedTool) {
				        			((IItemAddedTool)itemAdded).setToolMaterial(Item.ToolMaterial.WOOD);
				        		}
				        	}
				        	card.deleteAddition();
				        	card.viewScreen.refreshView();
				        }
				        super.actionPerformed(button);
				    }
				});
			}
		}
		
	}

	@Override
	protected void deleteAddition() {
		AdditionTypeItemMaterial.INSTANCE.deleteAddition(this.addon, this.addition);
	}
	
	@Override
	public boolean filterApplies(String filter) {
		return filter == null || filter.isEmpty() || this.filterId.contains(filter.toLowerCase()) || this.filterRepairMaterial.contains(filter.toLowerCase()) || this.filterType.contains(filter.toLowerCase());
	}
	
	private boolean needs3Lines() {
		return this.viewScreen.getFontRenderer().getStringWidth(this.textId) > this.getColumnWidth() || this.viewScreen.getFontRenderer().getStringWidth(this.textRepairMaterial) > this.getColumnWidth() || this.width < 240;
	}
	
	private int getColumnWidth() {
		return (this.width - 45) / 2; 
	}

}
