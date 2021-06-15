package com.tmtravlr.additions.addon.recipes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * An added recipe for modifying an item's damage. The item can either be the output or be left 
 * in the crafting table with another item as the output.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date March 2019
 */
public class RecipeAddedCraftingModifyDamage extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe, IRecipeAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "crafting_modify_damage");
	
	public Item itemToModify;
	public ItemStack output = ItemStack.EMPTY;
	public Map<IngredientOreNBT, Integer> modifyAmounts;
	public int minAmount = 0;
	public int maxAmount = Short.MAX_VALUE;

	@Override
	public void setId(ResourceLocation id) {
		this.setRegistryName(id);
	}

	@Override
	public ResourceLocation getId() {
		return this.getRegistryName();
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack stackToModify = ItemStack.EMPTY;
        List<ItemStack> modifierList = Lists.<ItemStack>newArrayList();
        int damageModifier = 0;
        
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack craftingStack = inv.getStackInSlot(i);
            
            if (!craftingStack.isEmpty()) {

	            if (craftingStack.getItem() == this.itemToModify) {
	            	if (!stackToModify.isEmpty()) {
	            		return false;
	            	}
	            	
	            	stackToModify = craftingStack;
	            } else {
	            	boolean isModifier = false;
	            	
	            	for(Ingredient ingredient : this.modifyAmounts.keySet()) {
	            		if (ingredient.apply(craftingStack)) {
	            			isModifier = true;
	            			damageModifier += this.modifyAmounts.get(ingredient);
	            			modifierList.add(craftingStack);
	            			break;
	            		}
	            	}
	            	
	            	if (!isModifier) {
	            		return false;
	            	}
	            }
            }
        }
        
        return !stackToModify.isEmpty() && !modifierList.isEmpty();
    }

	@Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
		if (this.output.isEmpty()) {
			ItemStack stackToModify = ItemStack.EMPTY;
	        int damageModifier = 0;
	        
	        for (int i = 0; i < inv.getSizeInventory(); ++i) {
	            ItemStack craftingStack = inv.getStackInSlot(i);
	            
	            if (!craftingStack.isEmpty()) {

		            if (craftingStack.getItem() == this.itemToModify) {
		            	stackToModify = craftingStack.copy();
		            	stackToModify.setCount(1);
		            } else {
		            	for(Ingredient ingredient : this.modifyAmounts.keySet()) {
		            		if (ingredient.apply(craftingStack)) {
		            			damageModifier += this.modifyAmounts.get(ingredient);
		            			break;
		            		}
		            	}
		            }
	            }
	        }
            
            this.modifyStack(stackToModify, damageModifier);
	        return stackToModify;
		} else {
			return this.output.copy();
		}
    }

	@Override
    public ItemStack getRecipeOutput() {
		 return ItemStack.EMPTY;
    }

	@Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remainingItems = ForgeHooks.defaultRecipeGetRemainingItems(inv);
        
        if (!this.output.isEmpty()) {
        	ItemStack stackToModify = ItemStack.EMPTY;
            int damageModifier = 0;
            
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack craftingStack = inv.getStackInSlot(i);
                
                if (!craftingStack.isEmpty()) {

    	            if (craftingStack.getItem() == this.itemToModify) {
    	            	stackToModify = craftingStack.copy();
    	            	
    	            	if (remainingItems.size() > i) {
    	            		remainingItems.set(i, stackToModify);
    	            	} else {
    	            		AdditionsMod.logger.warn("Couldn't set recipe remaining item for crafting recipe " + this.getRegistryName());
    	            	}
    	            } else {
    	            	for(Ingredient ingredient : this.modifyAmounts.keySet()) {
    	            		if (ingredient.apply(craftingStack)) {
    	            			damageModifier += this.modifyAmounts.get(ingredient);
    	            			break;
    	            		}
    	            	}
    	            }
                }
            }
            
            this.modifyStack(stackToModify, damageModifier);
        }
        
        return remainingItems;
    }

	@Override
    public boolean isDynamic() {
        return true;
    }

	@Override
    public boolean canFit(int width, int height)  {
		return width * height >= 2;
    }
	
	@Override
	public void registerRecipe(IForgeRegistry<IRecipe> registry) {
		registry.register(this);
	}
	
	private void modifyStack(ItemStack stack, int modifier) {
		int stackDamage = stack.getItemDamage();
        
        if (modifier < 0) {
        	stackDamage = Math.max(this.minAmount, stackDamage + modifier);
        } else {
        	stackDamage = Math.min(this.maxAmount, stackDamage + modifier);
        }

        stack.setItemDamage(stackDamage);
        
        if (stack.isItemStackDamageable() && stackDamage > stack.getMaxDamage()) {
        	stack.shrink(1);
        }
	}
	
	public static class Serializer extends IRecipeAdded.Serializer<RecipeAddedCraftingModifyDamage> {
		
		public Serializer() {
			super(TYPE, RecipeAddedCraftingModifyDamage.class);
		}

		public JsonObject serialize(RecipeAddedCraftingModifyDamage recipeAdded, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.addProperty("item_to_modify", recipeAdded.itemToModify.getRegistryName().toString());
			
			JsonArray modifiers = new JsonArray();
			
			if (recipeAdded.modifyAmounts.isEmpty()) {
				throw new IllegalArgumentException("Recipe must have modifier amounts");
			}
			
			for (Entry<IngredientOreNBT, Integer> modifierEntry : recipeAdded.modifyAmounts.entrySet()) {
				JsonObject modifierJson = new JsonObject();
				modifierJson.add("ingredient", IngredientOreNBT.Serializer.serialize(modifierEntry.getKey()));
				modifierJson.addProperty("amount", modifierEntry.getValue());
				modifiers.add(modifierJson);
			}
			
			json.add("modifiers", modifiers);
			
			if (!recipeAdded.output.isEmpty()) {
				json.add("output", OtherSerializers.ItemStackSerializer.serialize(recipeAdded.output));
			}
			
			if (recipeAdded.minAmount != 0) {
				json.addProperty("min_amount", recipeAdded.minAmount);
			}
			
			if (recipeAdded.maxAmount != 0) {
				json.addProperty("max_amount", recipeAdded.maxAmount);
			}
			
			return json;
        }
		
		@Override
		public RecipeAddedCraftingModifyDamage deserialize(JsonObject json, JsonDeserializationContext context) {
			RecipeAddedCraftingModifyDamage recipeAdded = new RecipeAddedCraftingModifyDamage();
			
			recipeAdded.itemToModify = JsonUtils.getItem(json, "item_to_modify");
			
			JsonArray modifierArray = JsonUtils.getJsonArray(json, "modifiers");
			Map<IngredientOreNBT, Integer> modifyAmounts = new HashMap<>();
			
			for (int i = 0; i < modifierArray.size(); i++) {
				JsonObject modifierJson = JsonUtils.getJsonObject(modifierArray.get(i), "member of modifiers");
				
				IngredientOreNBT modifierIngredient = IngredientOreNBT.Serializer.deserialize(JsonUtils.getJsonObject(modifierJson, "ingredient"));
				int modifierAmount = JsonUtils.getInt(modifierJson, "amount");
				
				modifyAmounts.put(modifierIngredient, modifierAmount);
			}

	        if (modifyAmounts.isEmpty()) {
	        	throw new JsonParseException("No damage modifiers");
	        }
			
			recipeAdded.modifyAmounts = modifyAmounts;
			
			if (json.has("output")) {
				recipeAdded.output = OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(json, "output"));
			}
			
			recipeAdded.minAmount = JsonUtils.getInt(json, "min_amount", 0);
			recipeAdded.maxAmount = JsonUtils.getInt(json, "max_amount", Short.MAX_VALUE);
			
			return recipeAdded;
		}
    }

}
