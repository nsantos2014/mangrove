/* Copyright (c) 2014, Julian Uy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package net.minecraft.mangrove.core.xray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class XrayModChooserGui extends GuiScreen {
	List<String> idList = new ArrayList<String>();
	List<String> invisibleIdList = new ArrayList<String>();
	int listPos = 0;

	public XrayModChooserGui() {
        @SuppressWarnings("unchecked")
		Iterator<Block> blockIterator = Block.blockRegistry.iterator();

        while (blockIterator.hasNext())
        {
        	Block currentBlock = blockIterator.next();
        	this.idList.add(Block.blockRegistry.getNameForObject(currentBlock));
        }
	}
	
	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		String[] blockListCache = XRayBlocks.instance.blockList;
		for (int i = 0; i < blockListCache.length; ++i) {
			this.invisibleIdList.add(blockListCache[i]);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		super.actionPerformed(par1GuiButton);
		if (par1GuiButton.id != 0) {
			this.invisibleIdList.add(this.idList.get(par1GuiButton.id));
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
		else if ((mouseScrollWheelStatus < 0) && (this.listPos < this.idList.size() - 1)) {
			++this.listPos;
		}
		if ((mouseButtonState) && (mouseButtonStatus != -1))  {
			if ((this.invisibleIdList.indexOf(this.idList.get(this.listPos)) >= 0)) {
				this.invisibleIdList.remove(this.idList.get(this.listPos));
			}
			else {
				this.invisibleIdList.add(this.idList.get(this.listPos));
			}
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		if (par2 != 1 && par2 != 14 && par2 != 29 && par2 != 157) {
			if (((par2 == 200) || (par1 == 'w') || (par1 == 'W')) && (this.listPos > 0)) {
				--this.listPos;
			}
			else if (((par2 == 208) || (par1 == 's') || (par1 == 'S')) && (this.listPos < this.idList.size() - 1)) {
				++this.listPos;
			}
			else if (par2 == 28) {
				if ((this.invisibleIdList.indexOf(this.idList.get(this.listPos)) >= 0)) {
					this.invisibleIdList.remove(this.idList.get(this.listPos));
				}
				else {
					this.invisibleIdList.add(this.idList.get(this.listPos));
				}
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
		String[] blockListCache = new String[this.invisibleIdList.size()];
		for (int i = 0; i < this.invisibleIdList.size(); ++i) {
			blockListCache[i] = this.invisibleIdList.get(i);
		}
		XRayBlocks.instance.blockList = blockListCache;
		XRayBlocks.instance.saveBlockList(XRayBlocks.instance.currentBlocklistName);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		int widthPlus100 = this.width / 2 + 100;
		int widthMinus100 = this.width / 2 - 100;
		int heightCalc = this.height / 3;
		int renderPosition = this.listPos - 4;
		int currentPos1Calc = heightCalc + 12 + (24 * 2);
		int currentPos2Calc = currentPos1Calc - 24;
		drawRect(widthPlus100 + 2, currentPos1Calc, widthMinus100 - 2, currentPos2Calc, -4144960);
		for (int i = 0; i < 40; ++i) {
			if (renderPosition >= 0 && renderPosition < this.idList.size()) {
				Block block = (Block) (Block.blockRegistry.getObject(this.idList.get(renderPosition)));
				ItemStack currentIcon = new ItemStack(block);
				if ((currentIcon.getItem() != null) && (RenderBlocks.renderItemIn3d(Block.getBlockFromItem(currentIcon.getItem()).getRenderType()))) {
					itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.getTextureManager(), currentIcon, this.width / 2 - 97, heightCalc - 60 + 4 + i * 24);
				}
				if (this.invisibleIdList.indexOf((this.idList.get(renderPosition))) >= 0) {
					drawRect(widthPlus100, heightCalc + 10 - 48 + i * 24, widthMinus100, heightCalc - 10 - 48 + i * 24, -65536);
				}
				String currentBlockName = block.getLocalizedName();
				if (currentBlockName == null) {
					currentBlockName = "No Name";
				}
				drawCenteredString(this.fontRendererObj, currentBlockName, this.width / 2, heightCalc - 60 + 7 + i * 24, 16777215);


			}
			++renderPosition;
		}
		super.drawScreen(par1, par2, par3);
	}

}
