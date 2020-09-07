package com.tmtravlr.additions.addon.loottables;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.JsonGenerator;
import com.tmtravlr.additions.util.JsonGenerator.JsonElementPair;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * A loot table preset that drops another loot table.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date Febuary 2019
 */
public class LootTablePresetOtherLootTable extends LootTablePreset {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "other_loot_table");
	
	public ResourceLocation otherLootTable;
	
	public static class Serializer extends LootTablePreset.Serializer<LootTablePresetOtherLootTable> {
		
		public Serializer() {
			super(TYPE, LootTablePresetOtherLootTable.class);
		}
		
		@Override
		public JsonObject serialize(LootTablePresetOtherLootTable preset, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.addProperty("preset_otherLootTable", preset.otherLootTable.toString());
			
			json.add("pools", JsonGenerator.createJsonObjectArray(
					JsonGenerator.createJsonObject(
							new JsonElementPair("name", "other_table_pool"),
							new JsonElementPair("rolls", 1),
							new JsonElementPair("entries", JsonGenerator.createJsonObjectArray(
									JsonGenerator.createJsonObject(
											new JsonElementPair("type", "loot_table"),
											new JsonElementPair("name", preset.otherLootTable.toString())
									)
							))
					)
			));
			
			return json;
		}
		
		@Override
		public LootTablePresetOtherLootTable deserialize(JsonObject json, JsonDeserializationContext context) {
			LootTablePresetOtherLootTable preset = new LootTablePresetOtherLootTable();
			
			preset.otherLootTable = new ResourceLocation(JsonUtils.getString(json, "preset_otherLootTable"));
			
			return preset;
		}
    }
	
}
