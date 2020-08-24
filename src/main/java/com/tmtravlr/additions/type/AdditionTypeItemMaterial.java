package com.tmtravlr.additions.type;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.items.ItemAddedHat;
import com.tmtravlr.additions.addon.items.ItemAddedMultiTool;
import com.tmtravlr.additions.addon.items.materials.ArmorMaterialAdded;
import com.tmtravlr.additions.addon.items.materials.ItemMaterialAdded;
import com.tmtravlr.additions.addon.items.materials.ToolMaterialAdded;
import com.tmtravlr.additions.util.GeneralUtils;
import com.tmtravlr.additions.util.ProblemNotifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Added item materials, for tools and armor
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since November 2017 
 */
public class AdditionTypeItemMaterial extends AdditionType<ItemMaterialAdded> {

	public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "item_material");
	public static final String FOLDER_NAME = "data" + File.separator + "item_materials";
	public static final String FILE_POSTFIX = JSON_POSTFIX;
	public static final AdditionTypeItemMaterial INSTANCE = new AdditionTypeItemMaterial();
	
	private static final Gson GSON = GeneralUtils.newBuilder()
			.registerTypeHierarchyAdapter(ItemMaterialAdded.class, new ItemMaterialAdded.Serializer())
			.create();
	
	private Multimap<Addon, ItemMaterialAdded> loadedItemMaterials = HashMultimap.create();
	private List<ItemMaterialAdded> vanillaItemMaterials = new ArrayList<>();
	
	// Can't load things like repair materials right away, since the items they need may not have loaded yet
	private Map<ItemMaterialAdded, JsonObject> materialsToPostDeserialize = new HashMap<>();

	@Override
	public void loadPreInit(List<Addon> addons, FMLPreInitializationEvent event) {
		
		//Load vanilla item materials
		for (Item.ToolMaterial toolMaterial : Item.ToolMaterial.values()) {
			if (toolMaterial != ItemAddedMultiTool.MULTI_MATERIAL) {
				vanillaItemMaterials.add(new ToolMaterialAdded(toolMaterial));
			}
		}
		for (ItemArmor.ArmorMaterial armorMaterial : ItemArmor.ArmorMaterial.values()) {
			if (armorMaterial != ItemAddedHat.HAT_MATERIAL) {
				vanillaItemMaterials.add(new ArmorMaterialAdded(armorMaterial));
			}
		}
		
		//Load new item materials
		AdditionsMod.logger.info("Loading addon item materials.");
		for (Addon addon : addons) {
			List<String> filePaths = new ArrayList<>();
			
			try {
				filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
			} catch (IOException e) {
				AdditionsMod.logger.error("Error loading item material files for addon " + addon.id + ". The item materials will not load.", e);
				ProblemNotifier.addProblemNotification(new TextComponentTranslation("gui.view.addon.itemMaterials.title", addon.id), new TextComponentString(e.getMessage()));
			}
			
			for (String filePath : filePaths) {
				try {
					String fileString = AddonLoader.readAddonFile(addon.addonFolder, filePath);
					
					String materialId = filePath;
					if (materialId.contains(File.separator)) {
						materialId = materialId.substring(materialId.lastIndexOf(File.separator) + 1);
					}

					if (materialId.endsWith(FILE_POSTFIX)) {
						materialId = materialId.substring(0, materialId.length() - FILE_POSTFIX.length());
					}
					
					ItemMaterialAdded.setNextMaterialId(materialId);
					JsonObject materialJson = GSON.fromJson(fileString, JsonObject.class);
					ItemMaterialAdded material = GSON.fromJson(materialJson, ItemMaterialAdded.class);
					this.materialsToPostDeserialize.put(material, materialJson);
					
					this.loadedItemMaterials.put(addon, material);
				} catch (IOException | JsonParseException e) {
					AdditionsMod.logger.error("Error loading item material " + filePath + " for addon " + addon.id + ". The item material will not load.", e);
					ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentString(e.getMessage()));
				}
			}
		}
	}

	@Override
	public void loadInit(List<Addon> addons, FMLInitializationEvent event) {
		AdditionsMod.logger.info("Initializing addon item materials.");
		
		for (Entry<ItemMaterialAdded, JsonObject> itemEntry : this.materialsToPostDeserialize.entrySet()) {
			try {
				ItemMaterialAdded.Serializer.postDeserialize(itemEntry.getValue(), itemEntry.getKey());
			} catch (JsonParseException e) {
				AdditionsMod.logger.error("There was a problem initializing item material " + itemEntry.getKey().getId()  + ". If things continue, the game will probably majorly break, so it should stop loading here.");
				throw e; 
			}
		}
		
		this.materialsToPostDeserialize.clear();
	}
	
	@Override
	public List<ItemMaterialAdded> getAllAdditions(Addon addon) {
		return new ArrayList<>(this.loadedItemMaterials.get(addon));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void saveAddition(Addon addon, ItemMaterialAdded addition) {
		if (!this.loadedItemMaterials.containsEntry(addon, addition)) {
			this.loadedItemMaterials.put(addon, addition);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);
		
		if (!additionFolder.isDirectory()) {
			additionFolder.mkdirs();
		}
		
		String fileName = addition.getId();
		
		if (fileName.startsWith(AdditionsMod.MOD_ID + "-")) {
			fileName = fileName.substring(AdditionsMod.MOD_ID.length() + 1);
		}
		
		File additionFile = new File(additionFolder, fileName + ".json");
		
		try {
			String fileContents = GSON.toJson(addition);
			FileUtils.write(additionFile, fileContents, StandardCharsets.UTF_8);
		} catch (IOException | IllegalArgumentException e) {
			AdditionsMod.logger.error("Error saving item material " + addition.getId(), e);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void deleteAddition(Addon addon, ItemMaterialAdded addition) {
		if (this.loadedItemMaterials.containsEntry(addon, addition)) {
			this.loadedItemMaterials.remove(addon, addition);
		}
		
		File additionFolder = new File(addon.addonFolder, FOLDER_NAME);
		
		String fileName = addition.getId();
		
		if (fileName.startsWith("additions.")) {
			fileName = fileName.substring(10);
		}

		File additionFile = new File(additionFolder, fileName + ".json");
		
		if (additionFile.exists()) {
			additionFile.delete();
		}
	}
	
	public boolean hasItemMaterialWithId(Addon addon, String id) {
		for (ItemMaterialAdded addition : this.getAllAdditions(addon)) {
			if (addition.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public Collection<ItemMaterialAdded> getAllItemMaterials() {
		List<ItemMaterialAdded> allMaterials = new ArrayList<>(this.loadedItemMaterials.values());
		allMaterials.addAll(this.vanillaItemMaterials);
		return allMaterials;
	}
	
	public List<ArmorMaterialAdded> getAllArmorMaterials() {
		return this.getAllItemMaterials().stream().filter(ItemMaterialAdded::isArmorMaterial).map(ItemMaterialAdded::getArmorMaterialAdded).collect(Collectors.toList());
	}
	
	public List<ToolMaterialAdded> getAllToolMaterials() {
		return this.getAllItemMaterials().stream().filter(ItemMaterialAdded::isToolMaterial).map(ItemMaterialAdded::getToolMaterialAdded).collect(Collectors.toList());
	}
	
	public ArmorMaterialAdded getArmorMaterialWithId(String id) {
		return this.getAllArmorMaterials().stream().filter(itemMaterial -> itemMaterial.getId().equals(id)).findAny().orElse(null);
	}
	
	public ToolMaterialAdded getToolMaterialWithId(String id) {
		return this.getAllToolMaterials().stream().filter(itemMaterial -> itemMaterial.getId().equals(id)).findAny().orElse(null);
	}
	
	public ArmorMaterialAdded getMaterialWithArmorMaterial(ItemArmor.ArmorMaterial armorMaterial) {
		return this.getAllArmorMaterials().stream().filter(itemMaterial -> itemMaterial.getArmorMaterial() == armorMaterial).findAny().orElse(null);
	}
	
	public ToolMaterialAdded getMaterialWithToolMaterial(Item.ToolMaterial toolMaterial) {
		return this.getAllToolMaterials().stream().filter(itemMaterial -> itemMaterial.getToolMaterial() == toolMaterial).findAny().orElse(null);
	}

}
