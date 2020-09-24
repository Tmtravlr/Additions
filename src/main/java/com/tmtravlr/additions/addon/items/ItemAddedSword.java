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

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Sword Item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date November 2017 
 */
public class ItemAddedSword extends ItemSword implements IItemAdded, IItemAddedTool {

	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "sword");
	
	public String displayName = "";
	public List<String> extraTooltip = new ArrayList<>();
	public List<String> oreDictEntries = new ArrayList<>();
	public boolean shines = false;
	public int burnTime = -1;
	public boolean isBeaconPayment = false;
	public Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifiers = HashMultimap.create();
	
	public boolean applyVanillaAttributes = true;
	
	private Item.ToolMaterial material;
	
	public ItemAddedSword() {
		super(Item.ToolMaterial.WOOD);
	}
	
	@Override
	public void setToolMaterial(Item.ToolMaterial material) {
		if (material != this.material) {
			this.material = material;
			ObfuscationReflectionHelper.setPrivateValue(ItemSword.class, this, material, "field_150933_b", "material");
			ObfuscationReflectionHelper.setPrivateValue(ItemSword.class, this, 3.0F + material.getAttackDamage(), "field_150934_a", "attackDamage");
			this.setMaxDamage(material.getMaxUses());
		}
	}
	
	@Override
	public void setApplyVanillaAttributes(boolean applyVanillaAttributes) {
		this.applyVanillaAttributes = applyVanillaAttributes;
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
	public void setIsBeaconPayment(boolean isBeaconPayment) {
		this.isBeaconPayment = isBeaconPayment;
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
	public Item.ToolMaterial getToolMaterial() {
		return this.material;
	}
	
	@Override
	public boolean shouldApplyVanillaAttributes() {
		return this.applyVanillaAttributes;
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
	
	public static class Serializer extends IItemAdded.Serializer<ItemAddedSword> {
		
		public Serializer() {
			super(TYPE, ItemAddedSword.class);
		}
		
		@Override
		public JsonObject serialize(ItemAddedSword itemAdded, JsonSerializationContext context) {
			JsonObject json = super.serialize(itemAdded, context);
			
			json.addProperty("material", itemAdded.getToolMaterial().toString());
			if (!itemAdded.applyVanillaAttributes) {
				json.addProperty("vanilla_attributes", false);
			}
			
			return json;
		}
		
		@Override
		public ItemAddedSword deserialize(JsonObject json, JsonDeserializationContext context) {
			ItemAddedSword itemAdded = new ItemAddedSword();
			
			itemAdded.applyVanillaAttributes = JsonUtils.getBoolean(json, "vanilla_attributes", true);
			
			super.deserializeDefaults(json, context, itemAdded);
			return itemAdded;
		}
		
		@Override
		public void postDeserialize(JsonObject json, ItemAddedSword itemAdded) {
			String materialName = JsonUtils.getString(json, "material");
			Item.ToolMaterial toolMaterial = null;
			
			try {
				toolMaterial = Item.ToolMaterial.valueOf(materialName);
			} catch(IllegalArgumentException e) {
				AdditionsMod.logger.warn("Tried to parse invalid tool material " + materialName, e);
				toolMaterial = Item.ToolMaterial.WOOD;
			}
			
			itemAdded.setToolMaterial(toolMaterial);
		}
    }

}
