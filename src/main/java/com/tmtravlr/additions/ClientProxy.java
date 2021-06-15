package com.tmtravlr.additions;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.tmtravlr.additions.addon.blocks.BlockAddedGrass;
import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.entities.EntityAddedProjectile;
import com.tmtravlr.additions.addon.entities.renderers.RenderAddedProjectile;
import com.tmtravlr.additions.addon.items.IItemAdded;
import com.tmtravlr.additions.addon.items.ItemAddedShield;
import com.tmtravlr.additions.gui.registration.GuiFactoryRegistration;
import com.tmtravlr.additions.network.CToSMessage;
import com.tmtravlr.additions.network.PacketHandlerServer;
import com.tmtravlr.additions.util.client.AddonLoadingException;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
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
		MC.getRenderManager().entityRenderMap.put(EntityAddedProjectile.class, new RenderAddedProjectile(MC.getRenderManager(), MC.getRenderItem()));
	}
	
	@Override
	public void registerBlockRenderWithDamage(Block block, int damage, String name) {
		Item item = Item.getItemFromBlock(block);
		if (item != Items.AIR) {
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
		if (item != Items.AIR) {
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
	public void registerBlockColors(List<Block> blocks) {
		List<Block> grassBlocks = new ArrayList<>();
		
		blocks.forEach(block -> {
			if (block instanceof BlockAddedGrass) {
				grassBlocks.add(block);
			}
		});
		
		MC.getBlockColors().registerBlockColorHandler(new IBlockColor() {
			@Override
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
				if (state.getBlock() instanceof BlockAddedGrass && ((BlockAddedGrass)state.getBlock()).useBiomeColor) {
					return world != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
				}
				
                return -1;
            }
        }, blocks.toArray(new Block[0]));
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
					
					if (packFile != null && packFile.equals(addon)) {
						packIterator.remove();
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void refreshResources() {
		MC.refreshResources();
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
	public void syncPlayerInventory(PacketBuffer buff) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			int inventorySize = buff.readInt();
			EntityPlayer player = Minecraft.getMinecraft().player;
			
			try {
				for (int i = 0; i < inventorySize; i++) {
					ItemStack newStack = buff.readItemStack();
					ItemStack existingStack = player.inventory.getStackInSlot(i);
					if (newStack.getItem() != existingStack.getItem() || newStack.getMetadata() != existingStack.getMetadata() || newStack.getCount() != existingStack.getCount() || !ItemStack.areItemStackTagsEqual(newStack, existingStack)) {
						player.inventory.setInventorySlotContents(i, newStack);
					}
				}
			} catch (Exception e) {
				AdditionsMod.logger.error("Error while attempting to sync player inventory.", e);
			}
		});
	}
	
	@Override
	public RuntimeException createLoadingException(String message) {
		return new AddonLoadingException(message);
	}
}
