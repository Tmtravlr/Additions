package com.tmtravlr.additions.util.models;

import com.google.gson.JsonObject;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.JsonGenerator;
import com.tmtravlr.additions.util.JsonGenerator.JsonElementPair;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.util.EnumFacing;

public class BlockStateGenerator {

	public static String getBlockStateSimple(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("variants", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("normal", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId)
				))
		));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getBlockStateStairs(String blockId) {
		JsonObject json = new JsonObject();
		JsonObject variants = new JsonObject();
		String modelName = AdditionsMod.MOD_ID + ":" + blockId;
		
		for (EnumFacing facing : BlockStairs.FACING.getAllowedValues()) {
			for (EnumShape shape : BlockStairs.SHAPE.getAllowedValues()) {
				for (EnumHalf half : BlockStairs.HALF.getAllowedValues()) {
					String variantName = "facing=" + facing.getName() + ",half=" + half.getName() + ",shape=" + shape.getName();
					String model = modelName;
					int rotationX = 0;
					int rotationY = 0;
					
					if (shape == EnumShape.OUTER_LEFT || shape == EnumShape.OUTER_RIGHT) {
						model += BlockModelManager.MODEL_OUTER_ENDING;
					} else if (shape == EnumShape.INNER_LEFT || shape == EnumShape.INNER_RIGHT) {
						model += BlockModelManager.MODEL_INNER_ENDING;
					}
					
					if (half == EnumHalf.TOP) {
						rotationX = 180;
					}
					
					if (shape == EnumShape.STRAIGHT || shape == EnumShape.INNER_RIGHT || shape == EnumShape.OUTER_RIGHT) {
						if (facing == EnumFacing.WEST) {
							rotationY = 180;
						} else if (facing == EnumFacing.SOUTH) {
							rotationY = 90;
						} else if (facing == EnumFacing.NORTH) {
							rotationY = 270;
						}
					} else {
						if (half == EnumHalf.BOTTOM) {
							if (facing == EnumFacing.EAST) {
								rotationY = 270;
							} else if (facing == EnumFacing.WEST) {
								rotationY = 90;
							} else if (facing == EnumFacing.NORTH) {
								rotationY = 180;
							}
						} else {
							if (facing == EnumFacing.EAST) {
								rotationY = 90;
							} else if (facing == EnumFacing.WEST) {
								rotationY = 270;
							} else if (facing == EnumFacing.SOUTH) {
								rotationY = 180;
							}
						}
					}
					
					JsonObject variantJson = new JsonObject();
					variantJson.addProperty("model", model);
					
					if (rotationX > 0) {
						variantJson.addProperty("x", rotationX);
					}
					
					if (rotationY > 0) {
						variantJson.addProperty("y", rotationY);
					}
					
					if (rotationX > 0 || rotationY > 0) {
						variantJson.addProperty("uvlock", true);
					}
					
					variants.add(variantName, variantJson);
				}
			}
		}
		json.add("variants", variants);
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getBlockStateSlab(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("variants", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("half=full", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_FULL_ENDING)
				)),
				new JsonGenerator.JsonElementPair("half=bottom", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId)
				)),
				new JsonGenerator.JsonElementPair("half=top", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_TOP_ENDING)
				)),
				new JsonGenerator.JsonElementPair("half=east", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_VERTICAL_ENDING)
				)),
				new JsonGenerator.JsonElementPair("half=south", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_VERTICAL_ENDING),
						new JsonGenerator.JsonElementPair("y", 90)
				)),
				new JsonGenerator.JsonElementPair("half=west", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_VERTICAL_ENDING),
						new JsonGenerator.JsonElementPair("y", 180)
				)),
				new JsonGenerator.JsonElementPair("half=north", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_VERTICAL_ENDING),
						new JsonGenerator.JsonElementPair("y", 270)
				))
		));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getBlockStateCarpet(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("variants", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("facing=up", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId)
				)),
				new JsonGenerator.JsonElementPair("facing=down", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("x", 180)
				)),
				new JsonGenerator.JsonElementPair("facing=east", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("x", 270),
						new JsonGenerator.JsonElementPair("y", 270)
				)),
				new JsonGenerator.JsonElementPair("facing=south", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("x", 270)
				)),
				new JsonGenerator.JsonElementPair("facing=west", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("x", 270),
						new JsonGenerator.JsonElementPair("y", 90)
				)),
				new JsonGenerator.JsonElementPair("facing=north", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("x", 270),
						new JsonGenerator.JsonElementPair("y", 180)
				))
		));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getBlockStateFacing(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("variants", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("facing=up", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("x", 270)
				)),
				new JsonGenerator.JsonElementPair("facing=down", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("x", 90)
				)),
				new JsonGenerator.JsonElementPair("facing=east", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("y", 90)
				)),
				new JsonGenerator.JsonElementPair("facing=south", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("y", 180)
				)),
				new JsonGenerator.JsonElementPair("facing=west", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("y", 270)
				)),
				new JsonGenerator.JsonElementPair("facing=north", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId)
				))
		));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getBlockStatePillar(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("variants", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("axis=y", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId)
				)),
				new JsonGenerator.JsonElementPair("axis=z", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("x", 90)
				)),
				new JsonGenerator.JsonElementPair("axis=x", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("x", 90),
						new JsonGenerator.JsonElementPair("y", 90)
				))
		));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getBlockStateLadder(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("variants", JsonGenerator.createJsonObject(
				new JsonGenerator.JsonElementPair("facing=north", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId)
				)),
				new JsonGenerator.JsonElementPair("facing=east", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("y", 90)
				)),
				new JsonGenerator.JsonElementPair("facing=south", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("y", 180)
				)),
				new JsonGenerator.JsonElementPair("facing=west", JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId),
						new JsonGenerator.JsonElementPair("y", 270)
				))
		));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getBlockStatePane(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("multipart", JsonGenerator.createJsonObjectArray(
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_POST_ENDING)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("north", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("east", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("y", 90)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("south", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ALT_ENDING)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("west", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ALT_ENDING),
								new JsonGenerator.JsonElementPair("y", 90)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("north", false))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_NO_SIDE_ENDING)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("east", false))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_NO_SIDE_ALT_ENDING)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("south", false))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_NO_SIDE_ALT_ENDING),
								new JsonGenerator.JsonElementPair("y", 90)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("west", false))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_NO_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("y", 270)
						))
				)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getBlockStateFence(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("multipart", JsonGenerator.createJsonObjectArray(
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_POST_ENDING)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("north", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("uvlock", true)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("east", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("y", 90),
								new JsonGenerator.JsonElementPair("uvlock", true)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("south", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("y", 180),
								new JsonGenerator.JsonElementPair("uvlock", true)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("west", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("y", 270),
								new JsonGenerator.JsonElementPair("uvlock", true)
						))
				)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}

	public static String getBlockStateWall(String blockId) {
		JsonObject json = new JsonObject();
		
		json.add("multipart", JsonGenerator.createJsonObjectArray(
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("up", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_POST_ENDING)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("north", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("uvlock", true)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("east", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("y", 90),
								new JsonGenerator.JsonElementPair("uvlock", true)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("south", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("y", 180),
								new JsonGenerator.JsonElementPair("uvlock", true)
						))
				),
				JsonGenerator.createJsonObject(
						new JsonGenerator.JsonElementPair("when", JsonGenerator.createJsonObject(new JsonGenerator.JsonElementPair("west", true))),
						new JsonGenerator.JsonElementPair("apply", JsonGenerator.createJsonObject(
								new JsonGenerator.JsonElementPair("model", AdditionsMod.MOD_ID + ":" + blockId + BlockModelManager.MODEL_SIDE_ENDING),
								new JsonGenerator.JsonElementPair("y", 270),
								new JsonGenerator.JsonElementPair("uvlock", true)
						))
				)
		));
		
		return ModelGenerator.GSON.toJson(json);
	}

}
