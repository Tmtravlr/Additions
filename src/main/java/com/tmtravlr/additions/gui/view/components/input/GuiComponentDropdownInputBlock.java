package com.tmtravlr.additions.gui.view.components.input;

import java.util.ArrayList;

import com.tmtravlr.additions.gui.view.components.helpers.GuiScrollingDropdown;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Dropdown list specifically for blocks, which renders the item blocks in the list if they exist.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017
 */
public class GuiComponentDropdownInputBlock extends GuiComponentDropdownInput<Block> {

	public GuiComponentDropdownInputBlock(String label, GuiEdit editScreen) {
		super(label, editScreen);

		ArrayList<Block> registeredBlocks = new ArrayList<>();
		Block.REGISTRY.forEach(block->registeredBlocks.add(block));
		this.setSelections(registeredBlocks);
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
				if (!this.selectionDisplay.isEmpty()) {
					Block toDisplay = this.selectionDisplay.get(slot);
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
