package com.tmtravlr.additions.addon.loottables;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Represents a preset for a loot table, which auto-generates the loot table from values passed in.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date January 2019
 */
public abstract class LootTablePreset {
	
	public static final LootTablePreset EMPTY = new LootTablePresetEmpty();
	
	public ResourceLocation id;
	
	public abstract static class Serializer<T extends LootTablePreset> {
		
		private final ResourceLocation lootTablePresetType;
        private final Class<T> lootTablePresetClass;

        protected Serializer(ResourceLocation location, Class<T> clazz) {
            this.lootTablePresetType = location;
            this.lootTablePresetClass = clazz;
        }

		public ResourceLocation getLootTablePresetType() {
			return this.lootTablePresetType;
		}
		
		public Class<T> getLootTablePresetClass() {
			return this.lootTablePresetClass;
		}

		public abstract JsonObject serialize(T lootTablePreset, JsonSerializationContext context);

		public abstract T deserialize(JsonObject json, JsonDeserializationContext context);
    }

}
