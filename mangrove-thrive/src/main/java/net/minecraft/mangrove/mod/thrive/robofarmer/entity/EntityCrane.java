package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.mangrove.core.INBTTagable;
import net.minecraft.mangrove.core.Position;
import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.core.proxy.FactoryProxy;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.FertilizeEndBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.FertilizeStartBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.HarvestEndBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.HarvestStartBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.IrrigateEndBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.IrrigateStartBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.MoveEndBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.PlantEndBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.PlantStartBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.PlowEndBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.PlowStartBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.SetupBehavior;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour.VoidBehavior;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.gson.JsonObject;

public class EntityCrane implements INBTTagable {

	private static final VoidBehavior VOID_BEHAVIOR = new VoidBehavior();
	private final EntityBlock boom;
	private final EntityBlock jib;
	private final EntityBlock head;

	private final Map<String, IBehaviour> behaviours = new HashMap<String, IBehaviour>();
	private World worldObj;
	private IBehaviour activeBehaviour = VOID_BEHAVIOR;
	private final Position position;

	public EntityCrane(World worldObj, Position position) {
		this.worldObj = worldObj;
		this.position = position;
		
		boom = FactoryProxy.proxy.newPumpTube(this.worldObj);
		jib = FactoryProxy.proxy.newPumpTube(this.worldObj);
		head = FactoryProxy.proxy.newPumpTube(this.worldObj);
		boom.setPosition(position.x, position.y, position.z);
		jib.setPosition(position.x, position.y, position.z);
		head.setPosition(position.x, position.y, position.z);
//		
//		this.behaviours.put(key(Activity.Setup, OperationStatus.End),
//				new SetupBehavior(this.boom, this.jib));
//		this.behaviours.put(key(Activity.Move, OperationStatus.End),
//				new MoveEndBehavior(this.boom, this.jib));
//		this.behaviours.put(key(Activity.Plow, OperationStatus.Start),
//				new PlowStartBehavior(this.head));
//		this.behaviours.put(key(Activity.Plow, OperationStatus.End),
//				new PlowEndBehavior(this.head));
//		this.behaviours.put(key(Activity.Irrigate, OperationStatus.Start),
//				new IrrigateStartBehavior(this.head));
//		this.behaviours.put(key(Activity.Irrigate, OperationStatus.End),
//				new IrrigateEndBehavior(this.head));
//		this.behaviours.put(key(Activity.Plant, OperationStatus.Start),
//				new PlantStartBehavior(this.head));
//		this.behaviours.put(key(Activity.Plant, OperationStatus.End),
//				new PlantEndBehavior(this.head));
//		this.behaviours.put(key(Activity.Fertilize, OperationStatus.Start),
//				new FertilizeStartBehavior(this.head));
//		this.behaviours.put(key(Activity.Fertilize, OperationStatus.End),
//				new FertilizeEndBehavior(this.head));
//		this.behaviours.put(key(Activity.Harvest, OperationStatus.Start),
//				new HarvestStartBehavior(this.head));
//		this.behaviours.put(key(Activity.Harvest, OperationStatus.End),
//				new HarvestEndBehavior(this.head));

	}

	private String key(Activity plow, OperationStatus start) {
		return plow.name() + ":" + start.name();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {

	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {

	}

	public JsonObject getData() {
		JsonObject data = JSON.newObject();
		return data;
	}

	public void setData(JsonObject asJsonObject) {

	}

	public void create(World worldObj) {
		if (!boom.addedToChunk) {
			this.worldObj.spawnEntityInWorld(boom);
		}
		if (!jib.addedToChunk) {
			this.worldObj.spawnEntityInWorld(jib);
		}
		if (!head.addedToChunk) {
			this.worldObj.spawnEntityInWorld(head);
		}
	}

	public void destroy(World worldObj) {
		worldObj.removeEntity(boom);
		worldObj.removeEntity(jib);
		worldObj.removeEntity(head);

	}

	public IBehaviour getBehaviour(Activity machineState, OperationStatus status) {
		create(worldObj);
		final String key = key(machineState, status);
		final IBehaviour iBehaviour;
		if (this.behaviours.containsKey(key)) {
			iBehaviour = this.behaviours.get(key);
		} else {
			iBehaviour = VOID_BEHAVIOR;
		}

		if (this.activeBehaviour != iBehaviour) {
			//iBehaviour.init(worldObj, position);
			iBehaviour.start();
			this.activeBehaviour = iBehaviour;
		}
		return iBehaviour;
	}
}
