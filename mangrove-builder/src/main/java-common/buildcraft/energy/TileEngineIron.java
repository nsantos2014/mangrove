/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.energy;

import java.util.LinkedList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftEnergy;
import buildcraft.api.core.StackKey;
import buildcraft.api.fuels.BuildcraftFuelRegistry;
import buildcraft.api.fuels.ICoolant;
import buildcraft.api.fuels.IFuel;
import buildcraft.api.gates.ITrigger;
import buildcraft.core.GuiIds;
import buildcraft.core.IItemPipe;
import buildcraft.core.fluids.FluidUtils;
import buildcraft.core.fluids.Tank;
import buildcraft.core.fluids.TankManager;
import buildcraft.core.inventory.InvUtils;
import buildcraft.energy.gui.ContainerEngine;

public class TileEngineIron extends TileEngineWithInventory implements IFluidHandler {

	public static int MAX_LIQUID = FluidContainerRegistry.BUCKET_VOLUME * 10;
	public static float HEAT_PER_RF = 0.00023F;
	public static float COOLDOWN_RATE = 0.05F;
	public static int MAX_COOLANT_PER_TICK = 40;

	public Tank tankFuel = new Tank("tankFuel", MAX_LIQUID, this);
	public Tank tankCoolant = new Tank("tankCoolant", MAX_LIQUID, this);

	private int burnTime = 0;

	private TankManager<Tank> tankManager = new TankManager<Tank>();
	private IFuel currentFuel;
	private int penaltyCooling = 0;
	private boolean lastPowered = false;
	private BiomeGenBase biomeCache;

	public TileEngineIron() {
		super(1);
		tankManager.add(tankFuel);
		tankManager.add(tankCoolant);
	}

	@Override
	public ResourceLocation getBaseTexture() {
		return BASE_TEXTURES[2];
	}

	@Override
	public ResourceLocation getChamberTexture() {
		return CHAMBER_TEXTURES[2];
	}

	@Override
	public boolean onBlockActivated(EntityPlayer player, ForgeDirection side) {
		ItemStack current = player.getCurrentEquippedItem();
		if (current != null) {
			if (current.getItem() instanceof IItemPipe) {
				return false;
			}
			if (!worldObj.isRemote) {
				if (FluidUtils.handleRightClick(this, side, player, true, true)) {
					return true;
				}
			} else {
				if (FluidContainerRegistry.isContainer(current)) {
					return true;
				}
			}
		}
		if (!worldObj.isRemote) {
			player.openGui(BuildCraftEnergy.instance, GuiIds.ENGINE_IRON, worldObj, xCoord, yCoord, zCoord);
		}
		return true;
	}

	@Override
	public float explosionRange() {
		return 4;
	}

	@Override
	public float getPistonSpeed() {
		if (!worldObj.isRemote) {
			return Math.max(0.07f * getHeatLevel(), 0.01f);
		}
		switch (getEnergyStage()) {
			case BLUE:
				return 0.04F;
			case GREEN:
				return 0.05F;
			case YELLOW:
				return 0.06F;
			case RED:
				return 0.07F;
			default:
				return 0;
		}
	}

	private float getBiomeTempScalar() {
		if (biomeCache == null) {
			biomeCache = worldObj.getBiomeGenForCoords(xCoord, zCoord);
		}
		float tempScalar = biomeCache.temperature - 1.0F;
		tempScalar *= 0.5F;
		tempScalar += 1.0F;
		return tempScalar;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		biomeCache = null;
	}

	@Override
	public boolean isBurning() {
		FluidStack fuel = tankFuel.getFluid();
		return fuel != null && fuel.amount > 0 && penaltyCooling == 0 && isRedstonePowered;
	}

