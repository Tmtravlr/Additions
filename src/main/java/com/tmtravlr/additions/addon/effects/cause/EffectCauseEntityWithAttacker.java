package com.tmtravlr.additions.addon.effects.cause;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.util.DamageTypeChecker;
import com.tmtravlr.additions.util.EntityCategoryChecker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Template for a cause that uses an entity with an attacker.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public abstract class EffectCauseEntityWithAttacker extends EffectCauseEntity {
	
	public boolean checkEntity;
	public boolean checkAttacker;
	public boolean checkDamageSource;
	public boolean targetSelf = false;

	public Set<ResourceLocation> attackerTypes = new HashSet<>();
	public Set<EntityCategoryChecker.EntityCategory> attackerCategories = new HashSet<>();
	public NBTTagCompound attackerTag;
	
	public Set<DamageTypeChecker.DamageType> damageTypes = new HashSet<>();
	public Set<DamageTypeChecker.DamageCategory> damageCategories = new HashSet<>();
	
	protected boolean entityMatches(@Nullable Entity attackerToCheck, Entity entityToCheck, DamageSource damageSource) {
		return this.attackerMatches(attackerToCheck) && this.entityMatches(entityToCheck) && this.damageSourceMatches(damageSource);
	}
	
	@Override
	protected boolean entityMatches(Entity entityToCheck) {
		if (this.checkEntity) {
			return super.entityMatches(entityToCheck);
		}
		
		return true;
	}

	protected boolean attackerMatches(Entity attackerToCheck) {
		if (this.checkAttacker) {
			if (attackerToCheck == null) {
				return false;
			}
			
			for (ResourceLocation entityType : this.attackerTypes) {
				if (PLAYER_ENTITY.equals(entityType) && attackerToCheck instanceof EntityPlayer) {
					return true;
				}
				
				if (EntityList.isMatchingName(attackerToCheck, entityType)) {
					return true;
				}
			}
			
			for (EntityCategoryChecker.EntityCategory entityCategory : this.attackerCategories) {
				if (EntityCategoryChecker.entityMatches(attackerToCheck, entityCategory)) {
					return true;
				}
			}
			
			if (this.attackerTag != null && !this.attackerTag.hasNoTags()) {
				NBTTagCompound tagToCheck = attackerToCheck.writeToNBT(new NBTTagCompound());
				
				if (NBTUtil.areNBTEquals(this.attackerTag, tagToCheck, true)) {
					return true;
				}
			}
			
			return false;
		}
		
		return true;
	}
	
	protected boolean damageSourceMatches(DamageSource damageSource) {
		if (this.checkDamageSource) {
			for (DamageTypeChecker.DamageType damageType : this.damageTypes) {
				if (DamageTypeChecker.damageMatches(damageSource, damageType)) {
					return true;
				}
			}
			
			for (DamageTypeChecker.DamageCategory damageCategory : this.damageCategories) {
				if (DamageTypeChecker.damageMatches(damageSource, damageCategory)) {
					return true;
				}
			}
			
			return false;
		}
		
		return true;
	}

	public static class Serializer {
		
		public static JsonObject serialize(EffectCauseEntityWithAttacker effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if (!effectCause.entityTypes.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				effectCause.entityTypes.forEach(type -> jsonArray.add(type.toString()));
				
				json.add("entity_types", jsonArray);
			}
			
			if (!effectCause.entityCategories.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				effectCause.entityCategories.forEach(category -> jsonArray.add(category.toString()));
				
				json.add("entity_categories", jsonArray);
			}
			
			if (effectCause.entityTag != null && !effectCause.entityTag.hasNoTags()) {
				json.addProperty("entity_tag", effectCause.entityTag.toString());
			}
			
			if (!effectCause.attackerTypes.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				effectCause.attackerTypes.forEach(type -> jsonArray.add(type.toString()));
				
				json.add("attacker_types", jsonArray);
			}
			
			if (!effectCause.attackerCategories.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				effectCause.attackerCategories.forEach(category -> jsonArray.add(category.toString()));
				
				json.add("attacker_categories", jsonArray);
			}
			
			if (effectCause.attackerTag != null && !effectCause.attackerTag.hasNoTags()) {
				json.addProperty("attacker_tag", effectCause.attackerTag.toString());
			}
			
			if (!effectCause.damageTypes.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				effectCause.damageTypes.forEach(type -> jsonArray.add(type.toString()));
				
				json.add("damage_types", jsonArray);
			}
			
			if (!effectCause.damageCategories.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				effectCause.damageCategories.forEach(category -> jsonArray.add(category.toString()));
				
				json.add("damage_categories", jsonArray);
			}
			
			if (effectCause.checkEntity) {
				json.addProperty("check_entity", true);
			}
			
			if (effectCause.checkAttacker) {
				json.addProperty("check_attacker", true);
			}
			
			if (effectCause.checkDamageSource) {
				json.addProperty("check_damage_source", true);
			}
			
			if (effectCause.targetSelf) {
				json.addProperty("target_self", true);
			}
			
			return json;
		}
		
		public static void deserialize(EffectCauseEntityWithAttacker effectCause, JsonObject json, JsonDeserializationContext context) {
			if (JsonUtils.isJsonArray(json, "entity_types")) {
				effectCause.entityTypes = new HashSet<>();
				
				JsonUtils.getJsonArray(json, "entity_types").forEach(jsonElement -> {
					effectCause.entityTypes.add(new ResourceLocation(JsonUtils.getString(jsonElement, "member of entity_types")));
				});
			}
			
			if (JsonUtils.isJsonArray(json, "entity_categories")) {
				effectCause.entityCategories = new HashSet<>();
				
				JsonUtils.getJsonArray(json, "entity_categories").forEach(jsonElement -> {
					String categoryName = JsonUtils.getString(jsonElement, "member of entity_categories");
					
					try {
						effectCause.entityCategories.add(EntityCategoryChecker.EntityCategory.valueOf(categoryName.toUpperCase()));
					} catch (IllegalArgumentException e) {
						throw new JsonSyntaxException("Unknown entity category '" + categoryName + "' in entity_categories.", e);
					}
				});
			}
			
			if (JsonUtils.isString(json, "entity_tag")) {
				try {
					effectCause.entityTag = JsonToNBT.getTagFromJson(JsonUtils.getString(json, "entity_tag"));
                } catch (NBTException nbtexception) {
                    throw new JsonSyntaxException(nbtexception);
                }
			}
			
			if (JsonUtils.isJsonArray(json, "attacker_types")) {
				effectCause.attackerTypes = new HashSet<>();
				
				JsonUtils.getJsonArray(json, "attacker_types").forEach(jsonElement -> {
					effectCause.attackerTypes.add(new ResourceLocation(JsonUtils.getString(jsonElement, "member of attacker_types")));
				});
			}
			
			if (JsonUtils.isJsonArray(json, "attacker_categories")) {
				effectCause.attackerCategories = new HashSet<>();
				
				JsonUtils.getJsonArray(json, "attacker_categories").forEach(jsonElement -> {
					String categoryName = JsonUtils.getString(jsonElement, "member of attacker_categories");
					
					try {
						effectCause.attackerCategories.add(EntityCategoryChecker.EntityCategory.valueOf(categoryName.toUpperCase()));
					} catch (IllegalArgumentException e) {
						throw new JsonSyntaxException("Unknown entity category '" + categoryName + "' in attacker_categories.", e);
					}
				});
			}
			
			if (JsonUtils.isString(json, "attacker_tag")) {
				try {
					effectCause.attackerTag = JsonToNBT.getTagFromJson(JsonUtils.getString(json, "attacker_tag"));
                } catch (NBTException nbtexception) {
                    throw new JsonSyntaxException(nbtexception);
                }
			}
			
			if (JsonUtils.isJsonArray(json, "damage_types")) {
				effectCause.attackerCategories = new HashSet<>();
				
				JsonUtils.getJsonArray(json, "damage_types").forEach(jsonElement -> {
					String typeName = JsonUtils.getString(jsonElement, "member of damage_types");
					
					try {
						effectCause.damageTypes.add(DamageTypeChecker.DamageType.valueOf(typeName.toUpperCase()));
					} catch (IllegalArgumentException e) {
						throw new JsonSyntaxException("Unknown damage type '" + typeName + "' in damage_types.", e);
					}
				});
			}
			
			if (JsonUtils.isJsonArray(json, "damage_categories")) {
				effectCause.attackerCategories = new HashSet<>();
				
				JsonUtils.getJsonArray(json, "damage_categories").forEach(jsonElement -> {
					String categoryName = JsonUtils.getString(jsonElement, "member of damage_categories");
					
					try {
						effectCause.damageCategories.add(DamageTypeChecker.DamageCategory.valueOf(categoryName.toUpperCase()));
					} catch (IllegalArgumentException e) {
						throw new JsonSyntaxException("Unknown damage category '" + categoryName + "' in damage_categories.", e);
					}
				});
			}
			
			effectCause.checkEntity = JsonUtils.getBoolean(json, "check_entity", false);
			effectCause.checkAttacker = JsonUtils.getBoolean(json, "check_attacker", false);
			effectCause.checkDamageSource = JsonUtils.getBoolean(json, "check_damage_source", false);
			effectCause.targetSelf = JsonUtils.getBoolean(json, "target_self", false);
		}
    }
}
