package com.tmtravlr.additions.gui.view.components.input.dropdown;

import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.mapcolors.BlockMapColorManager;
import com.tmtravlr.additions.addon.blocks.materials.BlockMaterialManager;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlockMapColor.MapColorSelection;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlockMaterial.MaterialSelection;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.util.client.CommonGuiUtils;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * Dropdown list specifically for block map colors, which renders the color in the list.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date December 2018
 */
public class GuiComponentDropdownInputBlockMapColor extends GuiComponentDropdownInput<MapColorSelection> {

	public GuiComponentDropdownInputBlockMapColor(String label, GuiEdit editScreen) {
		super(label, editScreen);

		List<MapColorSelection> selections = BlockMapColorManager.getBlockMapColors().entrySet()
				.stream().map(entry -> new MapColorSelection(entry.getKey(), entry.getValue())).collect(Collectors.toList());
		this.setSelections(selections);
	}
	
	@Override
	public String getSelectionName(MapColorSelection selected) {
		return selected == null ? "" : selected.name.toString();
	}
	
	@Override
	protected GuiScrollingDropdown createScrollingDropdown(int dropdownHeight, boolean above) {
		return new GuiScrollingDropdown(this, this.selections, dropdownHeight, above) {

			@Override
			protected void drawSlot(int slot, int right, int top, int slotBuffer, Tessellator tess) {
				if (!this.selectionDisplay.isEmpty()) {
					MapColorSelection toDisplay = this.selectionDisplay.get(slot);
					if (toDisplay != null) {
						GuiComponentDropdownInputBlockMapColor.this.editScreen.drawRect(this.left + 7, top  + 1, this.left + 17, top + 11, 0xFF000000 + toDisplay.mapColor.colorValue);
						this.parentInput.editScreen.getFontRenderer().drawString(this.parentInput.getSelectionName(toDisplay), this.left + 25, top + 2, 0xFFFFFF);
					}
				} else {
					this.parentInput.editScreen.getFontRenderer().drawString(I18n.format("gui.dropdown.nothingToShow"), this.left + 5, top + 2, 0x888888);
				}
			}
		};
	}
	
	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		int colorDisplayTop = y + 10;
		
		CommonGuiUtils.drawOutline(x, colorDisplayTop - 1, 22, 22, 0xFFA0A0A0);
		if (this.getMapColorSelected() != null) {
			Gui.drawRect(x + 1, colorDisplayTop, x + 21, colorDisplayTop + 20, 0xFF000000 + this.getMapColorSelected().colorValue);
		}
		
		this.selectedText.x = x + 30;
		this.selectedText.y = colorDisplayTop;
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
	
	public void setDefaultMapColorSelected(MapColor mapColor) {
		if (mapColor == null || mapColor.colorIndex <= 0) {
			this.setDefaultSelected(null);
		} else {
			ResourceLocation mapColorName = BlockMapColorManager.getBlockMapColorName(mapColor);
			
			if (mapColorName == null) {
				AdditionsMod.logger.warn("Tried to set unknown block map color '" + mapColor.getClass().getName() + "'");
				mapColor = MapColor.AIR;
				mapColorName = BlockMapColorManager.getBlockMapColorName(mapColor);
			}
			
			this.setDefaultSelected(new MapColorSelection(mapColorName, mapColor));
		}
	}
	
	public MapColor getMapColorSelected() {
		return this.getSelected() == null ? null : this.getSelected().mapColor;
	}

	public static class MapColorSelection {
		public ResourceLocation name;
		public MapColor mapColor;
		
		public MapColorSelection(ResourceLocation name, MapColor mapColor) {
			this.name = name;
			this.mapColor = mapColor;
		}
	}
}
