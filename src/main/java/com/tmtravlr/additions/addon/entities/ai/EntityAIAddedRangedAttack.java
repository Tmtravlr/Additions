package com.tmtravlr.additions.addon.entities.ai;

import com.tmtravlr.additions.addon.entities.EntityAddedProjectile;
import com.tmtravlr.additions.addon.entities.IEntityAddedProjectile;
import com.tmtravlr.additions.addon.items.ItemAddedBow;
import com.tmtravlr.additions.addon.items.ItemAddedGun;
import com.tmtravlr.additions.addon.items.ItemAddedThrowable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;

public class EntityAIAddedRangedAttack<T extends EntityLiving & IRangedAttackMob> extends EntityAIBase {

	public final T entity;
    public int rangedAttackTime;
    public double entityMoveSpeed;
    public int attackCooldown;
    public float maxAttackDistanceSquared;
    public int timeSeen;
    public int attackTime = -1;
    public boolean strafingClockwise;
    public boolean strafingBackwards;
    public int strafingTime = -1;

    public EntityAIAddedRangedAttack(T attackMob, double moveSpeedToSet, int attackCooldown, float maxDistanceToSet) {
        this.rangedAttackTime = -1;

        this.entity = attackMob;
        this.entityMoveSpeed = moveSpeedToSet;
        this.attackCooldown = attackCooldown;
        this.maxAttackDistanceSquared = maxDistanceToSet * maxDistanceToSet;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
    	return this.entity.getAttackTarget() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.entity.setSwingingArms(true);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.timeSeen = 0;
        this.attackTime = -1;
        this.entity.setSwingingArms(false);
        this.entity.resetActiveHand();
    }

