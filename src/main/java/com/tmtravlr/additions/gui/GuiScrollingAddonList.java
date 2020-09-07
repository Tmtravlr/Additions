package com.tmtravlr.additions.gui;

import java.util.ArrayList;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.GuiScrollingList;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

/**
 * Main list of addons.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2017
 */
public class GuiScrollingAddonList extends GuiScrollingList {

	private GuiAdditionsMainMenu parent;
	private ArrayList<Addon> addons;
	
	public GuiScrollingAddonList(GuiAdditionsMainMenu parent, ArrayList<Addon> addons, int listWidth, int listHeight, int minY, int slotHeight) {
        super(parent.mc, listWidth, listHeight, minY, minY + listHeight, (parent.width - listWidth) / 2, slotHeight, parent.width, parent.height);
        this.parent = parent;
        this.addons = addons;
	}
	
	@Override
	protected int getSize() {
		return addons.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		this.parent.addonSelected(this.addons.get(index));
	}

	@Override
	protected boolean isSelected(int index) {
		return false;
	}

	@Override
	protected void drawBackground() {
		this.parent.drawDefaultBackground();
	}

	@Override
	protected void drawSlot(int index, int right, int top, int height, Tessellator tess) {
		top -= 2;
		int left = this.left + 2;
		boolean isHovering = mouseX >= left && mouseX <= right &&
                mouseY >= top && mouseY < top + this.slotHeight;
		
        this.parent.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
	    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        if (isHovering) {
        	this.parent.drawTexturedModalRect(left, top, 0, 32, 256, 32);
        } else {
        	this.parent.drawTexturedModalRect(left, top, 0, 0, 256, 32);
        }
        
        Addon addon = this.addons.get(index);
        
        if (addon == this.parent.createNew) {
        	this.parent.drawTexturedModalRect(left + 5, top + 5, 0, 64, 21, 21);
        	this.parent.drawString(this.parent.getFontRenderer(), I18n.format("gui.edit.addon.title"), left + 42, top + 13, 0xFFFFFF);
        } else {
        	RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        	this.parent.mc.getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)null, addon.getLogoItem(), left + 7, top + 8);
            this.parent.mc.getRenderItem().renderItemOverlayIntoGUI(this.parent.getFontRenderer(), addon.getLogoItem(), left + 7, top + 8, null);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            
            if (!addon.locked) {
            	this.parent.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
                GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
                
                this.parent.drawTexturedModalRect(left + 22, top + 19, 154, 64, 7, 10);
            }
            
            String addonName = addon.name;
            
            if (addon.locked && !StringUtils.isNullOrEmpty(addon.version)) {
            	addonName += TextFormatting.RESET + " v" + addon.version;
            }
            
        	this.parent.drawString(this.parent.getFontRenderer(), CommonGuiUtils.trimWithDots(this.parent.getFontRenderer(), addonName, 210), left + 42, top + 7, 0xFFFFFF);
        	this.parent.drawString(this.parent.getFontRenderer(), CommonGuiUtils.trimWithDots(this.parent.getFontRenderer(), I18n.format("gui.additions.main.byAuthor", addon.author, addon.id), 210), left + 42, top + 19, 0xFFFFFF);
        }
	}

}
