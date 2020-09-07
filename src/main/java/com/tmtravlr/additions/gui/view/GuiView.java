package com.tmtravlr.additions.gui.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.gui.view.components.GuiComponentShowAdvanced;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * Screen with an embedded list of components to display, meant for viewing objects.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date July 2017
 */
public abstract class GuiView extends GuiScreen {
	
	public static final int LABEL_OFFSET = 140;
	public static final int MAX_WIDTH = 700;
	
	private static final int TITLE_HEIGHT = 30;
	private static final int BUTTON_FOOTER_HEIGHT = 40;
			
	protected int buttonCount = 0;
	protected final int BUTTON_BACK = buttonCount++;
	
	protected GuiScreen parentScreen;
	protected String title;
	protected GuiScrollingView editArea;
	protected List<IGuiViewComponent> components = new ArrayList<>();
	protected List<IGuiViewComponent> advancedComponents = new ArrayList<>();
	protected boolean initializedComponents = false;
	
	private boolean showAdvanced = false;
	private boolean toggleShowAdvanced = false;
	private GuiComponentShowAdvanced showAdvancedElement;
	private List<Runnable> postRender = new ArrayList<>();
	
	public GuiView(GuiScreen parentScreen, String title) {
		this.parentScreen = parentScreen;
		this.title = title;
		this.showAdvancedElement = new GuiComponentShowAdvanced(this);
	}
	
