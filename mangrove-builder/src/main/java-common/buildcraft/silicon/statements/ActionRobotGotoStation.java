/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.silicon.statements;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.util.ForgeDirection;

import buildcraft.api.core.BlockIndex;
import buildcraft.api.gates.ActionParameterItemStack;
import buildcraft.api.gates.IActionParameter;
import buildcraft.api.gates.IGate;
import buildcraft.api.robots.AIRobot;
import buildcraft.core.ItemMapLocation;
import buildcraft.core.robots.AIRobotGoAndLinkToDock;
import buildcraft.core.robots.DockingStation;
import buildcraft.core.robots.EntityRobot;
import buildcraft.core.robots.RobotRegistry;
import buildcraft.core.triggers.BCActionActive;
import buildcraft.core.utils.StringUtils;
import buildcraft.transport.Pipe;
import buildcraft.transport.TileGenericPipe;

public class ActionRobotGotoStation extends BCActionActive {

	public ActionRobotGotoStation() {
		super("buildcraft:robot.goto_station");
	}

	@Override
	public String getDescription() {
		return StringUtils.localize("gate.action.robot.goto_station");
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icon = iconRegister.registerIcon("buildcraft:triggers/action_robot_goto_station");
	}

	@Override
	public void actionActivate(IGate gate, IActionParameter[] parameters) {
		Pipe<?> pipe = (Pipe<?>) gate.getPipe();
		TileGenericPipe tile = pipe.container;
		RobotRegistry registry = RobotRegistry.getRegistry(pipe.getWorld());

		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
			DockingStation station = tile.getStation(d);

			if (station != null && station.robotTaking() != null) {
				EntityRobot robot = (EntityRobot) station.robotTaking();
				AIRobot ai = robot.getOverridingAI();

				if (ai != null) {
					continue;
				}

				DockingStation newStation = station;

				if (parameters[0] != null) {
					ActionParameterItemStack stackParam = (ActionParameterItemStack) parameters[0];
					ItemStack item = stackParam.getItemStackToDraw();

					if (item.getItem() instanceof ItemMapLocation) {
						BlockIndex index = ItemMapLocation.getBlockIndex(item);

						if (index != null) {
							ForgeDirection side = ItemMapLocation.getSide(item);
							DockingStation paramStation = (DockingStation)
									registry.getStation(index.x,
									index.y, index.z, side);

							if (paramStation != null) {
								newStation = paramStation;
							}
						}
					}
				}

				robot.overrideAI(new AIRobotGoAndLinkToDock(robot, newStation));
			}
		}
	}

	@Override
	public int maxParameters() {
		return 1;
	}

	@Override
	public IActionParameter createParameter(int index) {
		return new ActionParameterItemStack();
	}

}
