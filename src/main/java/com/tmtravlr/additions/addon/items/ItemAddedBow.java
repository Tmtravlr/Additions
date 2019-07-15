package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.type.AdditionTypeEffect;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAddedBow extends ItemBow implements IItemAdded {
	
	private static final Random RAND = new Random();
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "bow");
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public boolean isBeaconPayment = false;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
	
	public int enchantability = 1;
	public IngredientOreNBT repairStacks = IngredientOreNBT.EMPTY;
	
	public float extraDamage = 0;
	public float scattering = 1;
	public int drawTime = 20;
	public int shotCount = 1;
	public boolean firesVanillaArrows = true;
	public boolean alwaysInfinite = false;
	public boolean consumesOneAmmo = false;
	public float efficiencyMultiplier = 0;
	public SoundEvent shotSound = SoundEvents.ENTITY_ARROW_SHOOT;
	public List<Item> ammoItems = new ArrayList<>();
	public List<Effect> shotEffects = new ArrayList<>();
	
	public ItemAddedBow() {
		super();
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
        	
            @Override
			@SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                if (entity == null) {
                    return 0.0F;
                } else {
                    return entity.getActiveItemStack().getItem() != ItemAddedBow.this ? 0.0F : (float)(stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / ItemAddedBow.this.getDrawTime(stack);
                }
            }
            
        });
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
		double damage = this.getProjectileDamage(stack);
		if (damage > 0) {
			tooltip.add(TextFormatting.GRAY + I18n.translateToLocalFormatted("item.projectile.info.damage", ItemStack.DECIMALFORMAT.format(damage)));
		}
		
		tooltip.add(TextFormatting.GRAY + I18n.translateToLocalFormatted("item.bow.info.drawTime", ItemStack.DECIMALFORMAT.format(this.getDrawTime(stack) / 20f)));
		
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
	public int getItemEnchantability(ItemStack stack) {
		return this.enchantability;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack bowStack, World world, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            boolean bowFired = false;
            float projectileVelocity = 0;
            boolean shouldUseInfinity = this.allowInfinity(player, bowStack);
            
            for (int i = 0; i < this.shotCount; i++) {
	            ItemStack ammoStack = this.findAmmo(player);

	            int charge = this.getMaxItemUseDuration(bowStack) - timeLeft;
	            charge = ForgeEventFactory.onArrowLoose(bowStack, world, player, charge, !ammoStack.isEmpty() || shouldUseInfinity);
	            
	            if (charge < 0) {
	            	continue;
	            }
	
                projectileVelocity = getProjectileVelocity(bowStack, charge);
	
	            if (!ammoStack.isEmpty() || shouldUseInfinity) {
	            	boolean useInfinity = player.capabilities.isCreativeMode || (shouldUseInfinity && this.isAmmoInfinite(ammoStack.getItem(), bowStack)) || ammoStack.isEmpty();
	            	
	                if (ammoStack.isEmpty()) {
	                    Item ammoItem = this.findInfinityAmmo(bowStack);
	                    
	                    if (ammoItem == null && player.capabilities.isCreativeMode) {
	                    	ammoItem = this.findCreativeAmmo(bowStack);
	                    }
	                    
	                    if (ammoItem == null) {
	                    	continue;
	                    }
	                    
	                    ammoStack = new ItemStack(ammoItem);
	                }
	
	                if (projectileVelocity >= 0.1F) {
	                    
                    	if (!ammoStack.isEmpty()) {
	                        if (!player.world.isRemote) {
	                        	Entity projectile = this.createProjectile(world, bowStack, ammoStack, player, projectileVelocity, useInfinity);
	                        	
	                        	if (projectile != null) {
	                        		// Add an offset to the projectiles if this bow fires multiple
	                        		if (i <= 4) {
	                        			projectile.posY += 0.2 * i;
	                        		}
	                        		
	                        		world.spawnEntity(projectile);
	                        		bowFired = true;
	                        	}
	                        }
	                        
	                        if (!useInfinity) {
	                        	ammoStack.shrink(1);
	
	                            if (ammoStack.isEmpty()) {
	                                player.inventory.deleteStack(ammoStack);
	                            }
	                        }
                    	}
                    }
                }
            }
            
            if (bowFired) {
        		bowStack.damageItem(1, player);
        		
        		if (!this.shotEffects.isEmpty()) {
        			this.shotEffects.forEach(effect -> {
        				effect.affectEntity(player, player);
        				if (!bowStack.isEmpty()) {
        					effect.affectItemStack(player, world, bowStack);
        				}
        			});
        		}
        		
                player.addStat(StatList.getObjectUseStats(this));
                if (this.shotSound != null) {
                	world.playSound(null, player.posX, player.posY, player.posZ, this.shotSound, SoundCategory.PLAYERS, 1f, 1f / (itemRand.nextFloat() * 0.4f + 1.2f) + projectileVelocity * 0.5F);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack bowStack = player.getHeldItem(hand);
        boolean hasAmmo = !this.findAmmo(player).isEmpty();

        ActionResult<ItemStack> customResult = ForgeEventFactory.onArrowNock(bowStack, world, player, hand, hasAmmo);
        if (customResult != null) {
        	return customResult;
        }
        
        if (hasAmmo || allowInfinity(player, bowStack)) {
            player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, bowStack);
        } else {
        	return hasAmmo ? new ActionResult(EnumActionResult.PASS, bowStack) : new ActionResult(EnumActionResult.FAIL, bowStack);
        }
    }
    
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (this.efficiencyMultiplier > 0 && enchantment == Enchantments.EFFICIENCY) {
			return true;
		}
		
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
    
    public float getProjectileVelocity(ItemStack bowStack, int charge) {
		float velocity = (float)charge / this.getDrawTime(bowStack);
		velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
		
		if (velocity > 1.0F) {
			velocity = 1.0F;
		}

        return velocity;
    }
    
    private boolean allowInfinity(EntityPlayer player, ItemStack bowStack) {
    	return player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bowStack) > 0 || this.alwaysInfinite;
    }

    private ItemStack findAmmo(EntityPlayer player) {
        if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
            
        } else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
            
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack ammoStack = player.inventory.getStackInSlot(i);

                if (this.isAmmo(ammoStack))
                {
                    return ammoStack;
                }
            }

            return ItemStack.EMPTY;
        }
    }
    
    public boolean isAmmo(ItemStack ammoStack) {
    	return (this.firesVanillaArrows && this.isArrow(ammoStack)) || this.ammoItems.contains(ammoStack.getItem());
    }
    
    public boolean hasInfinityAmmo(ItemStack bowStack) {
    	return findInfinityAmmo(bowStack) != null;
    }
    
    // Find a random infinity item in the list of ammo, or a vanilla arrow if this bow can fire them
    public Item findInfinityAmmo(ItemStack bowStack) {
    	Item infinityItem = null;
    	List<Item> infinityItems = this.ammoItems.stream().filter(item -> this.isAmmoInfinite(item, bowStack)).collect(Collectors.toList());
    	
    	if (this.firesVanillaArrows) {
    		infinityItems.add(Items.ARROW);
    	}
    	
    	if (!infinityItems.isEmpty()) {
    		int randomIndex = RAND.nextInt(infinityItems.size());
    		infinityItem = infinityItems.get(randomIndex);
    	}
    	
    	return infinityItem;
    }
    
    private boolean isAmmoInfinite(Item item, ItemStack bowStack) {
    	if (item instanceof ItemArrow) {
    		try {
    			return ((ItemArrow)item).isInfinite(new ItemStack(item), bowStack, null);
    		} catch (NullPointerException e) {
				return false;
			}
    	} else if (item instanceof IItemAddedProjectile) {
    		return ((IItemAddedProjectile)item).isInfinite(new ItemStack(item), bowStack);
    	} else {
    		return true;
    	}
    }
    
    //Item to fall back to if there is no infinity ammo item
    public Item findCreativeAmmo(ItemStack bowStack) {
    	Item creativeAmmo = null;
    	
    	if (!this.ammoItems.isEmpty()) {
	    	int randomIndex = RAND.nextInt(this.ammoItems.size());
	    	creativeAmmo = this.ammoItems.get(randomIndex);
    	}
    	
    	return creativeAmmo;
    }
    
    public double getProjectileDamage(ItemStack bowStack) {
    	int powerEnch = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bowStack);
    	
    	return this.extraDamage + 0.5 * powerEnch + (powerEnch > 0 ? 0.5 : 0);
    }
    
    public int getDrawTime(ItemStack bowStack) {
    	int efficiencyEnch = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, bowStack), 0, 5);
    	
    	float reloadMultiplier = 1.0f;
    	if (this.efficiencyMultiplier > 0 && efficiencyEnch > 0) {
    		reloadMultiplier = 1 - this.efficiencyMultiplier * efficiencyEnch / 5;
    	}
    	
    	return MathHelper.floor(this.drawTime * reloadMultiplier);
    }
    
    public Entity createProjectile(World world, ItemStack bowStack, ItemStack ammoStack, EntityLivingBase shooter, float arrowVelocity, boolean useInfinity) {
    	if (ammoStack.getItem() instanceof IItemAddedProjectile) {
    		IItemAddedProjectile itemProjectile = (IItemAddedProjectile) ammoStack.getItem();
    		IEntityAddedProjectile entityProjectile = itemProjectile.createProjectile(world, ammoStack, bowStack, shooter);
    		entityProjectile.shootProjectile(shooter, shooter.rotationPitch, shooter.rotationYaw, 0, arrowVelocity * 3f, this.scattering + 0.5f);

            if (arrowVelocity == 1.0F) {
            	entityProjectile.setIsProjectileCritical(true);
            }

        	entityProjectile.setProjectileDamage(entityProjectile.getProjectileDamage() + this.getProjectileDamage(bowStack));

            int punchEnch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bowStack);

            if (punchEnch > 0) {
            	entityProjectile.setProjectileKnockback(punchEnch);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bowStack) > 0) {
            	entityProjectile.setProjectileFire(100);
            }

            if (useInfinity) {
            	entityProjectile.setProjectilePickupStatus(EntityArrow.PickupStatus.CREATIVE_ONLY);
            }
    		
    		return entityProjectile.getAsEntity();
    	} else if (ammoStack.getItem() instanceof ItemArrow) {
            ItemArrow itemArrow = (ItemArrow) ammoStack.getItem();
            EntityArrow entityArrow = itemArrow.createArrow(world, bowStack, shooter);
            entityArrow.shoot(shooter, shooter.rotationPitch, shooter.rotationYaw, 0.0F, arrowVelocity * 3.0F, this.scattering + 0.5f);

            if (arrowVelocity == 1.0F) {
                entityArrow.setIsCritical(true);
            }
            
            entityArrow.setDamage(entityArrow.getDamage() + this.getProjectileDamage(bowStack));

            int punchEnch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bowStack);

            if (punchEnch > 0) {
                entityArrow.setKnockbackStrength(punchEnch);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bowStack) > 0) {
                entityArrow.setFire(100);
            }

            if (useInfinity) {
                entityArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
            }
            
            return entityArrow;
    	} else {
    		ItemStack stackShot = ammoStack.copy();
    		stackShot.setCount(1);
    		
    		EntityAddedProjectile entityProjectile = new EntityAddedProjectile(world, shooter, stackShot);
    		
    		if (!bowStack.isEmpty()) {
    			entityProjectile.setBowStack(bowStack);
    		}
    		
    		entityProjectile.shoot(shooter, shooter.rotationPitch, shooter.rotationYaw, 0, arrowVelocity * 3f, this.scattering + 0.5f);

            if (arrowVelocity == 1.0F) {
            	entityProjectile.setIsCritical(true);
            }

        	entityProjectile.setDamage(entityProjectile.getDamage() + this.getProjectileDamage(bowStack));

            int punchEnch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bowStack);

            if (punchEnch > 0) {
            	entityProjectile.setKnockbackStrength(punchEnch);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bowStack) > 0) {
            	entityProjectile.setFire(100);
            }

            if (useInfinity) {
            	entityProjectile.setProjectilePickupStatus(EntityArrow.PickupStatus.CREATIVE_ONLY);
            }
    		
    		return entityProjectile.getAsEntity();
    	}
    }
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedBow> {
		
		public Serializer() {
			super(TYPE, ItemAddedBow.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedBow itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			json.addProperty("durability", itemAdded.getMaxDamage());
			json.addProperty("enchantability", itemAdded.enchantability);
			json.add("repair_stacks", IngredientOreNBT.Serializer.serialize(itemAdded.repairStacks));
			
			if (itemAdded.extraDamage > 0) {
				json.addProperty("extra_damage", itemAdded.extraDamage);
			}
			
			if (itemAdded.scattering != 1) {
				json.addProperty("scattering", itemAdded.scattering);
			}
			
			json.addProperty("draw_time", itemAdded.drawTime);
			
			if (itemAdded.shotCount > 1) {
				json.addProperty("shot_count", itemAdded.shotCount);
			}
			
			if (!itemAdded.firesVanillaArrows) {
				json.addProperty("fires_vanilla_arrows", false);
			}
			
			if (itemAdded.alwaysInfinite) {
				json.addProperty("always_infinite", true);
			}
			
			if (itemAdded.consumesOneAmmo) {
				json.addProperty("consumes_one_ammo", true);
			}
			
			if (itemAdded.efficiencyMultiplier > 0) {
				json.addProperty("efficiency_multiplier", itemAdded.efficiencyMultiplier);
			}
			
			if (!itemAdded.ammoItems.isEmpty()) {
				json.add("ammo_items", OtherSerializers.ItemListSerializer.serialize(itemAdded.ammoItems));
			}
			
			if (itemAdded.shotSound != null) {
				json.add("shot_sound", OtherSerializers.SoundEventSerializer.serialize(itemAdded.shotSound));
			}
			
			if (!itemAdded.shotEffects.isEmpty()) {
				JsonArray jsonArray = new JsonArray();
				
				for (Effect effect : itemAdded.shotEffects) {
					jsonArray.add(AdditionTypeEffect.GSON.toJsonTree(effect, Effect.class));
				}
				
				json.add("shot_effects", jsonArray);
			}
			
			return json;
		}
		
		@Override
		public ItemAddedBow deserialize(JsonObject json, JsonDeserializationContext context) {
			
			ItemAddedBow itemAdded = new ItemAddedBow();
			
			itemAdded.setMaxDamage(JsonUtils.getInt(json, "durability"));
			itemAdded.enchantability = JsonUtils.getInt(json, "enchantability");
			
			itemAdded.extraDamage = JsonUtils.getFloat(json, "extra_damage", 0);
			itemAdded.scattering = JsonUtils.getFloat(json, "scattering", 1);
			itemAdded.drawTime = JsonUtils.getInt(json, "draw_time", 20);
			
			if (itemAdded.drawTime < 1) {
				itemAdded.drawTime = 1;
			}
			
			itemAdded.shotCount = JsonUtils.getInt(json, "shot_count", 1);
			
			if (itemAdded.shotCount < 1) {
				itemAdded.shotCount = 1;
			}
			
			itemAdded.firesVanillaArrows = JsonUtils.getBoolean(json, "fires_vanilla_arrows", true);
			itemAdded.alwaysInfinite = JsonUtils.getBoolean(json, "always_infinite", false);
			itemAdded.consumesOneAmmo = JsonUtils.getBoolean(json, "consumes_one_ammo", false);
			itemAdded.efficiencyMultiplier = JsonUtils.getFloat(json, "efficiency_multiplier", 0);
			
			if (JsonUtils.hasField(json, "shot_sound")) {
				itemAdded.shotSound = OtherSerializers.SoundEventSerializer.deserialize(json, "shot_sound");
			}
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedBow itemAdded) {
			
			itemAdded.repairStacks = IngredientOreNBT.Serializer.deserialize(JsonUtils.getJsonObject(json, "repair_stacks"));
			
			if (JsonUtils.hasField(json, "ammo_items")) {
				itemAdded.ammoItems = OtherSerializers.ItemListSerializer.deserialize(json.get("ammo_items"), "ammo_items");
			}

			if (json.has("shot_effects")) {
				itemAdded.shotEffects = new ArrayList<>();
				
				JsonUtils.getJsonArray(json, "shot_effects").forEach(effectJson -> {
					itemAdded.shotEffects.add(AdditionTypeEffect.GSON.fromJson(effectJson, Effect.class));
				});
			}
			
			postDeserializeDefaults(json, itemAdded);
		}
    }

}
