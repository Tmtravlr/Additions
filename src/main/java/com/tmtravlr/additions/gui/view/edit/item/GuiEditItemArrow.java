package com.tmtravlr.additions.gui.view.edit.item;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedArrow;

import net.minecraft.client.gui.GuiScreen;

/**
 * Page for adding a new arrow or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2018
 */
public class GuiEditItemArrow extends GuiEditItemBaseProjectile<ItemAddedArrow> {
    
	public GuiEditItemArrow(GuiScreen parentScreen, String title, Addon addon, ItemAddedArrow item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedArrow();
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
		this.components.add(this.itemBaseDamageInput);
		this.components.add(this.itemBasePunchInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemGravityInput);
		this.advancedComponents.add(this.itemWorksWithInfinityInput);
		this.advancedComponents.add(this.itemSticksInGroundInput);
		this.advancedComponents.add(this.itemPiercesEntitiesInput);
		this.advancedComponents.add(this.itemWorksInWaterInput);
		this.advancedComponents.add(this.itemHitEffectsInput);
		this.advancedComponents.add(this.itemHasPotionEffectsInput);
		this.advancedComponents.add(this.itemRenders3DInput);
		this.advancedComponents.add(this.itemDamageIgnoresSpeedInput);
		this.advancedComponents.add(this.itemHitSoundInput);
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemIsBeaconPaymentInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
}
