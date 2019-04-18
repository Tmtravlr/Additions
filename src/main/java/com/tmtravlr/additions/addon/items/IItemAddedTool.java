package com.tmtravlr.additions.addon.items;

import net.minecraft.item.Item;

public interface IItemAddedTool {

	public Item.ToolMaterial getToolMaterial();
	
	public boolean shouldApplyVanillaAttributes();
	
	public void setToolMaterial(Item.ToolMaterial toolMaterial);
	
	public void setApplyVanillaAttributes(boolean applyVanillaAttributes);
	
}
