package com.tmtravlr.additions.addon.loottables;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.util.JsonGenerator;
import com.tmtravlr.additions.util.OtherSerializers;
import com.tmtravlr.additions.util.JsonGenerator.JsonElementPair;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * A loot table preset for a block that drops an item other than itself.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date January 2019
 */
public class LootTablePresetBlockItemDrop extends LootTablePreset {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "block_item_drop");
	
	public Block block;
	public ItemStack dropStack;
	public int itemMin = 1;
	public int itemMax = 1;
	public boolean fortunable = true;
	public boolean silkTouchable = true;
	
	private String getItemNameFromBlock() {
		String itemName = Item.getItemFromBlock(this.block).getRegistryName().toString();
		
		if (this.block instanceof IBlockAdded && ((IBlockAdded)this.block).getItemBlock() != null) {
			itemName = ((IBlockAdded)this.block).getItemBlock().getAsItem().getRegistryName().toString();
		}
		
		return itemName;
	}
	
	public static class Serializer extends LootTablePreset.Serializer<LootTablePresetBlockItemDrop> {
		
		public Serializer() {
			super(TYPE, LootTablePresetBlockItemDrop.class);
		}
		
		@Override
		public JsonObject serialize(LootTablePresetBlockItemDrop preset, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.addProperty("preset_block", preset.block.getRegistryName().toString());
			json.add("preset_dropStack", OtherSerializers.ItemStackSerializer.serialize(preset.dropStack));
			
			if (preset.itemMin != 0) {
				json.addProperty("preset_dropMin", preset.itemMin);
			}
			
			if (preset.itemMax != preset.itemMin) {
				json.addProperty("preset_dropMax", preset.itemMax);
			}
			
			if (preset.fortunable) {
				json.addProperty("preset_fortunable", true);
			}
			
			if (preset.silkTouchable) {
				json.addProperty("preset_silkTouchable", true);
			}
			
			JsonArray pools = new JsonArray();
			
			JsonObject itemJson = new JsonObject();
			itemJson.addProperty("name", "item_pool");
			itemJson.addProperty("rolls", 1);
			JsonArray entries = new JsonArray();
			JsonObject itemEntry = new JsonObject();
			itemEntry.addProperty("type", "item");
			itemEntry.addProperty("name", preset.dropStack.getItem().getRegistryName().toString());
			itemEntry.addProperty("weight", 1);
			JsonArray functions = new JsonArray();
			
			if (preset.itemMin != 1 || preset.itemMax != 1) {
				JsonObject function = new JsonObject();
				function.addProperty("function", "set_count");
				if (preset.itemMin == preset.itemMax) {
					function.addProperty("count", preset.itemMin);
				} else {
					function.add("count", JsonGenerator.createJsonObject(
							new JsonElementPair("min", preset.itemMin), 
							new JsonElementPair("max", preset.itemMax)
					));
				}
				functions.add(function);
			}
			
			if (preset.dropStack.getMetadata() != 0) {
				functions.add(JsonGenerator.createJsonObject(
						new JsonElementPair("function", "set_data"), 
						new JsonElementPair("data", preset.dropStack.getMetadata())
				));
			}
			
			if (preset.dropStack.getTagCompound() != null && preset.dropStack.getTagCompound().getSize() != 0) {
				functions.add(JsonGenerator.createJsonObject(
						new JsonElementPair("function", "set_nbt"), 
						new JsonElementPair("tag", preset.dropStack.getTagCompound().toString())
				));
			}
			
			if (preset.fortunable) {
				functions.add(JsonGenerator.createJsonObject(
						new JsonElementPair("function", "lootoverhaul:fortune_enchant"), 
						new JsonElementPair("count", JsonGenerator.createJsonObject(
								new JsonElementPair("min", 0), 
								new JsonElementPair("max", 1)
						))
				));
			}
			
			itemEntry.add("functions", functions);
			entries.add(itemEntry);
			itemJson.add("entries", entries);
			
			if (preset.silkTouchable) {
				itemJson.add("conditions", JsonGenerator.createJsonObjectArray(
						JsonGenerator.createJsonObject(
								new JsonElementPair("condition", "lootoverhaul:not"),
								new JsonElementPair("conditions", JsonGenerator.createJsonObjectArray(
										JsonGenerator.createJsonObject(
												new JsonElementPair("condition", "lootoverhaul:silk_touch")
										)
								))
						)
				));
			}
			
			pools.add(itemJson);
			
			if (preset.silkTouchable) {
				pools.add(JsonGenerator.createJsonObject(
						new JsonElementPair("name", "silk_touch_pool"),
						new JsonElementPair("rolls", 1),
						new JsonElementPair("entries", JsonGenerator.createJsonObjectArray(
								JsonGenerator.createJsonObject(
										new JsonElementPair("type", "item"),
										new JsonElementPair("name", preset.getItemNameFromBlock()),
										new JsonElementPair("weight", 1)
								)
						)),
						new JsonElementPair("conditions", JsonGenerator.createJsonObjectArray(
								JsonGenerator.createJsonObject(
										new JsonElementPair("condition", "lootoverhaul:silk_touch")
								)
						))
				));
			}
			
			json.add("pools", pools);
			
			return json;
		}
		
		@Override
		public LootTablePresetBlockItemDrop deserialize(JsonObject json, JsonDeserializationContext context) {
			LootTablePresetBlockItemDrop preset = new LootTablePresetBlockItemDrop();
			
			String blockName = JsonUtils.getString(json, "preset_block");
			preset.block = Block.getBlockFromName(blockName);
			
			if (preset.block == null) {
				throw new JsonSyntaxException("Expected preset_block to be a block, was unknown string '" + blockName + "'");
			}
			
			preset.dropStack = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "preset_dropStack"));
			
			if (json.has("preset_dropTag")) {
				String tagString = JsonUtils.getString(json, "preset_dropTag");
				try {
					preset.dropStack.setTagCompound(JsonToNBT.getTagFromJson(tagString));
				} catch (NBTException e) {
					throw new JsonSyntaxException("Couldn't parse nbt tag preset_dropTag: ", e);
				}
			}
			
			preset.itemMin = JsonUtils.getInt(json, "preset_dropMin", 0);
			preset.itemMax = JsonUtils.getInt(json, "preset_dropMax", preset.itemMin);
			preset.fortunable = JsonUtils.getBoolean(json, "preset_fortunable", false);
			preset.silkTouchable = JsonUtils.getBoolean(json, "preset_silkTouchable", false);
			
			return preset;
		}
    }
	
}
