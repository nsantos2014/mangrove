package net.minecraft.mangrove.mod.vehicles.mav.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMixMAV extends ModelBase{
	 private ModelRenderer model;
	
	public ModelMixMAV() {
		model=new ModelRenderer(this);
		model.addBox(-2, -1, -2, 4, 2, 4);
	}
	
	/**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7){
        model.render(par7);     
    }
}
