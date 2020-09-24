package com.tmtravlr.additions.gui.view.edit.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentButton;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentAttributeModifierInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntegerInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInputOreDict;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.texture.GuiEditItemTexture;
import com.tmtravlr.additions.type.AdditionTypeItem;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Page for adding a new simple item or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2017
 */
public abstract class GuiEditItem<T extends IItemAdded> extends GuiEdit {
	
	protected final int BUTTON_TEXTURE = this.buttonCount++;
	
	protected Addon addon;
	
    protected boolean isNew;
    protected T item;
    protected T copyFrom;

    protected GuiComponentStringInput itemIdInput;
    protected GuiComponentStringInput itemNameInput;
    protected GuiComponentIntegerInput itemStackSizeInput;
    protected GuiComponentBooleanInput itemShinesInput;
    protected GuiComponentIntegerInput itemBurnTimeInput;
    protected GuiComponentBooleanInput itemIsBeaconPaymentInput;
    protected GuiComponentListInput<GuiComponentStringInput> itemTooltipInput;
	protected GuiComponentButton itemTextureButton;
	protected GuiComponentListInput<GuiComponentSuggestionInputOreDict> itemOreDictInput;
	protected GuiComponentDropdownInputItem itemContainerInput;
	protected GuiComponentListInput<GuiComponentAttributeModifierInput> itemAttributesInput;
    
	public GuiEditItem(GuiScreen parentScreen, String title, Addon addon) {
		super(parentScreen, title);
		this.addon = addon;
	}

	@Override
	public void initComponents() {
		
		this.itemIdInput = new GuiComponentStringInput(I18n.format("gui.edit.item.itemId.label"), this);
		if (this.isNew) {
			this.itemIdInput.setRequired();
			this.itemIdInput.setMaxStringLength(32);
			this.itemIdInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
			this.itemIdInput.setValidator(input -> input.matches("[a-z0-9\\_]*"));
		} else {
			this.itemIdInput.setEnabled(false);
			this.itemIdInput.setMaxStringLength(1024);
			this.itemIdInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
			this.itemIdInput.setDefaultText(this.item.getId());
		}
		
		this.itemNameInput = new GuiComponentStringInput(I18n.format("gui.edit.item.itemName.label"), this);
		this.itemNameInput.setRequired();
		this.itemNameInput.setMaxStringLength(128);
		this.itemNameInput.setHasColorSelect();
		this.itemNameInput.setDefaultText(this.item.getDisplayName());
		
		this.itemStackSizeInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.maxStackSize.label"), this, false);
		this.itemStackSizeInput.setMinimum(1);
		this.itemStackSizeInput.setMaximum(64);
		this.itemStackSizeInput.setDefaultInteger(this.item.getAsItem().getItemStackLimit());
		
		this.itemShinesInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.itemShines.label"), this);
		this.itemShinesInput.setDefaultBoolean(this.item.getAlwaysShines());
		
		this.itemBurnTimeInput = new GuiComponentIntegerInput(I18n.format("gui.edit.item.burnTime.label"), this, false);
		this.itemBurnTimeInput.setMinimum(0);
		this.itemBurnTimeInput.setMaximum(Integer.MAX_VALUE);
		this.itemBurnTimeInput.setInfo(new TextComponentTranslation("gui.edit.item.burnTime.info"));
		this.itemBurnTimeInput.setDefaultInteger(Math.max(this.item.getBurnTime(), 0));
		
		this.itemIsBeaconPaymentInput = new GuiComponentBooleanInput(I18n.format("gui.edit.item.isBeaconPayment.label"), this);
		this.itemIsBeaconPaymentInput.setDefaultBoolean(this.item.getIsBeaconPayment());
		
		this.itemTooltipInput = new GuiComponentListInput<GuiComponentStringInput>(I18n.format("gui.edit.item.itemTooltip.label"), this) {

			@Override
			public GuiComponentStringInput createBlankComponent() {
				GuiComponentStringInput input = new GuiComponentStringInput("", this.editScreen);
				input.setMaxStringLength(256);
				input.setHasColorSelect();
				return input;
			}
			
		};
		this.item.getTooltip().forEach(toAdd->{
			GuiComponentStringInput input = this.itemTooltipInput.createBlankComponent();
			input.setDefaultText(toAdd);
			this.itemTooltipInput.addDefaultComponent(input);
		});
		
		if (!this.isNew) {
			this.itemTextureButton = new GuiComponentButton(this, BUTTON_TEXTURE, I18n.format("gui.edit.item.updateTexture.label"));
			this.itemTextureButton.visible = true;
		}
		
		this.itemOreDictInput = new GuiComponentListInput<GuiComponentSuggestionInputOreDict>(I18n.format("gui.edit.item.oreDictEntries.label"), this) {

			@Override
			public GuiComponentSuggestionInputOreDict createBlankComponent() {
				GuiComponentSuggestionInputOreDict input = new GuiComponentSuggestionInputOreDict("", this.editScreen);
				return input;
			}
			
		};
		this.item.getOreDict().forEach(toAdd->{
			GuiComponentSuggestionInputOreDict input = this.itemOreDictInput.createBlankComponent();
			input.setDefaultText(toAdd);
			this.itemOreDictInput.addDefaultComponent(input);
		});
		
		this.itemContainerInput = new GuiComponentDropdownInputItem(I18n.format("gui.edit.item.itemContainer.label"), this);
		this.itemContainerInput.setDefaultSelected(this.item.getAsItem().getContainerItem());
		
		this.itemAttributesInput = new GuiComponentListInput<GuiComponentAttributeModifierInput>(I18n.format("gui.edit.item.attributeModifiers.label"), this) {

			@Override
			public GuiComponentAttributeModifierInput createBlankComponent() {
				return new GuiComponentAttributeModifierInput("", this.editScreen);
			}
			
		};
		this.item.getAttributeModifiers().forEach((slot, modifier) -> {
			GuiComponentAttributeModifierInput modifierInput = this.itemAttributesInput.createBlankComponent();
			modifierInput.setDefaultAttributeModifier(modifier, slot);
			this.itemAttributesInput.addDefaultComponent(modifierInput);
		});
	}
	
