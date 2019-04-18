package com.tmtravlr.additions.type;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.blocks.BlockAddedManager;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.addon.items.blocks.IItemAddedBlock;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Added blocks
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018 
 */
public class AdditionTypeBlock extends AdditionType<IBlockAdded> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "block");
	public static final String FOLDER_NAME = "data" + File.separator + "blocks";
	public static final String FILE_POSTFIX = ".json";
	public static final AdditionTypeBlock INSTANCE = new AdditionTypeBlock();
	
	public static final Gson GSON = new GsonBuilder()
			.registerTypeHierarchyAdapter(IItemAdded.class, new ItemAddedManager.Serializer())
			.registerTypeHierarchyAdapter(IBlockAdded.class, new BlockAddedManager.Serializer())
			.setPrettyPrinting()
			.create();
	
	private Multimap<Addon, IBlockAdded> loadedBlocks = HashMultimap.create();
	
	// Can't load things like lists of ammo right away, since the items they need may not have loaded yet
	private Map<IBlockAdded, JsonObject> blocksToPostDeserialize = new HashMap<>();

	@Override
	public void loadPreInit(List<Addon> addons, FMLPreInitializationEvent event) {
		AdditionsMod.logger.info("Loading addon blocks.");
		
		for (Addon addon : addons) {
			List<String> filePaths = new ArrayList<>();
			
			try {
				filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading block files for addon " + addon.id + ". The blocks will not load.", e);
			}
			
			for (String filePath : filePaths) {
				try {
					String fileString = AddonLoader.readAddonFile(addon.addonFolder, filePath);
					JsonObject blockJson = GSON.fromJson(fileString, JsonObject.class);
					IBlockAdded blockAdded = GSON.fromJson(blockJson, IBlockAdded.class);
					blocksToPostDeserialize.put(blockAdded, blockJson);
					
					Block block = blockAdded.getAsBlock();
					
					String blockName = filePath;
					if (blockName.contains(File.separator)) {
						blockName = blockName.substring(blockName.lastIndexOf(File.separator) + 1);
					}
					
					if (blockName.endsWith(FILE_POSTFIX)) {
						blockName = blockName.substring(0, blockName.length() - FILE_POSTFIX.length());
					
						ResourceLocation blockRegistryName = new ResourceLocation(AdditionsMod.MOD_ID, blockName);
						
						this.loadedBlocks.put(addon, blockAdded);
						ForgeRegistries.BLOCKS.register(block.setUnlocalizedName(blockName).setRegistryName(blockRegistryName));
					
						IItemAddedBlock itemAdded = blockAdded.getItemBlock();
						if (itemAdded != null) {
							ItemBlock item = itemAdded.getAsItemBlock();
							
							blockName = blockName.substring(0, blockName.length() - FILE_POSTFIX.length());
							
							ForgeRegistries.ITEMS.register(item.setUnlocalizedName(blockName).setRegistryName(blockRegistryName));
							
							for (String oreName : itemAdded.getOreDict()) {
								OreDictionary.registerOre(oreName, item);
							}
						}
					}
				} catch (IOException | JsonParseException e) {
					AdditionsMod.logger.error("Error loading block " + filePath + " for addon " + addon.id + ". The block will not load.", e);
				}
			}
		}
	}

	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		AdditionsMod.logger.info("Initializing addon blocks.");		
		for (Entry<IBlockAdded, JsonObject> blockEntry : this.blocksToPostDeserialize.entrySet()) {
			try {
				BlockAddedManager.Serializer.postDeserialize(blockEntry.getValue(), blockEntry.getKey());
			} catch (JsonParseException e) {
				AdditionsMod.logger.error("There was a problem initializing block " + blockEntry.getKey().getId()  + ". If things continue, the game will probably majorly break, so it should stop loading here.");
				throw e; 
			}
		}
		
		this.blocksToPostDeserialize.clear();
		
		for (Addon addon : addons) {
			for (IBlockAdded block : this.loadedBlocks.get(addon)) {
				block.registerModels();
				
				IItemAddedBlock itemBlock = block.getItemBlock();
				if (itemBlock != null) {
					itemBlock.registerModels();
				}
			}
		}
	}
	
	@Override
	public List<IBlockAdded> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.loadedBlocks.get(addon));
	}
	
	public List<IBlockAdded> getAllAdditions() {
		return new ArrayList<>(this.loadedBlocks.values());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, IBlockAdded addition) {
		if (!this.loadedBlocks.containsEntry(addon, addition)) {
			this.loadedBlocks.put(addon, addition);
		}
		
		File blockFolder = new File(addon.addonFolder, FOLDER_NAME);
		
		if (!blockFolder.isDirectory()) {
			blockFolder.mkdir();
		}
		
		File additionFile = new File(blockFolder, addition.getId() + FILE_POSTFIX);
		
		try {
			String fileContents = GSON.toJson(addition);
			FileUtils.write(additionFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException | IllegalArgumentException e) {
			AdditionsMod.logger.error("Error saving addon " + addon.name, e);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, IBlockAdded addition) {
		if (this.loadedBlocks.containsEntry(addon, addition)) {
			this.loadedBlocks.remove(addon, addition);
		}
		
		File blockFolder = new File(addon.addonFolder, FOLDER_NAME);

		File additionFile = new File(blockFolder, addition.getId() + FILE_POSTFIX);
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
	}
}
