package com.tmtravlr.additions.addon.recipes;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.type.AdditionTypeRecipe;
import com.tmtravlr.additions.util.OtherSerializers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * An added recipe for dying an item, and optionally washing it off in a cauldron too.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date March 2019
 */
public class RecipeAddedCraftingDyeItem extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe, IRecipeAdded {
	
	public static final ResourceLocation TYPE = new ResourceLocation(AdditionsMod.MOD_ID, "crafting_dye_item");
	
	public Item itemToDye;
	public boolean washInCauldron = false;
	public String[] colorTagPath = new String[]{"display", "color"};

	@Override
	public void setId(ResourceLocation id) {
		this.setRegistryName(id);
	}

	@Override
	public ResourceLocation getId() {
		return this.getRegistryName();
	}
	
	@Override
	public void registerRecipe(IForgeRegistry<IRecipe> registry) {
		registry.register(this);
		
		if (this.washInCauldron) {
			AdditionTypeRecipe.INSTANCE.cauldronWashingRecipes.add(this);
		}
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack dyeableStack = ItemStack.EMPTY;
        List<ItemStack> dyeList = Lists.<ItemStack>newArrayList();
        
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack craftingStack = inv.getStackInSlot(i);
            
            if (!craftingStack.isEmpty()) {

	            if (craftingStack.getItem() == this.itemToDye) {
	            	if (!dyeableStack.isEmpty()) {
	            		return false;
	            	}
	            	dyeableStack = craftingStack;
	            } else if (DyeUtils.isDye(craftingStack)) {
	            	dyeList.add(craftingStack);
	            } else {
	            	return false;
	            }
            }
        }
        
