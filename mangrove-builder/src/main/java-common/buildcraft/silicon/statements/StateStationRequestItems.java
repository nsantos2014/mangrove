/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.silicon.statements;

import java.util.LinkedList;

import net.minecraft.item.ItemStack;

import buildcraft.api.gates.ActionState;
import buildcraft.core.inventory.filters.IStackFilter;

public class StateStationRequestItems extends ActionState {

	LinkedList<ItemStack> items;

	public StateStationRequestItems(LinkedList<ItemStack> filter) {
		items = filter;
	}

	public boolean matches(IStackFilter filter) {
		if (items.size() == 0) {
			return true;
		} else {
			for (ItemStack stack : items) {
				if (filter.matches(stack)) {
					return true;
				}
			}
		}

		return false;
	}

}
