package com.tmtravlr.additions.gui.view;

import java.io.IOException;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionList;
import com.tmtravlr.additions.gui.view.components.GuiComponentAdditionTypeList;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEditAddon;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * Shows you info about an addition type (like items, or creative tabs).
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public abstract class GuiViewAdditionType extends GuiView {
	
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");
    private static final int ADD_ADDITION_HEIGHT = 40;
	
	protected final Addon addon;
	protected String newAdditionText;
	protected final GuiComponentAddAddition addAddition = new GuiComponentAddAddition(this);
	protected GuiComponentAdditionFilter filter;
	protected GuiComponentAdditionList additions;

	public GuiViewAdditionType(GuiScreen parentScreen, String title, Addon addon, String newAdditionText) {
		super(parentScreen, title);
		this.addon = addon;
		this.newAdditionText = newAdditionText;
	}

	@Override
	public void initComponents() {
		this.filter = new GuiComponentAdditionFilter(this);
		this.additions = new GuiComponentAdditionList(this, this.addon);
		
		this.components.add(this.addAddition);
		this.components.add(this.filter);
		this.components.add(this.additions);
	}
	
	@Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BACK_BUTTON) {
			super.actionPerformed(button);
    	}
	}
	
	protected abstract GuiScreen getNewAdditionScreen();
	
	private void filterChanged() {
		this.additions.filter(this.filter.getText());
	}
	
	private class GuiComponentAddAddition implements IGuiViewComponent {
		
		private GuiViewAdditionType viewScreen;
		private int x;
		private int y;
		private int width;
		
		public GuiComponentAddAddition(GuiViewAdditionType parentView) {
			this.viewScreen = parentView;
		}

		@Override
		public int getHeight(int left, int right) {
			return ADD_ADDITION_HEIGHT;
		}

		@Override
		public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
			this.y = y + 10;
			int height = 20;
			
			this.x = x + 10;
			this.width = right - this.x - 10;
			
			if (this.width < 100) {
				this.width = 100;
				this.x = (right - x) / 2 - 50;
			}
			
			if (this.width > 500) {
				this.width = 500;
				this.x = (right - x) / 2 - 250;
			}
			
			if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY < this.y + height ) {
				this.viewScreen.drawRect(this.x + 1, this.y, this.x + this.width - 1, this.y + height, 0xFF102123);
				this.viewScreen.drawRect(this.x + 3, this.y + 2, this.x + this.width - 3, this.y + height - 2, 0xFF1C393C);
				this.viewScreen.drawRect(this.x + 5, this.y + 4, this.x + this.width - 5, this.y + height - 4, 0xFF2D5C60);
			}
			
			int newAdditionTextWidth = this.viewScreen.fontRenderer.getStringWidth(this.viewScreen.newAdditionText);
			int midX = this.x + this.width/2;
			int midY = this.y + height/2;
			
			this.viewScreen.drawString(this.viewScreen.fontRenderer, this.viewScreen.newAdditionText, midX - newAdditionTextWidth/2, midY - 4, 0xFFFFFF);
			
			this.viewScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
		    GlStateManager.enableAlpha();
		    
			this.viewScreen.drawTexturedModalRect(midX - newAdditionTextWidth/2 - 23, midY - 7, 73, 64, 13, 13);
			this.viewScreen.drawTexturedModalRect(midX + newAdditionTextWidth/2 + 10, midY - 7, 73, 64, 13, 13);
		}

		@Override
		public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
			int height = 20;
			if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY < this.y + height ) {
				this.viewScreen.mc.displayGuiScreen(this.viewScreen.getNewAdditionScreen());
			}
		}

		@Override
		public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
			return false;
		}

		@Override
		public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
		
	}
	
	private class GuiComponentAdditionFilter extends GuiTextField implements IGuiViewComponent {

		private GuiViewAdditionType viewScreen;
		
		public GuiComponentAdditionFilter(GuiViewAdditionType viewScreen) {
			super(0, viewScreen.getFontRenderer(), 0, 0, 0, 20);
			this.viewScreen = viewScreen;
		}

		@Override
		public int getHeight(int left, int right) {
			return 40;
		}

		@Override
		public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
			
			this.y = y + 10;
			
			this.x = x + 10;
			this.width = right - 10 - this.x;
			
			if (this.width < 100) {
				this.width = 100;
				this.x = (right - x) / 2 - 50;
			}
			
			if (this.width > 400) {
				this.width = 400;
				this.x = (right - x) / 2 - 200;
			}
			
			this.drawTextBox();
			
			this.viewScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
	        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
			// Add search icon
		    int searchX = this.x + this.width - 16;
			int searchY = this.y + (this.height / 2 - 6);
			
			this.viewScreen.drawTexturedModalRect(searchX, searchY, 47, 64, 13, 13);
		}

		@Override
		public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	    	this.mouseClicked(mouseX, mouseY, mouseButton);
		}

		@Override
		public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
			return false;
		}

		@Override
		public void onKeyTyped(char keyTyped, int keyCode) throws IOException {
			this.textboxKeyTyped(keyTyped, keyCode);
		}
		
		@Override
	    public void setResponderEntryValue(int idIn, String textIn) {
	    	super.setResponderEntryValue(idIn, textIn);
	    	
	    	this.viewScreen.filterChanged();
    	}

	}

}
