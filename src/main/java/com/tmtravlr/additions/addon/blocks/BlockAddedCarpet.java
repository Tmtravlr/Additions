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
	
	public boolean placeOnWalls;
	public boolean placeOnCeiling;
	public boolean needsSupport = true;

	public BlockAddedCarpet() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(0)).withProperty(BlockDirectional.FACING, EnumFacing.UP));
		this.setBoundingBoxMaxY(0.0625F);
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

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	return this.rotateBoundingBox(super.getBoundingBox(state, source, pos), state);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
    	AxisAlignedBB boundingBox = super.getCollisionBoundingBox(state, world, pos);
    	
    	if (boundingBox != Block.NULL_AABB && !this.hasSameCollisionBoundingBox()) {
    		boundingBox = this.rotateBoundingBox(boundingBox, state);
    	}
    	return boundingBox;
    }
    
    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
    	AxisAlignedBB boundingBox = this.getBoundingBox(state, world, pos);
    	return boundingBox.maxY == 1 && boundingBox.minX <= 0.45 && boundingBox.minZ <= 0.45 && boundingBox.maxX >= 0.55 && boundingBox.maxZ >= 0.55;
    }
    
    @Override
    public boolean isTopSolid(IBlockState state) {
    	AxisAlignedBB boundingBox = this.rotateBoundingBox(new AxisAlignedBB(this.getBoundingBoxMinX(), this.getBoundingBoxMinY(), this.getBoundingBoxMinZ(), this.getBoundingBoxMaxX(), this.getBoundingBoxMaxY(), this.getBoundingBoxMaxZ()), state);
        return state.getMaterial().isOpaque() && boundingBox.maxY == 1 && boundingBox.minX == 0 && boundingBox.minZ == 0 && boundingBox.maxX == 1 && boundingBox.maxZ == 1;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    	if (!this.isNormalCube(state, world, pos)) {
    		return BlockFaceShape.UNDEFINED;
    	} else {
    		AxisAlignedBB boundingBox = this.getBoundingBox(state, world, pos);
	    	boolean solid = false;
	    	
	    	if (face == EnumFacing.UP) {
	    		solid = boundingBox.maxY == 1 && boundingBox.minX <= 0 && boundingBox.minZ <= 0 && boundingBox.maxX >= 1 && boundingBox.maxZ >= 1;
	    	} else if (face == EnumFacing.DOWN) {
	    		solid = boundingBox.minY == 0 && boundingBox.minX <= 0 && boundingBox.minZ <= 0 && boundingBox.maxX >= 1 && boundingBox.maxZ >= 1;
	    	} else if (face == EnumFacing.EAST) {
	    		solid = boundingBox.maxX == 1 && boundingBox.minY <= 0 && boundingBox.minZ <= 0 && boundingBox.maxY >= 1 && boundingBox.maxZ >= 1;
	    	} else if (face == EnumFacing.WEST) {
	    		solid = boundingBox.minX == 0 && boundingBox.minY <= 0 && boundingBox.minZ <= 0 && boundingBox.maxY >= 1 && boundingBox.maxZ >= 1;
	    	} else if (face == EnumFacing.SOUTH) {
	    		solid = boundingBox.maxZ == 1 && boundingBox.minY <= 0 && boundingBox.minX <= 0 && boundingBox.maxY >= 1 && boundingBox.maxX >= 1;
	    	} else if (face == EnumFacing.NORTH) {
	    		solid = boundingBox.minZ == 0 && boundingBox.minY <= 0 && boundingBox.minX <= 0 && boundingBox.maxY >= 1 && boundingBox.maxX >= 1;
	    	}
	    	return solid ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    	}
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return this.getBlockFaceShape(world, state, pos, face) == BlockFaceShape.SOLID;
    }
    
    private AxisAlignedBB rotateBoundingBox(AxisAlignedBB original, IBlockState state) {
    	switch(state.getValue(BlockDirectional.FACING)) {
    	case NORTH:
    		return new AxisAlignedBB(original.minX, original.minZ, 1 - original.maxY, original.maxX, original.maxZ, 1 - original.minY);
    	case EAST:
    		return new AxisAlignedBB(original.minY, 1 - original.maxX, original.minZ, original.maxY, 1 - original.minX, original.maxZ);
    	case SOUTH:
    		return new AxisAlignedBB(original.minX, 1 - original.maxZ, original.minY, original.maxX, 1 - original.minZ, original.maxY);
    	case WEST:
    		return new AxisAlignedBB(1 - original.maxY, original.minX, original.minZ, 1 - original.minY, original.maxX, original.maxZ);
    	case DOWN:
    		return new AxisAlignedBB(original.minX, 1 - original.maxY, original.minZ, original.maxX, 1 - original.minY, original.maxZ);
    	default:
    		return original;
    	}
    }

	public static class Serializer extends IBlockAdded.Serializer<BlockAddedCarpet> {
		
		public Serializer() {
			super(TYPE, BlockAddedCarpet.class);
		}
		
		@Override
		public JsonObject serialize(BlockAddedCarpet blockAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(blockAdded, context);
			
			if (!blockAdded.hasCollisionBox()) {
				json.addProperty("has_collision_box", false);
			}
			
			if (!blockAdded.hasSameCollisionBoundingBox()) {
				json.addProperty("same_collision_bounding_box", false);
			}
			
			if (blockAdded.getBoundingBoxMinX() != 0) {
				json.addProperty("bounding_box_min_x", blockAdded.getBoundingBoxMinX());
			}
			
			if (blockAdded.getBoundingBoxMinY() != 0) {
				json.addProperty("bounding_box_min_y", blockAdded.getBoundingBoxMinY());
			}
			
			if (blockAdded.getBoundingBoxMinZ() != 0) {
				json.addProperty("bounding_box_min_z", blockAdded.getBoundingBoxMinZ());
			}
			
			if (blockAdded.getBoundingBoxMaxX() != 1) {
				json.addProperty("bounding_box_max_x", blockAdded.getBoundingBoxMaxX());
			}
			
			if (blockAdded.getBoundingBoxMaxY() != 0.0625F) {
				json.addProperty("bounding_box_max_y", blockAdded.getBoundingBoxMaxY());
			}
			
			if (blockAdded.getBoundingBoxMaxZ() != 1) {
				json.addProperty("bounding_box_max_z", blockAdded.getBoundingBoxMaxZ());
			}
			
			if (blockAdded.getCollisionBoxMinX() != 0) {
				json.addProperty("collision_box_min_x", blockAdded.getCollisionBoxMinX());
			}
			
			if (blockAdded.getCollisionBoxMinY() != 0) {
				json.addProperty("collision_box_min_y", blockAdded.getCollisionBoxMinY());
			}
			
			if (blockAdded.getCollisionBoxMinZ() != 0) {
				json.addProperty("collision_box_min_z", blockAdded.getCollisionBoxMinZ());
			}
			
			if (blockAdded.getCollisionBoxMaxX() != 1) {
				json.addProperty("collision_box_max_x", blockAdded.getCollisionBoxMaxX());
			}
			
			if (blockAdded.getCollisionBoxMaxY() != 0.0625F) {
				json.addProperty("collision_box_max_y", blockAdded.getCollisionBoxMaxY());
			}
			
			if (blockAdded.getCollisionBoxMaxZ() != 1) {
				json.addProperty("collision_box_max_z", blockAdded.getCollisionBoxMaxZ());
			}
			
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

			blockAdded.setHasCollisionBox(JsonUtils.getBoolean(json, "has_collision_box", true));
			blockAdded.setHasSameCollisionBoundingBox(JsonUtils.getBoolean(json, "same_collision_bounding_box", true));
			blockAdded.setBoundingBoxMinX(JsonUtils.getFloat(json, "bounding_box_min_x", 0));
			blockAdded.setBoundingBoxMinY(JsonUtils.getFloat(json, "bounding_box_min_y", 0));
			blockAdded.setBoundingBoxMinZ(JsonUtils.getFloat(json, "bounding_box_min_z", 0));
			blockAdded.setBoundingBoxMaxX(Math.max(JsonUtils.getFloat(json, "bounding_box_max_x", 1), blockAdded.getBoundingBoxMinX()));
			blockAdded.setBoundingBoxMaxY(Math.max(JsonUtils.getFloat(json, "bounding_box_max_y", 0.0625F), blockAdded.getBoundingBoxMinY()));
			blockAdded.setBoundingBoxMaxZ(Math.max(JsonUtils.getFloat(json, "bounding_box_max_z", 1), blockAdded.getBoundingBoxMinZ()));
			blockAdded.setCollisionBoxMinX(JsonUtils.getFloat(json, "collision_box_min_x", 0));
			blockAdded.setCollisionBoxMinY(JsonUtils.getFloat(json, "collision_box_min_y", 0));
			blockAdded.setCollisionBoxMinZ(JsonUtils.getFloat(json, "collision_box_min_z", 0));
			blockAdded.setCollisionBoxMaxX(Math.max(JsonUtils.getFloat(json, "collision_box_max_x", 1), blockAdded.getCollisionBoxMinX()));
			blockAdded.setCollisionBoxMaxY(Math.max(JsonUtils.getFloat(json, "collision_box_max_y", 0.0625F), blockAdded.getCollisionBoxMinY()));
			blockAdded.setCollisionBoxMaxZ(Math.max(JsonUtils.getFloat(json, "collision_box_max_z", 1), blockAdded.getCollisionBoxMinZ()));
			blockAdded.placeOnWalls = JsonUtils.getBoolean(json, "place_on_walls", false);
			blockAdded.placeOnCeiling = JsonUtils.getBoolean(json, "place_on_ceiling", false);
			blockAdded.needsSupport = JsonUtils.getBoolean(json, "needs_support", true);
			
			return blockAdded;
		}
    }
}
