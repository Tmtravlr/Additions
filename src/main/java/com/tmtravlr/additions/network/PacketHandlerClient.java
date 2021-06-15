package com.tmtravlr.additions.network;

import com.tmtravlr.additions.AdditionsMod;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Tmtravlr
 */
public class PacketHandlerClient implements IMessageHandler<SToCMessage, IMessage> {

	//Types of packets
	public static final int SYNC_INVENTORY = 0;

	public IMessage onMessage(SToCMessage packet, MessageContext context) {
		PacketBuffer buff = new PacketBuffer(Unpooled.wrappedBuffer(packet.getData()));

		int type = buff.readInt();

		switch(type) {
		case SYNC_INVENTORY: {
			AdditionsMod.proxy.syncPlayerInventory(buff);
			
		}
		default:
			//Do nothing
		}

		return null;
	}
}
