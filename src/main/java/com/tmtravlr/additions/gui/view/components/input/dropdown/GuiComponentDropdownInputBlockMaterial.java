package com.tmtravlr.additions.gui.view.components.input.dropdown;

import java.util.List;
import java.util.stream.Collectors;

import com.tmtravlr.additions.AdditionsMod;
import com.tmtravlr.additions.addon.blocks.materials.BlockMaterialManager;
import com.tmtravlr.additions.gui.view.components.input.dropdown.GuiComponentDropdownInputBlockMaterial.MaterialSelection;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

/**
 * Dropdown list specifically for block materials.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018
 */
public class GuiComponentDropdownInputBlockMaterial extends GuiComponentDropdownInput<MaterialSelection> {

	public GuiComponentDropdownInputBlockMaterial(String label, GuiEdit editScreen) {
		super(label, editScreen);

		List<MaterialSelection> selections = BlockMaterialManager.getBlockMaterials().entrySet()
				.stream().map(entry -> new MaterialSelection(entry.getKey(), entry.getValue())).collect(Collectors.toList());
		this.setSelections(selections);
	}
	
	@Override
	public String getSelectionName(MaterialSelection selected) {
		return selected == null ? "" : selected.name.toString();
	}
	
	public void setDefaultMaterialSelected(Material material) {
		if (material == null) {
			this.setDefaultSelected(null);
		} else {
			ResourceLocation materialName = BlockMaterialManager.getBlockMaterialName(material);
			
			if (materialName == null) {
				AdditionsMod.logger.warn("Tried to set unknown block material '" + material.getClass().getName() + "'");
				material = Material.ROCK;
				materialName = BlockMaterialManager.getBlockMaterialName(material);
			}
			
			this.setDefaultSelected(new MaterialSelection(materialName, material));
		}
	}
	
	public Material getMaterialSelected() {
		return this.getSelected() == null ? null : this.getSelected().material;
	}

	public static class MaterialSelection {
		public ResourceLocation name;
		public Material material;
		
		public MaterialSelection(ResourceLocation name, Material material) {
			this.name = name;
			this.material = material;
		}
	}
}
