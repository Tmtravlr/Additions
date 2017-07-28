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

public class AddonInfo {
	
	public File addonFile;
	public List<CreativeTabAdded> creativeTabsAdded = new ArrayList<>();
	public List<IItemAdded> itemsAdded = new ArrayList<>();
	public List<IBlockAdded> blocksAdded = new ArrayList<>();
	
	public String id;
	public String name;
	public String logoItem;
	public List<String> addonsRequiredBefore = new ArrayList<>();
	public List<String> addonsRequiredAfter = new ArrayList<>();
	public List<String> requiredAddons = new ArrayList<>();
	public List<String> requiredMods = new ArrayList<>();
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLogoItem(String itemName) {
		this.logoItem = itemName;
	}
	
	public void setRequiredBefore(List<String> requiredBefore) {
		this.addonsRequiredBefore = requiredBefore;
	}
	
	public void setRequiredAfter(List<String> requiredAfter) {
		this.addonsRequiredAfter = requiredAfter;
	}
	
	public void setRequiredAddons(List<String> requiredAddons) {
		this.requiredAddons = requiredAddons;
	}
	
	public void setRequiredMods(List<String> requiredMods) {
		this.requiredMods = requiredMods;
	}
	
	public void setAddonFile(File addonFile) {
		this.addonFile = addonFile;
	}
	
	public ItemStack getLogoItem() {
		if (this.logoItem != null) {
			Item item = Item.REGISTRY.getObject(new ResourceLocation(this.logoItem));
			if (item != null) {
				return new ItemStack(item);
			}
		}
		
		if(!this.creativeTabsAdded.isEmpty()) {
			return this.creativeTabsAdded.get(0).getTabIconItem();
		}
		
		return ItemStack.EMPTY;
	}
	
	public static class Serializer implements JsonDeserializer<AddonInfo>, JsonSerializer<AddonInfo> {

		@Override
		public JsonElement serialize(AddonInfo info, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			json.addProperty("id", info.id);
			json.addProperty("name", info.name);
			json.addProperty("logo_item", info.logoItem);
			return json;
		}

		@Override
		public AddonInfo deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
			JsonObject json = JsonUtils.getJsonObject(jsonElement, "addon_info");
			AddonInfo addon = new AddonInfo();
			
			addon.setId(JsonUtils.getString(json, "id").toLowerCase());
			
			if (AddonLoader.ADDONS_NAMED.containsKey(addon.id)) {
				throw new JsonSyntaxException("Tried to load an addon with the id '" + addon.id + "', but an addon with that id already exists.");
			}
			
			if (!addon.id.matches("^[\\w]+$")) {
				throw new JsonSyntaxException("Id '" + addon.id + "' can only contain alphanumeric characters or underscores.");
			}
			
			addon.setName(JsonUtils.getString(json, "name"));
			addon.setLogoItem(JsonUtils.getString(json, "logo_item", ""));
			return addon;
		}
		
	}

}
