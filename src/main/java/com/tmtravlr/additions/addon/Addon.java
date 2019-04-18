package com.tmtravlr.additions.addon;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

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
	public String version = "";
	public String name = "";
	public String logoItem = "";
	public String author = "";
	public List<String> dependencies = new ArrayList<>();
	public List<String> dependents = new ArrayList<>();
	public List<String> requiredAddons = new ArrayList<>();
	public List<String> requiredMods = new ArrayList<>();
	public ResourceLocation loopFunction;
	public boolean locked;
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setVersion(String version) {
		this.version = version;
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
	
	public void setLocked(boolean locked) {
		this.locked = locked;
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
	
	public void setLoopFunction(ResourceLocation loopFunction) {
		this.loopFunction = loopFunction;
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
		return this.name + TextFormatting.RESET + " (" + this.id + (this.locked ? (" v" + this.version) : "") + ")";
	}
	
	public static class Serializer implements JsonDeserializer<Addon>, JsonSerializer<Addon> {

		@Override
		public JsonElement serialize(Addon info, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			json.addProperty("id", info.id);
			json.addProperty("name", info.name);
			json.addProperty("author", info.author);
			json.addProperty("logo_item", info.logoItem);
			
			if (!info.version.isEmpty()) {
				json.addProperty("version", info.version);
			}
			
			if (!info.dependencies.isEmpty()) {
				json.add("dependencies", OtherSerializers.StringListSerializer.serialize(info.dependencies));
			}
			
			if (!info.dependents.isEmpty()) {
				json.add("dependents", OtherSerializers.StringListSerializer.serialize(info.dependents));
			}
			
			if (!info.requiredMods.isEmpty()) {
				json.add("required_mods", OtherSerializers.StringListSerializer.serialize(info.requiredMods));
			}
			
			if (!info.requiredAddons.isEmpty()) {
				json.add("required_addons", OtherSerializers.StringListSerializer.serialize(info.requiredAddons));
			}
			
			if (info.loopFunction != null) {
				json.addProperty("loop_function", info.loopFunction.toString());
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
			
			addon.setVersion(JsonUtils.getString(json, "version", ""));
			addon.setName(JsonUtils.getString(json, "name", ""));
			addon.setAuthor(JsonUtils.getString(json, "author", ""));
			addon.setLogoItem(JsonUtils.getString(json, "logo_item", ""));
			
			if (JsonUtils.hasField(json, "dependencies")) {
				addon.setDependencies(OtherSerializers.StringListSerializer.deserialize(json.get("dependencies"), "dependencies"));
			}
			
			if (JsonUtils.hasField(json, "dependents")) {
				addon.setDependents(OtherSerializers.StringListSerializer.deserialize(json.get("dependents"), "dependents"));
			}
			
			if (JsonUtils.hasField(json, "required_mods")) {
				addon.setRequiredMods(OtherSerializers.StringListSerializer.deserialize(json.get("required_mods"), "required_mods"));
			}
			
			if (JsonUtils.hasField(json, "required_addons")) {
				addon.setRequiredAddons(OtherSerializers.StringListSerializer.deserialize(json.get("required_addons"), "required_addons"));
			}
			
			if (JsonUtils.hasField(json, "loop_function")) {
				addon.setLoopFunction(new ResourceLocation(JsonUtils.getString(json, "loop_function")));
			}
			
			return addon;
		}
		
	}

}
