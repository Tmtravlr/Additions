package com.tmtravlr.additions.util.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.JsonGenerator;
import com.tmtravlr.additions.util.JsonGenerator.JsonElementPair;

import net.minecraft.util.ResourceLocation;

/**
 * Generates item/block/etc. models
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018
 */
public class ItemModelGenerator {
	
	public static String getSimpleItemModel(String itemId, boolean hasColorLayer) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", "item/generated");
		json.add("textures", createItemTextures(itemId, hasColorLayer));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getToolItemModel(String itemId, boolean hasColorLayer) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", "item/handheld");
		json.add("textures", createItemTextures(itemId, hasColorLayer));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getShieldItemModel(String itemId, boolean hasColorLayer) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", "item/generated");
		json.add("textures", createItemTextures(itemId, hasColorLayer));
		
		JsonObject display = new JsonObject();
		display.add("thirdperson_righthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(10, 100, 0)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(2.5, -2, 1)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		display.add("thirdperson_lefthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(10, 100, 0)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(2.5, -2, 1)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		display.add("firstperson_righthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, -20, 0)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(3, -2, 2)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		display.add("firstperson_lefthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, -20, 0)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(3, -2, 2)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		display.add("gui", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, 0, 0)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(0, 0, 0)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		display.add("head", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, 90, 0)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(0, 8, 0)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		json.add("display", display);
		
		JsonArray overrides = new JsonArray();
		overrides.add(JsonGenerator.createJsonObject(
				new JsonElementPair("predicate", JsonGenerator.createJsonObject(new JsonElementPair("blocking", 1))), 
				new JsonElementPair("model", AdditionsMod.MOD_ID + ":item/" + itemId + ItemModelManager.MODEL_SHIELD_BLOCKING_ENDING)
		));
		json.add("overrides", overrides);
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getShieldBlockingItemModel(String itemId, boolean hasColorLayer) {

		JsonObject json = new JsonObject();
		
		json.addProperty("parent", "item/generated");
		json.add("textures", createItemTextures(itemId, hasColorLayer));
		
		JsonObject display = new JsonObject();
		display.add("thirdperson_righthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(40, 330, 0)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(1, 0, 0)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		display.add("thirdperson_lefthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(40, 330, 0)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(1, 0, 0)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		display.add("firstperson_righthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, 0, 10)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(-4, 2, 0)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		display.add("firstperson_lefthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, 0, 10)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(-4, 2, 0)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		json.add("display", display);
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getHatItemModel(String itemId, boolean hasColorLayer) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", "item/generated");
		json.add("textures", createItemTextures(itemId, hasColorLayer));
		
		JsonObject display = new JsonObject();
		display.add("head", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, 0, 0)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(0, 0, -7)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(1, 1, 1))
		));
		json.add("display", display);
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBowItemModel(String itemId, boolean hasColorLayer) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", "item/bow");
		json.add("textures", createItemTextures(itemId, hasColorLayer));
		
		JsonObject display = new JsonObject();
		display.add("thirdperson_righthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(-80, 260, -40)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(-1, -2, 2.5)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(0.9, 0.9, 0.9))
		));
		display.add("thirdperson_lefthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(-80, -280, 40)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(-1, -2, 2.5)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(0.9, 0.9, 0.9))
		));
		display.add("firstperson_righthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, -90, 25)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(1.13, 3.2, 1.13)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(0.68, 0.68, 0.68))
		));
		display.add("firstperson_lefthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, 90, -25)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(1.13, 3.2, 1.13)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(0.68, 0.68, 0.68))
		));
		json.add("display", display);
		
		JsonArray overrides = new JsonArray();
		overrides.add(JsonGenerator.createJsonObject(
				new JsonElementPair("predicate", JsonGenerator.createJsonObject(new JsonElementPair("pulling", 1))), 
				new JsonElementPair("model", AdditionsMod.MOD_ID + ":item/" + itemId + ItemModelManager.MODEL_BOW_PULLING_0_ENDING)
		));
		overrides.add(JsonGenerator.createJsonObject(
				new JsonElementPair("predicate", JsonGenerator.createJsonObject(
						new JsonElementPair("pulling", 1), 
						new JsonElementPair("pull", 0.65)
				)), 
				new JsonElementPair("model", AdditionsMod.MOD_ID + ":item/" + itemId + ItemModelManager.MODEL_BOW_PULLING_1_ENDING)
		));
		overrides.add(JsonGenerator.createJsonObject(
				new JsonElementPair("predicate", JsonGenerator.createJsonObject(
						new JsonElementPair("pulling", 1), 
						new JsonElementPair("pull", 0.9)
				)), 
				new JsonElementPair("model", AdditionsMod.MOD_ID + ":item/" + itemId + ItemModelManager.MODEL_BOW_PULLING_2_ENDING)
		));
		json.add("overrides", overrides);
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBowPullingItemModel(String itemId, String pulling, boolean hasColorLayer) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", AdditionsMod.MOD_ID + ":item/" + itemId);
		json.add("textures", createItemTextures(itemId + pulling, hasColorLayer));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getGunItemModel(String itemId, boolean hasColorLayer) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", "item/generated");
		json.add("textures", createItemTextures(itemId, hasColorLayer));
		
		JsonObject display = new JsonObject();
		display.add("thirdperson_righthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, 90, 35)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(0, 5, 3)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(0.9, 0.9, 0.9))
		));
		display.add("thirdperson_lefthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(0, -90, -35)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(0, 5, 3)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(0.9, 0.9, 0.9))
		));
		display.add("firstperson_righthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(230, 90, 90)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(1.13, 5, 1.13)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(0.68, 0.68, 0.68))
		));
		display.add("firstperson_lefthand", JsonGenerator.createJsonObject(
				new JsonElementPair("rotation", JsonGenerator.createJsonNumberArray(230, -90, -90)), 
				new JsonElementPair("translation", JsonGenerator.createJsonNumberArray(1.13, 5, 1.13)),
				new JsonElementPair("scale", JsonGenerator.createJsonNumberArray(0.68, 0.68, 0.68))
		));
		json.add("display", display);
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getItemBlockModel(ResourceLocation blockName) {
		JsonObject json = new JsonObject();
		json.addProperty("parent", blockName.getResourceDomain() + ":block/" + blockName.getResourcePath());
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	private static JsonObject createItemTextures(String itemId, boolean hasColorLayer) {
		JsonObject textures = new JsonObject();
		textures.addProperty("layer0", AdditionsMod.MOD_ID + ":items/" + itemId + ItemModelManager.TEXTURE_BASE_ENDING);
		
		if (hasColorLayer) {
			textures.addProperty("layer1", AdditionsMod.MOD_ID + ":items/" + itemId + ItemModelManager.TEXTURE_COLOR_ENDING);
		}
		
		return textures;
	}
	
}
