/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.inventory.filters;

import net.minecraftforge.fluids.Fluid;

/**
 * Returns true if the stack matches any one one of the filter stacks.
 */
public class PassThroughFluidFilter implements IFluidFilter {

	@Override
	public boolean matches(Fluid fluid) {
		return fluid != null;
	}

}
