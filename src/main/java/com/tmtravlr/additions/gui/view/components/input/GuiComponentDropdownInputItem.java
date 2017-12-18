package com.tmtravlr.additions.gui.view.components.input;

import java.util.ArrayList;

import com.tmtravlr.additions.gui.view.components.helpers.GuiScrollingDropdown;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

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
	protected GuiScrollingDropdown<Item> createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown<Item>(this, this.selections, dropdownHeight, above) {

			@Override
			protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
				if (!this.selectionDisplay.isEmpty()) {
					Item toDisplay = this.selectionDisplay.get(slot);
					if (toDisplay != null) {
						ItemStack stackToDisplay = new ItemStack(toDisplay);
						
						RenderHelper.enableGUIStandardItemLighting();
						this.parentInput.editScreen.mc.getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)null, stackToDisplay, this.left + 5, top - 1);
			            RenderHelper.disableStandardItemLighting();
			            
						this.parentInput.editScreen.getFontRenderer().drawString(this.parentInput.getSelectionName(toDisplay), this.left + 25, top + 2, 0xFFFFFF);
					}
				} else {
					this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.dropdown.nothingToShow"), this.left + 5, top + 2, 0x888888);
				}
			}
		};
	}

}
