package com.tmtravlr.additions.addon.items;

import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Represents an added item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2017
 */
public interface IItemAdded {

	void setTooltip(List<String> infoToAdd);

	void setOreDict(List<String> oreDict);

	void setDisplayName(String name);

	void setAlwaysShines(boolean alwaysShines);

	void setBurnTime(int burnTime);

	void setIsBeaconPayment(boolean isBeaconPayment);

	void setAttributeModifiers(Multimap<EntityEquipmentSlot, AttributeModifier> attributeModifierList);

	List<String> getTooltip();

	List<String> getOreDict();

	String getDisplayName();

	boolean getAlwaysShines();

	int getBurnTime();

	boolean getIsBeaconPayment();

	Multimap<EntityEquipmentSlot, AttributeModifier> getAttributeModifiers();

	default int getColor(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("display", 10) && stack.getTagCompound().getCompoundTag("display").hasKey("color")) {
			return stack.getTagCompound().getCompoundTag("display").getInteger("color");
		}
		
		return 16777215;
	}
	
	default void registerModels() {
		AdditionsMod.proxy.registerItemRender(this.getAsItem());
	}
	
	default Item getAsItem() {
		if (!(this instanceof Item)) {
			throw new IllegalArgumentException("An IItemAdded must be an instance of Item.");
		}
		return (Item) this;
	}
	
    default String getId() {
    	ResourceLocation registryName = this.getAsItem().getRegistryName();
    	
    	if (registryName != null) {
    		return registryName.getResourcePath();
    	}
    	
        String unlocalizedName = this.getAsItem().getUnlocalizedName();
        
        if (unlocalizedName.startsWith("item.")) {
        	return unlocalizedName.substring(5);
        }
        
        return unlocalizedName;
    }

	abstract class Serializer<T extends IItemAdded> {
		
		private final ResourceLocation itemAddedType;
        private final Class<T> itemAddedClass;

        protected Serializer(ResourceLocation location, Class<T> clazz)
        {
            this.itemAddedType = location;
            this.itemAddedClass = clazz;
        }

		public ResourceLocation getItemAddedType() {
			return this.itemAddedType;
		}
		
		public Class<T> getItemAddedClass() {
			return this.itemAddedClass;
		}

		public JsonObject serialize(T itemAddedObj, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			Item itemAdded = itemAddedObj.getAsItem();
			
			json.addProperty("name", itemAddedObj.getDisplayName());
			
			if (itemAddedObj.getAlwaysShines()) {
				json.addProperty("shines", true);
			}
			
			if (itemAddedObj.getBurnTime() != -1) {
				json.addProperty("burn_time", itemAddedObj.getBurnTime());
			}
			
			if (itemAddedObj.getIsBeaconPayment()) {
				json.addProperty("is_beacon_payment", true);
			}
			
			if (!itemAddedObj.getTooltip().isEmpty()) {
				json.add("tooltip", OtherSerializers.StringListSerializer.serialize(itemAddedObj.getTooltip()));
			}
			
			if (itemAdded.getContainerItem() != null) {
				json.addProperty("container", Item.REGISTRY.getNameForObject(itemAdded.getContainerItem()).toString());
			}
			
			if (itemAdded.getItemStackLimit() != 64) {
				json.addProperty("max_stack", itemAdded.getItemStackLimit());
			}
			
			if (!itemAddedObj.getAttributeModifiers().isEmpty()) {
				json.add("attribute_modifiers", OtherSerializers.AttributeModifierListSerializer.serialize(itemAddedObj.getAttributeModifiers()));
			}
			
			if (!itemAddedObj.getOreDict().isEmpty()) {
				json.add("ore_dict", OtherSerializers.StringListSerializer.serialize(itemAddedObj.getOreDict()));
			}
			
			return json;
        }

		public abstract T deserialize(JsonObject json, JsonDeserializationContext context);
		
		public <V extends Item & IItemAdded> V deserializeDefaults(JsonObject json, JsonDeserializationContext context, V itemAdded) {
			
        	itemAdded.setDisplayName(JsonUtils.getString(json, "name"));
        	itemAdded.setAlwaysShines(JsonUtils.getBoolean(json, "shines", false));
        	itemAdded.setBurnTime(JsonUtils.getInt(json, "burn_time", -1));
        	itemAdded.setIsBeaconPayment(JsonUtils.getBoolean(json, "is_beacon_payment", false));
        	
        	if (json.has("tooltip")) {
        		itemAdded.setTooltip(OtherSerializers.StringListSerializer.deserialize(json.get("tooltip"), "tooltip"));
        	}
        	
        	if (json.has("max_stack")) {
        		itemAdded.setMaxStackSize(JsonUtils.getInt(json, "max_stack"));
        	}
        	
        	if (json.has("attribute_modifiers")) {
        		itemAdded.setAttributeModifiers(OtherSerializers.AttributeModifierListSerializer.deserialize(json.get("attribute_modifiers").getAsJsonArray()));
        	}
        	
        	if (json.has("ore_dict")) {
        		itemAdded.setOreDict(OtherSerializers.StringListSerializer.deserialize(json.get("ore_dict"), "ore_dict"));
        	}
        	
        	return itemAdded;
        }
		
		public void postDeserializeItemAdded(JsonObject json, IItemAdded itemAddedObj) {
			if (itemAddedObj.getClass() != this.itemAddedClass) {
				throw new IllegalArgumentException("Tried to call post serialize for an object of the wrong type! Expected: " + this.itemAddedClass + ", Actual: " + itemAddedObj.getClass());
			}
			
			postDeserialize(json, (T) itemAddedObj);
		}
		
		public void postDeserialize(JsonObject json, T itemAdded) {
			postDeserializeDefaults(json, itemAdded);
		}
		
		public void postDeserializeDefaults(JsonObject json, T itemAdded) {
        	
        	if (json.has("container")) {
        		itemAdded.getAsItem().setContainerItem(Item.REGISTRY.getObject(new ResourceLocation(JsonUtils.getString(json, "container"))));
        	}
        }
    }
	
}
