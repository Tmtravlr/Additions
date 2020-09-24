//package com.tmtravlr.additions.addon.blocks;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSyntaxException;
//import com.tmtravlr.additions.AdditionsMod;
//import com.tmtravlr.additions.util.BlockStateInfo;
//import com.tmtravlr.additions.util.OtherSerializers;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockBush;
//import net.minecraft.block.BlockDirt;
//import net.minecraft.block.BlockGrass;
//import net.minecraft.block.BlockLiquid;
//import net.minecraft.block.BlockMushroom;
//import net.minecraft.block.BlockTallGrass;
//import net.minecraft.block.IGrowable;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.SoundEvents;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemHoe;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumActionResult;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.JsonUtils;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.SoundCategory;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//import net.minecraftforge.common.EnumPlantType;
//import net.minecraftforge.common.IPlantable;
//
///**
// * An added block that can spread onto another block like dirt. Can be snowy too.
// * 
// * @author Rebeca Rey (Tmtravlr)
// * @date July 2019
// */
//public class BlockAddedGrass extends BlockAddedSimple implements IGrowable {
//	
//	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "grass");
//	
//	private static final BlockStateInfo DIRT_BLOCK = new BlockStateInfo(Blocks.DIRT, Collections.singletonMap(BlockDirt.VARIANT.getName(), BlockDirt.DirtType.DIRT.getName()));
//	private static final BlockStateInfo PATH_BLOCK = new BlockStateInfo(Blocks.GRASS_PATH);
//	
//	public List<BlockStateInfo> spreadBlocks = Collections.singletonList(DIRT_BLOCK);
//	public BlockStateInfo pathBlock = PATH_BLOCK;
//	public List<IBlockState> bonemealBlocks = new ArrayList<>();
//	public ResourceLocation bonemealLootTable = null;
//	public boolean vanillaBonemeal = true;
//	public boolean allowBonemeal = true;
//	public boolean allowSnowy = true;
//	public boolean allowHoeing = true;
//	public float spreadChance = 1;
//	public int minLight = 4;
//	public int maxLight = 15;
//	public int minSpreadLight = 9;
//	public int maxSpreadLight = 15;
//
//	public BlockAddedGrass() {
//		this.setBlockMaterial(Material.GRASS);
//		this.setDefaultState(this.getDefaultState().withProperty(BlockGrass.SNOWY, Boolean.valueOf(false)).withProperty(BlockLiquid.LEVEL, 0));
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
//        if (!world.isRemote && !this.spreadBlocks.isEmpty() && rand.nextFloat() <= this.spreadChance && world.isAreaLoaded(pos, 2)) {
//        	
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
//    	return world.getLightFromNeighbors(pos) <= maxLight && world.getLightFromNeighbors(pos) >= minLight && state.getLightOpacity(world, pos) <= 2;
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
//    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
//        return vanillaBonemeal || !bonemealBlocks.isEmpty() || bonemealLootTable != null;
//    }
//
//    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
//        return canGrow(world, pos, state, world.isRemote);
//    }
//
//    public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
//        BlockPos posAbove = pos.up();
//
//        for (int i = 0; i < 128; ++i) {
//            BlockPos nearbyPos = posAbove;
//            int j = 0;
//
//            while (true) {
//                if (j >= i / 16) {
//                    if (world.isAirBlock(nearbyPos)) {
//                    	if (this.vanillaBonemeal) {
//	                        if (rand.nextInt(8) == 0) {
//	                        	world.getBiome(nearbyPos).plantFlower(world, rand, nearbyPos);
//	                        } else {
//	                            IBlockState tallgrassState = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
//	
//	                            if (Blocks.TALLGRASS.canBlockStay(world, nearbyPos, tallgrassState)) {
//	                            	world.setBlockState(nearbyPos, tallgrassState, 3);
//	                            }
//	                        }
//                    	} else if (!this.bonemealBlocks.isEmpty()) {
//                    		int index = rand.nextInt(this.bonemealBlocks.size());
//                    		IBlockState plantState = this.bonemealBlocks.get(index);
//                    		
//                    		if (plantState.getBlock().canPlaceBlockAt(world, nearbyPos)) {
//                    			world.setBlockState(nearbyPos, plantState, 3);
//                    		}
//                    	}
//                    }
//
//                    break;
//                }
//
//                nearbyPos = nearbyPos.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
//
//                if (world.getBlockState(nearbyPos.down()).getBlock() != this || world.getBlockState(nearbyPos).isNormalCube()) {
//                    break;
//                }
//
//                ++j;
//            }
//        }
//    }
//    
//    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//    	ItemStack stack = player.getHeldItem(hand);
//    	Item item = stack.getItem();	
//
//	    if (this.pathBlock != null && item.getToolClasses(stack).contains("shovel") && player.canPlayerEdit(pos.offset(facing), facing, stack) &&
//	    		facing != EnumFacing.DOWN && world.getBlockState(pos.up()).getMaterial() == Material.AIR) {
//            IBlockState pathState = this.pathBlock.getBlockState();
//            world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
//
//            if (!world.isRemote) {
//                world.setBlockState(pos, pathState, 11);
//                stack.damageItem(1, player);
//            }
//
//            return true;
//	    }
//	    
//	    if (this.allowHoeing && !this.spreadBlocks.isEmpty() && item instanceof ItemHoe || item.getToolClasses(stack).contains("hoe")) {
//			BlockStateInfo firstSpreadBlock = this.spreadBlocks.get(0);
//			
//			//Temporarily set the block to the first block in the spread block list
//			world.setBlockState(pos, firstSpreadBlock.getBlockState(), 4 | 16);
//			
//			EnumActionResult result = item.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
//			
//			if (result != EnumActionResult.SUCCESS) {
//				//Set the block back to the original block if it didn't get hoe'd
//				world.setBlockState(pos, state, 4 | 16);
//			} else {
//				return true;
//			}
//		}
//	    
//	    return false;
//    }
//    
//    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
//    	if (state.isSideSolid(world, pos, EnumFacing.UP)) {
//	    	if (!this.spreadBlocks.isEmpty()) {
//	    		state = this.spreadBlocks.get(0).getBlockState();
//	    		return state.getBlock().canSustainPlant(state, world, pos, direction, plantable);
//	    	} else {
//		        IBlockState plant = plantable.getPlant(world, pos.offset(direction));
//		        EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));
//		        
//		    	switch (plantType) {
//			    	case Desert: 
//			    		return this.getBlockMaterial() == Material.SAND;
//			        case Cave:   
//			        	return true;
//			        case Plains: 
//			        	return this.getBlockMaterial() == Material.GRASS || this.getBlockMaterial() == Material.GROUND;
//			        case Water:  
//			        	return state.getMaterial() == Material.WATER && state.getValue(BlockLiquid.LEVEL) == 0;
//			        case Beach:
//			            boolean isBeach = this.getBlockMaterial() == Material.GRASS || this.getBlockMaterial() == Material.GROUND || this.getBlockMaterial() == Material.SAND;
//			            boolean hasWater = (world.getBlockState(pos.east()).getMaterial() == Material.WATER ||
//			                                world.getBlockState(pos.west()).getMaterial() == Material.WATER ||
//			                                world.getBlockState(pos.north()).getMaterial() == Material.WATER ||
//			                                world.getBlockState(pos.south()).getMaterial() == Material.WATER);
//			            return isBeach && hasWater;
//			        default:    
//			        	return false;
//		    	}
//	    	}
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
//			IBlockAddedModifiableBoundingBox.Serializer.serialize(json, blockAdded);
//			
//			if (!blockAdded.spreadBlocks.isEmpty()) {
//				JsonArray blocksArray = new JsonArray();
//				
//				for (BlockStateInfo info : blockAdded.spreadBlocks) {
//					blocksArray.add(BlockStateInfo.Serializer.serialize(info));
//				}
//				
//				json.add("spread_blocks", blocksArray);
//			}
//			
//			if (blockAdded.pathBlock != null) {
//				json.add("path_block", BlockStateInfo.Serializer.serialize(blockAdded.pathBlock));
//			}
//			
//			if (!blockAdded.bonemealBlocks.isEmpty()) {
//				JsonArray blocksArray = new JsonArray();
//				
//				for (IBlockState state : blockAdded.bonemealBlocks) {
//					blocksArray.add(OtherSerializers.BlockStateSerializer.serialize(state));
//				}
//				
//				json.add("bonemeal_blocks", blocksArray);
//			}
//			
//			if (blockAdded.bonemealLootTable != null) {
//				json.addProperty("bonemeal_loot_table", blockAdded.bonemealLootTable.toString());
//			}
//			
//			if (!blockAdded.vanillaBonemeal) {
//				json.addProperty("vanilla_bonemeal", false);
//			}
//			
//			if (!blockAdded.allowBonemeal) {
//				json.addProperty("allow_bonemeal", false);
//			}
//			
//			if (!blockAdded.allowSnowy) {
//				json.addProperty("allow_snowy", false);
//			}
//			
//			if (!blockAdded.allowHoeing) {
//				json.addProperty("allow_hoeing", false);
//			}
//			
//			if (blockAdded.spreadChance < 1) {
//				json.addProperty("spread_chance", blockAdded.spreadChance);
//			}
//			
//			if (blockAdded.minLight != 4) {
//				json.addProperty("min_light", 4);
//			}
//			
//			if (blockAdded.maxLight != 15) {
//				json.addProperty("max_light", 15);
//			}
//			
//			if (blockAdded.minSpreadLight != 9) {
//				json.addProperty("min_spread_light", 9);
//			}
//			
//			if (blockAdded.maxSpreadLight != 15) {
//				json.addProperty("max_spread_light", 15);
//			}
//			
//			return json;
//		}
//		
//		@Override
//		public BlockAddedGrass deserialize(JsonObject json, JsonDeserializationContext context) {
//			BlockAddedGrass blockAdded = new BlockAddedGrass();
//			
//			super.deserializeDefaults(json, context, blockAdded);
//			IBlockAddedModifiableBoundingBox.Serializer.deserialize(json, blockAdded);
//			
//			if (json.has("spread_blocks")) {
//				JsonArray blocksArray = JsonUtils.getJsonArray(json, "spread_blocks");
//				List<BlockStateInfo> spreadBlocks = new ArrayList<>();
//				
//				blocksArray.forEach(jsonElement -> {
//					if (!jsonElement.isJsonObject()) {
//						throw new JsonSyntaxException("Expected 'member of spread_blocks' to be a json object, was " + String.valueOf(jsonElement));
//					}
//					spreadBlocks.add(BlockStateInfo.Serializer.deserialize(jsonElement.getAsJsonObject()));
//				});
//				
//				blockAdded.spreadBlocks = spreadBlocks;
//			}
//			
//			if (json.has("path_block")) {
//				blockAdded.pathBlock = BlockStateInfo.Serializer.deserialize(JsonUtils.getJsonObject(json, "path_block"));
//			}
//			
//			if (json.has("bonemeal_blocks")) {
//				JsonArray blocksArray = JsonUtils.getJsonArray(json, "bonemeal_blocks");
//				List<IBlockState> bonemealBlocks = new ArrayList<>();
//				
//				blocksArray.forEach(jsonElement -> {
//					if (!jsonElement.isJsonObject()) {
//						throw new JsonSyntaxException("Expected 'member of bonemeal_blocks' to be a json object, was " + String.valueOf(jsonElement));
//					}
//					bonemealBlocks.add(OtherSerializers.BlockStateSerializer.deserialize(jsonElement.getAsJsonObject(), "member of bonemeal_blocks"));
//				});
//				
//				blockAdded.bonemealBlocks = bonemealBlocks;
//			}
//			
//			if (json.has("bonemeal_loot_table")) {
//				blockAdded.bonemealLootTable = new ResourceLocation(JsonUtils.getString(json, "bonemeal_loot_table"));
//			}
//			
//			blockAdded.vanillaBonemeal = JsonUtils.getBoolean(json, "vanilla_bonemeal", true);
//			blockAdded.allowSnowy = JsonUtils.getBoolean(json, "allow_snowy", true);
//			blockAdded.allowHoeing = JsonUtils.getBoolean(json, "allow_hoeing", true);
//			blockAdded.spreadChance = JsonUtils.getFloat(json, "spread_chance", 1);
//			blockAdded.minLight = JsonUtils.getInt(json, "min_light", 4);
//			blockAdded.maxLight = JsonUtils.getInt(json, "max_light", 15);
//			blockAdded.minSpreadLight = JsonUtils.getInt(json, "min_spread_light", 9);
//			blockAdded.maxSpreadLight = JsonUtils.getInt(json, "max_spread_light", 15);
//			
//			return blockAdded;
//		}
//    }
//
//}
