package net.minecraft.mangrove.mod.thrive.block.harvester.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.mangrove.mod.thrive.block.harvester.TileEntityHarvester;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiHarvester extends MGGui {
	static final ResourceLocation rl = new ResourceLocation("mangrove","textures/gui/container/gratedhopper.png");

	public GuiHarvester(InventoryPlayer inventoryPlayer,TileEntityHarvester tileEntity) {
		super(new ContainerHarvester(tileEntity,inventoryPlayer),tileEntity,rl);
	}

	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		this.fontRendererObj.drawString("Harvester", 8, 6, 4210752);

		this.fontRendererObj.drawString("=", 35, 55, 4210752);

		this.fontRendererObj.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				this.ySize - 128 + 33, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		this.mc.getTextureManager().bindTexture(this.rl);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

}
