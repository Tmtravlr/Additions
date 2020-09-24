package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Simple boolean input, with a clickable boolean selector like in the new minecraft launcher.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2017 
 */
public class GuiComponentBooleanInput extends Gui implements IGuiViewComponent {

	private GuiEdit editScreen;
	private String label = "";
	private List<String> info = new ArrayList<>();
	private boolean input = false;
	private int x;
	private int y;
	private boolean hidden = false;
	
	public GuiComponentBooleanInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
	}
	
	public void setDefaultBoolean(boolean input) {
		this.input = input;
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
		return 40;
	}
	
	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y + 10;
		
		this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
        
        this.editScreen.drawTexturedModalRect(this.x, this.y, 192, 64, 32, 16);
        
        if (input) {
            this.editScreen.drawTexturedModalRect(this.x + 16, this.y, 240, 64, 16, 16);
        } else {
            this.editScreen.drawTexturedModalRect(this.x, this.y, 224, 64, 16, 16);
        }
		
		// Add info icon
		if (!this.info.isEmpty()) {
			int infoX = this.x + 45;
			int infoY = this.y + 2;
	
			this.editScreen.drawTexturedModalRect(infoX, infoY, 21, 64, 13, 13);
			
			if (mouseX > infoX && mouseX < infoX + 13 && mouseY > infoY && mouseY < infoY + 13) {
				this.editScreen.renderInfoTooltip(info, mouseX, mouseY);
			}
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseX >= this.x && mouseX <= this.x + 32 && mouseY >= this.y && mouseY <= this.y + 16) {
			this.setBoolean(!this.input);
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	protected void setBoolean(boolean input) {
		this.setDefaultBoolean(input);
		this.editScreen.notifyHasChanges();
	}
	
	public void setInfo(ITextComponent info) {
		if (info == null) {
			this.info.clear();
		} else {
			this.info = this.editScreen.getFontRenderer().listFormattedStringToWidth(info.getFormattedText(), 200);
		}
	}
	
	public boolean getBoolean() {
		return input;
	}

}
