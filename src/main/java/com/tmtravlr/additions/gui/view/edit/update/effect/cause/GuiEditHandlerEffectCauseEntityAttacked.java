package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import com.tmtravlr.additions.addon.effects.cause.EffectCause;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityAttacked;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Creates components and handles saving the entity attacked effect cause.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date May 2021
 */
public class GuiEditHandlerEffectCauseEntityAttacked extends GuiEditHandlerEffectCauseEntityWithAttacker {

	@Override
	protected TextComponentBase getDescription() {
		return new TextComponentTranslation("type.effectCause.entityAttacked.description");
	}

	@Override
	public EffectCause createEffectCause() {
		return this.populateEffectCause(new EffectCauseEntityAttacked());
	}

}
