package net.minecraft.mangrove.mod.maps.tasks;

import net.minecraft.mangrove.mod.maps.Mw;
import net.minecraft.mangrove.mod.maps.map.MapTexture;
import net.minecraft.mangrove.mod.maps.region.MwChunk;
import net.minecraft.mangrove.mod.maps.region.RegionManager;

public class UpdateSurfaceChunksTask extends Task {
	MwChunk[] chunkArray;
	RegionManager regionManager;
	MapTexture mapTexture;
	
	public UpdateSurfaceChunksTask(Mw mw, MwChunk[] chunkArray) {
		this.mapTexture = mw.mapTexture;
		this.regionManager = mw.regionManager;
		this.chunkArray = chunkArray;
	}
	
	@Override
	public void run() {
		for (MwChunk chunk : this.chunkArray) {
			if (chunk != null) {
				// update the chunk in the region pixels
				this.regionManager.updateChunk(chunk);
				// copy updated region pixels to maptexture
				this.mapTexture.updateArea(
					this.regionManager,
					chunk.x << 4, chunk.z << 4,
					MwChunk.SIZE, MwChunk.SIZE, chunk.dimension
				);
			}
		}
	}
	
	@Override
	public void onComplete() {
	}
}
