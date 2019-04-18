package com.tmtravlr.additions.util.models;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;

public class BlockModelManager {

	public static final String TEXTURE_FILE_POSTFIX = ".png";
	public static final String ANIMATION_FILE_POSTFIX = ".png.mcmeta";
	public static final String MODEL_FILE_POSTFIX = ".json";
	
	public static void saveBlockTexture(Addon addon, IBlockAdded block, File texture, BlockModelType type) throws IOException {
		File blockStateFolder = getBlockStateFolder(addon);
		File modelFolder = getBlockModelFolder(addon);
		File textureFolder = getBlockTextureFolder(addon);
		
		String blockStateFileContents;
		String modelFileContents;
		
		switch (type) {
		default:
			blockStateFileContents = BlockModelGenerator.getSimpleBlockState(block.getId());
			modelFileContents = BlockModelGenerator.getSimpleBlockModel(block.getId());
		}
		
		File blockStateFile = new File(blockStateFolder, getBlockModelName(block));
		FileUtils.writeStringToFile(blockStateFile, blockStateFileContents, StandardCharsets.UTF_8);
		
		File modelFile = new File(modelFolder, getBlockModelName(block));
		FileUtils.writeStringToFile(modelFile, modelFileContents, StandardCharsets.UTF_8);
		
		File textureFile = new File(textureFolder, block.getId() + TEXTURE_FILE_POSTFIX);
		FileUtils.copyFile(texture, textureFile);
		
		ItemModelManager.saveItemBlockModel(addon, block);
	}
	
	public static void saveTextureAnimation(Addon addon, IBlockAdded block, @Nullable File textureAnimation) throws IOException {
		File textureFolder = getBlockTextureFolder(addon);
		
		File animationFile = new File(textureFolder, block.getId() + TEXTURE_FILE_POSTFIX + ANIMATION_FILE_POSTFIX);

		if (textureAnimation != null) {
			FileUtils.copyFile(textureAnimation, animationFile);
		} else {			
			FileUtils.writeStringToFile(animationFile, ModelGenerator.getAnimation(), StandardCharsets.UTF_8);
		}
		
	}
	
	public static void deleteTextureAnimation(Addon addon, IBlockAdded block) throws IOException {
		File textureFolder = getBlockTextureFolder(addon);
		File animationFile = new File(textureFolder, block.getId() + TEXTURE_FILE_POSTFIX + ANIMATION_FILE_POSTFIX);
		
		if (animationFile.exists()) {
			animationFile.delete();
		}
	}
	
	public static String getBlockModelName(IBlockAdded block) {
		return block.getId() + MODEL_FILE_POSTFIX;
	}
	
	public static File getBlockStateFolder(Addon addon) {
		File modelFolder = new File(addon.addonFolder, "assets" + File.separator + "additions" + File.separator + "blockstates");
		
		if (!modelFolder.isDirectory()) {
			modelFolder.mkdirs();
		}
		
		return modelFolder;
	}
	
	public static File getBlockModelFolder(Addon addon) {
		File modelFolder = new File(addon.addonFolder, "assets" + File.separator + "additions" + File.separator + "models" + File.separator + "block");
		
		if (!modelFolder.isDirectory()) {
			modelFolder.mkdirs();
		}
		
		return modelFolder;
	}
	
	public static File getBlockTextureFolder(Addon addon) {
		File textureFolder = new File(addon.addonFolder, "assets" + File.separator + "additions" + File.separator + "textures" + File.separator + "blocks");
		
		if (!textureFolder.isDirectory()) {
			textureFolder.mkdirs();
		}
		
		return textureFolder;
	}

	public static enum BlockModelType {
		SIMPLE,
		SLAB,
		STAIRS,
		TILE,
		FENCE,
		FENCE_GATE,
		WALL,
		BARS,
		LADDER,
		BUTTON,
		PRESSURE_PLATE,
		TRAPDOOR,
		LAMP,
		FACING,
		PLANT,
		PILLAR,
		CAKE,
		FURNACE
	}
}
