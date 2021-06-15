package com.tmtravlr.additions.addon.recipes;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * An added recipe for tipping a projectile, like vanilla tipped arrows
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date March 2019
 */
public class RecipeAddedCraftingPotionTipping extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe, IRecipeAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "crafting_potion_tipping");
	
	public Item untippedProjectile;
	public Item tippedProjectile;

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
        if (inv.getWidth() == 3 && inv.getHeight() == 3) {
            for (int x = 0; x < inv.getWidth(); ++x) {
                for (int y = 0; y < inv.getHeight(); ++y) {
                    ItemStack craftingStack = inv.getStackInRowAndColumn(x, y);

                    if (craftingStack.isEmpty()) {
                        return false;
                    }

                    Item item = craftingStack.getItem();

                    if (x == 1 && y == 1) {
                        if (item != Items.LINGERING_POTION) {
                            return false;
                        }
                    } else if (item != this.untippedProjectile) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

	@Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack middleStack = inv.getStackInRowAndColumn(1, 1);

        if (middleStack.getItem() != Items.LINGERING_POTION) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack1 = new ItemStack(this.tippedProjectile, 8);
            PotionUtils.addPotionToItemStack(itemstack1, PotionUtils.getPotionFromItem(middleStack));
            PotionUtils.appendEffects(itemstack1, PotionUtils.getFullEffectsFromItem(middleStack));
            return itemstack1;
        }
    }

	@Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(this.tippedProjectile, 8);
    }

	@Override
    public boolean isDynamic() {
        return true;
    }

	@Override
    public boolean canFit(int width, int height)  {
        return width >= 2 && height >= 2;
    }
	
	@Override
	public void registerRecipe(IForgeRegistry<IRecipe> registry) {
		registry.register(this);
	}
	
	public static class Serializer extends IRecipeAdded.Serializer<RecipeAddedCraftingPotionTipping> {
		
		public Serializer() {
			super(TYPE, RecipeAddedCraftingPotionTipping.class);
		}

		public JsonObject serialize(RecipeAddedCraftingPotionTipping recipeAdded, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.addProperty("untipped_projectile", recipeAdded.untippedProjectile.getRegistryName().toString());
			json.addProperty("tipped_projectile", recipeAdded.tippedProjectile.getRegistryName().toString());
			
			return json;
        }
		
		@Override
		public RecipeAddedCraftingPotionTipping deserialize(JsonObject json, JsonDeserializationContext context) {
			RecipeAddedCraftingPotionTipping recipeAdded = new RecipeAddedCraftingPotionTipping();
			
			recipeAdded.untippedProjectile = JsonUtils.getItem(json, "untipped_projectile");
			recipeAdded.tippedProjectile = JsonUtils.getItem(json, "tipped_projectile");
			
			return recipeAdded;
		}
    }

}
