package com.tmtravlr.additions.gui.view.edit.item;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedClub;
import com.tmtravlr.additions.addon.items.ItemAddedSword;

import net.minecraft.client.gui.GuiScreen;

/**
 * Page for adding a new club or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2017
 */
public class GuiEditItemClub extends GuiEditItemTool<ItemAddedClub> {
    
	public GuiEditItemClub(GuiScreen parentScreen, String title, Addon addon, ItemAddedClub item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedClub();
		} else {
			this.item = item;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.itemIdInput);
		this.components.add(this.itemNameInput);
		this.components.add(this.itemShinesInput);
		this.components.add(this.itemMaterialInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemApplyVanillaAttributesInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
}
