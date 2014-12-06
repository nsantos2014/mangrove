package net.minecraft.mangrove.mod.maps.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.EntityLiving;
import net.minecraft.mangrove.mod.maps.api.IMwChunkOverlay;
import net.minecraft.mangrove.mod.maps.api.IMwDataProvider;
import net.minecraft.mangrove.mod.maps.map.MapView;
import net.minecraft.mangrove.mod.maps.map.mapmode.MapMode;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.xml.internal.stream.Entity;

public class OverlayMobs implements IMwDataProvider {

    public class MobChunkOverlay implements IMwChunkOverlay{

		Point coord;
		
		public MobChunkOverlay(int x, int z){
			this.coord = new Point(x, z);
		}
		
		@Override
		public Point getCoordinates() {	return this.coord; }

		@Override
		public int getColor() {	return 0x50ff0000; }

		@Override
		public float getFilling() {	return 0.1f; }

		@Override
		public boolean hasBorder() { return true; }

		@Override
		public float getBorderWidth() { return 0.05f; }

		@Override
		public int getBorderColor() { return 0xff000000; }
		
	}
	
	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ) {
		
		// We should pass the center of the map too to reduce the display like in this case
		// and the zoom lvl, to provide higher level informations
		
		final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        final World world = player.getEntityWorld();
        if (world.provider.dimensionId != dim)
			return new ArrayList<IMwChunkOverlay>();
		
//		int minChunkX = (MathHelper.ceiling_double_int(minX) >> 4) - 1;
//		int minChunkZ = (MathHelper.ceiling_double_int(minZ) >> 4) - 1;
//		int maxChunkX = (MathHelper.ceiling_double_int(maxX) >> 4) + 1;
//		int maxChunkZ = (MathHelper.ceiling_double_int(maxZ) >> 4) + 1;
//		int cX = (MathHelper.ceiling_double_int(centerX) >> 4) + 1;
//		int cZ = (MathHelper.ceiling_double_int(centerZ) >> 4) + 1;
//		
//		int limitMinX = Math.max(minChunkX, cX - 100);
//		int limitMaxX = Math.min(maxChunkX, cX + 100);
//		int limitMinZ = Math.max(minChunkZ, cZ - 100);
//		int limitMaxZ = Math.min(maxChunkZ, cZ + 100);

		final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(minX, player.posY-5, minZ, maxX, player.posY+5, maxZ);
        final List list = world.getEntitiesWithinAABB(EntityLiving.class, boundingBox);
		//System.out.println("Entity : "+list.size()+":"+boundingBox);
        final ArrayList<IMwChunkOverlay> chunks = new ArrayList<IMwChunkOverlay>();
        for (final Object entityObj:list){
            
            if (entityObj instanceof EntityLiving) {
                EntityLiving entityLiving = (EntityLiving) entityObj;
                
                int x = MathHelper.ceiling_double_int(entityLiving.posX);
                int z = MathHelper.ceiling_double_int(entityLiving.posZ);
                System.out.println("Entity at : ("+x+","+z+") : "+entityLiving);
                chunks.add(new MobChunkOverlay(x>> 4, z>> 4));
            } 
            
        }		
		return chunks;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ) { return "";	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, MapView mapview){	}

	@Override
	public void onDimensionChanged(int dimension, MapView mapview) {	}

	@Override
	public void onMapCenterChanged(double vX, double vZ, MapView mapview) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onZoomChanged(int level, MapView mapview) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOverlayActivated(MapView mapview) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOverlayDeactivated(MapView mapview) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDraw(MapView mapview, MapMode mapmode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMouseInput(MapView mapview, MapMode mapmode) {
		// TODO Auto-generated method stub
		return false;
	}

}
