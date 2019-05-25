package com.tmtravlr.additions;

import java.io.File;

import com.tmtravlr.additions.addon.blocks.IBlockAdded;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void registerGuiFactories() {
		//Do nothing on the server!
	}
	
	public void registerEntityRenderers() {
		//Do nothing on the server!
	}
	
	public void registerBlockRender(Block block) {
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
	
	public void ignoreLiquidLevel(IBlockAdded block) {
		//Do nothing on the server!
	}

	public void registerAsResourcePack(File addon) {
		//Do nothing on the server!
	}

	public void unRegisterResourcePack(File addon) {
		//Do nothing on the server!
	}
	
	public void refreshResources() {
		//Do nothing on the server!
	}
	
	public void checkForShieldBash() {
		//Do nothing on the server!
	}
	
	public RuntimeException createLoadingException(String message) {
		return new RuntimeException(message);
	}

}
