package net.minecraft.mangrove.mod.vehicles.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.core.core.gui.MGContainer;
import net.minecraft.mangrove.mod.vehicles.proxy.IKeyControlable;
import net.minecraft.mangrove.mod.vehicles.proxy.IKeyControlable.ID;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class KeyboardMessage implements IMessage {
	private int key;

	public KeyboardMessage() {
	}

	public KeyboardMessage(ID id) {
        this(id.ordinal());
    }
	
	public KeyboardMessage(int key) {
		this.key = key;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		key = buffer.readInt();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(key);
	}

	public void handleClientSide(EntityPlayer player) {

	}

	public void handleServerSide(EntityPlayer player) {
	    if(player.ridingEntity != null && player.ridingEntity instanceof IKeyControlable){
            ((IKeyControlable)player.ridingEntity).pressKey(ID.values()[key], player);
        }
	}

}
