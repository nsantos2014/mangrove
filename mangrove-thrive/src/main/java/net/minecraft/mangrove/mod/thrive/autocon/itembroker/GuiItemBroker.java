package net.minecraft.mangrove.mod.thrive.autocon.itembroker;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiItemBroker extends GuiContainer {
	private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("mgthrive", "textures/gui/container/item_broker.png");

	private static final int[][] coords = new int[][] { { 42, 6}, { 42, 24 }, { 42, 42 }, { 42, 60}, { 42, 78 }, { 42, 96 } };
	private static final int[][] coords2 = new int[][] { { 22, 8}, { 22, 26 }, {22, 44 }, { 22, 62}, { 22, 80 }, { 22, 98 } };

	private final TileItemBroker tileItemBroker;

	private IInventory upperChestInventory;
	private int inventoryRows;

	public GuiItemBroker(TileItemBroker tileItemBroker, IInventory upperInv) {
		super(new ContainerItemBroker(tileItemBroker,upperInv, Minecraft.getMinecraft().thePlayer));
		this.xSize=190;
		this.ySize=202;
		this.tileItemBroker = tileItemBroker;
		this.upperChestInventory = upperInv;

		this.allowUserInput = false;
	}

	@Override
	public void initGui() {
		super.initGui();
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;

		for (EnumFacing facing : EnumFacing.values()) {
		
			ConnectionConfig connectorConfig = tileItemBroker.getConnectorConfig(facing);
			if( connectorConfig.getState()!=EnumConnectionState.DISCONNECTED){
				int[] coord = coords[facing.ordinal()];
				int x = i + coord[0];
				int y = j + coord[1];
				ConnectionButton connectionButton = new ConnectionButton(1 + facing.ordinal(), x, y, facing,connectorConfig.getState());
				this.buttonList.add(connectionButton);
			}
		}

	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = "Some Shit!!!";
		int x,y;
		GlStateManager.pushMatrix();
//		int x = this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2;
//		int y = 6;
		x = 8;
		y = this.ySize - 96 + 8;
		
		GlStateManager.translate(x, y, 0);
		GlStateManager.rotate(-90, 0, 0, 1);
		this.fontRendererObj.drawString(s, 0, 0, 4210752);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		x = 8;
		y = this.ySize-8;		
		GlStateManager.translate(x, y, 0);
		GlStateManager.rotate(-90, 0, 0, 1);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 0, 0, 4210752);
				
		GlStateManager.popMatrix();
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		
		
		int coordIndex=0;
		this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		for (EnumFacing facing : EnumFacing.values()) {
			
			ConnectionConfig connectorConfig = tileItemBroker.getConnectorConfig(facing);
//			if( connectorConfig.getState()!=State.DISCONNECTED){
//				this.drawTexturedModalRect(k+7, l+17+coordIndex*18, 8, 225, 162, 18);
				
				int[] coord = coords2[coordIndex++];
				int x = k+coord[0];
				int y = l+coord[1];
				
				this.drawTexturedModalRect(x, y, 2+facing.ordinal()*16, 204, 16, 16);
//			}
		}
		
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button instanceof ConnectionButton) {
			ConnectionButton connectButton = (ConnectionButton) button;
			tileItemBroker.updateConnectorState(connectButton.getFacing(), connectButton.toggleNext());
		}
	}

	@SideOnly(Side.CLIENT)
	static class ConnectionButton extends GuiButton {
		private EnumConnectionState state;
		private EnumFacing facing;

		public ConnectionButton(int buttonID, int x, int y, EnumFacing facing, EnumConnectionState state) {
			super(buttonID, x, y, 12, 18, "");
			this.facing = facing;
			this.state = state;

		}

		public EnumFacing getFacing() {
			return facing;
		}
		
		public EnumConnectionState toggleNext() {
			switch (state) {
			case INLET:
				return (state = EnumConnectionState.OUTLET);
			case OUTLET:
				return (state = EnumConnectionState.NONE);
				
			case NONE:
				return state = EnumConnectionState.INLET;
			
			case DISCONNECTED:
			default:
				break;
			}
			return EnumConnectionState.DISCONNECTED;
		}

		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			if (this.visible) {
				mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
				int k = 0;
				int l = 190;
				k += this.height * state.ordinal();
				if (flag && state != EnumConnectionState.DISCONNECTED) {
					l += this.width;
				}
				this.drawTexturedModalRect(this.xPosition, this.yPosition, l, k, this.width, this.height);
			}
		}
	}
}
