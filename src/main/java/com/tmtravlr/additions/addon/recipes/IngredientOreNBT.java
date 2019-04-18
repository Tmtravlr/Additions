package com.tmtravlr.additions.addon.recipes;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.util.OtherSerializers;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

/**
 * Ingredient that combines ore dictionary with a list of nbt-sensitive ingredients
 * @author Rebeca Rey (Tmtravlr)
 * @since February 2019
 */
public class IngredientOreNBT extends Ingredient {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "ingredient_ore_nbt");
	public static final IngredientOreNBT EMPTY = new IngredientOreNBT();

	private IntList stacksPacked = null;
    private ItemStack[] stacksExploded = null;
    private boolean simple;
	private NonNullList<ItemStack> oreStacks = NonNullList.create();
    private int lastOreSize = -1;
	
	private String oreName = "";
	private NonNullList<ItemStack> itemStacks = NonNullList.create();
    
    public IngredientOreNBT() {
    	super(0);
    }
    
    public IngredientOreNBT(String oreName) {
    	super(0);
    	this.setOreName(oreName);
    }

    
    public IngredientOreNBT(ItemStack... itemStacks) {
    	super(0);
    	NonNullList<ItemStack> stackList = NonNullList.create();
    	stackList.addAll(Arrays.asList(itemStacks));
    	this.setStackList(stackList);
    }

	@Override
	public boolean apply(@Nullable ItemStack stack) {
		return this.itemMatches(stack, false, true);
    }
    
    @Override
    public ItemStack[] getMatchingStacks() {
        this.validateOreStacks();
        return this.stacksExploded;
    }

    @Override
    public IntList getValidItemStacksPacked() {
        this.validateOreStacks();

    	if (this.stacksPacked == null) {
    		this.packStacks();
    	}
    	
        return this.stacksPacked;
    }
    
    @Override
    protected void invalidate() {
        this.stacksPacked = null;
    }

    @Override
    public boolean isSimple() {
        return this.simple;
    }
    
    public boolean isEmpty() {
    	if (this == IngredientOreNBT.EMPTY) {
    		return true;
    	}
    	
		this.validateOreStacks();
    	return this.stacksExploded.length == 0;
    }
	
	public boolean itemMatches(ItemStack stack, boolean strict, boolean compareNBT) {
		if (stack != null) {
			for (ItemStack match : this.getMatchingStacks()) {
				if (OreDictionary.itemMatches(stack, match, strict) && (!compareNBT || NBTUtil.areNBTEquals(match.getTagCompound(), stack.getTagCompound(), true))) {
					return true;
				}
			}
		}
		
		return false;
	}
    
    public String getOreName() {
    	return this.oreName;
    }
    
    public NonNullList<ItemStack> getStackList() {
    	return this.itemStacks;
    }
	
	public NonNullList<ItemStack> getAllStacks() {
		NonNullList<ItemStack> allStacks = NonNullList.create();
		allStacks.addAll(this.itemStacks);
		allStacks.addAll(this.oreStacks);
		return allStacks;
	}
    
    public void setOreName(String oreName) {
		this.oreName = oreName;
		
		if (OreDictionary.doesOreNameExist(oreName)) {
			this.oreStacks = OreDictionary.getOres(oreName);
		} else {
			this.oreStacks = OreDictionary.EMPTY_LIST;
		}

		this.explodeStacks();
    }
    
    public void setStackList(NonNullList<ItemStack> stackList) {
    	this.itemStacks = stackList;
    	this.explodeStacks();
    }
    
    public void setOreEntryAndStackList(String oreEntry, NonNullList<ItemStack> stackList) {
    	this.itemStacks = stackList;
		this.oreName = oreEntry;
		
		if (OreDictionary.doesOreNameExist(oreName)) {
			this.oreStacks = OreDictionary.getOres(oreEntry);
		} else {
			this.oreStacks = OreDictionary.EMPTY_LIST;
		}

		this.explodeStacks();
    }
	
	public void clear() {
		this.setOreEntryAndStackList("", NonNullList.create());
	}
    
    private void validateOreStacks() {
    	if (this.lastOreSize != this.oreStacks.size() || (this.oreStacks == OreDictionary.EMPTY_LIST && OreDictionary.doesOreNameExist(oreName))) {
    		this.explodeStacks();
    	}
    }
    
    private void explodeStacks() {
		NonNullList<ItemStack> stacksExplodedList = NonNullList.create();
		boolean simple = true;
		
		for (ItemStack stack : this.getAllStacks()) {
		    if (stack.isEmpty()) {
		        continue;
		    }
		    
		    if (stack.getItem().isDamageable() || stack.hasTagCompound()) {
		        simple = false;
		    }

		    if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
		    	NonNullList<ItemStack> itemsInCreativeTab = NonNullList.create();
		        stack.getItem().getSubItems(CreativeTabs.SEARCH, itemsInCreativeTab);
		        
		        if (!itemsInCreativeTab.isEmpty()) {
		        	stacksExplodedList.addAll(itemsInCreativeTab);
		        } else {
		        	stacksExplodedList.add(new ItemStack(stack.getItem()));
		        }
		    } else {
		        stacksExplodedList.add(stack);
		    }
		}
		
		this.stacksExploded = stacksExplodedList.toArray(new ItemStack[stacksExplodedList.size()]);
		this.simple = simple && stacksExplodedList.size() > 0;
		this.lastOreSize = this.oreStacks.size();
		this.invalidate();
    }
    
    private void packStacks() {
        this.stacksPacked = new IntArrayList(this.oreStacks.size());

        for (ItemStack stack : this.getAllStacks()) {
        	
            if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                NonNullList<ItemStack> subStacks = NonNullList.create();
                stack.getItem().getSubItems(CreativeTabs.SEARCH, subStacks);
                
                for (ItemStack subStack : subStacks) {
                	this.stacksPacked.add(RecipeItemHelper.pack(subStack));
                }
            } else {
                this.stacksPacked.add(RecipeItemHelper.pack(stack));
            }
        }

        this.stacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oreName == null) ? 0 : oreName.hashCode());
		result = prime * result + ((itemStacks == null) ? 0 : itemStacks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IngredientOreNBT other = (IngredientOreNBT) obj;
		if (oreName == null) {
			if (other.oreName != null)
				return false;
		} else if (!oreName.equals(other.oreName))
			return false;
		if (itemStacks == null) {
			if (other.itemStacks != null)
				return false;
		} else {
			if (itemStacks.size() != other.itemStacks.size()) {
				return false;
			}
			for (int i = 0; i < itemStacks.size(); i++) {
				ItemStack thisStack = itemStacks.get(i);
				NBTTagCompound thisTag = thisStack.getTagCompound();
				if (thisTag == null) {
					thisTag = new NBTTagCompound();
				}
				
				ItemStack otherStack = other.itemStacks.get(i);
				NBTTagCompound otherTag = otherStack.getTagCompound();
				if (otherTag == null) {
					otherTag = new NBTTagCompound();
				}
				
				if (thisStack != otherStack && (thisStack.getItem() != otherStack.getItem() || thisStack.getItemDamage() != otherStack.getItemDamage() || !thisTag.equals(otherTag))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static class Serializer {
		
		public static JsonObject serialize(IngredientOreNBT ingredients) {
			JsonObject json = new JsonObject();
			
			if (!ingredients.getOreName().isEmpty()) {
				json.addProperty("ore_name", ingredients.getOreName());
			}
			
			if (!ingredients.getStackList().isEmpty()) {
				JsonArray jsonList = new JsonArray();
				for (ItemStack stack : ingredients.getStackList()) {
					jsonList.add(OtherSerializers.ItemStackSerializer.serialize(stack));
				}
				json.add("stack_list", jsonList);
			}
			
			return json;
		}
		
		public static IngredientOreNBT deserialize(JsonObject json) {
			IngredientOreNBT ingredients = new IngredientOreNBT();
			String oreName = "";
			NonNullList<ItemStack> stacks = NonNullList.create();
			
			if (json.has("ore_name")) {
				oreName = JsonUtils.getString(json, "ore_name");
			}
			
			if (json.has("stack_list")) {
				JsonArray jsonList = JsonUtils.getJsonArray(json, "stack_list");
				for (int i = 0; i < jsonList.size(); i++) {
					stacks.add(OtherSerializers.ItemStackSerializer.deserialize(JsonUtils.getJsonObject(jsonList.get(i), "member of stack_list")));
				}
			}
			
			ingredients.setOreEntryAndStackList(oreName, stacks);
			
			return ingredients;
		}
		
	}
	
}
