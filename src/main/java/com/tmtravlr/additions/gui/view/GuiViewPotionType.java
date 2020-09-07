package com.tmtravlr.additions.gui.view;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.potiontypes.PotionTypeAdded;
import com.tmtravlr.additions.gui.type.card.GuiAdditionCardPotionType;
import com.tmtravlr.additions.gui.view.edit.GuiEditPotionType;
import com.tmtravlr.additions.type.AdditionTypePotionType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

/**
 * the Additions cards for potion types
 * @date July 2020
 * @author sschr15
 */
public class GuiViewPotionType extends GuiViewAdditionType {


    public GuiViewPotionType(GuiScreen parentScreen, String title, Addon addon) {
        super(parentScreen, title, addon, I18n.format("gui.edit.potionType.title"), new TextComponentTranslation("gui.view.addon.potionTypes.none.message", addon.name));
    }

    @Override
    protected void addAdditions() {
        List<PotionTypeAdded> additions = AdditionTypePotionType.INSTANCE.getAllAdditions(this.addon);

        additions.sort((first, second) -> {
            if (first == null || second == null) return 0;
            return first.id.compareTo(second.id);
        });

        for (PotionTypeAdded addition : additions) {
            this.additions.addAdditionCard(new GuiAdditionCardPotionType(this, this.addon, addition));
        }
    }

    @Override
    protected GuiScreen getNewAdditionScreen() {
        return new GuiEditPotionType(this, I18n.format("gui.edit.potionType.title"), this.addon, null);
    }
}
