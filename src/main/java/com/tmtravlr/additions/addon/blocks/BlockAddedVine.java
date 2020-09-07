//package com.tmtravlr.additions.addon.blocks;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import javax.annotation.Nullable;
//
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonSerializationContext;
//import com.tmtravlr.additions.AdditionsMod;
//import com.tmtravlr.additions.addon.blocks.materials.BlockMaterialManager;
//import com.tmtravlr.additions.addon.items.blocks.IItemAddedBlock;
//import com.tmtravlr.lootoverhaul.loot.LootContextExtendedBuilder;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockLiquid;
//import net.minecraft.block.BlockVine;
//import net.minecraft.block.SoundType;
//import net.minecraft.block.material.EnumPushReaction;
//import net.minecraft.block.material.MapColor;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.enchantment.EnchantmentHelper;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.Enchantments;
//import net.minecraft.item.ItemStack;
//import net.minecraft.stats.StatList;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.JsonUtils;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.SoundEvent;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.text.translation.I18n;
//import net.minecraft.world.Explosion;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldServer;
//import net.minecraft.world.storage.loot.LootTable;
//import net.minecraftforge.event.ForgeEventFactory;
//import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
///**
// * Vine block
// * 
// * @author Tmtravlr (Rebeca Rey)
// * @date July 2019
// */
//public class BlockAddedVine extends BlockVine implements IBlockAdded {
//
//	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "vine");
//	
//	private IItemAddedBlock itemBlock = null;
//	private String displayName = "";
//	private int harvestLevel = 0;
//	private String harvestTool = "";
//	private List<String> effectiveTools = new ArrayList<>();
//	private int bookshelfStrength = 0;
//	private float[] beaconColorMultiplier = null;
//	private boolean isSlime = false;
//	private boolean isBeaconBase = false;
//	private boolean canPistonsPush = true;
//	private int xpDroppedMin = 0;
//	private int xpDroppedMax = 0;
//	private Boolean droppedFromExplosions;
//	private SoundEvent placeSound = null;
//	private SoundEvent breakSound = null;
//	private SoundEvent hitSound = null;
//	private SoundEvent stepSound = null;
//	private SoundEvent fallSound = null;
//	
//	public boolean canClimb = true;
//	public boolean canShear = true;
//
//	public BlockAddedVine() {
//		super();
//		this.setDefaultState(this.getDefaultState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(0)));
//		this.setBlockMapColor(null);
//		this.setLightOpacity(0);
//	}
//	
//	@Override
//	public void setItemBlock(IItemAddedBlock itemBlock) {
//		this.itemBlock = itemBlock;
//	}
//	
//	@Override
//	public void setDisplayName(String displayName) {
//		this.displayName = displayName;
//	}
//	
//	@Override
//	public void setBlockMaterial(Material material) {
//		ObfuscationReflectionHelper.setPrivateValue(Block.class, this, material, "field_149764_J", "blockMaterial");
//        this.translucent = !material.blocksLight();
//        this.updateSoundType();
//	}
//
//	@Override
//	public void setPlaceSound(SoundEvent sound) {
//		this.placeSound = sound;
//        this.updateSoundType();
//	}
//
//	@Override
//	public void setBreakSound(SoundEvent sound) {
//		this.breakSound = sound;
//        this.updateSoundType();
//	}
//
//	@Override
//	public void setHitSound(SoundEvent sound) {
//		this.hitSound = sound;
//        this.updateSoundType();
//	}
//
//	@Override
//	public void setStepSound(SoundEvent sound) {
//		this.stepSound = sound;
//        this.updateSoundType();
//	}
//
//	@Override
//	public void setFallSound(SoundEvent sound) {
//		this.fallSound = sound;
//        this.updateSoundType();
//	}
//	
//	private void updateSoundType() {
//		SoundType blockSoundType = BlockMaterialManager.getBlockSoundType(this.blockMaterial);
//        
//        if (blockSoundType == null) {
//        	blockSoundType = SoundType.STONE;
//        }
//        
//        if (this.placeSound != null || this.breakSound != null || this.hitSound != null || this.stepSound != null || this.fallSound != null) {
//        	blockSoundType = new SoundType(1.0F, 1.0F, 
//					this.breakSound == null ? blockSoundType.getBreakSound() : this.breakSound, 
//					this.stepSound == null ? blockSoundType.getStepSound() : this.stepSound, 
//        			this.placeSound == null ? blockSoundType.getPlaceSound() : this.placeSound, 
//					this.hitSound == null ? blockSoundType.getHitSound() : this.hitSound, 
//					this.fallSound == null ? blockSoundType.getFallSound() : this.fallSound);
//        }
//        
//        this.setSoundType(blockSoundType);
//	}
//	
//	@Override
//	public void setBlockMapColor(MapColor mapColor) {
//		ObfuscationReflectionHelper.setPrivateValue(Block.class, this, mapColor, "field_181083_K", "blockMapColor");
//	}
//
//	@Override
//	public void setHarvestLevel(int harvestLevel) {
//		this.harvestLevel = harvestLevel;
//	}
//
//	@Override
//	public void setHarvestTool(String harvestTool) {
//		this.harvestTool = harvestTool;
//	}
//
//	@Override
//	public void setEffectiveTools(List<String> effectiveTools) {
//		this.effectiveTools = effectiveTools;
//	}
//
//	@Override
//	public void setBookshelfStrength(int bookshelfStrength) {
//		this.bookshelfStrength = bookshelfStrength;
//	}
//
//	@Override
//	public void setBeaconColorMultiplier(float[] beaconColorMultiplier) {
//		this.beaconColorMultiplier = beaconColorMultiplier;
//	}
//
//	@Override
//	public void setSlipperiness(float slipperiness) {
//		this.slipperiness = slipperiness;
//	}
//
//	@Override
//	public void setIsSlime(boolean isSlime) {
//		this.isSlime = isSlime;
//	}
//
//	@Override
//	public void setIsBeaconBase(boolean isBeaconBase) {
//		this.isBeaconBase = isBeaconBase;
//	}
//
//	@Override
//	public void setCanPistonsPush(boolean canPistonsPush) {
//		this.canPistonsPush = canPistonsPush;
//	}
//	
//	@Override
//	public void setXpDroppedMin(int xpDroppedMin) {
//		this.xpDroppedMin = xpDroppedMin;
//	}
//	
//	@Override
//	public void setXpDroppedMax(int xpDroppedMax) {
//		this.xpDroppedMax = xpDroppedMax;
//	}
//	
//	@Override
//	public void setDroppedFromExplosions(Boolean droppedFromExplosions) {
//		this.droppedFromExplosions = droppedFromExplosions;
//	}
//	
//	@Override
//	public IItemAddedBlock getItemBlock() {
//		return this.itemBlock;
//	}
//	
//	@Override
//	public String getDisplayName() {
//		return this.displayName;
//	}
//	
//	@Override
//	public Material getBlockMaterial() {
//		return this.blockMaterial;
//	}
//
//	@Override
//	public SoundEvent getPlaceSound() {
//		return this.placeSound;
//	}
//
//	@Override
//	public SoundEvent getBreakSound() {
//		return this.breakSound;
//	}
//
//	@Override
//	public SoundEvent getHitSound() {
//		return this.hitSound;
//	}
//
//	@Override
//	public SoundEvent getStepSound() {
//		return this.stepSound;
//	}
//
//	@Override
//	public SoundEvent getFallSound() {
//		return this.fallSound;
//	}
//	
//	@Override
//	public MapColor getBlockMapColor() {
//		return this.blockMapColor;
//	}
//	
//	@Override
//	public int getHarvestLevel() {
//		return this.harvestLevel;
//	}
//
//	@Override
//	public String getHarvestTool() {
//		return this.harvestTool;
//	}
//
//	@Override
//	public List<String> getEffectiveTools() {
//		return this.effectiveTools;
//	}
//
//	@Override
//	public int getBookshelfStrength() {
//		return this.bookshelfStrength;
//	}
//
//	@Override
//	public float[] getBeaconColorMultiplier() {
//		return this.beaconColorMultiplier;
//	}
//
//	@Override
//	public float getSlipperiness() {
//		return this.slipperiness;
//	}
//
//	@Override
//	public boolean isSlime() {
//		return this.isSlime;
//	}
//
//	@Override
//	public boolean isBeaconBase() {
//		return this.isBeaconBase;
//	}
//
//	@Override
//	public boolean canPistonsPush() {
//		return this.canPistonsPush;
//	}
//	
//	@Override
//	public int getXpDroppedMax() {
//		return this.xpDroppedMax;
//	}
//	
//	@Override
//	public int getXpDroppedMin() {
//		return this.xpDroppedMin;
//	}
//	
//	@Override
//	public Boolean getDroppedFromExplosions() {
//		return this.droppedFromExplosions;
//	}
//	
//	@Override
//	public float getHardness() {
//		return this.blockHardness;
//	}
//	
//	@Override
//	public float getResistance() {
//		return this.blockResistance;
//	}
//	
//	@Override
//	public int getOpacity() {
//		return this.lightOpacity;
//	}
//	
//	@Override
//	public int getLightLevel() {
//		return this.lightValue;
//	}
//	
//	@Override
//	@SideOnly(Side.CLIENT)
//    public BlockRenderLayer getBlockLayer() {
//        return BlockRenderLayer.TRANSLUCENT;
//    }
//	
//	@Override
//	public String getLocalizedName() {
//		return I18n.canTranslate(this.displayName) ?  I18n.translateToLocal(this.displayName) : this.displayName;
//	}
//	
//	@Override
//	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
//		if (this.blockMapColor == null) {
//			return this.blockMaterial.getMaterialMapColor();
//		}
//		
//        return this.blockMapColor;
//    }
//	
//    @Override
//    public String getHarvestTool(IBlockState state) {
//        return this.harvestTool.isEmpty() ? null : this.harvestTool;
//    }
//
//    @Override
//    public int getHarvestLevel(IBlockState state) {
//        return this.harvestLevel;
//    }
//
//    @Override
//    public boolean isToolEffective(String type, IBlockState state) {
//        return this.effectiveTools.isEmpty() && this.harvestTool.isEmpty() || this.effectiveTools.contains(type);
//    }
//    
//    @Override
//    public float getEnchantPowerBonus(World world, BlockPos pos) {
//        return this.bookshelfStrength;
//    }
//    
//    @Override
//    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
//        return this.beaconColorMultiplier == null || this.beaconColorMultiplier.length != 3 ? null : this.beaconColorMultiplier;
//    }
//    
//    @Override
//    public boolean isStickyBlock(IBlockState state) {
//        return this.isSlime;
//    }
//
//    @Override
//    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
//        if (!this.isSlime || entity.isSneaking()) {
//            super.onFallenUpon(world, pos, entity, fallDistance);
//        } else {
//            entity.fall(fallDistance, 0.0F);
//        }
//    }
//
//    @Override
//    public void onLanded(World world, Entity entity) {
//    	if (!this.isSlime || entity.isSneaking()) {
//            super.onLanded(world, entity);
//        } else if (entity.motionY < 0.0D) {
//            entity.motionY = -entity.motionY;
//
//            if (!(entity instanceof EntityLivingBase)) {
//                entity.motionY *= 0.8D;
//            }
//        }
//    }
//
//    @Override
//    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
//        if (this.isSlime && Math.abs(entity.motionY) < 0.1D && !entity.isSneaking()) {
//            double motionMultiplier = 0.4D + Math.abs(entity.motionY) * 0.2D;
//            entity.motionX *= motionMultiplier;
//            entity.motionZ *= motionMultiplier;
//        }
//
//        super.onEntityWalk(world, pos, entity);
//    }
//    
//    @Override
//    public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon) {
//    	return this.isBeaconBase;
//    }
//    
//    @Override
//    public EnumPushReaction getMobilityFlag(IBlockState state) {
//        return this.canPistonsPush ? this.blockMaterial.getMobilityFlag() : EnumPushReaction.BLOCK;
//    }
//    
//    @Override
//    public CreativeTabs getCreativeTabToDisplayOn() {
//    	return CreativeTabs.DECORATIONS;
//    }
//    
//    @Override
//    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
//    	int xpDropped = 0;
//    	
//    	if (this.xpDroppedMax > 0) {
//    		Random rand = world instanceof World ? ((World)world).rand : new Random();
//    		xpDropped = MathHelper.getInt(rand, this.xpDroppedMin, this.xpDroppedMax);
//    	}
//    	
//        return xpDropped;
//    }
//    
//	@Override
//	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess blockAccess, BlockPos pos, IBlockState state, int fortune) {
//		boolean doNormalDrops = true;
//		
//		if (blockAccess instanceof WorldServer) {
//			WorldServer world = (WorldServer) blockAccess;
//			ResourceLocation lootTableName = new ResourceLocation(this.getRegistryName().getResourceDomain(), "blocks/" + this.getRegistryName().getResourcePath());
//			
//			LootTable dropLootTable = world.getLootTableManager().getLootTableFromLocation(lootTableName);
//			
//			if (dropLootTable != LootTable.EMPTY_LOOT_TABLE) {
//				doNormalDrops = false;
//				EntityPlayer player = this.harvesters.get();
//				TileEntity tileEntity = world.getTileEntity(pos);
//				
//				LootContextExtendedBuilder contextBuilder = new LootContextExtendedBuilder(world);
//				contextBuilder.withPosition(pos).withBrokenState(state).withFortune(fortune);
//				if (tileEntity != null) {
//					contextBuilder.withBrokenTileEntity(tileEntity);
//				}
//				if (player != null) {
//					boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0;
//					contextBuilder.withLooter(player).withSilkTouch(silkTouch).withLuck(player.getLuck());
//				}
//				
//				NonNullList<ItemStack> separatedDrops = NonNullList.create();
//				for (ItemStack stack : dropLootTable.generateLootForPools(world.rand, contextBuilder.build())) {
//					if (stack != null) {
//						ItemStack singleStack = stack.copy();
//						singleStack.setCount(1);
//						
//						for (int i = 0; i < stack.getCount(); i++) {
//							separatedDrops.add(singleStack);
//						}
//					}
//				}
//				
//				drops.addAll(separatedDrops);
//			}
//		}
//		
//		if (doNormalDrops) {
//			super.getDrops(drops, blockAccess, pos, state, fortune);
//		}
//	}
//	
//	@Override
//	public boolean canDropFromExplosion(Explosion explosion) {
//        return this.droppedFromExplosions == null || this.droppedFromExplosions;
//    }
//	
//    @Override
//	protected BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, new IProperty[] {BlockLiquid.LEVEL, UP, NORTH, EAST, SOUTH, WEST});
//    }
//    
//    @Override
//    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
//    	return worldIn.getBlockState(pos).getMaterial().isReplaceable();
//    }
//    
//    @Override 
//    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) { 
//    	return this.canClimb;
//	}
//    
//    @Override 
//    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
//    	return this.canShear;
//    }
//    
//    @Override
//	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
//        if (this.canShear) {
//            super.harvestBlock(world, player, pos, state, te, stack);
//        } else {
//        	player.addStat(StatList.getBlockStats(this));
//            player.addExhaustion(0.005F);
//
//            if (this.canSilkHarvest(world, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
//                List<ItemStack> items = new ArrayList<ItemStack>();
//                ItemStack itemstack = this.getSilkTouchDrop(state);
//
//                if (!itemstack.isEmpty()) {
//                    items.add(itemstack);
//                }
//
//                ForgeEventFactory.fireBlockHarvesting(items, world, pos, state, 0, 1.0f, true, player);
//                for (ItemStack item : items) {
//                    spawnAsEntity(world, pos, item);
//                }
//            } else {
//                harvesters.set(player);
//                int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
//                this.dropBlockAsItem(world, pos, state, i);
//                harvesters.set(null);
//            }
//        }
//    }
//	
//	public static class Serializer extends IBlockAdded.Serializer<BlockAddedVine> {
//		
//		public Serializer() {
//			super(TYPE, BlockAddedVine.class);
//		}
//		
//		@Override
//		public JsonObject serialize(BlockAddedVine addition, JsonSerializationContext context) {
//			JsonObject json = super.serialize(addition, context);
//			
//			if (!addition.canClimb) {
//				json.addProperty("can_climb", false);
//			}
//			
//			if (!addition.canShear) {
//				json.addProperty("can_shear", false);
//			}
//			
//			return json;
//		}
//		
//		@Override
//		public BlockAddedVine deserialize(JsonObject json, JsonDeserializationContext context) {
//			BlockAddedVine blockAdded = new BlockAddedVine();
//			super.deserializeDefaults(json, context, blockAdded);
//			
//			blockAdded.canClimb = JsonUtils.getBoolean(json, "can_climb", true);
//			blockAdded.canShear = JsonUtils.getBoolean(json, "can_shear", true);
//			
//			return blockAdded;
//		}
//    }
//
//}
