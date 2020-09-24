package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputItem;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Lets you choose a color.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date December 2018 
 */
public class GuiComponentColorInput implements IGuiViewComponent {

	public GuiEdit editScreen;
	public GuiTextField selectedText;
	
	private int x;
	private int y;
	private int width;
	private String label = "";
	private boolean required = false;
	private boolean hidden = false;
	private float[] colorArray = null;
	private int colorInt = 0;
	
	public GuiComponentColorInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
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
	public String getLabel() {
		return this.label;
	}

	@Override
	public int getHeight(int left, int right) {
		return 40;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y;
		this.width = right - x;

		int colorDisplayTop = this.y + 10;
		
		CommonGuiUtils.drawOutline(this.x, colorDisplayTop - 1, 22, 22, 0xFFA0A0A0);
		if (this.colorArray != null) {
			Gui.drawRect(this.x + 1, colorDisplayTop, this.x + 21, colorDisplayTop + 20, 0xFF000000 + this.colorInt);
		}
		
		this.selectedText.x = x + 30;
		this.selectedText.y = y + 10;
		this.selectedText.width = right - 90 - x;
		
		this.selectedText.drawTextBox();
		
		if (this.colorArray != null) {
			this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (!this.selectedText.getText().isEmpty()) {
			
			if(mouseX >= deleteX && mouseX < this.selectedText.x + this.selectedText.width && mouseY >= deleteY && mouseY < deleteY + 13) {
				this.clearColor();
			}
		}
		
		if (mouseX >= this.selectedText.x - 30 && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			this.editScreen.mc.displayGuiScreen(new GuiMessageBoxEditColor(this.editScreen.mc.currentScreen));
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setDefaultColorArray(float[] color) {
		if (color != null && color.length != 3) {
			color = null;
		}
		
		this.colorArray = color;
		if (color == null) {
			this.selectedText.setText("");
			this.colorInt = 0;
		} else {
			this.colorInt = this.getColorIntFromFloats(color);
			this.selectedText.setText(String.valueOf(this.colorInt));
		}
		this.selectedText.setCursorPositionZero();
	}
	
	public void setDefaultColorInt(int color) {
		this.colorInt = color;
		this.colorArray = this.getColorFloatsFromInt(color);
		this.selectedText.setText(String.valueOf(this.colorInt));
		this.selectedText.setCursorPositionZero();
	}
	
	public void setColorArray(float[] color) {
		this.setDefaultColorArray(color);
		this.editScreen.notifyHasChanges();
	}
	
	public void setColorInt(int color) {
		this.setDefaultColorInt(color);
		this.editScreen.notifyHasChanges();
	}
	
	public float[] getColorArray() {
		return this.colorArray;
	}
	
	public int getColorInt() {
		return this.colorInt;
	}

	private void clearColor() {
		this.setColorArray(null);
	}
	
	private int getColorIntFromFloats(float[] color) {
		if (color == null || color.length != 3) {
			return 0;
		}
		
		return MathHelper.floor(color[0] * 0xFF) << 16 | MathHelper.floor(color[1] * 0xFF)  << 8 | MathHelper.floor(color[2] * 0xFF);
	}
	
	private float[] getColorFloatsFromInt(int color) {
		return new float[]{((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, (color & 0xFF) / 255f};
	}
	
	protected class GuiMessageBoxEditColor extends GuiMessageBoxTwoButton {
        
		private static final int SLIDER_WIDTH = 100;
        private static final int SLIDER_HEIGHT = 7;
		
		private final GuiComponentIntegerInput colorInput;

	    private float sliderRed = 0f;
	    private float sliderGreen = 0f;
	    private float sliderBlue = 0f;
	    
	    private boolean wasClicking;
	    private boolean slidingRed;
	    private boolean slidingGreen;
	    private boolean slidingBlue;

		public GuiMessageBoxEditColor(GuiScreen parentScreen) {
			super(parentScreen, parentScreen, I18n.format("gui.popup.color.title"), new TextComponentString(""), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.update"));
			
			this.colorInput = new GuiComponentIntegerInput("", GuiComponentColorInput.this.editScreen, false) {
				
				@Override
			    public void setResponderEntryValue(int id, String text) {
			    	super.setResponderEntryValue(id, text);
			    	GuiMessageBoxEditColor.this.updateColorFromInt();
			    }
			};
			
			float[] color = GuiComponentColorInput.this.colorArray;
			if (color != null) {
				this.sliderRed = color[0];
				this.sliderGreen = color[1];
				this.sliderBlue = color[2];
				this.colorInput.setDefaultInteger(GuiComponentColorInput.this.getColorIntFromFloats(color));
			}
			
			this.setViewScreen(GuiComponentColorInput.this.editScreen);
		}

		@Override
	    protected void onSecondButtonClicked() {
	        GuiComponentColorInput.this.setColorArray(new float[]{sliderRed, sliderGreen, sliderBlue});
        	this.mc.displayGuiScreen(this.parentScreen);
	    }
	    
		@Override
	    protected int getPopupWidth() {
	    	return 350;
	    }
		
	    @Override
	    protected int getPopupHeight() {
	    	return this.getComponentsHeight() + 90;
	    }

	    @Override
	    public void drawScreenOverlay(int mouseX, int mouseY, float partialTicks) {
			int popupWidth = this.getPopupWidth();
	    	int popupHeight = this.getPopupHeight();
	    	int popupX = this.width / 2 - popupWidth / 2;
	    	int popupY = this.height / 2 - popupHeight / 2;
	    	int popupRight = popupX + popupWidth;

			int colorDisplayX = popupX + 10;
			int colorDisplayY = popupY + 50;
	    	
	    	int sliderX = popupX + 60;
	        int sliderY = colorDisplayY + 2;
	        int sliderPadding = SLIDER_HEIGHT + 8;

			int colorInputX = sliderX;
			int colorInputY = popupY + 50;
	        
	    	if (Mouse.isButtonDown(0)) {
	    		if (!this.wasClicking) {
		        	this.slidingRed = CommonGuiUtils.isMouseWithin(mouseX, mouseY, sliderX, sliderY, SLIDER_WIDTH, SLIDER_HEIGHT);
		        	this.slidingGreen = CommonGuiUtils.isMouseWithin(mouseX, mouseY, sliderX, sliderY + sliderPadding, SLIDER_WIDTH, SLIDER_HEIGHT);
		        	this.slidingBlue =  CommonGuiUtils.isMouseWithin(mouseX, mouseY, sliderX, sliderY + 2*sliderPadding, SLIDER_WIDTH, SLIDER_HEIGHT);
	    		}
		        
		        this.wasClicking = true;
		
		        if (this.slidingRed) {
		        	this.sliderRed = MathHelper.clamp((float)(mouseX - sliderX) / ((float)SLIDER_WIDTH), 0.0F, 1.0F);
		        }
		
		        if (this.slidingGreen) {
		        	this.sliderGreen = MathHelper.clamp((float)(mouseX - sliderX) / ((float)SLIDER_WIDTH), 0.0F, 1.0F);
		        }
		
		        if (this.slidingBlue) {
		        	this.sliderBlue =  MathHelper.clamp((float)(mouseX - sliderX) / ((float)SLIDER_WIDTH), 0.0F, 1.0F);
		        }
		        
		        if (this.slidingRed || this.slidingGreen || this.slidingBlue) {
			        this.updateColorFromSliders();
		        }
	        } else {
	        	this.wasClicking = this.slidingRed = this.slidingGreen = this.slidingBlue = false;
	        }
	    	
			super.drawScreenOverlay(mouseX, mouseY, partialTicks);
			
			Gui.drawRect(colorDisplayX, colorDisplayY - 1, colorDisplayX + 42, colorDisplayY + 41, 0xFFA0A0A0);
			Gui.drawRect(colorDisplayX + 1, colorDisplayY, colorDisplayX + 41, colorDisplayY + 40, 0xFF000000 + this.colorInput.getInteger());
			
			GuiComponentColorInput.this.editScreen.mc.getTextureManager().bindTexture(CommonGuiUtils.GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
			GuiComponentColorInput.this.editScreen.drawTexturedModalRect(sliderX - 1, sliderY, 81, 87, SLIDER_WIDTH + 2, SLIDER_HEIGHT);
			GuiComponentColorInput.this.editScreen.drawTexturedModalRect(sliderX - 1, sliderY + sliderPadding, 81, 87 + SLIDER_HEIGHT, SLIDER_WIDTH + 2, SLIDER_HEIGHT);
			GuiComponentColorInput.this.editScreen.drawTexturedModalRect(sliderX - 1, sliderY + 2*sliderPadding, 81, 87 + 2*SLIDER_HEIGHT, SLIDER_WIDTH + 2, SLIDER_HEIGHT);
			
			GuiComponentColorInput.this.editScreen.drawTexturedModalRect(sliderX + (this.sliderRed * SLIDER_WIDTH) - 2, sliderY - 1, 183, 87, 5, 9);
			GuiComponentColorInput.this.editScreen.drawTexturedModalRect(sliderX + (this.sliderGreen * SLIDER_WIDTH) - 2, sliderY + sliderPadding - 1, 183, 87, 5, 9);
			GuiComponentColorInput.this.editScreen.drawTexturedModalRect(sliderX + (this.sliderBlue * SLIDER_WIDTH) - 2, sliderY  + 2*sliderPadding - 1, 183, 87, 5, 9);

			this.colorInput.drawInList(colorInputX + SLIDER_WIDTH + 10, colorInputY, colorInputX + SLIDER_WIDTH + 139, mouseX, mouseY);
			
			this.drawPostRender();
	    }
	    
	    @Override
	    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	    	this.colorInput.onMouseClicked(mouseX, mouseY, mouseButton);
	    	
	    	super.mouseClicked(mouseX, mouseY, mouseButton);
	    }

	    @Override
	    public void keyTyped(char keyTyped, int keyCode) throws IOException {
	    	this.colorInput.onKeyTyped(keyTyped, keyCode);

	    	if (keyCode != 1) {
	    		super.keyTyped(keyTyped, keyCode);
	    	}
	    }
	    
	    private int getComponentsHeight() {
	    	return 50;
	    }
		
	    private void updateColorFromInt() {
	    	float[] color = GuiComponentColorInput.this.getColorFloatsFromInt(this.colorInput.getInteger());
	    	this.sliderRed = color[0];
	    	this.sliderGreen = color[1];
	    	this.sliderBlue = color[2];
	    }
		
	    private void updateColorFromSliders() {
	    	this.colorInput.setDefaultInteger(GuiComponentColorInput.this.getColorIntFromFloats(new float[]{this.sliderRed, this.sliderGreen, this.sliderBlue}));
	    }
	}
}
