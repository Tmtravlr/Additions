package com.tmtravlr.additions.gui.view.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.util.text.ITextComponent;

/**
 * Displays text in a view screen
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2017 
 */
public class GuiComponentDisplayText implements IGuiViewComponent {
	
	private GuiView viewScreen;
	private ITextComponent text;
	private ITextComponent hoverComponent;
	private ITextComponent clickComponent;
	private boolean ignoreLabel = false;
	private boolean centered = false;
	private boolean hidden = false;
	
	private int width;
	private int x;
	private int y;
	
	public GuiComponentDisplayText(GuiView viewScreen, ITextComponent text) {
		this.viewScreen = viewScreen;
		this.setDisplayText(text);
	}
	
	public void setDisplayText(ITextComponent text) {
		this.text = text;
		
		//Find a hover event and click event if there is one
		this.hoverComponent = this.findFirstHoverComponent(text);
		this.clickComponent = this.findFirstClickComponent(text);
	}
	
	public void setIgnoreLabel(boolean ignoreLabel) {
		this.ignoreLabel = ignoreLabel;
	}
	
	public void setCentered(boolean centered) {
		this.centered = centered;
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
		int lines = this.viewScreen.getFontRenderer().listFormattedStringToWidth(text.getFormattedText(), right - left - 20 - (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET)).size();
		return lines * this.viewScreen.getFontRenderer().FONT_HEIGHT + (lines - 1) * 5 + 20;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.width = right - x - 20 - (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET);
		this.x = x;
		this.y = y;
		int textY = y + 10;
		int currentTextY = textY;
		List<String> linesToRender = this.viewScreen.getFontRenderer().listFormattedStringToWidth(text.getFormattedText(), this.width);
		
		for (String toRender : linesToRender) {
			if (this.centered) { 
				this.viewScreen.drawCenteredString(this.viewScreen.getFontRenderer(), toRender, x + 10 + (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET) + this.width/2, currentTextY, 0xFFFFFF);
			} else {
				this.viewScreen.drawString(this.viewScreen.getFontRenderer(), toRender, x + 10 + (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET), currentTextY, 0xFFFFFF);
			}
			currentTextY += viewScreen.getFontRenderer().FONT_HEIGHT + 5;
		}
		
		if (this.hoverComponent != null) {
			int textX = this.centered ? x + 10 + (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET) + this.width/2 : x + 10 + (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET);
			int textHeight = this.viewScreen.getFontRenderer().FONT_HEIGHT * linesToRender.size() + 5*(linesToRender.size() - 1);
			int textWidth = linesToRender.stream().map(line -> this.viewScreen.getFontRenderer().getStringWidth(line)).max(Integer::compare).orElse(0);
			
			if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, textX, textY, textWidth, textHeight)) {
				this.viewScreen.addPostRender(() -> this.viewScreen.handleComponentHover(this.hoverComponent, mouseX, mouseY));
			}
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (this.clickComponent != null) {
			List<String> linesToRender = this.viewScreen.getFontRenderer().listFormattedStringToWidth(text.getFormattedText(), this.width);
			
			int textX = this.centered ? this.x + 10 + (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET) + this.width/2 : this.x + 10 + (this.ignoreLabel ? 0 : GuiView.LABEL_OFFSET);
			int textY = this.y + 10;
			int textHeight = this.viewScreen.getFontRenderer().FONT_HEIGHT * linesToRender.size() + 5*(linesToRender.size() - 1);
			int textWidth = linesToRender.stream().map(line -> this.viewScreen.getFontRenderer().getStringWidth(line)).max(Integer::compare).orElse(0);
			
			if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, textX, textY, textWidth, textHeight)) {
				this.viewScreen.handleComponentClick(this.clickComponent);
			}
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	private ITextComponent findFirstHoverComponent(ITextComponent text) {
		if (text.getStyle().getHoverEvent() != null) {
			return text;
		} else {
			Iterator<ITextComponent> iterator = text.iterator();
			
			while (iterator.hasNext()) {
				ITextComponent sibling = iterator.next();
				
				if (sibling != text) {
					ITextComponent hoverComponent = this.findFirstHoverComponent(sibling);
					
					if (hoverComponent != null) {
						return hoverComponent;
					}
				}
			}
		
			return null;
		}
	}
	
	private ITextComponent findFirstClickComponent(ITextComponent text) {
		if (text.getStyle().getClickEvent() != null) {
			return text;
		} else {
			Iterator<ITextComponent> iterator = text.iterator();
			
			while (iterator.hasNext()) {
				ITextComponent sibling = iterator.next();
				
				if (sibling != text) {
					ITextComponent clickComponent = this.findFirstClickComponent(sibling);
					
					if (clickComponent != null) {
						return clickComponent;
					}
				}
			}
		
			return null;
		}
	}

}
