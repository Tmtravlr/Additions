package com.tmtravlr.additions.addon.items.blocks;

import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.items.IItemAdded;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public interface IItemAddedBlock extends IItemAdded {

	void setBlock(IBlockAdded block);
	
	Block getBlock();
	
	default ItemBlock getAsItemBlock() {
		if (!(this instanceof ItemBlock)) {
			throw new IllegalArgumentException("An IItemAddedBlock must be an instance of ItemBlock.");
		}
		return (ItemBlock) this;
	}
	
}
