package com.tmtravlr.additions.addon.items;

import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.entities.IEntityAddedProjectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.List;

public interface IItemAddedProjectile {
	
	float getBaseDamage();
	
	void setBaseDamage(float baseDamage);
	
	float getBasePunch();
	
	void setBasePunch(float basePunch);
	
	float getGravity();
	
	void setGravity(float gravity);
	
	boolean worksInWater();
	
	void setWorksInWater(boolean worksInWater);
	
	boolean sticksInGround();
	
	void setSticksInGround(boolean sticksInGround);
	
	boolean hasPotionEffects();
	
	void setHasPotionEffects(boolean hasPotionEffects);
	
	boolean worksWithInfinity();
	
	void setWorksWithInfinity(boolean worksWithInfinity);
	
	boolean renders3D();
	
	void setRenders3D(boolean renders3D);
	
	boolean piercesEntities();
	
	void setPiercesEntities(boolean piercesEntities);
	
	boolean damageIgnoresSpeed();
	
	void setDamageIgnoresSpeed(boolean damageIgnoresSpeed);
	
	SoundEvent getHitSound();
	
	void setHitSound(SoundEvent hitSound);
	
	List<Effect> getHitEffects();
	
	void setHitEffects(List<Effect> hitEffects);

	boolean isInfinite(ItemStack projectile, ItemStack bow);
	
	IEntityAddedProjectile createProjectile(World world, ItemStack projectile, ItemStack bow, EntityLivingBase shooter);
	
	DamageSource getDamageSource(IEntityAddedProjectile projectileEntity, Entity shooter, ItemStack damageItem);
	
}