        return !dyeableStack.isEmpty() && !dyeList.isEmpty();
    }

	@Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack dyedStack = ItemStack.EMPTY;
        int[] colorArray = new int[3];
        int maxColor = 0;
        int dyeCount = 0;

        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack stackInSlot = inv.getStackInSlot(k);

            if (!stackInSlot.isEmpty()) {
                if (stackInSlot.getItem() == this.itemToDye) {

                    dyedStack = stackInSlot.copy();
                    dyedStack.setCount(1);

                    if (this.hasColor(stackInSlot)) {
                        int color = this.getColor(dyedStack);
                        float red = (float)(color >> 16 & 255) / 255.0F;
                        float green = (float)(color >> 8 & 255) / 255.0F;
                        float blue = (float)(color & 255) / 255.0F;
                        maxColor = (int)((float)maxColor + Math.max(red, Math.max(green, blue)) * 255.0F);
                        colorArray[0] = (int)((float)colorArray[0] + red * 255.0F);
                        colorArray[1] = (int)((float)colorArray[1] + green * 255.0F);
                        colorArray[2] = (int)((float)colorArray[2] + blue * 255.0F);
                        ++dyeCount;
                    }
                } else {
                    if (!DyeUtils.isDye(stackInSlot)) {
                        return ItemStack.EMPTY;
                    }

                    float[] stackColorArray = DyeUtils.colorFromStack(stackInSlot).get().getColorComponentValues();
                    int stackRed = (int)(stackColorArray[0] * 255.0F);
                    int stackGreen = (int)(stackColorArray[1] * 255.0F);
                    int stackBlue = (int)(stackColorArray[2] * 255.0F);
                    maxColor += Math.max(stackRed, Math.max(stackGreen, stackBlue));
                    colorArray[0] += stackRed;
                    colorArray[1] += stackGreen;
                    colorArray[2] += stackBlue;
                    ++dyeCount;
                }
            }
        }

        if (!dyedStack.isEmpty() && dyeCount > 0) {
        	int averageRed = colorArray[0] / dyeCount;
            int averageGreen = colorArray[1] / dyeCount;
            int averageBlue = colorArray[2] / dyeCount;
            float averageMaxColor = (float)maxColor / (float)dyeCount;
            float maxAverageColor = (float)Math.max(averageRed, Math.max(averageGreen, averageBlue));
            averageRed = (int)((float)averageRed * averageMaxColor / maxAverageColor);
            averageGreen = (int)((float)averageGreen * averageMaxColor / maxAverageColor);
            averageBlue = (int)((float)averageBlue * averageMaxColor / maxAverageColor);
            int color = (averageRed << 8) + averageGreen;
            color = (color << 8) + averageBlue;
            this.setColor(dyedStack, color);
        }
        
        return dyedStack;
    }

	@Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

	@Override
    public boolean isDynamic() {
        return true;
    }

	@Override
    public boolean canFit(int width, int height)  {
		return width * height >= 2;
    }
	
	public boolean hasColor(ItemStack stack) {
		boolean hasColor = false;
		
		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			
			for (int i = 0; i < this.colorTagPath.length; i++) {
				String key = this.colorTagPath[i];
				
				if (i == this.colorTagPath.length - 1) {
					hasColor = tag.hasKey(key);
				} else {
					if (tag.hasKey(key, 10)) {
						tag = tag.getCompoundTag(key);
					} else {
						break;
					}
				}
			}
		}
		
		return hasColor;
	}
	
	public int getColor(ItemStack stack) {
		int color = 0;
		
		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			
			for (int i = 0; i < this.colorTagPath.length; i++) {
				String key = this.colorTagPath[i];
				
				if (i == this.colorTagPath.length - 1) {
					color = tag.getInteger(key);
				} else {
					if (tag.hasKey(key, 10)) {
						tag = tag.getCompoundTag(key);
					} else {
						break;
					}
				}
			}
		}
		
		return color;
	}
	
	public void setColor(ItemStack stack, int color) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		
		for (int i = 0; i < this.colorTagPath.length; i++) {
			String key = this.colorTagPath[i];
			
			if (i == this.colorTagPath.length - 1) {
				tag.setInteger(key, color);
			} else {
				if (tag.hasKey(key, 10)) {
					tag = tag.getCompoundTag(key);
				} else {
					NBTTagCompound innerTag = new NBTTagCompound();
					tag.setTag(key, innerTag);
					tag = innerTag;
				}
			}
		}
	}
	
	public void removeColor(ItemStack stack) {
		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			
			for (int i = 0; i < this.colorTagPath.length; i++) {
				String key = this.colorTagPath[i];
				
				if (i == this.colorTagPath.length - 1) {
					tag.removeTag(key);
				} else {
					if (tag.hasKey(key, 10)) {
						tag = tag.getCompoundTag(key);
					} else {
						NBTTagCompound innerTag = new NBTTagCompound();
						tag.setTag(key, innerTag);
						tag = innerTag;
					}
				}
			}
		}
	}
	
	public static class Serializer extends IRecipeAdded.Serializer<RecipeAddedCraftingDyeItem> {
		
		public Serializer() {
			super(TYPE, RecipeAddedCraftingDyeItem.class);
		}

		public JsonObject serialize(RecipeAddedCraftingDyeItem recipeAdded, JsonSerializationContext context) {
			JsonObject json = new JsonObject();
			
			json.addProperty("item_to_dye", recipeAdded.itemToDye.getRegistryName().toString());
			
			if (recipeAdded.washInCauldron) {
				json.addProperty("wash_in_cauldron", true);
			}
			
			if (!(recipeAdded.colorTagPath.length == 2 && "display".equals(recipeAdded.colorTagPath[0]) && "color".equals(recipeAdded.colorTagPath[1]))) {
				json.add("color_tag_path", OtherSerializers.StringListSerializer.serialize(recipeAdded.colorTagPath));
			}
			
			return json;
        }
		
		@Override
		public RecipeAddedCraftingDyeItem deserialize(JsonObject json, JsonDeserializationContext context) {
			RecipeAddedCraftingDyeItem recipeAdded = new RecipeAddedCraftingDyeItem();
			
			recipeAdded.itemToDye = JsonUtils.getItem(json, "item_to_dye");
			recipeAdded.washInCauldron = JsonUtils.getBoolean(json, "wash_in_cauldron", false);
			
			if (json.has("color_tag_path")) {
				recipeAdded.colorTagPath = OtherSerializers.StringListSerializer.deserialize(json.get("color_tag_path"), "color_tag_path").toArray(new String[0]);
			} else {
				recipeAdded.colorTagPath = new String[]{"display", "color"};
			}
			
			return recipeAdded;
		}
    }

}
