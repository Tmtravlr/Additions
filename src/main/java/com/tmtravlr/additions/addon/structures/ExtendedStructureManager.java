package com.tmtravlr.additions.addon.structures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Multimap;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.type.AdditionTypeLootTable;
import com.tmtravlr.additions.type.AdditionTypeStructure;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Replaces the vanilla template manager, so it can load
 * and reload addon structures with the vanilla ones
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class ExtendedStructureManager extends TemplateManager {
	
	private final Map<String, Template> structures;
	private final String baseFolder;
	private final DataFixer fixer;
	
	private final Map<ResourceLocation, Template> loadedAddonStructures = new HashMap<>();

	public ExtendedStructureManager(String baseFolder, DataFixer fixer) {
		super(baseFolder, fixer);
		this.structures = ObfuscationReflectionHelper.getPrivateValue(TemplateManager.class, this, "field_186240_a", "templates");
		this.baseFolder = baseFolder;
		this.fixer = fixer;
	}
	
	@Override
    public Template get(MinecraftServer server, ResourceLocation location) {
    	boolean worldStructureExists = new File(this.baseFolder, location.getResourcePath() + AdditionTypeStructure.FILE_POSTFIX).exists();
        
    	if (AdditionTypeStructure.INSTANCE.hasLocation(location) && (!worldStructureExists || (worldStructureExists && !location.getResourceDomain().equals("minecraft")))) {
    		if (this.loadedAddonStructures.containsKey(location)) {
            	return this.loadedAddonStructures.get(location);
            } else {
            	return this.getAddonStructure(location);
            }
    	}
    	
		return super.get(server, location);
    }
	
	public void reload() {
		AdditionTypeStructure.INSTANCE.reloadAllStructures(AddonLoader.addonsLoaded);
		this.structures.clear();
		this.loadedAddonStructures.clear();
	}

	private Template getAddonStructure(ResourceLocation location) {
        Addon addonToGetStructureFor = null;
        
        for (Addon addon : AddonLoader.addonsLoaded) {
        	if (AdditionTypeStructure.INSTANCE.getAllAdditions(addon).contains(location)) {
        		addonToGetStructureFor = addon;
        	}
        }
        
        File structureFile = new File(addonToGetStructureFor.addonFolder, "structures/" + location.getResourceDomain() + "/" + location.getResourcePath() + AdditionTypeStructure.FILE_POSTFIX);
        Template structure = null;
        
        if (structureFile.exists()) {
        	InputStream inputStream = null;
            
        	try {
        		inputStream = new FileInputStream(structureFile);
        		structure = this.readTemplateFromStream(inputStream);
        		this.loadedAddonStructures.put(location, structure);
            } catch (Throwable e) {
                AdditionsMod.logger.warn("Couldn't load structure " + location + " from addon " + addonToGetStructureFor.id, e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
        
        return structure;
    }

    private Template readTemplateFromStream(InputStream stream) throws IOException {
        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(stream);

        if (!nbttagcompound.hasKey("DataVersion", 99)) {
            nbttagcompound.setInteger("DataVersion", 500);
        }

        Template template = new Template();
        template.read(this.fixer.process(FixTypes.STRUCTURE, nbttagcompound));
        return template;
    }
}
