package com.tmtravlr.additions;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectCancelNormal;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockBroken;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockContact;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockDigging;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockPlaced;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockRandom;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseBlockRightClicked;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityAttacked;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityDeath;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityRightClicked;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntitySpawned;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseEntityUpdate;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemAttack;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemBreakBlock;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemDiggingBlock;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemEquipped;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemInHand;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemInInventory;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemKill;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemLeftClick;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClick;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClickBlock;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemRightClickEntity;
import com.tmtravlr.additions.addon.effects.cause.EffectCauseItemUsing;
import com.tmtravlr.additions.addon.entities.ai.RangedAttackReplacer;
import com.tmtravlr.additions.addon.structures.AddonStructureManager;
import com.tmtravlr.additions.network.CToSMessage;
import com.tmtravlr.additions.network.PacketHandlerClient;
import com.tmtravlr.additions.network.PacketHandlerServer;
import com.tmtravlr.additions.network.SToCMessage;
import com.tmtravlr.additions.type.AdditionTypeEffect;
import com.tmtravlr.additions.type.AdditionTypeLootTable;
import com.tmtravlr.additions.type.AdditionTypeRecipe;
import com.tmtravlr.additions.util.ProblemNotifier;
import com.tmtravlr.lootoverhaul.loot.ExtraLootManager.LoadLootTableExtrasEvent;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = AdditionsMod.MOD_ID)
public class CommonEventHandler {
	
