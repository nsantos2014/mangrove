package net.minecraft.mangrove.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMessageHandler implements IMessageHandler<TileEntityMessage, IMessage>{

	@Override
	public IMessage onMessage(TileEntityMessage message, MessageContext ctx) {
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
