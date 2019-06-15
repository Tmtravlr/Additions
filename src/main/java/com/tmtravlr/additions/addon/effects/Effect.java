package com.tmtravlr.additions.addon.effects;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
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
	
	public void affectEntity(@Nullable Entity cause, Entity entity) {}
	
	public void affectBlock(@Nullable Entity cause, World world, BlockPos pos) {}
	
	public void affectItemStack(@Nullable Entity cause, World world, ItemStack stack) {}
	
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
