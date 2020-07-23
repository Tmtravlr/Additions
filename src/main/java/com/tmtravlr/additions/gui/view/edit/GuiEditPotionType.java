package com.tmtravlr.additions.gui.view.edit;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.potiontypes.PotionTypeAdded;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxNeedsRestart;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.input.*;
import com.tmtravlr.additions.type.AdditionTypePotionType;
import com.tmtravlr.additions.util.GeneralUtils;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
public class GuiEditPotionType extends GuiEdit {

    private Addon addon;
    // TODO add potion color input

    private boolean isNew;
    private PotionTypeAdded addition;
    private PotionTypeAdded copyFrom;

    private GuiComponentStringInput idInput;
    private GuiComponentStringInput baseNameInput;
    private GuiComponentListInput<GuiComponentPotionEffectInput> potionEffectListInput;

    // advanced components
    private GuiComponentDisplayText inputInformation;

    private GuiComponentStringInput splashNameInput;
    private GuiComponentStringInput lingeringNameInput;
    private GuiComponentStringInput arrowNameInput;

    private GuiComponentBooleanInput useUniqueColorInput;
    private GuiComponentColorInput potionColorInput; //TODO implement this

    public GuiEditPotionType(GuiScreen parentScreen, String title, Addon addon, @Nullable PotionTypeAdded type) {
        super(parentScreen, title);
        this.addon = addon;
        this.isNew = type == null;
        this.addition = type;
    }

