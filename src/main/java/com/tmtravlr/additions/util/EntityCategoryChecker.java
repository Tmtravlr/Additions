package com.tmtravlr.additions.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;

/**
 * Convenience utility for checking an entity's category.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since June 2021
 */
public class EntityCategoryChecker {
	
	public static boolean entityMatches(Entity entity, EntityCategory category) {
		if (category.getCreatureAttribute() != null && entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getCreatureAttribute() == category.getCreatureAttribute()) {
			return true;
		} else if (category == EntityCategory.MONSTER && entity instanceof IMob) {
			return true;
		} else if (category == EntityCategory.ANIMAL && entity instanceof EntityAnimal) {
			return true;
		} else if (category == EntityCategory.AMBIENT && (entity instanceof EntityAmbientCreature || entity instanceof EntitySquid)) {
			return true;
		} else if (category == EntityCategory.NPC && entity instanceof INpc) {
			return true;
		} else if (category == EntityCategory.GOLEM && entity instanceof EntityGolem) {
			return true;
		} else if (category == EntityCategory.ZOMBIE && entity instanceof EntityZombie) {
			return true;
		} else if (category == EntityCategory.SKELETON && entity instanceof AbstractSkeleton) {
			return true;
		} else if (category == EntityCategory.CREEPER && entity instanceof EntityCreeper) {
			return true;
		} else if (category == EntityCategory.SPIDER && entity instanceof EntitySpider) {
			return true;
		} else if (category == EntityCategory.SILVERFISH && entity instanceof EntitySilverfish) {
			return true;
		} else if (category == EntityCategory.WITCH && entity instanceof EntityWitch) {
			return true;
		} else if (category == EntityCategory.ENDERMAN && entity instanceof EntityEnderman) {
			return true;
		} else if (category == EntityCategory.GHAST && entity instanceof EntityGhast) {
			return true;
		} else if (category == EntityCategory.BLAZE && entity instanceof EntityBlaze) {
			return true;
		}
		
		return false;
	}
	
	public static enum EntityCategory {
		MONSTER,
		ANIMAL,
		AMBIENT,
		NPC,
		GOLEM,
		ZOMBIE,
		SKELETON,
		CREEPER,
		SPIDER,
		WITCH,
		SILVERFISH,
		GHAST,
		BLAZE,
		ENDERMAN,
		UNDEAD(EnumCreatureAttribute.UNDEAD),
		ARTHROPOD(EnumCreatureAttribute.ARTHROPOD),
		ILLAGER(EnumCreatureAttribute.ILLAGER);
		
		private EnumCreatureAttribute creatureAttribute;
		
		private EntityCategory() {
			this(null);
		}
		
		private EntityCategory(EnumCreatureAttribute creatureAttribute) {
			this.creatureAttribute = creatureAttribute;
		}
		
		public EnumCreatureAttribute getCreatureAttribute() {
			return this.creatureAttribute;
		}
	}
}
