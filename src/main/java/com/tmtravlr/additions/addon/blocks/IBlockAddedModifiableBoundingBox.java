package com.tmtravlr.additions.addon.blocks;

public interface IBlockAddedModifiableBoundingBox {
	
	public void setHasCollisionBox(boolean hasCollisionBox);
	
	public void setHasSameCollisionBoundingBox(boolean sameCollisionBoundingBox);
	
	public void setBoundingBoxMinX(float boundingBoxMinX);
	
	public void setBoundingBoxMinY(float boundingBoxMinY);
	
	public void setBoundingBoxMinZ(float boundingBoxMinZ);
	
	public void setBoundingBoxMaxX(float boundingBoxMaxX);
	
	public void setBoundingBoxMaxY(float boundingBoxMaxY);
	
	public void setBoundingBoxMaxZ(float boundingBoxMaxZ);
	
	public void setCollisionBoxMinX(float collisionBoxMinX);
	
	public void setCollisionBoxMinY(float collisionBoxMinY);
	
	public void setCollisionBoxMinZ(float collisionBoxMinZ);
	
	public void setCollisionBoxMaxX(float collisionBoxMaxX);
	
	public void setCollisionBoxMaxY(float collisionBoxMaxY);
	
	public void setCollisionBoxMaxZ(float collisionBoxMaxZ);
	
	public boolean hasCollisionBox();
	
	public boolean hasSameCollisionBoundingBox();
	
	public float getBoundingBoxMinX();
	
	public float getBoundingBoxMinY();
	
	public float getBoundingBoxMinZ();
	
	public float getBoundingBoxMaxX();
	
	public float getBoundingBoxMaxY();
	
	public float getBoundingBoxMaxZ();
	
	public float getCollisionBoxMinX();
	
	public float getCollisionBoxMinY();
	
	public float getCollisionBoxMinZ();
	
	public float getCollisionBoxMaxX();
	
	public float getCollisionBoxMaxY();
	
	public float getCollisionBoxMaxZ();

}
