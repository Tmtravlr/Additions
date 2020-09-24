package com.tmtravlr.additions.gui.view.components.input.dropdown;

import java.util.ArrayList;

import com.tmtravlr.additions.ConfigLoader;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

/**
 * Dropdown list specifically for blocks, which renders the item blocks in the list if they exist.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2017
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
	protected GuiScrollingDropdown createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown(this, this.selections, dropdownHeight, above) {

			@Override
			protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
				if (!this.selectionDisplay.isEmpty()) {
					Block toDisplay = this.selectionDisplay.get(slot);
					
					if (toDisplay != null) {
						int textOffset = 5;
						
						if (ConfigLoader.renderItemsInLists.getBoolean(true)) {
							ItemStack stackToDisplay = new ItemStack(toDisplay);
							int itemX = this.left + 5;
							int itemY = top - 1;
							textOffset = 25;
							
							this.parentInput.editScreen.renderItemStack(stackToDisplay, itemX, itemY, 0, 0, false, true);
						}
						
						this.parentInput.editScreen.getFontRenderer().drawString(this.parentInput.getSelectionName(toDisplay), this.left + textOffset, top + 2, 0xFFFFFF);
					}
				} else {
					this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.dropdown.nothingToShow"), this.left + 5, top + 2, 0x888888);
				}
			}
		};
	}

}
