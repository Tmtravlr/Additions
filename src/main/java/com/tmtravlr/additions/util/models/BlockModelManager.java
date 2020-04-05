package com.tmtravlr.additions.util.models;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.util.models.ItemModelManager.ItemBlockModelType;

public class BlockModelManager {

	public static final String TEXTURE_FILE_POSTFIX = ".png";
	public static final String ANIMATION_FILE_POSTFIX = ".png.mcmeta";
	public static final String MODEL_FILE_POSTFIX = ".json";
	
	public static final String MODEL_FULL_ENDING = "_full";
	public static final String MODEL_TOP_ENDING = "_top";
	public static final String MODEL_VERTICAL_ENDING = "_vertical";
	public static final String MODEL_INNER_ENDING = "_inner";
	public static final String MODEL_OUTER_ENDING = "_outer";
	public static final String MODEL_POST_ENDING = "_post";
	public static final String MODEL_SIDE_ENDING = "_side";
	public static final String MODEL_SIDE_ALT_ENDING = "_side_alt";
	public static final String MODEL_NO_SIDE_ENDING = "_noside";
	public static final String MODEL_NO_SIDE_ALT_ENDING = "_noside_alt";
	public static final String MODEL_INVENTORY_ENDING = "_inventory";
	
	public static final String TEXTURE_SIDE_ENDING = "_side";
	public static final String TEXTURE_TOP_ENDING = "_top";
	
	public static void saveBlockTexture(Addon addon, IBlockAdded block, File texture, BlockModelType type) throws IOException {
		File blockStateFolder = getBlockStateFolder(addon);
		File modelFolder = getBlockModelFolder(addon);
		File textureFolder = getBlockTextureFolder(addon);
		
		String blockStateFileContents = "";
		Map<String, String> modelFileContents = new HashMap<>();
		
		switch (type) {
		case SLAB:
			blockStateFileContents = BlockStateGenerator.getBlockStateSlab(block.getId());
			modelFileContents.put(block.getId() + MODEL_FULL_ENDING, BlockModelGenerator.getBlockModelSimple(block.getId()));
			modelFileContents.put(block.getId(), BlockModelGenerator.getBlockModelSlabBottom(block.getId()));
			modelFileContents.put(block.getId() + MODEL_TOP_ENDING, BlockModelGenerator.getBlockModelSlabTop(block.getId()));
			modelFileContents.put(block.getId() + MODEL_VERTICAL_ENDING, BlockModelGenerator.getBlockModelSlabVertical(block.getId()));
			break;
		case STAIRS:
			blockStateFileContents = BlockStateGenerator.getBlockStateStairs(block.getId());
			modelFileContents.put(block.getId(), BlockModelGenerator.getBlockModelStairs(block.getId()));
			modelFileContents.put(block.getId() + MODEL_INNER_ENDING, BlockModelGenerator.getBlockModelStairsInner(block.getId()));
			modelFileContents.put(block.getId() + MODEL_OUTER_ENDING, BlockModelGenerator.getBlockModelStairsOuter(block.getId()));
			break;
		case TILE:
			blockStateFileContents = BlockStateGenerator.getBlockStateCarpet(block.getId());
			modelFileContents.put(block.getId(), BlockModelGenerator.getBlockModelCarpetBottom(block.getId()));
			break;
		case FACING:
			blockStateFileContents = BlockStateGenerator.getBlockStateFacing(block.getId());
			modelFileContents.put(block.getId(), BlockModelGenerator.getBlockModelFacing(block.getId()));
			break;
		case PILLAR:
			blockStateFileContents = BlockStateGenerator.getBlockStatePillar(block.getId());
			modelFileContents.put(block.getId(), BlockModelGenerator.getBlockModelPillar(block.getId()));
			break;
		case LADDER:
			blockStateFileContents = BlockStateGenerator.getBlockStateLadder(block.getId());
			modelFileContents.put(block.getId(), BlockModelGenerator.getBlockModelLadder(block.getId()));
			break;
		case PANE:
			blockStateFileContents = BlockStateGenerator.getBlockStatePane(block.getId());
			modelFileContents.put(block.getId() + MODEL_POST_ENDING, BlockModelGenerator.getBlockModelPanePost(block.getId()));
			modelFileContents.put(block.getId() + MODEL_SIDE_ENDING, BlockModelGenerator.getBlockModelPaneSide(block.getId()));
			modelFileContents.put(block.getId() + MODEL_SIDE_ALT_ENDING, BlockModelGenerator.getBlockModelPaneSideAlt(block.getId()));
			modelFileContents.put(block.getId() + MODEL_NO_SIDE_ENDING, BlockModelGenerator.getBlockModelPaneNoSide(block.getId()));
			modelFileContents.put(block.getId() + MODEL_NO_SIDE_ALT_ENDING, BlockModelGenerator.getBlockModelPaneNoSideAlt(block.getId()));
			break;
		case FENCE:
			blockStateFileContents = BlockStateGenerator.getBlockStateFence(block.getId());
			modelFileContents.put(block.getId() + MODEL_POST_ENDING, BlockModelGenerator.getBlockModelFencePost(block.getId()));
			modelFileContents.put(block.getId() + MODEL_SIDE_ENDING, BlockModelGenerator.getBlockModelFenceSide(block.getId()));
			modelFileContents.put(block.getId() + MODEL_INVENTORY_ENDING, BlockModelGenerator.getBlockModelFenceInventory(block.getId()));
			break;
		case WALL:
			blockStateFileContents = BlockStateGenerator.getBlockStateWall(block.getId());
			modelFileContents.put(block.getId() + MODEL_POST_ENDING, BlockModelGenerator.getBlockModelWallPost(block.getId()));
			modelFileContents.put(block.getId() + MODEL_SIDE_ENDING, BlockModelGenerator.getBlockModelWallSide(block.getId()));
			modelFileContents.put(block.getId() + MODEL_INVENTORY_ENDING, BlockModelGenerator.getBlockModelWallInventory(block.getId()));
			break;
		default:
			blockStateFileContents = BlockStateGenerator.getBlockStateSimple(block.getId());
			modelFileContents.put(block.getId(), BlockModelGenerator.getBlockModelSimple(block.getId()));
		}
		
		File blockStateFile = new File(blockStateFolder, getBlockModelName(block));
		FileUtils.writeStringToFile(blockStateFile, blockStateFileContents, StandardCharsets.UTF_8);
		
		for (String modelPrefix : modelFileContents.keySet()) {
			File modelFile = new File(modelFolder, getBlockModelName(modelPrefix));
			FileUtils.writeStringToFile(modelFile, modelFileContents.get(modelPrefix), StandardCharsets.UTF_8);
		}
		
		
		File textureFile = new File(textureFolder, block.getId() + TEXTURE_FILE_POSTFIX);
		FileUtils.copyFile(texture, textureFile);
		
		ItemModelManager.saveItemBlockModel(addon, block, type.getItemBlockType());
	}
	
