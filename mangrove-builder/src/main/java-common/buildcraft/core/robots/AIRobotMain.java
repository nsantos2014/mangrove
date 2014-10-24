/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.robots;

import buildcraft.api.robots.AIRobot;
import buildcraft.api.robots.EntityRobotBase;

public class AIRobotMain extends AIRobot {

	private AIRobot overridingAI;

	public AIRobotMain(EntityRobotBase iRobot) {
		super(iRobot);
	}

	@Override
	public void preempt(AIRobot ai) {
		if (!(ai instanceof AIRobotRecharge)) {
			if (robot.getEnergy() < EntityRobotBase.MAX_ENERGY / 4.0) {
				startDelegateAI(new AIRobotRecharge(robot));
			} else if (overridingAI != null && ai != overridingAI) {
				startDelegateAI(overridingAI);
			}
		}
	}

	@Override
	public void update() {
		AIRobot board = robot.getBoard();

		if (board != null) {
			startDelegateAI(board);
		}
	}

	@Override
	public void delegateAIEnded(AIRobot ai) {
		if (ai == overridingAI) {
			overridingAI = null;
		}
	}

	public void setOverridingAI(AIRobot ai) {
		if (overridingAI == null) {
			overridingAI = ai;
		}
	}

	public AIRobot getOverridingAI() {
		return overridingAI;
	}

	@Override
	public boolean canLoadFromNBT() {
		return true;
	}
}
