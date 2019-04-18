package com.tmtravlr.additions.gui.view.components.input.dropdown;

import java.util.ArrayList;

import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Dropdown list specifically for items, which renders the items in the list.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017
 */
public class GuiComponentDropdownInputItem extends GuiComponentDropdownInput<Item> {

	public GuiComponentDropdownInputItem(String label, GuiEdit editScreen) {
		super(label, editScreen);

		ArrayList<Item> registeredItems = new ArrayList<>();
		Item.REGISTRY.forEach(item->registeredItems.add(item));
		this.setSelections(registeredItems);
	}
	
	@Override
	public String getSelectionName(Item selected) {
		return "" + Item.REGISTRY.getNameForObject(selected);
	}
	
	@Override
	protected GuiScrollingDropdown createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown(this, this.selections, dropdownHeight, above) {

			@Override
			protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
				if (!this.selectionDisplay.isEmpty()) {
					Item toDisplay = this.selectionDisplay.get(slot);
					if (toDisplay != null) {
						ItemStack stackToDisplay = new ItemStack(toDisplay);
						int itemX = this.left + 5;
						int itemY = top - 1;

						this.parentInput.editScreen.renderItemStack(stackToDisplay, itemX, itemY, 0, 0, false, true);
						this.parentInput.editScreen.getFontRenderer().drawString(this.parentInput.getSelectionName(toDisplay), this.left + 25, top + 2, 0xFFFFFF);
					}
				} else {
					this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.dropdown.nothingToShow"), this.left + 5, top + 2, 0x888888);
				}
			}
		};
	}

}
