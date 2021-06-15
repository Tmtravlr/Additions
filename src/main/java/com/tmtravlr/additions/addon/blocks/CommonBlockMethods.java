package com.tmtravlr.additions.addon.blocks;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectCancelNormal;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockRandom;
import com.tmtravlr.additions.type.AdditionTypeEffect;
import com.tmtravlr.lootoverhaul.loot.LootContextExtendedBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonBlockMethods {
	
	public static boolean isOpaqueCube(IBlockAdded block, IBlockState state) {
        return !canLightPassThrough(block) && state.isFullBlock();
    }
	
	public static boolean allowLightInsideBlock(IBlockAdded block, IBlockState state) {
		return !state.isOpaqueCube();
	}
	
	public static boolean isFullCube(IBlockAdded block, IBlockState state) {
        return state.isOpaqueCube();
    }
	
	@SideOnly(Side.CLIENT)
	public static BlockRenderLayer getBlockLayer(IBlockAdded block) {
        return !canLightPassThrough(block) ? BlockRenderLayer.SOLID : block.isSemiTransparent() ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT;
    }
	
	public static String getLocalizedName(IBlockAdded block) {
		return I18n.canTranslate(block.getDisplayName()) ?  I18n.translateToLocal(block.getDisplayName()) : block.getDisplayName();
	}
	
    @SideOnly(Side.CLIENT)
	public static boolean shouldSideBeRendered(IBlockAdded block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		IBlockState offsetState = world.getBlockState(pos.offset(side));
        return !(offsetState.getBlock() == block.getAsBlock() && doFacesMatch(block, world, pos, state, offsetState, side));
    }
	
	public static boolean canLightPassThrough(IBlockAdded block) {
        return block.getOpacity() < 15;
    }
    
    public static boolean doFacesMatch(IBlockAdded block, IBlockAccess world, BlockPos pos, IBlockState state, IBlockState adjacentState, EnumFacing side) {
    	AxisAlignedBB bounds = state.getBoundingBox(world, pos);
    	AxisAlignedBB adjacentBounds = adjacentState.getBoundingBox(world, pos.offset(side));
    	
    	switch(side) {
    	case UP:
    		return bounds.maxY == 1 && adjacentBounds.minY == 0 && bounds.minX == adjacentBounds.minX && bounds.minZ == adjacentBounds.minZ && bounds.maxX == adjacentBounds.maxX && bounds.maxZ == adjacentBounds.maxZ;
    	case DOWN:
    		return bounds.minY == 0 && adjacentBounds.maxY == 1 && bounds.minX == adjacentBounds.minX && bounds.minZ == adjacentBounds.minZ && bounds.maxX == adjacentBounds.maxX && bounds.maxZ == adjacentBounds.maxZ;
    	case SOUTH:
    		return bounds.maxZ == 1 && adjacentBounds.minZ == 0 && bounds.minX == adjacentBounds.minX && bounds.minY == adjacentBounds.minY && bounds.maxX == adjacentBounds.maxX && bounds.maxY == adjacentBounds.maxY;
    	case NORTH:
    		return bounds.minZ == 0 && adjacentBounds.maxZ == 1 && bounds.minX == adjacentBounds.minX && bounds.minY == adjacentBounds.minY && bounds.maxX == adjacentBounds.maxX && bounds.maxY == adjacentBounds.maxY;
    	case EAST:
    		return bounds.maxX == 1 && adjacentBounds.minX == 0 && bounds.minY == adjacentBounds.minY && bounds.minZ == adjacentBounds.minZ && bounds.maxY == adjacentBounds.maxY && bounds.maxZ == adjacentBounds.maxZ;
    	case WEST:
    		return bounds.minX == 0 && adjacentBounds.maxX == 1 && bounds.minY == adjacentBounds.minY && bounds.minZ == adjacentBounds.minZ && bounds.maxY == adjacentBounds.maxY && bounds.maxZ == adjacentBounds.maxZ;
    	}
    	
    	return false;
    }
    
    public static MapColor getMapColor(IBlockAdded block) {
		if (block.getBlockMapColor() == null) {
			return block.getBlockMaterial().getMaterialMapColor();
		}
		
        return block.getBlockMapColor();
    }
	
    public static String getHarvestTool(IBlockAdded block) {
        return block.getHarvestTool().isEmpty() ? null : block.getHarvestTool();
    }

    public static int getHarvestLevel(IBlockAdded block) {
        return block.getHarvestLevel();
    }

    public static boolean isToolEffective(IBlockAdded block, String type) {
        return block.getEffectiveTools().isEmpty() && block.getHarvestTool().isEmpty() || block.getEffectiveTools().contains(type);
    }
    
    public static float getEnchantPowerBonus(IBlockAdded block) {
        return block.getBookshelfStrength();
    }
    
    public static float[] getBeaconColorMultiplier(IBlockAdded block) {
        return block.getBeaconColorMultiplier() == null || block.getBeaconColorMultiplier().length != 3 ? null : block.getBeaconColorMultiplier();
    }
    
    public static boolean isBeaconBase(IBlockAdded block) {
    	return block.isBeaconBase();
    }
    
    public static boolean isStickyBlock(IBlockAdded block) {
        return block.isSlime();
    }

    public static boolean onFallenUpon(IBlockAdded block, Entity entity, float fallDistance) {
        if (block.isSlime() && !entity.isSneaking()) {
            entity.fall(fallDistance, 0.0F);
            return true;
        }
        
        return false;
    }

    public static boolean onLanded(IBlockAdded block, Entity entity) {
    	if (block.isSlime() && !entity.isSneaking() && entity.motionY < 0.0D) {
            entity.motionY = -entity.motionY;

            if (!(entity instanceof EntityLivingBase)) {
                entity.motionY *= 0.8D;
            }
            
            return true;
        }
    	
    	return false;
    }

    public static void onEntityWalk(IBlockAdded block, Entity entity) {
        if (block.isSlime() && Math.abs(entity.motionY) < 0.1D && !entity.isSneaking()) {
            double motionMultiplier = 0.4D + Math.abs(entity.motionY) * 0.2D;
            entity.motionX *= motionMultiplier;
            entity.motionZ *= motionMultiplier;
        }
    }
    
    public static EnumPushReaction getMobilityFlag(IBlockAdded block) {
        return block.canPistonsPush() ? block.getBlockMaterial().getMobilityFlag() : EnumPushReaction.BLOCK;
    }
    
    public static int getExpDrop(IBlockAdded block, IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
    	int xpDropped = 0;
    	
    	if (block.getXpDroppedMax() > 0) {
    		Random rand = world instanceof World ? ((World)world).rand : new Random();
    		xpDropped = MathHelper.getInt(rand, block.getXpDroppedMin(), block.getXpDroppedMax());
    	}
    	
        return xpDropped;
    }
    
    public static boolean getDrops(IBlockAdded block, NonNullList<ItemStack> drops, IBlockAccess blockAccess, BlockPos pos, IBlockState state, int fortune) {
		boolean usedDropLootTable = false;
		
		if (blockAccess instanceof WorldServer) {
			WorldServer world = (WorldServer) blockAccess;
			ResourceLocation lootTableName = new ResourceLocation(AdditionsMod.MOD_ID, "blocks/" + block.getId());
			
			LootTable dropLootTable = world.getLootTableManager().getLootTableFromLocation(lootTableName);
			
			if (dropLootTable != LootTable.EMPTY_LOOT_TABLE) {
				usedDropLootTable = true;
				EntityPlayer player = getHarvestPlayer(block);
				TileEntity tileEntity = world.getTileEntity(pos);
				
				LootContextExtendedBuilder contextBuilder = new LootContextExtendedBuilder(world);
				contextBuilder.withPosition(pos).withBrokenState(state).withFortune(fortune);
				
				if (tileEntity != null) {
					contextBuilder.withBrokenTileEntity(tileEntity);
				}
				
				if (player != null) {
					boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0;
					contextBuilder.withLooter(player).withSilkTouch(silkTouch).withLuck(player.getLuck());
				}
				
				List<ItemStack> dropList = dropLootTable.generateLootForPools(world.rand, contextBuilder.build());
				
				NonNullList<ItemStack> separatedDrops = NonNullList.create();
				for (ItemStack stack : dropList) {
					if (stack != null) {
						ItemStack singleStack = stack.copy();
						singleStack.setCount(1);
						
						for (int i = 0; i < stack.getCount(); i++) {
							separatedDrops.add(singleStack.copy());
						}
					}
				}
				
				drops.addAll(separatedDrops);
			}
		}
		
		return usedDropLootTable;
	}
    
    public static EntityPlayer getHarvestPlayer(IBlockAdded block) {
    	EntityPlayer player = null;
    	
    	try {
	    	ThreadLocal<EntityPlayer> harvester = ReflectionHelper.getPrivateValue(Block.class, block.getAsBlock(), "harvesters");
	    	
	    	if (harvester != null) {
	    		player = harvester.get();
	    	}
    	} catch (UnableToAccessFieldException e) {
    		// Swallowing and returning null...
    	}
    	
    	return player;
    }
	
    public static boolean canDropFromExplosion(IBlockAdded block) {
        return block.getDroppedFromExplosions() == null || block.getDroppedFromExplosions();
    }
    
    public static boolean getTickRandomly(IBlockAdded block) {
        return AdditionTypeEffect.INSTANCE.getBlockAddedRandomEffects().containsKey(block);
    }
    
    public static void updateTick(IBlockAdded block, World world, BlockPos pos, IBlockState state, Random rand) {
    	if (world instanceof WorldServer && AdditionTypeEffect.INSTANCE.getBlockAddedRandomEffects().containsKey(block)) {
	    	int tickSpeed = world.getGameRules().getInt("randomTickSpeed");
	    	Map<EffectCauseBlockRandom, List<Effect>> blockEffects = AdditionTypeEffect.INSTANCE.getBlockAddedRandomEffects().get(block);
			
			if (tickSpeed > 0) {
            	TileEntity te = world.getTileEntity(pos);
				NBTTagCompound blockTag = te == null ? null : te.writeToNBT(new NBTTagCompound());
            	
            	for (Entry<EffectCauseBlockRandom, List<Effect>> entry : blockEffects.entrySet()) {
            		if (entry.getKey().applies(state, blockTag)) {
						for (Effect effect : entry.getValue()) {
							if (!(effect instanceof EffectCancelNormal)) {
								effect.affectBlock(null, world, pos);
							}
						}
					}
				}
			}
    	}
    }
    
    // Methods for blocks with a modifiable bounding box

    public static AxisAlignedBB getBoundingBox(IBlockAddedModifiableBoundingBox block, IBlockState state) {
		return block.modifyBoundingBoxForState(block.getBoundingBox(), state);
    }
    
    public static AxisAlignedBB getCollisionBoundingBox(IBlockAddedModifiableBoundingBox block, IBlockState state, IBlockAccess world, BlockPos pos) {
    	if (!block.hasCollisionBox()) {
    		return Block.NULL_AABB;
    	} else if (!block.hasSameCollisionBoundingBox()) {
    		return block.modifyBoundingBoxForState(block.getCollisionBox(), state);
    	}
        return state.getBoundingBox(world, pos);
    }
	
	public static boolean isFullBlock(IBlockAddedModifiableBoundingBox block, IBlockState state) {
		AxisAlignedBB boundingBox = block.modifyBoundingBoxForState(block.getBoundingBox(), state);
		return boundingBox.minX == 0 && boundingBox.minY == 0 && boundingBox.minZ == 0 && boundingBox.maxX == 1 && boundingBox.maxY == 1 && boundingBox.maxZ == 1;
	}
    
    public static boolean canPlaceTorchOnTop(IBlockAddedModifiableBoundingBox block, IBlockState state, IBlockAccess world, BlockPos pos) {
		AxisAlignedBB boundingBox = block.modifyBoundingBoxForState(block.getBoundingBox(), state);
    	return boundingBox.maxY == 1 && boundingBox.minX <= 0.45 && boundingBox.minZ <= 0.45 && boundingBox.maxX >= 0.55 && boundingBox.maxZ >= 0.55;
    }
    
    public static boolean isTopSolid(IBlockAddedModifiableBoundingBox block, IBlockState state) {
		AxisAlignedBB boundingBox = block.modifyBoundingBoxForState(block.getBoundingBox(), state);
        return state.getMaterial().isOpaque() && boundingBox.maxY == 1 && boundingBox.minX <= 0 && boundingBox.minZ <= 0 && boundingBox.maxX >= 1 && boundingBox.maxZ >= 1;
    }
    
    public static BlockFaceShape getBlockFaceShape(IBlockAddedModifiableBoundingBox block, IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    	BlockFaceShape shape = BlockFaceShape.UNDEFINED;
    	AxisAlignedBB boundingBox = state.getBoundingBox(world, pos);
	    	
    	if (face == EnumFacing.UP) {
    		shape = getFaceShape(boundingBox.maxY, boundingBox.minX, boundingBox.maxX, boundingBox.minZ, boundingBox.maxZ, false);
    	} else if (face == EnumFacing.DOWN) {
    		shape = getFaceShape(1 - boundingBox.minY, boundingBox.minX, boundingBox.maxX, boundingBox.minZ, boundingBox.maxZ, false);
    	} else if (face == EnumFacing.EAST) {
    		shape = getFaceShape(boundingBox.maxX, boundingBox.minZ, boundingBox.maxZ, boundingBox.minY, boundingBox.maxY, true);
    	} else if (face == EnumFacing.WEST) {
    		shape = getFaceShape(1 - boundingBox.minX, boundingBox.minZ, boundingBox.maxZ, boundingBox.minY, boundingBox.maxY, true);
    	} else if (face == EnumFacing.SOUTH) {
    		shape = getFaceShape(boundingBox.maxZ, boundingBox.minX, boundingBox.maxX, boundingBox.minY, boundingBox.maxY, true);
    	} else if (face == EnumFacing.NORTH) {
    		shape = getFaceShape(1 - boundingBox.minZ, boundingBox.minX, boundingBox.maxX, boundingBox.minY, boundingBox.maxY, true);
    	}
    	
    	return shape;
    }
    
    public static BlockFaceShape getFaceShape(double depth, double xMin, double xMax, double yMin, double yMax, boolean side) {
    	if (depth != 1) {
    		return BlockFaceShape.UNDEFINED;
    	}
    	
    	if (xMin == 0 && xMax == 1 && yMin == 0 && yMax == 1) {
    		return BlockFaceShape.SOLID;
    	}
    	
    	if (xMin >= 0.4 && xMin < 0.45 && xMax >= 0.55 && xMax < 0.6 && yMin >= 0.4 && yMin < 0.45 && yMax >= 0.55 && yMax < 0.6) {
    		return BlockFaceShape.CENTER_SMALL;
    	}
    	
    	if (xMin >= 0.3 && xMin < 0.4 && xMax >= 0.6 && xMax < 0.7 && yMin >= 0.3 && yMin < 0.4 && yMax >= 0.6 && yMax < 0.7) {
    		return BlockFaceShape.CENTER;
    	}
    	
    	if (xMin >= 0.2 && xMin < 0.3 && xMax >= 0.7 && xMax < 0.8 && yMin >= 0.2 && yMin < 0.3 && yMax >= 0.7 && yMax < 0.8) {
    		return BlockFaceShape.CENTER_BIG;
    	}
    	
    	if (side && xMin >= 0.4 && xMin < 0.45 && xMax >= 0.55 && xMax < 0.6 && yMin == 0 && yMax == 1) {
    		return BlockFaceShape.MIDDLE_POLE_THIN;
    	}
    	
    	if (side && xMin >= 0.3 && xMin < 0.4 && xMax >= 0.6 && xMax < 0.7 && yMin == 0 && yMax == 1) {
    		return BlockFaceShape.MIDDLE_POLE;
    	}
    	
    	if (side && xMin >= 0.2 && xMin < 0.3 && xMax >= 0.7 && xMax < 0.8 && yMin == 0 && yMax == 1) {
    		return BlockFaceShape.MIDDLE_POLE_THICK;
    	}

		return BlockFaceShape.UNDEFINED;
    }
    
    public static boolean isPassable(IBlockAddedModifiableBoundingBox block) {
        return !block.hasCollisionBox();
    }
    
    public static boolean doesSideBlockRendering(IBlockAddedModifiableBoundingBox block, IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    	return state.isOpaqueCube() && block.getAsBlock().getBlockFaceShape(world, state, pos, face) == BlockFaceShape.SOLID;
    }

}
