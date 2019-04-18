package com.tmtravlr.additions.addon.functions;

import java.util.List;

import net.minecraft.util.ResourceLocation;

/**
 * Holds info necessary to add/edit a function
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @since August 2018 
 */
public class FunctionAdded {
	public ResourceLocation id;
	public List<String> commands;
	
	public FunctionAdded(ResourceLocation id, List<String> commands) {
		this.id = id;
		this.commands = commands;
	}
}
