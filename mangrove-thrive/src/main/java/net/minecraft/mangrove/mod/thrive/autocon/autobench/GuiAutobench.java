package net.minecraft.mangrove.mod.thrive.autocon.autobench;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.mangrove.mod.thrive.autobench.TileEntityAutobench;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAutobench extends GuiContainer {
	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");
	private GuiButton setButton;

	public GuiAutobench(InventoryPlayer playerInv, World worldIn, BlockPos blockPosition) {
		super(new ContainerAutobench(playerInv, worldIn, blockPosition));
		this.xSize=190;
		this.ySize=202;
		this.allowUserInput = false;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
		this.setButton = new GuiButton(1, k+(xSize/2)+30, l+60,40,20, "Set");
		
		buttonList.add(setButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

//	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
//		this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
//		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
//	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
}
