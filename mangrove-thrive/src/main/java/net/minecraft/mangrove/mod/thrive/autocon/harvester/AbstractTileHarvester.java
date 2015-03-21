package net.minecraft.mangrove.mod.thrive.autocon.harvester;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.mangrove.core.inventory.transactor.TransactorSimple;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.mangrove.mod.thrive.autocon.junction.BlockStorageJunction;
import net.minecraft.mangrove.mod.thrive.autocon.junction.TileStorageJunction;
import net.minecraft.mangrove.mod.thrive.robot.entity.TileRobotKernel;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.TileEntityMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing.AxisDirection;

public abstract class AbstractTileHarvester extends AbstractTileAutocon implements IUpdatePlayerListBox, ITileUpdatable {
	private Lifecycle stage = Lifecycle.Off;
	private int tick = 0;

	@Override
	public final void update() {
		List<SearchItem> result = SearchUtil.findAllBlockFrom(worldObj, pos, MGThriveBlocks.duct_connector, MGThriveBlocks.storage_junction);
		if (result.isEmpty()) {
			doStop();
			return;
		}
		if (result.size() > 1) {
			doStop();
			return;
		}
		final SearchItem storageJuntionPosition = result.get(0);
		boolean powered = worldObj.getBlockState(storageJuntionPosition.getPos()).getValue(BlockStorageJunction.POWERED).equals(true);
		if (!powered) {
			doStop();
			return;
		}
		if (stage == Lifecycle.Off) {
			doStart();
		}
		tick++;
		if (this.worldObj.isRemote) {
			updateClient(storageJuntionPosition.getPos());
		} else {
			updateServer(storageJuntionPosition.getPos());
		}
	}

	private void updateServer(BlockPos storageJuntionPos) {
		switch (stage) {
		case Init:
			if (tick % _stageDuration(stage) == 0) {
				if (doInit(tick)) {
					this.stage = Lifecycle.RenderScene;
					fireServerLifecycleEvent();
				} else {
					this.stage = Lifecycle.Rollback;
					fireServerLifecycleEvent();
				}
			}
			break;
		case Execute:
			if (tick % _stageDuration(stage) == 0) {
				if (doExecute(tick)) {
					this.stage = Lifecycle.RenderSceneOut;
					fireServerLifecycleEvent();
				} else {
					this.stage = Lifecycle.Rollback;
					fireServerLifecycleEvent();
				}
			}
			break;
		case Commit:
			if (tick % _stageDuration(stage) == 0) {
				doCommit(tick);
				this.stage = Lifecycle.RenderCooldown;
				fireServerLifecycleEvent();
			}
			break;
		case Rollback:
			if (tick % _stageDuration(stage) == 0) {
				doRollback(tick);
				this.stage = Lifecycle.RenderFailScene;
				fireServerLifecycleEvent();
			}
			break;
		default:
			if (tick % 128 == 0) {
				System.out.println("Server Side Stage=" + stage);
				fireServerLifecycleEvent();
			}
			break;
		}
	}

	private void updateClient(BlockPos storageJuntionPos) {
		switch (stage) {
		case RenderScene:
			if (tick % _stageDuration(stage) == 0) {
				System.out.println("Render scene");
				if (renderScene(tick)) {
					this.stage = Lifecycle.Execute;
					fireClientLifecycleEvent();
				}
			}
			break;
		case RenderSceneOut:
			if (tick % _stageDuration(stage) == 0) {
				if (renderSceneOut(tick)) {
					this.stage = Lifecycle.Commit;
					fireClientLifecycleEvent();
				}
			}
			break;
		case RenderCooldown:
			if (tick % _stageDuration(stage) == 0) {
				if (renderCooldown(tick)) {
					this.stage = Lifecycle.Init;
					fireClientLifecycleEvent();
				}
			}
			break;
		case RenderFailScene:
			if (tick % _stageDuration(stage) == 0) {
				System.out.println("Failure");
				if (renderFailure(tick)) {
					this.stage = Lifecycle.Init;
					fireClientLifecycleEvent();
				}
			}
			break;
		default:
			if (tick % 128 == 0) {
				System.out.println("Cient Side Stage=" + stage);
				fireClientLifecycleEvent();
			}
			return;
		}

	}

