package net.minecraft.mangrove.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.core.gui.MGContainer;

public class GuiWidgetPacket extends AbstractPacket{
	private byte windowId, widgetId;
	private byte[] payload;
	
	public GuiWidgetPacket(int windowId, int widgetId, byte[] data) {
		this.windowId = (byte) windowId;
        this.widgetId = (byte) widgetId;
        this.payload = data;
	}
	    
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		 buffer.writeByte(windowId);
	     buffer.writeByte(widgetId);
	     buffer.writeBytes(payload);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		 windowId = buffer.readByte();
	     widgetId = buffer.readByte();
	     this.payload=new byte[buffer.readableBytes()];
	     buffer.readBytes(this.payload);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		if (player.openContainer instanceof MGContainer && player.openContainer.windowId == windowId) {
            ((MGContainer) player.openContainer).handleWidgetClientData(widgetId, this.payload);
        }
	}

}
