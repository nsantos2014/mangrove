package net.minecraft.mangrove.mod.thrive.autocon.junction;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.mangrove.mod.thrive.robot.entity.TileRobotKernel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiStorageJunction extends MGGui {
	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
	private static final ResourceLocation craftingTableGuiTextures2 = new ResourceLocation("textures/gui/container/creative_inventory/tab_items.png");
	private static final ResourceLocation ledger = new ResourceLocation("textures/gui/demo_background.png");
	private TileStorageJunction crate;

	private GuiButton nextPageButton;
	private GuiButton prevPageButton;

	private ContainerStorageJunction container;

	private GuiStorageJunction(ContainerStorageJunction container, TileStorageJunction autobench) {
		super(container, autobench, ledger);
		this.container = container;
		this.crate = autobench;
	}

	public GuiStorageJunction(InventoryPlayer par1InventoryPlayer, TileStorageJunction autobench) {
		this(new ContainerStorageJunction(autobench, par1InventoryPlayer), autobench);
	}

	@Override
	public void initGui() {
		super.initGui();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		this.prevPageButton = new GuiButton(0, k + (xSize / 2) - 25, l + 60, 20, 20, "<");
		this.nextPageButton = new GuiButton(1, k + (xSize / 2) + 30, l + 60, 20, 20, ">");
		buttonList.add(prevPageButton);
		buttonList.add(nextPageButton);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		this.fontRendererObj.drawString(String.format("Crate - Pos %d to %d", container.getStartPos(), container.getEndPos()), 28, 6, 4210752);
		this.fontRendererObj.drawString("Crate", 28, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(ledger);

		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize - 8, 256);
		this.drawTexturedModalRect(k + xSize - 8, l, 256 - 16, 0, 16, 256);
		
		this.mc.getTextureManager().bindTexture(inventoryBackground);
		this.drawTexturedModalRect(k + 6, l + 80, 6, 80, xSize - 6, ySize - 70 - 8);

		this.mc.getTextureManager().bindTexture(craftingTableGuiTextures2);

		this.drawTexturedModalRect(k + 8, l + 16, 8, 16, 54, 55);
		this.drawTexturedModalRect(k + (xSize / 2) - 8, l + 16, 8, 16, 18, 19);
		this.drawTexturedModalRect(k + xSize - 8 - 54, l + 16, 8, 16, 54, 55);

	}

	@Override
	protected void actionPerformed(GuiButton btn) throws IOException {
		super.actionPerformed(btn);
		switch (btn.id) {
		case 1:
			container.nextPage();
			break;
		case 0:
			container.previousPage();
			break;
		}
	}
}
