package com.tmtravlr.additions.gui.view.edit.update.effect;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;
import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectManager;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseManager;
import com.tmtravlr.additions.api.gui.IGuiEffectEditHandler;
import com.tmtravlr.additions.api.gui.IGuiEffectFactory;
import com.tmtravlr.additions.gui.message.GuiMessageBox;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInput;
import com.tmtravlr.additions.gui.view.components.input.effect.GuiComponentEffectInput;
import com.tmtravlr.additions.gui.view.edit.update.GuiEditUpdate;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates or updates an effect
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2019
 */
public class GuiEditEffectInput extends GuiEditUpdate {
	
	private final GuiComponentEffectInput parent;
	private final Addon addon;
	
	private Effect effect;
	private IGuiEffectEditHandler editHandler;
	
	private GuiComponentDropdownInput<ResourceLocation> effectTypeInput;
	private GuiComponentDisplayText noGuiWarning;
	private List<IGuiViewComponent> effectComponents = new ArrayList();
	
	public GuiEditEffectInput(GuiScreen parentScreen, GuiComponentEffectInput parent, Effect effect, Addon addon) {
		super(parentScreen, I18n.format("gui.edit.effect.title"));
		this.parent = parent;
		this.addon = addon;
		this.effect = effect;
	}
	
	@Override
	public void initComponents() {
		this.noGuiWarning = new GuiComponentDisplayText(this, new TextComponentTranslation(""));
		this.noGuiWarning.setHidden(true);
		
		this.effectTypeInput = new GuiComponentDropdownInput<ResourceLocation>(I18n.format("gui.edit.effect.type.label"), this) {
			
			@Override
			public String getSelectionName(ResourceLocation selection) {
				IGuiEffectFactory factory = EffectManager.getGuiFactoryFor(selection);
				
				return factory == null ? selection.toString() : factory.getTitle();
			}
			
			@Override
			public void setDefaultSelected(ResourceLocation selected) {
				if (!Objects.equal(selected, this.getSelected())) {
					GuiEditEffectInput.this.handleEffectTypeSelected(selected);
				}
				super.setDefaultSelected(selected);
			}
			
		};
		this.effectTypeInput.setRequired();
		this.effectTypeInput.disallowDelete();
		this.effectTypeInput.setSelections(EffectManager.getAllTypes());
		
		this.components.add(effectTypeInput);
		this.components.add(noGuiWarning);
		
		if (this.effect != null) {
			this.effectTypeInput.setDefaultSelected(EffectManager.getTypeFor(this.effect));
		}
	}

	@Override
    public void saveObject() {
		if (this.editHandler == null) {
			this.mc.displayGuiScreen(new GuiMessageBox(this, I18n.format("gui.edit.effect.problem.title"), new TextComponentTranslation("gui.edit.effect.problem.noGui.message"), I18n.format("gui.buttons.back")));
			return;
		}
		
		Effect effect = this.editHandler.createEffect();
		
		if (effect == null) {
			return;
		}
		
		this.parent.setEffect(effect);
		this.mc.displayGuiScreen(this.parentScreen);
	}
	
	private void handleEffectTypeSelected(ResourceLocation selected) {
		this.components.removeAll(this.effectComponents);
		this.effectComponents.clear();
		this.noGuiWarning.setHidden(true);
		this.refreshView();

		if (selected != null) {
			IGuiEffectFactory guiFactory = EffectManager.getGuiFactoryFor(selected);
			if (guiFactory == null || guiFactory.getEditHandler() == null) {
				this.noGuiWarning.setHidden(false);
			} else {
				this.editHandler = guiFactory.getEditHandler();
				this.effectComponents.addAll(this.editHandler.getViewComponents(this, this.effect));
				this.components.addAll(this.effectComponents);
			}
		}
	}

}
