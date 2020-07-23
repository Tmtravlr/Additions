package com.tmtravlr.additions.type;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.AddonLoader;
import com.tmtravlr.additions.addon.potiontypes.PotionTypeAdded;
import com.tmtravlr.additions.util.ProblemNotifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Added potion types
 * @since July 2020
 * @author sschr15
 */
public class AdditionTypePotionType extends AdditionType<PotionTypeAdded> {

    public static final ResourceLocation NAME = new ResourceLocation(AdditionsMod.MOD_ID, "potion_type");
    public static final String FOLDER_NAME = "data" + File.separator + "potion_types";
    public static final String FILE_POSTFIX = JSON_POSTFIX;
    public static final AdditionTypePotionType INSTANCE = new AdditionTypePotionType();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(PotionTypeAdded.class, new PotionTypeAdded.Serializer())
            .setPrettyPrinting()
            .create();

    private final Multimap<Addon, PotionTypeAdded> loadedPotionTypes = HashMultimap.create();

    @Override
    public void loadPreInit(List<Addon> addons, FMLPreInitializationEvent event) {
        AdditionsMod.logger.info("Loading addon potion types.");
        for (Addon addon : addons) {
            List<String> filePaths = new ArrayList<>();

            try {
                filePaths = AddonLoader.getAddonFilePaths(addon.addonFolder, FOLDER_NAME);
            } catch (IOException e) {
                AdditionsMod.logger.error("Error loading potion type files for addon " + addon.id + ". The potion types will not load.", e);
                ProblemNotifier.addProblemNotification(new TextComponentTranslation("gui.view.addon.potionTypes.problem"), new TextComponentString(e.getMessage()));
            }

            for (String filePath : filePaths) {
                try {
                    String fileString = AddonLoader.readAddonFile(addon.addonFolder, filePath);
                    PotionTypeAdded potionTypeAdded = GSON.fromJson(fileString, PotionTypeAdded.class);

                    String typeBaseName = filePath;
                    if (typeBaseName.contains(File.separator)) {
                        typeBaseName = typeBaseName.substring(typeBaseName.lastIndexOf(File.separator) + 1);
                    }

                    if (typeBaseName.endsWith(FILE_POSTFIX)) {
                        typeBaseName = typeBaseName.substring(0, typeBaseName.length() - FILE_POSTFIX.length());
                    }

                    ResourceLocation potionTypeRegistryName = new ResourceLocation(AdditionsMod.MOD_ID, typeBaseName);

                    this.loadedPotionTypes.put(addon, potionTypeAdded);
                    ForgeRegistries.POTION_TYPES.register(potionTypeAdded.setRegistryName(potionTypeRegistryName));

                } catch (IOException | JsonParseException e) {
                    AdditionsMod.logger.error("Error loading potion type " + filePath + " for addon " + addon.id + ". The item will not load.", e);
                    ProblemNotifier.addProblemNotification(ProblemNotifier.createLabelFromPath(addon.addonFolder, filePath), new TextComponentString(e.getMessage()));
                }
            }
        }
    }

    @Override
    public List<PotionTypeAdded> getAllAdditions(Addon addon) {
        return new ArrayList<>(this.loadedPotionTypes.get(addon));
    }

    @Override
    public void saveAddition(Addon addon, PotionTypeAdded addition) {
        if (!this.loadedPotionTypes.containsEntry(addon, addition)) {
            this.loadedPotionTypes.put(addon, addition);
        }

        File additionFolder = new File(addon.addonFolder, FOLDER_NAME);
        if (!additionFolder.isDirectory()) additionFolder.mkdirs();

        File additionFile = new File(additionFolder, addition.id + FILE_POSTFIX);

        try {
            String fileContents = GSON.toJson(addition);
            FileUtils.write(additionFile, fileContents, StandardCharsets.UTF_8);
        } catch (IOException | IllegalArgumentException e) {
            AdditionsMod.logger.error("Error saving addon " + addon.name, e);
        }
    }

    @Override
    public void deleteAddition(Addon addon, PotionTypeAdded addition) {
        if (this.loadedPotionTypes.containsEntry(addon, addition)) {
            this.loadedPotionTypes.remove(addon, addition);
        }

        File additionFolder = new File(addon.addonFolder, FOLDER_NAME);
        File additionFile = new File(additionFolder, addition.id + FILE_POSTFIX);

        additionFile.delete();
        if (Objects.requireNonNull(additionFolder.listFiles()).length == 0) additionFolder.delete();
    }
}