	@Override
	public void initGui() {
		if (!this.initializedComponents) {
			this.recreateComponents();
			this.initializedComponents = true;
		}
		
		this.addButtons();
		
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
	}
	
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == BUTTON_BACK) {
			if (this.parentScreen instanceof GuiView) {
				((GuiView)this.parentScreen).refreshView();
			}
    		this.mc.displayGuiScreen(this.parentScreen);
    	}
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (this.toggleShowAdvanced) {
			if (this.showAdvanced) {
				for (IGuiViewComponent component : this.advancedComponents) {
					component.setHidden(true);
				}
			} else if (!this.showAdvanced) {
				for (IGuiViewComponent component : this.advancedComponents) {
					component.setHidden(false);
				}
			}
			
			this.showAdvanced = !this.showAdvanced;
			this.toggleShowAdvanced = false;
			this.onToggleShowAdvanced(this.showAdvanced);
		}
		
		this.editArea.drawScreen(mouseX, mouseY, partialTicks);
		
		this.drawBackground();
		
		int titleX = (this.width - this.fontRenderer.getStringWidth(this.title)) / 2;
		this.fontRenderer.drawString(this.title, titleX, 11, 0xFFFFFF);
        
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		this.drawPostRender();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseY > TITLE_HEIGHT && mouseY < this.height - BUTTON_FOOTER_HEIGHT) {
			this.editArea.onMouseClicked(mouseX, mouseY, mouseButton);
		}
		
		this.editArea.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void keyTyped(char keyTyped, int keyCode) throws IOException {
		this.editArea.onKeyTyped(keyTyped, keyCode);

    	if (keyCode != 1) {
    		super.keyTyped(keyTyped, keyCode);
    	}
	}

    @Override
    public void handleMouseInput() throws IOException {
    	super.handleMouseInput();
    	
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        
    	this.editArea.handleMouseInput(mouseX, mouseY);
    }
    
    @Override
	public void handleComponentHover(ITextComponent component, int x, int y) {
    	super.handleComponentHover(component, x, y);
    }
	
	protected void addButtons() {
		this.buttonList.add(new GuiButton(BUTTON_BACK, this.width - 70, this.height - 30, 60, 20, I18n.format("gui.buttons.back")));
	}
	
	public void addPostRender(Runnable runnable) {
		this.postRender.add(runnable);
	}
	
	public void drawPostRender() {
		this.postRender.forEach(runnable -> runnable.run());
		this.postRender.clear();
	}
	
    public void drawBackground() {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        this.mc.getTextureManager().bindTexture(OPTIONS_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, 30.0D, 0.0D).tex(0.0D, 30.0F / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(this.width, 30.0D, 0.0D).tex(this.width / 32.0F, 30.0F / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(this.width, 0.0D, 0.0D).tex(this.width / 32.0F, 0.0D).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, this.height, 0.0D).tex(0.0D, 40.0F / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(this.width, this.height, 0.0D).tex(this.width / 32.0F, 40.0F / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(this.width, this.height - 40.0D, 0.0D).tex(this.width / 32.0F, 0.0D).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, this.height - 40.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }
    
    public FontRenderer getFontRenderer() {
    	return this.fontRenderer;
    }
    
    public void buttonClicked(GuiButton button) {
    	this.actionPerformed(button);
    }
    
    public void renderItemStack(ItemStack stack, int x, int y) {
        this.renderItemStack(stack, x, y, 0, 0, false);
    }
    
    public void renderItemStack(ItemStack stack, int x, int y, int mouseX, int mouseY, boolean showTooltip) {
    	this.renderItemStack(stack, x, y, mouseX, mouseY, showTooltip, false);
    }
    
    public void renderItemStack(ItemStack stack, int x, int y, int mouseX, int mouseY, boolean showTooltip, boolean showOverlay) {
    	try {
	        RenderHelper.enableGUIStandardItemLighting();
	        GlStateManager.enableLighting();
	        GlStateManager.enableDepth();
	        GlStateManager.enableRescaleNormal();
	    	this.mc.getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)null, stack, x, y);
	    	if (showOverlay) {
	            this.mc.getRenderItem().renderItemOverlays(this.fontRenderer, stack, x, y);
	    	}
	    	if (showTooltip && CommonGuiUtils.isMouseWithin(mouseX, mouseY, x, y, 16, 16)) {
	    		this.renderItemStackTooltip(stack, mouseX, mouseY);
	    	}
			RenderHelper.disableStandardItemLighting();
	        GlStateManager.disableRescaleNormal();
	        GlStateManager.disableLighting();
	        GlStateManager.disableDepth();
    	} catch (Exception e) {
    		AdditionsMod.logger.warn("Caught exception while trying to render an item stack: " + stack, e);
    	}
    }
    
    public void renderItemStackTooltip(ItemStack stack, int x, int y) {
    	this.addPostRender(() -> {
    		try {
	    		this.renderToolTip(stack, x, y);
	        	GlStateManager.disableRescaleNormal();
	            RenderHelper.disableStandardItemLighting();
	            GlStateManager.disableLighting();
	            GlStateManager.disableDepth();
    		} catch (Exception e) {
    			// Null pointer exceptions are thrown if the advanced tooltips are shown and the item isn't registered yet...
    			// I could check for that, but there's no guarantee mods won't add info with a similar problem, so swallowing.
    		}
		});
    }
    
    public void renderInfoTooltip(List<String> info, int x, int y) {
    	this.addPostRender(() -> {
	    	GuiUtils.drawHoveringText(info, x, y, this.width, this.height, -1, this.fontRenderer);
	        RenderHelper.disableStandardItemLighting();
    	});
    }
    
    public void toggleShowAdvanced() {
    	this.toggleShowAdvanced = true;
    }
	
	public boolean getShowAdvanced() {
		return this.toggleShowAdvanced ? !this.showAdvanced : this.showAdvanced;
	}
	
	public GuiScreen getParentScreen() {
		return this.parentScreen;
	}
	
	public void refreshView() {
		float scrollDistance = this.editArea.getScrollDistance();
		this.recreateComponents();
		this.editArea.setScrollDistance(scrollDistance);
	}
	
	protected void onToggleShowAdvanced(boolean showAdvanced) {}
	
	protected void recreateComponents() {
		this.components.clear();
		this.advancedComponents.clear();
		this.initComponents();
		
		if (!this.advancedComponents.isEmpty()) {
			this.components.add(this.showAdvancedElement);
			this.components.addAll(this.advancedComponents);
			
			if (!this.showAdvanced) {
				this.advancedComponents.forEach(component -> component.setHidden(true));
			}
		}
	}
	
	public abstract void initComponents();
	
}
