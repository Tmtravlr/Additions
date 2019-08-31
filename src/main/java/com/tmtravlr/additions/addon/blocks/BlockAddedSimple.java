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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
public class BlockAddedSimple extends Block implements IBlockAdded, IBlockAddedModifiableBoundingBox {
	
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
	private Boolean droppedFromExplosions;
	private SoundEvent placeSound = null;
	private SoundEvent breakSound = null;
	private SoundEvent hitSound = null;
	private SoundEvent stepSound = null;
	private SoundEvent fallSound = null;
	private boolean hasCollisionBox = true;
	private boolean sameCollisionBoundingBox = true;
	private AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	private AxisAlignedBB collisionBox = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

	public BlockAddedSimple() {
		super(Material.ROCK);
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
        return this.lightOpacity >= 15 && this.boundingBox.minX == 0 && this.boundingBox.minY == 0 && this.boundingBox.minZ == 0 && this.boundingBox.maxX == 1 && this.boundingBox.maxY == 1 && this.boundingBox.maxZ == 1;
    }
	
	@Override
    public boolean isFullCube(IBlockState state) {
        return this.isOpaqueCube(state);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return this.lightOpacity >= 15 ? BlockRenderLayer.SOLID : BlockRenderLayer.TRANSLUCENT;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		if (!this.isOpaqueCube(state)) {
			IBlockState offsetState = world.getBlockState(pos.offset(side));
	        return !(offsetState.getBlock() == this && this.doFacesMatch(world, pos, state, offsetState, side));
    	}
    	
    	return super.shouldSideBeRendered(state, world, pos, side);
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
        return this.effectiveTools.isEmpty() && this.harvestTool.isEmpty() || this.effectiveTools.contains(type);
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
        return this.canPistonsPush ? this.blockMaterial.getMobilityFlag() : EnumPushReaction.BLOCK;
    }
    
