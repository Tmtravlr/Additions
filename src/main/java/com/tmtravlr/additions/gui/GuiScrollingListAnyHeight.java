package com.tmtravlr.additions.gui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Adapted from GuiScrollingList, but supports elements having any arbitrary height.
 * Also, it doesn't scroll if you click and drag except on the scroll bar.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date August 2017
 */
public abstract class GuiScrollingListAnyHeight {

	private final Minecraft client;
    protected int listWidth;
    protected int listHeight;
    protected int screenWidth;
    protected int screenHeight;
    protected int top;
    protected int bottom;
    protected int right;
    protected int left;
    private int scrollUpActionId;
    private int scrollDownActionId;
    protected int mouseX;
    protected int mouseY;
    private float initialMouseClickY = -2.0F;
    private float scrollFactor;
    private float scrollDistance;
    protected int selectedIndex = -1;
    private long lastClickTime = 0L;
    private boolean highlightSelected = true;
    private boolean hasHeader;
    private int headerHeight;
    protected boolean captureMouse = true;

    public GuiScrollingListAnyHeight(Minecraft client, int width, int height, int top, int left, int screenWidth, int screenHeight) {
        this.client = client;
        this.listWidth = width;
        this.listHeight = height;
        this.top = top;
        this.bottom = height + this.top;
        this.left = left;
        this.right = width + this.left;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    protected void setHeaderInfo(boolean hasHeader, int headerHeight) {
        this.hasHeader = hasHeader;
        this.headerHeight = headerHeight;
        if (!hasHeader) this.headerHeight = 0;
    }

    protected abstract int getSize();

    protected abstract void elementClicked(int index, boolean doubleClick);

    protected abstract boolean isSelected(int index);

    protected int getContentHeight(int right) {
    	int scrollAreaHeight = 0;
    	for (int i = 0; i < this.getSize(); i++) {
    		scrollAreaHeight += this.getSlotHeight(i, right);
    	}
        return scrollAreaHeight + this.headerHeight;
    }

    protected void drawBackground() {
    	this.drawDefaultBackground();
    }
    
    protected abstract int getSlotHeight(int slotId, int entryRight);

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected abstract void drawSlot(int slotId, int entryRight, int slotTop, int slotBuffer, Tessellator tess);
    
    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {}

    protected void clickHeader(int x, int y) {}
    
    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected void drawScreen(int mouseX, int mouseY) {}

    private void applyScrollLimits() {
        int entryRight = this.left + this.listWidth - 7;
        
        int listHeight = this.getContentHeight(entryRight) - (this.bottom - this.top - 4);

        if (listHeight < 0) {
            listHeight /= 2;
        }

        if (this.scrollDistance < 0.0F) {
            this.scrollDistance = 0.0F;
        }

        if (this.scrollDistance > (float)listHeight) {
            this.scrollDistance = (float)listHeight;
        }
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == this.scrollUpActionId) {
                this.scrollDistance -= 10.0F;
                this.initialMouseClickY = -2.0F;
                this.applyScrollLimits();
            } else if (button.id == this.scrollDownActionId) {
                this.scrollDistance += 10.0F;
                this.initialMouseClickY = -2.0F;
                this.applyScrollLimits();
            }
        }
    }


    public void handleMouseInput(int mouseX, int mouseY) throws IOException {
        boolean isHovering = mouseX >= this.left && mouseX <= this.left + this.listWidth &&
                             mouseY >= this.top && mouseY <= this.bottom;
        if (!isHovering)
            return;

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            this.scrollDistance += (float)((-1 * scroll / 120.0F) * 20);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        boolean isHovering = mouseX >= this.left && mouseX <= this.left + this.listWidth &&
                             mouseY >= this.top && mouseY <= this.bottom;
        int listLength     = this.getSize();
        int scrollBarWidth = 6;
        int scrollBarRight = this.left + this.listWidth;
        int scrollBarLeft  = scrollBarRight - scrollBarWidth;
        int entryLeft      = this.left;
        int entryRight     = scrollBarLeft - 1;
        int viewHeight     = this.bottom - this.top;
        int border         = 4;

        if (Mouse.isButtonDown(0)) {
            if (this.initialMouseClickY == -1.0F) {
                if (isHovering) {
                    int mouseListY = mouseY - this.top - this.headerHeight + (int)this.scrollDistance - border;
                    int slotIndex = this.getSlotIndex(mouseListY, entryRight);

                    if (mouseX >= entryLeft && mouseX <= entryRight && slotIndex >= 0 && mouseListY >= 0 && slotIndex < listLength)
                    {
                        this.elementClicked(slotIndex, slotIndex == this.selectedIndex && System.currentTimeMillis() - this.lastClickTime < 250L);
                        this.selectedIndex = slotIndex;
                        this.lastClickTime = System.currentTimeMillis();
                    }
                    else if (mouseX >= entryLeft && mouseX <= entryRight && mouseListY < 0)
                    {
                        this.clickHeader(mouseX - entryLeft, mouseY - this.top + (int)this.scrollDistance - border);
                    }

                    if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight)
                    {
                        this.scrollFactor = -1.0F;
                        int scrollHeight = this.getContentHeight(entryRight) - viewHeight - border;
                        if (scrollHeight < 1) scrollHeight = 1;

                        int var13 = (int)((float)(viewHeight * viewHeight) / (float)this.getContentHeight(entryRight));

                        if (var13 < 32) var13 = 32;
                        if (var13 > viewHeight - border*2)
                            var13 = viewHeight - border*2;

                        this.scrollFactor /= (float)(viewHeight - var13) / (float)scrollHeight;
                    }
                    else
                    {
                        this.scrollFactor = 1.0F;
                    }

                    this.initialMouseClickY = mouseY;
                }
                else
                {
                    this.initialMouseClickY = -2.0F;
                }
            }
        }
        else
        {
            this.initialMouseClickY = -1.0F;
        }

        this.applyScrollLimits();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldr = tess.getBuffer();

        ScaledResolution res = new ScaledResolution(client);
        double scaleW = client.displayWidth / res.getScaledWidth_double();
        double scaleH = client.displayHeight / res.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int)(left      * scaleW), (int)(client.displayHeight - (bottom * scaleH)),
                       (int)(listWidth * scaleW), (int)(viewHeight * scaleH));

        this.drawBackground();
        
        int baseY = this.top + border - (int)this.scrollDistance;

        if (this.hasHeader) {
            this.drawHeader(entryRight, baseY, tess);
        }
        
        int slotTop = baseY + this.headerHeight;
        for (int slotId = 0; slotId < listLength; ++slotId) {
            
        	int slotHeight = this.getSlotHeight(slotId, entryRight);
            int slotBuffer = slotHeight - border;

            if (slotTop <= this.bottom && slotTop + slotBuffer >= this.top)
            {
                if (this.highlightSelected && this.isSelected(slotId))
                {
                    int min = this.left;
                    int max = entryRight;
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableTexture2D();
                    worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    worldr.pos(min,     slotTop + slotBuffer + 2, 0).tex(0, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(max,     slotTop + slotBuffer + 2, 0).tex(1, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(max,     slotTop              - 2, 0).tex(1, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(min,     slotTop              - 2, 0).tex(0, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(min + 1, slotTop + slotBuffer + 1, 0).tex(0, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(max - 1, slotTop + slotBuffer + 1, 0).tex(1, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(max - 1, slotTop              - 1, 0).tex(1, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(min + 1, slotTop              - 1, 0).tex(0, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    tess.draw();
                    GlStateManager.enableTexture2D();
                }

                this.drawSlot(slotId, entryRight, slotTop, slotBuffer, tess);
            }
            
        	slotTop += slotHeight;
        }

        GlStateManager.disableDepth();

        int extraHeight = (this.getContentHeight(entryRight) + border) - viewHeight;
        if (extraHeight > 0)
        {
            int height = (viewHeight * viewHeight) / this.getContentHeight(entryRight);

            if (height < 32) height = 32;

            if (height > viewHeight - border*2)
                height = viewHeight - border*2;

            int barTop = (int)this.scrollDistance * (viewHeight - height) / extraHeight + this.top;
            if (barTop < this.top)
            {
                barTop = this.top;
            }

            GlStateManager.disableTexture2D();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft,  this.bottom, 0.0D).tex(0.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarRight, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarRight, this.top,    0.0D).tex(1.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft,  this.top,    0.0D).tex(0.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            tess.draw();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft,  barTop + height, 0.0D).tex(0.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarRight, barTop + height, 0.0D).tex(1.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarRight, barTop,          0.0D).tex(1.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarLeft,  barTop,          0.0D).tex(0.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            tess.draw();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft,      barTop + height - 1, 0.0D).tex(0.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarRight - 1, barTop + height - 1, 0.0D).tex(1.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarRight - 1, barTop,              0.0D).tex(1.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarLeft,      barTop,              0.0D).tex(0.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            tess.draw();
        }

        this.drawScreen(mouseX, mouseY);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2)
    {
        float a1 = (float)(color1 >> 24 & 255) / 255.0F;
        float r1 = (float)(color1 >> 16 & 255) / 255.0F;
        float g1 = (float)(color1 >>  8 & 255) / 255.0F;
        float b1 = (float)(color1       & 255) / 255.0F;
        float a2 = (float)(color2 >> 24 & 255) / 255.0F;
        float r2 = (float)(color2 >> 16 & 255) / 255.0F;
        float g2 = (float)(color2 >>  8 & 255) / 255.0F;
        float b2 = (float)(color2       & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right, top, 0.0D).color(r1, g1, b1, a1).endVertex();
        buffer.pos(left,  top, 0.0D).color(r1, g1, b1, a1).endVertex();
        buffer.pos(left,  bottom, 0.0D).color(r2, g2, b2, a2).endVertex();
        buffer.pos(right, bottom, 0.0D).color(r2, g2, b2, a2).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    protected void drawDefaultBackground() {
    	Tessellator tess = Tessellator.getInstance();
    	BufferBuilder worldr = tess.getBuffer();
    	
    	if (this.client.world != null)
        {
            this.drawGradientRect(this.left, this.top, this.right, this.bottom, 0xC0101010, 0xD0101010);
        }
        else // Draw dark dirt background
        {
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            this.client.renderEngine.bindTexture(Gui.OPTIONS_BACKGROUND);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float scale = 32.0F;
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(this.left,  this.bottom, 0.0D).tex(this.left  / scale, (this.bottom + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(this.right, this.bottom, 0.0D).tex(this.right / scale, (this.bottom + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(this.right, this.top,    0.0D).tex(this.right / scale, (this.top    + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            worldr.pos(this.left,  this.top,    0.0D).tex(this.left  / scale, (this.top    + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
            tess.draw();
        }
    }
    
    private int getSlotIndex(int slotY, int right) {
    	int currentY = 0;
    	for(int i = 0; i < this.getSize(); i++) {
    		currentY += this.getSlotHeight(i, right);
    		if (slotY < currentY) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public float getScrollDistance() {
    	return this.scrollDistance;
    }
    
    public void setScrollDistance(float scrollDistance) {
    	this.scrollDistance = scrollDistance;
    }

	public void setNewDimensions(int x, int y, int width, int height) {
		this.left = x;
		this.right = x + width;
		this.top = y;
		this.bottom = y + height;
		this.listWidth = width;
		this.listHeight = height;
		this.applyScrollLimits();
	}
}
