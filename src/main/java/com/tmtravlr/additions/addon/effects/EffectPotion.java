package com.tmtravlr.additions.addon.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Info about a potion effect to apply.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date July 2017
 */
public class EffectPotion extends Effect {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "potion");
	
	private static final Random RAND = new Random();

	public PotionType potionType;
	public PotionEffect potion;
	public int reapplicationDelay = 0;
	public float chance = 1;

	@Override
	public void applyEffect(Entity entity) {
		if (!entity.world.isRemote && entity instanceof EntityLivingBase && RAND.nextFloat() <= this.chance) {
			List<PotionEffect> effectsToApply = new ArrayList<>();
			
			if (this.potionType != null) {
				effectsToApply.addAll(this.potionType.getEffects());
			}
			
			if (this.potion != null) {
				effectsToApply.add(this.potion);
			}
			
			if (!effectsToApply.isEmpty()) {
				boolean apply = true;
				
				if (this.reapplicationDelay > 0) {
					boolean hasAllEffects = true;
					for (PotionEffect effect : effectsToApply) {
						if (!((EntityLivingBase)entity).isPotionActive(effect.getPotion())) {
							hasAllEffects = false;
							break;
						}
					}
				
					apply = !hasAllEffects || entity.ticksExisted % this.reapplicationDelay == 0;
				}
				
				if (apply) {
					for (PotionEffect effect : effectsToApply) {
						((EntityLivingBase) entity).addPotionEffect(new PotionEffect(effect));
					}
				}
			}
		}
	}

	@Override
	public void applyEffect(World world, BlockPos pos) {
		//Nothing to do here...
	}

	public static class Serializer extends Effect.Serializer<EffectPotion> {
		
		public Serializer() {
			super(TYPE, EffectPotion.class);
		}
		
		@Override
		public JsonObject serialize(EffectPotion effect, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if (effect.potionType != null) {
				json.addProperty("potion_type", effect.potionType.getRegistryName().toString());
			}
			
			if (effect.potion != null) {
				json.add("potion", OtherSerializers.PotionEffectSerializer.serialize(effect.potion));
			}
			
			if (effect.reapplicationDelay > 0) {
				json.addProperty("reapplication_delay", effect.reapplicationDelay);
			}
			
			if (effect.chance < 1) {
				json.addProperty("chance", effect.chance);
			}
			
			return json;
		}
		
		@Override
		public EffectPotion deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectPotion effect = new EffectPotion();
			
			if (json.has("potion_type")) {
				String potionTypeName = JsonUtils.getString(json, "potion_type");
				effect.potionType = PotionType.getPotionTypeForName(potionTypeName);
				
				if (effect.potionType == null) {
					throw new JsonSyntaxException("Expected potion_type to be an potion type, was unknown string '" + potionTypeName + "'");
				}
			}
			
			if (json.has("potion")) {
				effect.potion = OtherSerializers.PotionEffectSerializer.deserialize(JsonUtils.getJsonObject(json, "potion"));
			}
			
			effect.reapplicationDelay = JsonUtils.getInt(json, "reapplication_delay", 0);
			effect.chance = JsonUtils.getFloat(json, "chance", 1);
			
			return effect;
		}
    }
}
