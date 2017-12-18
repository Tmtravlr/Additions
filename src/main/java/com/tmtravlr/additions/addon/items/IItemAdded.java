package com.tmtravlr.additions.addon.items;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.type.AdditionTypeItem;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Represents an added item
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public interface IItemAdded {
	
	public void setTooltip(List<String> infoToAdd);
	
	public void setOreDict(List<String> oreDict);
	
	public void setDisplayName(String name);
	
	public void setAlwaysShines(boolean alwaysShines);
	
	public void setAttributeModifiers(Multimap<EntityEquipmentSlot, AttributeModifier> attributeModiferList);
	
	public List<String> getTooltip();
	
	public List<String> getOreDict();
	
	public String getDisplayName();
	
	public boolean getAlwaysShines();
	
	public Multimap<EntityEquipmentSlot, AttributeModifier> getAttributeModifiers();

	public default int getColor(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("display", 10) && stack.getTagCompound().getCompoundTag("display").hasKey("color")) {
			return stack.getTagCompound().getCompoundTag("display").getInteger("color");
		}
		
		return 16777215;
	}
	
	public default void registerModels() {
		AdditionsMod.proxy.registerItemRender(this.getAsItem());
	}
	
	public default Item getAsItem() {
		if (!(this instanceof Item)) {
			throw new IllegalArgumentException("An IItemAdded must be an instance of Item.");
		}
		return (Item) this;
	}
	
    public default String getId() {
        String unlocalizedName = this.getAsItem().getUnlocalizedName();
        if (unlocalizedName.startsWith("item.")) {
        	return unlocalizedName.substring(5);
        }
        return unlocalizedName;
    }

	public abstract static class Serializer<T extends IItemAdded> {
		
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
			
			if (!itemAddedObj.getTooltip().isEmpty()) {
				json.add("tooltip", OtherSerializers.StringListSerializer.serialize(itemAddedObj.getTooltip(), context));
			}
			
			if(itemAdded.getContainerItem() != null) {
				json.addProperty("container", Item.REGISTRY.getNameForObject(itemAdded.getContainerItem()).toString());
			}
			
			if (itemAdded.getItemStackLimit() != 64) {
				json.addProperty("max_stack", itemAdded.getItemStackLimit());
			}
			
			if (!itemAddedObj.getAttributeModifiers().isEmpty()) {
				json.add("attribute_modifiers", OtherSerializers.AttributeModifierListSerializer.serialize(itemAddedObj.getAttributeModifiers(), context));
			}
			
			if (!itemAddedObj.getOreDict().isEmpty()) {
				json.add("ore_dict", OtherSerializers.StringListSerializer.serialize(itemAddedObj.getOreDict(), context));
			}
			
			return json;
        }

		public abstract T deserialize(JsonObject json, JsonDeserializationContext context);
		
		public <V extends Item & IItemAdded> V deserializeDefaults(JsonObject json, JsonDeserializationContext context, V itemAdded) {
			
        	itemAdded.setDisplayName(JsonUtils.getString(json, "name"));
        	
        	itemAdded.setAlwaysShines(JsonUtils.getBoolean(json, "shines", false));
        	
        	if (json.has("tooltip")) {
        		itemAdded.setTooltip(OtherSerializers.StringListSerializer.deserialize(json.get("tooltip"), "tooltip", context));
        	}
        	
        	if (json.has("container")) {
        		itemAdded.setContainerItem(Item.REGISTRY.getObject(new ResourceLocation(JsonUtils.getString(json, "container"))));
        	}
        	
        	if (json.has("max_stack")) {
        		itemAdded.setMaxStackSize(JsonUtils.getInt(json, "max_stack"));
        	}
        	
        	if (json.has("attribute_modifiers")) {
        		itemAdded.setAttributeModifiers(OtherSerializers.AttributeModifierListSerializer.deserialize(json.get("attribute_modifiers").getAsJsonArray(), context));
        	}
        	
        	if (json.has("ore_dict")) {
        		itemAdded.setOreDict(OtherSerializers.StringListSerializer.deserialize(json.get("ore_dict"), "ore_dict", context));
        	}
        	
        	return itemAdded;
        }
    }
	
}
