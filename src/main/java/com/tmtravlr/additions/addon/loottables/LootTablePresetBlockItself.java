package com.tmtravlr.additions.addon.loottables;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.util.JsonGenerator;
import com.tmtravlr.additions.util.JsonGenerator.JsonElementPair;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * A loot table preset for a block that drops itself.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date February 2019
 */
public class LootTablePresetBlockItself extends LootTablePreset {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "block_itself");
	
	public Block block;
	
	private String getItemNameFromBlock() {
		String itemName = Item.getItemFromBlock(this.block).getRegistryName().toString();
		
		if (this.block instanceof IBlockAdded && ((IBlockAdded)this.block).getItemBlock() != null) {
			itemName = ((IBlockAdded)this.block).getItemBlock().getAsItem().getRegistryName().toString();
		}
		
		return itemName;
	}
	
	public static class Serializer extends LootTablePreset.Serializer<LootTablePresetBlockItself> {
		
		public Serializer() {
			super(TYPE, LootTablePresetBlockItself.class);
		}
		
		@Override
		public JsonObject serialize(LootTablePresetBlockItself preset, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.addProperty("preset_block", preset.block.getRegistryName().toString());
			
			json.add("pools", JsonGenerator.createJsonObjectArray(
					JsonGenerator.createJsonObject(
							new JsonElementPair("name", "block_itself_pool"),
							new JsonElementPair("rolls", 1),
							new JsonElementPair("entries", JsonGenerator.createJsonObjectArray(
									JsonGenerator.createJsonObject(
											new JsonElementPair("type", "item"),
											new JsonElementPair("name", preset.getItemNameFromBlock())
									)
							))
					)
			));
			
			return json;
		}
		
		@Override
		public LootTablePresetBlockItself deserialize(JsonObject json, JsonDeserializationContext context) {
			LootTablePresetBlockItself preset = new LootTablePresetBlockItself();
			
			String blockName = JsonUtils.getString(json, "preset_block");
			preset.block = Block.getBlockFromName(blockName);
			
			if (preset.block == null) {
				throw new JsonSyntaxException("Expected preset_block to be a block, was unknown string '" + blockName + "'");
			}
			
			return preset;
		}
    }
	
}