	public static void saveBlockTextureWithEnding(Addon addon, IBlockAdded block, File texture, String textureEnding) throws IOException {
		File textureFolder = getBlockTextureFolder(addon);

		File textureFile = new File(textureFolder, block.getId() + textureEnding + TEXTURE_FILE_POSTFIX);
		FileUtils.copyFile(texture, textureFile);
	}
	
	public static void saveTextureAnimation(Addon addon, IBlockAdded block, String ending, @Nullable File textureAnimation) throws IOException {
		File textureFolder = getBlockTextureFolder(addon);
		
		File animationFile = new File(textureFolder, block.getId() + ending + ANIMATION_FILE_POSTFIX);

		if (textureAnimation != null) {
			FileUtils.copyFile(textureAnimation, animationFile);
		} else {			
			FileUtils.writeStringToFile(animationFile, ModelGenerator.getAnimation(), StandardCharsets.UTF_8);
		}
		
	}
	
	public static void deleteTextureAnimation(Addon addon, IBlockAdded block) throws IOException {
		deleteTextureAnimation(addon, block, "");
	}
	
	public static void deleteTextureAnimation(Addon addon, IBlockAdded block, String ending) throws IOException {
		File textureFolder = getBlockTextureFolder(addon);
		File animationFile = new File(textureFolder, block.getId() + ending + ANIMATION_FILE_POSTFIX);
		
		if (animationFile.exists()) {
			animationFile.delete();
		}
	}
	
	public static String getBlockModelName(IBlockAdded block) {
		return getBlockModelName(block.getId());
	}
	
	public static String getBlockModelName(String modelPrefix) {
		return modelPrefix + MODEL_FILE_POSTFIX;
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
		FACING,
		PILLAR,
		PANE(ItemBlockModelType.SIMPLE),
		WALL(ItemBlockModelType.INVENTORY),
		FENCE(ItemBlockModelType.INVENTORY),
		FENCE_GATE,
		DOOR,
		TRAPDOOR,
		LAMP,
		BUTTON,
		PRESSURE_PLATE,
		LEVER(ItemBlockModelType.SIMPLE),
		LADDER(ItemBlockModelType.SIMPLE),
		VINE(ItemBlockModelType.SIMPLE),
		PLANT(ItemBlockModelType.SIMPLE),
		CAKE,
		FURNACE,
		WORKBENCH;
		
		private final ItemBlockModelType itemBlockType;
		
		BlockModelType() {
			this.itemBlockType = ItemBlockModelType.BLOCK;
		}
		
		BlockModelType(ItemBlockModelType type) {
			this.itemBlockType = type;
		}
		
		public ItemBlockModelType getItemBlockType() {
			return this.itemBlockType;
		}
	}
}
