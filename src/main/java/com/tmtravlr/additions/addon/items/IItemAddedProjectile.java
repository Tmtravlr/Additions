package com.tmtravlr.additions.addon.items;

import java.util.List;

import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.entities.IEntityAddedProjectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public interface IItemAddedProjectile {
	
	public float getBaseDamage();
	
	public void setBaseDamage(float baseDamage);
	
	public float getBasePunch();
	
	public void setBasePunch(float basePunch);
	
	public float getGravity();
	
	public void setGravity(float gravity);
	
	public boolean worksInWater();
	
	public void setWorksInWater(boolean worksInWater);
	
	public boolean sticksInGround();
	
	public void setSticksInGround(boolean sticksInGround);
	
	public boolean hasPotionEffects();
	
	public void setHasPotionEffects(boolean hasPotionEffects);
	
	public boolean worksWithInfinity();
	
	public void setWorksWithInfinity(boolean worksWithInfinity);
	
	public boolean renders3D();
	
	public void setRenders3D(boolean renders3D);
	
	public boolean piercesEntities();
	
	public void setPiercesEntities(boolean piercesEntities);
	
	public boolean damageIgnoresSpeed();
	
	public void setDamageIgnoresSpeed(boolean damageIgnoresSpeed);
	
	public SoundEvent getHitSound();
	
	public void setHitSound(SoundEvent hitSound);
	
	public List<Effect> getHitEffects();
	
	public void setHitEffects(List<Effect> hitEffects);

	public boolean isInfinite(ItemStack projectile, ItemStack bow);
	
	public IEntityAddedProjectile createProjectile(World world, ItemStack projectile, ItemStack bow, EntityLivingBase shooter);
	
	public DamageSource getDamageSource(IEntityAddedProjectile projectileEntity, Entity shooter, ItemStack damageItem);
	
}
