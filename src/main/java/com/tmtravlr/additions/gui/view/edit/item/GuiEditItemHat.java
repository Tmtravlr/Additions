package com.tmtravlr.additions.gui.view.edit.item;

import java.util.Arrays;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.ItemAddedHat;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIngredientOreNBTInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemHatTexture;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemTexture;
import com.tmtravlr.additions.util.models.ItemModelManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Page for adding a new hat or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date December 2017
 */
public class GuiEditItemHat extends GuiEditItem<ItemAddedHat> {
	
	private static final List<String> TOOL_TYPE_SUGGESTIONS = Arrays.asList(new String[]{"sword", "club", "pickaxe", "axe", "shovel", "hoe", "shears", "flint_and_steel"});
	
	private GuiComponentIntegerInput itemDurabilityInput;
	private GuiComponentIntegerInput itemEnchantabilityInput;
	private GuiComponentBooleanInput itemHasScreenOverlayInput;
	private GuiComponentIngredientOreNBTInput itemRepairStacksInput;
    
	public GuiEditItemHat(GuiScreen parentScreen, String title, Addon addon, ItemAddedHat item) {
		super(parentScreen, title, addon);
		
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedHat();
		} else {
			this.item = item;
		}
	}

	@Override
	public void initComponents() {
		super.initComponents();
		
		this.itemDurabilityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.durability.label"), this, false);
		this.itemDurabilityInput.setMinimum(0);
		this.itemDurabilityInput.setRequired();
		if (!this.isNew) {
			this.itemDurabilityInput.setDefaultInteger(this.item.getMaxDamage());
		}
		
		this.itemEnchantabilityInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.enchantability.label"), this, false);
		this.itemEnchantabilityInput.setMinimum(0);
		this.itemEnchantabilityInput.setInfo(new TextComponentTranslation("gui.edit.item.enchantability.info"));
		if (!this.isNew) {
			this.itemEnchantabilityInput.setDefaultInteger(this.item.enchantability);
		}
		
		this.itemHasScreenOverlayInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.hat.hasScreenOverlay.label"), this);
		if (!this.isNew) {
			this.itemHasScreenOverlayInput.setDefaultBoolean(this.item.hasScreenOverlay);
		}
		
		this.itemRepairStacksInput = new GuiComponentIngredientOreNBTInput(I18n.format("gui.edit.item.repairStacks.label"), this);
		if (!this.isNew) {
			this.itemRepairStacksInput.setDefaultIngredient(this.item.repairStacks);
		}
		
		this.components.add(this.itemIdInput);
		this.components.add(this.itemNameInput);
		this.components.add(this.itemShinesInput);
		this.components.add(this.itemDurabilityInput);
		this.components.add(this.itemEnchantabilityInput);
		this.components.add(this.itemHasScreenOverlayInput);
		this.components.add(this.itemRepairStacksInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemTooltipInput);
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemIsBeaconPaymentInput);
		this.advancedComponents.add(this.itemBurnTimeInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
	
	@Override
	public void saveObject() {
		
		this.item.setMaxDamage(this.itemDurabilityInput.getInteger());
		this.item.enchantability = this.itemEnchantabilityInput.getInteger();
		this.item.hasScreenOverlay = this.itemHasScreenOverlayInput.getBoolean();
		this.item.repairStacks = this.itemRepairStacksInput.getIngredient();
		
		super.saveObject();
		
		if (!this.isNew && this.item.hasScreenOverlay && !ItemModelManager.doesHatOverlayTextureExist(addon, this.item)) {
			this.mc.displayGuiScreen(new GuiMessageBoxTwoButton(getItemCreatedPopup(), this.getHatOverlayTextureScreen(this), I18n.format("gui.edit.item.hat.problem.noOverlay.title"), new TextComponentTranslation("gui.edit.item.hat.problem.noOverlay.message"), I18n.format("gui.popup.texture.addTexturesLater"), I18n.format("gui.popup.texture.addTexturesNow")));
		}
	}
	
	@Override
	protected void copyFromOther() {
		this.itemDurabilityInput.setDefaultInteger(this.copyFrom.getMaxDamage());
		this.itemEnchantabilityInput.setDefaultInteger(this.copyFrom.enchantability);
		this.itemHasScreenOverlayInput.setDefaultBoolean(this.copyFrom.hasScreenOverlay);
		this.itemRepairStacksInput.setDefaultIngredient(this.copyFrom.repairStacks);
		
		super.copyFromOther();
	}
    
	@Override
	protected void openTextureDialogue(GuiScreen nextScreen) {
    	if (this.item.hasScreenOverlay) {
	    	this.mc.displayGuiScreen(this.getHatOverlayTextureScreen(nextScreen));
    	} else {
    		this.mc.displayGuiScreen(new GuiEditItemTexture(nextScreen, this.addon, this.item, this.isNew, ItemModelManager.ItemModelType.HAT));
    	}
    }
	
	private GuiScreen getHatOverlayTextureScreen(GuiScreen nextScreen) {
		return new GuiEditItemHatTexture(nextScreen, this.addon, this.item, this.isNew);
	}
}
