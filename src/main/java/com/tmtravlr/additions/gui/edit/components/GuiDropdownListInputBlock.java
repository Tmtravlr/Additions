package com.tmtravlr.additions.gui.edit.components;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.tmtravlr.additions.gui.edit.GuiEdit;
import com.tmtravlr.additions.gui.edit.components.helpers.GuiScrollingDropdown;

/**
 * Dropdown list specifically for items, which renders the items in the list.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date August 2017
 */
public class GuiDropdownListInputBlock extends GuiDropdownListInput<Block> {

	public GuiDropdownListInputBlock(String label, GuiEdit parentScreen) {
		super(label, parentScreen);
	}
	
	@Override
	public String getSelectionName(Block selected) {
		return "" + Block.REGISTRY.getNameForObject(selected);
	}
	
	@Override
	protected GuiScrollingDropdown<Block> createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown<Block>(this, this.selections, dropdownHeight, above) {

			@Override
			protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
				Block toDisplay = this.selectionDisplay.get(slot);
				if (toDisplay != null) {
					ItemStack stackToDisplay = new ItemStack(toDisplay);
					
					RenderHelper.enableGUIStandardItemLighting();
					this.parentInput.parent.mc.getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)null, stackToDisplay, this.left + 5, top - 1);
		            RenderHelper.disableStandardItemLighting();
		            
					this.parentInput.parent.getFontRenderer().drawString(this.parentInput.getSelectionName(toDisplay), this.left + 25, top + 2, 0xFFFFFF);
				}
			}
		};
	}

}
