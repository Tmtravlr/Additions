package com.tmtravlr.additions.gui.view.edit.update.effect;

import java.util.Collections;
import java.util.List;

import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectCancelNormal;
import com.tmtravlr.additions.api.gui.IGuiEffectEditHandler;
import com.tmtravlr.additions.gui.view.components.GuiComponentDisplayText;
import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates or updates a cancel normal effect
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date June 2019
 */
public class GuiEditHandlerEffectCancelNormal implements IGuiEffectEditHandler {
	private GuiEdit editScreen;

	private GuiComponentDisplayText description;

	@Override
	public List<IGuiViewComponent> getViewComponents(GuiEdit editScreen, Effect effect) {
		this.editScreen = editScreen;
		
		this.description = new GuiComponentDisplayText(editScreen, new TextComponentTranslation("type.effect.cancelNormal.description"));
		
		return Collections.singletonList(this.description);
	}

	@Override
	public Effect createEffect() {
		if (this.editScreen == null) {
			return null;
		}
		
		return new EffectCancelNormal();
	}

}
