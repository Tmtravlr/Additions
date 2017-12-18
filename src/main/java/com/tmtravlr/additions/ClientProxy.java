package com.tmtravlr.additions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.addon.Addon;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedManager;
import com.tmtravlr.additions.addon.items.ItemAddedSimple;
import com.tmtravlr.additions.gui.GuiFactoryRegistration;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButton;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonCreativeTab;
import com.tmtravlr.additions.gui.type.button.GuiAdditionTypeButtonItem;
import com.tmtravlr.additions.gui.type.button.IAdditionTypeGuiFactory;
import com.tmtravlr.additions.gui.view.GuiView;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;
import com.tmtravlr.additions.gui.view.edit.item.GuiEditItemSimple;
import com.tmtravlr.additions.gui.view.edit.item.IGuiEditItemFactory;
import com.tmtravlr.additions.type.AdditionTypeManager;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ClientProxy extends CommonProxy {
	
	private List defaultResourcePacks = null;
	private AddonLoadingException loadingException;
	
	@Override
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		super.registerEventHandlers();
	}
	
	@Override
	public void registerGuiFactories() {
		GuiFactoryRegistration.registerGuiFactories();
	}
	
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
		String itemName = getItemName(item.getUnlocalizedName());
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(new ResourceLocation(AdditionsMod.MOD_ID, itemName), "inventory"));
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
	
	@Override
	public void throwAddonLoadingException(String messageKey, Object ... args) {
		this.loadingException = new AddonLoadingException(I18n.format(messageKey, args));
		ReflectionHelper.setPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), this.loadingException, "customError");
	}
	
	@Override
	public boolean checkForLoadingException() {
		if (this.loadingException != null) {
			return true;
		}
		return false;
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

	private class AddonLoadingException extends CustomModLoadingErrorDisplayException {
		private String message;
		
		public AddonLoadingException(String message) {
			this.message = message;
		}
		
		@Override
		public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {}

		@Override
		public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime) {
			errorScreen.drawDefaultBackground();
			
			int middleX = errorScreen.width / 2;
			int middleY = errorScreen.height / 2;
			
			errorScreen.drawCenteredString(fontRenderer, AdditionsMod.MOD_NAME, middleX, middleY - 10, 0xbbbbbb);
			
			int messageWidth = fontRenderer.getStringWidth(message);
			fontRenderer.drawSplitString(message, (messageWidth > errorScreen.width - 80) ? 40 : middleX - messageWidth / 2, middleY + 10, errorScreen.width - 80, 0xffffff);
		}
	}
}
