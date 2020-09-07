package com.tmtravlr.additions.util.models;

import com.google.gson.JsonObject;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.JsonGenerator;

/**
 * Generates item/block/etc. models
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2018
 */
public class BlockModelGenerator {
	
	public static String getBlockModelSimple(String blockId) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", "block/cube_all");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("all", AdditionsMod.MOD_ID + ":blocks/" + blockId)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelStairs(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/stairs");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("bottom", textureName),
				new JsonGenerator.JsonElementPair("top", textureName),
				new JsonGenerator.JsonElementPair("side", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelStairsOuter(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/outer_stairs");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("bottom", textureName),
				new JsonGenerator.JsonElementPair("top", textureName),
				new JsonGenerator.JsonElementPair("side", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelStairsInner(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/inner_stairs");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("bottom", textureName),
				new JsonGenerator.JsonElementPair("top", textureName),
				new JsonGenerator.JsonElementPair("side", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelSlabBottom(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/half_slab");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("bottom", textureName),
				new JsonGenerator.JsonElementPair("top", textureName),
				new JsonGenerator.JsonElementPair("side", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelSlabTop(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/upper_slab");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("bottom", textureName),
				new JsonGenerator.JsonElementPair("top", textureName),
				new JsonGenerator.JsonElementPair("side", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelSlabVertical(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", AdditionsMod.MOD_ID + ":block/vertical_slab");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("bottom", textureName),
				new JsonGenerator.JsonElementPair("top", textureName),
				new JsonGenerator.JsonElementPair("side", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelCarpetBottom(String blockId) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", AdditionsMod.MOD_ID + ":block/tile");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("all", AdditionsMod.MOD_ID + ":blocks/" + blockId)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelCarpetTop(String blockId) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", AdditionsMod.MOD_ID + ":block/ceiling_tile");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("all", AdditionsMod.MOD_ID + ":blocks/" + blockId)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelCarpetVertical(String blockId) {
		JsonObject json = new JsonObject();
		
		json.addProperty("parent", AdditionsMod.MOD_ID + ":block/vertical_tile");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("all", AdditionsMod.MOD_ID + ":blocks/" + blockId)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelFacing(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/orientable");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("top", textureName + BlockModelManager.TEXTURE_TOP_ENDING),
				new JsonGenerator.JsonElementPair("front", textureName),
				new JsonGenerator.JsonElementPair("side", textureName + BlockModelManager.TEXTURE_SIDE_ENDING)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelPillar(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/cube_column");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("end", textureName + BlockModelManager.TEXTURE_TOP_ENDING),
				new JsonGenerator.JsonElementPair("side", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelLadder(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("ambientocclusion", false);
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("particle", textureName),
				new JsonGenerator.JsonElementPair("texture", textureName)
		));
		json.add("elements", JsonGenerator.createJsonObjectArray(JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("from", JsonGenerator.createJsonNumberArray(0, 0, 15.2)),
				new JsonGenerator.JsonElementPair("to", JsonGenerator.createJsonNumberArray(16, 16, 15.2)),
				new JsonGenerator.JsonElementPair("shade", false),
				new JsonGenerator.JsonElementPair("faces", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("north", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("uv", JsonGenerator.createJsonNumberArray(0, 0, 16, 16)),
								new JsonGenerator.JsonElementPair("texture", "#texture")
						)),
						new JsonGenerator.JsonElementPair("south", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("uv", JsonGenerator.createJsonNumberArray(0, 0, 16, 16)),
								new JsonGenerator.JsonElementPair("texture", "#texture")
						))
				))
		)));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelPanePost(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/pane_post");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("edge", textureName + BlockModelManager.TEXTURE_TOP_ENDING),
				new JsonGenerator.JsonElementPair("pane", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelPaneSide(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/pane_side");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("edge", textureName + BlockModelManager.TEXTURE_TOP_ENDING),
				new JsonGenerator.JsonElementPair("pane", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelPaneSideAlt(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/pane_side_alt");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("edge", textureName + BlockModelManager.TEXTURE_TOP_ENDING),
				new JsonGenerator.JsonElementPair("pane", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelPaneNoSide(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/pane_noside");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("pane", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelPaneNoSideAlt(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/pane_noside_alt");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("pane", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelFencePost(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/fence_post");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("texture", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelFenceSide(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/fence_side");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("texture", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelFenceInventory(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/fence_inventory");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("texture", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelWallPost(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/wall_post");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("wall", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelWallSide(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/wall_side");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("wall", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
	public static String getBlockModelWallInventory(String blockId) {
		JsonObject json = new JsonObject();
		String textureName = AdditionsMod.MOD_ID + ":blocks/" + blockId;
		
		json.addProperty("parent", "block/wall_inventory");
		json.add("textures", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("wall", textureName)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}
	
}
