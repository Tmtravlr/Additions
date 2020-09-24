package com.tmtravlr.additions.addon.items.blocks;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.BlockAddedSlab;
import com.tmtravlr.additions.addon.blocks.BlockAddedSlab.EnumAddedSlabHalf;
import com.tmtravlr.additions.addon.items.IItemAdded;
import mcp.MethodsReturnNonnullByDefault;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * ItemBlock for a slab block.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date May 2019
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemAddedBlockSlab extends ItemAddedBlockSimple {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "block_slab");
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (!stack.isEmpty() && player.canPlayerEdit(pos.offset(facing), facing, stack)) {
	        IBlockState state = world.getBlockState(pos);

            if (state.getBlock() == this.block) {
            	EnumAddedSlabHalf halfState = state.getValue(BlockAddedSlab.HALF);
            	
            	if (this.canMakeFullSlab(facing, halfState)) {
	            	this.placeFullBlock(player, stack, world, pos);
	                return EnumActionResult.SUCCESS;
            	}
            }

            return this.tryPlace(player, stack, world, pos.offset(facing)) ? EnumActionResult.SUCCESS : super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
	@SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == this.block) {
        	EnumAddedSlabHalf halfState = state.getValue(BlockAddedSlab.HALF);

            if (this.canMakeFullSlab(side, halfState)) {
                return true;
            }
        }

        pos = pos.offset(side);
        IBlockState offsetState = world.getBlockState(pos);
        return offsetState.getBlock() == this.block && offsetState.getValue(BlockAddedSlab.HALF) != EnumAddedSlabHalf.FULL || super.canPlaceBlockOnSide(world, pos, side, player, stack);
    }

    private boolean tryPlace(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == this.block) {
        	this.placeFullBlock(player, stack, world, pos);
            return true;
        }

        return false;
    }
    
    private void placeFullBlock(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
    	IBlockState fullState = block.getDefaultState().withProperty(BlockAddedSlab.HALF, EnumAddedSlabHalf.FULL);
        AxisAlignedBB boundingBox = fullState.getCollisionBoundingBox(world, pos);

        if (boundingBox != Block.NULL_AABB && boundingBox != null && world.checkNoEntityCollision(boundingBox.offset(pos)) && world.setBlockState(pos, fullState, 11)) {
            SoundType soundtype = block.getSoundType(fullState, world, pos, player);
            world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            stack.shrink(1);

            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            }
        }
    }
    
    private boolean canMakeFullSlab(EnumFacing facing, EnumAddedSlabHalf half) {
    	return half != EnumAddedSlabHalf.FULL && (facing == EnumFacing.DOWN && half == EnumAddedSlabHalf.TOP || facing == EnumFacing.UP && half == EnumAddedSlabHalf.BOTTOM ||
    			this.doWallSlabs() && (facing == EnumFacing.NORTH && half == EnumAddedSlabHalf.SOUTH || facing == EnumFacing.SOUTH && half == EnumAddedSlabHalf.NORTH ||
        		facing == EnumFacing.EAST && half == EnumAddedSlabHalf.WEST || facing == EnumFacing.WEST && half == EnumAddedSlabHalf.EAST));
    }
    
    private boolean doWallSlabs() {
    	if (!(this.block instanceof BlockAddedSlab)) {
    		throw new IllegalArgumentException("A slab item's block must be a slab block");
    	}
    	
    	return ((BlockAddedSlab) this.block).placeOnWalls;
    }
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedBlockSlab> {
		
		public Serializer() {
			super(TYPE, ItemAddedBlockSlab.class);
		}
		
		@Override
		public ItemAddedBlockSlab deserialize(JsonObject json, JsonDeserializationContext context) {
			ItemAddedBlockSlab itemAdded = new ItemAddedBlockSlab();
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
    }

}
