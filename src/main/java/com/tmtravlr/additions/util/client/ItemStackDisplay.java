package com.tmtravlr.additions.util.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import scala.actors.threadpool.Arrays;

public class ItemStackDisplay {

	private static final int DISPLAY_REFRESH_DELAY = 40;

	private int displayTimer = 0;
	private List<String> cachedDisplayText = new ArrayList<>();
	private NonNullList<ItemStack> cachedDisplayStacks = NonNullList.create();
    
    public void updateDisplay(ItemStack... stackArray) {
    	NonNullList<ItemStack> stacks = NonNullList.create();
    	stacks.addAll(Arrays.asList(stackArray));
    	this.updateDisplay(stacks);
    }
    
    public void updateDisplay(NonNullList<ItemStack> stacks) {
    	if (--this.displayTimer < 0) {
    		this.cachedDisplayStacks.clear();
    		this.cachedDisplayText.clear();
	    	
	    	for (ItemStack stack : stacks) {
	    		ItemStack copy = stack.copy();
	        	
	        	if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
	        		this.getDisplayStacksForWildcard(copy).forEach(displayStack -> {
	        			this.cachedDisplayStacks.add(displayStack);
	        			this.cachedDisplayText.add(this.getDisplayText(displayStack));
	        		});
	        	} else {	        	
		        	this.cachedDisplayStacks.add(copy);
		        	this.cachedDisplayText.add(this.getDisplayText(copy));
	        	}
	    	}
	    	
			this.displayTimer = this.cachedDisplayStacks.size()*DISPLAY_REFRESH_DELAY - 1;
		}
    }
    
    public ItemStack getDisplayStack() {
    	return this.cachedDisplayStacks.isEmpty() ? ItemStack.EMPTY : this.cachedDisplayStacks.get(this.displayTimer / DISPLAY_REFRESH_DELAY);
    }
    
    public String getDisplayText() {
    	return this.cachedDisplayText.isEmpty() ? "" : this.cachedDisplayText.get(this.displayTimer / DISPLAY_REFRESH_DELAY);
    }
    
    private NonNullList<ItemStack> getDisplayStacksForWildcard(ItemStack stack) {
    	NonNullList<ItemStack> stacks = NonNullList.create();
        stack.getItem().getSubItems(CreativeTabs.SEARCH, stacks);
        
        if (stacks.isEmpty()) {
        	ItemStack copy = stack.copy();
        	copy.setItemDamage(0);
        	stacks.add(copy);
		}
        
        return stacks;
    }
    
    protected String getDisplayText(ItemStack stack) {
    	return stack.getDisplayName();
    }

}