    @Override
    public void initComponents() {
        this.idInput = new GuiComponentStringInput(I18n.format("gui.edit.potionType.id.label"), this);

        if (this.isNew) {
            this.idInput.setRequired();
            this.idInput.setMaxStringLength(32);
            this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.info"));
            this.idInput.setValidator(input -> input.matches("[a-z0-9_]*"));
        } else {
            this.idInput.setEnabled(false);
            this.idInput.setMaxStringLength(1024);
            this.idInput.setInfo(new TextComponentTranslation("gui.edit.id.noEdit.info"));
            this.idInput.setDefaultText(this.addition.id);
        }

        this.baseNameInput = new GuiComponentStringInput(I18n.format("gui.edit.potionType.baseName.label"), this);
        this.baseNameInput.setRequired();
        this.baseNameInput.setHasColorSelect();
        if (!this.isNew) this.baseNameInput.setDefaultText(this.addition.getBaseName());

        this.potionEffectListInput = new GuiComponentListInput<GuiComponentPotionEffectInput>(I18n.format("gui.edit.potionType.effects.label"), this) {
            @Override
            public GuiComponentPotionEffectInput createBlankComponent() {
                return new GuiComponentPotionEffectInput("", this.editScreen);
            }
        };
        if (!this.isNew) {
            this.addition.getEffects().forEach(toAdd -> {
                GuiComponentPotionEffectInput input = this.potionEffectListInput.createBlankComponent();
                input.setDefaultPotionEffect(toAdd);
                this.potionEffectListInput.addDefaultComponent(input);
            });
        }

        if (this.copyFrom != null) {
            this.copyFromOther();
        }

        // Advanced components
        this.inputInformation = new GuiComponentDisplayText(this, new TextComponentTranslation("gui.edit.potionType.inputInformation"));

        this.splashNameInput = new GuiComponentStringInput(I18n.format("gui.edit.potionType.splashName.label"), this);
        this.splashNameInput.setHasColorSelect();
        this.lingeringNameInput = new GuiComponentStringInput(I18n.format("gui.edit.potionType.lingeringName.label"), this);
        this.lingeringNameInput.setHasColorSelect();
        this.arrowNameInput = new GuiComponentStringInput(I18n.format("gui.edit.potionType.arrowName.label"), this);
        this.arrowNameInput.setInfo(new TextComponentTranslation("gui.edit.potionType.arrowName.info"));
        this.arrowNameInput.setHasColorSelect();

        this.useUniqueColorInput = new GuiComponentBooleanInput(I18n.format("gui.edit.potionType.potionColor.switch.label"), this);
        // Reason for noinspection: readability. The suggested change was to `!this.isNew && this.addition.hasColor`
        //noinspection SimplifiableConditionalExpression
        this.useUniqueColorInput.setDefaultBoolean(this.isNew ? false : this.addition.hasColor());
        this.potionColorInput = new GuiComponentColorInput(I18n.format("gui.edit.potionType.potionColor.label"), this);

        // Add all components to the screen
        this.components.add(this.idInput);
        this.components.add(this.baseNameInput);
        this.components.add(this.potionEffectListInput);

        this.advancedComponents.add(this.inputInformation);
        this.advancedComponents.add(this.splashNameInput);
        this.advancedComponents.add(this.lingeringNameInput);
        this.advancedComponents.add(this.arrowNameInput);
//        this.advancedComponents.add(this.useUniqueColorInput);
//        this.advancedComponents.add(this.potionColorInput); //TODO implement this
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void saveObject() {
        String name = this.isNew ? this.addon.id + "-" + this.idInput.getText() : this.addition.id;
        if (CommonGuiUtils.requireStringField(
                this.idInput,
                this,
                "gui.edit.potionType.problem.title",
                new TextComponentTranslation("gui.edit.problem.noId")
        ) == null) return;
        String baseName = CommonGuiUtils.requireStringField(this.baseNameInput, this, "gui.edit.potionType.problem.title", new TextComponentTranslation("gui.edit.potionType.problem.noBase"));
        if (baseName == null) return;

        String base = TextFormatting.getTextWithoutFormattingCodes(baseName), formats=baseName.substring(0, baseName.indexOf(base));
        String splashName = this.splashNameInput.getText().isEmpty() ? formats + "Splash " + base : this.splashNameInput.getText();
        String lingeringName = this.lingeringNameInput.getText().isEmpty() ? formats + "Lingering " + base : this.lingeringNameInput.getText();
        String arrowName;
        if (this.arrowNameInput.getText().isEmpty()) {
            arrowName = baseName.startsWith("Potion of") ? baseName.replace("Potion of", "Arrow of") : formats + "Arrow of " + base;
        } else {
            arrowName = this.arrowNameInput.getText();
        }
        int potionColor = this.useUniqueColorInput.getBoolean() ? this.potionColorInput.getColorInt() : -1;

        PotionEffect[] effects = new PotionEffect[this.potionEffectListInput.getComponents().size()];
        List<GuiComponentPotionEffectInput> effectInputs = this.potionEffectListInput.getComponents();
        for (int i = 0; i < effectInputs.size(); i++) {
            effects[i] = effectInputs.get(i).getPotionEffect();
        }

        PotionTypeAdded type = new PotionTypeAdded(name, baseName, splashName, lingeringName, arrowName, GeneralUtils.removeNulls(effects));

        if (!this.isNew) {
            this.addition.effects = type.effects;
            this.addition.setNames(baseName, splashName, lingeringName, arrowName);
            AdditionTypePotionType.INSTANCE.saveAddition(this.addon, this.addition);
            this.mc.displayGuiScreen(new GuiMessageBox(this.parentScreen, I18n.format("gui.edit.potionType.success.title"), new TextComponentTranslation("gui.edit.potionType.success.message"), I18n.format("gui.buttons.continue")));
        } else {
            type.setRegistryName(AdditionsMod.MOD_ID, name);
            AdditionTypePotionType.INSTANCE.saveAddition(this.addon, type);
            this.mc.displayGuiScreen(new GuiMessageBoxNeedsRestart(this.parentScreen, I18n.format("gui.warnDialogue.restart.created.title"), new TextComponentTranslation("gui.edit.potionType.restart.message")));
        }
    }

    /**
     * Returns itself so you can inline this in a {@code displayGuiScreen} call
     * @param type the type to copy
     */
    public GuiEditPotionType copyFrom(PotionTypeAdded type) {
        this.copyFrom = type;

        if (this.initializedComponents) {
            this.copyFromOther();
        }
        return this;
    }

    public void copyFromOther() {
        this.potionEffectListInput.removeAllComponents();
        this.copyFrom.getEffects().forEach(toAdd -> {
            GuiComponentPotionEffectInput input = this.potionEffectListInput.createBlankComponent();
            input.setDefaultPotionEffect(toAdd);
            this.potionEffectListInput.addDefaultComponent(input);
        });

        this.copyFrom = null;
    }

    @Override
    public void updateScreen() {
        this.potionColorInput.setHidden(!this.useUniqueColorInput.getBoolean() || !this.getShowAdvanced());
    }
}
