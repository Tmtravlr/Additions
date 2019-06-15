package com.tmtravlr.additions.addon.effects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Consumes an item from the ItemStack.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class EffectConsumeItem extends Effect {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "consume_item");
	
	public int amount = 1;
	public float chance = 1;

	@Override
	public void affectItemStack(Entity cause, World world, ItemStack stack) {
		if (world.getMinecraftServer() != null && world.rand.nextFloat() <= this.chance) {
			if (!(cause instanceof EntityPlayer && ((EntityPlayer)cause).isCreative())) {
				stack.setCount(stack.getCount() - amount);
			}
		}
	}

	public static class Serializer extends Effect.Serializer<EffectConsumeItem> {
		
		public Serializer() {
			super(TYPE, EffectConsumeItem.class);
		}
		
		@Override
		public JsonObject serialize(EffectConsumeItem effect, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if (effect.amount > 1) {
				json.addProperty("amount", effect.amount);
			}
			
			return json;
		}
		
		@Override
		public EffectConsumeItem deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectConsumeItem effect = new EffectConsumeItem();
			
			effect.amount = JsonUtils.getInt(json, "amount", 1);
			
			return effect;
		}
    }
}
