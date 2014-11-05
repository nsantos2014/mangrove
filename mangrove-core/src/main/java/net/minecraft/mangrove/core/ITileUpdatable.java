package net.minecraft.mangrove.core;

import java.io.IOException;

import com.google.gson.JsonObject;

public interface ITileUpdatable {

	JsonObject getTilePacketData();
	
	void handleClientUpdate(JsonObject data) throws IOException;
	
	void handleServerUpdate(JsonObject data) throws IOException;
}
