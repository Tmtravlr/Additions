package com.tmtravlr.additions.addon.loottables;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.util.ResourceLocation;

/**
 * A loot table preset for an empty loot table.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since January 2019 
 */
public class LootTablePresetEmpty extends LootTablePreset {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "empty");
	
	public static class Serializer extends LootTablePreset.Serializer<LootTablePresetEmpty> {
		
		public Serializer() {
			super(TYPE, LootTablePresetEmpty.class);
		}
		
		@Override
		public JsonObject serialize(LootTablePresetEmpty preset, JsonSerializationContext context) {
			return new JsonObject();
		}
		
		@Override
		public LootTablePresetEmpty deserialize(JsonObject json, JsonDeserializationContext context) {
			return new LootTablePresetEmpty();
		}
    }
	
}
