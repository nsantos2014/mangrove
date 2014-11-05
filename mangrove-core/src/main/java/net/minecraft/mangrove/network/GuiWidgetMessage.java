package net.minecraft.mangrove.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.core.gui.MGContainer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class GuiWidgetMessage implements IMessage {
	private int windowId, widgetId;
	private byte[] payload;

	public GuiWidgetMessage() {
	}

	public GuiWidgetMessage(int windowId, int widgetId, byte[] payload) {
		this.windowId = windowId;
		this.widgetId = widgetId;
		this.payload = payload;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		windowId = buffer.readInt();
		widgetId = buffer.readInt();
		this.payload = new byte[buffer.readableBytes()];
		buffer.readBytes(this.payload);
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(windowId);
		buffer.writeInt(widgetId);
		buffer.writeBytes(payload);
	}

	public void handleClientSide(EntityPlayer player) {

	}

	public void handleServerSide(EntityPlayer player) {
		if (player.openContainer instanceof MGContainer
				&& player.openContainer.windowId == windowId) {
			((MGContainer) player.openContainer).handleWidgetClientData(
					widgetId, this.payload);
		}
	}

}
