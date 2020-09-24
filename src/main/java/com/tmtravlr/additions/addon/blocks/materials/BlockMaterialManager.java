package com.tmtravlr.additions.addon.blocks.materials;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BlockMaterialManager {
	
	private static final Map<ResourceLocation, Material> BLOCK_MATERIALS = new HashMap<>();
	private static final Map<Material, ResourceLocation> BLOCK_MATERIAL_NAMES = new HashMap<>();
	private static final Map<Material, SoundType> BLOCK_SOUND_TYPES = new HashMap<>();
	
	public static void registerDefaultBlockMaterials() {
		registerBlockMaterial(new ResourceLocation("air"), Material.AIR, null);
		registerBlockMaterial(new ResourceLocation("grass"), Material.GRASS, SoundType.PLANT);
		registerBlockMaterial(new ResourceLocation("dirt"), Material.GROUND, SoundType.GROUND);
		registerBlockMaterial(new ResourceLocation("wood"), Material.WOOD, SoundType.WOOD);
		registerBlockMaterial(new ResourceLocation("stone"), Material.ROCK, SoundType.STONE);
		registerBlockMaterial(new ResourceLocation("metal"), Material.IRON, SoundType.METAL);
		registerBlockMaterial(new ResourceLocation("anvil"), Material.ANVIL, SoundType.ANVIL);
		registerBlockMaterial(new ResourceLocation("water"), Material.WATER, null);
		registerBlockMaterial(new ResourceLocation("lava"), Material.LAVA, null);
		registerBlockMaterial(new ResourceLocation("leaves"), Material.LEAVES, SoundType.PLANT);
		registerBlockMaterial(new ResourceLocation("plants"), Material.PLANTS, SoundType.PLANT);
		registerBlockMaterial(new ResourceLocation("vine"), Material.VINE, SoundType.PLANT);
		registerBlockMaterial(new ResourceLocation("sponge"), Material.SPONGE, SoundType.PLANT);
		registerBlockMaterial(new ResourceLocation("wool"), Material.CLOTH, SoundType.CLOTH);
		registerBlockMaterial(new ResourceLocation("fire"), Material.FIRE, SoundType.CLOTH);
		registerBlockMaterial(new ResourceLocation("sand"), Material.SAND, SoundType.SAND);
		registerBlockMaterial(new ResourceLocation("circuits"), Material.CIRCUITS, SoundType.STONE);
		registerBlockMaterial(new ResourceLocation("carpet"), Material.CARPET, SoundType.CLOTH);
		registerBlockMaterial(new ResourceLocation("glass"), Material.GLASS, SoundType.GLASS);
		registerBlockMaterial(new ResourceLocation("redstone_lamp"), Material.REDSTONE_LIGHT, SoundType.GLASS);
		registerBlockMaterial(new ResourceLocation("tnt"), Material.TNT, SoundType.PLANT);
		registerBlockMaterial(new ResourceLocation("coral"), Material.CORAL, SoundType.PLANT);
		registerBlockMaterial(new ResourceLocation("ice"), Material.ICE, SoundType.GLASS);
		registerBlockMaterial(new ResourceLocation("packed_ice"), Material.PACKED_ICE, SoundType.GLASS);
		registerBlockMaterial(new ResourceLocation("snow_layer"), Material.SNOW, SoundType.SNOW);
		registerBlockMaterial(new ResourceLocation("snow"), Material.CRAFTED_SNOW, SoundType.SNOW);
		registerBlockMaterial(new ResourceLocation("cactus"), Material.CACTUS, SoundType.CLOTH);
		registerBlockMaterial(new ResourceLocation("clay"), Material.CLAY, SoundType.GROUND);
		registerBlockMaterial(new ResourceLocation("gourd"), Material.GOURD, SoundType.WOOD);
		registerBlockMaterial(new ResourceLocation("dragon_egg"), Material.DRAGON_EGG, SoundType.STONE);
		registerBlockMaterial(new ResourceLocation("portal"), Material.PORTAL, SoundType.GLASS);
		registerBlockMaterial(new ResourceLocation("cake"), Material.CAKE, SoundType.CLOTH);
		registerBlockMaterial(new ResourceLocation("web"), Material.WEB, SoundType.CLOTH);
		registerBlockMaterial(new ResourceLocation("piston"), Material.PISTON, SoundType.STONE);
		registerBlockMaterial(new ResourceLocation("barrier"), Material.BARRIER, null);
		registerBlockMaterial(new ResourceLocation("structure_void"), Material.STRUCTURE_VOID, null);
	}
	
	public static void registerBlockMaterial(ResourceLocation name, Material blockMaterial, SoundType blockSoundType) {
		BLOCK_MATERIALS.put(name, blockMaterial);
		BLOCK_MATERIAL_NAMES.put(blockMaterial, name);
		BLOCK_SOUND_TYPES.put(blockMaterial, blockSoundType);
	}
	
	public static Map<ResourceLocation, Material> getBlockMaterials() {
		return BLOCK_MATERIALS;
	}

	public static Material getBlockMaterial(ResourceLocation name) {
		return BLOCK_MATERIALS.get(name);
	}
	
	public static ResourceLocation getBlockMaterialName(Material blockMaterial) {
		return BLOCK_MATERIAL_NAMES.get(blockMaterial);
	}
	
	public static SoundType getBlockSoundType(Material blockMaterial) {
		return BLOCK_SOUND_TYPES.get(blockMaterial);
	}
}
