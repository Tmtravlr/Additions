//package com.tmtravlr.additions.addon.blocks;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonSerializationContext;
//import com.tmtravlr.additions.AdditionsMod;
//import com.tmtravlr.additions.util.BlockStateInfo;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockDirt;
//import net.minecraft.block.BlockGrass;
//import net.minecraft.block.BlockLiquid;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.init.Blocks;
//import net.minecraft.util.JsonUtils;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//
///**
// * An added block that can spread onto another block like dirt. Can be snowy too.
// * 
// * @author Rebeca Rey (Tmtravlr)
// * @since July 2019
// */
//public class BlockAddedGrass extends BlockAddedSimple {
//	
//	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "grass");
//	
//	private static final BlockStateInfo DIRT_BLOCK = new BlockStateInfo(Blocks.DIRT, Collections.singletonMap(BlockDirt.VARIANT.getName(), BlockDirt.DirtType.DIRT.getName()));
//	
//	public List<BlockStateInfo> spreadBlocks = Collections.singletonList(DIRT_BLOCK);
//	public boolean allowSnowy = true;
//	public int minSpreadLight = 9;
//	public int maxSpreadLight = 15;
//	public int minGrowthLight = 4;
//	public int maxGrowthLight = 15;
//
//	public BlockAddedGrass() {
//		this.setDefaultState(this.getDefaultState().withProperty(BlockGrass.SNOWY, Boolean.valueOf(false)));
//        this.setTickRandomly(true);
//	}
//	
//	@Override
//	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
//		if (this.allowSnowy) {
//	        Block block = world.getBlockState(pos.up()).getBlock();
//	        return state.withProperty(BlockGrass.SNOWY, Boolean.valueOf(block == Blocks.SNOW || block == Blocks.SNOW_LAYER));
//		}
//		return super.getActualState(state, world, pos);
//    }
//
//    @Override
//	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
//        if (!world.isRemote && !this.spreadBlocks.isEmpty() && world.isAreaLoaded(pos, 2)) {
//            if (!this.canStayAt(world, world.getBlockState(pos.up()), pos.up())) {
//            	world.setBlockState(pos, this.spreadBlocks.get(0).getBlockState());
//            } else {
//            	int light = world.getLightFromNeighbors(pos.up());
//            	
//                if (light >= this.minSpreadLight && light <= this.maxSpreadLight) {
//                	
//                    for (int i = 0; i < 4; ++i) {
//                        BlockPos spreadPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
//                        BlockPos spreadPosAbove = spreadPos.up();
//                        IBlockState spreadState = world.getBlockState(spreadPos);
//                        IBlockState spreadStateAbove = world.getBlockState(spreadPosAbove);
//
//                        if (this.canSpreadTo(spreadState) && this.canStayAt(world, spreadStateAbove, spreadPosAbove)) {
//                            world.setBlockState(spreadPos, this.getDefaultState());
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//	protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, new IProperty[] {BlockLiquid.LEVEL, BlockGrass.SNOWY});
//    }
//    
//    private boolean canStayAt(World world, IBlockState state, BlockPos pos) {
//    	return world.getLightFromNeighbors(pos) <= maxGrowthLight && world.getLightFromNeighbors(pos) >= minGrowthLight && state.getLightOpacity(world, pos) <= 2;
//    }
//    
//    private boolean canSpreadTo(IBlockState state) {
//    	for (BlockStateInfo info : this.spreadBlocks) {
//    		if (info.matches(state)) {
//    			return true;
//    		}
//    	}
//    	
//    	return false;
//    }
//	
//	public static class Serializer extends IBlockAdded.Serializer<BlockAddedGrass> {
//		
//		public Serializer() {
//			super(TYPE, BlockAddedGrass.class);
//		}
//		
//		@Override
//		public JsonObject serialize(BlockAddedGrass blockAdded, JsonSerializationContext context) {
//			JsonObject json = super.serialize(blockAdded, context);
//			
//			if (!blockAdded.hasCollisionBox()) {
//				json.addProperty("has_collision_box", false);
//			}
//			
//			if (!blockAdded.hasSameCollisionBoundingBox()) {
//				json.addProperty("same_collision_bounding_box", false);
//			}
//			
//			if (blockAdded.getBoundingBoxMinX() != 0) {
//				json.addProperty("bounding_box_min_x", blockAdded.getBoundingBoxMinX());
//			}
//			
//			if (blockAdded.getBoundingBoxMinY() != 0) {
//				json.addProperty("bounding_box_min_y", blockAdded.getBoundingBoxMinY());
//			}
//			
//			if (blockAdded.getBoundingBoxMinZ() != 0) {
//				json.addProperty("bounding_box_min_z", blockAdded.getBoundingBoxMinZ());
//			}
//			
//			if (blockAdded.getBoundingBoxMaxX() != 1) {
//				json.addProperty("bounding_box_max_x", blockAdded.getBoundingBoxMaxX());
//			}
//			
//			if (blockAdded.getBoundingBoxMaxY() != 1) {
//				json.addProperty("bounding_box_max_y", blockAdded.getBoundingBoxMaxY());
//			}
//			
//			if (blockAdded.getBoundingBoxMaxZ() != 1) {
//				json.addProperty("bounding_box_max_z", blockAdded.getBoundingBoxMaxZ());
//			}
//			
//			if (blockAdded.getCollisionBoxMinX() != 0) {
//				json.addProperty("collision_box_min_x", blockAdded.getCollisionBoxMinX());
//			}
//			
//			if (blockAdded.getCollisionBoxMinY() != 0) {
//				json.addProperty("collision_box_min_y", blockAdded.getCollisionBoxMinY());
//			}
//			
//			if (blockAdded.getCollisionBoxMinZ() != 0) {
//				json.addProperty("collision_box_min_z", blockAdded.getCollisionBoxMinZ());
//			}
//			
//			if (blockAdded.getCollisionBoxMaxX() != 1) {
//				json.addProperty("collision_box_max_x", blockAdded.getCollisionBoxMaxX());
//			}
//			
//			if (blockAdded.getCollisionBoxMaxY() != 1) {
//				json.addProperty("collision_box_max_y", blockAdded.getCollisionBoxMaxY());
//			}
//			
//			if (blockAdded.getCollisionBoxMaxZ() != 1) {
//				json.addProperty("collision_box_max_z", blockAdded.getCollisionBoxMaxZ());
//			}
//			
//			return json;
//		}
//		
//		@Override
//		public BlockAddedGrass deserialize(JsonObject json, JsonDeserializationContext context) {
//			BlockAddedGrass blockAdded = new BlockAddedGrass();
//			super.deserializeDefaults(json, context, blockAdded);
//			
//			blockAdded.setHasCollisionBox(JsonUtils.getBoolean(json, "has_collision_box", true));
//			blockAdded.setHasSameCollisionBoundingBox(JsonUtils.getBoolean(json, "same_collision_bounding_box", true));
//			blockAdded.setBoundingBoxMinX(JsonUtils.getFloat(json, "bounding_box_min_x", 0));
//			blockAdded.setBoundingBoxMinY(JsonUtils.getFloat(json, "bounding_box_min_y", 0));
//			blockAdded.setBoundingBoxMinZ(JsonUtils.getFloat(json, "bounding_box_min_z", 0));
//			blockAdded.setBoundingBoxMaxX(Math.max(JsonUtils.getFloat(json, "bounding_box_max_x", 1), blockAdded.getBoundingBoxMinX()));
//			blockAdded.setBoundingBoxMaxY(Math.max(JsonUtils.getFloat(json, "bounding_box_max_y", 1), blockAdded.getBoundingBoxMinY()));
//			blockAdded.setBoundingBoxMaxZ(Math.max(JsonUtils.getFloat(json, "bounding_box_max_z", 1), blockAdded.getBoundingBoxMinZ()));
//			blockAdded.setCollisionBoxMinX(JsonUtils.getFloat(json, "collision_box_min_x", 0));
//			blockAdded.setCollisionBoxMinY(JsonUtils.getFloat(json, "collision_box_min_y", 0));
//			blockAdded.setCollisionBoxMinZ(JsonUtils.getFloat(json, "collision_box_min_z", 0));
//			blockAdded.setCollisionBoxMaxX(Math.max(JsonUtils.getFloat(json, "collision_box_max_x", 1), blockAdded.getCollisionBoxMinX()));
//			blockAdded.setCollisionBoxMaxY(Math.max(JsonUtils.getFloat(json, "collision_box_max_y", 1), blockAdded.getCollisionBoxMinY()));
//			blockAdded.setCollisionBoxMaxZ(Math.max(JsonUtils.getFloat(json, "collision_box_max_z", 1), blockAdded.getCollisionBoxMinZ()));
//			
//			return blockAdded;
//		}
//    }
//
//}
