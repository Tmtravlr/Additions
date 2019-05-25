package com.tmtravlr.additions;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.entities.EntityAddedProjectile;
import com.tmtravlr.additions.addon.entities.renderers.RenderAddedProjectile;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedShield;
import com.tmtravlr.additions.gui.GuiFactoryRegistration;
import com.tmtravlr.additions.network.CToSMessage;
import com.tmtravlr.additions.network.PacketHandlerServer;
import com.tmtravlr.additions.util.client.AddonLoadingException;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ClientProxy extends CommonProxy {

	private static final Minecraft MC = Minecraft.getMinecraft();
	
	private List<IResourcePack> defaultResourcePacks = null;
	
	@Override
	public void registerGuiFactories() {
		GuiFactoryRegistration.registerGuiFactories();
	}
	
	@Override
	public void registerEntityRenderers() {
		Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityAddedProjectile.class, new RenderAddedProjectile(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()));
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
		MC.getRenderItem().getItemModelMesher().register(item, damage, new ModelResourceLocation(new ResourceLocation(AdditionsMod.MOD_ID, name), "inventory"));
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
		MC.getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(new ResourceLocation(AdditionsMod.MOD_ID, itemName), "inventory"));
	}
	
	@Override
	public void registerItemRenderDefinition(Item item) {
		MC.getRenderItem().getItemModelMesher().register(item, new ItemMeshDefinition() {
			@Override
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
		MC.getItemColors().registerItemColorHandler(new IItemColor() {
            @Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
            	if (stack.getItem() instanceof IItemAdded && tintIndex == 1) {
	                return ((IItemAdded)stack.getItem()).getColor(stack);
            	}
            	
            	return -1;
            }
        }, items);
	}
	
	@Override
	public void ignoreLiquidLevel(IBlockAdded block) {
		ModelLoader.setCustomStateMapper(block.getAsBlock(), (new StateMap.Builder()).ignore(new IProperty[] {BlockLiquid.LEVEL}).build());
	}
	
	@Override
	public void registerAsResourcePack(File addon) {
		
		if (this.defaultResourcePacks == null) {
			this.getDefaultResourcePacks();
		}
		
		this.defaultResourcePacks.add(addon.isDirectory() ? new FolderResourcePack(addon) : new FileResourcePack(addon));
	}
	
	@Override
	public void unRegisterResourcePack(File addon) {
		if (this.defaultResourcePacks != null) {
			Iterator<IResourcePack> packIterator = this.defaultResourcePacks.iterator();
			
			while (packIterator.hasNext()) {
				IResourcePack resourcePack = packIterator.next();
				if (resourcePack instanceof AbstractResourcePack) {
					File packFile = ObfuscationReflectionHelper.getPrivateValue(AbstractResourcePack.class, (AbstractResourcePack)resourcePack, "field_110597_b", "resourcePackFile");
					
					if (packFile.equals(addon)) {
						packIterator.remove();
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void refreshResources() {
		Minecraft.getMinecraft().refreshResources();
	}
	
	private void getDefaultResourcePacks() {
		try {
			this.defaultResourcePacks = ObfuscationReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "resourcePackList");
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
	public void checkForShieldBash() {
		if (MC.gameSettings.keyBindAttack.isKeyDown() && MC.player != null && !MC.player.isSpectator() && ItemAddedShield.canEntityBashAttack(MC.player)) {
			
			Entity attacked = null;
			
			if (MC.objectMouseOver != null && MC.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
				attacked = MC.objectMouseOver.entityHit;
			}
			
			ItemAddedShield.doBashAttack(MC.player, attacked);
			
			UUID playerId = MC.player.getUniqueID();
			UUID attackedId = attacked != null ? attacked.getUniqueID() : null;
			
			PacketBuffer buff = new PacketBuffer(Unpooled.buffer());
			buff.writeInt(PacketHandlerServer.SHIELD_BASH);
			buff.writeLong(playerId.getMostSignificantBits());
			buff.writeLong(playerId.getLeastSignificantBits());
			buff.writeLong(attackedId == null ? 0L : attackedId.getMostSignificantBits());
			buff.writeLong(attackedId == null ? 0L : attackedId.getLeastSignificantBits());
			
			AdditionsMod.networkWrapper.sendToServer(new CToSMessage(buff));
		}
	}
	
	@Override
	public RuntimeException createLoadingException(String message) {
		return new AddonLoadingException(message);
	}
}
