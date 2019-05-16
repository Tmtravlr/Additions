package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import com.tmtravlr.additions.addon.recipes.IngredientOreNBT;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.message.GuiMessageBoxTwoButton;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputPotion;
import com.tmtravlr.additions.gui.view.components.input.suggestion.GuiComponentSuggestionInput;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditIngredientOreNBTInput;
import com.tmtravlr.additions.type.attribute.AttributeTypeManager;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Lets you build a potion effect.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since May 2019
 */
public class GuiComponentPotionEffectInput implements IGuiViewComponent {

	protected GuiEdit editScreen;
	protected GuiTextField selectedText;
	
	protected int x;
	protected int y;
	protected int width;
	protected boolean hidden = false;
	protected String label = "";
	protected boolean required = false;
	protected PotionEffect effect = null;
	
	protected ItemStack displayStack = ItemStack.EMPTY;
	
	public GuiComponentPotionEffectInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
		this.selectedText = new GuiTextField(0, this.editScreen.getFontRenderer(), 0, 0, 0, 20);
		this.selectedText.setMaxStringLength(1024);
		this.selectedText.setEnabled(false);
	}
	
	@Override
	public boolean isRequired() {
		return this.required;
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

		int displayY = this.y + 10;
		
		Gui.drawRect(this.x, displayY - 1, this.x + 22, displayY + 21, 0xFFA0A0A0);
		Gui.drawRect(this.x + 1, displayY, this.x + 21, displayY + 20, 0xFF000000);
		
		if (!this.displayStack.isEmpty()) {
			this.editScreen.renderItemStack(this.displayStack, this.x + 3, displayY + 2, mouseX, mouseY, true);
		}
		
		this.selectedText.x = x + 30;
		this.selectedText.y = y + 10;
		this.selectedText.width = right - 90 - x;
		
		this.selectedText.drawTextBox();
		
		if (!this.selectedText.getText().isEmpty()) {
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
		
		if (!this.selectedText.getText().isEmpty() && CommonGuiUtils.isMouseWithin(mouseX, mouseY, deleteX, deleteY, 13, 13)) {
			this.setPotionEffect(null);
		} else {		
			if (CommonGuiUtils.isMouseWithin(mouseX, mouseY, this.x, this.y + 10, this.selectedText.x + this.selectedText.width - this.x, this.selectedText.height)) {
				this.editScreen.mc.displayGuiScreen(new GuiMessageBoxEditPotionEffect(this.editScreen.mc.currentScreen, this));
			}
		}
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	
	public void setRequired() {
		this.required = true;
	}
	
	public void setDefaultPotionEffect(PotionEffect effect) {
		this.effect = effect;
		
		if (effect == null) {
			this.selectedText.setText("");
			this.displayStack = ItemStack.EMPTY;
		} else {
			this.selectedText.setText(I18n.format(effect.getEffectName()));
			this.displayStack = new ItemStack(Items.POTIONITEM);
			
			PotionUtils.appendEffects(this.displayStack, Collections.singleton(effect));
			PotionEffect effectWithParticles = new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier());
			this.displayStack.getTagCompound().setInteger("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(Collections.singleton(effectWithParticles)));
		}
		
		this.selectedText.setCursorPositionZero();
	}
	
	public void setPotionEffect(PotionEffect potionEffect) {
		this.setDefaultPotionEffect(potionEffect);
		this.editScreen.notifyHasChanges();
	}
	
	public PotionEffect getPotionEffect() {
		return this.effect;
	}
	
	protected class GuiMessageBoxEditPotionEffect extends GuiMessageBoxTwoButton {
		
		private GuiScreen parentScreen;
		private GuiComponentPotionEffectInput parent;
		
		private GuiComponentDropdownInputPotion potionInput;
		private GuiComponentIntegerInput durationInput;
		private GuiComponentIntegerInput amplifierInput;
		private GuiComponentDropdownInput<ParticleType> particleTypeInput;
		
		private ArrayList<IGuiViewComponent> components = new ArrayList<>();

		public GuiMessageBoxEditPotionEffect(GuiScreen parentScreen, GuiComponentPotionEffectInput parent) {
			super(parentScreen, parentScreen, I18n.format("gui.popup.potionEffect.title"), new TextComponentString(""), I18n.format("gui.buttons.back"), I18n.format("gui.buttons.update"));
			this.parentScreen = parentScreen;
			this.parent = parent;
			this.setViewScreen(parent.editScreen);
			
			this.potionInput = new GuiComponentDropdownInputPotion(I18n.format("gui.popup.potionEffect.potion.label"), this.parent.editScreen);
			if (parent.effect != null) {
				this.potionInput.setDefaultSelected(parent.effect.getPotion());
			}
			
			this.durationInput = new GuiComponentIntegerInput(I18n.format("gui.popup.potionEffect.duration.label"), this.parent.editScreen, false);
			this.durationInput.setInfo(new TextComponentTranslation("gui.popup.potionEffect.duration.info"));
			this.durationInput.setMinimum(1);
			if (parent.effect != null) {
				this.durationInput.setDefaultInteger(parent.effect.getDuration());
			} else {
				this.durationInput.setDefaultInteger(1);
			}
			
			this.amplifierInput = new GuiComponentIntegerInput(I18n.format("gui.popup.potionEffect.amplifier.label"), this.parent.editScreen, false);
			this.amplifierInput.setInfo(new TextComponentTranslation("gui.popup.potionEffect.amplifier.info"));
			this.amplifierInput.setMinimum(0);
			this.amplifierInput.setMaximum(Byte.MAX_VALUE);
			if (parent.effect != null) {
				this.amplifierInput.setDefaultInteger(parent.effect.getAmplifier());
			} else {
				this.amplifierInput.setDefaultInteger(0);
			}
			
			this.particleTypeInput = new GuiComponentDropdownInput<ParticleType>(I18n.format("gui.popup.potionEffect.particle.label"), this.parent.editScreen) {
				
				@Override
				public String getSelectionName(ParticleType selection) {
					return selection.getLabel();
				}
				
			};
			this.particleTypeInput.setSelections(ParticleType.values());
			this.particleTypeInput.disallowDelete();
			if (parent.effect != null) {
				this.particleTypeInput.setDefaultSelected(parent.effect.doesShowParticles() ? parent.effect.getIsAmbient() ? ParticleType.FADED : ParticleType.NORMAL : ParticleType.NONE);
			} else {
				this.particleTypeInput.setSelected(ParticleType.NORMAL);
			}
			
			this.components.add(this.potionInput);
			this.components.add(this.durationInput);
			this.components.add(this.amplifierInput);
			this.components.add(this.particleTypeInput);
		}

		@Override
	    protected void actionPerformed(GuiButton button) throws IOException {
	        if (button.id == SECOND_BUTTON) {
	        	if (this.potionInput.getSelected() == null) {
	        		this.parent.setPotionEffect(null);
	        	} else {
	        		ParticleType particleType = this.particleTypeInput.getSelected() == null ? ParticleType.NORMAL : this.particleTypeInput.getSelected();
	        		boolean showParticles = particleType != ParticleType.NONE;
	        		boolean ambient = particleType == ParticleType.FADED;
	        		
	        		this.parent.setPotionEffect(new PotionEffect(this.potionInput.getSelected(), this.durationInput.getInteger(), this.amplifierInput.getInteger(), ambient, showParticles));
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
	    	return this.getComponentsHeight() + 60;
	    }

	    @Override
	    public void drawScreenOverlay(int mouseX, int mouseY, float partialTicks) {
			super.drawScreenOverlay(mouseX, mouseY, partialTicks);
			
			int popupWidth = this.getPopupWidth();
	    	int popupHeight = this.getPopupHeight();
	    	int popupX = this.width / 2 - popupWidth / 2;
	    	int popupY = this.height / 2 - popupHeight / 2;
	    	int popupRight = popupX + popupWidth;
			
			int componentY = popupY + 30;
			int labelOffset = 80;

			for (IGuiViewComponent component : this.components) {
				this.drawString(this.fontRenderer, component.getLabel(), popupX + 10, componentY + component.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
				if (component.isRequired()) {
					this.drawString(this.fontRenderer, "*", popupX + labelOffset + 2, componentY + component.getHeight(popupX, popupRight)/2 - 5, 0xFFFFFF);
				}
				component.drawInList(popupX + labelOffset + 10, componentY, popupRight, mouseX, mouseY);
				componentY += component.getHeight(popupX, popupRight);
			}
			
			this.drawPostRender();
	    }
	    
	    @Override
	    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	    	
	    	for (IGuiViewComponent component : this.components) {
	    		component.onMouseClicked(mouseX, mouseY, mouseButton);
	    	}
	    	
	    	super.mouseClicked(mouseX, mouseY, mouseButton);
	    }

	    @Override
	    public void keyTyped(char keyTyped, int keyCode) throws IOException {

	    	for (IGuiViewComponent component : this.components) {
	    		component.onKeyTyped(keyTyped, keyCode);
	    	}
	    	
	    	if (keyCode != 1) {
	    		super.keyTyped(keyTyped, keyCode);
	    	}
	    }
	    
	    private int getComponentsHeight() {
	    	int popupWidth = this.getPopupWidth();
	    	int popupLeft = this.width / 2 - popupWidth / 2;
	    	int popupRight = popupLeft + popupWidth;
	    	int height = 0;
	    	
	    	for (IGuiViewComponent component : this.components) {
	    		height += component.getHeight(popupLeft, popupRight);
	    	}
	    	
	    	return height;
	    }
		
	}
	    
    private static enum ParticleType {
    	NORMAL(I18n.format("gui.popup.potionEffect.particle.normal.label")),
    	FADED(I18n.format("gui.popup.potionEffect.particle.faded.label")),
    	NONE(I18n.format("gui.popup.potionEffect.particle.none.label"));
    	
    	private String label;
    	
    	ParticleType(String label) {
    		this.label = label;
    	}
    	
    	public String getLabel() {
    		return this.label;
    	}
    }
}
