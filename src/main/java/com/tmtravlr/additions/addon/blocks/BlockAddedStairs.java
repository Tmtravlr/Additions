package com.tmtravlr.additions.addon.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.materials.BlockMaterialManager;
import com.tmtravlr.additions.addon.items.blocks.IItemAddedBlock;
import com.tmtravlr.lootoverhaul.loot.LootContextExtendedBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
 * @since May 2019 
 */
public class BlockAddedStairs extends BlockStairs implements IBlockAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "stairs");
	
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

	public BlockAddedStairs() {
		super(Blocks.AIR.getDefaultState());
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
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return this.lightOpacity >= 15 ? BlockRenderLayer.SOLID : BlockRenderLayer.TRANSLUCENT;
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
        return new BlockStateContainer(this, new IProperty[] {BlockLiquid.LEVEL, FACING, HALF, SHAPE});
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {}

    @Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {}

    @Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {}

    @Override
	@SideOnly(Side.CLIENT)
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
    	int i = source.getCombinedLight(pos, state.getLightValue(source, pos));

        if (i == 0 && state.getBlock() instanceof BlockSlab)
        {
            pos = pos.down();
            state = source.getBlockState(pos);
            return source.getCombinedLight(pos, state.getLightValue(source, pos));
        }
        else
        {
            return i;
        }
    }

    @Override
	public float getExplosionResistance(Entity exploder) {
        return this.blockResistance / 5.0F;
    }

    @Override
	public int tickRate(World worldIn) {
        return 10;
    }

    @Override
	public Vec3d modifyAcceleration(World world, BlockPos pos, Entity entity, Vec3d motion) {
        return motion;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
    	return state.getBoundingBox(world, pos).offset(pos);
    }

    @Override
	public boolean isCollidable() {
        return true;
    }

    @Override
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
    	return this.isCollidable();
    }

    @Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
    	return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }

    @Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {}

    @Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
    	if (this.hasTileEntity(state)) {
            world.removeTileEntity(pos);
        }
    }

    @Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {}

    @Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
	public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {}
	
	public static class Serializer extends IBlockAdded.Serializer<BlockAddedStairs> {
		
		public Serializer() {
			super(TYPE, BlockAddedStairs.class);
		}
		
		@Override
		public BlockAddedStairs deserialize(JsonObject json, JsonDeserializationContext context) {
			BlockAddedStairs blockAdded = new BlockAddedStairs();
			super.deserializeDefaults(json, context, blockAdded);
			return blockAdded;
		}
    }

}
