package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;

import com.tmtravlr.additions.gui.GuiMessagePopupTwoButton;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Lets you build an item stack.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since September 2017 
 */
public class GuiComponentItemStackInput implements IGuiViewComponent {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("additions:textures/gui/additions_gui_textures.png");

	public GuiEdit editScreen;
	public GuiTextField selectedText;
	
	private int x;
	private int y;
	private int width;
	private String label = "";
	private boolean required = false;
	private ItemStack itemStack = ItemStack.EMPTY;
	private boolean hasMeta = true;
	private boolean hasCount = true;
	private boolean hasNBT = true;
	
	public GuiComponentItemStackInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
	}
	
	public void disableMetadata() {
		this.hasMeta = false;
	}
	
	public void disableCount() {
		this.hasCount = false;
	}
	
	public void disableNBT() {
		this.hasNBT = false;
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

		int itemDisplayTop = this.y + 10;
		
		Gui.drawRect(this.x, itemDisplayTop - 1, this.x + 22, itemDisplayTop + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + 1, itemDisplayTop, this.x + 21, itemDisplayTop + 20, 0xFF000000);
		
		this.selectedText.x = x + 30;
		this.selectedText.y = y + 10;
		this.selectedText.width = right - 60 - x;
		
		this.selectedText.drawTextBox();
		
		if (!this.selectedText.getText().isEmpty()) {
			this.editScreen.mc.getTextureManager().bindTexture(GUI_TEXTURES);
		    GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F);
			
		    int deleteX = this.selectedText.x + this.selectedText.width - 15;
			int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
			this.editScreen.drawTexturedModalRect(deleteX, deleteY, 60, 64, 13, 13);
		}

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    	this.editScreen.mc.getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)null, this.itemStack, this.x + 3, itemDisplayTop + 2);
    	if (this.itemStack.getCount() > 1) {
    		this.editScreen.drawCenteredString(this.editScreen.getFontRenderer(), "" + this.itemStack.getCount(), x + 15, itemDisplayTop + 12, 0xFFFFFF);
    	}
    	if (mouseX >= this.x + 3 && mouseX < this.x + 19 && mouseY >= itemDisplayTop + 2 && mouseY < itemDisplayTop + 18) {
    		GlStateManager.pushMatrix();
    		GlStateManager.translate(0, 0, -100);
    		this.editScreen.renderItemStackTooltip(this.itemStack, mouseX, mouseY);
    		GlStateManager.popMatrix();
    	}
    	GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int deleteX = this.selectedText.x + this.selectedText.width - 15;
		int deleteY = this.selectedText.y + (this.selectedText.height / 2 - 6);
		
		if (!this.selectedText.getText().isEmpty()) {
			
			if(mouseX >= deleteX && mouseX < this.selectedText.x + this.selectedText.width && mouseY >= deleteY && mouseY < deleteY + 13) {
				this.clearItemStack();
			}
		}
		
		if (mouseX >= this.selectedText.x - 30 && mouseX < deleteX && mouseY >= this.selectedText.y && mouseY < this.selectedText.y + this.selectedText.height) {
			this.editScreen.mc.displayGuiScreen(new GuiPopupEditItemStack(this.editScreen.mc.currentScreen, this));
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setDefaultItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		if (itemStack == ItemStack.EMPTY) {
			this.selectedText.setText("");
		} else {
			this.selectedText.setText(this.itemStack.getDisplayName());
		}
		this.selectedText.setCursorPositionZero();
		
	}

	public void clearItemStack() {
		this.itemStack = ItemStack.EMPTY;
		this.selectedText.setText("");
		this.editScreen.notifyHasChanges();
	}
	
	public ItemStack getItemStack() {
		return this.itemStack;
	}
	
	class GuiPopupEditItemStack extends GuiMessagePopupTwoButton {
		
		private GuiScreen parentScreen;
		private GuiComponentItemStackInput parent;
		
		private GuiComponentDropdownInputItem itemInput;
		private GuiComponentIntInput metaInput;
		private GuiComponentIntInput countInput;
		private GuiComponentNBTInput nbtInput;

		public GuiPopupEditItemStack(GuiScreen parentScreen, GuiComponentItemStackInput parent) {
			super(parentScreen, parentScreen, I18n.format("gui.popup.itemstack.title"), new TextComponentString(""), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.update"));
			this.parentScreen = parentScreen;
			this.parent = parent;
			
			this.itemInput = new GuiComponentDropdownInputItem(I18n.format("gui.popup.itemstack.item.label"), this.parent.editScreen);
			this.itemInput.setDefaultSelected(parent.itemStack.isEmpty() ? null : parent.itemStack.getItem());
			
			this.metaInput = new GuiComponentIntInput(I18n.format("gui.popup.itemstack.meta.label"), this.parent.editScreen, false);
			this.metaInput.setInteger(parent.itemStack.getMetadata());
			
			this.countInput = new GuiComponentIntInput(I18n.format("gui.popup.itemstack.count.label"), this.parent.editScreen, false);
			this.countInput.setMaximum(64);
			this.countInput.setMinimum(1);
			this.countInput.setInteger(parent.itemStack.getCount());
			
			this.nbtInput = new GuiComponentNBTInput(I18n.format("gui.popup.itemstack.nbt.label"), this.parent.editScreen);
			this.nbtInput.setDefaultText(parent.itemStack.hasTagCompound() ? parent.itemStack.getTagCompound().toString() : "{}");
		}

		@Override
	    protected void actionPerformed(GuiButton button) throws IOException {
	        if (button.id == BUTTON_CONTINUE) {
	        	if (this.itemInput.getSelected() == null) {
	        		this.parent.clearItemStack();
	        	} else {
	        		this.parent.itemStack = new ItemStack(this.itemInput.getSelected(), this.parent.hasCount ? this.countInput.getInteger() : 1, this.metaInput.getInteger());
	        		if (this.nbtInput.getTag() != null) {
	        			this.parent.itemStack.setTagCompound(this.nbtInput.getTag());
	        		}
	        		this.parent.selectedText.setText(this.parent.itemStack.getDisplayName());
	        	}
	        	this.mc.displayGuiScreen(parentScreen);
	        } else {
	        	super.actionPerformed(button);
	        }
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
	    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			super.drawScreen(mouseX, mouseY, partialTicks);
			
			int popupWidth = this.getPopupWidth();
	    	int popupHeight = this.getPopupHeight();
	    	int popupX = this.width / 2 - popupWidth / 2;
	    	int popupY = this.height / 2 - popupHeight / 2;
	    	int popupRight = popupX + popupWidth;
			
			int componentY = popupY + 30;
			int labelOffset = 80;

			this.drawString(this.fontRenderer, this.itemInput.getLabel(), popupX + 10, componentY + this.itemInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
			this.itemInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
			componentY += this.itemInput.getHeight(popupX, popupRight);
			
			if (this.parent.hasMeta) {
				this.drawString(this.fontRenderer, this.metaInput.getLabel(), popupX + 10, componentY + this.metaInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
				this.metaInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
				componentY += this.metaInput.getHeight(popupX, popupRight);
			}
			
			if (this.parent.hasCount) {
				this.drawString(this.fontRenderer, this.countInput.getLabel(), popupX + 10, componentY + this.countInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
				this.countInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
				componentY += this.countInput.getHeight(popupX, popupRight);
			}
			
			if (this.parent.hasNBT) {
				this.drawString(this.fontRenderer, this.nbtInput.getLabel(), popupX + 10, componentY + this.nbtInput.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
				this.nbtInput.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
				componentY += this.nbtInput.getHeight(popupX, popupRight);
			}
	    }
	    
	    @Override
	    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	    	this.itemInput.onMouseClicked(mouseX, mouseY, mouseButton);
	    	
	    	if (this.parent.hasMeta) {
	    		this.metaInput.onMouseClicked(mouseX, mouseY, mouseButton);
	    	}

	    	if (this.parent.hasCount) {
	    		this.countInput.onMouseClicked(mouseX, mouseY, mouseButton);
	    	}
	    	
	    	if (this.parent.hasNBT) {
	    		this.nbtInput.onMouseClicked(mouseX, mouseY, mouseButton);
	    	}
	    	
	    	super.mouseClicked(mouseX, mouseY, mouseButton);
	    }

	    @Override
	    public void keyTyped(char keyTyped, int keyCode) throws IOException {
	    	this.itemInput.onKeyTyped(keyTyped, keyCode);
	    	
	    	if (this.parent.hasMeta) {
	    		this.metaInput.onKeyTyped(keyTyped, keyCode);
	    	}

	    	if (this.parent.hasCount) {
	    		this.countInput.onKeyTyped(keyTyped, keyCode);
	    	}
	    	
	    	if (this.parent.hasNBT) {
	    		this.nbtInput.onKeyTyped(keyTyped, keyCode);
	    	}

	    	if (keyCode != 1) {
	    		super.keyTyped(keyTyped, keyCode);
	    	}
	    }
	    
	    private int getComponentsHeight() {
	    	int popupWidth = this.getPopupWidth();
	    	int popupLeft = this.width / 2 - popupWidth / 2;
	    	int popupRight = popupLeft + popupWidth;
	    	
	    	int height = this.itemInput.getHeight(popupLeft, popupRight);
	    	
	    	if (this.parent.hasMeta) {
	    		height += this.metaInput.getHeight(popupLeft, popupRight);
	    	}
	    	
	    	if (this.parent.hasCount) {
	    		height += this.countInput.getHeight(popupLeft, popupRight);
	    	}
	    	
	    	if (this.parent.hasNBT) {
	    		height += this.nbtInput.getHeight(popupLeft, popupRight);
	    	}
	    	
	    	return height;
	    }
		
	}
}
