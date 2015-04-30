package net.minecraft.mangrove.mod.thrive.autocon.itembroker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.inventory.filter.ArrayStackFilter;
import net.minecraft.mangrove.core.inventory.filter.StackFilter;
import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.mangrove.core.inventory.transactor.Transactor;
import net.minecraft.mangrove.core.inventory.transactor.TransactorSimple;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.BlockAutobench;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.TileEntityMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import com.google.gson.JsonObject;

public class TileItemBroker extends AbstractTileAutocon implements ITileUpdatable, IUpdatePlayerListBox {

	private ItemBrokerSupport brokerSupport=new ItemBrokerSupport();
	private ItemBrokerProcessor processor= new ItemBrokerProcessor() ;
//	private final ConnectionConfig[] connectors = new ConnectionConfig[6];
	private long tick = 0;

	public TileItemBroker() {
//		for (EnumFacing facing : EnumFacing.values()) {
//			connectors[facing.ordinal()] = new ConnectionConfig(facing, EnumConnectionState.DISCONNECTED);
//		}
	}

	@Override
	public void update() {
		if ((this.worldObj == null) || (this.worldObj.isRemote)) {
			return;
		}

		if (!isPowered()) {
			return;
		}
			if (tick % 32 == 0) {
				final List<ConnectionConfig> inletConnections = this.brokerSupport.getConnections(EnumConnectionState.INLET);
				final List<ConnectionConfig> outletConnections = this.brokerSupport.getConnections(EnumConnectionState.OUTLET);
				if( inletConnections.isEmpty() || outletConnections.isEmpty()){
					System.out.println("Error must have configured both inlet and outlet channels");
				}else{
					process(inletConnections,outletConnections);
				}
			}
			tick++;
	}
	
	public boolean isPowered() {
		return (Boolean) worldObj.getBlockState(pos).getValue(
				BlockAutobench.POWERED);
	}


	private void process(List<ConnectionConfig> inletConnections, List<ConnectionConfig> outletConnections) {
		processor.startProcessing();
//		final List<SearchItem> inletInventories=new ArrayList<SearchItem>();
		for(ConnectionConfig config:inletConnections){
			final BlockPos connectorPos = getPos().offset(config.facing);
			List<SearchItem> inventories = SearchUtil.findAllTileFrom(worldObj, connectorPos, MGThriveBlocks.duct_conveyor, IInventory.class);
//			inletInventories.addAll(inventories);						
			processor.addInlet(config, inventories);
		}
//		final List<SearchItem> outletInventories=new ArrayList<SearchItem>();
		for(ConnectionConfig config:outletConnections){
			final BlockPos connectorPos = getPos().offset(config.facing);
			List<SearchItem> inventories = SearchUtil.findAllTileFrom(worldObj, connectorPos, MGThriveBlocks.duct_conveyor, IInventory.class);
//			outletInventories.addAll(inventories);		
			processor.addOutlet(config, inventories);
		}
		if( processor.canProcess()){
			processor.process(this.worldObj);
		}
//		System.out.println("Inlet inventories:" + inletInventories);
//		System.out.println("Outlet inventories:" + outletInventories);
//		if( inletInventories.isEmpty() || outletConnections.isEmpty()){
//			
//		}else{
//			Iterator<SearchItem> itInlet = inletInventories.iterator();
//			Iterator<SearchItem> itOutlet = outletInventories.iterator();
//			
//			SearchItem inletSearch = itInlet.next();
//			SearchItem outletSearch = itOutlet.next();
//			
//			while(true){
//				
//				ITransactor inletTransactor = Transactor.getTransactorFor(worldObj.getTileEntity(inletSearch.getPos()));
//				
//				final ItemStack iStack = inletTransactor.remove(StackFilter.ALL, inletSearch.getFacing().getOpposite(), false);
//				if( iStack!=null && iStack.stackSize>0) {
//
//					do{
//						ITransactor outletTransactor = Transactor.getTransactorFor(worldObj.getTileEntity(outletSearch.getPos()));
//						final ItemStack iStack2 = outletTransactor.add(iStack, outletSearch.getFacing().getOpposite(), true);
//						if (iStack2.stackSize < iStack.stackSize) {
//							iStack.stackSize-=iStack2.stackSize;
//							if( itOutlet.hasNext()){
//								outletSearch=itOutlet.next();
//							}else{
//								return;
//							}
//						}else{
//							iStack.stackSize=0;
//						}
//					}while(iStack.stackSize>0);
//					inletTransactor.remove(StackFilter.ALL, inletSearch.getFacing().getOpposite(), true);
//					return;
//				}else{
//					if( itInlet.hasNext()){
//						inletSearch=itInlet.next();
//					}else{
//						return;
//					}
//				}
//				
//			}
//			for( SearchItem item:inletInventories){
//				
//				
//				if( iStack!=null && iStack.stackSize>0) {
//					transactor.remove(StackFilter.ALL, invFace, true);
//				}
//			}
//		}
		processor.endProcessing();
		
	}


