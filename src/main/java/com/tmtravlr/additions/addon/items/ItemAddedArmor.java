package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Armor Item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017 
 */
public class ItemAddedArmor extends ItemArmor implements IItemAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "armor");
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
	
	public boolean applyVanillaAttributes = true;
	public boolean colored = false;
	
	public ItemAddedArmor() {
		this(EntityEquipmentSlot.HEAD);
	}
	
	public ItemAddedArmor(EntityEquipmentSlot slot) {
		super(ItemArmor.ArmorMaterial.LEATHER, 0, slot);
	}
	
	public void setArmorMaterial(ItemArmor.ArmorMaterial material) {
		if (material != this.getArmorMaterial()) {
			ObfuscationReflectionHelper.setPrivateValue(ItemArmor.class, this, material, "field_77878_bZ", "material");
	        this.setMaxDamage(material.getDurability(this.armorType));
	        ObfuscationReflectionHelper.setPrivateValue(ItemArmor.class, this, material.getDamageReductionAmount(this.armorType), "field_77879_b", "damageReduceAmount");
	        ObfuscationReflectionHelper.setPrivateValue(ItemArmor.class, this, material.getToughness(), "field_189415_e", "toughness");
		}
	}
	
	public void setEquipmentSlot(EntityEquipmentSlot slot) {
		if (slot != this.armorType) {
			ObfuscationReflectionHelper.setPrivateValue(ItemArmor.class, this, slot, "field_77881_a", "armorType");
	        this.setMaxDamage(this.getArmorMaterial().getDurability(slot));
	        ObfuscationReflectionHelper.setPrivateValue(ItemArmor.class, this, this.getArmorMaterial().getDamageReductionAmount(slot), "field_77879_b", "damageReduceAmount");
		}
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
		Multimap<String, AttributeModifier> modifiersForSlot;
		
		if (this.applyVanillaAttributes) {
			modifiersForSlot = super.getItemAttributeModifiers(slot);
		} else {
			modifiersForSlot = HashMultimap.create();
		}
		
		if (this.attributeModifiers.containsKey(slot)) {
			for (AttributeModifier modifier : this.attributeModifiers.get(slot)) {
				modifiersForSlot.put(modifier.getName(), modifier);
			}
		}
		
		return modifiersForSlot;
	}
	
	@Override
    public boolean hasOverlay(ItemStack stack) {
        return !this.colored || getColor(stack) != 0x00FFFFFF;
    }

    @Override
    public boolean hasColor(ItemStack stack) {
    	
        if (!this.colored) {
            return false;
            
        } else {
            NBTTagCompound nbttagcompound = stack.getTagCompound();
            return nbttagcompound != null && nbttagcompound.hasKey("display", 10) ? nbttagcompound.getCompoundTag("display").hasKey("color", 3) : false;
        }
    }

    @Override
    public int getColor(ItemStack stack) {
    	
        if (!this.colored) {
            return 16777215;
            
        } else {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound != null) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

                if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3)) {
                    return nbttagcompound1.getInteger("color");
                }
            }

            return 16777215;
        }
    }

    @Override
    public void removeColor(ItemStack stack) {
        if (this.colored) {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound != null) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

                if (nbttagcompound1.hasKey("color")) {
                    nbttagcompound1.removeTag("color");
                }
            }
        }
    }

    @Override
    public void setColor(ItemStack stack, int color) {
        if (this.colored) {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (nbttagcompound == null) {
                nbttagcompound = new NBTTagCompound();
                stack.setTagCompound(nbttagcompound);
            }

            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (!nbttagcompound.hasKey("display", 10)) {
                nbttagcompound.setTag("display", nbttagcompound1);
            }

            nbttagcompound1.setInteger("color", color);
        }
    }
    
    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    	if ("overlay".equals(type) && this.colored) {
    		return this.getArmorTextureOverlay();
    	} else {
    		return this.getArmorTextureBase();
    	}
    }
    
    public String getArmorTextureBase() {
    	String domain = this.getRegistryName().getResourceDomain();
    	String path = this.getRegistryName().getResourcePath();
    	
    	return domain + ":textures/models/armor/" + path + ".png";
    }
    
    public String getArmorTextureOverlay() {
    	String domain = this.getRegistryName().getResourceDomain();
    	String path = this.getRegistryName().getResourcePath();
    	
    	return domain + ":textures/models/armor/" + path + "_overlay.png";
    }
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedArmor> {
		
		public Serializer() {
			super(TYPE, ItemAddedArmor.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedArmor itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			json.addProperty("material", itemAdded.getArmorMaterial().toString());
			json.addProperty("slot", itemAdded.armorType.toString());
			if (!itemAdded.applyVanillaAttributes) {
				json.addProperty("vanilla_attributes", false);
			}
			if (itemAdded.colored) {
				json.addProperty("colored", true);
			}
			
			return json;
		}
		
		@Override
		public ItemAddedArmor deserialize(JsonObject json, JsonDeserializationContext context) {
			String slotName = JsonUtils.getString(json, "slot");
			EntityEquipmentSlot slot = null;
			
			try {
				slot = EntityEquipmentSlot.valueOf(slotName);
			} catch(IllegalArgumentException e) {
				AdditionsMod.logger.warn("Tried to parse invalid equipment slot " + slotName, e);
				throw new JsonSyntaxException("Unknown equipment slot " + slotName);
			}
			
			ItemAddedArmor itemAdded = new ItemAddedArmor(slot);
			
			itemAdded.applyVanillaAttributes = JsonUtils.getBoolean(json, "vanilla_attributes", true);
			itemAdded.colored = JsonUtils.getBoolean(json, "colored", false);
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedArmor itemAdded) {
			String materialName = JsonUtils.getString(json, "material");
			ArmorMaterial armorMaterial = null;
			
			try {
				armorMaterial = ItemArmor.ArmorMaterial.valueOf(materialName);
			} catch(IllegalArgumentException e) {
				AdditionsMod.logger.warn("Tried to parse invalid armor material " + materialName, e);
				armorMaterial = ItemArmor.ArmorMaterial.LEATHER;
			}
			
			itemAdded.setArmorMaterial(armorMaterial);
		}
    }

}