	@Override
	public void saveObject() {
		String name = this.isNew ? this.addon.id + "-" + this.itemIdInput.getText() : this.item.getId();
		String displayName = this.itemNameInput.getText();
		
		if (this.itemIdInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.problem.noId", displayName), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (displayName.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.problem.noName", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.isNew && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(AdditionsMod.MOD_ID, name))) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.problem.duplicate", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.item.setDisplayName(displayName);
		this.item.getAsItem().setMaxStackSize(this.itemStackSizeInput.getInteger());
		this.item.setAlwaysShines(this.itemShinesInput.getBoolean());
		this.item.setBurnTime(this.itemBurnTimeInput.getInteger() == 0 ? -1 : this.itemBurnTimeInput.getInteger());
		this.item.setIsBeaconPayment(this.itemIsBeaconPaymentInput.getBoolean());
		
		List<String> tooltips = new ArrayList<>();
		this.itemTooltipInput.getComponents().forEach(stringInput -> tooltips.add(stringInput.getText()));
		this.item.setTooltip(tooltips);
		
		List<String> oreDicts = new ArrayList<>();
		this.itemOreDictInput.getComponents().forEach(stringInput -> oreDicts.add(stringInput.getText()));
		this.item.setOreDict(oreDicts);
		
		this.item.getAsItem().setContainerItem(this.itemContainerInput.getSelected());
		
		Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
		if (!this.itemAttributesInput.getComponents().isEmpty()) {
			for (GuiComponentAttributeModifierInput modifierInput : this.itemAttributesInput.getComponents()) {
				if (modifierInput.getAttributeModifier() != null) {
					attributeModifiers.put(modifierInput.getSlot(), modifierInput.getAttributeModifier());
				}
			}
		}
		this.item.setAttributeModifiers(attributeModifiers);
		
		if (this.isNew) {
			this.item.getAsItem().setUnlocalizedName(name);
			this.item.getAsItem().setRegistryName(AdditionsMod.MOD_ID, name);
		}
		
		AdditionTypeItem.INSTANCE.saveAddition(this.addon, this.item);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		if (this.isNew) {
    		this.openTextureDialogue(this.getItemCreatedPopup());
		} else {
			this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart.updated.title"), new TextComponentTranslation("gui.warnDialogue.restart.updated.message")));
		}
	}
	
	@Override
	public void refreshView() {
		
		this.itemOreDictInput.getComponents().forEach(input -> {
			input.refreshOreNames();
		});
	}
	
	public void copyFrom(T item) {
		this.copyFrom = item;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	protected void copyFromOther() {
		this.itemNameInput.setDefaultText(this.copyFrom.getDisplayName());
		this.itemStackSizeInput.setDefaultInteger(this.copyFrom.getAsItem().getItemStackLimit());
		this.itemShinesInput.setDefaultBoolean(this.copyFrom.getAlwaysShines());
		this.itemBurnTimeInput.setDefaultInteger(Math.max(this.copyFrom.getBurnTime(), 0));
		this.itemIsBeaconPaymentInput.setDefaultBoolean(this.copyFrom.getIsBeaconPayment());
		
		this.itemTooltipInput.removeAllComponents();
		this.copyFrom.getTooltip().forEach(toAdd -> {
			GuiComponentStringInput input = this.itemTooltipInput.createBlankComponent();
			input.setDefaultText(toAdd);
			this.itemTooltipInput.addDefaultComponent(input);
		});

		this.itemOreDictInput.removeAllComponents();
		this.copyFrom.getOreDict().forEach(toAdd -> {
			GuiComponentSuggestionInputOreDict input = this.itemOreDictInput.createBlankComponent();
			input.setDefaultText(toAdd);
			this.itemOreDictInput.addDefaultComponent(input);
		});
		
		this.itemContainerInput.setDefaultSelected(this.copyFrom.getAsItem().getContainerItem());
		this.itemAttributesInput.removeAllComponents();
		this.copyFrom.getAttributeModifiers().forEach((slot, modifier) -> {
			GuiComponentAttributeModifierInput modifierInput = this.itemAttributesInput.createBlankComponent();
			modifierInput.setDefaultAttributeModifier(modifier, slot);
			this.itemAttributesInput.addDefaultComponent(modifierInput);
		});
		
		this.copyFrom = null;
	}
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_TEXTURE) {
    		this.openTextureDialogue(this);
    	} else {
    		super.actionPerformed(button);
    	}
    }
    
    protected void openTextureDialogue(GuiScreen nextScreen) {
    	this.mc.displayGuiScreen(new GuiEditItemTexture(nextScreen, this.addon, this.item, this.isNew));
    }
    
    protected GuiScreen getItemCreatedPopup() {
    	return new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart.created.title"), new TextComponentTranslation("gui.warnDialogue.restart.created.message"));
    }

}
