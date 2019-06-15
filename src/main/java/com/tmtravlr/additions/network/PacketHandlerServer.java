package com.tmtravlr.additions.network;

import java.util.UUID;

import com.tmtravlr.additions.CommonEventHandler;
import com.tmtravlr.additions.addon.items.ItemAddedShield;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Tmtravlr 
 */
public class PacketHandlerServer implements IMessageHandler<CToSMessage,IMessage> {

	//Types of packets
	public static final int SHIELD_BASH = 0;
	public static final int LEFT_CLICK_ITEM = 1;

	/**
	 * Handles Server Side Packets. Only returns null.
	 */
	@Override
	public IMessage onMessage(CToSMessage packet, MessageContext context) {
		
		PacketBuffer buff = new PacketBuffer(Unpooled.wrappedBuffer(packet.getData()));

		int type = buff.readInt();
		
		switch(type) {
		case SHIELD_BASH: {
			UUID attackerId = new UUID(buff.readLong(), buff.readLong());
			UUID attackedId = new UUID(buff.readLong(), buff.readLong());
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			
			if (server != null) {
				Entity attacker = server.getEntityFromUuid(attackerId);
				Entity attacked = server.getEntityFromUuid(attackedId);
				
				if (attacker instanceof EntityLivingBase) {
					server.addScheduledTask(() -> {
						ItemAddedShield.doBashAttack((EntityLivingBase)attacker, attacked);
					});
				}
			}
		}
		case LEFT_CLICK_ITEM: {
			UUID playerId = new UUID(buff.readLong(), buff.readLong());
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			
			if (server != null) {
				Entity player = server.getEntityFromUuid(playerId);
				
				if (player instanceof EntityLivingBase) {
					ItemStack stack = ((EntityLivingBase)player).getHeldItemMainhand();
					
					if (!stack.isEmpty()) {
						server.addScheduledTask(() -> CommonEventHandler.handleLeftClickItemEvent(player, stack));
					}
				}
			}
		}
		default:
			//do nothing.
		}

		return null;
	}
}
