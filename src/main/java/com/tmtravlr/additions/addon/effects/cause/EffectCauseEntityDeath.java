package com.tmtravlr.additions.addon.effects.cause;

import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.EntityCategoryChecker;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Cause for an entity death.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public class EffectCauseEntityDeath extends EffectCauseEntityWithAttacker {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "entity_death");
	
	public boolean applies(Entity attacker, Entity entity, DamageSource damageSource) {
		return this.entityMatches(attacker, entity, damageSource);
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseEntityDeath> {
		
		public Serializer() {
			super(TYPE, EffectCauseEntityDeath.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseEntityDeath effectCause, JsonSerializationContext context) {
			return EffectCauseEntityWithAttacker.Serializer.serialize(effectCause, context);
		}
		
		@Override
		public EffectCauseEntityDeath deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseEntityDeath effectCause = new EffectCauseEntityDeath();
			EffectCauseEntityWithAttacker.Serializer.deserialize(effectCause, json, context);
			
			return effectCause;
		}
    }
}
