package net.minecraft.mangrove.core.craftpedia.renderer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.mangrove.core.craftpedia.CRBom;
import net.minecraft.mangrove.core.craftpedia.CRBomItem;
import net.minecraft.mangrove.core.craftpedia.CRPattern;
import net.minecraft.mangrove.core.craftpedia.CraftpediaRecipe;
import net.minecraft.mangrove.core.craftpedia.CraftpediaRecipeRenderer;
import net.minecraft.mangrove.core.craftpedia.Util;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedOreCRR extends Gui implements CraftpediaRecipeRenderer {

	private FontRenderer fontRendererObj;
	private Minecraft	 mc;
	private RenderItem	 itemRender;
	private int	         width;
	private int	         height;

	public ShapedOreCRR() {
	}

	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.mc = mc;
		this.itemRender = mc.getRenderItem();
		this.fontRendererObj = mc.fontRendererObj;
		this.width = width-215-5;
		this.height = height-5-5;
	}

	@Override
	public void renderRecipe(CraftpediaRecipe craftpediaRecipe) {
		ShapedOreRecipe sRecipe = (ShapedOreRecipe) craftpediaRecipe.getRecipe();
		final int recipeSize = sRecipe.getRecipeSize();
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		
		GL11.glTranslatef(215, 5, 0);
		GL11.glColor4f(1.0f, 1.0f,1.0f, 1.0f);
		
		int left = 0;
		int top = 0;
		int right = this.width;
		int bottom = this.height;
		
		drawRect(left, top, right, bottom,	-4144960);
		
		CRBom bom = craftpediaRecipe.getBom();
		CRPattern pattern = bom.getPattern();
		final String data = String.format("%s %dx%d %s", 
				craftpediaRecipe.getOutputName(),
				pattern.getRows(),
				pattern.getCols(),
				(bom.isMirrored()?"(Mirrored)":"") 
			);

		int titleLeft = 24;
		int iconLeft = 5;
		
		renderItem(iconLeft, 10, craftpediaRecipe.getRecipe().getRecipeOutput());
		
		drawString(this.fontRendererObj, data, titleLeft, 20, 16777215);
		
		int iconTop = 34;
		
		for( int i=0; i<pattern.getRows();i++){
			for( int j=0; j<pattern.getCols();j++){
				List<ItemStack> items = pattern.getCell(i, j);
				if( !items.isEmpty()){
					renderItem(iconLeft+j*18, iconTop+i*18, items.get(0));
				}
			}	
		}
		int idx = 0;
//		for (Object obj : sRecipe.getInput()) {
//			renderRecipeComponent(calcPoint((this.width / 2) + 20, 100, idx, recipeSize, sRecipe.getRecipeOutput()), obj);
//			idx++;
//		}
		int heightCalc = this.height / 2;
		idx=0;
		for(CRBomItem bomItem:bom.getContents()){
			Item item = bomItem.getItem();
			bomItem.getName();
			if ((item != null)) {
				renderItem(iconLeft, heightCalc+ idx * 18, bomItem.getItemStack());
			}
			drawString(this.fontRendererObj, bomItem.getName(), titleLeft, heightCalc+ idx * 18, 16777215);
			idx++;
		}
		
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
		
	}

	private void renderRecipeComponent(Point p, Object obj) {
		if (obj instanceof ItemStack) {
			ItemStack iStack = (ItemStack) obj;
			renderItem(p.x, p.y, iStack);
		} else if (obj instanceof Item) {
			ItemStack iStack = new ItemStack((Item) obj);
			renderItem(p.x, p.y, iStack);
		} else if (obj instanceof Collection<?>) {
			// System.out.print("     :> ");
			for (Object sub : ((Collection<?>) obj)) {
				renderRecipeComponent(p, sub);
			}
			// System.out.println();
		}
	}

	private void renderItem(int iconLeft, int iconTop, final ItemStack currentIcon) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		
//		if( currentIcon.getHasSubtypes()){
//			ItemModelMesher itemModelMesher = itemRender.getItemModelMesher();
//			IBakedModel model = itemModelMesher.getItemModel(currentIcon);
//			if( model==null  || model.getTexture()==null){
//				List<ItemStack> subItems=new ArrayList<ItemStack>();
//				Item item = currentIcon.getItem();
//				item.getSubItems(item, null, subItems);
//				itemRender.renderItemAndEffectIntoGUI(subItems.get(0), iconLeft, iconTop);
//			}else{
//				itemRender.renderItemAndEffectIntoGUI(currentIcon, iconLeft, iconTop);
//			}
//		}else{
			itemRender.renderItemAndEffectIntoGUI(currentIcon, iconLeft, iconTop);	
//		}

//		GL11.glEnable(GL11.GL_LIGHTING);
//		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);

		
		if( currentIcon.stackSize>1){
			itemRender.renderItemOverlayIntoGUI(fontRendererObj, currentIcon, iconLeft, iconTop, ""+currentIcon.stackSize);
		}
//		GL11.glDisable(GL11.GL_LIGHTING);
		itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	private Point calcPoint(int left, int top, int idx, int recipeSize, ItemStack itemStack) {
		final Item item = itemStack.getItem();
		int iconSize = 18;
		final String name = item.getUnlocalizedName();
		switch (recipeSize) {
		case 1:
			return new Point(left + iconSize, top + iconSize);
		case 2:
			if (item instanceof ItemCloth) {
				return new Point(left + (idx % 2) * iconSize, top + iconSize);
			}
			if (item instanceof ItemBlock || item instanceof ItemMinecart) {
				System.out.println(" 2-Block==> " + (item == null ? "<NULL>" : item.getClass()) + ":" + name + ":" + itemStack);
				// if( horizontalBlocks.contains(name)){
				// return new Point(left + (idx % 2)*iconSize,top+iconSize);
				// }
				return new Point(left + iconSize, top + iconSize + (idx % 2) * iconSize);
			}
			System.out.println(" 2==> " + (item == null ? "<NULL>" : item.getClass()) + ":" + name + ":" + itemStack);
			return new Point(left + (idx % 2) * iconSize, top + iconSize);
		case 3:
			System.out.println(" 3==> " + (item == null ? "<NULL>" : item.getClass()) + ":" + item + ":" + itemStack);
			if (item instanceof ItemTool || item instanceof ItemSword) {
				return new Point(left + iconSize, top + (idx % 3) * iconSize);
			} else {
				return new Point(left + (idx % 3) * iconSize, top + 40);
			}
		case 4:
			return new Point(left + (idx % 2) * iconSize, top + iconSize + (idx / 2) * iconSize);
		case 9:
			return new Point(left + (idx % 3) * iconSize, top + (idx / 3) * iconSize);
		case 6:
			System.out.println(" 6==> " + (item == null ? "<NULL>" : item.getClass()) + ":" + item + ":" + itemStack);
			if (item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemDoor) {
				return new Point(left + (idx % 2) * iconSize, top + (idx / 2) * iconSize);
			} else {
				return new Point(left + (idx % 3) * iconSize, top + iconSize + (idx / 3) * iconSize);
			}
		}
		return new Point(0, 0);
	}
	
	public int rgba(float red, float green, float blue, float alpha) {
		int redInt = Math.round(red * 255) & 255;
		int greenInt = Math.round(green * 255) & 255;
		int blueInt = Math.round(blue * 255) & 255;
		int alphaInt = Math.round(alpha * 255) & 255;
		return redInt << 24 | greenInt << 16 | blueInt << 8 | alphaInt;
	}
}
