package com.tmtravlr.additions.addon.blocks;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.AxisAlignedBB;

public interface IBlockAddedModifiableBoundingBox extends IBlockAdded {
	
	void setHasCollisionBox(boolean hasCollisionBox);
	
	void setHasSameCollisionBoundingBox(boolean sameCollisionBoundingBox);
	
	void setBoundingBox(AxisAlignedBB boundingBox);
	
	void setCollisionBox(AxisAlignedBB collisionBox);
	
	boolean hasCollisionBox();
	
	boolean hasSameCollisionBoundingBox();
	
	AxisAlignedBB getBoundingBox();
	
	AxisAlignedBB getCollisionBox();
	
	default AxisAlignedBB getDefaultBoundingBox() {
		return Block.FULL_BLOCK_AABB;
	}
	
	default AxisAlignedBB getDefaultCollisionBox() {
		return getDefaultBoundingBox();
	}
	
	default AxisAlignedBB modifyBoundingBoxForState(AxisAlignedBB box, IBlockState state) {
		return box;
	}
	
	class Serializer {
		
		public static void serialize(JsonObject json, IBlockAddedModifiableBoundingBox blockAdded) {
			if (!blockAdded.hasCollisionBox()) {
				json.addProperty("has_collision_box", false);
			}
			
			if (!blockAdded.hasSameCollisionBoundingBox()) {
				json.addProperty("same_collision_bounding_box", false);
			}
			
			if (blockAdded.getBoundingBox().minX != blockAdded.getDefaultBoundingBox().minX) {
				json.addProperty("bounding_box_min_x", blockAdded.getBoundingBox().minX);
			}
			
			if (blockAdded.getBoundingBox().minY != blockAdded.getDefaultBoundingBox().minY) {
				json.addProperty("bounding_box_min_y", blockAdded.getBoundingBox().minY);
			}
			
			if (blockAdded.getBoundingBox().minZ != blockAdded.getDefaultBoundingBox().minZ) {
				json.addProperty("bounding_box_min_z", blockAdded.getBoundingBox().minZ);
			}
			
			if (blockAdded.getBoundingBox().maxX != blockAdded.getDefaultBoundingBox().maxX) {
				json.addProperty("bounding_box_max_x", blockAdded.getBoundingBox().maxX);
			}
			
			if (blockAdded.getBoundingBox().maxY != blockAdded.getDefaultBoundingBox().maxY) {
				json.addProperty("bounding_box_max_y", blockAdded.getBoundingBox().maxY);
			}
			
			if (blockAdded.getBoundingBox().maxZ != blockAdded.getDefaultBoundingBox().maxZ) {
				json.addProperty("bounding_box_max_z", blockAdded.getBoundingBox().maxZ);
			}
			
			if (blockAdded.getCollisionBox().minX != blockAdded.getDefaultCollisionBox().minX) {
				json.addProperty("collision_box_min_x", blockAdded.getCollisionBox().minX);
			}
			
			if (blockAdded.getCollisionBox().minY != blockAdded.getDefaultCollisionBox().minY) {
				json.addProperty("collision_box_min_y", blockAdded.getCollisionBox().minY);
			}
			
			if (blockAdded.getCollisionBox().minZ != blockAdded.getDefaultCollisionBox().minZ) {
				json.addProperty("collision_box_min_z", blockAdded.getCollisionBox().minZ);
			}
			
			if (blockAdded.getCollisionBox().maxX != blockAdded.getDefaultCollisionBox().maxX) {
				json.addProperty("collision_box_max_x", blockAdded.getCollisionBox().maxX);
			}
			
			if (blockAdded.getCollisionBox().maxY != blockAdded.getDefaultCollisionBox().maxY) {
				json.addProperty("collision_box_max_y", blockAdded.getCollisionBox().maxY);
			}
			
			if (blockAdded.getCollisionBox().maxZ != blockAdded.getDefaultCollisionBox().maxZ) {
				json.addProperty("collision_box_max_z", blockAdded.getCollisionBox().maxZ);
			}
		}
		
		public static void deserialize(JsonObject json, IBlockAddedModifiableBoundingBox blockAdded) {
			blockAdded.setHasCollisionBox(JsonUtils.getBoolean(json, "has_collision_box", true));
			blockAdded.setHasSameCollisionBoundingBox(JsonUtils.getBoolean(json, "same_collision_bounding_box", true));
			
			float minX = JsonUtils.getFloat(json, "bounding_box_min_x", (float) blockAdded.getDefaultBoundingBox().minX);
			float minY = JsonUtils.getFloat(json, "bounding_box_min_y", (float) blockAdded.getDefaultBoundingBox().minY);
			float minZ = JsonUtils.getFloat(json, "bounding_box_min_z", (float) blockAdded.getDefaultBoundingBox().minZ);
			
			blockAdded.setBoundingBox(new AxisAlignedBB(
					minX, 
					minY, 
					minZ,
					Math.max(JsonUtils.getFloat(json, "bounding_box_max_x", (float) blockAdded.getDefaultBoundingBox().maxX), minX),
					Math.max(JsonUtils.getFloat(json, "bounding_box_max_y", (float) blockAdded.getDefaultBoundingBox().maxY), minY),
					Math.max(JsonUtils.getFloat(json, "bounding_box_max_z", (float) blockAdded.getDefaultBoundingBox().maxZ), minZ)
			));
			
			minX = JsonUtils.getFloat(json, "collision_box_min_x", (float) blockAdded.getDefaultCollisionBox().minX);
			minY = JsonUtils.getFloat(json, "collision_box_min_y", (float) blockAdded.getDefaultCollisionBox().minY);
			minZ = JsonUtils.getFloat(json, "collision_box_min_z", (float) blockAdded.getDefaultCollisionBox().minZ);
			
			blockAdded.setCollisionBox(new AxisAlignedBB(
					minX, 
					minY, 
					minZ,
					Math.max(JsonUtils.getFloat(json, "collision_box_max_x", (float) blockAdded.getDefaultCollisionBox().maxX), minX),
					Math.max(JsonUtils.getFloat(json, "collision_box_max_y", (float) blockAdded.getDefaultCollisionBox().maxY), minY),
					Math.max(JsonUtils.getFloat(json, "collision_box_max_z", (float) blockAdded.getDefaultCollisionBox().maxZ), minZ)
			));
		}
		
	}

}
