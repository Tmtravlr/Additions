package com.tmtravlr.additions.addon.effects.cause;

import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Cause for equipping an item or set.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class EffectCauseItemEquipped extends EffectCause {
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "item_equipped");
	
	public Multimap<EntityEquipmentSlot, ItemStack> equipment = HashMultimap.create();
	public int piecesRequired = 1;
	
	public boolean applies(EntityLivingBase entity) {
		return this.getRelevantStacks(entity).size() >= this.piecesRequired;
	}
	
	public NonNullList<ItemStack> getRelevantStacks(EntityLivingBase entity) {
		NonNullList<ItemStack> relevantStacks = NonNullList.create();
		
		for (Entry<EntityEquipmentSlot, ItemStack> entry : equipment.entries()) {
			ItemStack equippedStack = entity.getItemStackFromSlot(entry.getKey());
			
			if (!equippedStack.isEmpty() && itemMatches(entry.getValue(), equippedStack)) {
				relevantStacks.add(equippedStack);
			}
		}
		
		return relevantStacks;
	}
	
	private boolean itemMatches(ItemStack equippedStack, ItemStack stackToCheck) {
		if (OreDictionary.itemMatches(equippedStack, stackToCheck, false) && NBTUtil.areNBTEquals(equippedStack.getTagCompound(), stackToCheck.getTagCompound(), true)) {
			return true;
		}
		return false;
	}

	public static class Serializer extends EffectCause.Serializer<EffectCauseItemEquipped> {
		
		public Serializer() {
			super(TYPE, EffectCauseItemEquipped.class);
		}
		
		@Override
		public JsonObject serialize(EffectCauseItemEquipped effectCause, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			JsonArray equipmentArray = new JsonArray();
			
			for (Entry<EntityEquipmentSlot, ItemStack> entry : effectCause.equipment.entries()) {
				JsonObject equipmentJson = new JsonObject();
				equipmentJson.addProperty("slot", entry.getKey().getName());
				equipmentJson.add("item", OtherSerializers.ItemStackSerializer.serialize(entry.getValue()));
				equipmentArray.add(equipmentJson);
			}
			
			json.add("equipment", equipmentArray);
			json.addProperty("pieces_required", effectCause.piecesRequired);
			
			return json;
		}
		
		@Override
		public EffectCauseItemEquipped deserialize(JsonObject json, JsonDeserializationContext context) {
			EffectCauseItemEquipped effectCause = new EffectCauseItemEquipped();
			
			JsonArray equipmentArray = JsonUtils.getJsonArray(json, "equipment");
			
			equipmentArray.forEach(equipmentElement -> {
				JsonObject equipmentJson = JsonUtils.getJsonObject(equipmentElement, "member of equipment");
				EntityEquipmentSlot slot = EntityEquipmentSlot.fromString(JsonUtils.getString(equipmentJson, "slot"));
				ItemStack item = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(equipmentJson, "item"));
				effectCause.equipment.put(slot, item);
			});
			
			effectCause.piecesRequired = JsonUtils.getInt(json, "pieces_required", 1);
			
			return effectCause;
		}
    }
}
