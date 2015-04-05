package net.minecraft.mangrove.core.craftpedia;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Util {
	public static final Util	i	= new Util();
	private Field	         mirrorField;

	public Util() {
		try {
			this.mirrorField = ShapedOreRecipe.class.getDeclaredField("mirrored");
			this.mirrorField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isMirrored(IRecipe recipe) {
		if (recipe instanceof ShapedOreRecipe) {
			try {
				return mirrorField.getBoolean(recipe);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public Map<Item, CRBomItem> resolveBOM(final Object[] bomArray) {
		final Map<Item, CRBomItem> bom = new LinkedHashMap<Item, CRBomItem>();
		for (Object bomData : bomArray) {
			resolveBomItem(bom, bomData);
		}
		return bom;
	}

	private void resolveBomItem(final Map<Item, CRBomItem> bom, Object bomData) {
		if (bomData instanceof ItemStack) {
			ItemStack iStack = (ItemStack) bomData;
			Item item = iStack.getItem();
			CRBomItem bomItem = bom.get(item);
			
			if (bomItem == null) {
				if( item.getHasSubtypes() && iStack.getMetadata()==OreDictionary.WILDCARD_VALUE){
					List<ItemStack> subItems=new ArrayList<>();
					item.getSubItems(item, null, subItems);
					bom.put(item, CRBomItem.of(item,subItems));
				}else{
					bom.put(item, CRBomItem.of(item,Arrays.asList(iStack)));
				}
			} else {
				if(  item.getHasSubtypes()&& iStack.getMetadata()==OreDictionary.WILDCARD_VALUE){
					bomItem.incAll();
				}else if (item.getHasSubtypes()){
					bomItem.inc(iStack.getMetadata());
				}else{
					bomItem.inc();
				}
			}
		} else if (bomData instanceof Item) {
			Item item = (Item) bomData;
			ItemStack iStack = new ItemStack(item);
			CRBomItem bomItem = bom.get(item);
			if (bomItem == null) {
				bom.put(item, CRBomItem.of(item,Arrays.asList(iStack)));
			} else {
				bomItem.inc();
			}
		} else if (bomData instanceof Collection<?>) {
			for (Object bomSub : (Collection<?>) bomData) {
				resolveBomItem(bom, bomSub);
			}
		}
	}

	public static String resolveItemName(final ItemStack iStack) {
		final Item item = iStack.getItem();

		String name = item.getUnlocalizedName(iStack);

		if (name == null) {
			name = "No Name";
		} else if (StatCollector.canTranslate(name + ".name")) {
			name = StatCollector.translateToLocal(name + ".name");
		} else {
			name = StatCollector.translateToFallback(name + ".name");
		}
		return name;
	}

	public CRPattern resolvePattern(int recipeSize, Object[] bomArray) {
		final CRPattern pattern = new CRPattern();
		if (recipeSize != 1 && recipeSize != 4 && recipeSize != 9) {
			return pattern;
		}
		int rowSize;
		switch (recipeSize) {
		case 9:
			rowSize = 3;
			pattern.setDimension(3, 3);
			break;
		case 4:
			rowSize = 2;
			pattern.setDimension(2, 2);
			break;
		default:
			rowSize = 1;
			pattern.setDimension(1, 1);
			break;
		}
		int idx = 0;
		for (Object bomData : bomArray) {
			List<ItemStack> items = resolvePatternItems(bomData);
			pattern.addCell(idx / rowSize, idx % rowSize, items);
			idx++;
		}
		return pattern;
	}

	private List<ItemStack> resolvePatternItems(Object bomData) {
		if (bomData instanceof ItemStack) {
			ItemStack iStack = (ItemStack) bomData;
			if( iStack.getHasSubtypes() && iStack.getMetadata() == OreDictionary.WILDCARD_VALUE){
				List<ItemStack> subItems=new ArrayList<ItemStack>();
				Item item = iStack.getItem();
				item.getSubItems(item, null, subItems);
				return subItems;
			}
			return Arrays.asList(iStack);
		} else if (bomData instanceof Item) {
			Item item = (Item) bomData;
			return Arrays.asList(new ItemStack(item));
		} else if (bomData instanceof Collection<?>) {
			final List<ItemStack> items = new ArrayList<ItemStack>();
			for (Object bomSub : (Collection<?>) bomData) {
				items.addAll(resolvePatternItems(bomSub));
			}
			return items;
		}
		return Collections.emptyList();
	}
}
