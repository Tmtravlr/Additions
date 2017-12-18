package com.tmtravlr.additions.gui.view;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.gui.view.components.GuiComponentShowAdvanced;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

/**
 * Screen with an embedded list of components to display, meant for viewing objects.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since July 2017 
 */
public abstract class GuiView extends GuiScreen {
	
	public static final int LABEL_OFFSET = 140;
	
	private static final int TITLE_HEIGHT = 30;
	private static final int BUTTON_FOOTER_HEIGHT = 40;
			
	protected int buttonCount = 0;
	protected final int BACK_BUTTON = buttonCount++;
	
	protected GuiScreen parentScreen;
	protected String title;
	protected GuiScrollingView editArea;
	protected List<IGuiViewComponent> components = new ArrayList<>();
	protected List<IGuiViewComponent> advancedComponents = new ArrayList<>();
	protected boolean initializedComponents = false;
	
	private boolean showAdvanced = false;
	private GuiComponentShowAdvanced showAdvancedElement;
	
	public GuiView(GuiScreen parentScreen, String title) {
		this.parentScreen = parentScreen;
		this.title = title;
		this.showAdvancedElement = new GuiComponentShowAdvanced(this);
	}
	
	@Override
	public void initGui() {
		if (!this.initializedComponents) {
			this.initComponents();
			this.initializedComponents = true;
		}
		
		this.buttonList.add(new GuiButton(BACK_BUTTON, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.back")));
		
		float scrollDistance = 0.0f;
		if (this.editArea != null) {
			scrollDistance = this.editArea.getScrollDistance();
		}
		
		this.editArea = new GuiScrollingView(this);
		this.editArea.setScrollDistance(scrollDistance);
		for (IGuiViewComponent component : this.components) {
			this.editArea.addComponent(component);
			component.onInitGui();
		}
		
		if (!this.advancedComponents.isEmpty()) {
			this.editArea.addComponent(this.showAdvancedElement);
			
			if (this.showAdvanced) {
				for (IGuiViewComponent component : this.advancedComponents) {
					this.editArea.addComponent(component);
					component.onInitGui();
				}
			}
		}
	}
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BACK_BUTTON) {
    		this.mc.displayGuiScreen(this.parentScreen);
    	}
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.editArea.drawScreen(mouseX, mouseY, partialTicks);
		
		this.drawBackground();
		
		int titleX = (this.width - this.fontRenderer.getStringWidth(this.title)) / 2;
		this.fontRenderer.drawString(this.title, titleX, 11, 0xFFFFFF);
        
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
    public void drawBackground() {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        this.mc.getTextureManager().bindTexture(OPTIONS_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, 30.0D, 0.0D).tex(0.0D, (double)(30.0F / 32.0F)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double)this.width, 30.0D, 0.0D).tex((double)((float)this.width / 32.0F), (double)(30.0F / 32.0F)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double)this.width, 0.0D, 0.0D).tex((double)((float)this.width / 32.0F), 0.0D).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double)this.height, 0.0D).tex(0.0D, (double)(40.0F / 32.0F)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double)this.width, (double)this.height, 0.0D).tex((double)((float)this.width / 32.0F), (double)(40.0F / 32.0F)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double)this.width, (double)this.height - 40.0D, 0.0D).tex((double)((float)this.width / 32.0F), 0.0D).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, (double)this.height - 40.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseY > TITLE_HEIGHT && mouseY < this.height - BUTTON_FOOTER_HEIGHT) {
			for (IGuiViewComponent component : components) {
				component.onMouseClicked(mouseX, mouseY, mouseButton);
			}
			
			if (!this.advancedComponents.isEmpty()) {
				
				if (this.showAdvanced) {
					for (IGuiViewComponent component : advancedComponents) {
						component.onMouseClicked(mouseX, mouseY, mouseButton);
					}
				}
				
				this.showAdvancedElement.onMouseClicked(mouseX, mouseY, mouseButton);
	
			}
		}
		
		this.editArea.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void keyTyped(char keyTyped, int keyCode) throws IOException {
		for (IGuiViewComponent component : components) {
			component.onKeyTyped(keyTyped, keyCode);
		}
		
		if (this.showAdvanced) {
			for (IGuiViewComponent component : advancedComponents) {
				component.onKeyTyped(keyTyped, keyCode);
			}
		}

    	if (keyCode != 1) {
    		super.keyTyped(keyTyped, keyCode);
    	}
	}

    @Override
    public void handleMouseInput() throws IOException {
    	super.handleMouseInput();
    	
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        
        boolean scrollEditArea = true;
        
        for (IGuiViewComponent component : components) {
			if (component.onHandleMouseInput(mouseX, mouseY)) {
				scrollEditArea = false;
			}
		}
		
		if (this.showAdvanced) {
			for (IGuiViewComponent component : advancedComponents) {
				if (component.onHandleMouseInput(mouseX, mouseY)) {
					scrollEditArea = false;
				}
			}
		}
        
        if (scrollEditArea) {
        	this.editArea.handleMouseInput(mouseX, mouseY);
        }
    }
    
    public FontRenderer getFontRenderer() {
    	return this.fontRenderer;
    }
    
    public void buttonClicked(GuiButton button) {
    	this.actionPerformed(button);
    }
    
    public void renderItemStackTooltip(ItemStack stack, int x, int y) {
    	this.renderToolTip(stack, x, y);
    }
	
	public void setShowAdvanced(boolean show) {
		
		if (this.showAdvanced && !show) {
			for (IGuiViewComponent component : this.advancedComponents) {
				this.editArea.removeComponent(component);
			}
		} else if (!this.showAdvanced && show) {
			for (IGuiViewComponent component : this.advancedComponents) {
				this.editArea.addComponent(component);
			}
		}
		
		this.showAdvanced = show;
	}
	
	public boolean getShowAdvanced() {
		return this.showAdvanced;
	}
	
	public GuiScreen getParentScreen() {
		return this.parentScreen;
	}
	
	public void refreshView() {
		float scrollDistance = this.editArea.getScrollDistance();
		this.components.clear();
		this.initComponents();
		this.editArea.setScrollDistance(scrollDistance);
	}
	
	public abstract void initComponents();
	
}