	@Override
	public void burn() {
		FluidStack fuel = this.tankFuel.getFluid();
		if (currentFuel == null && fuel != null) {
			currentFuel = BuildcraftFuelRegistry.fuel.getFuel(fuel.getFluid());
		}

		if (currentFuel == null) {
			return;
		}

		if (penaltyCooling <= 0 && isRedstonePowered) {

			lastPowered = true;

			if (burnTime > 0 || (fuel != null && fuel.amount > 0)) {
				if (burnTime > 0) {
					burnTime--;
				}
				if (burnTime <= 0) {
					if (fuel != null) {
						if (--fuel.amount <= 0) {
							tankFuel.setFluid(null);
						}
						burnTime = currentFuel.getTotalBurningTime() / FluidContainerRegistry.BUCKET_VOLUME;
					} else {
						currentFuel = null;
						return;
					}
				}

				if (!this.constantPower) {
					currentOutput = currentFuel.getPowerPerCycle();
				}

				addEnergy(currentFuel.getPowerPerCycle());
				heat += currentFuel.getPowerPerCycle() * HEAT_PER_RF * getBiomeTempScalar();
			}
		} else if (penaltyCooling <= 0) {
			if (lastPowered) {
				lastPowered = false;
				penaltyCooling = 10;
				// 10 tick of penalty on top of the cooling
			}
		}
	}

	@Override
	public void updateHeatLevel() {
	}

	@Override
	public void engineUpdate() {

		final ItemStack stack = getStackInSlot(0);
		if (stack != null) {
			FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(stack);
			if (liquid == null && heat > MIN_HEAT * 2) {
				liquid = BuildcraftFuelRegistry.coolant.getSolidCoolant(StackKey.stack(stack)).getFluidFromSolidCoolant(stack);
			}

			if (liquid != null) {
				if (fill(ForgeDirection.UNKNOWN, liquid, false) == liquid.amount) {
					fill(ForgeDirection.UNKNOWN, liquid, true);
					setInventorySlotContents(0, InvUtils.consumeItem(stack));
				}
			}
		}

		if (heat > MIN_HEAT && (penaltyCooling > 0 || !isRedstonePowered)) {
			heat -= COOLDOWN_RATE;
			coolEngine(MIN_HEAT);
			getEnergyStage();
		} else if (heat > IDEAL_HEAT) {
			coolEngine(IDEAL_HEAT);
		}

		if (heat <= MIN_HEAT && penaltyCooling > 0) {
			penaltyCooling--;
		}

		if (heat <= MIN_HEAT) {
			heat = MIN_HEAT;
		}
	}

