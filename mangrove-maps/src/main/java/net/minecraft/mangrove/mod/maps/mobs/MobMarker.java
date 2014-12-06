package net.minecraft.mangrove.mod.maps.mobs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.mangrove.mod.maps.Render;
import net.minecraft.mangrove.mod.maps.api.IMwChunkOverlay;
import net.minecraft.mangrove.mod.maps.map.MapView;
import net.minecraft.mangrove.mod.maps.map.mapmode.MapMode;
import net.minecraft.mangrove.mod.maps.overlay.OverlayMobs.MobChunkOverlay;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MobMarker {
    public Point.Double screenPos = new Point.Double(0, 0);
    
    public MobMarker() {
    }
    
    public void draw(MapMode mapMode, MapView mapView, int borderColour) {
        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        final World world = player.getEntityWorld();
        double scale = mapView.getDimensionScaling(world.provider.dimensionId);
        
        final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(player.posX-50, player.posY-10, player.posZ-50,  player.posX+50, player.posY+10, player.posZ+50);
        final List list = world.getEntitiesWithinAABB(EntityLiving.class, boundingBox);
        //System.out.println("Entity : "+list.size()+":"+boundingBox);
        for (final Object entityObj:list){
            if (entityObj instanceof EntityLiving) {
                EntityLiving entityLiving = (EntityLiving) entityObj;
                
                int x = MathHelper.ceiling_double_int(entityLiving.posX);
                int z = MathHelper.ceiling_double_int(entityLiving.posZ);
                drawAt(mapMode,mapView,entityLiving,borderColour,x,z,scale);
            } 
            
        }        

        
    }

    private void drawAt(MapMode mapMode, MapView mapView, EntityLiving entityLiving, int borderColour, int x, int z,double scale) {
        
        Point.Double p = mapMode.getClampedScreenXY(mapView, x * scale, z * scale);
        this.screenPos.setLocation(p.x + mapMode.xTranslation, p.y + mapMode.yTranslation);
        
        // draw a coloured rectangle centered on the calculated (x, y)
        double mSize = mapMode.markerSize;
        double halfMSize = mapMode.markerSize / 2.0;
        
        net.minecraft.client.renderer.entity.Render renderer = RenderManager.instance.getEntityRenderObject(entityLiving);
        
        Render.setColour(borderColour);        
        Render.drawRect(p.x - halfMSize, p.y - halfMSize, mSize, mSize);
        Render.setColour(0xff0C0C0F);
        Render.drawRect(p.x - halfMSize + 0.5, p.y - halfMSize + 0.5, mSize - 1.0, mSize - 1.0);
    }
    
}