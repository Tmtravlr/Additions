package com.tmtravlr.additions.util;

import java.util.function.Function;

import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

/**
 * Convenience utility for checking a damage source's damage type or category.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since June 2021
 */
public class DamageTypeChecker {
	
	public static boolean damageMatches(DamageSource source, DamageType type) {
		return source == type.damageSource;
	}
	
	public static boolean damageMatches(DamageSource source, DamageCategory category) {
		return category.matchCheck.apply(source);
	}
	
	public static enum DamageType {
		
		IN_FIRE(DamageSource.IN_FIRE),
	    LIGHTNING_BOLT(DamageSource.LIGHTNING_BOLT),
	    ON_FIRE(DamageSource.ON_FIRE),
	    LAVA(DamageSource.LAVA),
	    HOT_FLOOR(DamageSource.HOT_FLOOR),
	    IN_WALL(DamageSource.IN_WALL),
	    CRAMMING(DamageSource.CRAMMING),
	    DROWN(DamageSource.DROWN),
	    STARVE(DamageSource.STARVE),
	    CACTUS(DamageSource.CACTUS),
	    FALL(DamageSource.FALL),
	    FLY_INTO_WALL(DamageSource.FLY_INTO_WALL),
	    OUT_OF_WORLD(DamageSource.OUT_OF_WORLD),
	    GENERIC(DamageSource.GENERIC),
	    MAGIC(DamageSource.MAGIC),
	    WITHER(DamageSource.WITHER),
	    ANVIL(DamageSource.ANVIL),
	    FALLING_BLOCK(DamageSource.FALLING_BLOCK),
	    DRAGON_BREATH(DamageSource.DRAGON_BREATH),
	    FIREWORKS(DamageSource.FIREWORKS);
		
		public DamageSource damageSource;
		
		DamageType(DamageSource damageSource) {
			this.damageSource = damageSource;
		}
	}
	
	public static enum DamageCategory {
		
		PROJECTILE(damageSource -> damageSource.isProjectile()),
		MAGIC(damageSource -> damageSource.isMagicDamage()),
		FIRE(damageSource -> damageSource.isFireDamage()),
		EXPLOSION(damageSource -> damageSource.isExplosion()),
		DIFFICULTY_SCALED(damageSource -> damageSource.isDifficultyScaled()),
		ABSOLUTE(damageSource -> damageSource.isDamageAbsolute()),
		UNBLOCKABLE(damageSource -> damageSource.isUnblockable()),
		HURTS_CREATIVE(damageSource -> damageSource.isCreativePlayer()),
		THORNS(damageSource -> damageSource instanceof EntityDamageSource && ((EntityDamageSource)damageSource).getIsThornsDamage());
		
		public Function<DamageSource, Boolean> matchCheck;
		
		DamageCategory(Function<DamageSource, Boolean> matchCheck) {
			this.matchCheck = matchCheck;
		}
	}
}
