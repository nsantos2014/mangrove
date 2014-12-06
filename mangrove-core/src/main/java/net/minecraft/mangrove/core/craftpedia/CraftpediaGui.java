package net.minecraft.mangrove.core.craftpedia;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.mangrove.core.gui.button.GuiSlider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.registry.GameRegistry;
public class CraftpediaGui extends GuiScreen{
	protected static final ResourceLocation inventory = new ResourceLocation("textures/gui/container/inventory.png");
	private static final Set<String> verticalBlocks=new HashSet<>();
	private static final Set<String> horizontalBlocks=new HashSet<>();
	static{
		verticalBlocks.add("item.magmaCream");
		//verticalBlocks.add("item.eyeOfEnder");ItemEnderEye
		horizontalBlocks.add("");
		
	}
	FontRenderer render;
	private GuiButton exit;
	
//	private List<String> idList = new ArrayList<String>();
//	private List<String> invisibleIdList = new ArrayList<String>();
	private int listPos = 0;
	private final List<IRecipe> recipeList = new ArrayList<>();
	
	private static final Point[] slotPos=new Point[]{
		
	};
	
	public CraftpediaGui() {
		super();
				
		final Iterator it = CraftingManager.getInstance().getRecipeList().iterator();
		while (it.hasNext()) {
			final IRecipe recipe = (IRecipe) it.next();
			if( recipe.getRecipeOutput()==null){
				System.out.println("Found an null receipt : "+recipe);
			}else{
				this.recipeList.add(recipe);
			}
		}
//		final Iterator<Block> blockIterator = Block.blockRegistry.iterator();
//
//        while (blockIterator.hasNext()) {
//        	final Block currentBlock = blockIterator.next();
//        	this.idList.add(Block.blockRegistry.getNameForObject(currentBlock));
//        }
	}
	
	public static void show() {
		Minecraft.getMinecraft().displayGuiScreen(new CraftpediaGui());
	}
	
