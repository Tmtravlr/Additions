package com.tmtravlr.additions.addon.items.blocks;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.BlockAddedLadder;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.items.IItemAdded;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ItemBlock for a slab block.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019
 */
public class ItemAddedBlockLadder extends ItemAddedBlockSimple {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "block_ladder");
	
	private BlockAddedLadder blockLadder;
	
	@Override
	public void setBlock(IBlockAdded block) {
		if (!(block instanceof BlockAddedLadder)) {
    		throw new IllegalArgumentException("A ladder item's block must be a ladder block");
    	}
		
		this.blockLadder = (BlockAddedLadder) block;
		super.setBlock(block);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        IBlockState placementState = null;
        
        BlockPos placeBelow = player.isSneaking() ? null : offsetIfPlaceDownward(world, pos);
        
        if (placeBelow != null) {
        	pos = placeBelow;
        	placementState = world.getBlockState(pos.up());
        	facing = placementState.getValue(BlockLadder.FACING);
        } else if (!block.isReplaceable(world, pos)) {
            pos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && world.mayPlace(this.block, pos, false, facing, (Entity)null)) {
            int i = this.getMetadata(itemstack.getMetadata());
            
            if (placementState == null) {
            	placementState = this.block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, i, player, hand);
            }

            if (placeBlockAt(itemstack, player, world, pos, facing, hitX, hitY, hitZ, placementState)) {
            	placementState = world.getBlockState(pos);
                SoundType soundtype = placementState.getBlock().getSoundType(placementState, world, pos, player);
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

	@Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
		if (this.blockLadder != null && this.blockLadder.placeDownward && world.getBlockState(pos).getBlock() == this.block) {
    		return true;
    	}
    	
    	return super.canPlaceBlockOnSide(world, pos, side, player, stack);
    }
    
    private BlockPos offsetIfPlaceDownward(World world, BlockPos pos) {
    	if (!(this.block instanceof BlockAddedLadder)) {
    		throw new IllegalArgumentException("A slab item's block must be a slab block");
    	}
    	
    	if (this.blockLadder != null && this.blockLadder.placeDownward && world.getBlockState(pos).getBlock() == this.block) {
    		return findPosBelow(world, pos);
    	}
    	
    	return null;
    }
    
    private BlockPos findPosBelow(World world, BlockPos pos) {
    	EnumFacing facing = world.getBlockState(pos).getValue(BlockLadder.FACING);
    	pos = pos.down();
		IBlockState blockState = world.getBlockState(pos);
    	
		if (world.isOutsideBuildHeight(pos)) {
			return null;
		} else if (blockState.getBlock() == this.block) {
    		return findPosBelow(world, pos);
    	} else if (blockState.getBlock().isReplaceable(world, pos) && this.blockLadder.canAttachTo(world, pos.offset(facing.getOpposite()), facing)) {
    		return pos;
    	} else {
    		return null;
    	}
    }
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedBlockLadder> {
		
		public Serializer() {
			super(TYPE, ItemAddedBlockLadder.class);
		}
		
		@Override
		public ItemAddedBlockLadder deserialize(JsonObject json, JsonDeserializationContext context) {
			ItemAddedBlockLadder itemAdded = new ItemAddedBlockLadder();
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
    }

}
