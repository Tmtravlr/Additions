package com.tmtravlr.additions;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void registerBlockRender(Block block) {
		//Do nothing on the server!
	}
	
	public void registerRenderers() {
		//Do nothing on the server!
	}

	public void registerItemRender(Item item) {
		//Do nothing on the server!		
	}
	
	public void registerItemRenderWithDamage(Item item, int damage, String name) {
		//Do nothing on the server!		
	}
	
	public void registerBlockRenderWithDamage(Block block, int damage, String name) {
		//Do nothing on the server!		
	}
	
	public void registerItemRenderDefinition(Item item) {
		//Do nothing on the server!		
	}
	
	public void registerVariants(Item item, ResourceLocation... args) {
		//Do nothing on the server!
	}
	
	public void registerItemColors(Item[] items) {
		//Do nothing on the server!
	}

	public void registerAsResourcePack(File addon) {
		//Do nothing on the server!
	}
	
	public void refreshResources() {
		//Do nothing on the server!
	}

	public void throwAddonLoadingException(String messageKey, Object ... args) {
		//Do nothing on the server!
	}

}