	private void coolEngine(float idealHeat) {
		float extraHeat = heat - idealHeat;

		FluidStack coolant = this.tankCoolant.getFluid();
		if (coolant == null) {
			return;
		}

		int coolantAmount = Math.min(MAX_COOLANT_PER_TICK, coolant.amount);
		ICoolant currentCoolant = BuildcraftFuelRegistry.coolant.getCoolant(coolant.getFluid());
		if (currentCoolant != null) {
			float cooling = currentCoolant.getDegreesCoolingPerMB(heat);
			cooling /= getBiomeTempScalar();
			if (coolantAmount * cooling > extraHeat) {
				tankCoolant.drain(Math.round(extraHeat / cooling), true);
				heat -= extraHeat;
			} else {
				tankCoolant.drain(coolantAmount, true);
				heat -= coolantAmount * cooling;
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		tankManager.readFromNBT(data);

		burnTime = data.getInteger("burnTime");
		penaltyCooling = data.getInteger("penaltyCooling");

	}

	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		tankManager.writeToNBT(data);

		data.setInteger("burnTime", burnTime);
		data.setInteger("penaltyCooling", penaltyCooling);

	}

	@Override
	public void getGUINetworkData(int id, int value) {
		super.getGUINetworkData(id, value);
		switch (id) {
			// Fluid Fuel ID
			case 15:
				if (tankFuel.getFluid() == null) {
					tankFuel.setFluid(new FluidStack(value, 0));
				} else {
					tankFuel.getFluid().fluidID = value;
				}
				break;
			// Fluid Coolant ID
			case 16:
				if (tankCoolant.getFluid() == null) {
					tankCoolant.setFluid(new FluidStack(value, 0));
				} else {
					tankCoolant.getFluid().fluidID = value;
				}
				break;
			// Fluid Fuel amount
			case 17:
				if (tankFuel.getFluid() == null) {
					tankFuel.setFluid(new FluidStack(0, value));
				} else {
					tankFuel.getFluid().amount = value;
				}
				break;
			// Fluid Coolant amount
			case 18:
				if (tankCoolant.getFluid() == null) {
					tankCoolant.setFluid(new FluidStack(0, value));
				} else {
					tankCoolant.getFluid().amount = value;
				}
				break;
			//Fluid Fuel color
			case 19:
				tankFuel.colorRenderCache = value;
				break;
			//Fluid Coolant color
			case 20:
				tankCoolant.colorRenderCache = value;
				break;
		}
	}

	@Override
	public void sendGUINetworkData(ContainerEngine containerEngine, ICrafting iCrafting) {
		super.sendGUINetworkData(containerEngine, iCrafting);
		iCrafting.sendProgressBarUpdate(containerEngine, 15, tankFuel.getFluid() != null ? tankFuel.getFluid().fluidID : 0);
		iCrafting.sendProgressBarUpdate(containerEngine, 16, tankCoolant.getFluid() != null ? tankCoolant.getFluid().fluidID : 0);
		iCrafting.sendProgressBarUpdate(containerEngine, 17, tankFuel.getFluid() != null ? tankFuel.getFluid().amount : 0);
		iCrafting.sendProgressBarUpdate(containerEngine, 18, tankCoolant.getFluid() != null ? tankCoolant.getFluid().amount : 0);
		iCrafting.sendProgressBarUpdate(containerEngine, 19, tankFuel.colorRenderCache);
		iCrafting.sendProgressBarUpdate(containerEngine, 20, tankCoolant.colorRenderCache);
	}

	@Override
	public boolean isActive() {
		return penaltyCooling <= 0;
	}

	/* ITANKCONTAINER */
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tankFuel.drain(maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (resource == null) {
			return null;
		}
		if (tankCoolant.getFluidType() == resource.getFluid()) {
			return tankCoolant.drain(resource.amount, doDrain);
		}
		if (tankFuel.getFluidType() == resource.getFluid()) {
			return tankFuel.drain(resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {

		// Handle coolant
		if (BuildcraftFuelRegistry.coolant.getCoolant(resource.getFluid()) != null) {
			return tankCoolant.fill(resource, doFill);
		} else if (BuildcraftFuelRegistry.fuel.getFuel(resource.getFluid()) != null) {
			return tankFuel.fill(resource, doFill);
		} else {
			return 0;
		}
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return BuildcraftFuelRegistry.coolant.getCoolant(fluid) != null || BuildcraftFuelRegistry.fuel.getFuel(fluid) != null;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection direction) {
		return tankManager.getTankInfo(direction);
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		} else if (BuildcraftFuelRegistry.coolant.getSolidCoolant(StackKey.stack(itemstack)) != null) {
			return true;
		} else {
			FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
			return fluidStack != null && canFill(ForgeDirection.UNKNOWN, fluidStack.getFluid());
		}
	}

	public FluidStack getFuel() {
		return tankFuel.getFluid();
	}

	public FluidStack getCoolant() {
		return tankCoolant.getFluid();
	}

	@Override
	public int maxEnergyReceived() {
		return 20000;
	}

	@Override
	public int maxEnergyExtracted() {
		return 5000;
	}

	@Override
	public int getMaxEnergy() {
		return 100000;
	}

	@Override
	public int getCurrentOutput() {
		if (currentFuel == null) {
			return 0;
		} else {
			return currentFuel.getPowerPerCycle();
		}
	}

	@Override
	public LinkedList<ITrigger> getTriggers() {
		LinkedList<ITrigger> triggers = super.getTriggers();
		triggers.add(BuildCraftCore.triggerEmptyFluid);
		triggers.add(BuildCraftCore.triggerContainsFluid);
		triggers.add(BuildCraftCore.triggerSpaceFluid);
		triggers.add(BuildCraftCore.triggerFullFluid);
		triggers.add(BuildCraftCore.triggerFluidContainerBelow25);
		triggers.add(BuildCraftCore.triggerFluidContainerBelow50);
		triggers.add(BuildCraftCore.triggerFluidContainerBelow75);

		return triggers;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}
}
