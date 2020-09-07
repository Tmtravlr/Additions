package com.tmtravlr.additions.gui.view.components.input.dropdown;

import java.util.ArrayList;

import com.tmtravlr.additions.ConfigLoader;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

/**
 * Dropdown list specifically for potion types, which renders the items in the list.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date May 2019
 */
public class GuiComponentDropdownInputPotionType extends GuiComponentDropdownInput<PotionType> {
	
	private ItemStack displayStack = ItemStack.EMPTY;

	public GuiComponentDropdownInputPotionType(String label, GuiEdit editScreen) {
		super(label, editScreen);

		ArrayList<PotionType> registeredTypes = new ArrayList<>();
		PotionType.REGISTRY.forEach(type->registeredTypes.add(type));
		this.setSelections(registeredTypes);
	}
	
	@Override
	public String getSelectionName(PotionType selected) {
		return "" + PotionType.REGISTRY.getNameForObject(selected);
	}
	
	@Override
	public void setDefaultSelected(PotionType selected) {
		super.setDefaultSelected(selected);
		
		if (selected != null) {
			this.displayStack = new ItemStack(Items.POTIONITEM);
			PotionUtils.addPotionToItemStack(this.displayStack, selected);
		}
	}
	
	@Override
	protected GuiScrollingDropdown createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown(this, this.selections, dropdownHeight, above) {

			@Override
			protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
				if (!this.selectionDisplay.isEmpty()) {
					PotionType toDisplay = this.selectionDisplay.get(slot);
					
					if (toDisplay != null) {
						int textOffset = 5;
						
						if (ConfigLoader.renderItemsInLists.getBoolean(true)) {
							ItemStack stackToDisplay = new ItemStack(Items.POTIONITEM);
							PotionUtils.addPotionToItemStack(stackToDisplay, toDisplay);
							
							int itemX = this.left + 5;
							int itemY = top - 1;
							textOffset = 25;
	
							this.parentInput.editScreen.renderItemStack(stackToDisplay, itemX, itemY, 0, 0, false);
						}
						
						this.parentInput.editScreen.getFontRenderer().drawString(this.parentInput.getSelectionName(toDisplay), this.left + textOffset, top + 2, 0xFFFFFF);
					}
				} else {
					this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.dropdown.nothingToShow"), this.left + 5, top + 2, 0x888888);
				}
			}
		};
	}
	
	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		int potionDisplayTop = y + 10;

		Gui.drawRect(x, potionDisplayTop - 1, x + 22, potionDisplayTop + 21, 0xFFA0A0A0);
		Gui.drawRect(x + 1, potionDisplayTop, x + 21, potionDisplayTop + 20, 0xFF000000);
		
		if (!this.displayStack.isEmpty()) {
			this.editScreen.renderItemStack(this.displayStack, x + 3, potionDisplayTop + 2, mouseX, mouseY, true);
		}
		
		this.selectedText.x = x + 30;
		this.selectedText.y = potionDisplayTop;
		this.selectedText.width = right - 90 - x;
		
		this.selectedText.drawTextBox();

		if (!this.selectedText.getText().isEmpty()) {
			this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}
	}

}
