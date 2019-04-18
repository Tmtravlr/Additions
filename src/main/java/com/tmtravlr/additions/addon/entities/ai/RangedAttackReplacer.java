package com.tmtravlr.additions.addon.entities.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.tmtravlr.additions.addon.items.ItemAddedBow;
import com.tmtravlr.additions.addon.items.ItemAddedGun;
import com.tmtravlr.additions.addon.items.ItemAddedThrowable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class RangedAttackReplacer {
	
	public static HashMap<EntityLiving, EntityAIAddedRangedAttack> replacedAIs = new HashMap<>();
	public static HashMap<EntityLiving, EntityAIBase> originalAIs = new HashMap<>();
	public static ArrayList<EntityLiving> delayedMobs = new ArrayList<>();
	
	public static <T extends EntityLiving & IRangedAttackMob> void checkIfAINeedsReplacing(T mob, ItemStack heldStack) {
		if (!heldStack.isEmpty() && (heldStack.getItem() instanceof ItemAddedBow || heldStack.getItem() instanceof ItemAddedGun || heldStack.getItem() instanceof ItemAddedThrowable)) {
			if (!replacedAIs.containsKey(mob) && !delayedMobs.contains(mob)) {
				delayedMobs.add(mob);
			}
		}
		else {
			if (replacedAIs.containsKey(mob)) {
				removeAddedRangedAIFromMob(mob);
			}
		}
	}
	
	public static void cleanupDeadMobs() {
		for (EntityLiving living : delayedMobs) {
			if (living instanceof IRangedAttackMob && !living.isDead) {
				addAddedRangedAIToMob((EntityLiving & IRangedAttackMob) living);
			}
		}
		
		delayedMobs.clear();
	
		if (FMLCommonHandler.instance().getMinecraftServerInstance().getTickCounter() % 13 == 0 && !replacedAIs.isEmpty()) {
			Iterator<Entry<EntityLiving, EntityAIAddedRangedAttack>> it = replacedAIs.entrySet().iterator();
			
			while (it.hasNext()) {
				Entry<EntityLiving, EntityAIAddedRangedAttack> entry = it.next();
				if (entry.getKey() == null || entry.getKey().isDead) {
					it.remove();
					originalAIs.remove(entry.getKey());
				}
			}
		}
	}
	
	private static <T extends EntityLiving & IRangedAttackMob> void addAddedRangedAIToMob(T mob) {
		int priority = 2;
		EntityAIBase originalTask = null;
		
        for (EntityAITasks.EntityAITaskEntry entry : mob.tasks.taskEntries) {
            if (entry.action instanceof EntityAIAttackRanged || entry.action instanceof EntityAIAttackRangedBow || entry.action instanceof EntityAIAttackMelee) {
            	priority = entry.priority;
            	originalTask = entry.action;
            	break;
            }
        }
        
        EntityAIAddedRangedAttack aiRanged = new EntityAIAddedRangedAttack(mob, 1.0D, 20, 15.0F);
        
        if (originalTask instanceof EntityAIAttackRanged) {
        	EntityAIAttackRanged rangedTask = (EntityAIAttackRanged) originalTask;
        	
        	aiRanged.entityMoveSpeed = ObfuscationReflectionHelper.getPrivateValue(EntityAIAttackRanged.class, rangedTask, "field_75321_e", "entityMoveSpeed");
        	aiRanged.attackCooldown = ObfuscationReflectionHelper.getPrivateValue(EntityAIAttackRanged.class, rangedTask, "field_96561_g", "attackIntervalMin");
        	aiRanged.maxAttackDistanceSquared = ObfuscationReflectionHelper.getPrivateValue(EntityAIAttackRanged.class, rangedTask, "field_82642_h", "maxAttackDistance");
        } else if (originalTask instanceof EntityAIAttackRangedBow) {
        	EntityAIAttackRangedBow rangedBowTask = (EntityAIAttackRangedBow) originalTask;
        	
        	aiRanged.entityMoveSpeed = ObfuscationReflectionHelper.getPrivateValue(EntityAIAttackRangedBow.class, rangedBowTask, "field_188500_b", "moveSpeedAmp");
        	aiRanged.attackCooldown = ObfuscationReflectionHelper.getPrivateValue(EntityAIAttackRangedBow.class, rangedBowTask, "field_188501_c", "attackCooldown");
        	aiRanged.maxAttackDistanceSquared = ObfuscationReflectionHelper.getPrivateValue(EntityAIAttackRangedBow.class, rangedBowTask, "field_188502_d", "maxAttackDistance");
        } else if (originalTask instanceof EntityAIAttackMelee) {
        	EntityAIAttackMelee meleeTask = (EntityAIAttackMelee) originalTask;
        	
        	aiRanged.entityMoveSpeed = ObfuscationReflectionHelper.getPrivateValue(EntityAIAttackMelee.class, meleeTask, "field_75440_e", "speedTowardsTarget");
        }
        
        if (originalTask != null) {
        	mob.tasks.removeTask(originalTask);
        	originalAIs.put(mob, originalTask);
        }
		
		replacedAIs.put(mob, aiRanged);
		mob.tasks.addTask(priority, aiRanged);
	}
	
	private static void removeAddedRangedAIFromMob(EntityLiving mob) {
		int priority = 2;
		EntityAIBase replacedTask = replacedAIs.get(mob);
		
		if (replacedTask != null) {
			for (EntityAITasks.EntityAITaskEntry entry : mob.tasks.taskEntries) {
	            
	            if(entry.action == replacedTask) {
	            	priority = entry.priority;
	            	break;
	            }
	        }
			mob.tasks.removeTask(replacedTask);
			replacedAIs.remove(mob);
		}
        
        if (originalAIs.containsKey(mob)) {
        	mob.tasks.addTask(priority, originalAIs.get(mob));
        	originalAIs.remove(mob);
        }
	}
}
