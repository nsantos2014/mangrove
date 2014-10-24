/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;

public class PacketRPCGui extends PacketRPC {
	public PacketRPCGui() {
	}

	public PacketRPCGui(ByteBuf bytes) {
		contents = bytes;
	}

	@Override
	public void call (EntityPlayer sender) {
		super.call(sender);

		RPCMessageInfo info = new RPCMessageInfo();
		info.sender = sender;

		RPCHandler.receiveRPC(sender.openContainer, info, contents);
	}
}
