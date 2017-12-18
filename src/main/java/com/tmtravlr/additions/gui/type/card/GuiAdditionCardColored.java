package com.tmtravlr.additions.gui.type.card;

import java.io.IOException;
import java.util.Collections;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.GuiMessagePopupTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;

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
 * @since September 2017 
 */
public abstract class GuiAdditionCardColored extends GuiAdditionCard {
	
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
	
	protected int backgroundColor;
	protected int borderColor;
	protected int height;

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
			this.x = (right - x) / 2 - 50;
		}
		
		if (this.width > 500) {
			this.width = 500;
			this.x = (right - x) / 2 - 250;
		} 
		
		this.height = this.getCardHeight() - 20;

		this.viewScreen.drawRect(this.x - 1, this.y + 1, this.x + this.width + 1, this.y + this.height - 1, this.borderColor);
		this.viewScreen.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.borderColor);
		this.viewScreen.drawRect(this.x + 1, this.y - 1, this.x + this.width - 1, this.y + this.height + 1, this.borderColor);
		this.viewScreen.drawRect(this.x, this.y + 1, this.x + this.width, this.y + this.height - 1, this.backgroundColor);
		this.viewScreen.drawRect(this.x + 1, this.y, this.x + this.width - 1, this.y + this.height, this.backgroundColor);
		
		this.drawCardInfo(mouseX, mouseY);
		
		this.viewScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
	    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
	    
	    this.viewScreen.drawTexturedModalRect(this.x + this.width - 54, this.y + 5, 125, 64, 13, 13);
	    this.viewScreen.drawTexturedModalRect(this.x + this.width - 36, this.y + 5, 112, 64, 13, 13);
	    this.viewScreen.drawTexturedModalRect(this.x + this.width - 18, this.y + 5, 60, 64, 13, 13);
	    
	    if (this.hoveringEdit(mouseX, mouseY)) {
			GuiUtils.drawHoveringText(Collections.singletonList(I18n.format("gui.buttons.edit")), mouseX, mouseY, this.viewScreen.width, this.viewScreen.height, -1, this.viewScreen.getFontRenderer());
            RenderHelper.disableStandardItemLighting();
	    }
	    
	    if (this.hoveringDuplicate(mouseX, mouseY)) {
			GuiUtils.drawHoveringText(Collections.singletonList(I18n.format("gui.buttons.duplicate")), mouseX, mouseY, this.viewScreen.width, this.viewScreen.height, -1, this.viewScreen.getFontRenderer());
            RenderHelper.disableStandardItemLighting();
	    }
	    
	    if (this.hoveringDelete(mouseX, mouseY)) {
			GuiUtils.drawHoveringText(Collections.singletonList(I18n.format("gui.buttons.delete")), mouseX, mouseY, this.viewScreen.width, this.viewScreen.height, -1, this.viewScreen.getFontRenderer());
            RenderHelper.disableStandardItemLighting();
	    }
	}

	@Override
	public void onClick(int mouseX, int mouseY) {
		if (this.hoveringEdit(mouseX, mouseY)) {
			this.editAddition();
	    }
	    
	    if (this.hoveringDuplicate(mouseX, mouseY)) {
			this.duplicateAddition();
	    }
	    
	    if (this.hoveringDelete(mouseX, mouseY)) {
			this.confirmDelete();
	    }
	}
	
	protected abstract void drawCardInfo(int mouseX, int mouseY);
	
	protected abstract void editAddition();
	
	protected abstract void duplicateAddition();
	
	protected abstract void deleteAddition();
	
	protected boolean hoveringEdit(int mouseX, int mouseY) {
		return mouseX >= this.x + this.width - 54 && mouseX <= this.x + this.width - 41 && mouseY >= this.y + 5 && mouseY <= this.y + 18;
	}
	
	protected boolean hoveringDuplicate(int mouseX, int mouseY) {
		return mouseX >= this.x + this.width - 36 && mouseX <= this.x + this.width - 23 && mouseY >= this.y + 5 && mouseY <= this.y + 18;
	}
	
	protected boolean hoveringDelete(int mouseX, int mouseY) {
		return mouseX >= this.x + this.width - 18 && mouseX <= this.x + this.width - 5 && mouseY >= this.y + 5 && mouseY <= this.y + 18;
	}
	
	protected void confirmDelete() {
		final GuiAdditionCardColored card = this;
		this.viewScreen.mc.displayGuiScreen(new GuiMessagePopupTwoButton(this.viewScreen, this.viewScreen, I18n.format("gui.warnDialogue.delete.title"), new TextComponentTranslation("gui.warnDialogue.delete.message"), I18n.format("gui.buttons.cancel"), I18n.format("gui.buttons.delete")) {

			protected void actionPerformed(GuiButton button) throws IOException {
		        if (button.id == BUTTON_CONTINUE) {
		        	card.deleteAddition();
		        	card.viewScreen.refreshView();
		        }
		        super.actionPerformed(button);
		    }
		});
	}

}
