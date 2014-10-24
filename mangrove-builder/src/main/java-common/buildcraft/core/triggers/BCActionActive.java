/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.triggers;

import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionParameter;

public abstract class BCActionActive extends BCStatement implements IAction {

	public BCActionActive(String... uniqueTag) {
		super(uniqueTag);
	}

	@Override
	public IActionParameter createParameter(int index) {
		return null;
	}
}
