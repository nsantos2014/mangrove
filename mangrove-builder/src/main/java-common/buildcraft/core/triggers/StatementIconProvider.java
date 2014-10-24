/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.triggers;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import buildcraft.api.core.IIconProvider;

/**
 * Don't put new Trigger Icons in here please, put them in the Trigger classes
 * like the #TriggerClockTimer. This class will go away someday.
 */
public final class StatementIconProvider implements IIconProvider {

	public static StatementIconProvider INSTANCE = new StatementIconProvider();
	public static final int Action_MachineControl_On = 0;
	public static final int Action_MachineControl_Off = 1;
	public static final int Action_MachineControl_Loop = 2;
	public static final int Trigger_EngineHeat_Blue = 3;
	public static final int Trigger_EngineHeat_Green = 4;
	public static final int Trigger_EngineHeat_Yellow = 5;
	public static final int Trigger_EngineHeat_Red = 6;
	public static final int Trigger_Inventory_Empty = 7;
	public static final int Trigger_Inventory_Contains = 8;
	public static final int Trigger_Inventory_Space = 9;
	public static final int Trigger_Inventory_Full = 10;
	public static final int Trigger_FluidContainer_Empty = 11;
	public static final int Trigger_FluidContainer_Contains = 12;
	public static final int Trigger_FluidContainer_Space = 13;
	public static final int Trigger_FluidContainer_Full = 14;
	public static final int Trigger_Machine_Active = 15;
	public static final int Trigger_Machine_Inactive = 16;
	public static final int Trigger_PipeContents_Empty = 17;
	public static final int Trigger_PipeContents_ContainsItems = 18;
	public static final int Trigger_PipeContents_ContainsFluid = 19;
	public static final int Trigger_PipeContents_ContainsEnergy = 20;
	public static final int Trigger_PipeSignal_Red_Active = 21;
	public static final int Trigger_PipeSignal_Blue_Active = 22;
	public static final int Trigger_PipeSignal_Green_Active = 23;
	public static final int Trigger_PipeSignal_Yellow_Active = 24;
	public static final int Trigger_PipeSignal_Red_Inactive = 25;
	public static final int Trigger_PipeSignal_Blue_Inactive = 26;
	public static final int Trigger_PipeSignal_Green_Inactive = 27;
	public static final int Trigger_PipeSignal_Yellow_Inactive = 28;
	public static final int Trigger_RedstoneInput_Active = 29;
	public static final int Trigger_RedstoneInput_Inactive = 30;
	public static final int Trigger_PipeContents_RequestsEnergy = 31;
	public static final int Trigger_PipeContents_TooMuchEnergy = 32;
	public static final int Trigger_Inventory_Below25 = 33;
	public static final int Trigger_Inventory_Below50 = 34;
	public static final int Trigger_Inventory_Below75 = 35;
	public static final int Trigger_FluidContainer_Below25 = 36;
	public static final int Trigger_FluidContainer_Below50 = 37;
	public static final int Trigger_FluidContainer_Below75 = 38;

    public static final int Action_Parameter_Direction_Down = 39;
    public static final int Action_Parameter_Direction_Up = 40;
    public static final int Action_Parameter_Direction_North = 41;
    public static final int Action_Parameter_Direction_South = 42;
    public static final int Action_Parameter_Direction_West = 43;
    public static final int Action_Parameter_Direction_East = 44;


	public static final int MAX = 45;

	@SideOnly(Side.CLIENT)
	private final IIcon[] icons = new IIcon[MAX];