	public static void close() {
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
	
	public void initGui() {
		super.initGui();
		this.render = this.fontRendererObj;
		int width = this.width;
		int height = this.height;		
		
		this.exit = new GuiButton(0, width / 9 * 7, height - 22, 70, 20, "Exit");
		
		Keyboard.enableRepeatEvents(true);
//		String[] blockListCache = modInstance.blockList;
//		for (int i = 0; i < blockListCache.length; ++i) {
//			this.invisibleIdList.add(blockListCache[i]);
//		}
	}


	protected void actionPerformed(GuiButton par1GuiButton) {
		switch (par1GuiButton.id) {
		case 0:
			Minecraft.getMinecraft().displayGuiScreen(null);
//			MyMod.cooldownTicks = 0;
			break;
		default:
			//this.invisibleIdList.add(this.idList.get(par1GuiButton.id));
		}
	
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int mouseScrollWheelStatus = Mouse.getEventDWheel();
		int mouseButtonStatus = Mouse.getEventButton();
		boolean mouseButtonState = Mouse.getEventButtonState();
		if ((mouseScrollWheelStatus > 0) && (this.listPos > 0)) {
			--this.listPos;
		}
		else if ((mouseScrollWheelStatus < 0) && (this.listPos < this.recipeList.size() - 1)) {
			++this.listPos;
		}
		if ((mouseButtonState) && (mouseButtonStatus != -1))  {
//			if ((this.invisibleIdList.indexOf(this.idList.get(this.listPos)) >= 0)) {
//				this.invisibleIdList.remove(this.idList.get(this.listPos));
//			}
//			else {
//				this.invisibleIdList.add(this.idList.get(this.listPos));
//			}
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		if (par2 != 1 && par2 != 14 && par2 != 29 && par2 != 157) {
			if (((par2 == 200) || (par1 == 'w') || (par1 == 'W')) && (this.listPos > 0)) {
				--this.listPos;
			}
			else if (((par2 == 208) || (par1 == 's') || (par1 == 'S')) && (this.listPos < this.recipeList.size() - 1)) {
				++this.listPos;
			}
			else if (par2 == 28) {
//				if ((this.invisibleIdList.indexOf(this.idList.get(this.listPos)) >= 0)) {
//					this.invisibleIdList.remove(this.idList.get(this.listPos));
//				}
//				else {
//					this.invisibleIdList.add(this.idList.get(this.listPos));
//				}
			}
			else {
				java.awt.Toolkit.getDefaultToolkit().beep();
			}
		}
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(true);
//		String[] blockListCache = new String[this.invisibleIdList.size()];
//		for (int i = 0; i < this.invisibleIdList.size(); ++i) {
//			blockListCache[i] = this.invisibleIdList.get(i);
//		}
//		modInstance.blockList = blockListCache;
//		modInstance.saveBlockList(modInstance.currentBlocklistName);
	}
	
	@Override
	public void drawScreen(int x, int y, float par3) {
		//drawDefaultBackground();
		//super.drawScreen(x, y, par3);
		int width = this.width;
		int height = this.height;
		drawBackground(0);

//		int left = this.width / 2 + 100;
//		int right = this.width / 2 - 100;
//		int iconLeft = this.width / 2 - 97;
//		int titleCenter = this.width / 2;
		int iconLeft = 3;
		int titleCenter = 100;
		
		int left = 0;
		int right = 200;
		
		int heightCalc = this.height / 3;
		int renderPosition = this.listPos - 4;
		int currentPos1Calc = heightCalc + 12 + (24 * 2);
		int currentPos2Calc = currentPos1Calc - 24;
		 GL11.glDisable(GL11.GL_LIGHTING);
		drawRect(left + 2, currentPos1Calc, right - 2, currentPos2Calc, -4144960);
		
		this.mc.getTextureManager().bindTexture(inventory);
		this.drawTexturedModalRect(this.width/2+18, 96, 6, 80, 164, 82);		
		 GL11.glEnable(GL11.GL_LIGHTING);
		for (int i = 0; i < 40; ++i) {
			if (renderPosition >= 0 && renderPosition < this.recipeList.size()) {
				final IRecipe recipe = this.recipeList.get(renderPosition);
				if( renderPosition == this.listPos){
					//System.out.println("Recipe : "+recipe);
					if (recipe instanceof ShapedRecipes) {
						final ShapedRecipes sRecipe = (ShapedRecipes) recipe;
						
						final int recipeSize = sRecipe.getRecipeSize();
						final String data=String.format("%02d %s", recipeSize,"true");
						drawCenteredString(this.fontRendererObj, data, (this.width/2)+120, 80, 16777215);
						
						int icLeft=(this.width/2)+20;
						int icTop=100;
						int idx=0;
						for(Object obj:sRecipe.recipeItems){
							renderRecipeComponent(calcPoint((this.width/2)+20, 100,idx,recipeSize,sRecipe.getRecipeOutput()), obj);
							idx++;
						}
					}
					if (recipe instanceof ShapedOreRecipe) {
						final ShapedOreRecipe sRecipe = (ShapedOreRecipe) recipe;
						
						final int recipeSize = sRecipe.getRecipeSize();
						final String data=String.format("Ore %02d %s", recipeSize,"true");
						drawCenteredString(this.fontRendererObj, data, (this.width/2)+120, 80, 16777215);
						
						int idx=0;
						for(Object obj:sRecipe.getInput()){
							renderRecipeComponent(calcPoint((this.width/2)+20, 100,idx,recipeSize,sRecipe.getRecipeOutput()), obj);
							idx++;
						}
					}
					if (recipe instanceof ShapelessOreRecipe) {
						final ShapelessOreRecipe sRecipe = (ShapelessOreRecipe) recipe;
					
						int recipeSize = sRecipe.getRecipeSize();
						final String data=String.format("Ore %02d %s", recipeSize,"false");
						drawCenteredString(this.fontRendererObj, data, (this.width/2)+120, 80, 16777215);
						
						int idx=0;
						for(Object obj:sRecipe.getInput()){
							renderRecipeComponent(calcPoint((this.width/2)+20, 100,idx,recipeSize,sRecipe.getRecipeOutput()), obj);
							idx++;
						}
					}
					if (recipe instanceof ShapelessRecipes) {
						final ShapelessRecipes sRecipe = (ShapelessRecipes) recipe;
						
						int recipeSize = sRecipe.getRecipeSize();
						final String data=String.format("%02d %s", recipeSize,"false");
						drawCenteredString(this.fontRendererObj, data, (this.width/2)+120, 80, 16777215);
						
						int idx=0;
						for(Object obj:sRecipe.recipeItems){
							renderRecipeComponent(calcPoint((this.width/2)+20, 100,idx,recipeSize,sRecipe.getRecipeOutput()), obj);
							idx++;
						}
					}
				}
				
				final ItemStack currentIcon = recipe.getRecipeOutput();
//				final Block block = (Block) (Block.blockRegistry.getObject(objectId));
				if( currentIcon!=null){
					final Item item = currentIcon.getItem();
					
					drawRect(left+3, heightCalc + 10 - 48 + i * 24, right-3, heightCalc - 10 - 48 + i * 24, rgba(.30f, .30f, .30f,1.0f));
					if ((item != null) /*&& (RenderBlocks.renderItemIn3d(blockFromItem.getRenderType()))*/) {
					
						renderItem(iconLeft, heightCalc - 60 + 4 + i * 24,  currentIcon);
											
					}
//					final Block block= Block.getBlockFromItem(item);
//					
//					String currentBlockName = block.getLocalizedName();
					String currentBlockName =item.getUnlocalizedName(currentIcon);
					
					if (currentBlockName == null) {
						currentBlockName = "No Name";
					}else  if( StatCollector.canTranslate(currentBlockName+".name")){
					    currentBlockName = StatCollector.translateToLocal(currentBlockName+".name");					     
					}else {
					    currentBlockName = StatCollector.translateToFallback(currentBlockName+".name");
					}
//					
					drawCenteredString(this.fontRendererObj, currentBlockName, titleCenter, heightCalc - 60 + 7 + i * 24, 16777215);
				}
//				itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), currentIcon, iconLeft, heightCalc - 60 + 4 + i * 24);
//				if (this.invisibleIdList.indexOf(objectId) >= 0) {
					
//				}
				


			}
			
//			if (renderPosition >= 0 && renderPosition < this.idList.size()) {
//				final String objectId = this.idList.get(renderPosition);
//				final Block block = (Block) (Block.blockRegistry.getObject(objectId));
//				final ItemStack currentIcon = new ItemStack(block);
//				final Item item = currentIcon.getItem();
//				final Block blockFromItem = Block.getBlockFromItem(item);
//				if ((item != null) /*&& (RenderBlocks.renderItemIn3d(blockFromItem.getRenderType()))*/) {
//				
//					GL11.glEnable(GL11.GL_LIGHTING);
//				    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//				        
//					itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), currentIcon, iconLeft, heightCalc - 60 + 4 + i * 24);
//					itemRender.renderItemOverlayIntoGUI(fontRendererObj, this.mc.getTextureManager(), currentIcon, iconLeft, heightCalc - 60 + 4 + i * 24);
//					GL11.glDisable(GL11.GL_LIGHTING);
//				}
////				itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), currentIcon, iconLeft, heightCalc - 60 + 4 + i * 24);
//				if (this.invisibleIdList.indexOf(objectId) >= 0) {
//					drawRect(left, heightCalc + 10 - 48 + i * 24, right, heightCalc - 10 - 48 + i * 24, -65536);
//				}
//				String currentBlockName = block.getLocalizedName();
//				if (currentBlockName == null) {
//					currentBlockName = "No Name";
//				}
//				
//				drawCenteredString(this.fontRendererObj, currentBlockName, titleCenter, heightCalc - 60 + 7 + i * 24, 16777215);
//
//
//			}
			++renderPosition;
		}
		super.drawScreen(x, y, par3);
	}

	private Point calcPoint(int left, int top, int idx, int recipeSize, ItemStack itemStack) {
		final Item item = itemStack.getItem();
		int iconSize = 18;
		final String name = item.getUnlocalizedName();
		switch(recipeSize){
		case 1:
			return new Point(left+iconSize,top+iconSize);
		case 2:
			if( item instanceof ItemCloth){
				return new Point(left + (idx % 2)*iconSize,top+iconSize);
			}
			if( item instanceof ItemBlock || item instanceof ItemMinecart){
				System.out.println(" 2-Block==> "+(item==null?"<NULL>":item.getClass())+":"+name+":"+itemStack);
				if( horizontalBlocks.contains(name)){
					return new Point(left + (idx % 2)*iconSize,top+iconSize);
				}
				return new Point(left + iconSize,top+iconSize+(idx % 2)*iconSize);
			}
			System.out.println(" 2==> "+(item==null?"<NULL>":item.getClass())+":"+name+":"+itemStack);
			return new Point(left + (idx % 2)*iconSize,top+iconSize);
		case 3:		
			System.out.println(" 3==> "+(item==null?"<NULL>":item.getClass())+":"+item+":"+itemStack);
			if( item instanceof ItemTool || item instanceof ItemSword){
				return new Point(left + iconSize,top+(idx % 3)*iconSize);			
			}else {
				return new Point(left + (idx % 3)*iconSize,top+40);
			}
		case 4:
			return new Point(left + (idx % 2)*iconSize,top+iconSize+(idx / 2)*iconSize);
		case 9:
			return new Point(left + (idx % 3)*iconSize,top+(idx / 3)*iconSize);
		case 6:
			System.out.println(" 6==> "+(item==null?"<NULL>":item.getClass())+":"+item+":"+itemStack);
			if( item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemDoor){
				return new Point(left + (idx %2 )*iconSize,top+(idx / 2)*iconSize);
			}else{
				return new Point(left + (idx % 3)*iconSize,top+iconSize+(idx / 3)*iconSize);
			}
		}
		return new Point(0,0);
	}

	private void renderRecipeComponent(Point p, Object obj) {
		
		if( obj instanceof ItemStack){
			ItemStack iStack=(ItemStack) obj;
			renderItem(p.x, p.y, iStack);
		}else	if( obj instanceof Item){
			ItemStack iStack=new ItemStack((Item) obj);
			renderItem(p.x, p.y, iStack);
		} else if(obj instanceof Collection<?>){
			//System.out.print("     :> ");
			for( Object sub:((Collection<?>)obj)){
				renderRecipeComponent(p, sub);
			}
			//System.out.println();
		}
	}

	private void renderItem(int iconLeft, int iconTop, final ItemStack currentIcon) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
        itemRender.zLevel = 200.0F;
//
        
//
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		 GL11.glEnable(GL11.GL_DEPTH_TEST);
		 
		 
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), currentIcon, iconLeft, iconTop);
		itemRender.renderItemOverlayIntoGUI(fontRendererObj, this.mc.getTextureManager(), currentIcon, iconLeft, iconTop);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
	}

	public int rgba(float red,float green, float blue, float alpha){
		int redInt = Math.round(red*255) & 255; 
		int greenInt = Math.round(green*255) & 255;
		int blueInt = Math.round(blue*255) & 255;
		int alphaInt = Math.round(alpha*255) & 255;
		return redInt<<24 | greenInt<<16 | blueInt<<8 |  alphaInt;		
	}
}
