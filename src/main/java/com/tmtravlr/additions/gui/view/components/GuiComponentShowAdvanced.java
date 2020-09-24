package com.tmtravlr.additions.gui.view.components;

import java.io.IOException;

import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * Displays a Show Advanced boolean selector, which if clicked, will show the 'advanced' components, normally hidden for simplicity's sake.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public class GuiComponentShowAdvanced implements IGuiViewComponent {
	
	private GuiView viewScreen;
	private int toggleX;
	private int toggleY;
	private boolean hidden = false;
	
	public GuiComponentShowAdvanced(GuiView viewScreen) {
		this.viewScreen = viewScreen;
	}

	@Override
	public boolean isHidden() {
		return this.hidden;
	}
	
	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public int getHeight(int left, int right) {
		return 30;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		String text = I18n.format("gui.view.showAdvanced");
		
		int textWidth = this.viewScreen.getFontRenderer().getStringWidth(text);
		this.toggleX = x + (right - x) / 2 - (32 + 10 + textWidth) / 2;
		this.toggleY = y + 8;
		
		this.viewScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        
        this.viewScreen.drawTexturedModalRect(this.toggleX, this.toggleY, 192, 64, 32, 16);
        
        if (this.viewScreen.getShowAdvanced()) {
            this.viewScreen.drawTexturedModalRect(this.toggleX + 16, this.toggleY, 240, 64, 16, 16);
        } else {
            this.viewScreen.drawTexturedModalRect(this.toggleX, this.toggleY, 224, 64, 16, 16);
        }
        
        this.viewScreen.drawString(this.viewScreen.getFontRenderer(), text, this.toggleX + 42, this.toggleY +4, 0xFFFFFF);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.toggleX, this.toggleY, 32, 16)) {
			this.viewScreen.toggleShowAdvanced();
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}

}
