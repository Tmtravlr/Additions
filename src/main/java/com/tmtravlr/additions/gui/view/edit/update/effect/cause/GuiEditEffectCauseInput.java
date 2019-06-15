package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseManager;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseEditHandler;
import com.tmtravlr.additions.api.gui.IGuiEffectCauseFactory;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.effect.GuiComponentEffectCauseInput;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditUpdate;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates or updates an effect cause
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class GuiEditEffectCauseInput extends GuiEditUpdate {
	
	private final GuiComponentEffectCauseInput parent;
	private final Addon addon;
	
	private EffectCause effectCause;
	private IGuiEffectCauseEditHandler editHandler;
	
	private GuiComponentDropdownInput<ResourceLocation> effectCauseTypeInput;
	private GuiComponentDisplayText noGuiWarning;
	private List<IGuiViewComponent> effectCauseComponents = new ArrayList();
	
	public GuiEditEffectCauseInput(GuiScreen parentScreen, GuiComponentEffectCauseInput parent, EffectCause effectCause, Addon addon) {
		super(parentScreen, I18n.format("gui.edit.effectCause.title"));
		this.parent = parent;
		this.addon = addon;
		this.effectCause = effectCause;
	}
	
	@Override
	public void initComponents() {
		this.noGuiWarning = new GuiComponentDisplayText(this, new TextComponentTranslation(""));
		this.noGuiWarning.setHidden(true);
		
		this.effectCauseTypeInput = new GuiComponentDropdownInput<ResourceLocation>(I18n.format("gui.edit.effectCause.type.label"), this) {
			
			@Override
			public String getSelectionName(ResourceLocation selection) {
				IGuiEffectCauseFactory factory = EffectCauseManager.getGuiFactoryFor(selection);
				
				return factory == null ? selection.toString() : factory.getTitle(null);
			}
			
			@Override
			public void setDefaultSelected(ResourceLocation selected) {
				if (!Objects.equal(selected, this.getSelected())) {
					GuiEditEffectCauseInput.this.handleEffectCauseTypeSelected(selected);
				}
				super.setDefaultSelected(selected);
			}
			
		};
		this.effectCauseTypeInput.setRequired();
		this.effectCauseTypeInput.disallowDelete();
		this.effectCauseTypeInput.setSelections(EffectCauseManager.getAllTypes());
		
		this.components.add(effectCauseTypeInput);
		this.components.add(noGuiWarning);
		
		if (this.effectCause != null) {
			this.effectCauseTypeInput.setDefaultSelected(EffectCauseManager.getTypeFor(this.effectCause));
		}
	}

	@Override
    public void saveObject() {
		if (this.editHandler == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.effectCause.problem.title"), new TextComponentTranslation("gui.edit.effectCause.problem.noGui.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		EffectCause effectCause = this.editHandler.createEffectCause();
		
		if (effectCause == null) {
			return;
		}
		
		this.parent.setEffectCause(effectCause);
		this.mc.displayGuiScreen(this.parentScreen);
	}
	
	private void handleEffectCauseTypeSelected(ResourceLocation selected) {
		this.components.removeAll(this.effectCauseComponents);
		this.effectCauseComponents.clear();
		this.noGuiWarning.setHidden(true);
		
		if (selected != null) {
			IGuiEffectCauseFactory guiFactory = EffectCauseManager.getGuiFactoryFor(selected);
			if (guiFactory == null || guiFactory.getEditHandler() == null) {
				this.noGuiWarning.setHidden(false);
			} else {
				this.editHandler = guiFactory.getEditHandler();
				this.effectCauseComponents.addAll(this.editHandler.createViewComponents(this, this.effectCause));
				this.components.addAll(this.effectCauseComponents);
			}
		}
	}

}
