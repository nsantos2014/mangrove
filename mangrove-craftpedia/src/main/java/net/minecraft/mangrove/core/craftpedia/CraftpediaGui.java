package net.minecraft.mangrove.core.craftpedia;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
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
import net.minecraft.mangrove.core.craftpedia.renderer.ShapedOreCRR;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class CraftpediaGui extends GuiScreen {
	protected static final ResourceLocation	                  inventory	= new ResourceLocation("textures/gui/container/inventory.png");
	// private static final Set<String> verticalBlocks=new HashSet<>();
	// private static final Set<String> horizontalBlocks=new HashSet<>();
	// static{
	// verticalBlocks.add("item.magmaCream");
	// //verticalBlocks.add("item.eyeOfEnder");ItemEnderEye
	// horizontalBlocks.add("");
	//
	// }
	FontRenderer	                                          render;
	private GuiButton	                                      exit;

	// private List<String> idList = new ArrayList<String>();
	// private List<String> invisibleIdList = new ArrayList<String>();
	private int	                                              listPos	= 0;

	private GuiTextField	                                  searchbar;
	private EnumMap<EnumRecipeType, CraftpediaRecipeRenderer>	renderers;

	private static final Point[]	                          slotPos	= new Point[] {

	                                                                    };

	public CraftpediaGui() {
		super();
		this.renderers = new EnumMap<EnumRecipeType, CraftpediaRecipeRenderer>(EnumRecipeType.class);
		this.renderers.put(EnumRecipeType.MirroredShapedOreRecipe, new ShapedOreCRR());
		this.renderers.put(EnumRecipeType.NonMirroredShapedOreRecipe, new ShapedOreCRR());

	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		super.setWorldAndResolution(mc, width, height);
		for (CraftpediaRecipeRenderer renderer : this.renderers.values()) {
			renderer.setWorldAndResolution(mc, width, height);
		}
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

		this.searchbar = new GuiTextField(1, this.fontRendererObj, 4, 10, 196, this.fontRendererObj.FONT_HEIGHT+5);
		this.searchbar.setMaxStringLength(30);
		this.searchbar.setCanLoseFocus(false);
		this.searchbar.setFocused(true);
		this.searchbar.setTextColor(16777215);

	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		switch (par1GuiButton.id) {
		case 0:
			Minecraft.getMinecraft().displayGuiScreen(null);
			break;
		default:
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int mouseScrollWheelStatus = Mouse.getEventDWheel();
		int mouseButtonStatus = Mouse.getEventButton();
		boolean mouseButtonState = Mouse.getEventButtonState();
		if ((mouseScrollWheelStatus > 0) && (this.listPos > 0)) {
			--this.listPos;
		} else if ((mouseScrollWheelStatus < 0) && (this.listPos < Craftpedia.instance.size() - 1)) {
			++this.listPos;
		}
		if ((mouseButtonState) && (mouseButtonStatus != -1)) {
			// if ((this.invisibleIdList.indexOf(this.idList.get(this.listPos))
			// >= 0)) {
			// this.invisibleIdList.remove(this.idList.get(this.listPos));
			// }
			// else {
			// this.invisibleIdList.add(this.idList.get(this.listPos));
			// }
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {

		super.keyTyped(typedChar, keyCode);

		switch (keyCode) {
		case Keyboard.KEY_ESCAPE:
			break;
		case Keyboard.KEY_UP:
			if (this.listPos > 0) {
				listPos--;
			}
			break;
		case Keyboard.KEY_DOWN:
			if (this.listPos < Craftpedia.instance.size() - 1) {
				listPos++;
			}
			break;
		case Keyboard.KEY_HOME:
			listPos = 0;
			break;
		case Keyboard.KEY_END:
			this.listPos = Craftpedia.instance.size() - 1;
			break;
		default:
			if (this.searchbar.isFocused()) {
				this.searchbar.textboxKeyTyped(typedChar, keyCode);
				System.out.println("What to search:" + this.searchbar.getText());
				int found = Craftpedia.instance.find(this.searchbar.getText(), this.listPos);
				if (found >= 0) {
					this.listPos = found;
				} else if (this.listPos > 0) {
					found = Craftpedia.instance.find(this.searchbar.getText(), -this.listPos);

					if (found >= 0) {
						this.listPos = found;
					}
				}
			}
			break;
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	public void drawScreen(int x, int y, float par3) {
		// drawDefaultBackground();
		// super.drawScreen(x, y, par3);
		int width = this.width;
		int height = this.height;
		drawBackground(0);

		this.searchbar.drawTextBox();

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
		this.drawTexturedModalRect(this.width / 2 + 18, 96, 6, 80, 164, 82);
		// GL11.glEnable(GL11.GL_LIGHTING);
		for (int i = 0; i < 20; ++i) {
			if (renderPosition >= 0 && renderPosition < Craftpedia.instance.size()) {

				CraftpediaRecipe craftpediaRecipe = Craftpedia.instance.get(renderPosition);

				final IRecipe recipe = craftpediaRecipe.getRecipe();
				if (renderPosition == this.listPos) {
					onSelection(craftpediaRecipe);
				}

				final ItemStack currentIcon = recipe.getRecipeOutput();
				// final Block block = (Block)
				// (Block.blockRegistry.getObject(objectId));
				if (currentIcon != null) {
					final Item item = currentIcon.getItem();

					drawRect(left + 3, heightCalc + 10 - 48 + i * 24, right - 3, heightCalc - 10 - 48 + i * 24, rgba(.30f, .30f, .30f, 1.0f));
					if ((item != null)) {
						renderItem(iconLeft, heightCalc - 60 + 4 + i * 24, currentIcon);
					}
					drawCenteredString(this.fontRendererObj, craftpediaRecipe.getOutputName(), titleCenter, heightCalc - 60 + 7 + i * 24, 16777215);
				}

			}
			++renderPosition;
		}

		super.drawScreen(x, y, par3);
	}

	private void onSelection(final CraftpediaRecipe craftpediaRecipe) {
		// System.out.println("Recipe : "+recipe);
		CraftpediaRecipeRenderer renderer = this.renderers.get(craftpediaRecipe.getType());

		if (renderer != null) {
			renderer.renderRecipe(craftpediaRecipe);
			return;
		}
		switch (craftpediaRecipe.getType()) {
		case ShapedRecipes:
			onSelectionShappedRecipes((ShapedRecipes) craftpediaRecipe.getRecipe());
			break;
		case ShapelessRecipes:
			onSelectionShapelessRecipes((ShapelessRecipes) craftpediaRecipe.getRecipe());
			break;
		case ShapelessOreRecipe:
			onSelectionShapelessOreRecipe((ShapelessOreRecipe) craftpediaRecipe.getRecipe());
			break;
		case MirroredShapedOreRecipe:
			onSelectionMirroredShapedOreRecipe((ShapedOreRecipe) craftpediaRecipe.getRecipe());
			break;
		case NonMirroredShapedOreRecipe:
			onSelectionNonMirroredShapedOreRecipe((ShapedOreRecipe) craftpediaRecipe.getRecipe());
			break;
		default:
			break;
		}

	}

	private void onSelectionMirroredShapedOreRecipe(final ShapedOreRecipe sRecipe) {
		final int recipeSize = sRecipe.getRecipeSize();
		final String data = String.format("Ore %02d %s", recipeSize, "true");
		drawCenteredString(this.fontRendererObj, data, (this.width / 2) + 120, 80, 16777215);

		int idx = 0;
		for (Object obj : sRecipe.getInput()) {
			renderRecipeComponent(calcPoint((this.width / 2) + 20, 100, idx, recipeSize, sRecipe.getRecipeOutput()), obj);
			idx++;
		}
	}

	private void onSelectionNonMirroredShapedOreRecipe(final ShapedOreRecipe sRecipe) {
		final int recipeSize = sRecipe.getRecipeSize();
		final String data = String.format("Ore %02d %s", recipeSize, "true");
		drawCenteredString(this.fontRendererObj, data, (this.width / 2) + 120, 80, 16777215);

		int idx = 0;
		for (Object obj : sRecipe.getInput()) {
			renderRecipeComponent(calcPoint((this.width / 2) + 20, 100, idx, recipeSize, sRecipe.getRecipeOutput()), obj);
			idx++;
		}
	}

	private void onSelectionShapelessRecipes(final ShapelessRecipes sRecipe) {
		int recipeSize = sRecipe.getRecipeSize();
		final String data = String.format("%02d %s", recipeSize, "false");
		drawCenteredString(this.fontRendererObj, data, (this.width / 2) + 120, 80, 16777215);

		int idx = 0;
		for (Object obj : sRecipe.recipeItems) {
			renderRecipeComponent(calcPoint((this.width / 2) + 20, 100, idx, recipeSize, sRecipe.getRecipeOutput()), obj);
			idx++;
		}
	}

	private void onSelectionShapelessOreRecipe(final ShapelessOreRecipe sRecipe) {
		int recipeSize = sRecipe.getRecipeSize();
		final String data = String.format("Ore %02d %s", recipeSize, "false");
		drawCenteredString(this.fontRendererObj, data, (this.width / 2) + 120, 80, 16777215);

		int idx = 0;
		for (Object obj : sRecipe.getInput()) {
			renderRecipeComponent(calcPoint((this.width / 2) + 20, 100, idx, recipeSize, sRecipe.getRecipeOutput()), obj);
			idx++;
		}
	}

	private void onSelectionShappedRecipes(final ShapedRecipes sRecipe) {
		final int recipeSize = sRecipe.getRecipeSize();
		final String data = String.format("%02d %s", recipeSize, "true");
		drawCenteredString(this.fontRendererObj, data, (this.width / 2) + 120, 80, 16777215);

		int icLeft = (this.width / 2) + 20;
		int icTop = 100;
		int idx = 0;
		for (Object obj : sRecipe.recipeItems) {
			renderRecipeComponent(calcPoint((this.width / 2) + 20, 100, idx, recipeSize, sRecipe.getRecipeOutput()), obj);
			idx++;
		}
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

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		itemRender.renderItemAndEffectIntoGUI(currentIcon, iconLeft, iconTop);
		itemRender.renderItemOverlayIntoGUI(fontRendererObj, currentIcon, iconLeft, iconTop, "");

		GL11.glDisable(GL11.GL_LIGHTING);
		itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	public int rgba(float red, float green, float blue, float alpha) {
		int redInt = Math.round(red * 255) & 255;
		int greenInt = Math.round(green * 255) & 255;
		int blueInt = Math.round(blue * 255) & 255;
		int alphaInt = Math.round(alpha * 255) & 255;
		return redInt << 24 | greenInt << 16 | blueInt << 8 | alphaInt;
	}
}
