package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAddedArrow extends ItemArrow implements IItemAdded, IItemAddedProjectile {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "arrow");
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public boolean isBeaconPayment = false;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
	
	public float damage = 0;
	public float punch = 0;
	public float gravity = 0.03f;
	public boolean worksInWater = false;
	public boolean sticksInGround = false;
	public boolean hasPotionEffects = false;
	public boolean renders3D = true;
	public boolean piercesEntities = false;
	public boolean worksWithInfinity = false;
	public boolean damageIgnoresSpeed = false;
	public SoundEvent hitSound = null;
	public List<Effect> hitEffects = new ArrayList<>();
	
	public ItemAddedArrow() {
		super();
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
	public boolean isInfinite(ItemStack projectile, ItemStack bow, EntityPlayer shooter) {
		return this.isInfinite(projectile, bow);
	}
	
	@Override
	public boolean isInfinite(ItemStack projectile, ItemStack bow) {
		return this.worksWithInfinity;
	}
	
	@Override
	public IEntityAddedProjectile createProjectile(World world, ItemStack projectile, ItemStack bow, EntityLivingBase shooter) {
		EntityAddedProjectile entityProjectile = this.createProjectile(world, projectile, shooter);
		
		if (!bow.isEmpty()) {
			entityProjectile.setBowStack(bow);
		}
		
		return entityProjectile;
	}

	@Override
    public EntityArrow createArrow(World world, ItemStack projectile, EntityLivingBase shooter) {
		return this.createProjectile(world, projectile, shooter);
    }
	
	@Override
	public DamageSource getDamageSource(IEntityAddedProjectile projectileEntity, Entity shooter, ItemStack damageItem) {
		return new EntityDamageSourceIndirect("additions.projectile", projectileEntity.getAsEntity(), shooter) {
			
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
	public void setTooltip(List<String> infoToAdd) {
		this.extraTooltip = infoToAdd;
	}

	@Override
	public void setOreDict(List<String> oreDict) {
		this.oreDictEntries = oreDict;
	}

	@Override
	public void setDisplayName(String name) {
		this.displayName = name;
	}

	@Override
	public void setAlwaysShines(boolean alwaysShines) {
		this.shines = alwaysShines;
	}
	
	@Override
	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}
	
	@Override
	public void setIsBeaconPayment(boolean isBeaconPayment) {
		this.isBeaconPayment = isBeaconPayment;
	}

	@Override
	public void setAttributeModifiers(Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifierList) {
		this.attributeModifiers = attributeModifierList;
	}

	@Override
	public List<String> getTooltip() {
		return this.extraTooltip;
	}

	@Override
	public List<String> getOreDict() {
		return this.oreDictEntries;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public boolean getAlwaysShines() {
		return this.shines;
	}
	
	@Override
	public int getBurnTime() {
		return this.burnTime;
	}
	
	@Override
	public boolean getIsBeaconPayment() {
		return this.isBeaconPayment;
	}

	@Override
	public Multimap<EntityEquipmentSlot, AttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
	}
	
	@Override
	public int getColor(ItemStack stack) {
		return this.hasPotionEffects ? PotionUtils.getColor(stack) : IItemAdded.super.getColor(stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return shines ? true : super.hasEffect(stack);
    }
	
	@Override
	public int getItemBurnTime(ItemStack stack) {
		return this.burnTime;
	}
	
	@Override
	public boolean isBeaconPayment(ItemStack stack) {
        return this.isBeaconPayment;
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
		
		for (String line : extraTooltip) {
    		if(I18n.canTranslate(line)) {
    			line = I18n.translateToLocal(line);
    		}
    		tooltip.add(line);
    	}
    }
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (I18n.canTranslate(displayName)) {
			return (I18n.translateToLocal(displayName)).trim();
		} else {
			return displayName;
		}
    }
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> modifiersForSlot = HashMultimap.create();
		
		if (this.attributeModifiers.containsKey(slot)) {
			for (AttributeModifier modifier : this.attributeModifiers.get(slot)) {
				modifiersForSlot.put(modifier.getName(), modifier);
			}
		}
		
		return modifiersForSlot;
	}
	
	public EntityAddedProjectile createProjectile(World world, ItemStack projectile, EntityLivingBase shooter) {
		ItemStack stackShot = projectile.copy();
		stackShot.setCount(1);
		
		return new EntityAddedProjectile(world, shooter, stackShot);
	}
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedArrow> {
		
		public Serializer() {
			super(TYPE, ItemAddedArrow.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedArrow itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			if (itemAdded.damage > 0) {
				json.addProperty("damage", itemAdded.damage);
			}
			
			if (itemAdded.punch > 0) {
				json.addProperty("punch", itemAdded.punch);
			}
			
			if (itemAdded.gravity != 0.03f) {
				json.addProperty("gravity", itemAdded.gravity);
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
			
			if (!itemAdded.renders3D) {
				json.addProperty("renders_3d", false);
			}
			
			if (itemAdded.piercesEntities) {
				json.addProperty("pierces_entities", true);
			}
			
			if (itemAdded.damageIgnoresSpeed) {
				json.addProperty("damage_ignores_speed", true);
			}
			
			if (itemAdded.hitSound != null) {
				json.add("hit_sound", OtherSerializers.SoundEventSerializer.serialize(itemAdded.hitSound));
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
		public ItemAddedArrow deserialize(JsonObject json, JsonDeserializationContext context) {
			
			ItemAddedArrow itemAdded = new ItemAddedArrow();
			
			itemAdded.damage = Math.max(JsonUtils.getFloat(json, "damage", 0), 0);
			itemAdded.punch = Math.max(JsonUtils.getFloat(json, "punch", 0), 0);
			itemAdded.gravity = Math.max(JsonUtils.getFloat(json, "gravity", 0.03f), 0);
			itemAdded.worksInWater = JsonUtils.getBoolean(json, "works_in_water", false);
			itemAdded.sticksInGround = JsonUtils.getBoolean(json, "sticks_in_ground", false);
			itemAdded.hasPotionEffects = JsonUtils.getBoolean(json, "has_potion_effects", false);
			itemAdded.worksWithInfinity = JsonUtils.getBoolean(json, "is_infinite_ammo", false);
			itemAdded.renders3D = JsonUtils.getBoolean(json, "renders_3d", true);
			itemAdded.piercesEntities = JsonUtils.getBoolean(json, "pierces_entities", false);
			itemAdded.damageIgnoresSpeed = JsonUtils.getBoolean(json, "damage_ignores_speed", false);
			
			if (JsonUtils.hasField(json, "hit_sound")) {
				itemAdded.hitSound = OtherSerializers.SoundEventSerializer.deserialize(json, "hit_sound");
			}
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedArrow itemAdded) {
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
