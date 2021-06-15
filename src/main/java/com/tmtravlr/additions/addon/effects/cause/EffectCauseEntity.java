package com.tmtravlr.additions.addon.effects.cause;

import java.util.HashSet;
import java.util.Set;

import com.tmtravlr.additions.util.EntityCategoryChecker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;

/**
 * Template for a cause that uses an entity.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public abstract class EffectCauseEntity extends EffectCause {
	
	public static final ResourceLocation PLAYER_ENTITY = new ResourceLocation("minecraft", "player");

	public Set<ResourceLocation> entityTypes = new HashSet<>();
	public Set<EntityCategoryChecker.EntityCategory> entityCategories = new HashSet<>();
	public NBTTagCompound entityTag;
	
	protected boolean entityMatches(Entity entityToCheck) {
		if (entityTypes.isEmpty() && entityCategories.isEmpty() && (this.entityTag == null || this.entityTag.hasNoTags())) {
			return true;
		}
		
		for (ResourceLocation entityType : entityTypes) {
			if (PLAYER_ENTITY.equals(entityType) && entityToCheck instanceof EntityPlayer) {
				return true;
			}
			
			if (EntityList.isMatchingName(entityToCheck, entityType)) {
				return true;
			}
		}
		
		for (EntityCategoryChecker.EntityCategory entityCategory : entityCategories) {
			if (EntityCategoryChecker.entityMatches(entityToCheck, entityCategory)) {
				return true;
			}
		}
		
		if (this.entityTag != null && !this.entityTag.hasNoTags()) {
			NBTTagCompound tagToCheck = entityToCheck.writeToNBT(new NBTTagCompound());
			
			if (NBTUtil.areNBTEquals(this.entityTag, tagToCheck, true)) {
				return true;
			}
		}
		
		return false;
	}

}
