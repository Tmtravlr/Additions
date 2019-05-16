package com.tmtravlr.additions.addon.effects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.addon.effects.cause.EffectCause;

import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Effects like potion effects, loot tables, and commands.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date July 2017
 */
public abstract class Effect {
	
	public abstract void applyEffect(Entity entity);
	
	public abstract void applyEffect(World world, BlockPos pos);
	
	public abstract static class Serializer<T extends Effect> {
		private final ResourceLocation effectType;
        private final Class<T> effectClass;

        protected Serializer(ResourceLocation location, Class<T> clazz)
        {
            this.effectType = location;
            this.effectClass = clazz;
        }

		public ResourceLocation getEffectType() {
			return this.effectType;
		}
		
		public Class<T> getEffectClass() {
			return this.effectClass;
		}

		public abstract JsonObject serialize(T effect, JsonSerializationContext context);

		public abstract T deserialize(JsonObject json, JsonDeserializationContext context);
    }
	
}
