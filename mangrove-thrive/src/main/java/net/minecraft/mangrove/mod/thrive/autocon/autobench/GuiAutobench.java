package net.minecraft.mangrove.mod.thrive.autocon.autobench;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAutobench extends GuiContainer {
	// private static final ResourceLocation craftingTableGuiTextures = new
	// ResourceLocation("minecraft",
	// "textures/gui/container/crafting_table.png");
	private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation(
			"mgthrive", "textures/gui/container/autobench.png");
	private GuiButton setButton;
	private TileEntityAutobench autobench;

	public GuiAutobench(InventoryPlayer playerInv, World worldIn,
			BlockPos blockPosition, TileEntityAutobench autobench) {
		super(new ContainerAutobench(playerInv, worldIn, blockPosition,
				autobench));
		this.autobench = autobench;
		this.xSize = 190;
		this.ySize = 202;
		this.allowUserInput = false;
	}

	@Override
	public void initGui() {
		super.initGui();
		// int k = (this.width - this.xSize) / 2;
		// int l = (this.height - this.ySize) / 2;
		// this.setButton = new GuiButton(1, k+(xSize/2)+30, l+60,40,20, "Set");
		//
		// buttonList.add(setButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = "Some Shit!!!";
		int x, y;
	
		// int x = this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2;
		// int y = 6;
		x = 8;
		y = this.ySize - 96 + 8;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		GlStateManager.rotate(-90, 0, 0, 1);
		this.fontRendererObj.drawString(s, 0, 0, 4210752);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		x = 8;
		y = this.ySize - 8;
		GlStateManager.translate(x, y, 0);
		GlStateManager.rotate(-90, 0, 0, 1);
		this.fontRendererObj.drawString(
				I18n.format("container.inventory", new Object[0]), 0, 0,
				4210752);

		GlStateManager.popMatrix();
		this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int u = 190;
		int v = 80;
		int xPosition = (this.width - this.xSize) / 2;
		int yPosition = (this.height - this.ySize) / 2;
		if (this.autobench.isPowered()) {

			if (this.autobench.isCraftingDefined()) {
				this.drawTexturedModalRect(xPosition, yPosition, u + 30, v, 15,
						15);
			} else {
				this.drawTexturedModalRect(xPosition, yPosition, u + 15, v, 15,
						15);
			}
		} else {
			this.drawTexturedModalRect(xPosition, yPosition, u, v, 15, 15);
		}

	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
}
