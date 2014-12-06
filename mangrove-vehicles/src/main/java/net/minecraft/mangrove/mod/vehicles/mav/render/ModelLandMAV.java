package net.minecraft.mangrove.mod.vehicles.mav.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLandMAV extends ModelBase{
	 public ModelRenderer[] sideModels = new ModelRenderer[7];
	
	public ModelLandMAV() {
		
		
		//0 -Bottom
		//1 -Right
		//2 - Left
		//3 - Front
		//4 - Back
		this.sideModels[0] = new ModelRenderer(this, 0, 10);
        this.sideModels[1] = new ModelRenderer(this, 0, 0);
        this.sideModels[2] = new ModelRenderer(this, 0, 0);
        this.sideModels[3] = new ModelRenderer(this, 0, 0);
        this.sideModels[4] = new ModelRenderer(this, 0, 0);
        this.sideModels[5] = new ModelRenderer(this, 44, 10);
        byte b0 = 20;
        byte b1 = 8;
        byte b2 = 16;
        byte b3 = 4;
        float width = -1.0F;
        this.sideModels[0].addBox((float)(-b0 / 2), (float)(-b2 / 2), width, b0, b2, 2, 0.0F);
        this.sideModels[0].setRotationPoint(0.0F, (float)b3, 0.0F);
        this.sideModels[5].addBox((float)(-b0 / 2 + 1), (float)(-b2 / 2 + 1), width, b0 - 2, b2 - 2, 1, 0.0F);
        this.sideModels[5].setRotationPoint(0.0F, (float)b3, 0.0F);
        this.sideModels[1].addBox((float)(-b0 / 2 + 2), (float)(-b1 - 1), width, b0 - 4, b1, 2, 0.0F);
        this.sideModels[1].setRotationPoint((float)(-b0 / 2 + 1), (float)b3, 0.0F);
        this.sideModels[2].addBox((float)(-b0 / 2 + 2), (float)(-b1 - 1), width, b0 - 4, b1, 2, 0.0F);
        this.sideModels[2].setRotationPoint((float)(b0 / 2 - 1), (float)b3, 0.0F);
        this.sideModels[3].addBox((float)(-b0 / 2 + 2), (float)(-b1 - 1), width, b0 - 4, b1, 2, 0.0F);
        this.sideModels[3].setRotationPoint(0.0F, (float)b3, (float)(-b2 / 2 + 1));
        this.sideModels[4].addBox((float)(-b0 / 2 + 2), (float)(-b1 - 1), width, b0 - 4, b1, 2, 0.0F);
        this.sideModels[4].setRotationPoint(0.0F, (float)b3, (float)(b2 / 2 - 1));
        this.sideModels[0].rotateAngleX = ((float)Math.PI / 2F);
        this.sideModels[1].rotateAngleY = ((float)Math.PI * 3F / 2F);
        this.sideModels[2].rotateAngleY = ((float)Math.PI / 2F);
        this.sideModels[3].rotateAngleY = (float)Math.PI;
        this.sideModels[5].rotateAngleX = -((float)Math.PI / 2F);
        
	}
	
	/**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
//    	this.sideModels[5].rotationPointY = 4.0F - par4;
//    	this.sideModels[0].rotationPointY = 4.0F - par4;
    	this.sideModels[5].rotationPointY = 4.0F - par4;
    	
    	for (int i = 0; i < 6; ++i)
        {
            this.sideModels[i].render(par7);
        }
    }
}