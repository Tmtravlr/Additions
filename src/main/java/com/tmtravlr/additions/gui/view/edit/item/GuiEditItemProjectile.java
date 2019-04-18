package com.tmtravlr.additions.gui.view.edit.item;

import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedArrow;
import com.tmtravlr.additions.addon.items.ItemAddedProjectile;
import com.tmtravlr.additions.addon.items.ItemAddedThrowable;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentFloatInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputSoundEvent;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInput;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;

/**
 * Page for adding a new arrow or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2018
 */
public class GuiEditItemProjectile extends GuiEditItemBaseProjectile<ItemAddedProjectile> {
    
	public GuiEditItemProjectile(GuiScreen parentScreen, String title, Addon addon, ItemAddedProjectile item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedProjectile();
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
		this.advancedComponents.add(this.itemHasPotionEffectsInput);
		this.advancedComponents.add(this.itemRenders3DInput);
		this.advancedComponents.add(this.itemDamageIgnoresSpeedInput);
		this.advancedComponents.add(this.itemHitSoundInput);
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
}
