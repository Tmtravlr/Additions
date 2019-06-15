package com.tmtravlr.additions.addon.effects;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Damages the item in the ItemStack, if it is damageable.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class EffectDamageItem extends Effect {
	private static final Random RAND = new Random();
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "damage_item");
	
	public int amount = 1;
	public float chance = 1;

	@Override
	public void affectItemStack(Entity cause, World world, ItemStack stack) {
		if (world.getMinecraftServer() != null && world.rand.nextFloat() <= this.chance) {
			if (cause instanceof EntityLivingBase) {
				stack.damageItem(amount, (EntityLivingBase) cause);
			} else {
				stack.attemptDamageItem(amount, RAND, null);
			}
		}
	}

	public static class Serializer extends Effect.Serializer<EffectDamageItem> {
		
		public Serializer() {
			super(TYPE, EffectDamageItem.class);
		}
		
		@Override
		public JsonObject serialize(EffectDamageItem effect, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if (effect.amount > 1) {
				json.addProperty("amount", effect.amount);
			}
			
			if (effect.chance < 1) {
				json.addProperty("chance", effect.chance);
			}
			
			return json;
		}
		
		@Override
		public EffectDamageItem deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectDamageItem effect = new EffectDamageItem();
			
			effect.amount = JsonUtils.getInt(json, "amount", 1);
			effect.chance = JsonUtils.getFloat(json, "chance", 1);
			
			return effect;
		}
    }
}
