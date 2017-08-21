package com.tmtravlr.additions.gui.edit.components;

import java.io.IOException;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import com.tmtravlr.additions.gui.edit.GuiEdit;
import com.tmtravlr.additions.gui.edit.IGuiEditComponent;

public class GuiShowAdvanced implements IGuiEditComponent {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
	
	private GuiEdit parent;
	private int toggleX;
	private int toggleY;
	
	public GuiShowAdvanced(GuiEdit parent) {
		this.parent = parent;
	}

	@Override
	public int getHeight(int left, int right) {
		return 30;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		String text = I18n.format("gui.editObject.showAdvanced");
		
		int textWidth = this.parent.getFontRenderer().getStringWidth(text);
		this.toggleX = x + (right - x) / 2 - (32 + 10 + textWidth) / 2;
		this.toggleY = y + 8;
		
		this.parent.mc.getTextureManager().bindTexture(GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        
        this.parent.drawTexturedModalRect(this.toggleX, this.toggleY, 192, 64, 32, 16);
        
        if (this.parent.getShowAdvanced()) {
            this.parent.drawTexturedModalRect(this.toggleX + 16, this.toggleY, 240, 64, 16, 16);
        } else {
            this.parent.drawTexturedModalRect(this.toggleX, this.toggleY, 224, 64, 16, 16);
        }
        
        this.parent.drawString(this.parent.getFontRenderer(), text, this.toggleX + 42, this.toggleY +4, 0xFFFFFF);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseX >= this.toggleX && mouseX <= this.toggleX + 32 && mouseY >= this.toggleY && mouseY <= this.toggleY + 16) {
			this.parent.setShowAdvanced(!this.parent.getShowAdvanced());
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}

}
