package com.tmtravlr.additions.gui.view.edit.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.items.ItemAddedSimple;
import com.tmtravlr.additions.gui.GuiMessagePopup;
import com.tmtravlr.additions.gui.GuiMessagePopupTwoButton;
import com.tmtravlr.additions.gui.texture.GuiSelectItemTexturePopup;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.GuiComponentButton;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentAttributeModifierInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentBooleanInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentIntInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentListInput;
import com.tmtravlr.additions.gui.view.components.input.GuiComponentStringInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.AdditionTypeItem;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Page for adding a new simple item or editing an existing one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017
 */
public class GuiEditItemSimple extends GuiEdit {
	
	private final int BUTTON_TEXTURE = this.buttonCount++;
	
	private Addon addon;
	
    private boolean isNew;
    private ItemAddedSimple item;
    private ItemAddedSimple copyFrom;

    private GuiComponentStringInput itemIdInput;
	private GuiComponentStringInput itemNameInput;
	private GuiComponentIntInput itemStackSizeInput;
	private GuiComponentBooleanInput itemShinesInput;
	private GuiComponentListInput<GuiComponentStringInput> itemTooltipInput;
	private GuiComponentButton itemTextureButton;
	private GuiComponentListInput<GuiComponentStringInput> itemOreDictInput;
	private GuiComponentDropdownInputItem itemContainerInput;
	private GuiComponentListInput<GuiComponentAttributeModifierInput> itemAttributesInput;
	//private GuiComponentEventHandler eventHandler; TODO make event handler
    
	public GuiEditItemSimple(GuiScreen parentScreen, String title, Addon addon, ItemAddedSimple item) {
		super(parentScreen, title);
		this.addon = addon;
		this.isNew = item == null;
		
		if (this.isNew) {
			this.item = new ItemAddedSimple();
		} else {
			this.item = item;
		}
	}

