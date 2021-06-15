package com.tmtravlr.additions.addon.effects.cause;

import com.tmtravlr.additions.util.BlockStateInfo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Template for a cause that uses a block.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public abstract class EffectCauseBlock extends EffectCause {
	
	public BlockStateInfo blockState;
	public NBTTagCompound blockTag;
	
	protected boolean blockMatches(IBlockState blockToCheck, NBTTagCompound tagToCheck) {
		if (this.blockState.matches(blockToCheck) && NBTUtil.areNBTEquals(this.blockTag, tagToCheck, true)) {
			return true;
		}
		return false;
	}

}
