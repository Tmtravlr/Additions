package com.tmtravlr.additions.addon.effects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.util.ResourceLocation;

/**
 * Cancel an event.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class EffectCancelNormal extends Effect {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "cancel_normal");

	public static class Serializer extends Effect.Serializer<EffectCancelNormal> {
		
		public Serializer() {
			super(TYPE, EffectCancelNormal.class);
		}
		
		@Override
		public JsonObject serialize(EffectCancelNormal effect, JsonSerializationContext context) {
			return new JsonObject();
		}
		
		@Override
		public EffectCancelNormal deserialize(JsonObject json, JsonDeserializationContext context) {
			return new EffectCancelNormal();
		}
    }
}
