package com.tmtravlr.additions.addon.effects.cause;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.util.ResourceLocation;

/**
 * Represents an effect's cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public abstract class EffectCause {
	
	public abstract static class Serializer<T extends EffectCause> {
		private final ResourceLocation effectCauseType;
        private final Class<T> effectCauseClass;

        protected Serializer(ResourceLocation location, Class<T> clazz)
        {
            this.effectCauseType = location;
            this.effectCauseClass = clazz;
        }

		public ResourceLocation getEffectCauseType() {
			return this.effectCauseType;
		}
		
		public Class<T> getEffectCauseClass() {
			return this.effectCauseClass;
		}

		public abstract JsonObject serialize(T effectCause, JsonSerializationContext context);

		public abstract T deserialize(JsonObject json, JsonDeserializationContext context);
    }
	
}
