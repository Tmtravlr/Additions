package com.tmtravlr.additions.addon.creativetabs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabAdded extends CreativeTabs {
	
	public NonNullList<ItemStack> displayItems = NonNullList.create();
	public ItemStack customTabItemStack = ItemStack.EMPTY;
	public boolean hasSearchBar;
	
	public CreativeTabAdded(String label) {
		super(label);
	}
	
	public void setCustomTabItemStack(ItemStack customIcon) {
		this.customTabItemStack = customIcon;
	}
	
	public void setDisplayItems(NonNullList<ItemStack> displayItems) {
		this.displayItems = displayItems;
	}
	
	public void setHasSearch() {
		this.hasSearchBar = true;
		this.setBackgroundImageName("item_search.png");
	}

    /**
     * Gets the translated Label.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public String getTranslatedTabLabel() {
        return this.getTabLabel();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {
    	if (!this.customTabItemStack.isEmpty()) {
    		
    		return this.customTabItemStack;
    	} else if (!this.displayItems.isEmpty()) {
    		
    		return this.displayItems.get(0);
    	} else {
    		return new ItemStack(Blocks.CRAFTING_TABLE);
    	}
    }

    /**
     * only shows items which have tabToDisplayOn == this
     */
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_)
    {
        for (Item item : Item.REGISTRY)
        {
            item.getSubItems(this, p_78018_1_);
        }
    }

    /**
     * Determines if the search bar should be shown for this tab.
     *
     * @return True to show the bar
     */
    @Override
    public boolean hasSearchBar() {
        return this.hasSearchBar;
    }
	
	public static class Serializer implements JsonDeserializer<CreativeTabAdded>, JsonSerializer<CreativeTabAdded> {

		@Override
        public JsonElement serialize(CreativeTabAdded tabAdded, Type type, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            
            json.addProperty("name", tabAdded.getTabLabel());
            
            if (tabAdded.hasSearchBar()) {
            	json.addProperty("has_search", true);
            }
            
            if (tabAdded.customTabItemStack != null) {
            	json.add("tab_item", OtherSerializers.ItemStackSerializer.serialize(tabAdded.customTabItemStack, context));
            }
            
            if (!tabAdded.displayItems.isEmpty()) {
            	JsonArray items = new JsonArray();
            	
            	for (ItemStack item : tabAdded.displayItems) {
            		items.add(OtherSerializers.ItemStackSerializer.serialize(item, context));
            	}
            	
            	json.add("items", items);
            }
            
            return json;
        }
        
		@Override
        public CreativeTabAdded deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(jsonElement, "creative_tab");
            
            String displayName = JsonUtils.getString(json, "name");
            
            CreativeTabAdded tabAdded = new CreativeTabAdded(displayName);
            
            if (JsonUtils.getBoolean(json, "has_search", false)) {
            	tabAdded.setHasSearch();
            }
            
            if (json.has("tab_item") && json.get("tab_item").isJsonObject()) {
            	tabAdded.setCustomTabItemStack(OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "tab_item"), context));
            }
            
            JsonArray itemArray = JsonUtils.getJsonArray(json, "items");
            NonNullList<ItemStack> items = NonNullList.create();
            
            for(int i = 0; i < itemArray.size(); i++) {
            	items.add(OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(itemArray.get(i), "member of items"), context));
            }
            
            tabAdded.setDisplayItems(items);

            return tabAdded;
        }
    }
}
