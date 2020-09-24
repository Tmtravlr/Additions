package com.tmtravlr.additions.addon.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;

public interface IEntityAddedProjectile extends IProjectile {
	
	void shootProjectile(Entity shooter, float pitch, float yaw, float rotationOffset, float velocity, float inaccuracy);
	
	void setIsProjectileCritical(boolean critical);
	
	double getProjectileDamage();
	
	void setProjectileDamage(double damage);
	
	void setProjectileKnockback(int knockbackStrength);
	
	void setProjectileFire(int seconds);
	
	void setProjectilePickupStatus(EntityArrow.PickupStatus status);

	default Entity getAsEntity() {
		if (this instanceof Entity) {
			return (Entity) this;
		}
		
		return null;
	}
}
