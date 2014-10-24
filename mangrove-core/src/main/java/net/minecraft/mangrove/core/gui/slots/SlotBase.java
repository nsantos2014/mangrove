/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package net.minecraft.mangrove.core.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.mangrove.core.gui.tooltip.IToolTipProvider;
import net.minecraft.mangrove.core.gui.tooltip.ToolTip;

public class SlotBase extends Slot implements IToolTipProvider {

	private ToolTip toolTips;

	public SlotBase(IInventory iinventory, int slotIndex, int posX, int posY) {
		super(iinventory, slotIndex, posX, posY);
	}
	
	public void hide(){
		if(xDisplayPosition<6000){
			xDisplayPosition=6000+xDisplayPosition;
		}
		if(yDisplayPosition<6000){
			yDisplayPosition=6000+yDisplayPosition;
		}
	}
	public void show(){
		if( xDisplayPosition>6000){
			xDisplayPosition=xDisplayPosition-6000;
		}
		if( yDisplayPosition>6000){
			yDisplayPosition=yDisplayPosition-6000;
		}
	}
	public boolean isVisible(){
		return xDisplayPosition<6000 && yDisplayPosition<6000;
	}
	
	public boolean canShift() {
		return true;
	}

	/**
	 * @return the toolTips
	 */
	@Override
	public ToolTip getToolTip() {
		return toolTips;
	}

	/**
	 * @param toolTips the tooltips to set
	 */
	public void setToolTips(ToolTip toolTips) {
		this.toolTips = toolTips;
	}

	@Override
	public boolean isToolTipVisible() {
		return getStack() == null;
	}

	@Override
	public boolean isMouseOver(int mouseX, int mouseY) {
		return mouseX >= xDisplayPosition && mouseX <= xDisplayPosition + 16 && mouseY >= yDisplayPosition && mouseY <= yDisplayPosition + 16;
	}
}
