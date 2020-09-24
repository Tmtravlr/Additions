package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Multitool Item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017
 */
public class ItemAddedMultiTool extends ItemTool implements IItemAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "multitool");

	public static final Item.ToolMaterial MULTI_MATERIAL = EnumHelper.addToolMaterial("ADDITIONS_MULTITOOL", 0, 1, 1.0F, 0.0F, 1);
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public boolean isBeaconPayment = false;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
	
	public int enchantability = 1;
	public int harvestLevel = 0;
	public int harvestRadiusWidth = 0;
	public int harvestDepth = 0;
	public Set<String> toolClasses = new HashSet<>();
	public IngredientOreNBT repairStacks = IngredientOreNBT.EMPTY;
	
	//For faster access...
	private boolean isSword;
	private boolean isPick;
	private boolean isAxe;
	private boolean isShovel;
	private boolean isHoe;
	private boolean isClub;
	private boolean isShears;
	private boolean isFlintAndSteel;
	
	private boolean multibreaking = false;
	
	public ItemAddedMultiTool() {
		super(MULTI_MATERIAL, Collections.EMPTY_SET);
	}
	
	public void setToolClasses(Collection<String> toolClasses) {
		this.isSword = this.isPick = this.isAxe = this.isShovel = this.isHoe = this.isClub = this.isShears = this.isFlintAndSteel = false;
		
		this.toolClasses = new HashSet<>(toolClasses);
		
		this.isSword = toolClasses.contains("sword");
		this.isPick = toolClasses.contains("pickaxe");
		this.isAxe = toolClasses.contains("axe");
		this.isShovel = toolClasses.contains("shovel");
		this.isHoe = toolClasses.contains("hoe");
		this.isClub = toolClasses.contains("club");
		this.isShears = toolClasses.contains("shears");
		this.isFlintAndSteel = toolClasses.contains("firestarter");
	}
	
	public void setEfficiency(float efficiency) {
		this.efficiency = efficiency;
	}
	
	public void setHarvestLevel(int level) {
        this.setHarvestLevel("", level);
    }
	
	@Override
	public void setHarvestLevel(String toolClass, int level) {
        this.harvestLevel = level;
    }
	
	@Override
	public void setTooltip(List<String> infoToAdd) {
		this.extraTooltip = infoToAdd;
	}
	
	@Override
	public void setOreDict(List<String> oreDict) {
		this.oreDictEntries = oreDict;
	}
	
	@Override
	public void setAlwaysShines(boolean alwaysShines) {
		this.shines = alwaysShines;
	}
	
	@Override
	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}
	
	@Override
	public void setIsBeaconPayment(boolean isBeaconPayment) {
		this.isBeaconPayment = isBeaconPayment;
	}
	
	@Override
	public void setDisplayName(String name) {
		this.displayName = name;
	}

	@Override
	public void setAttributeModifiers(Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifierList) {
		this.attributeModifiers = attributeModifierList;
	}
	
	public float getEfficiency() {
		return this.efficiency;
	}

	@Override
    public Set<String> getToolClasses(ItemStack stack) {
        return toolClasses;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
    	return this.toolClasses.contains(toolClass) ? this.harvestLevel : -1;
    }

	@Override
	public List<String> getTooltip() {
		return this.extraTooltip;
	}

	@Override
	public List<String> getOreDict() {
		return this.oreDictEntries;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public boolean getAlwaysShines() {
		return this.shines;
	}
	
	@Override
	public int getBurnTime() {
		return this.burnTime;
	}
	
	@Override
	public boolean getIsBeaconPayment() {
		return this.isBeaconPayment;
	}

	@Override
	public Multimap<EntityEquipmentSlot, AttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
	}

    @Override
    public int getItemEnchantability() {
        return enchantability;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return shines ? true : super.hasEffect(stack);
    }
	
	@Override
	public int getItemBurnTime(ItemStack stack) {
		return this.burnTime;
	}
	
	@Override
	public boolean isBeaconPayment(ItemStack stack) {
        return this.isBeaconPayment;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
    	for (String line : extraTooltip) {
    		if(I18n.canTranslate(line)) {
    			line = I18n.translateToLocal(line);
    		}
    		tooltip.add(line);
    	}
    }
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (I18n.canTranslate(displayName)) {
			return (I18n.translateToLocal(displayName)).trim();
		} else {
			return displayName;
		}
    }
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> modifiersForSlot = HashMultimap.create();
		
		if (this.attributeModifiers.containsKey(slot)) {
			for (AttributeModifier modifier : this.attributeModifiers.get(slot)) {
				modifiersForSlot.put(modifier.getName(), modifier);
			}
		}
		
		return modifiersForSlot;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack materialStack) {
		return this.repairStacks.itemMatches(materialStack, false, true);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		float strength = super.getDestroySpeed(stack, state);
		
		if (isShears) {
			strength = Math.max(Items.SHEARS.getDestroySpeed(stack, state), strength);
		}
		
		if (isSword) {
			strength = Math.max(Items.WOODEN_SWORD.getDestroySpeed(stack, state), strength);
		}
		
		Material material = state.getMaterial();
		
		if (isPick && (material == Material.IRON || material == Material.ANVIL || material == Material.ROCK)) {
			strength = Math.max(this.efficiency, strength);
		}
		
		if (isAxe && (material == Material.WOOD || material == Material.PLANTS || material == Material.VINE)) {
			strength = Math.max(this.efficiency, strength);
		}
		
		for (String toolClass : this.toolClasses) {
			if (state.getBlock().isToolEffective(toolClass, state)) {
				strength = Math.max(this.efficiency, strength);
			}
		}
		
		return strength;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState state) {
		
		if (this.harvestLevel < state.getBlock().getHarvestLevel(state)) {
			return false;
		}
		
		Material material = state.getMaterial();
		
		if(isShears && (material == Material.CLOTH || Items.SHEARS.canHarvestBlock(state) || (state.getBlock() instanceof IShearable))) {
			return true;
		}
		
		if(isSword && Items.WOODEN_SWORD.canHarvestBlock(state)) {
			return true;
		}
		
		if (isPick && (material == Material.IRON || material == Material.ANVIL || material == Material.ROCK)) {
			return true;
		}
		
		if (isAxe && (material == Material.WOOD || material == Material.PLANTS || material == Material.VINE)) {
			return true;
		}

		for (String toolClass : this.toolClasses) {
			if (state.getBlock().isToolEffective(toolClass, state)) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		
		if ((isSword || isClub) && Items.WOODEN_SWORD.canApplyAtEnchantingTable(new ItemStack(Items.WOODEN_SWORD), enchantment)) {
			return true;
		}
		
		if (isPick && Items.WOODEN_PICKAXE.canApplyAtEnchantingTable(new ItemStack(Items.WOODEN_PICKAXE), enchantment)) {
			return true;
		}
		
		if (isAxe && Items.WOODEN_AXE.canApplyAtEnchantingTable(new ItemStack(Items.WOODEN_AXE), enchantment)) {
			return true;
		}
		
		if (isShovel && Items.WOODEN_SHOVEL.canApplyAtEnchantingTable(new ItemStack(Items.WOODEN_SHOVEL), enchantment)) {
			return true;
		}
		
		if (isHoe && Items.WOODEN_HOE.canApplyAtEnchantingTable(new ItemStack(Items.WOODEN_HOE), enchantment)) {
			return true;
		}
		
		if (isShears && Items.SHEARS.canApplyAtEnchantingTable(new ItemStack(Items.SHEARS), enchantment)) {
			return true;
		}
		
		if (isFlintAndSteel && Items.FLINT_AND_STEEL.canApplyAtEnchantingTable(new ItemStack(Items.FLINT_AND_STEEL), enchantment)) {
			return true;
		}
		
        return false;
    }
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (isSword || isHoe || isClub) {
			stack.damageItem(1, attacker);
			return true;
		}

		if (isPick || isShovel || isAxe) {
			stack.damageItem(2, attacker);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		
		if (isShears) {
			return Items.SHEARS.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}
		
		if (isPick || isShovel || isAxe) {
			return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}
		
		if (isSword || isClub) {
			return Items.WOODEN_SWORD.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}

		return true;
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
		//System.out.println("Multibreaking? " + this.multibreaking + ", Pos: " + pos);
		
		if (!player.world.isRemote && player instanceof EntityPlayerMP && !this.multibreaking) {
			this.multibreaking = true;
			this.multiHarvestBlocks(stack, pos, (EntityPlayerMP) player);
			this.multibreaking = false;
		}
		
		if (this.isShears) {
			return Items.SHEARS.onBlockStartBreak(stack, pos, player);
		} else {
			return false;
		}
    }
	
	private void multiHarvestBlocks(ItemStack stack, BlockPos pos, EntityPlayerMP player) {
		if (player.isSneaking()) {
			//Cancel multi-break on sneaking
			return;
		}
		
		if (this.harvestRadiusWidth == 0 && this.harvestDepth == 0) {
			return;
		}
		
		World world = player.world;
		IBlockState state = world.getBlockState(pos);

		if (!canHarvestBlock(state, stack)) {
			return;
		}
		
		RayTraceResult traceResult = ForgeHooks.rayTraceEyes(player, getBlockReachDistance(player) + 1);

		if (traceResult == null) {
			return;
		}
		
		int offsetXStart = 0;
		int offsetYStart = 0;
		int offsetZStart = 0;
		int offsetXEnd = 0;
		int offsetYEnd = 0;
		int offsetZEnd = 0;

		switch (traceResult.sideHit) {
			case DOWN:
				offsetXStart = -this.harvestRadiusWidth;
				offsetZStart = -this.harvestRadiusWidth;
				offsetXEnd = this.harvestRadiusWidth;
				offsetZEnd = this.harvestRadiusWidth;
				
				offsetYEnd = this.harvestDepth;
				break;
			case UP:
				offsetXStart = -this.harvestRadiusWidth;
				offsetZStart = -this.harvestRadiusWidth;
				offsetXEnd = this.harvestRadiusWidth;
				offsetZEnd = this.harvestRadiusWidth;
				
				offsetYStart = -this.harvestDepth;
				break;
			case NORTH:
				offsetXStart = -this.harvestRadiusWidth;
				offsetYStart = -this.harvestRadiusWidth;
				offsetXEnd = this.harvestRadiusWidth;
				offsetYEnd = this.harvestRadiusWidth;
				
				offsetZEnd = this.harvestDepth;
				break;
			case SOUTH:
				offsetXStart = -this.harvestRadiusWidth;
				offsetYStart = -this.harvestRadiusWidth;
				offsetXEnd = this.harvestRadiusWidth;
				offsetYEnd = this.harvestRadiusWidth;
				
				offsetZStart = -this.harvestDepth;
				break;
			case WEST:
				offsetZStart = -this.harvestRadiusWidth;
				offsetYStart = -this.harvestRadiusWidth;
				offsetZEnd = this.harvestRadiusWidth;
				offsetYEnd = this.harvestRadiusWidth;
				
				offsetXEnd = this.harvestDepth;
				break;
			case EAST:
				offsetZStart = -this.harvestRadiusWidth;
				offsetYStart = -this.harvestRadiusWidth;
				offsetZEnd = this.harvestRadiusWidth;
				offsetYEnd = this.harvestRadiusWidth;
				
				offsetXStart = -this.harvestDepth;
				break;
		}
		
		int numHarvested = 0;
		BlockPos currentPos;
		IBlockState currentState;
		float originalHardness = state.getBlockHardness(world, pos);
		float currentHardness;
		
		for (int x = pos.getX() + offsetXStart; x <= pos.getX() + offsetXEnd; x++) {
			for (int y = pos.getY() + offsetYStart; y <= pos.getY() + offsetYEnd; y++) {
				for (int z = pos.getZ() + offsetZStart; z <= pos.getZ() + offsetZEnd; z++) {
					if (x == pos.getX() && y == pos.getY() && z == pos.getZ()) {
						continue;
					}
					
					currentPos = new BlockPos(x, y, z);
					currentState = world.getBlockState(currentPos);
					currentHardness = currentState.getBlockHardness(world, currentPos);
					
					if (currentHardness >= 0 && currentHardness - originalHardness <= 5F) {
						if (harvestBlock(world, currentPos, player, stack)) {
							numHarvested++;
						}
					}
				}
			}
		}
		
		if (numHarvested > 0 && !player.isCreative()) {
			stack.damageItem(numHarvested, player);
		}
	}
	
	private boolean harvestBlock(World world, BlockPos pos, EntityPlayerMP player, ItemStack stack) {

		if (world.isAirBlock(pos)) {
			return false;
		}
		
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if (!this.canHarvestBlock(state, player.getHeldItemMainhand())) {
			return false;
		}
		
		if (!ForgeHooks.canHarvestBlock(block, player, world, pos)) {
			return false;
		}
		
		//Break the block
		return player.interactionManager.tryHarvestBlock(pos);
	}
	
	public static double getBlockReachDistance(EntityPlayer player) {

		return player.world.isRemote ? getBlockReachDistanceClient() : player instanceof EntityPlayerMP ? getBlockReachDistanceServer((EntityPlayerMP) player) : 5D;
	}

	private static double getBlockReachDistanceServer(EntityPlayerMP player) {

		return player.interactionManager.getBlockReachDistance();
	}

	@SideOnly (Side.CLIENT)
	private static double getBlockReachDistanceClient() {

		return Minecraft.getMinecraft().playerController.getBlockReachDistance();
	}
	
	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
        return isAxe || isClub;
    }

	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		EnumActionResult result;
		
		if (isShovel) {
			result = Items.WOODEN_SHOVEL.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
			if (result != EnumActionResult.PASS) {
				return result;
			}
		}
		
		if (isHoe) {
			result = Items.WOODEN_HOE.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
			if (result != EnumActionResult.PASS) {
				return result;
			}
		}
		
		if (isFlintAndSteel) {
			IBlockState blockState = world.getBlockState(pos);
			
			if (blockState.getBlock() instanceof BlockTNT) {
				if (!world.isRemote) {
					((BlockTNT) blockState.getBlock()).explode(world, pos, blockState.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)), player);
		            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
					
					player.getHeldItem(hand).damageItem(1, player);
				}
				
				return EnumActionResult.SUCCESS;
			} else {
				result = Items.FLINT_AND_STEEL.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
				if (result != EnumActionResult.PASS) {
					return result;
				}
			}
		}
		
        return EnumActionResult.PASS;
    }
	
	@Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		
		if (isShears && Items.SHEARS.itemInteractionForEntity(itemstack, player, entity, hand)) {
			return true;
		}
		
		if (isFlintAndSteel && entity instanceof EntityCreeper) {
			entity.world.playSound(player, entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, entity.getSoundCategory(), 1.0F, entity.getRNG().nextFloat() * 0.4F + 0.8F);
            player.swingArm(hand);

            if (!entity.world.isRemote)  {
                ((EntityCreeper)entity).ignite();
                itemstack.damageItem(1, player);
            }
            
            return true;
		}
		
		return false;
	}
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedMultiTool> {
		
		public Serializer() {
			super(TYPE, ItemAddedMultiTool.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedMultiTool itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			json.addProperty("durability", itemAdded.getMaxDamage());
			json.addProperty("harvest_level", itemAdded.harvestLevel);
			json.addProperty("harvest_radius_width", itemAdded.harvestRadiusWidth);
			json.addProperty("harvest_depth", itemAdded.harvestDepth);
			json.addProperty("efficiency", itemAdded.efficiency);
			json.addProperty("enchantability", itemAdded.enchantability);
			json.add("repair_stacks", IngredientOreNBT.Serializer.serialize(itemAdded.repairStacks));
			json.add("tool_classes", OtherSerializers.StringListSerializer.serialize(itemAdded.toolClasses));
			
			return json;
		}
		
		@Override
		public ItemAddedMultiTool deserialize(JsonObject json, JsonDeserializationContext context) {
			
			ItemAddedMultiTool itemAdded = new ItemAddedMultiTool();
			
			itemAdded.setMaxDamage(JsonUtils.getInt(json, "durability"));
			itemAdded.harvestLevel = JsonUtils.getInt(json, "harvest_level");
			itemAdded.harvestRadiusWidth = JsonUtils.getInt(json, "harvest_radius_width");
			itemAdded.harvestDepth = JsonUtils.getInt(json, "harvest_depth");
			itemAdded.efficiency = JsonUtils.getFloat(json, "efficiency");
			itemAdded.enchantability = JsonUtils.getInt(json, "enchantability");
			
			if (json.has("tool_classes")) {
				itemAdded.setToolClasses(OtherSerializers.StringListSerializer.deserialize(json.get("tool_classes"), "tool_classes"));
			}
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedMultiTool itemAdded) {
			
			itemAdded.repairStacks = IngredientOreNBT.Serializer.deserialize(JsonUtils.getJsonObject(json, "repair_stacks"));
			
			postDeserializeDefaults(json, itemAdded);
		}
    }

}