	private ITransactor resolveTransactor(BlockPos connectorPos) {
		TileEntity tile = worldObj.getTileEntity(connectorPos);

		// if (tile instanceof TileRobotKernel) {
		// return TransactorSimple.getTransactorFor((TileRobotKernel) tile);
		// }
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		System.out.println("Load: client=" + worldObj + " -> " + compound);
		super.readFromNBT(compound);

		this.brokerSupport.readFromNBT(compound);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		this.brokerSupport.writeToNBT(compound);
		System.out.println("Save: client=" + worldObj + " -> " + compound);
	}

	@Override
	public Packet getDescriptionPacket() {
		System.out.println("Get Description Packet");
		NBTTagCompound syncData = new NBTTagCompound();
		this.brokerSupport.writeToNBT(syncData);
		return new S35PacketUpdateTileEntity(getPos(), getBlockMetadata(), syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.brokerSupport.readFromNBT(pkt.getNbtCompound());
	}

	public void updateConnectorState(EnumFacing facing, EnumConnectionState state) {
		brokerSupport.updateConnectorState(facing,state);
		fireUpdate(facing, state);
	}

	private void fireUpdate(EnumFacing facing, EnumConnectionState state) {
		if (this.worldObj.isRemote) {
			final JsonObject evt = JSON.newObject();
			evt.addProperty("facing", facing.name());
			evt.addProperty("state", state.name());
			NetBus.sendToServer(new TileEntityMessage(this, evt));
		}
		markDirty();
	}

	public void updateConnectorStatus(EnumFacing facing, boolean status) {
		brokerSupport.updateConnectorStatus(facing,status);
		

	}

	public ConnectionConfig getConnectorConfig(EnumFacing facing) {
		return brokerSupport.getConnectorConfig(facing);
	}

	public boolean isSideConnected(EnumFacing facing) {
		return brokerSupport.isSideConnected(facing);
	}

	@Override
	public JsonObject getTilePacketData() {
		final JsonObject data = JSON.newObject();

		return data;
	}

	private JsonObject itemStackToJson(ItemStack itemStack) {
		final JsonObject data = JSON.newObject();
		data.addProperty("itemId", Item.getIdFromItem(itemStack.getItem()));
		data.addProperty("stackSize", itemStack.stackSize);
		data.addProperty("damageValue", itemStack.getMaxDamage());
		return data;
	}

	private ItemStack jsonToItemStack(JsonObject data) {
		int itemId = data.get("itemId").getAsInt();
		int stackSize = data.get("stackSize").getAsInt();
		int damageValue = data.get("damageValue").getAsInt();

		return new ItemStack(Item.getItemById(itemId), stackSize, damageValue);
	}

	public void setFilteredItemStack(EnumFacing facing, int slot, ItemStack itemStack) {
		System.out.println("Setting Item : "+facing+":"+slot+":"+itemStack+":"+worldObj);
		brokerSupport.setFilteredItemStack(facing,slot,itemStack);
	}

	@Override
	public void handleClientUpdate(JsonObject data) throws IOException {
		System.out.println("Client:" + data);
	}

	@Override
	public void handleServerUpdate(JsonObject data) throws IOException {
		System.out.println("Server:" + data);

		updateConnectorState(EnumFacing.valueOf(data.get("facing").getAsString()), EnumConnectionState.valueOf(data.get("state").getAsString()));
		markDirty();
		// JsonObject templateObj = data.get("template").getAsJsonObject();
		// this.template = jsonToItemStack(templateObj);
		// inventorySupport.setSlotContents(18, this.template);
		//
		// JsonArray bomArray = data.get("bom").getAsJsonArray();
		// Iterator<JsonElement> it = bomArray.iterator();
		// this.bom.clear();
		// while (it.hasNext()) {
		// JsonObject bomObj = it.next().getAsJsonObject();
		// this.bom.add(jsonToItemStack(bomObj));
		// }
	}

	public IInventory getConnectorInventory() {
		return this.brokerSupport;
	}

}
