package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;

import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Item that starts fires
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2018
 */
public class ItemAddedFirestarter extends ItemFlintAndSteel implements IItemAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "firestarter");
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public boolean isBeaconPayment = false;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
	
	public int enchantability = 1;
	public IngredientOreNBT repairStacks = IngredientOreNBT.EMPTY;
	
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
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState blockState = world.getBlockState(pos);
		
		if (blockState.getBlock() instanceof BlockTNT) {
			if (!world.isRemote) {
				((BlockTNT) blockState.getBlock()).explode(world, pos, blockState.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)), player);
	            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
				
				player.getHeldItem(hand).damageItem(1, player);
			}
			
			return EnumActionResult.SUCCESS;
		} else {
			return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
		}
    }
	
	@Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        if (entity instanceof EntityCreeper) {
			entity.world.playSound(player, entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, entity.getSoundCategory(), 1.0F, entity.getRNG().nextFloat() * 0.4F + 0.8F);
            player.swingArm(hand);
            
        	if (!player.world.isRemote) {
        		((EntityCreeper) entity).ignite();
        		itemstack.damageItem(1, player);
        	}
        	
            return true;
        }
        
        return false;
    }
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedFirestarter> {
		
		public Serializer() {
			super(TYPE, ItemAddedFirestarter.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedFirestarter itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);

			json.addProperty("durability", itemAdded.getMaxDamage());
			json.addProperty("enchantability", itemAdded.enchantability);
			json.add("repair_stacks", IngredientOreNBT.Serializer.serialize(itemAdded.repairStacks));
			
			return json;
		}
		
		@Override
		public ItemAddedFirestarter deserialize(JsonObject json, JsonDeserializationContext context) {
			ItemAddedFirestarter itemAdded = new ItemAddedFirestarter();

			itemAdded.setMaxDamage(JsonUtils.getInt(json, "durability"));
			itemAdded.enchantability = JsonUtils.getInt(json, "enchantability");
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedFirestarter itemAdded) {
			itemAdded.repairStacks = IngredientOreNBT.Serializer.deserialize(JsonUtils.getJsonObject(json, "repair_stacks"));
			
			postDeserializeDefaults(json, itemAdded);
		}
    }

}
