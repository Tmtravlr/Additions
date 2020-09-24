package com.tmtravlr.additions.gui.view.components.input.dropdown;

import java.util.ArrayList;

import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;

/**
 * Dropdown list specifically for items, which renders the items in the list.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date May 2019
 */
public class GuiComponentDropdownInputPotion extends GuiComponentDropdownInput<Potion> {

	public GuiComponentDropdownInputPotion(String label, GuiEdit editScreen) {
		super(label, editScreen);

		ArrayList<Potion> registeredPotions = new ArrayList<>();
		Potion.REGISTRY.forEach(potion -> registeredPotions.add(potion));
		this.setSelections(registeredPotions);
	}
	
	@Override
	public String getSelectionName(Potion selected) {
		return "" + Potion.REGISTRY.getNameForObject(selected);
	}
	
	@Override
	protected GuiScrollingDropdown createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown(this, this.selections, dropdownHeight, above) {

			@Override
			protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
				if (!this.selectionDisplay.isEmpty()) {
					Potion toDisplay = this.selectionDisplay.get(slot);
					if (toDisplay != null) {
						this.parentInput.editScreen.getFontRenderer().drawString(this.parentInput.getSelectionName(toDisplay), this.left + 5, top + 2, 0xFFFFFF);
					}
				} else {
					this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.dropdown.nothingToShow"), this.left + 5, top + 2, 0x888888);
				}
			}
		};
	}

}
