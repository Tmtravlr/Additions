package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.entities.EntityAddedProjectile;
import com.tmtravlr.additions.addon.entities.IEntityAddedProjectile;
import com.tmtravlr.additions.type.AdditionTypeEffect;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Sword Item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2018
 */
public class ItemAddedThrowable extends ItemAddedSimple implements IItemAddedProjectile {
	
	public static final String DESCRIPTION = "type.item.throwable.description";
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "throwable");
	
	public float damage = 0;
	public float punch = 0;
	public float velocity = 1.5f;
	public float gravity = 0.03f;
	public float inaccuracy = 0;
	public boolean worksInWater = false;
	public boolean sticksInGround = false;
	public boolean hasPotionEffects = false;
	public boolean renders3D = false;
	public boolean piercesEntities = false;
	public boolean worksWithInfinity = false;
	public boolean damageIgnoresSpeed = false;
	public SoundEvent throwSound = SoundEvents.ENTITY_SNOWBALL_THROW;
	public SoundEvent hitSound = null;
	public List<Effect> throwEffects = new ArrayList<>();
	public List<Effect> hitEffects = new ArrayList<>();
	
	public ItemAddedThrowable() {
		super();
		this.setCreativeTab(CreativeTabs.COMBAT);
	}
	
	@Override
	public float getBaseDamage() {
		return this.damage;
	}
	
	@Override
	public void setBaseDamage(float baseDamage) {
		this.damage = baseDamage;
	}
	
	@Override
	public float getBasePunch() {
		return this.punch;
	}
	
	@Override
	public void setBasePunch(float basePunch) {
		this.punch = basePunch;
	}
	
	@Override
	public float getGravity() {
		return this.gravity;
	}
	
	@Override
	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	@Override
	public boolean worksInWater() {
		return this.worksInWater;
	}

	@Override
	public void setWorksInWater(boolean worksInWater) {
		this.worksInWater = worksInWater;
	}

	@Override
	public boolean sticksInGround() {
		return this.sticksInGround;
	}

	@Override
	public void setSticksInGround(boolean sticksInGround) {
		this.sticksInGround = sticksInGround;
	}

	@Override
	public boolean hasPotionEffects() {
		return this.hasPotionEffects;
	}

	@Override
	public void setHasPotionEffects(boolean hasPotionEffects) {
		this.hasPotionEffects = hasPotionEffects;
	}

	@Override
	public boolean worksWithInfinity() {
		return this.worksWithInfinity;
	}

	@Override
	public void setWorksWithInfinity(boolean worksWithInfinity) {
		this.worksWithInfinity = worksWithInfinity;
	}

	@Override
	public boolean renders3D() {
		return this.renders3D;
	}

	@Override
	public void setRenders3D(boolean renders3D) {
		this.renders3D = renders3D;
	}

	@Override
	public boolean piercesEntities() {
		return this.piercesEntities;
	}

	@Override
	public void setPiercesEntities(boolean piercesEntities) {
		this.piercesEntities = piercesEntities;
	}

	@Override
	public boolean damageIgnoresSpeed() {
		return this.damageIgnoresSpeed;
	}

	@Override
	public void setDamageIgnoresSpeed(boolean damageIgnoresSpeed) {
		this.damageIgnoresSpeed = damageIgnoresSpeed;
	}

	@Override
	public SoundEvent getHitSound() {
		return this.hitSound;
	}

	@Override
	public void setHitSound(SoundEvent hitSound) {
		this.hitSound = hitSound;
	}
	
	@Override
	public List<Effect> getHitEffects() {
		return this.hitEffects;
	}
	
	@Override
	public void setHitEffects(List<Effect> hitEffects) {
		this.hitEffects = hitEffects;
	}
	
	@Override
	public boolean isInfinite(ItemStack projectile, ItemStack bow) {
		return this.worksWithInfinity;
	}
	
	@Override
	public IEntityAddedProjectile createProjectile(World world, ItemStack projectile, ItemStack bow, EntityLivingBase shooter) {
		return this.createThrownEntity(world, projectile, bow, shooter);
	}
	
	@Override
	public DamageSource getDamageSource(IEntityAddedProjectile projectileEntity, Entity shooter, ItemStack damageItem) {
		return new EntityDamageSourceIndirect("additions.thrown", projectileEntity.getAsEntity(), shooter) {
			
			@Override
			public ITextComponent getDeathMessage(EntityLivingBase entity) {
		        if (this.getTrueSource() == null) {
		        	return new TextComponentTranslation("death.attack." + this.damageType, entity.getDisplayName(), damageItem.getTextComponent());
		        } else {
		        	return new TextComponentTranslation("death.attack." + this.damageType + ".player", entity.getDisplayName(), this.getTrueSource().getDisplayName(), damageItem.getTextComponent());
		        }
		    }
			
		}.setProjectile();
	}
	
	@Override
	public int getColor(ItemStack stack) {
		return this.hasPotionEffects ? PotionUtils.getColor(stack) : super.getColor(stack);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack thrownStack = player.getHeldItem(hand);

        if (this.throwSound != null) {
        	world.playSound(null, player.posX, player.posY, player.posZ, this.throwSound, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        }

        if (!world.isRemote) {
        	EntityAddedProjectile thrown = this.createThrownEntity(world, thrownStack, ItemStack.EMPTY, player);
        	
        	if (player.capabilities.isCreativeMode) {
        		thrown.setProjectilePickupStatus(EntityArrow.PickupStatus.CREATIVE_ONLY);
        	}
        	
        	thrown.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, this.velocity, this.inaccuracy);
            world.spawnEntity(thrown);
    		
    		if (!this.throwEffects.isEmpty()) {
    			this.throwEffects.forEach(effect -> effect.affectEntity(player, player));
    		}
        }

        if (!player.capabilities.isCreativeMode) {
        	thrownStack.shrink(1);
        }

        player.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, thrownStack);
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		if (this.damage > 0) {
			tooltip.add(TextFormatting.GRAY + I18n.translateToLocalFormatted("item.projectile.info.damage", ItemStack.DECIMALFORMAT.format(this.damage)));
			
			if (this.hasPotionEffects) {
				tooltip.add("");
			}
		}
		
		if (this.hasPotionEffects) {
	        PotionUtils.addPotionTooltip(stack, tooltip, 0.125F);
		}
		
		super.addInformation(stack, world, tooltip, flag);
    }
	
	public EntityAddedProjectile createThrownEntity(World world, ItemStack projectile, ItemStack bow, EntityLivingBase shooter) {
		ItemStack stackShot = projectile.copy();
		stackShot.setCount(1);
		
		EntityAddedProjectile entityProjectile = new EntityAddedProjectile(world, shooter, stackShot);
		
		if (!bow.isEmpty()) {
			entityProjectile.setBowStack(bow);
		}
		
		return entityProjectile;
	}
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedThrowable> {
		
		public Serializer() {
			super(TYPE, ItemAddedThrowable.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedThrowable itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			if (itemAdded.damage > 0) {
				json.addProperty("damage", itemAdded.damage);
			}
			
			if (itemAdded.punch > 0) {
				json.addProperty("punch", itemAdded.punch);
			}
			
			if (itemAdded.velocity != 1.5f) {
				json.addProperty("velocity", itemAdded.velocity);
			}
			
			if (itemAdded.gravity != 0.03f) {
				json.addProperty("gravity", itemAdded.gravity);
			}
			
			if (itemAdded.inaccuracy > 0) {
				json.addProperty("inaccuracy", itemAdded.inaccuracy);
			}
			
			if (itemAdded.worksInWater) {
				json.addProperty("works_in_water", true);
			}
			
			if (itemAdded.sticksInGround) {
				json.addProperty("sticks_in_ground", true);
			}
			
			if (itemAdded.hasPotionEffects) {
				json.addProperty("has_potion_effects", true);
			}
			
			if (itemAdded.worksWithInfinity) {
				json.addProperty("is_infinite_ammo", true);
			}
			
			if (itemAdded.renders3D) {
				json.addProperty("renders_3d", true);
			}
			
			if (itemAdded.piercesEntities) {
				json.addProperty("pierces_entities", true);
			}
			
			if (itemAdded.damageIgnoresSpeed) {
				json.addProperty("damage_ignores_speed", true);
			}
			
			if (itemAdded.throwSound != null) {
				json.add("thrown_sound", OtherSerializers.SoundEventSerializer.serialize(itemAdded.throwSound));
			}
			
			if (itemAdded.hitSound != null) {
				json.add("hit_sound", OtherSerializers.SoundEventSerializer.serialize(itemAdded.hitSound));
			}
			
			if (!itemAdded.throwEffects.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				for (Effect effect : itemAdded.throwEffects) {
					jsonArray.add(AdditionTypeEffect.GSON.toJsonTree(effect, Effect.class));
				}
				
				json.add("throw_effects", jsonArray);
			}
			
			if (!itemAdded.hitEffects.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				for (Effect effect : itemAdded.hitEffects) {
					jsonArray.add(AdditionTypeEffect.GSON.toJsonTree(effect, Effect.class));
				}
				
				json.add("hit_effects", jsonArray);
			}
			
			return json;
		}
		
		@Override
		public ItemAddedThrowable deserialize(JsonObject json, JsonDeserializationContext context) {
			
			ItemAddedThrowable itemAdded = new ItemAddedThrowable();
			
			itemAdded.damage = Math.max(JsonUtils.getFloat(json, "damage", 0), 0);
			itemAdded.punch = Math.max(JsonUtils.getFloat(json, "punch", 0), 0);
			itemAdded.velocity = Math.max(JsonUtils.getFloat(json, "velocity", 1.5f), 0);
			itemAdded.gravity = Math.max(JsonUtils.getFloat(json, "gravity", 0.03f), 0);
			itemAdded.inaccuracy = Math.max(JsonUtils.getFloat(json, "inaccuracy", 0), 0);
			itemAdded.worksInWater = JsonUtils.getBoolean(json, "works_in_water", false);
			itemAdded.sticksInGround = JsonUtils.getBoolean(json, "sticks_in_ground", false);
			itemAdded.hasPotionEffects = JsonUtils.getBoolean(json, "has_potion_effects", false);
			itemAdded.worksWithInfinity = JsonUtils.getBoolean(json, "is_infinite_ammo", false);
			itemAdded.renders3D = JsonUtils.getBoolean(json, "renders_3d", false);
			itemAdded.piercesEntities = JsonUtils.getBoolean(json, "pierces_entities", false);
			itemAdded.damageIgnoresSpeed = JsonUtils.getBoolean(json, "damage_ignores_speed", false);
			
			if (JsonUtils.hasField(json, "thrown_sound")) {
				itemAdded.throwSound = OtherSerializers.SoundEventSerializer.deserialize(json, "thrown_sound");
			}
			
			if (JsonUtils.hasField(json, "hit_sound")) {
				itemAdded.hitSound = OtherSerializers.SoundEventSerializer.deserialize(json, "hit_sound");
			}
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedThrowable itemAdded) {
			if (json.has("throw_effects")) {
				itemAdded.throwEffects = new ArrayList<>();
				
				JsonUtils.getJsonArray(json, "throw_effects").forEach(effectJson -> {
					itemAdded.throwEffects.add(AdditionTypeEffect.GSON.fromJson(effectJson, Effect.class));
				});
			}
			
			if (json.has("hit_effects")) {
				itemAdded.hitEffects = new ArrayList<>();
				
				JsonUtils.getJsonArray(json, "hit_effects").forEach(effectJson -> {
					itemAdded.hitEffects.add(AdditionTypeEffect.GSON.fromJson(effectJson, Effect.class));
				});
			}
			
			this.postDeserializeDefaults(json, itemAdded);
		}
    }

}
