package com.tmtravlr.additions.addon.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.materials.BlockMaterialManager;
import com.tmtravlr.additions.addon.items.blocks.IItemAddedBlock;
import com.tmtravlr.lootoverhaul.loot.LootContextExtendedBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Basic Block
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018 
 */
public class BlockAddedLadder extends BlockLadder implements IBlockAdded, IBlockAddedModifiableBoundingBox {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "ladder");
	
	private IItemAddedBlock itemBlock = null;
	private String displayName = "";
	private int harvestLevel = 0;
	private String harvestTool = "";
	private List<String> effectiveTools = new ArrayList<>();
	private int bookshelfStrength = 0;
	private float[] beaconColorMultiplier = null;
	private boolean isSlime = false;
	private boolean isBeaconBase = false;
	private boolean canPistonsPush = true;
	private boolean semiTransparent = false;
	private int xpDroppedMin = 0;
	private int xpDroppedMax = 0;
	private Boolean droppedFromExplosions;
	private SoundEvent placeSound = null;
	private SoundEvent breakSound = null;
	private SoundEvent hitSound = null;
	private SoundEvent stepSound = null;
	private SoundEvent fallSound = null;
	private boolean hasCollisionBox = true;
	private boolean sameCollisionBoundingBox = true;
	private AxisAlignedBB boundingBox = LADDER_EAST_AABB;
	private AxisAlignedBB collisionBox = LADDER_EAST_AABB;
	
	public boolean supportBack = true;
	public boolean supportBottom = false;
	public boolean supportTop = false;
	public boolean placeDownward = false;

	public BlockAddedLadder() {
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(0)));
		this.setBlockMapColor(null);
		this.lightOpacity = 15;
	}
	
	@Override
	public void setItemBlock(IItemAddedBlock itemBlock) {
		this.itemBlock = itemBlock;
	}
	
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public void setBlockMaterial(Material material) {
		ObfuscationReflectionHelper.setPrivateValue(Block.class, this, material, "field_149764_J", "blockMaterial");
        this.updateSoundType();
	}

	@Override
	public void setPlaceSound(SoundEvent sound) {
		this.placeSound = sound;
        this.updateSoundType();
	}

	@Override
	public void setBreakSound(SoundEvent sound) {
		this.breakSound = sound;
        this.updateSoundType();
	}

	@Override
	public void setHitSound(SoundEvent sound) {
		this.hitSound = sound;
        this.updateSoundType();
	}

	@Override
	public void setStepSound(SoundEvent sound) {
		this.stepSound = sound;
        this.updateSoundType();
	}

	@Override
	public void setFallSound(SoundEvent sound) {
		this.fallSound = sound;
        this.updateSoundType();
	}
	
	private void updateSoundType() {
		SoundType blockSoundType = BlockMaterialManager.getBlockSoundType(this.blockMaterial);
        
        if (blockSoundType == null) {
        	blockSoundType = SoundType.STONE;
        }
        
        if (this.placeSound != null || this.breakSound != null || this.hitSound != null || this.stepSound != null || this.fallSound != null) {
        	blockSoundType = new SoundType(1.0F, 1.0F, 
					this.breakSound == null ? blockSoundType.getBreakSound() : this.breakSound, 
					this.stepSound == null ? blockSoundType.getStepSound() : this.stepSound, 
        			this.placeSound == null ? blockSoundType.getPlaceSound() : this.placeSound, 
					this.hitSound == null ? blockSoundType.getHitSound() : this.hitSound, 
					this.fallSound == null ? blockSoundType.getFallSound() : this.fallSound);
        }
        
        this.setSoundType(blockSoundType);
	}
	
	@Override
	public void setBlockMapColor(MapColor mapColor) {
		ObfuscationReflectionHelper.setPrivateValue(Block.class, this, mapColor, "field_181083_K", "blockMapColor");
	}

	@Override
	public void setHarvestLevel(int harvestLevel) {
		this.harvestLevel = harvestLevel;
	}

	@Override
	public void setHarvestTool(String harvestTool) {
		this.harvestTool = harvestTool;
	}

	@Override
	public void setEffectiveTools(List<String> effectiveTools) {
		this.effectiveTools = effectiveTools;
	}

	@Override
	public void setBookshelfStrength(int bookshelfStrength) {
		this.bookshelfStrength = bookshelfStrength;
	}

	@Override
	public void setBeaconColorMultiplier(float[] beaconColorMultiplier) {
		this.beaconColorMultiplier = beaconColorMultiplier;
	}

	@Override
	public void setSlipperiness(float slipperiness) {
		this.slipperiness = slipperiness;
	}

	@Override
	public void setIsSlime(boolean isSlime) {
		this.isSlime = isSlime;
	}

	@Override
	public void setIsBeaconBase(boolean isBeaconBase) {
		this.isBeaconBase = isBeaconBase;
	}

	@Override
	public void setCanPistonsPush(boolean canPistonsPush) {
		this.canPistonsPush = canPistonsPush;
	}
	
	@Override
	public void setSemiTransparent(boolean semiTransparent) {
		this.semiTransparent = semiTransparent;
	}
	
	@Override
	public void setXpDroppedMin(int xpDroppedMin) {
		this.xpDroppedMin = xpDroppedMin;
	}
	
	@Override
	public void setXpDroppedMax(int xpDroppedMax) {
		this.xpDroppedMax = xpDroppedMax;
	}
	
	@Override
	public void setDroppedFromExplosions(Boolean droppedFromExplosions) {
		this.droppedFromExplosions = droppedFromExplosions;
	}
	
	@Override
	public void setHasCollisionBox(boolean hasCollisionBox) {
		this.hasCollisionBox = hasCollisionBox;
	}
	
	@Override
	public void setHasSameCollisionBoundingBox(boolean sameCollisionBoundingBox) {
		this.sameCollisionBoundingBox = sameCollisionBoundingBox;
	}
	
	@Override
	public void setBoundingBox(AxisAlignedBB boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	@Override
	public void setCollisionBox(AxisAlignedBB collisionBox) {
		this.collisionBox = collisionBox;
	}
	
	@Override
	public IItemAddedBlock getItemBlock() {
		return this.itemBlock;
	}
	
	@Override
	public String getDisplayName() {
		return this.displayName;
	}
	
	@Override
	public Material getBlockMaterial() {
		return this.blockMaterial;
	}

	@Override
	public SoundEvent getPlaceSound() {
		return this.placeSound;
	}

	@Override
	public SoundEvent getBreakSound() {
		return this.breakSound;
	}

	@Override
	public SoundEvent getHitSound() {
		return this.hitSound;
	}

	@Override
	public SoundEvent getStepSound() {
		return this.stepSound;
	}

	@Override
	public SoundEvent getFallSound() {
		return this.fallSound;
	}
	
	@Override
	public MapColor getBlockMapColor() {
		return this.blockMapColor;
	}
	
	@Override
	public int getHarvestLevel() {
		return this.harvestLevel;
	}

	@Override
	public String getHarvestTool() {
		return this.harvestTool;
	}

	@Override
	public List<String> getEffectiveTools() {
		return this.effectiveTools;
	}

	@Override
	public int getBookshelfStrength() {
		return this.bookshelfStrength;
	}

	@Override
	public float[] getBeaconColorMultiplier() {
		return this.beaconColorMultiplier;
	}

	@Override
	public float getSlipperiness() {
		return this.slipperiness;
	}

	@Override
	public boolean isSlime() {
		return this.isSlime;
	}

	@Override
	public boolean isBeaconBase() {
		return this.isBeaconBase;
	}

	@Override
	public boolean canPistonsPush() {
		return this.canPistonsPush;
	}
	
	@Override
	public boolean isSemiTransparent() {
		return this.semiTransparent;
	}
	
	@Override
	public int getXpDroppedMax() {
		return this.xpDroppedMax;
	}
	
	@Override
	public int getXpDroppedMin() {
		return this.xpDroppedMin;
	}
	
	@Override
	public Boolean getDroppedFromExplosions() {
		return this.droppedFromExplosions;
	}
	
	@Override
	public float getHardness() {
		return this.blockHardness;
	}
	
	@Override
	public float getResistance() {
		return this.blockResistance;
	}
	
	@Override
	public int getOpacity() {
		return this.lightOpacity;
	}
	
	@Override
	public int getLightLevel() {
		return this.lightValue;
	}
	
	@Override
	public boolean hasCollisionBox() {
		return this.hasCollisionBox;
	}
	
	@Override
	public boolean hasSameCollisionBoundingBox() {
		return this.sameCollisionBoundingBox;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}
	
	@Override
	public AxisAlignedBB getCollisionBox() {
		return this.collisionBox;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
        return CommonBlockMethods.isOpaqueCube(this, state);
    }
	
	@Override
    public boolean isFullCube(IBlockState state) {
        return CommonBlockMethods.isFullCube(this, state);
    }
	
	@Override
	public boolean getUseNeighborBrightness(IBlockState state) {
        return CommonBlockMethods.allowLightInsideBlock(this, state);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean isTranslucent(IBlockState state) {
        return CommonBlockMethods.allowLightInsideBlock(this, state);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return CommonBlockMethods.getBlockLayer(this);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		if (!this.isOpaqueCube(state)) {
	        return CommonBlockMethods.shouldSideBeRendered(this, state, world, pos, side);
    	}
    	
    	return super.shouldSideBeRendered(state, world, pos, side);
    }
	
	@Override
	public String getLocalizedName() {
		return CommonBlockMethods.getLocalizedName(this);
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
		return CommonBlockMethods.getMapColor(this);
    }
	
    @Override
    public String getHarvestTool(IBlockState state) {
        return CommonBlockMethods.getHarvestTool(this);
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return CommonBlockMethods.getHarvestLevel(this);
    }

    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return CommonBlockMethods.isToolEffective(this, type);
    }
    
    @Override
    public float getEnchantPowerBonus(World world, BlockPos pos) {
        return CommonBlockMethods.getEnchantPowerBonus(this);
    }
    
    @Override
    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
        return CommonBlockMethods.getBeaconColorMultiplier(this);
    }
    
    @Override
    public boolean isStickyBlock(IBlockState state) {
        return CommonBlockMethods.isStickyBlock(this);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (!CommonBlockMethods.onFallenUpon(this, entity, fallDistance)) {
            super.onFallenUpon(world, pos, entity, fallDistance);
        }
    }

    @Override
    public void onLanded(World world, Entity entity) {
    	if (!CommonBlockMethods.onLanded(this, entity)) {
            super.onLanded(world, entity);
        }
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
	    CommonBlockMethods.onEntityWalk(this, entity);
        super.onEntityWalk(world, pos, entity);
    }
    
    @Override
    public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon) {
    	return CommonBlockMethods.isBeaconBase(this);
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return CommonBlockMethods.getMobilityFlag(this);
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
    	return CommonBlockMethods.getExpDrop(this, state, world, pos, fortune);
    }
    
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess blockAccess, BlockPos pos, IBlockState state, int fortune) {
		if (!CommonBlockMethods.getDrops(this, drops, blockAccess, pos, state, fortune)) {
			super.getDrops(drops, blockAccess, pos, state, fortune);
		}
	}
	
	@Override
	public boolean canDropFromExplosion(Explosion explosion) {
        return CommonBlockMethods.canDropFromExplosion(this);
    }
    
    @Override
    public boolean getTickRandomly() {
    	return CommonBlockMethods.getTickRandomly(this);
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    	CommonBlockMethods.updateTick(this, world, pos, state, rand);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CommonBlockMethods.getBoundingBox(this, state);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
    	return CommonBlockMethods.getCollisionBoundingBox(this, state, world, pos);
    }
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return CommonBlockMethods.isFullBlock(this, state);
	}
    
    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
    	return CommonBlockMethods.canPlaceTorchOnTop(this, state, world, pos);
    }
    
    @Override
    public boolean isTopSolid(IBlockState state) {
        return CommonBlockMethods.isTopSolid(this, state);
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    	return CommonBlockMethods.getBlockFaceShape(this, world, state, pos, face);
    }
    
    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return CommonBlockMethods.isPassable(this) || super.isPassable(world, pos);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return CommonBlockMethods.doesSideBlockRendering(this, world, state, pos, face);
    }

	@Override
	public AxisAlignedBB getDefaultBoundingBox() {
		return LADDER_EAST_AABB;
	}
	
	@Override
	public AxisAlignedBB modifyBoundingBoxForState(AxisAlignedBB original, IBlockState state) {
    	switch(state.getValue(BlockLadder.FACING)) {
    	case WEST:
    		return new AxisAlignedBB(1 - original.minX, original.minY, 1 - original.minZ, 1 - original.maxX, original.maxY, 1 - original.maxZ);
    	case SOUTH:
    		return new AxisAlignedBB(1 - original.minZ, original.minY, original.minX, 1 - original.maxZ, original.maxY, original.maxX);
    	case NORTH:
    		return new AxisAlignedBB(original.minZ, original.minY, 1 - original.minX, original.maxZ, original.maxY, 1 - original.maxX);
    	default:
    		return original;
    	}
    }
    
    @Override
    public CreativeTabs getCreativeTabToDisplayOn() {
    	return CreativeTabs.DECORATIONS;
    }
	
    @Override
	protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {BlockLadder.FACING, BlockLiquid.LEVEL});
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
        if (this.canAttachTo(world, pos.west(), EnumFacing.EAST)) {
            return true;
        } else if (this.canAttachTo(world, pos.east(), EnumFacing.WEST)) {
            return true;
        } else if (this.canAttachTo(world, pos.north(), EnumFacing.SOUTH)) {
            return true;
        } else if (this.canAttachTo(world, pos.south(), EnumFacing.NORTH)) {
            return true;
        } else if (this.canAttachTo(world, pos.up(), EnumFacing.DOWN)) {
            return true;
        } else {
            return this.canAttachTo(world, pos.down(), EnumFacing.UP);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (facing.getAxis().isHorizontal() && this.canAttachTo(world, pos.offset(facing.getOpposite()), facing)) {
            return this.getDefaultState().withProperty(FACING, facing);
        } else {
            for (EnumFacing connectFacing : EnumFacing.VALUES) {
                if (this.canAttachTo(world, pos.offset(connectFacing.getOpposite()), connectFacing)) {
                    return this.getDefaultState().withProperty(FACING, !connectFacing.getAxis().isHorizontal() ? placer.getHorizontalFacing().getOpposite() : connectFacing);
                }
            }

            return this.getDefaultState();
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    	if ((this.supportBack || this.supportTop || this.supportBottom) &&
    			!(this.supportBack && this.canAttachTo(worldIn, pos.offset(state.getValue(FACING).getOpposite()), state.getValue(FACING))) && 
    			!(this.supportTop && this.canAttachTo(worldIn, pos.up(), EnumFacing.DOWN)) &&
    			!(this.supportBottom && this.canAttachTo(worldIn, pos.down(), EnumFacing.UP))
		) {
    		this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
    	}
    }
    
    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
    	return true;
	}
    
    public boolean canAttachTo(World world, BlockPos pos, EnumFacing facing) {
    	if (!(this.supportBack || this.supportTop || this.supportBottom)) {
    		return true;
    	}
        
		if (this.supportTop) {
			IBlockState stateAbove = world.getBlockState(pos.offset(facing).up());
			
			if (stateAbove.getBlock() == this && stateAbove.getValue(FACING) == facing) {
				return true;
			}
		}
        
		if (this.supportBottom) {
			IBlockState stateBelow = world.getBlockState(pos.offset(facing).down());
			
			if (stateBelow.getBlock() == this && stateBelow.getValue(FACING) == facing) {
				return true;
			}
		}
    	
        IBlockState state = world.getBlockState(pos);
    	
    	if ((this.supportBack && facing.getAxis().isHorizontal()) || (this.supportTop && facing == EnumFacing.DOWN) || (this.supportBottom && facing == EnumFacing.UP)) {
    		return !isExceptBlockForAttachWithPiston(state.getBlock()) && state.getBlockFaceShape(world, pos, facing) == BlockFaceShape.SOLID && !state.canProvidePower();
    	}
    	
		return false;
    }
	
	public static class Serializer extends IBlockAdded.Serializer<BlockAddedLadder> {
		
		public Serializer() {
			super(TYPE, BlockAddedLadder.class);
		}
		
		@Override
		public JsonObject serialize(BlockAddedLadder blockAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(blockAdded, context);
			
			if (!blockAdded.supportBack) {
				json.addProperty("support_back", false);
			}
			
			if (blockAdded.supportTop) {
				json.addProperty("support_top", true);
			}
			
			if (blockAdded.supportBottom) {
				json.addProperty("support_bottom", true);
			}
			
			if (blockAdded.placeDownward) {
				json.addProperty("place_downward", true);
			}
			
			IBlockAddedModifiableBoundingBox.Serializer.serialize(json, blockAdded);
			
			return json;
		}
		
		@Override
		public BlockAddedLadder deserialize(JsonObject json, JsonDeserializationContext context) {
			BlockAddedLadder blockAdded = new BlockAddedLadder();
			super.deserializeDefaults(json, context, blockAdded);
			IBlockAddedModifiableBoundingBox.Serializer.deserialize(json, blockAdded);
			
			blockAdded.supportBack = JsonUtils.getBoolean(json, "support_back", true);
			blockAdded.supportTop = JsonUtils.getBoolean(json, "support_top", false);
			blockAdded.supportBottom = JsonUtils.getBoolean(json, "support_bottom", false);
			blockAdded.placeDownward = JsonUtils.getBoolean(json, "place_downward", false);
			
			return blockAdded;
		}
    }

}
