/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.robots;

import net.minecraft.entity.item.EntityItem;

import buildcraft.api.core.IInvSlot;
import buildcraft.api.robots.AIRobot;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.core.inventory.InventoryIterator;

public class AIRobotDisposeItems extends AIRobot {

	public AIRobotDisposeItems(EntityRobotBase iRobot) {
		super(iRobot);
	}

	@Override
	public void start () {
		startDelegateAI(new AIRobotGotoStationAndUnload(robot, robot.getZoneToWork()));
	}

	@Override
	public void delegateAIEnded(AIRobot ai) {
		if (ai instanceof AIRobotGotoStationAndUnload) {
			if (!ai.success()) {
				for (IInvSlot slot : InventoryIterator.getIterable(robot)) {
					if (slot.getStackInSlot() != null) {
						final EntityItem entity = new EntityItem(
								robot.worldObj,
								robot.posX,
								robot.posY,
								robot.posZ,
								slot.getStackInSlot());

						robot.worldObj.spawnEntityInWorld(entity);

						slot.setStackInSlot(null);
					}
				}
			} else if (robot.containsItems()) {
				startDelegateAI(new AIRobotGotoStationAndUnload(robot, robot.getZoneToWork()));
			}
		}
	}

}
