package com.tmtravlr.additions.gui.edit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.gui.GuiMessagePopupTwoButton;
import com.tmtravlr.additions.gui.edit.components.GuiShowAdvanced;

public abstract class GuiEdit extends GuiScreen {
	
	public static final int LABEL_OFFSET = 140;
			
	protected int buttonCount = 0;
	public final int SAVE_BUTTON = buttonCount++;
	public final int BACK_BUTTON = buttonCount++;
	
	protected GuiScreen parentScreen;
	protected String title;
	protected GuiScrollingEdit editArea;
	protected List<IGuiEditComponent> components = new ArrayList<>();
	protected List<IGuiEditComponent> advancedComponents = new ArrayList<>();
	protected boolean unsavedChanges = false;
	
	private boolean showAdvanced = false;
	private GuiShowAdvanced showAdvancedElement;
	private boolean initializedComponents = false;
	
	public GuiEdit(GuiScreen parentScreen, String title) {
		this.parentScreen = parentScreen;
		this.title = title;
		this.showAdvancedElement = new GuiShowAdvanced(this);
	}
	
	@Override
	public void initGui() {
		if (!this.initializedComponents) {
			this.initComponents();
			this.initializedComponents = true;
		}
		
		this.buttonList.add(new GuiButton(SAVE_BUTTON, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.save")));
		this.buttonList.add(new GuiButton(BACK_BUTTON, this.width - 140, this.height - 30, 60, 20, I18n.format("gui.buttons.back")));
		
		float scrollDistance = 0.0f;
		if (this.editArea != null) {
			scrollDistance = this.editArea.getScrollDistance();
		}
		
		this.editArea = new GuiScrollingEdit(this);
		this.editArea.setScrollDistance(scrollDistance);
		for (IGuiEditComponent component : this.components) {
			this.editArea.addComponent(component);
			component.onInitGui();
		}
		
		if (!this.advancedComponents.isEmpty()) {
			this.editArea.addComponent(this.showAdvancedElement);
			
			if (this.showAdvanced) {
				for (IGuiEditComponent component : this.advancedComponents) {
					this.editArea.addComponent(component);
					component.onInitGui();
				}
			}
		}
	}
	
	/**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == SAVE_BUTTON) {
    		saveObject();
    	} else if (button.id == BACK_BUTTON) {
    		cancelEdit();
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
		for (IGuiEditComponent component : components) {
			component.onMouseClicked(mouseX, mouseY, mouseButton);
		}
		
		if (!this.advancedComponents.isEmpty()) {
			
			if (this.showAdvanced) {
				for (IGuiEditComponent component : advancedComponents) {
					component.onMouseClicked(mouseX, mouseY, mouseButton);
				}
			}
			
			this.showAdvancedElement.onMouseClicked(mouseX, mouseY, mouseButton);

		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void keyTyped(char keyTyped, int keyCode) throws IOException {
		for (IGuiEditComponent component : components) {
			component.onKeyTyped(keyTyped, keyCode);
		}
		
		if (this.showAdvanced) {
			for (IGuiEditComponent component : advancedComponents) {
				component.onKeyTyped(keyTyped, keyCode);
			}
		}
		
		super.keyTyped(keyTyped, keyCode);
	}

    @Override
    public void handleMouseInput() throws IOException {
    	super.handleMouseInput();
    	
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        
        boolean scrollEditArea = true;
        
        for (IGuiEditComponent component : components) {
			if (component.onHandleMouseInput(mouseX, mouseY)) {
				scrollEditArea = false;
			}
		}
		
		if (this.showAdvanced) {
			for (IGuiEditComponent component : advancedComponents) {
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
	
	public void setHasUnsavedChanges() {
		this.unsavedChanges = true;
	}
	
	public void setShowAdvanced(boolean show) {
		
		if (this.showAdvanced && !show) {
			for (IGuiEditComponent component : this.advancedComponents) {
				this.editArea.removeComponent(component);
			}
		} else if (!this.showAdvanced && show) {
			for (IGuiEditComponent component : this.advancedComponents) {
				this.editArea.addComponent(component);
			}
		}
		
		this.showAdvanced = show;
	}
	
	public boolean getShowAdvanced() {
		return this.showAdvanced;
	}
	
	public abstract void saveObject();
	
	public void cancelEdit() {
		if (this.unsavedChanges) {
			final GuiEdit editObject = this;
			this.mc.displayGuiScreen(new GuiMessagePopupTwoButton(this, parentScreen, "gui.warnDialogue.unsaved.title", new TextComponentTranslation("gui.warnDialogue.unsaved.message"), "gui.buttons.close", "gui.warnDialogue.unsaved.continue"));
		} else {
			this.mc.displayGuiScreen(parentScreen);
		}
	}
	
	public abstract void initComponents();
	
}
