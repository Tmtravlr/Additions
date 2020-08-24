package com.tmtravlr.additions.gui.type.card;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.potiontypes.PotionTypeAdded;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonPotionType;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEditPotionType;
import com.tmtravlr.additions.type.AdditionTypePotionType;
import com.tmtravlr.additions.util.client.CommonGuiUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionType;
import net.minecraft.util.text.TextFormatting;

public class GuiAdditionCardPotionType extends GuiAdditionCardColored {
    private final PotionTypeAdded addition;
    private final String textCount;
    private final String textId;
    private final String filterId;
    private final String filterName;
    private final ItemStack displayItem;

    public GuiAdditionCardPotionType(GuiView viewScreen, Addon addon, PotionTypeAdded addition) {
        super(viewScreen, addon);

        this.addition = addition;

        this.textCount = TextFormatting.GRAY + I18n.format("gui.view.additionType.potionCount", TextFormatting.RESET + String.valueOf(this.addition.getEffectCount()) + TextFormatting.GRAY);
        this.textId = TextFormatting.GRAY + I18n.format("gui.view.additionType.id", TextFormatting.RESET + this.addition.id + TextFormatting.GRAY);

        this.filterId = this.addition.id;
        this.filterName = this.addition.getBaseName();

        this.setColors(GuiAdditionTypeButtonPotionType.BUTTON_COLOR_DARK, GuiAdditionTypeButtonPotionType.BUTTON_COLOR_HOVER);

        ItemStack itemStack;
        //noinspection ConstantConditions
        if (PotionType.REGISTRY.getNameForObject(this.addition) == null) {
            itemStack = new ItemStack(Items.POTIONITEM);
            itemStack.setTranslatableName(this.addition.getBaseName());
            NBTTagCompound stackTag = itemStack.getTagCompound();
            NBTTagCompound display = stackTag.getCompoundTag("display");
            NBTTagList lore = display.getTagList("Lore", 8);
            lore.appendTag(new NBTTagString(I18n.format("gui.edit.potionType.restartToRegister.first")));
            lore.appendTag(new NBTTagString(I18n.format("gui.edit.potionType.restartToRegister.second")));
            display.setTag("Lore", lore);
            stackTag.setTag("display", display);
            itemStack.setTagCompound(stackTag);
        }
        else itemStack = this.addition.getPotionItemStack();
        this.displayItem = itemStack;
    }

    @Override
    protected void drawCardInfo(int mouseX, int mouseY) {
        int itemDisplayTop = this.y + this.height / 2 - 10;
        int columnWidth = this.getColumnWidth();

        Gui.drawRect(this.x + 9, itemDisplayTop - 1, this.x + 31, itemDisplayTop + 21, 0xFFA0A0A0);
        Gui.drawRect(this.x + 10, itemDisplayTop, this.x + 30, itemDisplayTop + 20, 0xFF000000);

        CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textId, columnWidth + 2 - 60, this.x + 45, this.y + 8, 0xFFFFFF);

        if (this.needs3Lines()) {
            CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textCount, columnWidth*2 - 10, this.x + 45, this.y + 25, 0xFFFFFF);
        } else {
            CommonGuiUtils.drawStringWithDots(this.viewScreen.getFontRenderer(), this.textCount, columnWidth - 5, this.x + 45, this.y + 25, 0xFFFFFF);
        }

        this.viewScreen.renderItemStack(this.displayItem, this.x + 12, itemDisplayTop + 2, mouseX, mouseY, true);
    }

    @Override
    protected void editAddition() {
        this.viewScreen.mc.displayGuiScreen(new GuiEditPotionType(this.viewScreen, I18n.format("gui.edit.editing", this.addition.id), this.addon, this.addition));
    }

    @Override
    protected void duplicateAddition() {
        GuiEditPotionType editScreen = new GuiEditPotionType(this.viewScreen, I18n.format("gui.edit.potionType.title"), this.addon, null);
        this.viewScreen.mc.displayGuiScreen(editScreen.copyFrom(this.addition)); // see my comment in copyFrom :)
    }

    @Override
    protected void deleteAddition() {
        AdditionTypePotionType.INSTANCE.deleteAddition(this.addon, this.addition);
    }

    @Override
    public boolean filterApplies(String filter) {
        return filter == null || filter.isEmpty() || this.filterId.contains(filter.toLowerCase()) || this.filterName.contains(filter);
    }

    @Override
    public int getCardHeight() {
        return 60;
    }

    private int getColumnWidth() {
        return (this.width - 45) / 2;
    }

    private boolean needs3Lines() {
        return this.viewScreen.getFontRenderer().getStringWidth(this.textId) > this.getColumnWidth() || this.viewScreen.getFontRenderer().getStringWidth(this.textCount) > this.getColumnWidth() || this.width < 240;
    }
}
