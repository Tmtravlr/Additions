package com.tmtravlr.additions.addon.loottables;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

/**
 * Holds info about a loot table's location as well as its preset, if it has one.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since Febuary 2019 
 */
public class LootTableAdded {

	public ResourceLocation location;
	
	@Nullable
	public LootTablePreset preset;
	
	public LootTableAdded(ResourceLocation location) {
		this(location, null);
	}
	
	public LootTableAdded(ResourceLocation location, LootTablePreset preset) {
		this.location = location;
		this.preset = preset;
	}
	
}