	protected ITransactor getTransactor() {
		List<SearchItem> result = SearchUtil.findAllBlockFrom(worldObj, pos, MGThriveBlocks.duct_connector, MGThriveBlocks.storage_junction);
		if (!result.isEmpty()) {
			TileEntity tile = worldObj.getTileEntity(result.get(0).getPos());
			if (tile instanceof TileStorageJunction) {
				return TransactorSimple.getTransactorFor((TileStorageJunction) tile);
			}
		}
		return null;
	}

	private int _stageDuration(Lifecycle stage2) {
		int duration = stageDuration(stage2);
		return duration < 1 ? 1 : duration;
	}

	protected int stageDuration(Lifecycle stage2) {
		return 10;
	}

	public void doStart() {
		if (this.stage == Lifecycle.Off) {
			if (this.worldObj != null && !this.worldObj.isRemote) {
				this.stage = Lifecycle.Init;
			}
			fireServerLifecycleEvent();
			markDirty();
		}
	}

	public void doStop() {
		if (this.stage != Lifecycle.Off) {
			if (this.worldObj != null && !this.worldObj.isRemote) {
				this.stage = Lifecycle.Off;

			}
			fireServerLifecycleEvent();
			markDirty();
		}
	}

	protected abstract boolean doInit(int serverTick);

	protected abstract boolean doExecute(int serverTick);

	protected abstract void doCommit(int serverTick);

	protected abstract void doRollback(int serverTick);

	protected abstract boolean renderScene(int clientTick);

	protected abstract boolean renderSceneOut(int clientTick);

	protected abstract boolean renderCooldown(int clientTick);

	protected abstract boolean renderFailure(int clientTick);

	protected final void fireClientLifecycleEvent() {
		if (this.worldObj.isRemote) {
			final JsonObject evt = getTilePacketData();
			evt.addProperty("FireEvent", true);
			evt.addProperty("Side", "CLIENT");
			// evt.addProperty("stage", stage.name());
			// evt.addProperty("step", step);
			NetBus.sendToServer(new TileEntityMessage(this, evt));
		}
	}

	protected final void fireServerLifecycleEvent() {
		if (!this.worldObj.isRemote) {
			final JsonObject evt = getTilePacketData();
			evt.addProperty("FireEvent", true);
			evt.addProperty("Side", "SERVER");
			// evt.addProperty("stage", stage.name());
			// evt.addProperty("step", step);
			NetBus.sendToClient(new TileEntityMessage(this, evt));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		stage = Lifecycle.ofInt(compound.getInteger("Stage"));
		// stage=Lifecycle.ofString(compound.getString("Stage"));
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("Stage", stage.ordinal());
	}

	@Override
	public JsonObject getTilePacketData() {
		final JsonObject data = JSON.newObject();
		data.addProperty("SourceName", getClass().getSimpleName());
		// data.addProperty("step", this.step);
		data.addProperty("stage", stage.name());
		return data;
	}

	@Override
	public void handleClientUpdate(JsonObject data) throws IOException {
		// if (data.has("step")) {
		// if (data.get("step").isJsonNull()) {
		// this.step = 0;
		// } else {
		// this.step = data.get("step").getAsInt();
		// }
		// }
		if (data.has("stage")) {
			if (!data.get("stage").isJsonNull()) {
				this.stage = Lifecycle.valueOf(data.get("stage").getAsString());
			}
		}
		if (data.has("FireEvent")) {
			handleServerEvent(data);
		}
	}

	@Override
	public void handleServerUpdate(JsonObject data) throws IOException {
		if (data.has("stage")) {
			if (!data.get("stage").isJsonNull()) {
				this.stage = Lifecycle.valueOf(data.get("stage").getAsString());
			}
		}
		if (data.has("FireEvent")) {
			handleClientEvent(data);
		}
	}

	protected void handleClientEvent(JsonObject data) {
	}

	protected void handleServerEvent(JsonObject data) {
	}

	public double offsetX(BlockPos pos, AxisDirection dir, double delta) {
		return pos.getX() + (delta * dir.getOffset());
	}

	public double offsetY(BlockPos pos, AxisDirection dir, double delta) {
		return pos.getY() + (delta * dir.getOffset());
	}

	public double offsetZ(BlockPos pos, AxisDirection dir, double delta) {
		return pos.getZ() + (delta * dir.getOffset());
	}

}
