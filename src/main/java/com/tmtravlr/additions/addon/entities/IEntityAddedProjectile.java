package com.tmtravlr.additions.addon.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;

public interface IEntityAddedProjectile extends IProjectile {
	
	public void shootProjectile(Entity shooter, float pitch, float yaw, float rotationOffset, float velocity, float inaccuracy);
	
	public void setIsProjectileCritical(boolean critical);
	
	public double getProjectileDamage();
	
	public void setProjectileDamage(double damage);
	
	public void setProjectileKnockback(int knockbackStrength);
	
	public void setProjectileFire(int seconds);
	
	public void setProjectilePickupStatus(EntityArrow.PickupStatus status);

	public default Entity getAsEntity() {
		if (this instanceof Entity) {
			return (Entity) this;
		}
		
		return null;
	}
}
