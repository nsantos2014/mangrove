package net.minecraft.mangrove.mod.vehicles.mav.render;

import java.nio.FloatBuffer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.mod.vehicles.mav.EntityMAV;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMAV extends Render
{
    private static final ResourceLocation boatTextures = new ResourceLocation("textures/entity/boat.png");
    private static final ResourceLocation landTextures = new ResourceLocation("textures/entity/minecart.png");
    /** instance of ModelBoat for rendering */
    protected ModelLandMAV modelLand;
    protected ModelBoatMAV modelBoat;
    protected ModelMixMAV modelMix;
    protected final RenderBlocks field_94145_f;
    

    public RenderMAV(){
        this.shadowSize = 0.5F;
        this.modelLand = new ModelLandMAV();
        this.modelBoat = new ModelBoatMAV();
        this.modelMix=new ModelMixMAV();
        this.field_94145_f = new RenderBlocks();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
	public void doRenderLand3(EntityMAV entity, double x, double y, double z, float u, float v){
		
		 GL11.glPushMatrix();
	        this.bindEntityTexture(entity);
	        long i = (long)entity.getEntityId() * 493286711L;
	        i = i * i * 4392167121L + i * 98761L;
	        float f2 = (((float)(i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
	        float f3 = (((float)(i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
	        float f4 = (((float)(i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
	        GL11.glTranslatef(f2, f3, f4);
	        double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)v;
	        double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)v;
	        double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)v;
	        double d6 = 0.30000001192092896D;
	        Vec3 vec3 =  Vec3.createVectorHelper(d3, d4, d5);
//	        float f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * v;
//	        float f5 = entity.moveForward;//entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * v;
	        
//	        double y;
//			if (vec3 != null)
//	        {
//	            Vec3 vec31 = Vec3.createVectorHelper(d3, d4, d5);
//	            Vec3 vec32 = Vec3.createVectorHelper(d3, d4, d5);
//
//	            if (vec31 == null)
//	            {
//	                vec31 = vec3;
//	            }
//
//	            if (vec32 == null)
//	            {
//	                vec32 = vec3;
//	            }
//
//	            x += vec3.xCoord - d3;
//	            y += (vec31.yCoord + vec32.yCoord) / 2.0D - d4;
//	            z += vec3.zCoord - d5;
//	            Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);
//
//	            if (vec33.lengthVector() != 0.0D)
//	            {
//	                vec33 = vec33.normalize();
//	                u = (float)(Math.atan2(vec33.zCoord, vec33.xCoord) * 180.0D / Math.PI);
//	                f5 = (float)(Math.atan(vec33.yCoord) * 73.0D);
//	            }
//	        }

	        GL11.glTranslatef((float)x, (float)y, (float)z);
	        
	        GL11.glRotatef(180.0F - u, 0.0F, 1.0F, 0.0F);
//	        GL11.glRotatef(-f5, 0.0F, 0.0F, 1.0F);
	        float f7 = (float)10 - v;
	        float f8 = 10 - v;

	        if (f8 < 0.0F)
	        {
	            f8 = 0.0F;
	        }

//	        if (f7 > 0.0F)
//	        {
//	            GL11.glRotatef(MathHelper.sin(f7) * f7 * f8 / 10.0F * (float)1, 1.0F, 0.0F, 0.0F);
//	        }

//	        int k = entity.getDisplayTileOffset();
//	        Block block = entity.func_145820_n();
//	        int j = entity.getDisplayTileData();
//
//	        Block block = Blocks.glowstone;
//	        int k =2;
//	        int j=2;
//	        
////	        if (block.getRenderType() != -1)
////	        {
//	            GL11.glPushMatrix();
//	            this.bindTexture(TextureMap.locationBlocksTexture);
//	            float f6 = 0.75F;
//	            GL11.glScalef(f6, f6, f6);
//	            GL11.glTranslatef(0.0F, (float)k / 16.0F, 0.0F);
//	            this.func_147910_a(entity, v, block, j);
//	            GL11.glPopMatrix();
//	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//	            this.bindEntityTexture(entity);
////	        }
	        
	        GL11.glScalef(-1.0F, -1.0F, 1.0F);
	        this.modelLand.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
//	        this.modelBoat.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
	        GL11.glPopMatrix();
}
    public void doRenderLand2(EntityMAV par1EntityMinecart, double par2, double par4, double par6, float par8, float par9)
    {
    	GL11.glPushMatrix();
        this.bindEntityTexture(par1EntityMinecart);
        long i = (long)par1EntityMinecart.getEntityId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f2 = (((float)(i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f3 = (((float)(i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f4 = (((float)(i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        GL11.glTranslatef(f2, f3, f4);
        double d3 = par1EntityMinecart.lastTickPosX + (par1EntityMinecart.posX - par1EntityMinecart.lastTickPosX) * (double)par9;
        double d4 = par1EntityMinecart.lastTickPosY + (par1EntityMinecart.posY - par1EntityMinecart.lastTickPosY) * (double)par9;
        double d5 = par1EntityMinecart.lastTickPosZ + (par1EntityMinecart.posZ - par1EntityMinecart.lastTickPosZ) * (double)par9;
        double d6 = 0.30000001192092896D;
        float f5 = par1EntityMinecart.prevRotationPitch + (par1EntityMinecart.rotationPitch - par1EntityMinecart.prevRotationPitch) * par9;
        /*
        Vec3 vec3 = par1EntityMinecart.func_70489_a(d3, d4, d5);
        float f5 = par1EntityMinecart.prevRotationPitch + (par1EntityMinecart.rotationPitch - par1EntityMinecart.prevRotationPitch) * par9;

        if (vec3 != null)
        {
            Vec3 vec31 = par1EntityMinecart.func_70495_a(d3, d4, d5, d6);
            Vec3 vec32 = par1EntityMinecart.func_70495_a(d3, d4, d5, -d6);

            if (vec31 == null)
            {
                vec31 = vec3;
            }

            if (vec32 == null)
            {
                vec32 = vec3;
            }

            par2 += vec3.xCoord - d3;
            par4 += (vec31.yCoord + vec32.yCoord) / 2.0D - d4;
            par6 += vec3.zCoord - d5;
            Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);

            if (vec33.lengthVector() != 0.0D)
            {
                vec33 = vec33.normalize();
                par8 = (float)(Math.atan2(vec33.zCoord, vec33.xCoord) * 180.0D / Math.PI);
                f5 = (float)(Math.atan(vec33.yCoord) * 73.0D);
            }
        }
*/
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-f5, 0.0F, 0.0F, 1.0F);
//        float f7 = (float)par1EntityMinecart.getRollingAmplitude() - par9;
//        float f8 = par1EntityMinecart.getDamage() - par9;

//        if (f8 < 0.0F)
//        {
//            f8 = 0.0F;
//        }
//
//        if (f7 > 0.0F)
//        {
//            GL11.glRotatef(MathHelper.sin(f7) * f7 * f8 / 10.0F * (float)par1EntityMinecart.getRollingDirection(), 1.0F, 0.0F, 0.0F);
//        }

//        int k = par1EntityMinecart.getDisplayTileOffset();
//        Block block = par1EntityMinecart.func_145820_n();
//        int j = par1EntityMinecart.getDisplayTileData();
//
//        if (block.getRenderType() != -1)
//        {
//            GL11.glPushMatrix();
//            this.bindTexture(TextureMap.locationBlocksTexture);
//            float f6 = 0.75F;
//            GL11.glScalef(f6, f6, f6);
//            GL11.glTranslatef(0.0F, (float)k / 16.0F, 0.0F);
//            this.func_147910_a(par1EntityMinecart, par9, block, j);
//            GL11.glPopMatrix();
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            this.bindEntityTexture(par1EntityMinecart);
//        }

        int x = 15728880;
        
        int j = x % 65536;
        int k = x / 65536;
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        FloatBuffer matSpecular;
    	FloatBuffer lightPosition;
    	FloatBuffer whiteLight; 
    	FloatBuffer lModelAmbient;
    	
    	matSpecular = BufferUtils.createFloatBuffer(4);
		matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
		
		whiteLight = BufferUtils.createFloatBuffer(4);
		whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lModelAmbient = BufferUtils.createFloatBuffer(4);
		lModelAmbient.put(1.5f).put(1.5f).put(1.5f).put(1.0f).flip();
        
		this.modelLand.render(par1EntityMinecart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, matSpecular);				// sets specular material color
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);					// sets shininess
		
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);				// sets light position
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, whiteLight);				// sets specular light to white
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, whiteLight);					// sets diffuse light to white
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light 
		
        GL11.glEnable(GL11.GL_LIGHTING);										// enables lighting
        GL11.glEnable(GL11.GL_LIGHT0);										// enables light0
		
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
        
        GL11.glPopMatrix();
        
        renderEntityOnFire(par1EntityMinecart, par2, par4, par6, par8);
    }
    
    /**
     * Renders fire on top of the entity. Args: entity, x, y, z, partialTickTime
     */
    private void renderEntityOnFire(Entity p_76977_1_, double p_76977_2_, double p_76977_4_, double p_76977_6_, float p_76977_8_)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        IIcon iicon = Blocks.fire.getFireIcon(0);
        IIcon iicon1 = Blocks.fire.getFireIcon(1);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)p_76977_2_, (float)p_76977_4_, (float)p_76977_6_);
        float f1 = p_76977_1_.width * 1.4F;
        GL11.glScalef(f1, f1, f1);
        Tessellator tessellator = Tessellator.instance;
        float f2 = 0.5F;
        float f3 = 0.0F;
        float f4 = p_76977_1_.height / f1;
        float f5 = (float)(p_76977_1_.posY - p_76977_1_.boundingBox.minY);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float)((int)f4) * 0.02F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f6 = 0.0F;
        int i = 0;
        tessellator.startDrawingQuads();

        while (f4 > 0.0F)
        {
            IIcon iicon2 = i % 2 == 0 ? iicon : iicon1;
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f7 = iicon2.getMinU();
            float f8 = iicon2.getMinV();
            float f9 = iicon2.getMaxU();
            float f10 = iicon2.getMaxV();

            if (i / 2 % 2 == 0)
            {
                float f11 = f9;
                f9 = f7;
                f7 = f11;
            }

            tessellator.addVertexWithUV((double)(f2 - f3), (double)(0.0F - f5), (double)f6, (double)f9, (double)f10);
            tessellator.addVertexWithUV((double)(-f2 - f3), (double)(0.0F - f5), (double)f6, (double)f7, (double)f10);
            tessellator.addVertexWithUV((double)(-f2 - f3), (double)(1.4F - f5), (double)f6, (double)f7, (double)f8);
            tessellator.addVertexWithUV((double)(f2 - f3), (double)(1.4F - f5), (double)f6, (double)f9, (double)f8);
            f4 -= 0.45F;
            f5 -= 0.45F;
            f2 *= 0.9F;
            f6 += 0.03F;
            ++i;
        }

        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    
    protected void func_147910_a(EntityMAV p_147910_1_, float p_147910_2_, Block p_147910_3_, int p_147910_4_)
    {
        float f1 = p_147910_1_.getBrightness(p_147910_2_);
        GL11.glPushMatrix();
        this.field_94145_f.renderBlockAsItem(p_147910_3_, p_147910_4_, f1);
        GL11.glPopMatrix();
    }
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRenderBoat(EntityMAV entityMAV, double x, double y, double z, float par8, float par9)
    {
        GL11.glPushMatrix();

        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
//        float f2 = (float)entityMAV.getTimeSinceHit() - par9;
//        float f3 = entityMAV.getDamageTaken() - par9;
//
//        if (f3 < 0.0F)
//        {
//            f3 = 0.0F;
//        }
//
//        if (f2 > 0.0F)
//        {
//            GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10.0F * (float)entityMAV.getForwardDirection(), 1.0F, 0.0F, 0.0F);
//        }

        float f4 = 0.75F;
        GL11.glScalef(f4, f4, f4);
        GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
        this.bindEntityTexture(entityMAV);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelBoat.render(entityMAV, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
   
    public void doRenderMix(EntityMAV entityMAV, double x, double y, double z, float u, float v){
        GL11.glPushMatrix();

        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(180.0F - u, 0.0F, 1.0F, 0.0F);
        
//        float f4 = 1.0F;
//        GL11.glScalef(f4, f4, f4);
        //GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
        this.bindEntityTexture(entityMAV);
//        GL11.glScalef(-3.9F, -1.9F, 3.9F);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        this.modelMix.render(entityMAV, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
    
    public void doRenderLand(EntityMAV entityMAV, double x, double y, double z, float par8, float par9)
    {
        GL11.glPushMatrix();

        GL11.glTranslatef((float)x, (float)y, (float)z);
//        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        
//        float f2 = (float)entityMAV.getTimeSinceHit() - par9;
//        float f3 = entityMAV.getDamageTaken() - par9;
//
//        if (f3 < 0.0F)
//        {
//            f3 = 0.0F;
//        }
//
//        if (f2 > 0.0F)
//        {
//            GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10.0F * (float)entityMAV.getForwardDirection(), 1.0F, 0.0F, 0.0F);
//        }

        float f4 = 10.0F;
        GL11.glScalef(f4, f4, f4);
//        GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
        this.bindEntityTexture(entityMAV);
//        GL11.glScalef(-3.9F, -1.9F, 3.9F);
//        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        this.modelLand.render(entityMAV, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityMAV entity)
    {
//    	if( entity.isFloating())
//    		return boatTextures;
//    	else
    		return landTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.getEntityTexture((EntityMAV)par1Entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double x, double y, double z, float u, float v)
    {
    	EntityMAV entityMAV = (EntityMAV)entity;
//    	if( entityMAV.isFloating()){
    		this.doRenderBoat(entityMAV, x, y, z, u, v);
//    	}else{
//    		this.doRenderLand(entityMAV, x, y, z, u, v);
//    	}
//    	this.doRenderMix(entityMAV, x, y, z, u, v);
    }
}