package net.minecraft.mangrove.mod.vehicles.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class KeyboardMessageHandler implements IMessageHandler<KeyboardMessage, IMessage>{

	@Override
	public IMessage onMessage(KeyboardMessage message, MessageContext ctx) {
		EntityPlayer player;
		switch(ctx.side) {
		case CLIENT:
			//Object player = ctx.getClientHandler();
			player = this.getClientPlayer();
            message.handleClientSide(player);
            
			break;
		case SERVER:
			player=ctx.getServerHandler().playerEntity;
			message.handleServerSide(player);
			break;
		}
		
		return null;
	}


    @SideOnly(Side.CLIENT)
    private EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

}
