package com.tmtravlr.additions.addon.recipes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.brewing.AbstractBrewingRecipe;

/**
 * An added complete set of brewing recipes
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date April 2019
 */
public class RecipeAddedBrewingComplete extends AbstractBrewingRecipe<IngredientOreNBT> implements IRecipeAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "brewing_complete");
	
	public static final IngredientOreNBT INGREDIENT_GUNPOWDER = new IngredientOreNBT("gunpowder");
	public static final IngredientOreNBT INGREDIENT_REDSTONE = new IngredientOreNBT("dustRedstone");
	public static final IngredientOreNBT INGREDIENT_GLOWSTONE = new IngredientOreNBT("dustGlowstone");
	public static final NBTTagCompound TAG_AWKWARD_POTION = new NBTTagCompound();
	static {
		TAG_AWKWARD_POTION.setString("Potion", PotionTypes.AWKWARD.getRegistryName().toString());
	}
	
	public ResourceLocation id;
	public IngredientOreNBT ingredient = IngredientOreNBT.EMPTY;
	public NBTTagCompound inputTag = TAG_AWKWARD_POTION;
	public NBTTagCompound outputTag = new NBTTagCompound();
	public NBTTagCompound outputExtendedTag = new NBTTagCompound();
	public NBTTagCompound outputPoweredTag = new NBTTagCompound();
	public boolean allowMundane = true;
	public boolean allowSplash = true;
	public boolean allowLingering = true;
	
	public RecipeAddedBrewingComplete() {
		super(ItemStack.EMPTY, IngredientOreNBT.EMPTY, ItemStack.EMPTY);
	}

	@Override
	public void setId(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public void registerRecipe() {
		//BrewingRecipeRegistry.addRecipe(this);
		
		// Registering in a very cheeky way here instead of BrewingRecipeRegistry.addRecipe(this); because otherwise the vanilla recipe breaks any recipes that use glowstone or redstone dust
		RecipeAddedManager.BREWING_RECIPES.add(0, this);
	}
	
	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return this.ingredient.apply(ingredient) || (this.allowSplash && INGREDIENT_GUNPOWDER.apply(ingredient)) || (this.allowLingering && ingredient.getItem() == Items.DRAGON_BREATH) || 
				(!outputExtendedTag.hasNoTags() && INGREDIENT_REDSTONE.apply(ingredient)) || (!outputPoweredTag.hasNoTags() && INGREDIENT_GLOWSTONE.apply(ingredient));
	}
	
	@Override
	public boolean isInput(ItemStack input) {
		return input.getItem() == Items.POTIONITEM || (this.allowSplash && input.getItem() == Items.SPLASH_POTION) || (this.allowLingering && input.getItem() == Items.LINGERING_POTION);
	}

    @Override
    public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
    	ItemStack output = ItemStack.EMPTY;
    	
    	if (isIngredient(ingredient) && isInput(input)) {
	    	boolean tagMatchesNormal = NBTUtil.areNBTEquals(input.getTagCompound(), input.getItem() == Items.LINGERING_POTION ? this.getLingeringTag(outputTag) : outputTag, true);
	    	boolean tagMatchesExtended = !outputExtendedTag.hasNoTags() && NBTUtil.areNBTEquals(input.getTagCompound(), input.getItem() == Items.LINGERING_POTION ? this.getLingeringTag(outputExtendedTag) : outputExtendedTag, true);
	    	boolean tagMatchesPowered = !outputPoweredTag.hasNoTags() && NBTUtil.areNBTEquals(input.getTagCompound(), input.getItem() == Items.LINGERING_POTION ? this.getLingeringTag(outputPoweredTag) : outputPoweredTag, true);
	    	boolean tagMatchesAny = tagMatchesNormal || tagMatchesExtended || tagMatchesPowered;
    	
	    	if (this.ingredient.apply(ingredient)) {
	    		PotionType potionType = PotionUtils.getPotionFromItem(input);
	    		
	    		if (allowMundane && potionType == PotionTypes.WATER) {
	    			output = input.copy();
	    			output.setTagCompound(new NBTTagCompound());
	    			PotionUtils.addPotionToItemStack(output, PotionTypes.MUNDANE);
	    		} else if (potionType == PotionTypes.AWKWARD) {
	    			output = input.copy();
	    			output.setTagCompound(outputTag.copy());
	    		}
	    	} else if (allowSplash && INGREDIENT_GUNPOWDER.apply(ingredient) && input.getItem() == Items.POTIONITEM && tagMatchesAny) {
    			output = new ItemStack(Items.SPLASH_POTION, input.getCount(), input.getItemDamage());
    			output.setTagCompound(input.getTagCompound());
	    	} else if ( allowLingering && ingredient.getItem() == Items.DRAGON_BREATH && input.getItem() == Items.SPLASH_POTION && tagMatchesAny) {
    			output = new ItemStack(Items.LINGERING_POTION, input.getCount(), input.getItemDamage());
    			output.setTagCompound(this.getLingeringTag(input.getTagCompound()));
	    	} else if (INGREDIENT_REDSTONE.apply(ingredient) && !outputExtendedTag.hasNoTags() && ! tagMatchesExtended && (tagMatchesNormal || tagMatchesPowered)) {
	    		output = input.copy();
    			output.setTagCompound(input.getItem() == Items.LINGERING_POTION ? this.getLingeringTag(outputExtendedTag.copy()) : outputExtendedTag.copy());
	    	} else if (INGREDIENT_GLOWSTONE.apply(ingredient) && !outputPoweredTag.hasNoTags() && ! tagMatchesPowered && (tagMatchesNormal || tagMatchesExtended)) {
	    		output = input.copy();
    			output.setTagCompound(input.getItem() == Items.LINGERING_POTION ? this.getLingeringTag(outputPoweredTag.copy()) : outputPoweredTag.copy());
	    	}
    	}
    	
        return output;
    }
    
    public NBTTagCompound getLingeringTag(NBTTagCompound tag) {
    	NBTTagCompound lingeringTag = tag == null ? null : tag.copy();
    	
    	if (lingeringTag != null && lingeringTag.hasKey("CustomPotionEffects", 9)) {
			List<PotionEffect> newEffects = new ArrayList<>();
			List<PotionEffect> customEffects = new ArrayList<>();
			PotionUtils.addCustomPotionEffectToList(lingeringTag, customEffects);
			
			for (PotionEffect effect : customEffects) {
				newEffects.add(new PotionEffect(effect.getPotion(), MathHelper.ceil(effect.getDuration() / 4), effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
			}
			
			lingeringTag.removeTag("CustomPotionEffects");
			ItemStack lingeringStack = new ItemStack(Items.LINGERING_POTION);
			lingeringStack.setTagCompound(lingeringTag);
			PotionUtils.appendEffects(lingeringStack, newEffects);
		}
    	
    	return lingeringTag;
    }
	
	public static class Serializer extends IRecipeAdded.Serializer<RecipeAddedBrewingComplete> {
		
		public Serializer() {
			super(TYPE, RecipeAddedBrewingComplete.class);
		}

		public JsonObject serialize(RecipeAddedBrewingComplete recipeAdded, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			if (recipeAdded.ingredient.isEmpty()) {
				throw new IllegalArgumentException("Expected an ingredient for complete brewing recipe " + recipeAdded.id);
			}
			
			json.add("ingredient", IngredientOreNBT.Serializer.serialize(recipeAdded.ingredient));
			
			if (recipeAdded.outputTag.hasNoTags()) {
				throw new IllegalArgumentException("Expected an output tag for complete brewing recipe " + recipeAdded.id);
			}
			
			json.addProperty("output_tag", recipeAdded.outputTag.toString());
			
			if (!recipeAdded.outputExtendedTag.hasNoTags()) {
				json.addProperty("output_extended_tag", recipeAdded.outputExtendedTag.toString());
			}
			
			if (!recipeAdded.outputPoweredTag.hasNoTags()) {
				json.addProperty("output_powered_tag", recipeAdded.outputPoweredTag.toString());
			}
			
			if (!recipeAdded.allowMundane) {
				json.addProperty("allow_mundane", false);
			}
			
			if (!recipeAdded.allowSplash) {
				json.addProperty("allow_splash", false);
			}
			
			if (!recipeAdded.allowLingering) {
				json.addProperty("allow_lingering", false);
			}
			
			return json;
        }
		
		@Override
		public RecipeAddedBrewingComplete deserialize(JsonObject json, JsonDeserializationContext context) {
			RecipeAddedBrewingComplete recipeAdded = new RecipeAddedBrewingComplete();
			
			recipeAdded.ingredient = IngredientOreNBT.Serializer.deserialize(JsonUtils.getJsonObject(json, "ingredient"));
			
			if (recipeAdded.ingredient.isEmpty()) {
				throw new JsonParseException("Expected an ingredient for brewing recipe");
			}
			
			try {
				recipeAdded.outputTag = JsonToNBT.getTagFromJson(JsonUtils.getString(json, "output_tag"));
            } catch (NBTException nbtexception) {
                throw new JsonSyntaxException("Problem getting potion tag for brewing recipe", nbtexception);
            }
			
			if (json.has("output_extended_tag")) {
				try {
					recipeAdded.outputExtendedTag = JsonToNBT.getTagFromJson(JsonUtils.getString(json, "output_extended_tag"));
	            } catch (NBTException nbtexception) {
	                throw new JsonSyntaxException("Problem getting extended potion tag for brewing recipe", nbtexception);
	            }
			}
			
			if (json.has("output_powered_tag")) {
				try {
					recipeAdded.outputPoweredTag = JsonToNBT.getTagFromJson(JsonUtils.getString(json, "output_powered_tag"));
	            } catch (NBTException nbtexception) {
	                throw new JsonSyntaxException("Problem getting powered potion tag for brewing recipe", nbtexception);
	            }
			}

			recipeAdded.allowMundane = JsonUtils.getBoolean(json, "allow_mundane", true);
			recipeAdded.allowSplash = JsonUtils.getBoolean(json, "allow_splash", true);
			recipeAdded.allowLingering = JsonUtils.getBoolean(json, "allow_lingering", true);
			
			return recipeAdded;
		}
    }

}