    @Override
    public CreativeTabs getCreativeTabToDisplayOn() {
    	return CreativeTabs.BUILDING_BLOCKS;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    	if (!this.hasCollisionBox) {
    		return Block.NULL_AABB;
    	} else if (!this.sameCollisionBoundingBox) {
    		return new AxisAlignedBB(this.collisionBox.minX, this.collisionBox.minY, this.collisionBox.minZ, this.collisionBox.maxX, this.collisionBox.maxY, this.collisionBox.maxZ);
    	}
        return blockState.getBoundingBox(worldIn, pos);
    }
    
    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
    	return this.boundingBox.maxY == 1 && this.boundingBox.minX <= 0.45 && this.boundingBox.minZ <= 0.45 && this.boundingBox.maxX >= 0.55 && this.boundingBox.maxZ >= 0.55;
    }
    
    @Override
    public boolean isTopSolid(IBlockState state) {
        return state.getMaterial().isOpaque() && this.boundingBox.maxY == 1 && this.boundingBox.minX <= 0 && this.boundingBox.minZ <= 0 && this.boundingBox.maxX >= 1 && this.boundingBox.maxZ >= 1;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    	if (!this.isNormalCube(state, world, pos)) {
    		return BlockFaceShape.UNDEFINED;
    	} else {
	    	boolean solid = false;
	    	
	    	if (face == EnumFacing.UP) {
	    		solid = this.boundingBox.maxY == 1 && this.boundingBox.minX <= 0 && this.boundingBox.minZ <= 0 && this.boundingBox.maxX >= 1 && this.boundingBox.maxZ >= 1;
	    	} else if (face == EnumFacing.DOWN) {
	    		solid = this.boundingBox.minY == 0 && this.boundingBox.minX <= 0 && this.boundingBox.minZ <= 0 && this.boundingBox.maxX >= 1 && this.boundingBox.maxZ >= 1;
	    	} else if (face == EnumFacing.EAST) {
	    		solid = this.boundingBox.maxX == 1 && this.boundingBox.minY <= 0 && this.boundingBox.minZ <= 0 && this.boundingBox.maxY >= 1 && this.boundingBox.maxZ >= 1;
	    	} else if (face == EnumFacing.WEST) {
	    		solid = this.boundingBox.minX == 0 && this.boundingBox.minY <= 0 && this.boundingBox.minZ <= 0 && this.boundingBox.maxY >= 1 && this.boundingBox.maxZ >= 1;
	    	} else if (face == EnumFacing.SOUTH) {
	    		solid = this.boundingBox.maxZ == 1 && this.boundingBox.minY <= 0 && this.boundingBox.minX <= 0 && this.boundingBox.maxY >= 1 && this.boundingBox.maxX >= 1;
	    	} else if (face == EnumFacing.NORTH) {
	    		solid = this.boundingBox.minZ == 0 && this.boundingBox.minY <= 0 && this.boundingBox.minX <= 0 && this.boundingBox.maxY >= 1 && this.boundingBox.maxX >= 1;
	    	}
	    	return solid ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    	}
    }
    
    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return !this.hasCollisionBox || super.isPassable(world, pos);
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
    	int xpDropped = 0;
    	
    	if (this.xpDroppedMax > 0) {
    		Random rand = world instanceof World ? ((World)world).rand : new Random();
    		xpDropped = MathHelper.getInt(rand, this.xpDroppedMin, this.xpDroppedMax);
    	}
    	
        return xpDropped;
    }
    
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess blockAccess, BlockPos pos, IBlockState state, int fortune) {
		boolean doNormalDrops = true;
		
		if (blockAccess instanceof WorldServer) {
			WorldServer world = (WorldServer) blockAccess;
			ResourceLocation lootTableName = new ResourceLocation(this.getRegistryName().getResourceDomain(), "blocks/" + this.getRegistryName().getResourcePath());
			
			LootTable dropLootTable = world.getLootTableManager().getLootTableFromLocation(lootTableName);
			
			if (dropLootTable != LootTable.EMPTY_LOOT_TABLE) {
				doNormalDrops = false;
				EntityPlayer player = this.harvesters.get();
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
				
				NonNullList<ItemStack> separatedDrops = NonNullList.create();
				for (ItemStack stack : dropLootTable.generateLootForPools(world.rand, contextBuilder.build())) {
					if (stack != null) {
						ItemStack singleStack = stack.copy();
						singleStack.setCount(1);
						
						for (int i = 0; i < stack.getCount(); i++) {
							separatedDrops.add(singleStack);
						}
					}
				}
				
				drops.addAll(separatedDrops);
			}
		}
		
		if (doNormalDrops) {
			super.getDrops(drops, blockAccess, pos, state, fortune);
		}
	}
	
	@Override
	public boolean canDropFromExplosion(Explosion explosion) {
        return this.droppedFromExplosions == null || this.droppedFromExplosions;
    }
	
    @Override
	protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {BlockLiquid.LEVEL});
    }
    
    @Override
	public int getMetaFromState(IBlockState state) {
    	return 0;
    }
    
    protected boolean doFacesMatch(IBlockAccess world, BlockPos pos, IBlockState state, IBlockState adjacentState, EnumFacing side) {
    	AxisAlignedBB bounds = this.getBoundingBox(state, world, pos);
    	AxisAlignedBB adjacentBounds = this.getBoundingBox(adjacentState, world, pos.offset(side));
    	
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
	
	public static class Serializer extends IBlockAdded.Serializer<BlockAddedSimple> {
		
		public Serializer() {
			super(TYPE, BlockAddedSimple.class);
		}
		
		@Override
		public JsonObject serialize(BlockAddedSimple blockAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(blockAdded, context);
			
			IBlockAddedModifiableBoundingBox.Serializer.serialize(json, blockAdded);
			
			return json;
		}
		
		@Override
		public BlockAddedSimple deserialize(JsonObject json, JsonDeserializationContext context) {
			BlockAddedSimple blockAdded = new BlockAddedSimple();
			super.deserializeDefaults(json, context, blockAdded);
			IBlockAddedModifiableBoundingBox.Serializer.deserialize(json, blockAdded);
			
			return blockAdded;
		}
    }

}
