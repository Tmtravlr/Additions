package com.tmtravlr.additions.gui.type.button;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.GuiViewPotionType;
import com.tmtravlr.additions.type.AdditionTypePotionType;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import net.minecraft.client.resources.I18n;

/**
 * Button to see the added potion types
 * @since June 2020
 * @author sschr15
 */
public class GuiAdditionTypeButtonPotionType extends GuiAdditionTypeButtonColored {
    // please ignore my inconsistencies with Tmtravlr's code: I like capital hex numbers
    public static final int BUTTON_COLOR_HOVER = 0xFF3C84E2;
    public static final int BUTTON_COLOR_LIGHT = 0xFF6FB7FF;
    public static final int BUTTON_COLOR_DARK  = 0xFF0951AF;

    public GuiAdditionTypeButtonPotionType(GuiView view, Addon addon) {
        super(view, addon);

        int numAdded = AdditionTypePotionType.INSTANCE.getAllAdditions(this.addon).size();

        this.setLabel(I18n.format("gui.view.addon.potionTypes.label", numAdded));
        this.setColors(CommonGuiUtils.ADDITION_BUTTON_COLOR, BUTTON_COLOR_HOVER, BUTTON_COLOR_LIGHT, BUTTON_COLOR_DARK);
    }

    @Override
    public void onClick() {
        CommonGuiUtils.playClickSound();
        this.viewScreen.mc.displayGuiScreen(new GuiViewPotionType(
                this.viewScreen,
                I18n.format("gui.view.addon.potionTypes.title", this.addon.name),
                this.addon
        ));
    }

    @Override
    public boolean isAdvanced() {
        return true;
    }
}
