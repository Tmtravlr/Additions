package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.effects.EffectInfo;
import com.tmtravlr.additions.effects.EffectInfoPotion;

/**
 * Edible Item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class ItemAddedFood extends ItemFood implements IItemAdded {
	
	public static final String DESCRIPTION = "item.food.description";
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "food");
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	private boolean alwaysEdible = false;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
	public int eatTime = 32;
	
	public ItemAddedFood(int hunger, float saturation, boolean wolvesEat, boolean alwaysEdible, int eatTime) {
		super(hunger, saturation, wolvesEat);
		this.eatTime = eatTime;
		if(alwaysEdible) {
			this.alwaysEdible = true;
			this.setAlwaysEdible();
		}
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
	public void setDisplayName(String name) {
		this.displayName = name;
	}

	@Override
	public void setAttributeModifiers(Multimap<EntityEquipmentSlot, AttributeModifier> attributeModiferList) {
		this.attributeModifiers = attributeModiferList;
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
	public Multimap<EntityEquipmentSlot, AttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return shines ? true : super.hasEffect(stack);
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
		
		if(this.attributeModifiers.containsKey(slot)) {
			for(AttributeModifier modifier : this.attributeModifiers.get(slot)) {
				modifiersForSlot.put(modifier.getName(), modifier);
			}
		}
		
		return modifiersForSlot;
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		//TODO Apply effects
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
	@Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		ItemStack finishedStack = super.onItemUseFinish(stack, worldIn, entityLiving);
		
		if (this.getContainerItem() != null) {
			if (finishedStack.isEmpty()) {
				finishedStack = new ItemStack(this.getContainerItem());
			
			} else if (entityLiving instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityLiving;
				
				if (!player.capabilities.isCreativeMode) {
					player.addItemStackToInventory(new ItemStack(this.getContainerItem()));
				}
			}
		}
		
		return finishedStack;
    }
	
	/**
     * How long it takes to use or consume an item
     */
	@Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return eatTime;
    }
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedFood> {
		
		public Serializer() {
			super(TYPE, ItemAddedFood.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedFood itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			json.addProperty("hunger", itemAdded.getHealAmount(new ItemStack(itemAdded)));
			json.addProperty("saturation", itemAdded.getSaturationModifier(new ItemStack(itemAdded)));
			if (itemAdded.isWolfsFavoriteMeat()) {
				json.addProperty("wolves_eat", true);
			}
			if (itemAdded.alwaysEdible) {
				json.addProperty("always_edible", true);
			}
			if (itemAdded.eatTime != 32) {
				json.addProperty("eat_time", itemAdded.eatTime);
			}
			
			return json;
		}
		
		@Override
		public ItemAddedFood deserialize(JsonObject json, JsonDeserializationContext context) {
			
			int hunger = JsonUtils.getInt(json, "hunger");
			float saturation = JsonUtils.getFloat(json, "saturation");
			boolean wolvesEat = JsonUtils.getBoolean(json, "wolves_eat", false);
			boolean alwaysEdible = JsonUtils.getBoolean(json, "always_edible", false);
			int eatTime = JsonUtils.getInt(json, "eat_time", 32);
			
			ItemAddedFood itemAdded = new ItemAddedFood(hunger, saturation, wolvesEat, alwaysEdible, eatTime);
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
    }

}
