package com.tmtravlr.additions.effects;

import java.lang.reflect.Type;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.util.OtherSerializers;

/**
 * Info about a potion effect to apply.
 * @author Rebeca Rey (Tmtravlr)
 * @date July 2017
 */
public class EffectInfoPotion extends EffectInfo {
	
	private static final Random RAND = new Random();

	public PotionEffect potion;
	public float chance;
	
	public EffectInfoPotion(float chanceToSet, PotionEffect potionToSet) {
		this.chance = chanceToSet;
		this.potion = potionToSet;
	}

	@Override
	public void applyEffect(Entity entity) {
		if(RAND.nextFloat() <= this.chance && entity instanceof EntityLivingBase) {
			((EntityLivingBase) entity).addPotionEffect(potion);
		}
	}

	@Override
	public void applyEffect(BlockPos pos) {
		//Nothing to do here...
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((potion == null) ? 0 : potion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EffectInfoPotion other = (EffectInfoPotion) obj;
		if (potion == null) {
			if (other.potion != null)
				return false;
		} else if (!potion.equals(other.potion))
			return false;
		return true;
	}

	public static class Serializer implements JsonDeserializer<EffectInfoPotion>, JsonSerializer<EffectInfoPotion> {

		@Override
		public JsonElement serialize(EffectInfoPotion effect, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if(effect.chance < 1.0f) {
				json.add("chance", new JsonPrimitive(effect.chance));
			}
			
			json.add("potion", OtherSerializers.PotionEffectSerializer.serialize(effect.potion, context));
			
			return json;
        }
		
		@Override
		public EffectInfoPotion deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
			JsonObject json = jsonElement.getAsJsonObject();
			
        	float chance = JsonUtils.getFloat(json, "chance", 1.0f);
        	PotionEffect potion = OtherSerializers.PotionEffectSerializer.deserialize(JsonUtils.getJsonObject(json, "potion").getAsJsonObject(), context);
        	
        	return new EffectInfoPotion(chance, potion);
        }
    }
}
