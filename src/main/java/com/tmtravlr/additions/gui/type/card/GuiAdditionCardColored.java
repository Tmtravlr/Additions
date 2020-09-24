package com.tmtravlr.additions.gui.type.card;

import java.io.IOException;
import java.util.Collections;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * Implementation of GuiAdditionCard with simple colors.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017 
 */
public abstract class GuiAdditionCardColored extends GuiAdditionCard {
	
	protected int backgroundColor;
	protected int borderColor;
	protected int height;
	protected boolean allowEdit = true;

	public GuiAdditionCardColored(GuiView viewScreen, Addon addon) {
		super(viewScreen, addon);
	}
	
	public void setColors(int backgroundColor, int borderColor) {
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
	}

	@Override
	public void drawCard(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x + 10;
		this.y = y + 10;
		this.width = right - this.x - 10;

		if (this.width < 100) {
			this.width = 100;
			this.x = x + (right - x) / 2 - 50;
		}
		
		if (this.width > 500) {
			this.width = 500;
			this.x = x + (right - x) / 2 - 250;
		} 
		
		this.height = this.getCardHeight() - 20;

		this.viewScreen.drawRect(this.x - 1, this.y + 1, this.x + this.width + 1, this.y + this.height - 1, this.borderColor);
		this.viewScreen.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.borderColor);
		this.viewScreen.drawRect(this.x + 1, this.y - 1, this.x + this.width - 1, this.y + this.height + 1, this.borderColor);
		this.viewScreen.drawRect(this.x, this.y + 1, this.x + this.width, this.y + this.height - 1, this.backgroundColor);
		this.viewScreen.drawRect(this.x + 1, this.y, this.x + this.width - 1, this.y + this.height, this.backgroundColor);
		
		this.drawCardInfo(mouseX, mouseY);
	    
	    if (!this.addon.locked) {
			this.viewScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
	    
		    if (this.allowEdit) {
			    this.viewScreen.drawTexturedModalRect(this.x + this.width - 54, this.y + 5, 125, 64, 13, 13);
			    this.viewScreen.drawTexturedModalRect(this.x + this.width - 36, this.y + 5, 112, 64, 13, 13);
		    }
		    
		    this.viewScreen.drawTexturedModalRect(this.x + this.width - 18, this.y + 5, 60, 64, 13, 13);
		    
		    if (this.allowEdit) {
			    if (this.hoveringEdit(mouseX, mouseY)) {
			    	this.viewScreen.renderInfoTooltip(Collections.singletonList(I18n.format("gui.buttons.edit")), mouseX, mouseY);
			    }
			    
			    if (this.hoveringDuplicate(mouseX, mouseY)) {
			    	this.viewScreen.renderInfoTooltip(Collections.singletonList(I18n.format("gui.buttons.duplicate")), mouseX, mouseY);
			    }
		    }
		    
		    if (this.hoveringDelete(mouseX, mouseY)) {
		    	this.viewScreen.renderInfoTooltip(Collections.singletonList(I18n.format("gui.buttons.delete")), mouseX, mouseY);
		    }
	    }
	}

	@Override
	public void onClick(int mouseX, int mouseY) {
		if (!this.addon.locked) {
			if (this.allowEdit) {
				if (this.hoveringEdit(mouseX, mouseY)) {
					this.editAddition();
			    }
			    
			    if (this.hoveringDuplicate(mouseX, mouseY)) {
					this.duplicateAddition();
			    }
			}
	    
		    if (this.hoveringDelete(mouseX, mouseY)) {
				this.confirmDelete();
		    }
		}
	}
	
	protected abstract void drawCardInfo(int mouseX, int mouseY);
	
	protected void editAddition() {};
	
	protected void duplicateAddition() {};
	
	protected void deleteAddition() {};
	
	protected boolean hoveringEdit(int mouseX, int mouseY) {
		return CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + this.width - 54, this.y + 5, 13, 13);
	}
	
	protected boolean hoveringDuplicate(int mouseX, int mouseY) {
		return CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + this.width - 36, this.y + 5, 13, 13);
	}
	
	protected boolean hoveringDelete(int mouseX, int mouseY) {
		return CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x + this.width - 18, this.y + 5, 13, 13);
	}
	
	protected void confirmDelete() {
		final GuiAdditionCardColored card = this;
		this.viewScreen.mc.displayGuiScreen(new GuiMessageBoxTwoButton(this.viewScreen, this.viewScreen, I18n.format("gui.warnDialogue.delete.title"), new TextComponentTranslation("gui.warnDialogue.delete.message"), I18n.format("gui.buttons.cancel"), I18n.format("gui.buttons.delete")) {

			protected void actionPerformed(GuiButton button) throws IOException {
		        if (button.id == SECOND_BUTTON) {
		        	card.deleteAddition();
		        	card.viewScreen.refreshView();
		        }
		        super.actionPerformed(button);
		    }
		});
	}
}
