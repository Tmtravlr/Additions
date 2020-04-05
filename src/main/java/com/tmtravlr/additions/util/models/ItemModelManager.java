package com.tmtravlr.additions.util.models;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedArmor;
import com.tmtravlr.additions.addon.items.ItemAddedBow;
import com.tmtravlr.additions.addon.items.ItemAddedHat;

import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemModelManager {
	
	public static final String TEXTURE_FILE_POSTFIX = ".png";
	public static final String ANIMATION_FILE_POSTFIX = ".png.mcmeta";
	public static final String MODEL_FILE_POSTFIX = ".json";
	
	public static final String TEXTURE_BASE_ENDING = "-layer0";
	public static final String TEXTURE_COLOR_ENDING = "-layer1";
	public static final String TEXTURE_ARMOR_OVERLAY_ENDING = "-overlay";
	
	public static final String MODEL_SHIELD_BLOCKING_ENDING = "-blocking";
	public static final String MODEL_BOW_PULLING_0_ENDING = "-pulling_0";
	public static final String MODEL_BOW_PULLING_1_ENDING = "-pulling_1";
	public static final String MODEL_BOW_PULLING_2_ENDING = "-pulling_2";
	
	public static void saveItemTexture(Addon addon, IItemAdded item, File textureBase, @Nullable File textureColor, ItemModelType type) throws IOException {
		File modelFolder = getItemModelFolder(addon);
		File textureFolder = getItemTextureFolder(addon);
		
		String modelFileContents;
		
		switch (type) {
		case TOOL:
			modelFileContents = ItemModelGenerator.getToolItemModel(item.getId(), textureColor != null);
			break;
		case SHIELD:
			modelFileContents = ItemModelGenerator.getShieldItemModel(item.getId(), textureColor != null);
			break;
		case HAT:
			modelFileContents = ItemModelGenerator.getHatItemModel(item.getId(), textureColor != null);
			break;
		case BOW:
			modelFileContents = ItemModelGenerator.getBowItemModel(item.getId(), textureColor != null);
			break;
		case GUN:
			modelFileContents = ItemModelGenerator.getGunItemModel(item.getId(), textureColor != null);
			break;
		default:
			modelFileContents = ItemModelGenerator.getSimpleItemModel(item.getId(), textureColor != null);
		}
		
		File modelFile = new File(modelFolder, getItemModelName(item));
		FileUtils.writeStringToFile(modelFile, modelFileContents, StandardCharsets.UTF_8);
		
		File textureFile0 = new File(textureFolder, item.getId() + TEXTURE_BASE_ENDING + TEXTURE_FILE_POSTFIX);
		FileUtils.copyFile(textureBase, textureFile0);
		
		if (textureColor != null) {
			File textureFile1 = new File(textureFolder, item.getId() + TEXTURE_COLOR_ENDING + TEXTURE_FILE_POSTFIX);
			FileUtils.copyFile(textureColor, textureFile1);
		}
		
		//Add extra model file for shields blocking
		if (type == ItemModelType.SHIELD) {
			saveShieldBlockingModel(addon, item, textureColor != null);
		}
	}
	
	public static void saveItemBlockModel(Addon addon, IBlockAdded block, ItemBlockModelType type) throws IOException {
		if (block.getItemBlock() != null) {
			String modelFileContents;
			
			switch (type) {
			case SIMPLE:
				modelFileContents = ItemModelGenerator.getSimpleItemBlockModel(block.getId());
				break;
			case INVENTORY:
				modelFileContents = ItemModelGenerator.getItemBlockModel(block.getId() + BlockModelManager.MODEL_INVENTORY_ENDING);
				break;
			default:
				modelFileContents = ItemModelGenerator.getItemBlockModel(block.getId());
			}
			
			File modelFile = new File(getItemModelFolder(addon), block.getItemBlock().getId() + MODEL_FILE_POSTFIX);
			FileUtils.writeStringToFile(modelFile, modelFileContents, StandardCharsets.UTF_8);
		}
	}
	
	public static void deleteItemTexture(Addon addon, IItemAdded item, boolean isColorLayer) {
		File textureFolder = getItemTextureFolder(addon);
		File textureFile = new File(textureFolder, item.getId() + (isColorLayer ? TEXTURE_COLOR_ENDING : TEXTURE_BASE_ENDING) + TEXTURE_FILE_POSTFIX);
		
		if (textureFile.exists()) {
			textureFile.delete();
		}
	}
	
	public static void saveShieldBlockingModel(Addon addon, IItemAdded item, boolean isColorLayer) throws IOException {
		File modelBlockingFile = new File(getItemModelFolder(addon), item.getId() + MODEL_SHIELD_BLOCKING_ENDING + MODEL_FILE_POSTFIX);
		FileUtils.writeStringToFile(modelBlockingFile, ItemModelGenerator.getShieldBlockingItemModel(item.getId(), isColorLayer), StandardCharsets.UTF_8);
	}
	
	public static void saveBowPullingTexture(Addon addon, ItemAddedBow item, File textureBase, File textureColor, String textureEnding) throws IOException {
		File textureFolder = getItemTextureFolder(addon);
		File modelBlockingFile = new File(getItemModelFolder(addon), item.getId() + textureEnding + MODEL_FILE_POSTFIX);
		FileUtils.writeStringToFile(modelBlockingFile, ItemModelGenerator.getBowPullingItemModel(item.getId(), textureEnding, textureColor != null), StandardCharsets.UTF_8);

		File textureFile0 = new File(textureFolder, item.getId() + textureEnding + TEXTURE_BASE_ENDING + TEXTURE_FILE_POSTFIX);
		FileUtils.copyFile(textureBase, textureFile0);
		
		if (textureColor != null) {
			File textureFile1 = new File(textureFolder, item.getId() + textureEnding + TEXTURE_COLOR_ENDING + TEXTURE_FILE_POSTFIX);
			FileUtils.copyFile(textureColor, textureFile1);
		}
	}
	
	public static void deleteBowPullingTexture(Addon addon, ItemAddedBow item, String textureEnding, boolean isColorLayer) {
		File textureFolder = getItemTextureFolder(addon);
		File textureFile = new File(textureFolder, item.getId() + textureEnding + (isColorLayer ? TEXTURE_COLOR_ENDING : TEXTURE_BASE_ENDING) + TEXTURE_FILE_POSTFIX);
		
		if (textureFile.exists()) {
			textureFile.delete();
		}
	}
	
	public static void saveTextureAnimation(Addon addon, IItemAdded item, boolean isColorLayer, @Nullable File textureAnimation) throws IOException {
		File textureFolder = getItemTextureFolder(addon);
		
		File animationFile = new File(textureFolder, item.getId() + (isColorLayer ? TEXTURE_COLOR_ENDING : TEXTURE_BASE_ENDING) + ANIMATION_FILE_POSTFIX);

		if (textureAnimation != null) {
			FileUtils.copyFile(textureAnimation, animationFile);
		} else {			
			FileUtils.writeStringToFile(animationFile, ModelGenerator.getAnimation(), StandardCharsets.UTF_8);
		}
		
	}
	
	public static void deleteTextureAnimation(Addon addon, IItemAdded item, boolean isColorLayer) throws IOException {
		File textureFolder = getItemTextureFolder(addon);
		File animationFile = new File(textureFolder, item.getId() + (isColorLayer ? TEXTURE_COLOR_ENDING : TEXTURE_BASE_ENDING) + ANIMATION_FILE_POSTFIX);
		
		if (animationFile.exists()) {
			animationFile.delete();
		}
	}
	
	public static void saveBowPullingTextureAnimation(Addon addon, ItemAddedBow item, String textureEnding, boolean isColorLayer, @Nullable File textureAnimation) throws IOException {
		File textureFolder = getItemTextureFolder(addon);
		
		File animationFile = new File(textureFolder, item.getId() + textureEnding + (isColorLayer ? TEXTURE_COLOR_ENDING : TEXTURE_BASE_ENDING) + ANIMATION_FILE_POSTFIX);

		if (textureAnimation != null) {
			FileUtils.copyFile(textureAnimation, animationFile);
		} else {			
			FileUtils.writeStringToFile(animationFile, ModelGenerator.getAnimation(), StandardCharsets.UTF_8);
		}
		
	}
	
	public static void deleteBowPullingTextureAnimation(Addon addon, ItemAddedBow item, String textureEnding, boolean isColorLayer) throws IOException {
		File textureFolder = getItemTextureFolder(addon);
		File animationFile = new File(textureFolder, item.getId() + textureEnding + (isColorLayer ? TEXTURE_COLOR_ENDING : TEXTURE_BASE_ENDING) + ANIMATION_FILE_POSTFIX);
		
		if (animationFile.exists()) {
			animationFile.delete();
		}
	}
	
	public static void saveArmorTexture(Addon addon, ItemAddedArmor itemArmor, File texture, boolean loadingOverlay) throws IOException {
		File textureFolder = getItemArmorTextureFolder(addon);
		File textureFile = new File(textureFolder, itemArmor.getId() + (loadingOverlay ? TEXTURE_ARMOR_OVERLAY_ENDING : "") + TEXTURE_FILE_POSTFIX);
		
		FileUtils.copyFile(texture, textureFile);
	}
	
	public static void deleteArmorOverlayTexture(Addon addon, ItemAddedArmor itemArmor) throws IOException {
		File textureFolder = getItemArmorTextureFolder(addon);
		File textureFile = new File(textureFolder, itemArmor.getId() + TEXTURE_ARMOR_OVERLAY_ENDING + TEXTURE_FILE_POSTFIX);
		
		if (textureFile.exists()) {
			textureFile.delete();
		}
	}
	
	public static boolean doesArmorOverlayExist(Addon addon, ItemAddedArmor itemArmor) {
		File textureFolder = getItemArmorTextureFolder(addon);
		File textureFile = new File(textureFolder, itemArmor.getId() + TEXTURE_ARMOR_OVERLAY_ENDING + TEXTURE_FILE_POSTFIX);
		
		return textureFile.exists();
	}
	
	public static void saveHatOverlayTexture(Addon addon, ItemAddedHat itemHat, File texture) throws IOException {
		File textureFolder = getItemHatOverlayTextureFolder(addon);
		File textureFile = new File(textureFolder, itemHat.getId() + TEXTURE_FILE_POSTFIX);
		
		FileUtils.copyFile(texture, textureFile);
	}
	
	public static void deleteHatOverlayTexture(Addon addon, ItemAddedHat itemHat) throws IOException {
		File textureFolder = getItemHatOverlayTextureFolder(addon);
		File textureFile = new File(textureFolder, itemHat.getId() + TEXTURE_FILE_POSTFIX);
		
		if (textureFile.exists()) {
			textureFile.delete();
		}
	}
	
	public static boolean doesHatOverlayTextureExist(Addon addon, ItemAddedHat itemHat) {
		File textureFolder = getItemHatOverlayTextureFolder(addon);
		File textureFile = new File(textureFolder, itemHat.getId() + TEXTURE_FILE_POSTFIX);
		
		return textureFile.exists();
	}
	
	public static String getItemModelName(IItemAdded item) {
		return item.getId() + MODEL_FILE_POSTFIX;
	}
	
	public static String getArmorModelName(IItemAdded item, boolean overlay) {
		return item.getId() + TEXTURE_FILE_POSTFIX + (overlay ? TEXTURE_ARMOR_OVERLAY_ENDING : "");
	}
	
	public static String getHatOverlayTextureName(IItemAdded item) {
		return item.getId() + TEXTURE_FILE_POSTFIX;
	}
	
	public static File getItemArmorTextureFolder(Addon addon) {
		File textureFolder = new File(addon.addonFolder, "assets" + File.separator + "additions" + File.separator + "textures" + File.separator + "models" + File.separator + "armor");
		
		if (!textureFolder.isDirectory()) {
			textureFolder.mkdirs();
		}
		
		return textureFolder;
	}
	
	public static File getItemHatOverlayTextureFolder(Addon addon) {
		File textureFolder = new File(addon.addonFolder, "assets" + File.separator + "additions" + File.separator + "textures" + File.separator + "overlays");
		
		if (!textureFolder.isDirectory()) {
			textureFolder.mkdirs();
		}
		
		return textureFolder;
	}
	
	public static File getItemModelFolder(Addon addon) {
		File modelFolder = new File(addon.addonFolder, "assets" + File.separator + "additions" + File.separator + "models" + File.separator + "item");
		
		if (!modelFolder.isDirectory()) {
			modelFolder.mkdirs();
		}
		
		return modelFolder;
	}
	
	public static File getItemTextureFolder(Addon addon) {
		File textureFolder = new File(addon.addonFolder, "assets" + File.separator + "additions" + File.separator + "textures" + File.separator + "items");
		
		if (!textureFolder.isDirectory()) {
			textureFolder.mkdirs();
		}
		
		return textureFolder;
	}

	public static enum ItemModelType {
		SIMPLE,
		TOOL,
		SHIELD,
		HAT,
		BOW,
		GUN
	}

	public static enum ItemBlockModelType {
		BLOCK,
		SIMPLE,
		INVENTORY
	}
}
