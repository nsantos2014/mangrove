/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package net.minecraft.mangrove.core.craftpedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.lwjgl.input.Keyboard;

public class Craftpedia {
	public static final Craftpedia instance = new Craftpedia();

	private final List<CraftpediaRecipe> recipeList = new ArrayList<>();
	private final List<String> nameList=new ArrayList<String>();

	private ExecutorService executorService;

	private Future<Integer> lastSubmit;
	
	private Craftpedia() {
		 executorService = Executors.newFixedThreadPool(1);
		final Iterator it = CraftingManager.getInstance().getRecipeList().iterator();
		while (it.hasNext()) {
			final IRecipe recipe = (IRecipe) it.next();
			if( recipe.getRecipeOutput()==null){
				System.out.println("Found a null receipt : "+recipe);
			}else{
				CraftpediaRecipe craftpediaRecipe = CraftpediaRecipe.of(recipe);
				this.nameList.add(craftpediaRecipe.getOutputName());
				this.recipeList.add(craftpediaRecipe);
			}
		}
	}

	public int size() {
		return this.recipeList.size();
	}

	public CraftpediaRecipe get(int renderPosition) {
		return this.recipeList.get(renderPosition);
	}

	public int find(String text,int fromPos) {
		if( lastSubmit!=null){
			lastSubmit.cancel(true);
		}
		try {
			lastSubmit = this.executorService.submit(new SearchCallable(text,fromPos));
			return lastSubmit.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	private class SearchCallable implements Callable<Integer>{

		private String text;
		private int fromIndex;
		private int toIndex;

		public SearchCallable(final String text, int fromPos) {
			this.text = text;
			this.fromIndex = fromPos<0?0:fromPos;
			this.toIndex=fromPos<0?-fromPos: nameList.size();
		}

		@Override
		public Integer call() throws Exception {
			
			int i=fromIndex;
			for(final String name:nameList.subList(fromIndex, toIndex)){
				if(name.startsWith(text)){
					System.out.println("Found : '"+name+"' at "+i);
					return i;
				}
				i++;
			}
			return -1;
		}
		
	}
}