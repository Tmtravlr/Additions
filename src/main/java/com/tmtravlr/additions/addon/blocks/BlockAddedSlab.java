package com.tmtravlr.additions.addon.blocks;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.materials.BlockMaterialManager;
import com.tmtravlr.additions.addon.items.blocks.IItemAddedBlock;
import mcp.MethodsReturnNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic Block
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date May 2019
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockAddedSlab extends BlockSlab implements IBlockAdded {

	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "slab");
	
    public static final PropertyEnum<EnumAddedSlabHalf> HALF = PropertyEnum.create("half", EnumAddedSlabHalf.class);
    
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D);
	
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
	
	public boolean placeOnWalls;

	public BlockAddedSlab() {
		super(Material.ROCK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLiquid.LEVEL, 0).withProperty(HALF, EnumAddedSlabHalf.BOTTOM));
		this.setBlockMapColor(null);
		this.lightOpacity = 15;
	}
	
	@Override
	public void setItemBlock(@Nullable IItemAddedBlock itemBlock) {
		this.itemBlock = itemBlock;
	}
	
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public void setBlockMaterial(Material material) {
		ObfuscationReflectionHelper.setPrivateValue(Block.class, this, material, "field_149764_J", "blockMaterial");
        this.translucent = !material.blocksLight();
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
	public void setBlockMapColor(@Nullable MapColor mapColor) {
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
	public void setBeaconColorMultiplier(@Nullable float[] beaconColorMultiplier) {
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
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return CommonBlockMethods.getBlockLayer(this);
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
    public CreativeTabs getCreativeTabToDisplayOn() {
    	return CreativeTabs.BUILDING_BLOCKS;
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HALF, EnumAddedSlabHalf.getFromMeta(meta));
    }

    @Override
	public int getMetaFromState(IBlockState state) {
        return state.getValue(HALF).getMeta();
    }
	
    @Override
	protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockLiquid.LEVEL, HALF);
    }

	@Override
	public String getUnlocalizedName(int meta) {
		return this.getUnlocalizedName();
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public IProperty<?> getVariantProperty() {
		return null;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return null;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(HALF)) {
		case TOP:
			return AABB_TOP_HALF;
		case BOTTOM:
			return AABB_BOTTOM_HALF;
		case NORTH:
			return AABB_NORTH;
		case SOUTH:
			return AABB_SOUTH;
		case EAST:
			return AABB_EAST;
		case WEST:
			return AABB_WEST;
		default:
			return FULL_BLOCK_AABB;
		}
    }

    @Override
	public boolean isTopSolid(IBlockState state) {
    	switch (state.getValue(HALF)) {
		case TOP:
		case FULL:
			return true;
		default:
			return false;
		}
    }
    
    @Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    	EnumAddedSlabHalf half = state.getValue(HALF);
    	
        if (half == EnumAddedSlabHalf.FULL || half.getFullSide() == face) {
            return BlockFaceShape.SOLID;
        }
        
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return state.getValue(HALF) == EnumAddedSlabHalf.FULL;
	}

    @Override
	public boolean isOpaqueCube(IBlockState state) {
        return this.isFullBlock(state) && this.lightOpacity >= 15;
    }
	
	@Override
    public boolean isFullCube(IBlockState state) {
        return this.isOpaqueCube(state);
    }
	
	@Override
	public boolean getUseNeighborBrightness(IBlockState state) {
        return !this.isFullBlock(state) || this.lightOpacity < 15;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean isTranslucent(IBlockState state) {
        return this.lightOpacity < 15;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (state.isOpaqueCube()) {
        	return true;
        }
        
        if (ForgeModContainer.disableStairSlabCulling) {
        	return false;
        }

        return state.getValue(HALF).getFullSide() == face;
    }

    @Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    	EnumAddedSlabHalf half;
    	
    	if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
			half = EnumAddedSlabHalf.getFromFacing(facing.getOpposite());
    	} else {
    		if (this.placeOnWalls) {
    			half = EnumAddedSlabHalf.getFromFacing(facing.getOpposite());
    		} else if (hitY >= 0.5D) {
    			half = EnumAddedSlabHalf.TOP;
    		} else {
    			half = EnumAddedSlabHalf.BOTTOM;
    		}
    	}
    	
    	return this.getDefaultState().withProperty(HALF, half);
    }
    
    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
    	IBlockState state = world.getBlockState(pos);
    	EnumAddedSlabHalf half = state.getValue(HALF);

    	if (half != EnumAddedSlabHalf.FULL) {
    		EnumAddedSlabHalf newHalf = EnumAddedSlabHalf.BOTTOM;

    		if (half == EnumAddedSlabHalf.BOTTOM) {
    			newHalf = EnumAddedSlabHalf.TOP;
    		} else if (this.placeOnWalls) {
    			if (half == EnumAddedSlabHalf.TOP) {
    				newHalf = EnumAddedSlabHalf.NORTH;
    			} else if (half == EnumAddedSlabHalf.NORTH || half == EnumAddedSlabHalf.EAST || half == EnumAddedSlabHalf.SOUTH) {
    				if (half.getFullSide() != null) newHalf = EnumAddedSlabHalf.getFromFacing(half.getFullSide().rotateY());
    			}
    		}

    		world.setBlockState(pos, state.withProperty(HALF, newHalf));
    		return true;
    	}
    	
    	return false;
    }
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
    	EnumAddedSlabHalf half = state.getValue(HALF);
    	
    	if (half == EnumAddedSlabHalf.FULL || half == EnumAddedSlabHalf.BOTTOM || half == EnumAddedSlabHalf.TOP) {
    		return state;
    	}

		if (half.getFullSide() == null) return state;
    	
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            	return state.withProperty(HALF, EnumAddedSlabHalf.getFromFacing(half.getFullSide().rotateYCCW()));
            case CLOCKWISE_90:
            	return state.withProperty(HALF, EnumAddedSlabHalf.getFromFacing(half.getFullSide().rotateY()));
            default:
            	return state.withProperty(HALF, EnumAddedSlabHalf.getFromFacing(half.getFullSide().getOpposite()));
        }
    }

	public enum EnumAddedSlabHalf implements IStringSerializable {
        BOTTOM("bottom", EnumFacing.DOWN),
        TOP("top", EnumFacing.UP),
        NORTH("north", EnumFacing.NORTH),
        EAST("east", EnumFacing.EAST),
        SOUTH("south", EnumFacing.SOUTH),
        WEST("west", EnumFacing.WEST),
        FULL("full");

        private final String name;
        private final EnumFacing fullSide;

		EnumAddedSlabHalf(String name) {
            this(name, null);
        }
        
        EnumAddedSlabHalf(String name, @Nullable EnumFacing fullSide) {
        	this.name = name;
        	this.fullSide = fullSide;
		}

        @Override
		public String toString() {
            return this.name;
        }

        @Override
		public String getName() {
            return this.name;
        }

		@Nullable
		public EnumFacing getFullSide() {
        	return this.fullSide;
        }
        
        public int getMeta() {
        	return this.ordinal();
        }
        
        public static EnumAddedSlabHalf getFromMeta(int meta) {
        	if (meta < values().length) return values()[meta];
        	return FULL;
        }
        
        public static EnumAddedSlabHalf getFromFacing(EnumFacing facing) {
        	switch(facing) {
        	case UP:
        		return TOP;
        	case NORTH:
        		return NORTH;
        	case EAST:
        		return EAST;
        	case SOUTH:
        		return SOUTH;
        	case WEST: 
        		return WEST;
        	default:
        		return BOTTOM;
        	}
        }
    }
	
	public static class Serializer extends IBlockAdded.Serializer<BlockAddedSlab> {
		
		public Serializer() {
			super(TYPE, BlockAddedSlab.class);
		}
		
		@Override
		public JsonObject serialize(BlockAddedSlab blockAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(blockAdded, context);
			
			if (blockAdded.placeOnWalls) {
				json.addProperty("place_on_walls", true);
			}
			
			return json;
		}
		
		@Override
		public BlockAddedSlab deserialize(JsonObject json, JsonDeserializationContext context) {
			BlockAddedSlab blockAdded = new BlockAddedSlab();
			super.deserializeDefaults(json, context, blockAdded);
			
			blockAdded.placeOnWalls = JsonUtils.getBoolean(json, "place_on_walls", false);
			
			return blockAdded;
		}
    }
}
