package com.tmtravlr.additions.addon;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.creativetabs.CreativeTabAdded;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.util.OtherSerializers;

/**
 * Main addon class.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class Addon {
	
	public File addonFolder;
	
	public String id = "";
	public String name = "";
	public String logoItem = "";
	public String author = "";
	public List<String> dependencies = new ArrayList<>();
	public List<String> dependents = new ArrayList<>();
	public List<String> requiredAddons = new ArrayList<>();
	public List<String> requiredMods = new ArrayList<>();
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setLogoItem(String itemName) {
		this.logoItem = itemName;
	}
	
	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}
	
	public void setDependents(List<String> dependents) {
		this.dependents = dependents;
	}
	
	public void setRequiredAddons(List<String> requiredAddons) {
		this.requiredAddons = requiredAddons;
	}
	
	public void setRequiredMods(List<String> requiredMods) {
		this.requiredMods = requiredMods;
	}
	
	public void setAddonFolder(File addonFile) {
		this.addonFolder = addonFile;
	}
	
	public ItemStack getLogoItem() {
		if (this.logoItem != null) {
			Item item = Item.REGISTRY.getObject(new ResourceLocation(this.logoItem));
			if (item != null) {
				return new ItemStack(item);
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	public String toString() {
		return this.name + " (" + this.id + ")";
	}
	
	public static class Serializer implements JsonDeserializer<Addon>, JsonSerializer<Addon> {

		@Override
		public JsonElement serialize(Addon info, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			json.addProperty("id", info.id);
			json.addProperty("name", info.name);
			json.addProperty("author", info.author);
			json.addProperty("logo_item", info.logoItem);
			
			if (!info.dependencies.isEmpty()) {
				json.add("dependencies", OtherSerializers.StringListSerializer.serialize(info.dependencies, context));
			}
			
			if (!info.dependents.isEmpty()) {
				json.add("dependents", OtherSerializers.StringListSerializer.serialize(info.dependents, context));
			}
			
			if (!info.requiredMods.isEmpty()) {
				json.add("requiredMods", OtherSerializers.StringListSerializer.serialize(info.requiredMods, context));
			}
			
			if (!info.requiredAddons.isEmpty()) {
				json.add("requiredAddons", OtherSerializers.StringListSerializer.serialize(info.requiredAddons, context));
			}
			
			return json;
		}

		@Override
		public Addon deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
			JsonObject json = JsonUtils.getJsonObject(jsonElement, "addon");
			Addon addon = new Addon();
			
			addon.setId(JsonUtils.getString(json, "id").toLowerCase());
			
			if (AddonLoader.ADDONS_NAMED.containsKey(addon.id)) {
				throw new JsonSyntaxException("Tried to load an addon with the id '" + addon.id + "', but an addon with that id already exists.");
			}
			
			if (!addon.id.matches("[a-z0-9\\_]+")) {
				throw new JsonSyntaxException("Id '" + addon.id + "' can only contain lowercase letters, numbers, or underscores.");
			}
			
			addon.setName(JsonUtils.getString(json, "name"));
			addon.setAuthor(JsonUtils.getString(json, "author"));
			addon.setLogoItem(JsonUtils.getString(json, "logo_item", ""));
			
			if (JsonUtils.hasField(json, "dependencies")) {
				addon.setDependencies(OtherSerializers.StringListSerializer.deserialize(json.get("dependencies"), "dependencies", context));
			}
			
			if (JsonUtils.hasField(json, "dependents")) {
				addon.setDependents(OtherSerializers.StringListSerializer.deserialize(json.get("dependents"), "dependents", context));
			}
			
			if (JsonUtils.hasField(json, "requiredMods")) {
				addon.setRequiredMods(OtherSerializers.StringListSerializer.deserialize(json.get("requiredMods"), "requiredMods", context));
			}
			
			if (JsonUtils.hasField(json, "requiredAddons")) {
				addon.setRequiredAddons(OtherSerializers.StringListSerializer.deserialize(json.get("requiredAddons"), "requiredAddons", context));
			}
			
			return addon;
		}
		
	}

}
