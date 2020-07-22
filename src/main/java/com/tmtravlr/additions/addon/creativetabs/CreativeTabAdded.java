package com.tmtravlr.additions.addon.creativetabs;

import com.google.gson.*;
import com.tmtravlr.additions.addon.items.IItemAddedProjectile;
import com.tmtravlr.additions.addon.items.ItemAddedFood;
import com.tmtravlr.additions.util.OtherSerializers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Type;

/**
 * Represents an added creative tab
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public class CreativeTabAdded extends CreativeTabs {
	
	public String id;
	
	public NonNullList<ItemStack> displayItems = NonNullList.create();
	public ItemStack customTabItemStack = ItemStack.EMPTY;
	public boolean hasSearchBar = false;
	public final String tabLabel;
	
	public CreativeTabAdded(String label) {
		super(label);
		this.tabLabel = label;
		
	}
	
	public CreativeTabAdded(int index, String label) {
		super(index, label);
		this.tabLabel = label;
	}
	
	public void setId(String id) {
		this.id = id;
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

    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> itemsToDisplay) {
    	for (ItemStack displayItem : this.displayItems) {
    		if (displayItem.getItem() instanceof IItemAddedProjectile && ((IItemAddedProjectile)displayItem.getItem()).hasPotionEffects() && !displayItem.hasTagCompound()) {
    			this.addPotions(displayItem, itemsToDisplay);
    		} else if (displayItem.getItem() instanceof ItemAddedFood && ((ItemAddedFood)displayItem.getItem()).hasPotionEffects && !displayItem.hasTagCompound()) {
    			this.addPotions(displayItem, itemsToDisplay);
    		} else {
    			itemsToDisplay.add(displayItem);
    		}
    	}
    }

    @Override
    public boolean hasSearchBar() {
        return this.hasSearchBar;
    }
    
    public int getItemCount() {
    	return this.displayItems.size();
    }
    
    public NonNullList<ItemStack> getDisplayItems() {
    	return this.displayItems;
    }
    
    private void addPotions(ItemStack stack, NonNullList<ItemStack> itemsToDisplay) {
    	for (PotionType potionType : PotionType.REGISTRY) {
            if (potionType != PotionTypes.EMPTY) {
            	itemsToDisplay.add(PotionUtils.addPotionToItemStack(stack.copy(), potionType));
            }
        }
    }
	
	public static class Serializer implements JsonDeserializer<CreativeTabAdded>, JsonSerializer<CreativeTabAdded> {

		@Override
        public JsonElement serialize(CreativeTabAdded tabAdded, Type type, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            
            json.addProperty("name", tabAdded.tabLabel);
            
            if (tabAdded.hasSearchBar()) {
            	json.addProperty("has_search", true);
            }
            
            if (tabAdded.customTabItemStack != null) {
            	json.add("tab_item", OtherSerializers.ItemStackSerializer.serialize(tabAdded.customTabItemStack));
            }
            
            if (!tabAdded.displayItems.isEmpty()) {
            	JsonArray items = new JsonArray();
            	
            	for (ItemStack item : tabAdded.displayItems) {
            		items.add(OtherSerializers.ItemStackSerializer.serialize(item));
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
            	tabAdded.setCustomTabItemStack(OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "tab_item")));
            }

            if (json.has("items")) {
				JsonArray itemArray = JsonUtils.getJsonArray(json, "items");
				NonNullList<ItemStack> items = NonNullList.create();

				for (int i = 0; i < itemArray.size(); i++) {
					items.add(OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(itemArray.get(i), "member of items")));
				}
				tabAdded.setDisplayItems(items);
			}


            return tabAdded;
        }
    }
}
