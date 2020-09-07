package com.tmtravlr.additions.addon.structures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.type.AdditionTypeStructure;
import com.tmtravlr.additions.util.ProblemNotifier;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Replaces the vanilla template manager, so it can load
 * and reload addon structures with the vanilla ones
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2018 
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
        
        //Lambdas need final things
        final StructureWrapper structureWrapper = new StructureWrapper();
        final Addon finalAddon = addonToGetStructureFor;
        
        try {
        	AdditionTypeStructure.INSTANCE.useStructureInputStream(addonToGetStructureFor, location, (inputStream) -> {
        		if (inputStream != null) {
        	        try {
        	        	structureWrapper.setStructure(this.readTemplateFromStream(inputStream));
	            		this.loadedAddonStructures.put(location, structureWrapper.getStructure());
        		    } catch (Throwable e) {
        		        AdditionsMod.logger.warn("Couldn't load structure " + location + " from addon " + finalAddon.id, e);
        		        File structureFile = AdditionTypeStructure.INSTANCE.getStructureFile(finalAddon, location);
        				ProblemNotifier.addProblemNotification(structureFile.exists() ? ProblemNotifier.createLabelFromFile(structureFile) : new TextComponentString(location.toString()), new TextComponentString(e.getMessage()));
        		    }
            	}
        	});
	    } catch (Throwable e) {
	        AdditionsMod.logger.warn("Couldn't load structure " + location + " from addon " + addonToGetStructureFor.id, e);
	        File structureFile = AdditionTypeStructure.INSTANCE.getStructureFile(addonToGetStructureFor, location);
			ProblemNotifier.addProblemNotification(structureFile.exists() ? ProblemNotifier.createLabelFromFile(structureFile) : new TextComponentString(location.toString()), new TextComponentString(e.getMessage()));
	    }
        
        return structureWrapper.getStructure();
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
    
    private static class StructureWrapper extends Object {
    	private Template structure = null;
    	
    	private void setStructure(Template structure) {
    		this.structure = structure;
    	}
    	
    	private Template getStructure() {
    		return this.structure;
    	}
    }
}
