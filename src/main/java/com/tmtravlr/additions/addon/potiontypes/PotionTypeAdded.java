package com.tmtravlr.additions.addon.potiontypes;

import com.google.gson.*;
import com.tmtravlr.additions.util.OtherSerializers;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;

/**
 * An added potion type.
 *
 * A potion type is simply a list of potion effects as well as
 * a unique potion group in-game (ex. Minecraft's potion of leaping
 * contains a single effect: jump boost)
 * @since July 2020
 * @author sschr15
 */
public class PotionTypeAdded extends PotionType {
    public final String id;
    private String baseName;
    private String splashName;
    private String lingeringName;
    private String arrowName;
    private ItemStack potionItemStack = ItemStack.EMPTY;
    //TODO allow for custom color creation

    public PotionTypeAdded(String id, String baseName, String splashName, String lingeringName, String arrowName, PotionEffect... effects) {
        super(effects);
        this.id = id;
        this.baseName = baseName;
        this.splashName = splashName;
        this.lingeringName = lingeringName;
        this.arrowName = arrowName;
    }

    public int getEffectCount() {
        return this.getEffects().size();
    }

    /**
     * Overriding due to the fact that we don't actually have real translation files (yet)
     * @param prefix the given prefix: p_185174_1 in stable_39
     * @return the I18n-translateable version of the thing
     */
    @Override
    public String getNamePrefixed(String prefix) {
        switch (prefix) {
            case "potion.effect.":
                return this.baseName;
            case "splash_potion.effect.":
                return this.splashName;
            case "lingering_potion.effect.":
                return this.lingeringName;
            case "tipped_arrow.effect.":
                return this.arrowName;
            default:
                return super.getNamePrefixed(prefix);
        }
    }

    public String getBaseName() {
        return baseName;
    }

    public ItemStack getPotionItemStack() {
        if (this.potionItemStack.isEmpty()) this.potionItemStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), this);
        return this.potionItemStack;
    }

    public void setNames(String baseName, String splashName, String lingeringName, String arrowName) {
        this.baseName = baseName;
        this.splashName = splashName;
        this.lingeringName = lingeringName;
        this.arrowName = arrowName;
    }

    public static class Serializer implements JsonSerializer<PotionTypeAdded>, JsonDeserializer<PotionTypeAdded> {

        @Override
        public JsonElement serialize(PotionTypeAdded src, Type type, JsonSerializationContext context) {
            JsonObject json = new JsonObject();

            json.addProperty("id", src.id);

            json.addProperty("base_name", src.baseName);
            json.addProperty("splash_name", src.splashName);
            json.addProperty("lingering_name", src.lingeringName);
            json.addProperty("arrow_name", src.arrowName);

            if (!src.getEffects().isEmpty()) {
                JsonArray effects = new JsonArray();

                for (PotionEffect effect : src.getEffects()) {
                    effects.add(OtherSerializers.PotionEffectSerializer.serialize(effect));
                }

                json.add("effects", effects);
            }

            return json;
        }

        @Override
        public PotionTypeAdded deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = JsonUtils.getJsonObject(jsonElement, "potion_type");
            String id = JsonUtils.getString(json, "id");
            String baseName = JsonUtils.getString(json, "base_name");
            String splashName = JsonUtils.getString(json, "splash_name");
            String lingeringName = JsonUtils.getString(json, "lingering_name");
            String arrowName = JsonUtils.getString(json, "arrow_name");

            PotionEffect[] effects;
            if (json.has("effects")) {
                JsonArray effectsArray = JsonUtils.getJsonArray(json, "effects");
                effects = new PotionEffect[effectsArray.size()];
                for (int i = 0; i < effectsArray.size(); i++) {
                    effects[i] = OtherSerializers.PotionEffectSerializer.deserialize(JsonUtils.getJsonObject(effectsArray.get(i), "effects[" + i + "]"));
                }
            } else {
                effects = new PotionEffect[0];
            }

            return new PotionTypeAdded(id, baseName, splashName, lingeringName, arrowName, effects);
        }
    }
}
