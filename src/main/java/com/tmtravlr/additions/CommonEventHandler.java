package com.tmtravlr.additions;

import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.tmtravlr.additions.addon.blocks.IBlockAdded;
import com.tmtravlr.additions.addon.effects.Effect;
import com.tmtravlr.additions.addon.effects.EffectCancelNormal;
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
import com.tmtravlr.additions.network.PacketHandlerServer;
import com.tmtravlr.additions.type.AdditionTypeEffect;
import com.tmtravlr.additions.type.AdditionTypeLootTable;
import com.tmtravlr.additions.type.AdditionTypeRecipe;
import com.tmtravlr.lootoverhaul.loot.ExtraLootManager.LoadLootTableExtrasEvent;

import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

@Mod.EventBusSubscriber(modid = AdditionsMod.MOD_ID)
public class CommonEventHandler {

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (ConfigLoader.replaceManagers.getBoolean(true) && event.getWorld() instanceof WorldServer) {
			AddonStructureManager.replaceStructureManager((WorldServer) event.getWorld());
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
				if (entry.getKey().applies(event.getHand(), event.getItemStack())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
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
				if (entry.getKey().applies(event.getHand(), event.getItemStack())) {
					boolean against = entry.getKey().againstBlock && !(entry.getKey().exceptReplaceable && event.getWorld().getBlockState(event.getPos()).getBlock().isReplaceable(event.getWorld(), event.getPos()));
					BlockPos pos = against ? event.getPos().offset(event.getFace()) : event.getPos();
	
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
						} else {
							if (entry.getKey().targetSelf) {
								effect.affectEntity(event.getEntityLiving(), event.getEntityLiving());
							} else {
								effect.affectBlock(event.getEntityLiving(), event.getWorld(), pos);
							}
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
	public static void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		boolean cancelEvent = false;
		
		if (!event.getItemStack().isEmpty()) {
			for (Entry<EffectCauseItemRightClickEntity, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemRightClickEntityEffects().entrySet()) {
				if (entry.getKey().applies(event.getHand(), event.getItemStack())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
						} else {
							effect.affectEntity(event.getEntityLiving(), entry.getKey().targetSelf ? event.getEntity() : event.getTarget());
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
				if (entry.getKey().applies(event.getItemStack())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
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
		
		if (cancelEvent) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {
		boolean cancelEvent = false;
		
		if (!event.getPlayer().getHeldItemMainhand().isEmpty()) {
			for (Entry<EffectCauseItemBreakBlock, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemBreakBlockEffects().entrySet()) {
				if (entry.getKey().applies(event.getPlayer().getHeldItemMainhand())) {
					for (Effect effect : entry.getValue()) {
						if (effect instanceof EffectCancelNormal) {
							cancelEvent = true;
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
		
		if (cancelEvent) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onBlockExploded(BlockEvent.HarvestDropsEvent event) {
		if (event.getDropChance() < 1 && event.getDropChance() >= 0 && event.getState().getBlock() instanceof IBlockAdded && Boolean.TRUE.equals(((IBlockAdded)event.getState().getBlock()).getDroppedFromExplosions())) {
			event.setDropChance(1.0f);
		}
	}
	
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		boolean cancelEvent = false;
		Entity cause = event.getSource().getImmediateSource();
		
		if (!AdditionTypeEffect.INSTANCE.getItemAttackEffects().isEmpty()) {
			ItemStack causeStack = cause instanceof EntityLivingBase ? ((EntityLivingBase)cause).getHeldItemMainhand() : ItemStack.EMPTY;
			
			if (!causeStack.isEmpty()) {
				for (Entry<EffectCauseItemAttack, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemAttackEffects().entrySet()) {
					if (entry.getKey().applies(causeStack)) {
						for (Effect effect : entry.getValue()) {
							if (effect instanceof EffectCancelNormal) {
								cancelEvent = true;
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
		
		if (cancelEvent) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onEntityKilled(LivingDeathEvent event) {
		boolean cancelEvent = false;
		Entity cause = event.getSource().getImmediateSource();
		
		if (!AdditionTypeEffect.INSTANCE.getItemKillEffects().isEmpty()) {
			ItemStack causeStack = cause instanceof EntityLivingBase ? ((EntityLivingBase)cause).getHeldItemMainhand() : ItemStack.EMPTY;
			
			if (!causeStack.isEmpty()) {
				for (Entry<EffectCauseItemKill, List<Effect>> entry : AdditionTypeEffect.INSTANCE.getItemKillEffects().entrySet()) {
					if (entry.getKey().applies(causeStack)) {
						for (Effect effect : entry.getValue()) {
							if (effect instanceof EffectCancelNormal) {
								cancelEvent = true;
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
