package net.minecraft.mangrove.network;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;


public class NetBus {
	public static SimpleNetworkWrapper network;	
	private static final String CHANNEL_NAME = "MANGROVE_SC";
	//public static final PacketPipeline packetPipeline = new PacketPipeline();
	private static final AtomicInteger msgIdCounter=new AtomicInteger(); 

	static{
	    network = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL_NAME);
	}
	public static void initialize(){
			    
	}
	public static <REQ extends IMessage, REPLY extends IMessage>void register(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType){
	    final int msgId = msgIdCounter.getAndIncrement();
		network.registerMessage(messageHandler, requestMessageType, msgId, Side.SERVER);
		network.registerMessage(messageHandler, requestMessageType, msgId, Side.CLIENT);
		
	}
	
	public static void sendToServer(IMessage message) {
		network.sendToServer(message);		
	}
	public static void sendToClient(IMessage message) {
		network.sendToAll(message);		
	}
	public static void sendToPlayer(IMessage message, EntityPlayerMP player) {
		network.sendTo(message, player);
	}
	
	public static void notify(String zone,String message){        
        final EntityClientPlayerMP thePlayer = Minecraft.getMinecraft().thePlayer;
        if( thePlayer!=null){
            final ChatComponentText chatcomponenttext = new ChatComponentText(String.format("%s : %s", zone,message));
            thePlayer.addChatMessage(chatcomponenttext);
        }
    }
}