	private static int updateLCG = (new Random()).nextInt();

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (ConfigLoader.replaceManagers.getBoolean(true) && event.getWorld() instanceof WorldServer) {
			AddonStructureManager.replaceStructureManager((WorldServer) event.getWorld());
		}
	}
	
	@SubscribeEvent
	public static void onPlayerJoin(PlayerLoggedInEvent event) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance() != null) {
			ProblemNotifier.showProblemsIngame(FMLCommonHandler.instance().getMinecraftServerInstance());
		}
	}
	
	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event) {
		if (!event.getEntityLiving().getEntityWorld().isRemote && event.getEntityLiving().ticksExisted % 11 == 0 && event.getEntityLiving() instanceof IRangedAttackMob && event.getEntityLiving() instanceof EntityLiving && !event.getEntityLiving().isDead) {
			RangedAttackReplacer.checkIfAINeedsReplacing((EntityLiving & IRangedAttackMob) event.getEntityLiving(), event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND));
		}
		
		if (!(event.getEntityLiving().getHeldItemMainhand().isEmpty() && event.getEntityLiving().getHeldItemOffhand().isEmpty())) {
			for (Entry<EffectCauseItemInHand, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getInHandEffects().entrySet()) {
				if (entry.getKey().applies(event.getEntityLiving())) {
					ItemStack relevantStack = entry.getKey().getRelevantStack(event.getEntityLiving());
					
					for (Effect effect : entry.getValue()) {
						effect.affectEntity(event.getEntityLiving(), event.getEntityLiving());
						if (!relevantStack.isEmpty()) {
							effect.affectItemStack(event.getEntityLiving(), event.getEntityLiving().getEntityWorld(), relevantStack);
						}
					}
				}
			}
		}
		
		for (Entry<EffectCauseItemInInventory, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getInInventoryEffects().entrySet()) {
			if (entry.getKey().applies(event.getEntityLiving())) {
				ItemStack relevantStack = entry.getKey().getRelevantStack(event.getEntityLiving());
				
				for (Effect effect : entry.getValue()) {
					effect.affectEntity(event.getEntityLiving(), event.getEntityLiving());
					if (!relevantStack.isEmpty()) {
						effect.affectItemStack(event.getEntityLiving(), event.getEntityLiving().getEntityWorld(), relevantStack);
					}
				}
			}
		}
		
		for (Entry<EffectCauseItemEquipped, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getEquippedEffects().entrySet()) {
			if (entry.getKey().applies(event.getEntityLiving())) {
				NonNullList<ItemStack> relevantStacks = entry.getKey().getRelevantStacks(event.getEntityLiving());
				
				for (Effect effect : entry.getValue()) {
					effect.affectEntity(event.getEntityLiving(), event.getEntityLiving());
					for (ItemStack relevantStack : relevantStacks) {
						effect.affectItemStack(event.getEntityLiving(), event.getEntityLiving().getEntityWorld(), relevantStack);
					}
				}
			}
		}
		
		if (!event.getEntityLiving().getActiveItemStack().isEmpty()) {
			for (Entry<EffectCauseItemUsing, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getUsingEffects().entrySet()) {
				if (entry.getKey().applies(event.getEntityLiving())) {
					for (Effect effect : entry.getValue()) {
						effect.affectEntity(event.getEntityLiving(), event.getEntityLiving());
						effect.affectItemStack(event.getEntityLiving(), event.getEntityLiving().getEntityWorld(), event.getEntityLiving().getActiveItemStack());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
		boolean cancelEvent = false;
		
		if (!event.getItemStack().isEmpty()) {
			for (Entry<EffectCauseItemRightClick, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemRightClickEffects().entrySet()) {
				if (!cancelEvent && entry.getKey().applies(event.getHand(), event.getItemStack())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
							break;
						} else {
							effect.affectEntity(event.getEntityLiving(), event.getEntityLiving());
							effect.affectItemStack(event.getEntityLiving(), event.getWorld(), event.getItemStack());
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);
			event.setCancellationResult(EnumActionResult.SUCCESS);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		boolean cancelEvent = false;
		
		if (!event.getItemStack().isEmpty()) {
			for (Entry<EffectCauseItemRightClickBlock, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemRightClickBlockEffects().entrySet()) {
				if (!cancelEvent && entry.getKey().applies(event.getHand(), event.getItemStack())) {
					boolean against = entry.getKey().againstBlock && !(entry.getKey().exceptReplaceable && event.getWorld().getBlockState(event.getPos()).getBlock().isReplaceable(event.getWorld(), event.getPos()));
					BlockPos pos = against ? event.getPos().offset(event.getFace()) : event.getPos();
	
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
							break;
						} else {
							if (entry.getKey().targetSelf) {
								effect.affectEntity(event.getEntity(), event.getEntity());
							} else {
								effect.affectBlock(event.getEntity(), event.getWorld(), pos);
							}
							effect.affectItemStack(event.getEntity(), event.getWorld(), event.getItemStack());
						}
					}
				}
			}
		}
		
		if (!cancelEvent && event.getHand() == EnumHand.MAIN_HAND) {
			for (Entry<EffectCauseBlockRightClicked, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getBlockRightClickedEffects().entrySet()) {
				if (!cancelEvent) {
					IBlockState blockState = event.getWorld().getBlockState(event.getPos());
					TileEntity te = event.getWorld().getTileEntity(event.getPos());
					NBTTagCompound blockTag = te == null ? null : te.writeToNBT(new NBTTagCompound());
					
					if (entry.getKey().applies(blockState, blockTag)) {
						for (Effect effect : entry.getValue()) {
							if (effect instanceof EffectCancelNormal) {
								cancelEvent = true;
								break;
							} else {
								if (entry.getKey().targetSelf) {
									effect.affectEntity(event.getEntityPlayer(), event.getEntityPlayer());
								} else {
									effect.affectBlock(event.getEntityPlayer(), event.getWorld(), event.getPos());
								}
								
								if (!event.getItemStack().isEmpty()) {
									effect.affectItemStack(event.getEntity(), event.getWorld(), event.getItemStack());
								}
							}
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);
			event.setCancellationResult(EnumActionResult.SUCCESS);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		boolean cancelEvent = false;
		
		if (!event.getItemStack().isEmpty()) {
			for (Entry<EffectCauseItemRightClickEntity, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemRightClickEntityEffects().entrySet()) {
				if (!cancelEvent && entry.getKey().applies(event.getHand(), event.getItemStack())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
							break;
						} else {
							effect.affectEntity(event.getEntity(), entry.getKey().targetSelf ? event.getEntity() : event.getTarget());
							effect.affectItemStack(event.getEntity(), event.getWorld(), event.getItemStack());
						}
					}
				}
			}
		}
		
		if (!cancelEvent && event.getHand() == EnumHand.MAIN_HAND) {
			for (Entry<EffectCauseEntityRightClicked, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getEntityRightClickedEffects().entrySet()) {
				if (!cancelEvent) {
					if (entry.getKey().applies(event.getTarget())) {
						for (Effect effect : entry.getValue()) {
							if (effect instanceof EffectCancelNormal) {
								cancelEvent = true;
								break;
							} else {
								effect.affectEntity(event.getEntity(), entry.getKey().targetSelf ? event.getEntity() : event.getTarget());
								
								if (!event.getItemStack().isEmpty()) {
									effect.affectItemStack(event.getEntity(), event.getWorld(), event.getItemStack());
								}
							}
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);
			event.setCancellationResult(EnumActionResult.SUCCESS);
		}
	}
	
	//Only fires client-side
	@SubscribeEvent
	public static void onPlayerLeftClickAir(PlayerInteractEvent.LeftClickEmpty event) {
		if (!event.getItemStack().isEmpty() && !AdditionTypeEffect.INSTANCE.getItemLeftClickEffects().isEmpty()) {
			sendLeftClickItemToServer(event.getEntityPlayer());
			handleLeftClickItemEvent(event.getEntityPlayer(), event.getItemStack());
		}
	}
	
	private static void sendLeftClickItemToServer(Entity player) {
		UUID playerId = player.getUniqueID();
		
		PacketBuffer buff = new PacketBuffer(Unpooled.buffer());
		buff.writeInt(PacketHandlerServer.LEFT_CLICK_ITEM);
		buff.writeLong(playerId.getMostSignificantBits());
		buff.writeLong(playerId.getLeastSignificantBits());
		
		AdditionsMod.networkWrapper.sendToServer(new CToSMessage(buff));
	}
	
	public static void handleLeftClickItemEvent(Entity player, ItemStack stack) {
		for (Entry<EffectCauseItemLeftClick, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemLeftClickEffects().entrySet()) {
			if (entry.getKey().applies(stack)) {
				for (Effect effect : entry.getValue()) {
					effect.affectEntity(player, player);
					effect.affectItemStack(player, player.getEntityWorld(), stack);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		boolean cancelEvent = false;
		
		if (!event.getItemStack().isEmpty()) {
			for (Entry<EffectCauseItemDiggingBlock, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemDiggingBlockEffects().entrySet()) {
				if (!cancelEvent && entry.getKey().applies(event.getItemStack())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
							break;
						} else {
							if (entry.getKey().targetSelf) {
								effect.affectEntity(event.getEntityLiving(), event.getEntityLiving());
							} else {
								effect.affectBlock(event.getEntityLiving(), event.getWorld(), event.getPos());
							}
							effect.affectItemStack(event.getEntityLiving(), event.getWorld(), event.getItemStack());
						}
					}
				}
			}
		}
		
		if (!cancelEvent) {
			for (Entry<EffectCauseBlockDigging, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getBlockDiggingEffects().entrySet()) {
				if (!cancelEvent) {
					IBlockState blockState = event.getWorld().getBlockState(event.getPos());
					TileEntity te = event.getWorld().getTileEntity(event.getPos());
					NBTTagCompound blockTag = te == null ? null : te.writeToNBT(new NBTTagCompound());
					
					if (entry.getKey().applies(blockState, blockTag)) {
						for (Effect effect : entry.getValue()) {
							if (effect instanceof EffectCancelNormal) {
								cancelEvent = true;
								break;
							} else {
								if (entry.getKey().targetSelf) {
									effect.affectEntity(event.getEntityPlayer(), event.getEntityPlayer());
								} else {
									effect.affectBlock(event.getEntityPlayer(), event.getWorld(), event.getPos());
								}
								
								if (!event.getItemStack().isEmpty()) {
									effect.affectItemStack(event.getEntityLiving(), event.getWorld(), event.getItemStack());
								}
							}
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
		boolean cancelEvent = false;
		
		if (!event.getPlayer().getHeldItemMainhand().isEmpty()) {
			for (Entry<EffectCauseItemBreakBlock, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemBreakBlockEffects().entrySet()) {
				if (!cancelEvent && entry.getKey().applies(event.getPlayer().getHeldItemMainhand())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
							break;
						} else {
							if (entry.getKey().targetSelf) {
								effect.affectEntity(event.getPlayer(), event.getPlayer());
							} else {
								effect.affectBlock(event.getPlayer(), event.getWorld(), event.getPos());
							}
							effect.affectItemStack(event.getPlayer(), event.getWorld(), event.getPlayer().getHeldItemMainhand());
						}
					}
				}
			}
		}
		
		if (!cancelEvent) {
			for (Entry<EffectCauseBlockBroken, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getBlockBrokenEffects().entrySet()) {
				if (!cancelEvent) {
					IBlockState blockState = event.getWorld().getBlockState(event.getPos());
					TileEntity te = event.getWorld().getTileEntity(event.getPos());
					NBTTagCompound blockTag = te == null ? null : te.writeToNBT(new NBTTagCompound());
					
					if (entry.getKey().applies(blockState, blockTag)) {
						for (Effect effect : entry.getValue()) {
							if (effect instanceof EffectCancelNormal) {
								cancelEvent = true;
								break;
							} else {
								if (entry.getKey().targetSelf) {
									effect.affectEntity(event.getPlayer(), event.getPlayer());
								} else {
									effect.affectBlock(event.getPlayer(), event.getWorld(), event.getPos());
								}
								
								if (!event.getPlayer().getHeldItemMainhand().isEmpty()) {
									effect.affectItemStack(event.getPlayer(), event.getWorld(), event.getPlayer().getHeldItemMainhand());
								}
							}
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerPlaceBlock(BlockEvent.PlaceEvent event) {
		boolean cancelEvent = false;
		
		for (Entry<EffectCauseBlockPlaced, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getBlockPlacedEffects().entrySet()) {
			if (!cancelEvent) {
				IBlockState blockState = event.getWorld().getBlockState(event.getPos());
				TileEntity te = event.getWorld().getTileEntity(event.getPos());
				NBTTagCompound blockTag = te == null ? null : te.writeToNBT(new NBTTagCompound());
				
				if (entry.getKey().applies(blockState, blockTag)) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
							break;
						} else {
							if (entry.getKey().targetSelf) {
								effect.affectEntity(event.getPlayer(), event.getPlayer());
							} else {
								effect.affectBlock(event.getPlayer(), event.getWorld(), event.getPos());
							}
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);

			// Sync inventory, since this event only runs on the server side
			sendInventoryToClient(event.getPlayer());
		}
	}
	
	private static void sendInventoryToClient(EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			int inventorySize = player.inventory.getSizeInventory();
			
			PacketBuffer buff = new PacketBuffer(Unpooled.buffer());
			buff.writeInt(PacketHandlerClient.SYNC_INVENTORY);
			buff.writeInt(inventorySize);
			
			for (int i = 0; i < inventorySize; i++) {
				buff.writeItemStack(player.inventory.getStackInSlot(i));
			}
			
			AdditionsMod.networkWrapper.sendTo(new SToCMessage(buff), (EntityPlayerMP) player);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onBlockExploded(BlockEvent.HarvestDropsEvent event) {
		if (event.getDropChance() < 1 && event.getDropChance() >= 0 && event.getState().getBlock() instanceof IBlockAdded && Boolean.TRUE.equals(((IBlockAdded)event.getState().getBlock()).getDroppedFromExplosions())) {
			event.setDropChance(1.0f);
		}
	}
	
	@SubscribeEvent
	public static void onEntitySpawned(LivingSpawnEvent.SpecialSpawn event) {
		boolean cancelEvent = false;
		
		if (!AdditionTypeEffect.INSTANCE.getEntitySpawnedEffects().isEmpty()) {
			for (Entry<EffectCauseEntitySpawned, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getEntitySpawnedEffects().entrySet()) {
				if (!cancelEvent && entry.getKey().applies(event.getEntity())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
							break;
						} else {
							effect.affectEntity(event.getEntity(), event.getEntity());
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRespawned(PlayerRespawnEvent event) {
		if (!AdditionTypeEffect.INSTANCE.getEntitySpawnedEffects().isEmpty()) {
			for (Entry<EffectCauseEntitySpawned, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getEntitySpawnedEffects().entrySet()) {
				if (entry.getKey().applies(event.player)) {
					for (Effect effect : entry.getValue()) {
						effect.affectEntity(event.player, event.player);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		boolean cancelEvent = false;
		Entity cause = event.getSource().getImmediateSource();
		ItemStack causeStack = cause instanceof EntityLivingBase ? ((EntityLivingBase)cause).getHeldItemMainhand() : ItemStack.EMPTY;
		
		if (!AdditionTypeEffect.INSTANCE.getItemAttackEffects().isEmpty()) {
			if (!causeStack.isEmpty()) {
				for (Entry<EffectCauseItemAttack, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemAttackEffects().entrySet()) {
					if (!cancelEvent && entry.getKey().applies(causeStack)) {
						for (Effect effect : entry.getValue()) {
							if (effect instanceof EffectCancelNormal) {
								cancelEvent = true;
								break;
							} else {
								if (!entry.getKey().targetSelf) {
									effect.affectEntity(cause, event.getEntityLiving());
								} else if (cause != null) {
									effect.affectEntity(cause, cause);
								}
								effect.affectItemStack(cause, event.getEntityLiving().getEntityWorld(), causeStack);
							}
						}
					}
				}
			}
		}

		if (!cancelEvent && !AdditionTypeEffect.INSTANCE.getEntityAttackedEffects().isEmpty()) {
			for (Entry<EffectCauseEntityAttacked, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getEntityAttackedEffects().entrySet()) {
				if (!cancelEvent && entry.getKey().applies(cause, event.getEntity(), event.getSource())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
							break;
						} else {
							if (!entry.getKey().targetSelf) {
								effect.affectEntity(cause, event.getEntityLiving());
							} else if (cause != null) {
								effect.affectEntity(cause, cause);
							}
							
							if (!causeStack.isEmpty()) {
								effect.affectItemStack(cause, event.getEntityLiving().getEntityWorld(), causeStack);
							}
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onEntityKilled(LivingDeathEvent event) {
		boolean cancelEvent = false;
		Entity cause = event.getSource().getImmediateSource();
		ItemStack causeStack = cause instanceof EntityLivingBase ? ((EntityLivingBase)cause).getHeldItemMainhand() : ItemStack.EMPTY;
		
		if (!AdditionTypeEffect.INSTANCE.getItemKillEffects().isEmpty()) {
			if (!causeStack.isEmpty()) {
				for (Entry<EffectCauseItemKill, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemKillEffects().entrySet()) {
					if (!cancelEvent && entry.getKey().applies(causeStack)) {
						for (Effect effect : entry.getValue()) {
							if (effect instanceof EffectCancelNormal) {
								cancelEvent = true;
								break;
							} else {
								if (!entry.getKey().targetSelf) {
									effect.affectEntity(cause, event.getEntityLiving());
								} else if (cause != null) {
									effect.affectEntity(cause, cause);
								}
								effect.affectItemStack(cause, event.getEntityLiving().getEntityWorld(), causeStack);
							}
						}
					}
				}
			}
		}
		
		if (!cancelEvent && !AdditionTypeEffect.INSTANCE.getEntityDeathEffects().isEmpty()) {
			for (Entry<EffectCauseEntityDeath, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getEntityDeathEffects().entrySet()) {
				if (!cancelEvent && entry.getKey().applies(cause, event.getEntity(), event.getSource())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
							break;
						} else {
							if (!entry.getKey().targetSelf) {
								effect.affectEntity(cause, event.getEntityLiving());
							} else if (cause != null) {
								effect.affectEntity(cause, cause);
							}
							
							if (!causeStack.isEmpty()) {
								effect.affectItemStack(cause, event.getEntityLiving().getEntityWorld(), causeStack);
							}
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onEntityUpdate(LivingUpdateEvent event) {
		boolean cancelEvent = false;
		
		for (Entry<EffectCauseBlockContact, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getBlockContactEffects().entrySet()) {
			if (!cancelEvent) {
				if (entry.getKey().contactType != EffectCauseBlockContact.ContactType.ON) {
					IBlockState blockState = event.getEntity().getEntityWorld().getBlockState(event.getEntity().getPosition());
					TileEntity te = event.getEntity().getEntityWorld().getTileEntity(event.getEntity().getPosition());
					NBTTagCompound blockTag = te == null ? null : te.writeToNBT(new NBTTagCompound());
					
					if (entry.getKey().applies(blockState, blockTag)) {
						for (Effect effect : entry.getValue()) {
							if (entry.getKey().targetSelf) {
								effect.affectEntity(event.getEntity(), event.getEntity());
							} else {
								effect.affectBlock(event.getEntity(), event.getEntity().getEntityWorld(), event.getEntity().getPosition());
							}
						}
					}
				}
				
				if (event.getEntity().onGround && entry.getKey().contactType != EffectCauseBlockContact.ContactType.INSIDE) {
					IBlockState blockState = event.getEntity().getEntityWorld().getBlockState(event.getEntity().getPosition().down());
					TileEntity te = event.getEntity().getEntityWorld().getTileEntity(event.getEntity().getPosition().down());
					NBTTagCompound blockTag = te == null ? null : te.writeToNBT(new NBTTagCompound());
					
					if (entry.getKey().applies(blockState, blockTag)) {
						for (Effect effect : entry.getValue()) {
							if (entry.getKey().targetSelf) {
								effect.affectEntity(event.getEntity(), event.getEntity());
							} else {
								effect.affectBlock(event.getEntity(), event.getEntity().getEntityWorld(), event.getEntity().getPosition());
							}
						}
					}
				}
			}
		}
		
		if (!cancelEvent) {
			for (Entry<EffectCauseEntityUpdate, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getEntityUpdateEffects().entrySet()) {
				if (!cancelEvent) {				
					if (entry.getKey().applies(event.getEntity())) {
						for (Effect effect : entry.getValue()) {
							if (effect instanceof EffectCancelNormal) {
								cancelEvent = true;
								break;
							} else {
								effect.affectEntity(event.getEntity(), event.getEntity());
							}
						}
					}
				}
			}
		}
		
		if (cancelEvent) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			RangedAttackReplacer.cleanupDeadMobs();
		}
	}
	
	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.side == Side.SERVER) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
				int tickSpeed = event.world.getGameRules().getInt("randomTickSpeed");
				
				if (tickSpeed > 0 && !AdditionTypeEffect.INSTANCE.getBlockRandomEffects().isEmpty()) {
					WorldServer worldServer = (WorldServer) event.world;
					
					Iterator<Chunk> iterator = worldServer.getPersistentChunkIterable(worldServer.getPlayerChunkMap().getChunkIterator());
					while (iterator.hasNext()) {
		                Chunk chunk = iterator.next();
		                int chunkX = chunk.x * 16;
		                int chunkZ = chunk.z * 16;
		                
		                for (ExtendedBlockStorage extendedblockstorage : chunk.getBlockStorageArray()) {
		                    if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && !extendedblockstorage.isEmpty()) {
		                        for (int i = 0; i < tickSpeed; ++i) {
		                            updateLCG = updateLCG * 3 + 1013904223;
		                            int randPos = updateLCG >> 2;
		                            int blockX = randPos & 15;
		                            int blockZ = randPos >> 8 & 15;
		                            int blockY = randPos >> 16 & 15;
		                            IBlockState blockState = extendedblockstorage.get(blockX, blockY, blockZ);
		                            Block block = blockState.getBlock();
		
		                            if (block != Blocks.AIR) {
		                            	BlockPos pos = new BlockPos(blockX + chunkX, blockY + extendedblockstorage.getYLocation(), blockZ + chunkZ);
		                            	TileEntity te = event.world.getTileEntity(pos);
		                				NBTTagCompound blockTag = te == null ? null : te.writeToNBT(new NBTTagCompound());
		                            	
		                            	for (Entry<EffectCauseBlockRandom, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getBlockRandomEffects().entrySet()) {
		                            		if (entry.getKey().applies(blockState, blockTag)) {
		                						for (Effect effect : entry.getValue()) {
		                							if (!(effect instanceof EffectCancelNormal)) {
		                								effect.affectBlock(null, event.world, pos);
		                							}
		                						}
		                					}
		                				}
		                            }
		                        }
		                    }
		                }
					}
				}
			});
		}
	}
	
	@SubscribeEvent
	public static void onInteractWithCauldron(PlayerInteractEvent.RightClickBlock event) {
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		ItemStack stack = event.getItemStack();

		if (!event.getWorld().isRemote && state.getBlock() == Blocks.CAULDRON && state.getValue(BlockCauldron.LEVEL).intValue() > 0 && !stack.isEmpty() && stack.hasTagCompound()) {
			AdditionTypeRecipe.cauldronWashingRecipes.stream().filter(recipe -> stack.getItem() == recipe.itemToDye).findFirst().ifPresent(washRecipe -> {
				if (washRecipe.hasColor(stack)) {
					
		            ItemStack washedStack = stack.copy();
		            washedStack.setCount(1);
		            washRecipe.removeColor(washedStack);
		
		            if (!event.getEntityPlayer().capabilities.isCreativeMode) {
		                stack.shrink(1);
		                Blocks.CAULDRON.setWaterLevel(event.getWorld(), event.getPos(), state, state.getValue(BlockCauldron.LEVEL).intValue() - 1);
		            }
		
		            if (stack.isEmpty()) {
		                event.getEntityPlayer().setHeldItem(event.getHand(), washedStack);
		            } else if (!event.getEntityPlayer().inventory.addItemStackToInventory(washedStack)) {
		            	event.getEntityPlayer().dropItem(washedStack, false);
		            } else if (event.getEntityPlayer() instanceof EntityPlayerMP) {
		                ((EntityPlayerMP)event.getEntityPlayer()).sendContainerToPlayer(event.getEntityPlayer().inventoryContainer);
		            }
		            
		            event.getWorld().playSound(null, event.getPos(), SoundEventLoader.BLOCK_CAULDRON_WASH, SoundCategory.BLOCKS, 1.0f, 1.0f);
		            event.setCanceled(true);
		            event.setCancellationResult(EnumActionResult.SUCCESS);
				}
			});
		}
	}
	
	@SubscribeEvent
	public static void onLootTableExtrasLoad(LoadLootTableExtrasEvent event) {
		AdditionTypeLootTable.INSTANCE.getLootTableExtras(event.getLootTableLocation(), event.getLootTableManager()).forEach(event::addExtraLootTable);
	}
}
