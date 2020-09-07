package com.tmtravlr.additions.addon.functions;

import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Holds info necessary to add/edit a function
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2018
 */
public class FunctionAdded {
	public ResourceLocation id;
	public List<String> commands;
	
	public FunctionAdded(ResourceLocation id, List<String> commands) {
		this.id = id;
		this.commands = commands;
	}
}
