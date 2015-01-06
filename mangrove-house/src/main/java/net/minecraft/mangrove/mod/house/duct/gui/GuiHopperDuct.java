package net.minecraft.mangrove.mod.house.duct.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.mangrove.core.gui.button.GuiToggleButton;
import net.minecraft.mangrove.mod.house.duct.AbstractBlockDuct;
import net.minecraft.mangrove.mod.house.duct.AbstractTileEntityDuct;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;


public class GuiHopperDuct extends MGGui{
	private static final ResourceLocation rl = new ResourceLocation("mangrove","textures/gui/container/hopperduct.png");
	private AbstractTileEntityDuct tile;
	
	public GuiHopperDuct(InventoryPlayer inventory, AbstractTileEntityDuct tile) {
		super(new ContainerHopperDuct( tile,inventory),tile,rl);
		this.tile = tile;
	}
	@Override
	public void initGui() {
			super.initGui();
			buttonList.clear();
//			int w = (width - xSize) / 2;
//			int h = (height - ySize) / 2;
			int w = 40;
			int h =40;
			h+=100;
			
			this.buttonList.add(new GuiToggleButton(ForgeDirection.DOWN.ordinal() , w+40, h-10,20, "v",false));
			this.buttonList.add(new GuiToggleButton(ForgeDirection.UP.ordinal()   , w+40, h-30,20, "^",false));
			
			this.buttonList.add(new GuiToggleButton(ForgeDirection.NORTH.ordinal(), w-10, h-30,20, "N",false));
			this.buttonList.add(new GuiToggleButton(ForgeDirection.SOUTH.ordinal(), w-10, h+10,20, "S",false));
			this.buttonList.add(new GuiToggleButton(ForgeDirection.WEST.ordinal() , w-25, h-10,30, "W",false));
			this.buttonList.add(new GuiToggleButton(ForgeDirection.EAST.ordinal() , w+5, h-10,25, "E",false));			
	}
	
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		this.fontRendererObj.drawString("Duct", 8, 6, 4210752);
		int md = tile.getBlockMetadata();		
		int dir = AbstractBlockDuct.getDirectionFromMetadata(md);
		
		for (Object obj : this.buttonList) {
			if (obj instanceof GuiToggleButton) {
				GuiToggleButton btn = (GuiToggleButton) obj;
				btn.setActive(btn.id==dir);
			}
		}
		
		//public static final String[] facings = new String[] {"DOWN", "UP", "NORTH", "SOUTH", "WEST", "EAST"};
		
//		this.fontRendererObj.drawString("Connected Up : "+tile.isConnectedTo(1), 8, 16, 4210752);		
//		this.fontRendererObj.drawString("Connected Down : "+tile.isConnectedTo(0), 8, 26, 4210752);
//		this.fontRendererObj.drawString("Connected North : "+tile.isConnectedTo(2), 8, 36, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8,this.ySize - 128 + 2, 4210752);
		
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.rl);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}
		
	@Override
	protected void actionPerformed(GuiButton selectedBtn) {
	
		for (Object obj : this.buttonList) {
			if (obj instanceof GuiToggleButton) {
				GuiToggleButton btn = (GuiToggleButton) obj;
				if( btn==selectedBtn){
					btn.setActive(true);
					this.tile.setDirection(btn.id);
				}else{
					btn.setActive(false);
				}
				
			}
		}
	}

	
}
