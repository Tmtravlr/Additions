package com.tmtravlr.additions.addon.advancements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonParseException;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.type.AdditionTypeAdvancement;
import com.tmtravlr.additions.util.ProblemNotifier;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementTreeNode;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Replaces the vanilla advancement manager, so it can load
 * and reload addon advancements with the vanilla ones
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class ExtendedAdvancementManager extends AdvancementManager {
	
	private static final Logger LOGGER = ObfuscationReflectionHelper.getPrivateValue(AdvancementManager.class, null, "field_192782_a", "LOGGER");
    private static final AdvancementList ADVANCEMENT_LIST = ObfuscationReflectionHelper.getPrivateValue(AdvancementManager.class, null, "field_192784_c", "ADVANCEMENT_LIST");

    private final File advancementsDir;
    private boolean hasErrored;

    public ExtendedAdvancementManager(@Nullable File advancementsDirIn) {
    	super(advancementsDirIn);
        this.advancementsDir = advancementsDirIn;
        this.reload();
    }

    @Override
    public void reload() {
    	if (this.advancementsDir != null) {
	        this.hasErrored = false;
	        
	        ADVANCEMENT_LIST.clear();
	        Map<ResourceLocation, Advancement.Builder> map = this.loadCustomAdvancements();
	        this.loadAddonAdvancements(map);
	        this.loadBuiltInAdvancements(map);
	        this.hasErrored |= net.minecraftforge.common.ForgeHooks.loadAdvancements(map);
	        ADVANCEMENT_LIST.loadAdvancements(map);
	
	        for (Advancement advancement : ADVANCEMENT_LIST.getRoots()) {
	            if (advancement.getDisplay() != null) {
	                AdvancementTreeNode.layout(advancement);
	            }
	        }
    	}
    }

    @Override
    public boolean hasErrored() {
        return this.hasErrored;
    }

    private Map<ResourceLocation, Advancement.Builder> loadCustomAdvancements() {
        Map<ResourceLocation, Advancement.Builder> map = new HashMap<>();
        this.advancementsDir.mkdirs();

        for (File file1 : FileUtils.listFiles(this.advancementsDir, new String[] {"json"}, true)) {
            String s = FilenameUtils.removeExtension(this.advancementsDir.toURI().relativize(file1.toURI()).toString());
            String[] astring = s.split("/", 2);

            if (astring.length == 2) {
                ResourceLocation resourcelocation = new ResourceLocation(astring[0], astring[1]);

                try {
                    Advancement.Builder advancement$builder = JsonUtils.gsonDeserialize(GSON, FileUtils.readFileToString(file1, StandardCharsets.UTF_8), Advancement.Builder.class);

                    if (advancement$builder == null) {
                        LOGGER.error("Couldn't load custom advancement " + resourcelocation + " from " + file1 + " as it's empty or null");
                    } else {
                        map.put(resourcelocation, advancement$builder);
                    }
                } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                    LOGGER.error("Parsing error loading custom advancement " + resourcelocation, jsonparseexception);
    				ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromFile(file1), new TextComponentString(jsonparseexception.getMessage()));
                    this.hasErrored = true;
                } catch (IOException ioexception) {
                    LOGGER.error("Couldn't read custom advancement " + resourcelocation + " from " + file1, ioexception);
    				ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromFile(file1), new TextComponentString(ioexception.getMessage()));
                    this.hasErrored = true;
                }
            }
        }
        return map;
    }
    
    private void loadAddonAdvancements(Map<ResourceLocation, Advancement.Builder> map) {
		AdditionTypeAdvancement.INSTANCE.reloadAllAdvancements(AddonLoader.addonsLoaded);
    	Map<ResourceLocation, Advancement.Builder> advancementsToLoad = new HashMap<>();
		
		for (Addon addon : AddonLoader.addonsLoaded) {
			for (AdvancementAdded advancementAdded : AdditionTypeAdvancement.INSTANCE.getAllAdditions(addon)) {
				advancementsToLoad.put(advancementAdded.id, advancementAdded.advancementBuilder);
			}
		}
		
		for (ResourceLocation location : advancementsToLoad.keySet()) {
			// Assume advancements already in the list should not be overwritten (they were loaded from the world)
			if (!map.containsKey(location)) {
				map.put(location, advancementsToLoad.get(location));
			}
		}
    }

    private void loadBuiltInAdvancements(Map<ResourceLocation, Advancement.Builder> map) {
        FileSystem filesystem = null;

        try {
            URL url = AdvancementManager.class.getResource("/assets/.mcassetsroot");

            if (url != null) {
                URI uri = url.toURI();
                Path path;

                if ("file".equals(uri.getScheme())) {
                    path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/advancements").toURI());
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        LOGGER.error("Unsupported scheme " + uri + " trying to list all built-in advancements (NYI?)");
                        this.hasErrored = true;
                        return;
                    }

                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = filesystem.getPath("/assets/minecraft/advancements");
                }

                Iterator<Path> iterator = Files.walk(path).iterator();

                while (iterator.hasNext()) {
                    Path path1 = iterator.next();

                    if ("json".equals(FilenameUtils.getExtension(path1.toString()))) {
                        Path path2 = path.relativize(path1);
                        String s = FilenameUtils.removeExtension(path2.toString()).replaceAll("\\\\", "/");
                        ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);

                        if (!map.containsKey(resourcelocation)) {
                            BufferedReader bufferedreader = null;

                            try {
                                bufferedreader = Files.newBufferedReader(path1);
                                Advancement.Builder advancement$builder = JsonUtils.fromJson(GSON, bufferedreader, Advancement.Builder.class);
                                map.put(resourcelocation, advancement$builder);
                            } catch (JsonParseException jsonparseexception) {
                                LOGGER.error("Parsing error loading built-in advancement " + resourcelocation, jsonparseexception);
                                this.hasErrored = true;
                            } catch (IOException ioexception) {
                                LOGGER.error("Couldn't read advancement " + resourcelocation + " from " + path1, ioexception);
                                this.hasErrored = true;
                            } finally {
                                IOUtils.closeQuietly(bufferedreader);
                            }
                        }
                    }
                }

                return;
            }

            LOGGER.error("Couldn't find .mcassetsroot");
            this.hasErrored = true;
        } catch (IOException | URISyntaxException urisyntaxexception) {
            LOGGER.error("Couldn't get a list of all built-in advancement files", urisyntaxexception);
            this.hasErrored = true;
            return;
        } finally {
            IOUtils.closeQuietly(filesystem);
        }
    }
}
