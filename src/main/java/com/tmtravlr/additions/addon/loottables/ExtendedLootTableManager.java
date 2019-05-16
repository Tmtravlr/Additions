package com.tmtravlr.additions.addon.loottables;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.type.AdditionTypeLootTable;
import com.tmtravlr.additions.type.AdditionTypeStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ExtendedLootTableManager extends LootTableManager {
	
	public static final Gson LOOT_TABLE_GSON = ObfuscationReflectionHelper.getPrivateValue(LootTableManager.class, null, "field_186526_b", "GSON_INSTANCE");
	private static final Logger LOGGER = ObfuscationReflectionHelper.getPrivateValue(LootTableManager.class, null, "field_186525_a", "LOGGER"); 
    private final LoadingCache<ResourceLocation, LootTable> registeredLootTables = CacheBuilder.newBuilder().<ResourceLocation, LootTable>build(new ExtendedLootTableManager.Loader());
	
	private final File baseFolder;

	public ExtendedLootTableManager(File baseFolder) {
		super(baseFolder);
		this.baseFolder = baseFolder;
		this.reloadLootTables();
	}

    public LootTable getLootTableFromLocation(ResourceLocation location) {
        return this.registeredLootTables.getUnchecked(location);
    }

    public void reloadLootTables() {
    	AdditionTypeLootTable.INSTANCE.reloadAllLootTables(AddonLoader.addonsLoaded);
    	
    	if (this.baseFolder != null) {
	        this.registeredLootTables.invalidateAll();
	
	        for (ResourceLocation location : LootTableList.getAll()) {
	            this.getLootTableFromLocation(location);
	        }
    	}
    }
	
	private class Loader extends CacheLoader<ResourceLocation, LootTable> {
		public LootTable load(ResourceLocation location) throws Exception {
	        if (location.getResourcePath().contains(".")) {
	        	LOGGER.warn("Invalid loot table name '{}' (can't contain periods)", location);
	            
	            return LootTable.EMPTY_LOOT_TABLE;
	        } else {
	            LootTable loottable = this.loadLootTable(location);
	            
	            if (loottable == null) {
	            	loottable = this.loadAddonLootTable(location);
	            }

	            if (loottable == null) {
	                loottable = this.loadBuiltinLootTable(location);
	            }

	            if (loottable == null) {
	                loottable = LootTable.EMPTY_LOOT_TABLE;
	                LOGGER.warn("Couldn't find resource table {}", (Object)location);
	            }

	            return loottable;
	        }
	    }

	    @Nullable
	    private LootTable loadLootTable(ResourceLocation location) {
	        if (ExtendedLootTableManager.this.baseFolder == null) {
	            return null;
	        } else {
	            File file1 = new File(new File(ExtendedLootTableManager.this.baseFolder, location.getResourceDomain()), location.getResourcePath() + ".json");

	            if (file1.exists()) {
	                if (file1.isFile()) {
	                    String s;

	                    try {
	                        s = Files.toString(file1, StandardCharsets.UTF_8);
	                    } catch (IOException ioexception) {
	                    	LOGGER.warn("Couldn't load loot table {} from {}", location, file1, ioexception);
	                        return LootTable.EMPTY_LOOT_TABLE;
	                    }

	                    try {
	                        return net.minecraftforge.common.ForgeHooks.loadLootTable(LOOT_TABLE_GSON, location, s, true, ExtendedLootTableManager.this);
	                    } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
	                    	LOGGER.error("Couldn't load loot table {} from {}", location, file1, jsonparseexception);
	                        return LootTable.EMPTY_LOOT_TABLE;
	                    }
	                } else {
	                	LOGGER.warn("Expected to find loot table {} at {} but it was a folder.", location, file1);
	                    return LootTable.EMPTY_LOOT_TABLE;
	                }
	            } else {
	                return null;
	            }
	        }
	    }

	    @Nullable
	    private LootTable loadAddonLootTable(ResourceLocation location) {
	    	Addon addonToGetLootTableFor = null;
	    	LootTable lootTable = null;
			
			for (Addon addon : AddonLoader.addonsLoaded) {
	        	if (AdditionTypeLootTable.INSTANCE.getAllAdditions(addon).stream().anyMatch(lootTableAdded -> lootTableAdded.location.equals(location))) {
	        		addonToGetLootTableFor = addon;
	        	}
			}
			
			if (addonToGetLootTableFor != null) {
				String filePath = AdditionTypeLootTable.FOLDER_NAME + File.separator + location.getResourceDomain() + File.separator + location.getResourcePath() + AdditionTypeLootTable.FILE_POSTFIX;
			
				try {
					if (AddonLoader.addonFileExists(addonToGetLootTableFor.addonFolder, filePath)) {
						String data = AddonLoader.readAddonFile(addonToGetLootTableFor.addonFolder, filePath);
						
						lootTable = ForgeHooks.loadLootTable(LOOT_TABLE_GSON, location, data, true, ExtendedLootTableManager.this);
					}
	            } catch (JsonParseException e) {
	            	LOGGER.warn("Parsing error loading loot table {} for addon {}", location, addonToGetLootTableFor.id, e);
	            } catch (IOException ioexception) {
	            	LOGGER.error("Couldn't load addon loot table {} from {} for addon {}", location, filePath, addonToGetLootTableFor.id, ioexception);
				}
			}
			
			return lootTable;
	    }

	    @Nullable
	    private LootTable loadBuiltinLootTable(ResourceLocation location) {
	        URL url = LootTableManager.class.getResource("/assets/" + location.getResourceDomain() + "/loot_tables/" + location.getResourcePath() + ".json");

	        if (url != null) {
	            String s;

	            try {
	                s = Resources.toString(url, StandardCharsets.UTF_8);
	            } catch (IOException ioexception) {
	            	LOGGER.warn("Couldn't load loot table {} from {}", location, url, ioexception);
	                return LootTable.EMPTY_LOOT_TABLE;
	            }

	            try {
	                return net.minecraftforge.common.ForgeHooks.loadLootTable(LOOT_TABLE_GSON, location, s, false, ExtendedLootTableManager.this);
	            } catch (JsonParseException jsonparseexception) {
	            	LOGGER.error("Couldn't load loot table {} from {}", location, url, jsonparseexception);
	                return LootTable.EMPTY_LOOT_TABLE;
	            }
	        } else {
	            return null;
	        }
	    }
	}

}