    @Override
    public void updateTask() {
    	EntityLivingBase attackTarget = this.entity.getAttackTarget();
    	
    	if (attackTarget != null) {
	        double squareDistance = this.entity.getDistanceSq(attackTarget.posX, attackTarget.getEntityBoundingBox().minY, attackTarget.posZ);
	        boolean canSee = this.entity.getEntitySenses().canSee(attackTarget);
	
	        if (canSee) {
	            ++this.timeSeen;
	        } else {
	            --this.timeSeen;
	        }
	
	        if (squareDistance <= (double)this.maxAttackDistanceSquared && this.timeSeen >= 20) {
	            this.entity.getNavigator().clearPath();
                ++this.strafingTime;
	        } else {
	            this.entity.getNavigator().tryMoveToEntityLiving(attackTarget, this.entityMoveSpeed);
                this.strafingTime = -1;
	        }
	
	        this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
	        
	        ItemAddedBow bowItem = null;
	        ItemAddedThrowable throwableItem = null;
	        ItemAddedGun gunItem = null;
	    	ItemStack heldStack = this.entity.getHeldItem(EnumHand.MAIN_HAND);
	    	ItemStack possibleAmmoStack = this.entity.getHeldItem(EnumHand.OFF_HAND);
	        
	    	if (!heldStack.isEmpty()) {
	    		if (heldStack.getItem() instanceof ItemAddedBow) {
	    			bowItem = (ItemAddedBow) heldStack.getItem();
	    		} else if(heldStack.getItem() instanceof ItemAddedGun) {
	    			gunItem = (ItemAddedGun) heldStack.getItem();
	    		} else if(heldStack.getItem() instanceof ItemAddedThrowable) {
	    			throwableItem = (ItemAddedThrowable) heldStack.getItem();
	    		}
	    	}
	    	
	    	if (gunItem != null) {
	            this.entity.setSwingingArms(false);
	    	}
	        
	        float speedMultiplier = 1.0f;
	        
	        if (bowItem != null) {
	        	speedMultiplier = (float)bowItem.getDrawTime(heldStack) / 20f;
	        }
	        
	        if (gunItem != null) {
	        	speedMultiplier = (float)gunItem.getReloadTime(heldStack) / 20f;
	        }
	        
	        if (this.strafingTime >= 20) {
	            if ((double)this.entity.getRNG().nextFloat() < 0.3D) {
	                this.strafingClockwise = !this.strafingClockwise;
	            }
	
	            if ((double)this.entity.getRNG().nextFloat() < 0.3D) {
	                this.strafingBackwards = !this.strafingBackwards;
	            }
	
	            this.strafingTime = 0;
	        }
	
	        if (this.strafingTime > -1) {
	            if (squareDistance > (double)(this.maxAttackDistanceSquared * 0.75F)) {
	                this.strafingBackwards = false;
	            } else if (squareDistance < (double)(this.maxAttackDistanceSquared * 0.25F)) {
	                this.strafingBackwards = true;
	            }
	
	            this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
	            this.entity.faceEntity(entity, 30.0F, 30.0F);
	        } else {
	            this.entity.getLookHelper().setLookPositionWithEntity(entity, 30.0F, 30.0F);
	        }
	        
	        if ((bowItem == null && this.attackTime <= 0) || this.entity.isHandActive()) {
                if (!canSee && this.timeSeen < -60) {
                    this.entity.resetActiveHand();
                } else if (canSee) {
			        int attackCooldown = this.attackCooldown;
		                    
		            if (gunItem != null) {
		            	attackCooldown = gunItem.getReloadTime(heldStack);
		        	} else if (bowItem != null) {
		        		attackCooldown = bowItem.getDrawTime(heldStack);
		        	}
		            
                    int useTime = bowItem == null ? attackCooldown - this.attackTime : this.entity.getItemInUseMaxCount();

                    if (useTime >= attackCooldown) {
			            int arrowCount = 1;
			            float baseDamage = 0.0f;
			            SoundEvent shotSound = SoundEvents.ENTITY_SNOWBALL_THROW;
			            
			            if (throwableItem != null) {
			            	shotSound = throwableItem.throwSound;
			            }
			                        
			            if (bowItem != null) {
			            	arrowCount = Math.max(bowItem.shotCount, 1);
			            	baseDamage = bowItem.extraDamage;
			            	shotSound = bowItem.shotSound;
			            }
			            
			            if (gunItem != null) {
			            	arrowCount = Math.max(gunItem.shotCount, 1);
			            	baseDamage = gunItem.baseDamage;
			            	shotSound = gunItem.shotSound;
			            }
			            
			            float distanceRatio = MathHelper.sqrt(squareDistance) / this.maxAttackDistanceSquared;
			            float distanceFactor = MathHelper.clamp(distanceRatio, 0.1F, 1.0F);
		                double distanceX = attackTarget.posX - this.entity.posX;
		                double distanceY = attackTarget.posY + (double)attackTarget.getEyeHeight() - 1.100000023841858D - (this.entity.posY + (double)this.entity.getEyeHeight() - 0.10000000149011612D);
		                double distanceZ = attackTarget.posZ - this.entity.posZ;
		                double displacement = MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ) * 0.2;
			            
			            for (int i = 0; i < arrowCount; i++) {
			                
			            	if (throwableItem != null) {
			            		float gravity = throwableItem.gravity;
			            		EntityAddedProjectile thrown = throwableItem.createThrownEntity(this.entity.world, heldStack, ItemStack.EMPTY, this.entity);
			            		thrown.setProjectilePickupStatus(EntityArrow.PickupStatus.CREATIVE_ONLY);
			                	
			                    thrown.shoot(distanceX, distanceY + displacement * gravity / 0.05f, distanceZ, throwableItem.velocity, throwableItem.inaccuracy);
			                	this.entity.world.spawnEntity(thrown);
			            	} else if (bowItem != null) {
			            		ItemStack ammoStack = ItemStack.EMPTY;
			            		
			            		if (bowItem.isAmmo(possibleAmmoStack)) {
			            			ammoStack = possibleAmmoStack;
			            		} else {
			            			Item randomAmmoItem = bowItem.findInfinityAmmo(heldStack);
			            			
			            			if (randomAmmoItem != null) {
			            				ammoStack = new ItemStack(randomAmmoItem);
			            			}
			            		}
			            		
			            		if (!ammoStack.isEmpty()) {
			            			Entity projectile = bowItem.createProjectile(this.entity.world, heldStack, ammoStack, this.entity, distanceFactor, true);
			            			float gravity = 0.05f;
			            			
			            			if (projectile instanceof EntityAddedProjectile) {
					            		gravity = ((EntityAddedProjectile)projectile).getGravity();
			            			}
			            			
			            			if (projectile instanceof IEntityAddedProjectile) {
			            				((IEntityAddedProjectile)projectile).shoot(distanceX, distanceY + displacement * gravity / 0.05f, distanceZ, 1.6f, bowItem.scattering + 10 - this.entity.world.getDifficulty().getDifficultyId() * 3);
			            			} else if (projectile instanceof EntityArrow) {
			            				((EntityArrow)projectile).shoot(distanceX, distanceY + displacement * gravity / 0.05f, distanceZ, 1.6f, bowItem.scattering);
			            			}
			            			
			            			this.entity.world.spawnEntity(projectile);
			            		} else {
			            			return;
			            		}
			            	} else if (gunItem != null) {
			            		ItemStack ammoStack = ItemStack.EMPTY;
			            		
			            		if (gunItem.isAmmo(possibleAmmoStack)) {
			            			ammoStack = possibleAmmoStack;
			            		} else {
			            			Item randomAmmoItem = gunItem.findInfinityAmmo(heldStack);
			            			
			            			if (randomAmmoItem != null) {
			            				ammoStack = new ItemStack(randomAmmoItem);
			            			}
			            		}
			            		
			            		if (!ammoStack.isEmpty()) {
			            			Entity projectile = gunItem.createProjectile(this.entity.world, heldStack, ammoStack, this.entity, true);
			            			float gravity = 0.05f;
			            			
			            			if (projectile instanceof EntityAddedProjectile) {
					            		gravity = ((EntityAddedProjectile)projectile).getGravity();
			            			}
			            			
			            			if (projectile instanceof IEntityAddedProjectile) {
			            				((IEntityAddedProjectile)projectile).shoot(distanceX, distanceY + displacement * gravity / 0.05f, distanceZ, gunItem.shotVelocity * 1.6f, gunItem.scattering);
			            			} else if (projectile instanceof EntityArrow) {
			            				((EntityArrow)projectile).shoot(distanceX, distanceY + displacement * gravity / 0.05f, distanceZ, gunItem.shotVelocity * 1.6f, gunItem.scattering);
			            			}
			            			
			            			this.entity.world.spawnEntity(projectile);
			            		} else {
			            			return;
			            		}
			            	}
			            }
			            if (shotSound != null) {
			            	this.entity.world.playSound(null, this.entity.getPosition(), shotSound, SoundCategory.HOSTILE, 1.0F, 1.0F / (this.entity.getRNG().nextFloat() * 0.4F + 0.8F));
			            }
                        this.entity.resetActiveHand();
                        this.attackTime = MathHelper.floor(attackCooldown * (this.entity.world.getDifficulty() == EnumDifficulty.HARD ? 1.5 : 1));
			        }
                }
            } else if (--this.attackTime <= 0 && this.timeSeen >= -60) {
            	if (bowItem != null) {
            		this.entity.setActiveHand(EnumHand.MAIN_HAND);
            	}
            }
    	}
    }
    
    private void setRangedAttackTime(float speedMultiplier, float distance, ItemAddedGun gunItem, ItemAddedBow bowItem, ItemStack heldStack) {
    	if (gunItem != null) {
    		this.rangedAttackTime = gunItem.getReloadTime(heldStack);
    	} else if (bowItem != null) {
    		this.rangedAttackTime = bowItem.getDrawTime(heldStack);
    	} else {
    		this.rangedAttackTime = this.attackCooldown;
    	}
    }
}
