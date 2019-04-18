package com.tmtravlr.additions.addon.blocks.mapcolors;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.material.MaterialLogic;
import net.minecraft.block.material.MaterialPortal;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.util.ResourceLocation;

public class BlockMapColorManager {
	
	private static final Map<ResourceLocation, MapColor> BLOCK_MAP_COLORS = new HashMap<>();
	private static final Map<MapColor, ResourceLocation> BLOCK_MAP_COLOR_NAMES = new HashMap<>();
	
	public static void registerDefaultBlockMapColors() {
		registerBlockMapColor(new ResourceLocation("air"), MapColor.AIR);
		registerBlockMapColor(new ResourceLocation("grass"), MapColor.GRASS);
		registerBlockMapColor(new ResourceLocation("sand"), MapColor.SAND);
		registerBlockMapColor(new ResourceLocation("wool"), MapColor.CLOTH);
		registerBlockMapColor(new ResourceLocation("tnt"), MapColor.TNT);
		registerBlockMapColor(new ResourceLocation("ice"), MapColor.ICE);
		registerBlockMapColor(new ResourceLocation("iron"), MapColor.IRON);
		registerBlockMapColor(new ResourceLocation("foliage"), MapColor.FOLIAGE);
		registerBlockMapColor(new ResourceLocation("snow"), MapColor.SNOW);
		registerBlockMapColor(new ResourceLocation("clay"), MapColor.CLAY);
		registerBlockMapColor(new ResourceLocation("dirt"), MapColor.DIRT);
		registerBlockMapColor(new ResourceLocation("stone"), MapColor.STONE);
		registerBlockMapColor(new ResourceLocation("water"), MapColor.WATER);
		registerBlockMapColor(new ResourceLocation("wood"), MapColor.WOOD);
		registerBlockMapColor(new ResourceLocation("quartz"), MapColor.QUARTZ);
		registerBlockMapColor(new ResourceLocation("adobe"), MapColor.ADOBE);
		registerBlockMapColor(new ResourceLocation("magenta"), MapColor.MAGENTA);
		registerBlockMapColor(new ResourceLocation("light_blue"), MapColor.LIGHT_BLUE);
		registerBlockMapColor(new ResourceLocation("yellow"), MapColor.YELLOW);
		registerBlockMapColor(new ResourceLocation("lime"), MapColor.LIME);
		registerBlockMapColor(new ResourceLocation("pink"), MapColor.PINK);
		registerBlockMapColor(new ResourceLocation("gray"), MapColor.GRAY);
		registerBlockMapColor(new ResourceLocation("light_gray"), MapColor.SILVER);
		registerBlockMapColor(new ResourceLocation("cyan"), MapColor.CYAN);
		registerBlockMapColor(new ResourceLocation("purple"), MapColor.PURPLE);
		registerBlockMapColor(new ResourceLocation("blue"), MapColor.BLUE);
		registerBlockMapColor(new ResourceLocation("brown"), MapColor.BROWN);
		registerBlockMapColor(new ResourceLocation("green"), MapColor.GREEN);
		registerBlockMapColor(new ResourceLocation("red"), MapColor.RED);
		registerBlockMapColor(new ResourceLocation("black"), MapColor.BLACK);
		registerBlockMapColor(new ResourceLocation("gold"), MapColor.GOLD);
		registerBlockMapColor(new ResourceLocation("diamond"), MapColor.DIAMOND);
		registerBlockMapColor(new ResourceLocation("lapis"), MapColor.LAPIS);
		registerBlockMapColor(new ResourceLocation("emerald"), MapColor.EMERALD);
		registerBlockMapColor(new ResourceLocation("obsidian"), MapColor.OBSIDIAN);
		registerBlockMapColor(new ResourceLocation("netherrack"), MapColor.NETHERRACK);
		registerBlockMapColor(new ResourceLocation("white_stained_clay"), MapColor.WHITE_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("orange_stained_clay"), MapColor.ORANGE_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("magenta_stained_clay"), MapColor.MAGENTA_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("light_blue_stained_clay"), MapColor.LIGHT_BLUE_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("yellow_stained_clay"), MapColor.YELLOW_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("lime_stained_clay"), MapColor.LIME_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("pink_stained_clay"), MapColor.PINK_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("gray_stained_clay"), MapColor.GRAY_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("light_gray_stained_clay"), MapColor.SILVER_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("cyan_stained_clay"), MapColor.CYAN_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("purple_stained_clay"), MapColor.PURPLE_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("blue_stained_clay"), MapColor.BLUE_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("brown_stained_clay"), MapColor.BROWN_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("green_stained_clay"), MapColor.GREEN_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("red_stained_clay"), MapColor.RED_STAINED_HARDENED_CLAY);
		registerBlockMapColor(new ResourceLocation("black_stained_clay"), MapColor.BLACK_STAINED_HARDENED_CLAY);
	}
	
	public static void registerBlockMapColor(ResourceLocation name, MapColor blockMapColor) {
		BLOCK_MAP_COLORS.put(name, blockMapColor);
		BLOCK_MAP_COLOR_NAMES.put(blockMapColor, name);
	}
	
	public static Map<ResourceLocation, MapColor> getBlockMapColors() {
		return BLOCK_MAP_COLORS;
	}

	public static MapColor getBlockMaterial(ResourceLocation name) {
		return BLOCK_MAP_COLORS.get(name);
	}
	
	public static ResourceLocation getBlockMapColorName(MapColor blockMapColor) {
		return BLOCK_MAP_COLOR_NAMES.get(blockMapColor);
	}
}
