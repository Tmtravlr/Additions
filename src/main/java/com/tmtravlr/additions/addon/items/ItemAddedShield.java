package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.SoundEventLoader;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.util.EntityDamageSourceBash;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Multitool Item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017 
 */
public class ItemAddedShield extends ItemShield implements IItemAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "shield");
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
	
	public int enchantability = 1;
	public IngredientOreNBT repairStacks = IngredientOreNBT.EMPTY;
	public float bashDamage = 0;
	public int bashCooldown = 60;
	public float efficiencyMultiplier = 0.5f;
	public SoundEvent bashSwingSound = SoundEventLoader.ITEM_SHIELD_SWING;
	public SoundEvent bashHitSound = SoundEventLoader.ITEM_SHIELD_BASH;
	
	@Override
	public void setTooltip(List<String> infoToAdd) {
		this.extraTooltip = infoToAdd;
	}
	
	@Override
	public void setOreDict(List<String> oreDict) {
		this.oreDictEntries = oreDict;
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
	public void setDisplayName(String name) {
		this.displayName = name;
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
	public Multimap<EntityEquipmentSlot, AttributeModifier> getAttributeModifiers() {
		return this.attributeModifiers;
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		if (this.bashDamage > 0) {
			tooltip.add(TextFormatting.GRAY + I18n.translateToLocalFormatted("item.shield.info.bashDamage", ItemStack.DECIMALFORMAT.format(this.getTotalBashDamage(stack))));
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
	
	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack materialStack) {
		return this.repairStacks.itemMatches(materialStack, false, true);
	}
	
	@Override
	public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
        return true;
    }
	
	@Override
	public int getItemEnchantability(ItemStack stack) {
		return this.enchantability;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (this.canBashAttack()) {
			if (enchantment == Enchantments.KNOCKBACK || enchantment == Enchantments.FIRE_ASPECT || (this.efficiencyMultiplier < 1.0f && enchantment == Enchantments.EFFICIENCY)) {
				return true;
			}
		}
		
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		AdditionsMod.proxy.checkForShieldBash();
    }
	
	public boolean canBashAttack() {
		return this.bashDamage > 0;
	}
	
	public float getTotalBashDamage(ItemStack stack) {
		return this.bashDamage;
	}
	
	public static boolean canEntityBashAttack(EntityLivingBase attacker) {
		if (attacker == null || !attacker.isHandActive()) {
			return false;
		}
		
		ItemStack activeStack = attacker.getActiveItemStack();
		
		if (!(activeStack.getItem() instanceof ItemAddedShield)) {
			return false;
		}
		
		return ((ItemAddedShield)activeStack.getItem()).canBashAttack();
	}
	
	public static void doBashAttack(EntityLivingBase attacker, Entity attacked) {
		
		if (attacker instanceof EntityPlayer && MinecraftForge.EVENT_BUS.post(new AttackEntityEvent((EntityPlayer) attacker, attacked))) {
			return;
		}
		
		if (attacked != null && (!attacked.canBeAttackedWithItem() || attacked.hitByEntity(attacker))) {
			return;
		}
		
		ItemStack activeStack = attacker.getActiveItemStack();
		
		if (activeStack.isEmpty() || !(activeStack.getItem() instanceof ItemAddedShield)) {
			return;
		}
		
		EnumHand activeHand = attacker.getActiveHand();
		attacker.swingArm(activeHand);
		ItemAddedShield itemShield = (ItemAddedShield) activeStack.getItem();
		
		if (itemShield.bashSwingSound != null) {
			attacker.world.playSound(attacker.posX, attacker.posY, attacker.posZ, itemShield.bashSwingSound, attacker.getSoundCategory(), 1.0f, 1.0f, false);
		}
		
		if (attacker instanceof EntityPlayer) {
			int bashCooldown = ((ItemAddedShield)activeStack.getItem()).bashCooldown;
			int efficiency = Math.min(EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, activeStack), 5);
			((EntityPlayer)attacker).getCooldownTracker().setCooldown(activeStack.getItem(), bashCooldown - efficiency*bashCooldown / 10);
            attacker.resetActiveHand();
		}
		
		if (attacked == null) {
			return;
		}
		
		float bashDamage = itemShield.getTotalBashDamage(activeStack);
        
        if (bashDamage > 0.0F) {
        	
        	int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, activeStack);
        	int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, activeStack);
        	boolean setOnFire = false;
        	float health = 0.0F;

            double attackedMotionX = attacked.motionX;
            double attackedMotionY = attacked.motionY;
            double attackedMotionZ = attacked.motionZ;
        	
        	if (attacked instanceof EntityLivingBase) {
                health = ((EntityLivingBase)attacked).getHealth();

                if (fireAspect > 0 && !attacked.isBurning()) {
                	setOnFire = true;
                    attacked.setFire(1);
                }
            }
        	
        	if (attacked.attackEntityFrom(new EntityDamageSourceBash(attacker), bashDamage)) {
        		
        		if (itemShield.bashHitSound != null) {
        			attacked.world.playSound(null, attacked.posX, attacked.posY, attacked.posZ, itemShield.bashHitSound, attacked.getSoundCategory(), 1.0f, 1.0f);
        		}
        		
        		activeStack.damageItem(1, attacker);
        		
        		if (knockback > 0) {
                    if (attacked instanceof EntityLivingBase) {
                        ((EntityLivingBase)attacked).knockBack(attacker, (float)knockback * 0.5F, (double)MathHelper.sin(attacker.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(attacker.rotationYaw * 0.017453292F)));
                    } else {
                    	attacked.addVelocity((double)(-MathHelper.sin(attacker.rotationYaw * 0.017453292F) * (float)knockback * 0.5F), 0.1D, (double)(MathHelper.cos(attacker.rotationYaw * 0.017453292F) * (float)knockback * 0.5F));
                    }

                    attacker.motionX *= 0.6D;
                    attacker.motionZ *= 0.6D;
                    attacker.setSprinting(false);
                }

                if (attacked instanceof EntityPlayerMP && attacked.velocityChanged) {
                    ((EntityPlayerMP)attacked).connection.sendPacket(new SPacketEntityVelocity(attacked));
                    attacked.velocityChanged = false;
                    attacked.motionX = attackedMotionX;
                    attacked.motionY = attackedMotionY;
                    attacked.motionZ = attackedMotionZ;
                }

                attacker.setLastAttackedEntity(attacked);

                if (attacked instanceof EntityLivingBase) {
                    EnchantmentHelper.applyThornEnchantments((EntityLivingBase)attacked, attacker);
                }
                
                Entity entity = attacked;

                if (attacked instanceof MultiPartEntityPart) {
                    IEntityMultiPart ientitymultipart = ((MultiPartEntityPart)attacked).parent;

                    if (ientitymultipart instanceof EntityLivingBase)
                    {
                        entity = (EntityLivingBase)ientitymultipart;
                    }
                }

                if (entity instanceof EntityLivingBase && attacker instanceof EntityPlayer) {
                	
                    ItemStack beforeHitCopy = activeStack.copy();
                    activeStack.hitEntity((EntityLivingBase)entity, (EntityPlayer)attacker);

                    if (activeStack.isEmpty()) {
                        ForgeEventFactory.onPlayerDestroyItem((EntityPlayer)attacker, beforeHitCopy, activeHand);
                        ((EntityPlayer)attacker).setHeldItem(activeHand, ItemStack.EMPTY);
                        attacker.resetActiveHand();
                		attacker.world.playSound(null, attacker.posX, attacker.posY, attacker.posZ, SoundEvents.ITEM_SHIELD_BREAK, attacker.getSoundCategory(), 1.0f, 1.3f);
                    }
                }

                if (attacked instanceof EntityLivingBase) {
                	
            	    float healthLost = health - ((EntityLivingBase)attacked).getHealth();
            	    
                	if (attacker instanceof EntityPlayer) {
                	    ((EntityPlayer)attacker).addStat(StatList.DAMAGE_DEALT, Math.round(healthLost * 10.0F));
                	}

                    if (fireAspect > 0)
                    {
                        attacked.setFire(fireAspect * 4);
                    }

                    if (attacker.world instanceof WorldServer && healthLost > 2.0F)
                    {
                        int heartsLost = (int)((double)healthLost * 0.5D);
                        ((WorldServer)attacker.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, attacked.posX, attacked.posY + (double)(attacked.height * 0.5F), attacked.posZ, heartsLost, 0.1D, 0.0D, 0.1D, 0.2D);
                    }
                }

                if (attacker instanceof EntityPlayer) {
            	    ((EntityPlayer)attacker).addExhaustion(0.1F);
                }
        	} else {
                if (setOnFire) {
                    attacked.extinguish();
                }
            }
        } 
	}
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedShield> {
		
		public Serializer() {
			super(TYPE, ItemAddedShield.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedShield itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			json.addProperty("durability", itemAdded.getMaxDamage());
			json.addProperty("enchantability", itemAdded.enchantability);
			json.add("repair_stacks", IngredientOreNBT.Serializer.serialize(itemAdded.repairStacks));
			
			if (itemAdded.bashDamage > 0) {
				json.addProperty("bash_damage", itemAdded.bashDamage);
			}
			
			if (itemAdded.bashCooldown != 60) {
				json.addProperty("bash_cooldown", itemAdded.bashCooldown);
			}
			
			if (itemAdded.efficiencyMultiplier != 0.5f) {
				json.addProperty("efficiency_multiplier", itemAdded.efficiencyMultiplier);
			}
			
			if (itemAdded.bashSwingSound != null) {
				json.add("bash_swing_sound", OtherSerializers.SoundEventSerializer.serialize(itemAdded.bashSwingSound));
			}
			
			if (itemAdded.bashHitSound != null) {
				json.add("bash_hit_sound", OtherSerializers.SoundEventSerializer.serialize(itemAdded.bashHitSound));
			}
						
			return json;
		}
		
		@Override
		public ItemAddedShield deserialize(JsonObject json, JsonDeserializationContext context) {
			ItemAddedShield itemAdded = new ItemAddedShield();
			
			itemAdded.setMaxDamage(JsonUtils.getInt(json, "durability"));
			itemAdded.enchantability = JsonUtils.getInt(json, "enchantability");
			itemAdded.bashDamage = JsonUtils.getFloat(json, "bash_damage", 0f);
			itemAdded.bashCooldown = JsonUtils.getInt(json, "bash_cooldown", 60);
			itemAdded.efficiencyMultiplier = JsonUtils.getFloat(json, "efficiency_multiplier", 0.5f);
			
			if (json.has("bash_swing_sound")) {
				itemAdded.bashSwingSound = OtherSerializers.SoundEventSerializer.deserialize(json, "bash_swing_sound");
			}
			
			if (json.has("bash_hit_sound")) {
				itemAdded.bashHitSound = OtherSerializers.SoundEventSerializer.deserialize(json, "bash_hit_sound");
			}
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedShield itemAdded) {
			itemAdded.repairStacks = IngredientOreNBT.Serializer.deserialize(JsonUtils.getJsonObject(json, "repair_stacks"));
			
			postDeserializeDefaults(json, itemAdded);
		}
    }

}