	@Override
	public void initComponents() {
		
		this.itemIdInput = new GuiComponentStringInput("gui.edit.item.itemId.label", this);
		if (this.isNew) {
			this.itemIdInput.setRequired();
			this.itemIdInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
			this.itemIdInput.setValidator(new Predicate<String>() {
	
				@Override
				public boolean apply(String input) {
					return input.matches("[a-z0-9\\_]*");
				}
				
			});
		} else {
			this.itemIdInput.setEnabled(false);
			this.itemIdInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
			this.itemIdInput.setDefaultText(this.item.getId());
		}
		this.itemIdInput.setMaxStringLength(32);
		
		this.itemNameInput = new GuiComponentStringInput("gui.edit.item.itemName.label", this);
		this.itemNameInput.setRequired();
		this.itemNameInput.setMaxStringLength(128);
		this.itemNameInput.setHasColorSelect();
		this.itemNameInput.setDefaultText(this.item.getDisplayName());
		
		this.itemStackSizeInput = new GuiComponentIntInput("gui.edit.item.maxStackSize.label", this, false);
		this.itemStackSizeInput.setMinimum(1);
		this.itemStackSizeInput.setMaximum(64);
		this.itemStackSizeInput.setInteger(this.item.getItemStackLimit());
		
		this.itemShinesInput = new GuiComponentBooleanInput("gui.edit.item.itemShines.label", this);
		this.itemShinesInput.setDefaultBoolean(this.item.getAlwaysShines());
		
		this.itemTooltipInput = new GuiComponentListInput<GuiComponentStringInput>("gui.edit.item.itemTooltip.label", this) {

			@Override
			public GuiComponentStringInput createBlankComponent() {
				GuiComponentStringInput input = new GuiComponentStringInput("", this.editScreen);
				input.setMaxStringLength(256);
				input.setHasColorSelect();
				return input;
			}
			
		};
		this.item.getTooltip().forEach(toAdd->{
			GuiComponentStringInput input = new GuiComponentStringInput("", this);
			input.setMaxStringLength(256);
			input.setHasColorSelect();
			input.setDefaultText(toAdd);
			this.itemTooltipInput.addComponent(input);
		});
		
		if (!this.isNew) {
			this.itemTextureButton = new GuiComponentButton(this, BUTTON_TEXTURE, I18n.format("gui.edit.item.updateTexture.label"));
			this.itemTextureButton.visible = true;
		}
		
		this.itemOreDictInput = new GuiComponentListInput<GuiComponentStringInput>("gui.edit.item.oreDictEntries.label", this) {

			@Override
			public GuiComponentStringInput createBlankComponent() {
				return new GuiComponentStringInput("", this.editScreen);
			}
			
		};
		this.item.getOreDict().forEach(toAdd->{
			GuiComponentStringInput input = new GuiComponentStringInput("", this);
			input.setDefaultText(toAdd);
			this.itemOreDictInput.addComponent(input);
		});
		
		this.itemContainerInput = new GuiComponentDropdownInputItem("gui.edit.item.itemContainer.label", this);
		ArrayList<Item> registeredItems = new ArrayList<>();
		Item.REGISTRY.forEach(item->registeredItems.add(item));
		this.itemContainerInput.setSelections(registeredItems);
		this.itemContainerInput.setDefaultSelected(this.item.getContainerItem());
		
		this.itemAttributesInput = new GuiComponentListInput<GuiComponentAttributeModifierInput>(I18n.format("gui.edit.item.attributeModifiers.label"), this) {

			@Override
			public GuiComponentAttributeModifierInput createBlankComponent() {
				return new GuiComponentAttributeModifierInput("", this.editScreen);
			}
			
		};
		this.item.getAttributeModifiers().forEach((slot, modifier) -> {
			GuiComponentAttributeModifierInput modifierInput = new GuiComponentAttributeModifierInput("", this);
			modifierInput.setDefaultAttributeModifier(modifier, slot);
			this.itemAttributesInput.addComponent(modifierInput);
		});
		
		if (this.copyFrom != null) {
			this.copyFromOther();
		}
		
		this.components.add(this.itemIdInput);
		this.components.add(this.itemNameInput);
		this.components.add(this.itemStackSizeInput);
		this.components.add(this.itemShinesInput);
		this.components.add(this.itemTooltipInput);
		if (!this.isNew) {
			this.components.add(this.itemTextureButton);
		}
		
		this.advancedComponents.add(this.itemOreDictInput);
		this.advancedComponents.add(this.itemContainerInput);
		this.advancedComponents.add(this.itemAttributesInput);
	}
	
