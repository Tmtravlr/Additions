package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.recipes.IRecipeAdded;
import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.addon.recipes.RecipeAddedCraftingDyeItem;
import com.tmtravlr.additions.addon.recipes.RecipeAddedManager;
import com.tmtravlr.additions.util.ProblemNotifier;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Added blocks
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018 
 */
@EventBusSubscriber(modid = AdditionsMod.MOD_ID)
public class AdditionTypeRecipe extends AdditionType<IRecipeAdded> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "recipe");
	public static final String FOLDER_NAME = "data" + File.separator + "recipes";
	public static final String FILE_POSTFIX = ".json";
	public static final AdditionTypeRecipe INSTANCE = new AdditionTypeRecipe();
	
	public static final Gson GSON = new GsonBuilder()
			.registerTypeHierarchyAdapter(IRecipeAdded.class, new RecipeAddedManager.Serializer())
			.setPrettyPrinting()
			.create();
	
	private Multimap<Addon, IRecipeAdded> loadedRecipes = HashMultimap.create();
	
	public static List<RecipeAddedCraftingDyeItem> cauldronWashingRecipes = new ArrayList<>();
	
	@SubscribeEvent
	public static void onRegisterRecipes(Register<IRecipe> event) {
		INSTANCE.registerRecipes(event);
	}
	
	public void registerRecipes(Register<IRecipe> event) {
        CraftingHelper.register(IngredientOreNBT.TYPE, (IIngredientFactory) (context, json) -> IngredientOreNBT.Serializer.deserialize(json));
        
		AdditionsMod.logger.info("Loading addon recipes.");
		
		for (Addon addon : AddonLoader.addonsLoaded) {
			List<String> filePaths = new ArrayList<>();
			
			try {
				filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading recipe files for addon " + addon.id + ". The recipes will not load.", e);
			}
			
			for (String filePath : filePaths) {
				try {
					IRecipeAdded recipeAdded = GSON.fromJson(AddonLoader.readAddonFile(addon.addonFolder, filePath), IRecipeAdded.class);
					
					String recipeName = filePath;
					if (recipeName.contains(File.separator)) {
						recipeName = recipeName.substring(recipeName.lastIndexOf(File.separator) + 1);
					}
					
					if (recipeName.endsWith(FILE_POSTFIX)) {
						recipeName = recipeName.substring(0, recipeName.length() - FILE_POSTFIX.length());
					
						ResourceLocation recipeId = new ResourceLocation(AdditionsMod.MOD_ID, recipeName);
						recipeAdded.setId(recipeId);
						
						this.loadedRecipes.put(addon, recipeAdded);
						recipeAdded.registerRecipe(event.getRegistry());
					}
				} catch (IOException | JsonParseException e) {
					AdditionsMod.logger.error("Error loading recipe " + filePath + " for addon " + addon.id + ". The recipe will not load.", e);
					ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentString(e.getMessage()));
				}
			}
		}
	}
	
	@Override
	public List<IRecipeAdded> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.loadedRecipes.get(addon));
	}
	
	public List<IRecipeAdded> getAllAdditions() {
		return new ArrayList<>(this.loadedRecipes.values());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, IRecipeAdded addition) {
		if (!this.loadedRecipes.containsEntry(addon, addition)) {
			this.loadedRecipes.put(addon, addition);
		}
		
		File recipeFolder = new File(addon.addonFolder, FOLDER_NAME);
		
		if (!recipeFolder.isDirectory()) {
			recipeFolder.mkdir();
		}
		
		File additionFile = new File(recipeFolder, addition.getId().getResourcePath() + FILE_POSTFIX);
		
		try {
			String fileContents = GSON.toJson(addition);
			FileUtils.write(additionFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException | IllegalArgumentException e) {
			AdditionsMod.logger.error("Error saving addon " + addon.name, e);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, IRecipeAdded addition) {
		if (this.loadedRecipes.containsEntry(addon, addition)) {
			this.loadedRecipes.remove(addon, addition);
		}
		
		File recipeFolder = new File(addon.addonFolder, FOLDER_NAME);

		File additionFile = new File(recipeFolder, addition.getId().getResourcePath() + FILE_POSTFIX);
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
	}
}
