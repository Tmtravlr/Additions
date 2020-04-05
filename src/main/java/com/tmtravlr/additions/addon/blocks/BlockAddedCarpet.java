package com.tmtravlr.additions.addon.blocks;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Basic Block
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019 
 */
public class BlockAddedCarpet extends BlockAddedSimple {

	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "carpet");
	public static final AxisAlignedBB CARPET_DEFAULT_BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
	
	public boolean placeOnWalls;
	public boolean placeOnCeiling;
	public boolean needsSupport = true;

	public BlockAddedCarpet() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(0)).withProperty(BlockDirectional.FACING, EnumFacing.UP));
		this.getBoundingBox().setMaxY(0.0625F);
	}
	
	@Override
	public AxisAlignedBB modifyBoundingBoxForState(AxisAlignedBB original, IBlockState state) {
    	switch(state.getValue(BlockDirectional.FACING)) {
    	case EAST:
    		return new AxisAlignedBB(original.minY, 1 - original.minX, 1 - original.minZ, original.maxY, 1 - original.maxX, 1 - original.maxZ);
    	case WEST:
    		return new AxisAlignedBB(1 - original.maxY, 1 - original.minX, original.minZ, 1 - original.minY, 1 - original.maxX, original.maxZ);
    	case NORTH:
    		return new AxisAlignedBB(1 - original.minZ, 1 - original.minX, 1 - original.minY, 1 - original.maxZ, 1 - original.maxX, 1 - original.maxY);
    	case SOUTH:
    		return new AxisAlignedBB(original.minZ, 1 - original.minX, original.minY, original.maxZ, 1 - original.maxX, original.maxY);
    	case DOWN:
    		return new AxisAlignedBB(original.minZ, 1 - original.minY, 1 - original.minX, original.maxZ, 1 - original.maxY, 1 - original.maxX);
    	default:
    		return new AxisAlignedBB(original.minZ, original.minY, original.minX, original.maxZ, original.maxY, original.maxX);
    	}
    }

	@Override
	public AxisAlignedBB getDefaultBoundingBox() {
		return CARPET_DEFAULT_BB;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.getFront(meta));
    }

    @Override
	public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockDirectional.FACING).getIndex();
    }
	
    @Override
	protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {BlockLiquid.LEVEL, BlockDirectional.FACING});
    }

    @Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    	EnumFacing facing = state.getValue(BlockDirectional.FACING);
    	
        if (side != facing.getOpposite()) {
            return true;
        } else {
            return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? true : super.shouldSideBeRendered(state, blockAccess, pos, side);
        }
    }

    @Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    	EnumFacing placeFacing = EnumFacing.UP;
    	
    	if (this.placeOnWalls && (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH || facing == EnumFacing.WEST || facing == EnumFacing.EAST)) {
			placeFacing = facing;
    	} else if (this.placeOnCeiling && (facing == EnumFacing.DOWN || facing != EnumFacing.UP && !this.placeOnWalls && hitY >= 0.5D)) {
			placeFacing = EnumFacing.DOWN;
    	}
    	
    	if (this.needsSupport && world.isAirBlock(pos.offset(placeFacing.getOpposite()))) {
    		if (!world.isAirBlock(pos.down())) {
        		placeFacing = EnumFacing.UP;
        	} else if (this.placeOnCeiling && !world.isAirBlock(pos.up())) {
        		placeFacing = EnumFacing.DOWN;
        	} else if (this.placeOnWalls) {
        		if (!world.isAirBlock(pos.north())) {
        			placeFacing = EnumFacing.SOUTH;
        		} else if (!world.isAirBlock(pos.east())) {
        			placeFacing = EnumFacing.WEST;
        		} else if (!world.isAirBlock(pos.south())) {
        			placeFacing = EnumFacing.NORTH;
        		} else if (!world.isAirBlock(pos.west())) {
        			placeFacing = EnumFacing.EAST;
        		}
        	}
    	}
    	
    	return this.getDefaultState().withProperty(BlockDirectional.FACING, placeFacing);
    }
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
    	EnumFacing facing = state.getValue(BlockDirectional.FACING);
    	
    	if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
    		return state;
    	}
    	
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            	return state.withProperty(BlockDirectional.FACING, facing.rotateYCCW());
            case CLOCKWISE_90:
            	return state.withProperty(BlockDirectional.FACING, facing.rotateY());
            default:
            	return state.withProperty(BlockDirectional.FACING, facing.getOpposite());
        }
    }
    
    @Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
        if (super.canPlaceBlockAt(world, pos)) {
        	if (!this.needsSupport) {
        		return true;
        	}
        	
        	if (!world.isAirBlock(pos.down())) {
        		return true;
        	}
        	
        	if (this.placeOnCeiling && !world.isAirBlock(pos.up())) {
        		return true;
        	}
        	
        	if (this.placeOnWalls && (!world.isAirBlock(pos.north()) || !world.isAirBlock(pos.east()) || !world.isAirBlock(pos.south()) || !world.isAirBlock(pos.west()))) {
        		return true;
        	}
        }
        
        return false;
    }

    @Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (this.needsSupport && world.isAirBlock(pos.offset(state.getValue(BlockDirectional.FACING).getOpposite()))) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

	public static class Serializer extends IBlockAdded.Serializer<BlockAddedCarpet> {
		
		public Serializer() {
			super(TYPE, BlockAddedCarpet.class);
		}
		
		@Override
		public JsonObject serialize(BlockAddedCarpet blockAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(blockAdded, context);
			
			IBlockAddedModifiableBoundingBox.Serializer.serialize(json, blockAdded);
			
			if (blockAdded.placeOnWalls) {
				json.addProperty("place_on_walls", true);
			}
			
			if (blockAdded.placeOnCeiling) {
				json.addProperty("place_on_ceiling", true);
			}
			
			if (!blockAdded.needsSupport) {
				json.addProperty("needs_support", false);
			}
			
			return json;
		}
		
		@Override
		public BlockAddedCarpet deserialize(JsonObject json, JsonDeserializationContext context) {
			BlockAddedCarpet blockAdded = new BlockAddedCarpet();
			super.deserializeDefaults(json, context, blockAdded);
			IBlockAddedModifiableBoundingBox.Serializer.deserialize(json, blockAdded);
			
			blockAdded.placeOnWalls = JsonUtils.getBoolean(json, "place_on_walls", false);
			blockAdded.placeOnCeiling = JsonUtils.getBoolean(json, "place_on_ceiling", false);
			blockAdded.needsSupport = JsonUtils.getBoolean(json, "needs_support", true);
			
			return blockAdded;
		}
    }
}
