package net.minecraft.mangrove.mod.thrive.autobench.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.mangrove.mod.thrive.autobench.TileEntityAutobench;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AutobenchGui extends MGGui{
    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");    
//    private static final ResourceLocation craftingTableGuiTextures2 = new ResourceLocation("textures/gui/container/driller.png");
    private static final ResourceLocation craftingTableGuiTextures2 = new ResourceLocation("textures/gui/container/creative_inventory/tab_items.png");
    private static final ResourceLocation ledger = new ResourceLocation("textures/gui/demo_background.png");
    private TileEntityAutobench autobench;
	private boolean toggle;
	private GuiButton toggleButton;
	private GuiButton setButton;
	private AutobenchContainer container;
//	private ContainerDelegate container;
//	private ContainerTemplate container;
//	private ContainerProcessing container;

//    private GuiMAV(ContainerDelegate container, TileEntityCrate autobench){
//        super(container);
//		this.container = container;
//		this.autobench = autobench;		
//    }
    private AutobenchGui(AutobenchContainer container, TileEntityAutobench autobench){
        super(container,autobench,ledger);
		this.container = container;
		this.autobench = autobench;		
    }
    public AutobenchGui(InventoryPlayer par1InventoryPlayer, TileEntityAutobench autobench){
    	this(new AutobenchContainer(par1InventoryPlayer, autobench),autobench);
//    	this(new ContainerProcessing(par1InventoryPlayer, autobench),autobench);
//        this(new ContainerDelegate(
//        		new ContainerProcessing(par1InventoryPlayer, autobench),
//        		new ContainerTemplate(par1InventoryPlayer, autobench)),autobench);
    }
//	  private GuiMAV(ContainerTemplate container, TileEntityCrate autobench){
//		  super(container);
//		this.container = container;
//		this.autobench = autobench;		
//	}
//	  private GuiMAV(ContainerProcessing container, TileEntityCrate autobench){
//		  super(container);
//		this.container = container;
//		this.autobench = autobench;		
//	}
    @Override
    public void initGui() {
    	super.initGui();
    	int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        
    	this.toggleButton = new GuiButton(0, k+(xSize/2)-25, l+60,50, 20,"Template");
    	this.setButton = new GuiButton(1, -(k+(xSize/2)+30), -(l+60),40,20, "Set");
		buttonList.add(toggleButton);		
		buttonList.add(setButton);
		container.hideTemplateSlots();
		container.showOperationSlots();
		//container.setProcessingContainer();
    }
       
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
    	if( !toggle){
    		this.fontRendererObj.drawString("Processing", 28, 6, 4210752);
    	}else{
    		this.fontRendererObj.drawString("Template", 28, 6, 4210752);
        }
        //this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	this.mc.getTextureManager().bindTexture(ledger);
    	
    	int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        //this.drawTexturedModalRect(k, l, 0, 0, 512/2, 512/2);
        //this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        this.drawTexturedModalRect(k, l, 0, 0, xSize-8, 256);
        this.drawTexturedModalRect(k+xSize-8, l, 256-16, 0, 16, 256);
        this.mc.getTextureManager().bindTexture(field_147001_a);
        //this.drawTexturedModalRect(k, l, 0, 0, 352/2, 332/2);
        //this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		this.drawTexturedModalRect(k+6, l+80, 6, 80, xSize-6, ySize-70-8);
		
    	if( !toggle){
//    		this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
//    		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
//    	}else{
//    		this.mc.getTextureManager().bindTexture(ledger);
//    		this.drawTexturedModalRect(k+4, l+4, 8, 8,120, 68);
    		this.mc.getTextureManager().bindTexture(craftingTableGuiTextures2 );
    		
    		this.drawTexturedModalRect(k+8, l+16, 8, 16,54, 55);
    		this.drawTexturedModalRect(k+(xSize/2)-8, l+16, 8, 16,18, 19);
    		this.drawTexturedModalRect(k+xSize-8-54, l+16, 8, 16,54, 55);
    		
//    		this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
//    		this.drawTexturedModelRectFromIcon(k+(xSize/2)-8+1, l+16+1, Items.apple.getIconFromDamage(1), 16, 16);
    		
    	}else{
    		
    		this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
    		
    		this.drawTexturedModalRect(k+8, l+10, 29, 9, xSize-60, 68);
    	}
        
    }
    
    @Override
    protected void actionPerformed(GuiButton btn) {    	
    	super.actionPerformed(btn);
    	switch (btn.id) {
		case 1:
			container.setTemplate();
		case 0:
    		this.toggle=!toggle;
    		toggleButton.displayString=this.toggle?"Back":"Template";
    		if( this.toggle){
    			setButton.xPosition=Math.abs(setButton.xPosition);
    			setButton.yPosition=Math.abs(setButton.yPosition);
//    			container.setTemplateContainer();
    			container.hideOperationSlots();
    			container.showTemplateSlots();    			
    		}else {
    			setButton.xPosition=-Math.abs(setButton.xPosition);
    			setButton.yPosition=-Math.abs(setButton.yPosition);
//    			container.setProcessingContainer();
    			container.hideTemplateSlots();
    			container.showOperationSlots();
    		}
    		
    		break;
    	}
    }
}
