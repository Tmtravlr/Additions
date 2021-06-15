package com.tmtravlr.additions.addon.effects.cause;

import java.util.HashSet;

import javax.annotation.Nullable;

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
 * Cause for an entity attacked.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public class EffectCauseEntityAttacked extends EffectCauseEntityWithAttacker {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "entity_attacked");
	
	public boolean applies(@Nullable Entity attacker, Entity entity, DamageSource damageSource) {
		return this.entityMatches(attacker, entity, damageSource);
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseEntityAttacked> {
		
		public Serializer() {
			super(TYPE, EffectCauseEntityAttacked.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseEntityAttacked effectCause, JsonSerializationContext context) {
			return EffectCauseEntityWithAttacker.Serializer.serialize(effectCause, context);
		}
		
		@Override
		public EffectCauseEntityAttacked deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseEntityAttacked effectCause = new EffectCauseEntityAttacked();
			EffectCauseEntityWithAttacker.Serializer.deserialize(effectCause, json, context);
			
			return effectCause;
		}
    }
}