	private StatementIconProvider() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int iconIndex) {
		return icons[iconIndex];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		icons[StatementIconProvider.Action_MachineControl_On] = iconRegister.registerIcon("buildcraft:triggers/action_machinecontrol_on");
		icons[StatementIconProvider.Action_MachineControl_Off] = iconRegister.registerIcon("buildcraft:triggers/action_machinecontrol_off");
		icons[StatementIconProvider.Action_MachineControl_Loop] = iconRegister.registerIcon("buildcraft:triggers/action_machinecontrol_loop");

//		icons[ActionTriggerIconProvider.Trigger_EngineHeat_Blue] = iconRegister.registerIcon("buildcraft:triggers/trigger_engineheat_blue");
//		icons[ActionTriggerIconProvider.Trigger_EngineHeat_Green] = iconRegister.registerIcon("buildcraft:triggers/trigger_engineheat_green");
//		icons[ActionTriggerIconProvider.Trigger_EngineHeat_Yellow] = iconRegister.registerIcon("buildcraft:triggers/trigger_engineheat_yellow");
//		icons[ActionTriggerIconProvider.Trigger_EngineHeat_Red] = iconRegister.registerIcon("buildcraft:triggers/trigger_engineheat_red");
		icons[StatementIconProvider.Trigger_Inventory_Empty] = iconRegister.registerIcon("buildcraft:triggers/trigger_inventory_empty");
		icons[StatementIconProvider.Trigger_Inventory_Contains] = iconRegister.registerIcon("buildcraft:triggers/trigger_inventory_contains");
		icons[StatementIconProvider.Trigger_Inventory_Space] = iconRegister.registerIcon("buildcraft:triggers/trigger_inventory_space");
		icons[StatementIconProvider.Trigger_Inventory_Full] = iconRegister.registerIcon("buildcraft:triggers/trigger_inventory_full");
		icons[StatementIconProvider.Trigger_FluidContainer_Empty] = iconRegister.registerIcon("buildcraft:triggers/trigger_liquidcontainer_empty");
		icons[StatementIconProvider.Trigger_FluidContainer_Contains] = iconRegister.registerIcon("buildcraft:triggers/trigger_liquidcontainer_contains");
		icons[StatementIconProvider.Trigger_FluidContainer_Space] = iconRegister.registerIcon("buildcraft:triggers/trigger_liquidcontainer_space");
		icons[StatementIconProvider.Trigger_FluidContainer_Full] = iconRegister.registerIcon("buildcraft:triggers/trigger_liquidcontainer_full");
		icons[StatementIconProvider.Trigger_Machine_Active] = iconRegister.registerIcon("buildcraft:triggers/trigger_machine_active");
		icons[StatementIconProvider.Trigger_Machine_Inactive] = iconRegister.registerIcon("buildcraft:triggers/trigger_machine_inactive");
//		icons[ActionTriggerIconProvider.Trigger_PipeContents_Empty] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipecontents_empty");
//		icons[ActionTriggerIconProvider.Trigger_PipeContents_ContainsItems] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipecontents_containsitems");
//		icons[ActionTriggerIconProvider.Trigger_PipeContents_ContainsFluid] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipecontents_containsliquid");
//		icons[ActionTriggerIconProvider.Trigger_PipeContents_ContainsEnergy] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipecontents_containsenergy");
//		icons[ActionTriggerIconProvider.Trigger_PipeContents_RequestsEnergy] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipecontents_requestsenergy");
//		icons[ActionTriggerIconProvider.Trigger_PipeContents_TooMuchEnergy] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipecontents_toomuchenergy");
		icons[StatementIconProvider.Trigger_PipeSignal_Red_Active] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipesignal_red_active");
		icons[StatementIconProvider.Trigger_PipeSignal_Red_Inactive] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipesignal_red_inactive");
		icons[StatementIconProvider.Trigger_PipeSignal_Blue_Active] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipesignal_blue_active");
		icons[StatementIconProvider.Trigger_PipeSignal_Blue_Inactive] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipesignal_blue_inactive");
		icons[StatementIconProvider.Trigger_PipeSignal_Green_Active] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipesignal_green_active");
		icons[StatementIconProvider.Trigger_PipeSignal_Green_Inactive] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipesignal_green_inactive");
		icons[StatementIconProvider.Trigger_PipeSignal_Yellow_Active] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipesignal_yellow_active");
		icons[StatementIconProvider.Trigger_PipeSignal_Yellow_Inactive] = iconRegister.registerIcon("buildcraft:triggers/trigger_pipesignal_yellow_inactive");
		icons[StatementIconProvider.Trigger_RedstoneInput_Active] = iconRegister.registerIcon("buildcraft:triggers/trigger_redstoneinput_active");
		icons[StatementIconProvider.Trigger_RedstoneInput_Inactive] = iconRegister.registerIcon("buildcraft:triggers/trigger_redstoneinput_inactive");
		icons[StatementIconProvider.Trigger_Inventory_Below25] = iconRegister.registerIcon("buildcraft:triggers/trigger_inventory_below25");
		icons[StatementIconProvider.Trigger_Inventory_Below50] = iconRegister.registerIcon("buildcraft:triggers/trigger_inventory_below50");
		icons[StatementIconProvider.Trigger_Inventory_Below75] = iconRegister.registerIcon("buildcraft:triggers/trigger_inventory_below75");
		icons[StatementIconProvider.Trigger_FluidContainer_Below25] = iconRegister.registerIcon("buildcraft:triggers/trigger_liquidcontainer_below25");
		icons[StatementIconProvider.Trigger_FluidContainer_Below50] = iconRegister.registerIcon("buildcraft:triggers/trigger_liquidcontainer_below50");
		icons[StatementIconProvider.Trigger_FluidContainer_Below75] = iconRegister.registerIcon("buildcraft:triggers/trigger_liquidcontainer_below75");

		icons[StatementIconProvider.Action_Parameter_Direction_Down] = iconRegister.registerIcon("buildcraft:triggers/trigger_dir_down");
		icons[StatementIconProvider.Action_Parameter_Direction_Up] = iconRegister.registerIcon("buildcraft:triggers/trigger_dir_up");
		icons[StatementIconProvider.Action_Parameter_Direction_North] = iconRegister.registerIcon("buildcraft:triggers/trigger_dir_north");
		icons[StatementIconProvider.Action_Parameter_Direction_South] = iconRegister.registerIcon("buildcraft:triggers/trigger_dir_south");
		icons[StatementIconProvider.Action_Parameter_Direction_West] = iconRegister.registerIcon("buildcraft:triggers/trigger_dir_west");
		icons[StatementIconProvider.Action_Parameter_Direction_East] = iconRegister.registerIcon("buildcraft:triggers/trigger_dir_east");
	}
}
