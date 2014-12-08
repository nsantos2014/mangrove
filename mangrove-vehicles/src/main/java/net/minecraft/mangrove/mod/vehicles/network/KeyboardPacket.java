package net.minecraft.mangrove.mod.vehicles.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.core.gui.MGContainer;
import net.minecraft.mangrove.mod.vehicles.proxy.IKeyControlable;
import net.minecraft.mangrove.mod.vehicles.proxy.IKeyControlable.ID;
import net.minecraft.mangrove.network.AbstractPacket;

public class KeyboardPacket extends AbstractPacket{
    public int key;
    
	public KeyboardPacket(int key) {
        this.key = key;
	}
	    
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		 buffer.writeInt(key);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		 key = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
	    if(player.ridingEntity != null && player.ridingEntity instanceof IKeyControlable){
            ((IKeyControlable)player.ridingEntity).pressKey(ID.values()[key], player);
        }
	}

}
