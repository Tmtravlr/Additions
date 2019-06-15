package com.tmtravlr.additions.gui.view.edit.update.effect.cause;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum HandTypeLabel {

	MAINHAND_LABEL(I18n.format("gui.edit.effectCause.held.hand.mainhand.label")),
	OFFHAND_LABEL(I18n.format("gui.edit.effectCause.held.hand.offhand.label")),
	BOTH_HANDS_LABEL(I18n.format("gui.edit.effectCause.held.hand.both.label"));
	
	private String label;
	
	private HandTypeLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}
