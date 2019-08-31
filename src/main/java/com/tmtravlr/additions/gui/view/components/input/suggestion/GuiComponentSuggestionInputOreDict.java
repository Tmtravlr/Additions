package com.tmtravlr.additions.gui.view.components.input.suggestion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.tmtravlr.additions.ConfigLoader;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Dropdown list specifically for items, which renders the items in the list.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017
 */
public class GuiComponentSuggestionInputOreDict extends GuiComponentSuggestionInput {

	public GuiComponentSuggestionInputOreDict(String label, GuiEdit editScreen) {
		super(label, editScreen);

		this.setSuggestions(Arrays.asList(OreDictionary.getOreNames()));
	}
	
	public void refreshOreNames() {
		this.setSuggestions(Arrays.asList(OreDictionary.getOreNames()));
	}
	
	@Override
	protected GuiScrollingSuggestion createScrollingSuggestion(int dropdownHeight, boolean above) {
		return new GuiScrollingSuggestion(this, this.suggestions, dropdownHeight, above) {
			
			private Map<String, ItemStack> displayStacks = new HashMap<>();
			private int refreshCount = 0;
			private int displayRefreshTime = 0;
			private Map<String, NonNullList<ItemStack>> cachedDisplayMap = new HashMap<>();
			
			@Override
			public void drawScreen(int mouseX, int mouseY, float partialTicks) {
				if (this.displayRefreshTime-- <= 0) {
					this.displayRefreshTime = 40;
					this.refreshCount++;
					
					this.updateDisplayStacks();
				}
				
				super.drawScreen(mouseX, mouseY, partialTicks);
			}

			@Override
			protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
				if (!this.suggestionDisplay.isEmpty()) {
					String oreToDisplay = this.suggestionDisplay.get(slot);
					
					if (oreToDisplay != null) {
						int textOffset = 5;
						
						if (ConfigLoader.renderItemsInLists.getBoolean(true)) {
							ItemStack displayStack = this.getDisplayStack(oreToDisplay).copy();
							if (displayStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
								displayStack.setItemDamage(0);
							}
							
							int itemX = this.left + 5;
							int itemY = top - 1;
							textOffset = 25;
	
							this.parentInput.editScreen.renderItemStack(displayStack, itemX, itemY, 0, 0, false, true);
						}
						
						this.parentInput.editScreen.getFontRenderer().drawString(oreToDisplay, this.left + textOffset, top + 2, 0xFFFFFF);
					}
				} else {
					this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.dropdown.nothingToShow"), this.left + 5, top + 2, 0x888888);
				}
			}
			
			private void forceDisplayRefresh() {
				this.displayRefreshTime = 0;
			}
			
			// Gets an item stack that should display based on the time.
			private ItemStack getDisplayStack(String oreToDisplay) {
				if (this.displayStacks.containsKey(oreToDisplay)) {
					return this.displayStacks.get(oreToDisplay);
				}
				
				return ItemStack.EMPTY;
			}
			
			private void updateDisplayStacks() {
				for (String oreToDisplay : this.suggestionDisplay) {
					if (!this.cachedDisplayMap.containsKey(oreToDisplay)) {
						this.cachedDisplayMap.put(oreToDisplay, this.getOreStacks(oreToDisplay));
					}
					
					NonNullList<ItemStack> cachedDisplayItems = this.cachedDisplayMap.get(oreToDisplay);
					
					if (this.refreshCount >= 10000) {
						this.refreshCount = 0;
					}
					
					if (!cachedDisplayItems.isEmpty()) {
						int index = this.refreshCount % cachedDisplayItems.size();
						this.displayStacks.put(oreToDisplay, cachedDisplayItems.get(index));
					} else {
						this.displayStacks.put(oreToDisplay, ItemStack.EMPTY);
					}
				}
			}
			
			private NonNullList<ItemStack> getOreStacks(String oreName) {
				NonNullList<ItemStack> oreStacks = NonNullList.create();
				
				for (ItemStack stack : OreDictionary.getOres(oreName)) {
					if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
				    	NonNullList<ItemStack> itemsInCreativeTab = NonNullList.create();
				        stack.getItem().getSubItems(CreativeTabs.SEARCH, itemsInCreativeTab);
				        
				        if (!itemsInCreativeTab.isEmpty()) {
				        	oreStacks.addAll(itemsInCreativeTab);
				        } else {
				        	oreStacks.add(new ItemStack(stack.getItem()));
				        }
				    } else {
				    	oreStacks.add(stack);
				    }
				}
				
				return oreStacks;
			}
		};
	}

}
