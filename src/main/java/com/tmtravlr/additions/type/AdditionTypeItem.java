package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedManager;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Added items
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class AdditionTypeItem extends AdditionType<IItemAdded> {
	
	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "item");
	public static final AdditionTypeItem INSTANCE = new AdditionTypeItem();
	
	private static final String DEFAULT_ITEM_MODEL = "{\n"
			+ "  \"parent\": \"item/generated\",\n"
			+ "  \"textures\": {\n"
			+ "    \"layer0\": \"additions:items/%s\"\n"
			+ "  }\n"
			+ "}";
	private static final String DEFAULT_ANIMATION = "{\n"
			+ "  \"animation\": {\n"
			+ "    \"frametime\": 1\n"
			+ "  }\n"
			+ "}";
	private static final Gson GSON = new GsonBuilder()
			.registerTypeHierarchyAdapter(IItemAdded.class, new ItemAddedManager.Serializer())
			.setPrettyPrinting()
			.create();
	
	private HashMap<String, List<IItemAdded>> loadedItems = new HashMap<>();

	@Override
	public void loadPreInit(List<Addon> addons, FMLPreInitializationEvent event) {
		AdditionsMod.logger.info("Loading addon items.");
		
		for (Addon addon : addons) {
			this.loadedItems.put(addon.id, new ArrayList<>());
			
			File itemFolder = new File(addon.addonFolder, "items");
			
			if (itemFolder.isDirectory()) {
				for (File file : itemFolder.listFiles()) {
					try {
						String fileString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
						IItemAdded itemAdded = GSON.fromJson(fileString, IItemAdded.class);
						
						Item item = itemAdded.getAsItem();
						
						String itemName = file.getName();
						
						if (itemName.endsWith(".json")) {
							itemName = itemName.substring(0, itemName.length()-5);
						
							ResourceLocation itemRegistryName = new ResourceLocation(AdditionsMod.MOD_ID, itemName);
							
							this.loadedItems.get(addon.id).add(itemAdded);
							ForgeRegistries.ITEMS.register(item.setUnlocalizedName(itemName).setRegistryName(itemRegistryName));
							
							for (String oreName : itemAdded.getOreDict()) {
								OreDictionary.registerOre(oreName, item);
							}
							
							AdditionsMod.logger.info("Loaded item '" + itemRegistryName + "'");
						}
					} catch (IOException e) {
						AdditionsMod.logger.error("Error loading item " + file + ". The item will not load.", e);
					} catch (JsonSyntaxException e) {
						AdditionsMod.logger.error("Error loading item " + file + ". The item will not load.", e);
					}
				}
			}
		}
	}

	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		AdditionsMod.logger.info("Registering item models.");
		List<Item> colorItemsToRegister = new ArrayList<>();
		
		for (Addon addon : addons) {
			for (IItemAdded item : loadedItems.get(addon.id)) {
				item.registerModels();
				colorItemsToRegister.add(item.getAsItem());
			}
		}
		
		if(!colorItemsToRegister.isEmpty()) {
			AdditionsMod.proxy.registerItemColors(colorItemsToRegister.toArray(new Item[0]));
			colorItemsToRegister.clear();
		}
	}

	@Override
	public void loadPostInit(List<Addon> addons, FMLPostInitializationEvent event) {}

	@Override
	public void loadServerStarting(List<Addon> addons, FMLServerStartingEvent event) {}

	@Override
	public void setupNewAddon(Addon addon) {
		this.loadedItems.put(addon.id, new ArrayList<>());
	}
	
	@Override
	public List<IItemAdded> getAllAdditions(Addon addon) {
		return this.loadedItems.get(addon.id);
	}

	@Override
	public void saveAddition(Addon addon, IItemAdded addition) {
		if (!this.loadedItems.get(addon.id).contains(addition)) {
			this.loadedItems.get(addon.id).add(addition);
		}
		
		File itemFolder = new File(addon.addonFolder, "items");
		
		if (!itemFolder.isDirectory()) {
			itemFolder.mkdir();
		}
		
		File additionFile = new File(itemFolder, addition.getId() + ".json");
		
		try {
			String fileContents = GSON.toJson(addition);
			FileUtils.write(additionFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException e) {
			AdditionsMod.logger.warn("Error saving addon " + addon.name, e);
		} catch (IllegalArgumentException e) {
			AdditionsMod.logger.error("Error saving addon " + addon.name, e);
		}
	}
	
	@Override
	public void deleteAddition(Addon addon, IItemAdded addition) {
		if (this.loadedItems.get(addon.id).contains(addition)) {
			this.loadedItems.get(addon.id).remove(addition);
		}
		
		File itemFolder = new File(addon.addonFolder, "items");

		File additionFile = new File(itemFolder, addition.getId() + ".json");
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
	}
	
	public void saveAddonTexture(Addon addon, IItemAdded item, File texture) throws IOException {
		File modelFolder = this.getItemModelFolder(addon);
		File textureFolder = new File(addon.addonFolder, "assets/additions/textures/items");
		
		if (!modelFolder.isDirectory()) {
			modelFolder.mkdirs();
		}
		if (!textureFolder.isDirectory()) {
			textureFolder.mkdirs();
		}
		
		File modelFile = new File(modelFolder, this.getItemModelName(item));
		String modelFileContents = String.format(DEFAULT_ITEM_MODEL, item.getId());
		
		File textureFile = new File(textureFolder, item.getId()+".png");
		
		FileUtils.writeStringToFile(modelFile, modelFileContents, StandardCharsets.UTF_8);
		FileUtils.copyFile(texture, textureFile);
	}
	
	public void saveTextureAnimation(Addon addon, IItemAdded item, @Nullable File textureAnimation) throws IOException {
		File textureFolder = new File(addon.addonFolder, "assets/additions/textures/items");
		
		if (!textureFolder.isDirectory()) {
			textureFolder.mkdirs();
		}
		
		File animationFile = new File(textureFolder, item.getId()+".png.mcmeta");

		if (textureAnimation != null) {
			FileUtils.copyFile(textureAnimation, animationFile);
		} else {			
			FileUtils.writeStringToFile(animationFile, DEFAULT_ANIMATION, StandardCharsets.UTF_8);
		}
		
	}
	
	public String getItemModelName(IItemAdded item) {
		return item.getId() + ".json";
	}
	
	public File getItemModelFolder(Addon addon) {
		return new File(addon.addonFolder, "assets/additions/models/item");
	}

}