	@Override
	public void saveObject() {
		String name = this.isNew ? this.addon.id + "." + this.itemIdInput.getText() : this.item.getId();
		String displayName = this.itemNameInput.getText();
		
		if (this.itemIdInput.getText().isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessagePopup(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.problem.noId", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (displayName.isEmpty()) {
			this.mc.displayGuiScreen(new GuiMessagePopup(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.problem.noName", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		if (this.isNew && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(AdditionsMod.MOD_ID, name))) {
			this.mc.displayGuiScreen(new GuiMessagePopup(this, I18n.format("gui.edit.item.problem.title"), new TextComponentTranslation("gui.edit.item.problem.duplicate", name), I18n.format("gui.buttons.back")));
			return;
		}
		
		this.item.setDisplayName(displayName);
		this.item.setMaxStackSize(this.itemStackSizeInput.getInteger());
		this.item.setAlwaysShines(this.itemShinesInput.getBoolean());
		
		List<String> tooltips = new ArrayList<>();
		this.itemTooltipInput.getComponents().forEach(stringInput -> tooltips.add(stringInput.getText()));
		this.item.setTooltip(tooltips);
		
		List<String> oreDicts = new ArrayList<>();
		this.itemOreDictInput.getComponents().forEach(stringInput -> oreDicts.add(stringInput.getText()));
		this.item.setOreDict(oreDicts);
		
		this.item.setContainerItem(this.itemContainerInput.getSelected());
		
		if (!this.itemAttributesInput.getComponents().isEmpty()) {
			for (GuiComponentAttributeModifierInput modifierInput : this.itemAttributesInput.getComponents()) {
				this.item.attributeModifiers.clear();
				this.item.attributeModifiers.put(modifierInput.getSlot(), modifierInput.getAttributeModifier());
			}
		}
		
		if (this.isNew) {
			this.item.setUnlocalizedName(name);
			this.item.setRegistryName(AdditionsMod.MOD_ID, name);
		}
		
		AdditionTypeItem.INSTANCE.saveAddition(this.addon, this.item);
		
		if (this.parentScreen instanceof GuiView) {
			((GuiView) this.parentScreen).refreshView();
		}
		
		if (this.isNew) {
    		this.openTextureDialogue();
		} else {
			this.mc.displayGuiScreen(new GuiMessagePopupTwoButton(this.parentScreen, this.parentScreen, I18n.format("gui.warnDialogue.restart.updated.title"), new TextComponentTranslation("gui.warnDialogue.restart.updated.message"), I18n.format("gui.warnDialogue.restart.close"), I18n.format("gui.warnDialogue.restart.continue")) {
				@Override
				protected void actionPerformed(GuiButton button) throws IOException {
			        super.actionPerformed(button);
			        if (button.id == BUTTON_CONTINUE) {
			            this.mc.shutdown();
			        }
			    }
			});
		}
	}
	
	@Override
	public void refreshView() {
		
	}
	
	public void copyFrom(ItemAddedSimple item) {
		this.copyFrom = item;
		
		if (this.initializedComponents) {
			this.copyFromOther();
		}
	}
	
	private void copyFromOther() {
		this.itemNameInput.setDefaultText(this.copyFrom.getDisplayName());
		this.itemStackSizeInput.setInteger(this.copyFrom.getItemStackLimit());
		this.itemShinesInput.setDefaultBoolean(this.copyFrom.getAlwaysShines());
		
		this.itemTooltipInput.removeAllComponents();
		this.copyFrom.getTooltip().forEach(toAdd->{
			GuiComponentStringInput input = new GuiComponentStringInput("", this);
			input.setMaxStringLength(256);
			input.setHasColorSelect();
			input.setDefaultText(toAdd);
			this.itemTooltipInput.addComponent(input);
		});

		this.itemOreDictInput.removeAllComponents();
		this.copyFrom.getOreDict().forEach(toAdd->{
			GuiComponentStringInput input = new GuiComponentStringInput("", this);
			input.setDefaultText(toAdd);
			this.itemOreDictInput.addComponent(input);
		});
		
		this.itemContainerInput.setDefaultSelected(this.copyFrom.getContainerItem());
		this.itemAttributesInput.removeAllComponents();
		this.copyFrom.attributeModifiers.forEach((slot, modifier) -> {
			GuiComponentAttributeModifierInput modifierInput = new GuiComponentAttributeModifierInput("", this);
			modifierInput.setDefaultAttributeModifier(modifier, slot);
			this.itemAttributesInput.addComponent(modifierInput);
		});
		
		this.copyFrom = null;
	}
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_TEXTURE) {
    		this.openTextureDialogue();
    	} else {
    		super.actionPerformed(button);
    	}
    }
    
    private void openTextureDialogue() {
    	GuiScreen nextScreen;
    	if (this.isNew) {
    		 nextScreen = new GuiMessagePopupTwoButton(this.parentScreen, this.parentScreen, I18n.format("gui.warnDialogue.restart.created.title"), new TextComponentTranslation("gui.warnDialogue.restart.created.message"), I18n.format("gui.warnDialogue.restart.close"), I18n.format("gui.warnDialogue.restart.continue")) {
				@Override
				protected void actionPerformed(GuiButton button) throws IOException {
			        super.actionPerformed(button);
			        if (button.id == BUTTON_CONTINUE) {
			            this.mc.shutdown();
			        }
			    }
			};
    	} else {
    		nextScreen = this;
    	}
    	
    	GuiSelectItemTexturePopup selectTextureScreen = new GuiSelectItemTexturePopup(nextScreen, this.addon, this.item);
    	if (this.isNew) {
    		selectTextureScreen.setHasBackButton(false);
    	}
    	this.mc.displayGuiScreen(selectTextureScreen);
    }

}
