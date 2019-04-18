package com.tmtravlr.additions.gui.view.components.input.suggestion;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.type.AdditionTypeLootTable;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * Dropdown list specifically for tool types.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since December 2018
 */
public class GuiComponentSuggestionInputLootTable extends GuiComponentSuggestionInput {

	public GuiComponentSuggestionInputLootTable(String label, GuiEdit editScreen) {
		super(label, editScreen);
		
		Set<ResourceLocation> suggestions = new HashSet<>(LootTableList.getAll());
		suggestions.addAll(AdditionTypeLootTable.INSTANCE.getAllLootTablesAdded());

		this.setSuggestions(suggestions.stream().map(Object::toString).collect(Collectors.toSet()));
	}
}
