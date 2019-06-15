package com.tmtravlr.additions.addon.entities;

import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.items.IItemAddedProjectile;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * An entity for the throwable item
 * @author Rebeca Rey (Tmtravlr)
 * @since November 2018
 */
public class EntityAddedProjectile extends EntityArrow implements IEntityAddedProjectile {

    private static final DataParameter<ItemStack> PROJECTILE_ITEM = EntityDataManager.<ItemStack>createKey(EntityAddedProjectile.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Float> GRAVITY = EntityDataManager.<Float>createKey(EntityAddedProjectile.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> WORKS_IN_WATER = EntityDataManager.<Boolean>createKey(EntityAddedProjectile.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STICKS_IN_GROUND = EntityDataManager.<Boolean>createKey(EntityAddedProjectile.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RENDERS_3D = EntityDataManager.<Boolean>createKey(EntityAddedProjectile.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCES_ENTITIES = EntityDataManager.<Boolean>createKey(EntityAddedProjectile.class, DataSerializers.BOOLEAN);
	
    public float baseDamage = 0;
	public float basePunch = 0;
	public boolean hasPotionEffects = false;
	public boolean damageIgnoresSpeed = false;
	public SoundEvent hitSound = null;
	
	private int knockbackStrength;
	private ItemStack bowStack = ItemStack.EMPTY;
	private int timeInAir = 0;

	public EntityAddedProjectile(World world) {
		super(world);
	}

	public EntityAddedProjectile(World world, EntityLivingBase entity) {
		this(world, entity, ItemStack.EMPTY);
	}

	public EntityAddedProjectile(World world, EntityLivingBase entity, ItemStack thrown) {
		super(world, entity);
		this.setArrowStack(thrown);
	}

	public EntityAddedProjectile(World world, double x, double y, double z) {
		super(world, x, y, z);
	}
	
	@Override
	public void setKnockbackStrength(int knockbackStrength) {
		super.setKnockbackStrength(knockbackStrength);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		
		this.getDataManager().register(PROJECTILE_ITEM, ItemStack.EMPTY);
        this.getDataManager().register(GRAVITY, 0.05f);
        this.getDataManager().register(WORKS_IN_WATER, false);
        this.getDataManager().register(STICKS_IN_GROUND, false);
        this.getDataManager().register(RENDERS_3D, false);
        this.getDataManager().register(PIERCES_ENTITIES, false);
	}

	@Override
    protected void onHit(RayTraceResult result) {
		Entity entity = result.entityHit;
		List<Effect> collisionEffects = this.getCollisionEffects();
		
		if (entity != null) {
			if (!collisionEffects.isEmpty()) {
				collisionEffects.forEach(effect -> effect.affectEntity(this.shootingEntity, entity));
			}
			
			float motionMultiplier = this.damageIgnoresSpeed ? 1 : MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			float damage = motionMultiplier * Math.max(this.baseDamage + (float)this.getDamage(), 0);

            if (this.getIsCritical()) {
                damage += this.rand.nextDouble() * damage - damage / 2;
            }
			
			DamageSource damageSource;
			ItemStack damageStack = this.bowStack.isEmpty() ? this.getArrowStack() : this.bowStack;
			if (this.getArrowStack().getItem() instanceof IItemAddedProjectile) {
				damageSource = ((IItemAddedProjectile)this.getArrowStack().getItem()).getDamageSource(this, this.shootingEntity, damageStack);
			} else {
				damageSource = new EntityDamageSourceIndirect("additions.thrown", this, this.shootingEntity) {
					
					@Override
					public ITextComponent getDeathMessage(EntityLivingBase entity) {
						
				        if (this.getTrueSource() == null) {
				        	return new TextComponentTranslation("death.attack." + this.damageType, entity.getDisplayName(), damageStack.getTextComponent());
				        } else {
				        	return new TextComponentTranslation("death.attack." + this.damageType + ".player", entity.getDisplayName(), this.getTrueSource().getDisplayName(), damageStack.getTextComponent());
				        }
				    }
					
				}.setProjectile();
			}
			
			if (entity.attackEntityFrom(damageSource, damage)) {
				
				if (entity instanceof EntityLivingBase) {
					EntityLivingBase entityLiving = (EntityLivingBase) entity;

	                if (!this.world.isRemote && this.getArrowStack().getItem() instanceof ItemArrow) {
	                	entityLiving.setArrowCountInEntity(entityLiving.getArrowCountInEntity() + 1);
	                }
				
					float punch = this.basePunch + this.knockbackStrength;
					
					if (punch > 0) {
		                float force = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		
		                if (force > 0.0F) {
		                	entityLiving.addVelocity(this.motionX * (double)punch * 0.6000000238418579D / (double)force, 0.1D, this.motionZ * (double)punch * 0.6000000238418579D / (double)force);
		                }
		            }
		
		            if (this.isBurning() && !(entityLiving instanceof EntityEnderman)) {
		            	entityLiving.setFire(5);
		            }
		
		            if (this.shootingEntity instanceof EntityLivingBase) {
		                EnchantmentHelper.applyThornEnchantments(entityLiving, this.shootingEntity);
		                EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entityLiving);
		            }
	            
		            if (this.hasPotionEffects) {
		            	for (PotionEffect effect : PotionUtils.getEffectsFromStack(this.getArrowStack())) {
		                    entityLiving.addPotionEffect(new PotionEffect(effect.getPotion(), MathHelper.ceil(effect.getDuration() * 0.125), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
		            	}
		            }
				}
	
				if (!this.getPiercesEntities() && !this.world.isRemote) {
					
					if (!(entity instanceof EntityEnderman)) {
						this.doCollisionSpecialEffects();
						this.setDead();
					}
				}
			} else {
				if (!this.getSticksInGround()) {
					if (!this.world.isRemote) {
						this.doCollisionSpecialEffects();
						this.setDead();
					}
				} else if (!this.getPiercesEntities()) {
					this.motionX *= -0.10000000149011612D;
	                this.motionY *= -0.10000000149011612D;
	                this.motionZ *= -0.10000000149011612D;
	                this.rotationYaw += 180.0F;
	                this.prevRotationYaw += 180.0F;
	                ObfuscationReflectionHelper.setPrivateValue(EntityArrow.class, this, 0, "field_70257_an", "ticksInAir");

	                if (this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D) {
	                    if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED) {
	                        this.entityDropItem(this.getArrowStack(), 0.1F);
	                    }

	                    if (!this.world.isRemote) {
	                    	this.setDead();
	                    }
	                }
				}
			}
		} else { 
			if (!this.world.isRemote && !collisionEffects.isEmpty()) {
				collisionEffects.forEach(effect -> effect.affectBlock(this.shootingEntity, this.world, result.getBlockPos()));
			}
			
			if (this.getSticksInGround()) {
	            this.collideWithBlock(result);
			} else if (!this.world.isRemote) {
				this.doCollisionSpecialEffects();
				this.setDead();
			}
        }
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (!this.inGround) {
	        if (this.getWorksInWater() && this.isInWater()) {
	        	double multiplier = 1.65;
	        	 this.motionX *= multiplier;
	             this.motionY *= multiplier;
	             this.motionZ *= multiplier;
	        }
	        
	        if (this.getGravity() != 0.05000000074505806) {
	        	this.motionY += 0.05000000074505806 - this.getGravity();
	        }
	        
	        if ((this.getGravity() <= 0 && this.posY > 300) || ++this.timeInAir > 1200) {
	        	this.setDead();
	        }
	        
	        if (this.getGravity() == 0) {
	        	double multiplier = 1.0 / 0.99;
	            this.motionX *= multiplier;
	            this.motionY *= multiplier;
	            this.motionZ *= multiplier;
	        }
		} else {
			this.timeInAir = 0;
		}
	}

	@Override
    public ItemStack getArrowStack() {
        return this.getDataManager().get(PROJECTILE_ITEM);
    }
	
	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		super.shoot(x, y, z, velocity, inaccuracy);

        float horizontalDistance = MathHelper.sqrt(x * x + z * z);
        this.rotationPitch = (float)(MathHelper.atan2(y, (double)horizontalDistance) * (180D / Math.PI));
	}
	
	@Override
	public void shootProjectile(Entity shooter, float pitch, float yaw, float rotationOffset, float velocity, float inaccuracy) {
		this.shoot(shooter, pitch, yaw, rotationOffset, velocity, inaccuracy);
	}
	
	@Override
	public void setIsProjectileCritical(boolean critical) {
		this.setIsCritical(critical);
	}
	
	@Override
	public double getProjectileDamage() {
		return this.getDamage();
	}
	
	@Override
	public void setProjectileDamage(double damage) {
		this.setDamage(damage);
	}
	
	@Override
	public void setProjectileKnockback(int knockbackStrength) {
		this.setKnockbackStrength(knockbackStrength);
	}
	
	@Override
	public void setProjectileFire(int seconds) {
		this.setFire(seconds);
	}
	
	@Override
	public void setProjectilePickupStatus(EntityArrow.PickupStatus status) {
		this.pickupStatus = status;
	}
	
	public void setBowStack(ItemStack bowStack) {
		this.bowStack = bowStack;
	}
	
	public void setGravity(float gravity) {
		this.getDataManager().set(GRAVITY, gravity);
	}
	
	public float getGravity() {
        return this.getDataManager().get(GRAVITY);
	}
	
	public void setWorksInWater(boolean worksInWater) {
		this.getDataManager().set(WORKS_IN_WATER, worksInWater);
	}
	
	public boolean getWorksInWater() {
        return this.getDataManager().get(WORKS_IN_WATER);
	}
	
	public void setSticksInGround(boolean sticksInGround) {
		this.getDataManager().set(STICKS_IN_GROUND, sticksInGround);
	}
	
	public boolean getSticksInGround() {
        return this.getDataManager().get(STICKS_IN_GROUND);
	}
	
	public void setRenders3D(boolean renders3D) {
		this.getDataManager().set(RENDERS_3D, renders3D);
	}
	
	public boolean getRenders3D() {
        return this.getDataManager().get(RENDERS_3D);
	}
	
	public void setPiercesEntities(boolean piercesEntities) {
		this.getDataManager().set(PIERCES_ENTITIES, piercesEntities);
	}
	
	public boolean getPiercesEntities() {
        return this.getDataManager().get(PIERCES_ENTITIES);
	}

    public void setArrowStack(ItemStack stack) {
        this.getDataManager().set(PROJECTILE_ITEM, stack);
        this.getDataManager().setDirty(PROJECTILE_ITEM);
        
        if (stack.getItem() instanceof IItemAddedProjectile) {
        	IItemAddedProjectile throwable = (IItemAddedProjectile) stack.getItem();
        	this.baseDamage = throwable.getBaseDamage();
        	this.setGravity(throwable.getGravity());
        	this.basePunch = throwable.getBasePunch();
        	this.setWorksInWater(throwable.worksInWater());
        	this.setSticksInGround(throwable.sticksInGround());
        	this.hasPotionEffects = throwable.hasPotionEffects();
        	this.setRenders3D(throwable.renders3D());
        	this.setPiercesEntities(throwable.piercesEntities());
        	this.damageIgnoresSpeed = throwable.damageIgnoresSpeed();
        	this.hitSound = throwable.getHitSound();
        }
    }
    
    private List<Effect> getCollisionEffects() {
    	ItemStack projectileItem = this.getArrowStack();
    	
    	if (projectileItem.getItem() instanceof IItemAddedProjectile) {
    		return ((IItemAddedProjectile)projectileItem.getItem()).getHitEffects();
    	}
    	
    	return new ArrayList<>();
    }
	
	private void doCollisionSpecialEffects() {
		if (!this.world.isRemote) {
			if (this.world instanceof WorldServer) {
				((WorldServer)this.world).spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, 8, 0, 0, 0, ((double)this.rand.nextFloat() - 0.5D) * 0.2D, Item.getIdFromItem(this.getArrowStack().getItem()), this.getArrowStack().getItemDamage());
			}
			
	         if (this.hitSound != null) {
	        	this.world.playSound(null, this.posX, this.posY, this.posZ, this.hitSound, SoundCategory.NEUTRAL, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	        }
		}
	}
	
	private void collideWithBlock(RayTraceResult result) {
		BlockPos pos = result.getBlockPos();
        ObfuscationReflectionHelper.setPrivateValue(EntityArrow.class, this, pos.getX(), "field_145791_d", "xTile");
        ObfuscationReflectionHelper.setPrivateValue(EntityArrow.class, this, pos.getY(), "field_145792_e", "yTile");
        ObfuscationReflectionHelper.setPrivateValue(EntityArrow.class, this, pos.getZ(), "field_145789_f", "zTile");
        
        IBlockState state = this.world.getBlockState(pos);
        Block block = state.getBlock();
        ObfuscationReflectionHelper.setPrivateValue(EntityArrow.class, this, block, "field_145790_g", "inTile");
        ObfuscationReflectionHelper.setPrivateValue(EntityArrow.class, this, block.getMetaFromState(state), "field_70253_h", "inData");
        
        this.motionX = (double)((float)(result.hitVec.x - this.posX));
        this.motionY = (double)((float)(result.hitVec.y - this.posY));
        this.motionZ = (double)((float)(result.hitVec.z - this.posZ));
        
        float motion = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.posX -= this.motionX / (double)motion * 0.05000000074505806D;
        this.posY -= this.motionY / (double)motion * 0.05000000074505806D;
        this.posZ -= this.motionZ / (double)motion * 0.05000000074505806D;
        
        this.inGround = true;
        this.arrowShake = 7;
        this.setIsCritical(false);
    	this.world.playSound(null, this.posX, this.posY, this.posZ, this.hitSound, SoundCategory.NEUTRAL, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        
        if (state.getMaterial() != Material.AIR) {
        	block.onEntityCollidedWithBlock(this.world, pos, state, this);
        }
	}

	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		
		super.writeEntityToNBT(tagCompound);
		
		tagCompound.setFloat("BaseDamage", this.baseDamage);
		tagCompound.setFloat("Gravity", this.getGravity());
		tagCompound.setFloat("BasePunch", this.basePunch);
		tagCompound.setBoolean("WorksInWater", this.getWorksInWater());
		tagCompound.setBoolean("SticksInGround", this.getSticksInGround());
		tagCompound.setBoolean("HasPotionEffects", this.hasPotionEffects);
		tagCompound.setBoolean("Renders3D", this.getRenders3D());
		tagCompound.setBoolean("PiercesEntities", this.getPiercesEntities());
		tagCompound.setBoolean("DamageIgnoresSpeed", this.damageIgnoresSpeed);
		
		if (this.hitSound != null) {
			tagCompound.setString("HitSound", this.hitSound.getSoundName().toString());
		}
		
		ItemStack projectile = this.getArrowStack();
		if (!projectile.isEmpty()) {
			NBTTagCompound projectileTag = new NBTTagCompound();
			projectile.writeToNBT(projectileTag);
			tagCompound.setTag("Projectile", projectileTag);
		}
		
		if (!this.bowStack.isEmpty()) {
			NBTTagCompound bowTag = new NBTTagCompound();
			this.bowStack.writeToNBT(bowTag);
			tagCompound.setTag("Bow", bowTag);
		}
		
	}

	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		
		super.readEntityFromNBT(tagCompound);
		
		this.baseDamage = tagCompound.getFloat("BaseDamage");
		this.setGravity(tagCompound.getFloat("Gravity"));
		this.basePunch = tagCompound.getFloat("BasePunch");
		this.setWorksInWater(tagCompound.getBoolean("WorksInWater"));
		this.setSticksInGround(tagCompound.getBoolean("SticksInGround"));
		this.hasPotionEffects = tagCompound.getBoolean("HasPotionEffects");
		this.setRenders3D(tagCompound.getBoolean("Renders3D"));
		this.setPiercesEntities(tagCompound.getBoolean("PiercesEntities"));
		this.damageIgnoresSpeed = tagCompound.getBoolean("DamageIgnoresSpeed");
		this.hitSound = tagCompound.hasKey("HitSound", 8) ? ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(tagCompound.getString("HitSound"))) : null;
		this.setArrowStack(tagCompound.hasKey("Projectile", 10) ? new ItemStack(tagCompound.getCompoundTag("Projectile")) : ItemStack.EMPTY);
		this.bowStack = tagCompound.hasKey("Bow", 10) ? new ItemStack(tagCompound.getCompoundTag("Bow")) : ItemStack.EMPTY;
	}
}
