package com.tmtravlr.additions.addon.effects.cause;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Template for a cause that uses an item.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public abstract class EffectCauseItem extends EffectCause {
	
	public ItemStack itemStack;
	
	protected boolean itemMatches(ItemStack stackToCheck) {
		if (OreDictionary.itemMatches(this.itemStack, stackToCheck, false) && NBTUtil.areNBTEquals(this.itemStack.getTagCompound(), stackToCheck.getTagCompound(), true)) {
			return true;
		}
		return false;
	}

}
