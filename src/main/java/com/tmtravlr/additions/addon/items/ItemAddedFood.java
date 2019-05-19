package com.tmtravlr.additions.addon.items;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectManager;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.type.AdditionTypeEffect;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Edible Item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class ItemAddedFood extends ItemFood implements IItemAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "food");
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
	
	public int eatTime = 32;
	public boolean isDrink = false;
	public boolean hasPotionEffects = false;
	public List<Effect> eatenEffects = new ArrayList<>();
	private int hunger = 0;
	private float saturation = 0;
	private boolean wolvesEat = false;
	private boolean alwaysEdible = false;
	
	public ItemAddedFood() {
		this(0, 0, false, false);
	}
	
	public ItemAddedFood(int hunger, float saturation, boolean wolvesEat, boolean alwaysEdible) {
		super(hunger, saturation, wolvesEat);
		this.hunger = hunger;
		this.saturation = saturation;
		this.wolvesEat = wolvesEat;
		this.alwaysEdible = alwaysEdible;
		if(alwaysEdible) {
			this.setAlwaysEdible();
		}
	}
	
	public void setFoodStats(int hunger, float saturation, boolean wolvesEat, boolean alwaysEdible) {
		if (hunger != this.hunger) {
			this.hunger = hunger;
			ObfuscationReflectionHelper.setPrivateValue(ItemFood.class, this, hunger, "field_77853_b", "healAmount");
		}
		if (saturation != this.saturation) {
			this.saturation = saturation;
			ObfuscationReflectionHelper.setPrivateValue(ItemFood.class, this, saturation, "field_77854_c", "saturationModifier");
		}
		if (wolvesEat != this.wolvesEat) {
			this.wolvesEat = wolvesEat;
			ObfuscationReflectionHelper.setPrivateValue(ItemFood.class, this, wolvesEat, "field_77856_bY", "isWolfsFavoriteMeat");
		}
		if (alwaysEdible != this.alwaysEdible) {
			this.alwaysEdible = alwaysEdible;
			if (alwaysEdible) {
				this.setAlwaysEdible();
			} else {
				ObfuscationReflectionHelper.setPrivateValue(ItemFood.class, this, wolvesEat, "field_77852_bZ", "alwaysEdible");
			}
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
	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
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
	public Multimap<EntityEquipmentSlot, AttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
        return this.isDrink ? EnumAction.DRINK : super.getItemUseAction(stack);
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		if (this.hasPotionEffects) {
	        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
		}
		
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
	public int getColor(ItemStack stack) {
		return this.hasPotionEffects ? PotionUtils.getColor(stack) : IItemAdded.super.getColor(stack);
	}

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
	
	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		if (this.hasPotionEffects) {
        	for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
        		player.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
        	}
		}
		
		if (!this.eatenEffects.isEmpty()) {
			this.eatenEffects.forEach(effect -> effect.applyEffect(player, player));
		}
    }
	
	@Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return eatTime;
    }
	
	@Override
	public ItemFood setAlwaysEdible() {
		this.alwaysEdible = true;
		return super.setAlwaysEdible();
	}
	
	public int getHungerRestored() {
		return this.hunger;
	}
	
	public float getSaturationRestored() {
		return this.saturation;
	}
	
	public boolean doWolvesEat() {
		return this.wolvesEat;
	}
	
	public boolean isAlwaysEdible() {
		return this.alwaysEdible;
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
			
			if (itemAdded.isDrink) {
				json.addProperty("is_drink", true);
			}
			
			if (itemAdded.hasPotionEffects) {
				json.addProperty("has_potion_effects", true);
			}
			
			if (!itemAdded.eatenEffects.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				for (Effect effect : itemAdded.eatenEffects) {
					jsonArray.add(AdditionTypeEffect.GSON.toJsonTree(effect, Effect.class));
				}
				
				json.add("eaten_effects", jsonArray);
			}
			
			return json;
		}
		
		@Override
		public ItemAddedFood deserialize(JsonObject json, JsonDeserializationContext context) {
			
			int hunger = JsonUtils.getInt(json, "hunger");
			float saturation = JsonUtils.getFloat(json, "saturation");
			boolean wolvesEat = JsonUtils.getBoolean(json, "wolves_eat", false);
			boolean alwaysEdible = JsonUtils.getBoolean(json, "always_edible", false);
			
			ItemAddedFood itemAdded = new ItemAddedFood(hunger, saturation, wolvesEat, alwaysEdible);
			itemAdded.eatTime =JsonUtils.getInt(json, "eat_time", 32);
			itemAdded.isDrink = JsonUtils.getBoolean(json, "is_drink", false);
			itemAdded.hasPotionEffects = JsonUtils.getBoolean(json, "has_potion_effects", false);
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedFood itemAdded) {
			if (json.has("eaten_effects")) {
				itemAdded.eatenEffects = new ArrayList<>();
				
				JsonUtils.getJsonArray(json, "eaten_effects").forEach(effectJson -> {
					itemAdded.eatenEffects.add(AdditionTypeEffect.GSON.fromJson(effectJson, Effect.class));
				});
			}
			
			this.postDeserializeDefaults(json, itemAdded);
		}
    }

}
