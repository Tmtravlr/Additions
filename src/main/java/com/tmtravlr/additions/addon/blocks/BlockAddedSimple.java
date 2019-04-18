package com.tmtravlr.additions.addon.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.materials.BlockMaterialManager;
import com.tmtravlr.additions.addon.items.blocks.IItemAddedBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Basic Block
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018 
 */
public class BlockAddedSimple extends Block implements IBlockAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "simple");
	
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
	private int xpDroppedMin = 0;
	private int xpDroppedMax = 0;
	private boolean hasCollisionBox = true;
	private boolean sameCollisionBoundingBox = true;
	private float boundingBoxMinX = 0;
	private float boundingBoxMinY = 0;
	private float boundingBoxMinZ = 0;
	private float boundingBoxMaxX = 1;
	private float boundingBoxMaxY = 1;
	private float boundingBoxMaxZ = 1;
	private float collisionBoxMinX = 0;
	private float collisionBoxMinY = 0;
	private float collisionBoxMinZ = 0;
	private float collisionBoxMaxX = 1;
	private float collisionBoxMaxY = 1;
	private float collisionBoxMaxZ = 1;

	public BlockAddedSimple() {
		super(Material.ROCK);
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
        this.translucent = !material.blocksLight();
        this.setSoundType(BlockMaterialManager.getBlockSoundType(material));
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
	public void setXpDroppedMin(int xpDroppedMin) {
		this.xpDroppedMin = xpDroppedMin;
	}
	
	@Override
	public void setXpDroppedMax(int xpDroppedMax) {
		this.xpDroppedMax = xpDroppedMax;
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
	public void setBoundingBoxMinX(float boundingBoxMinX) {
		this.boundingBoxMinX = boundingBoxMinX;
	}
	
	@Override
	public void setBoundingBoxMinY(float boundingBoxMinY) {
		this.boundingBoxMinY = boundingBoxMinY;
	}
	
	@Override
	public void setBoundingBoxMinZ(float boundingBoxMinZ) {
		this.boundingBoxMinZ = boundingBoxMinZ;
	}
	
	@Override
	public void setBoundingBoxMaxX(float boundingBoxMaxX) {
		this.boundingBoxMaxX = boundingBoxMaxX;
	}
	
	@Override
	public void setBoundingBoxMaxY(float boundingBoxMaxY) {
		this.boundingBoxMaxY = boundingBoxMaxY;
	}
	
	@Override
	public void setBoundingBoxMaxZ(float boundingBoxMaxZ) {
		this.boundingBoxMaxZ = boundingBoxMaxZ;
	}
	
	@Override
	public void setCollisionBoxMinX(float collisionBoxMinX) {
		this.collisionBoxMinX = collisionBoxMinX;
	}
	
	@Override
	public void setCollisionBoxMinY(float collisionBoxMinY) {
		this.collisionBoxMinY = collisionBoxMinY;
	}
	
	@Override
	public void setCollisionBoxMinZ(float collisionBoxMinZ) {
		this.collisionBoxMinZ = collisionBoxMinZ;
	}
	
	@Override
	public void setCollisionBoxMaxX(float collisionBoxMaxX) {
		this.collisionBoxMaxX = collisionBoxMaxX;
	}
	
	@Override
	public void setCollisionBoxMaxY(float collisionBoxMaxY) {
		this.collisionBoxMaxY = collisionBoxMaxY;
	}
	
	@Override
	public void setCollisionBoxMaxZ(float collisionBoxMaxZ) {
		this.collisionBoxMaxZ = collisionBoxMaxZ;
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
	public int getXpDroppedMax() {
		return this.xpDroppedMax;
	}
	
	@Override
	public int getXpDroppedMin() {
		return this.xpDroppedMin;
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
	public boolean hasCollisionBox() {
		return this.hasCollisionBox;
	}
	
	@Override
	public boolean hasSameCollisionBoundingBox() {
		return this.sameCollisionBoundingBox;
	}
	
	@Override
	public float getBoundingBoxMinX() {
		return this.boundingBoxMinX;
	}
	
	@Override
	public float getBoundingBoxMinY() {
		return this.boundingBoxMinY;
	}
	
	@Override
	public float getBoundingBoxMinZ() {
		return this.boundingBoxMinZ;
	}
	
	@Override
	public float getBoundingBoxMaxX() {
		return this.boundingBoxMaxX;
	}
	
	@Override
	public float getBoundingBoxMaxY() {
		return this.boundingBoxMaxY;
	}
	
	@Override
	public float getBoundingBoxMaxZ() {
		return this.boundingBoxMaxZ;
	}
	
	@Override
	public float getCollisionBoxMinX() {
		return this.collisionBoxMinX;
	}
	
	@Override
	public float getCollisionBoxMinY() {
		return this.collisionBoxMinY;
	}
	
	@Override
	public float getCollisionBoxMinZ() {
		return this.collisionBoxMinZ;
	}
	
	@Override
	public float getCollisionBoxMaxX() {
		return this.collisionBoxMaxX;
	}
	
	@Override
	public float getCollisionBoxMaxY() {
		return this.collisionBoxMaxY;
	}
	
	@Override
	public float getCollisionBoxMaxZ() {
		return this.collisionBoxMaxZ;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
        return this.lightOpacity >= 15;
    }
	
	@Override
    public boolean isFullCube(IBlockState state) {
        return this.isOpaqueCube(state);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return this.lightOpacity >= 15 ? BlockRenderLayer.SOLID : BlockRenderLayer.TRANSLUCENT;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if(this.lightOpacity < 15) {
	        return blockAccess.getBlockState(pos.offset(side)).getBlock() != this;
    	}
    	
    	return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
	
	@Override
	public String getLocalizedName() {
		return I18n.canTranslate(this.displayName) ?  I18n.translateToLocal(this.displayName) : this.displayName;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (this.blockMapColor == null) {
			return this.blockMaterial.getMaterialMapColor();
		}
		
        return this.blockMapColor;
    }
	
    @Override
    public String getHarvestTool(IBlockState state) {
        return this.harvestTool.isEmpty() ? null : this.harvestTool;
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return this.harvestLevel;
    }

    @Override
    public boolean isToolEffective(String type, IBlockState state) {
        return this.effectiveTools.isEmpty() || this.effectiveTools.contains(type);
    }
    
    @Override
    public float getEnchantPowerBonus(World world, BlockPos pos) {
        return this.bookshelfStrength;
    }
    
    @Override
    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
        return this.beaconColorMultiplier == null || this.beaconColorMultiplier.length != 3 ? null : this.beaconColorMultiplier;
    }
    
    @Override
    public boolean isStickyBlock(IBlockState state) {
        return this.isSlime;
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (!this.isSlime || entity.isSneaking()) {
            super.onFallenUpon(world, pos, entity, fallDistance);
        } else {
            entity.fall(fallDistance, 0.0F);
        }
    }

    @Override
    public void onLanded(World world, Entity entity) {
    	if (!this.isSlime || entity.isSneaking()) {
            super.onLanded(world, entity);
        } else if (entity.motionY < 0.0D) {
            entity.motionY = -entity.motionY;

            if (!(entity instanceof EntityLivingBase)) {
                entity.motionY *= 0.8D;
            }
        }
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if (this.isSlime && Math.abs(entity.motionY) < 0.1D && !entity.isSneaking()) {
            double motionMultiplier = 0.4D + Math.abs(entity.motionY) * 0.2D;
            entity.motionX *= motionMultiplier;
            entity.motionZ *= motionMultiplier;
        }

        super.onEntityWalk(world, pos, entity);
    }
    
    @Override
    public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon) {
    	return this.isBeaconBase;
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return this.canPistonsPush ? EnumPushReaction.BLOCK : this.blockMaterial.getMobilityFlag();
    }
    
    @Override
    public CreativeTabs getCreativeTabToDisplayOn() {
    	return CreativeTabs.BUILDING_BLOCKS;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(this.boundingBoxMinX, this.boundingBoxMinY, this.boundingBoxMinZ, this.boundingBoxMaxX, this.boundingBoxMaxY, this.boundingBoxMaxZ);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    	if (!this.hasCollisionBox) {
    		return Block.NULL_AABB;
    	} else if (!this.sameCollisionBoundingBox) {
    		return blockState.getBoundingBox(worldIn, pos);
    	}
        return new AxisAlignedBB(this.boundingBoxMinX, this.boundingBoxMinY, this.boundingBoxMinZ, this.boundingBoxMaxX, this.boundingBoxMaxY, this.boundingBoxMaxZ);
    }
	
	public static class Serializer extends IBlockAdded.Serializer<BlockAddedSimple> {
		
		public Serializer() {
			super(TYPE, BlockAddedSimple.class);
		}
		
		@Override
		public BlockAddedSimple deserialize(JsonObject json, JsonDeserializationContext context) {
			BlockAddedSimple blockAdded = new BlockAddedSimple();
			super.deserializeDefaults(json, context, blockAdded);
			return blockAdded;
		}
    }

}
