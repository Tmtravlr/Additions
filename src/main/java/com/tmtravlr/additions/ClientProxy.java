package com.tmtravlr.additions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.items.IItemAdded;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ClientProxy extends CommonProxy {
	
	private List defaultResourcePacks = null;
	
	@Override
	public void registerBlockRenderWithDamage(Block block, int damage, String name) {
		Item item = Item.getItemFromBlock(block);
		if(item != null) {
			registerItemRenderWithDamage(item, damage, name);
		}
	}
	
	@Override
	public void registerItemRenderWithDamage(Item item, int damage, String name) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, damage, new ModelResourceLocation(new ResourceLocation(AdditionsMod.MOD_ID, name), "inventory"));
	}
	
	@Override
	public void registerBlockRender(Block block) {
		Item item = Item.getItemFromBlock(block);
		if(item != null) {
			registerItemRender(Item.getItemFromBlock(block));
		}
	}
	
	@Override
	public void registerItemRender(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(new ResourceLocation(AdditionsMod.MOD_ID, getItemName(item.getUnlocalizedName())), "inventory"));
	}
	
	@Override
	public void registerItemRenderDefinition(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, new ItemMeshDefinition() {
			public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(new ResourceLocation(AdditionsMod.MOD_ID, getItemName(stack.getItem().getUnlocalizedName())), "inventory");
            }
		});
	}
	
	@Override
	public void registerVariants(Item item, ResourceLocation... args) {
		ModelBakery.registerItemVariants(item, args);
	}
	
	@Override
	public void registerItemColors(Item[] items) {
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                return tintIndex == 0 ? -1 : ((IItemAdded)stack.getItem()).getColor(stack);
            }
        }, items);
	}
	
	//Attempts to register an "addon" as a resource pack
	@Override
	public void registerAsResourcePack(File addon) {
		
		if(this.defaultResourcePacks == null) {
			getDefaultResourcePacks();
		}
		
		this.defaultResourcePacks.add(addon.isDirectory() ? new FolderResourcePack(addon) : new FileResourcePack(addon));
	}
	
	@Override
	public void refreshResources() {
		Minecraft.getMinecraft().refreshResources();
	}
	
	private void getDefaultResourcePacks() {
		try {
			this.defaultResourcePacks = (List) ObfuscationReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), new String[]{"resourcePackList"});
		}
		catch(Exception e) {
			AdditionsMod.logger.warn("Caught exception while trying to load resource pack list. =( The addon resource packs aren't going to load!");
			this.defaultResourcePacks = new ArrayList();
		}
	}
	
	private String getItemName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf(".")+1);
	}
	
	@Override
	public void throwAddonLoadingException(String messageKey, Object ... args) {
		
		final String message = I18n.format("gui.additions.loading.modNotFound", args);
		
		throw new CustomModLoadingErrorDisplayException() {
			
			@Override
			public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {}

			@Override
			public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime) {
				errorScreen.drawDefaultBackground();
				errorScreen.drawCenteredString(fontRenderer, message, errorScreen.width / 2, errorScreen.height / 2, 0xffffff);
			}
			
		};
	}

}
