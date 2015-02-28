package net.minecraft.mangrove.mod.thrive.autocon.itembroker;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

public class ItemBrokerSupport implements IInventory{

	private final ConnectionConfig[] connectors = new ConnectionConfig[6];
	
	public ItemBrokerSupport() {
		for (EnumFacing facing : EnumFacing.values()) {
			connectors[facing.ordinal()] = new ConnectionConfig(facing, EnumConnectionState.DISCONNECTED);
		}
	}
		
	public void updateConnectorStatus(EnumFacing facing, boolean status) {
		if (status) {
			int count = 0;
			for (ConnectionConfig connector : connectors) {
				if (connector.state != EnumConnectionState.DISCONNECTED) {
					count++;
				}
			}
			if (count < 5) {
				connectors[facing.ordinal()].connect();
			}
	
		} else {
			connectors[facing.ordinal()].disconnect();
		}
	}

	@Override
	public int getSizeInventory() {
		return 6*7;
	}
	@Override
	public ItemStack getStackInSlot(int index) {
		int facingIndex = index/7;
		int slot=index%7;
		return connectors[facingIndex].getItemStack(slot);
	}
	
	public ItemStack getStackInSlotOnClosing(int index){
		int facingIndex = index/7;
		int slot=index%7;
		ItemStack itemStack = connectors[facingIndex].getItemStack(slot);
		
        if (itemStack!= null){
        	connectors[facingIndex].setItemStack((byte) slot,null);
            return itemStack;
        } else {
            return null;
        }
    }

    public ItemStack decrStackSize(int index, int count){
    	int facingIndex = index/7;
		int slot=index%7;
		ItemStack itemStack = connectors[facingIndex].getItemStack(slot);
		
        if (itemStack != null) {

            if (itemStack.stackSize <= count) {
            	connectors[facingIndex].setItemStack((byte) slot,null);
                return itemStack;
            } else  {
                ItemStack result = itemStack.splitStack(count);

                if (itemStack.stackSize == 0){
                	connectors[facingIndex].setItemStack((byte) slot,null);
                }

                return result;
            }
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack){
    	int facingIndex = index/7;
		int slot=index%7;
		connectors[facingIndex].setItemStack((byte) slot,stack);
    }
	

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for(EnumFacing facing:EnumFacing.values()){
			ConnectionConfig config = this.connectors[facing.ordinal()];
			for( byte slot=0; slot< 7; slot++){
				config.setItemStack(slot, null);
			}
		}
	}

	@Override
	public String getCommandSenderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	public void readFromNBT(NBTTagCompound compound) {
		NBTTagCompound connectorsTag = compound.getCompoundTag("Connectors");
		for (EnumFacing facing : EnumFacing.values()) {
			ConnectionConfig connectionConfig = new ConnectionConfig(facing, EnumConnectionState.DISCONNECTED);
			connectors[facing.ordinal()] = connectionConfig;
			if (connectorsTag.hasKey(facing.name())) {
				NBTTagCompound connector = connectorsTag.getCompoundTag(facing.name());
				EnumConnectionState state = EnumConnectionState.valueOf(connector.getString("State"));
				if (state != EnumConnectionState.DISCONNECTED) {
					connectionConfig.state = state;
					NBTTagList nbttaglist = connector.getTagList("Items", 10);					
					for (int i = 0; i < nbttaglist.tagCount(); i++) {
						NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
						byte slot = nbttagcompound1.getByte("Slot");
						if (slot < 7) {
							connectionConfig.setItemStack(slot, ItemStack.loadItemStackFromNBT(nbttagcompound1));
						}
					}

					// connectionConfig.item;
				}
			}

		}
	}
	public void writeToNBT(NBTTagCompound compound) {
		NBTTagCompound connectorsTag = new NBTTagCompound();
		for (EnumFacing facing : EnumFacing.values()) {
			ConnectionConfig connectionConfig = connectors[facing.ordinal()];
			if (connectionConfig.state != EnumConnectionState.DISCONNECTED) {
				NBTTagCompound connector = new NBTTagCompound();
				connector.setString("State", connectionConfig.state.name());

				NBTTagList nbttaglist = new NBTTagList();
				for (byte i = 0; i < 7; i++) {
					if (connectionConfig.items[i] != null) {
						NBTTagCompound nbttagcompound1 = new NBTTagCompound();

						nbttagcompound1.setByte("Slot", i);
						connectionConfig.items[i].writeToNBT(nbttagcompound1);

						nbttaglist.appendTag(nbttagcompound1);
					}
				}
				connector.setTag("Items", nbttaglist);
				connectorsTag.setTag(facing.name(), connector);
			}

		}
		compound.setTag("Connectors", connectorsTag);
	}

	public List<ConnectionConfig>getConnections(EnumConnectionState state) {
		List<ConnectionConfig> result=new ArrayList<ConnectionConfig>();
		for(ConnectionConfig connector:connectors){
			if(connector.state==state){
				result.add(connector);
			}
		}
		return result;
	}

	public void updateConnectorState(EnumFacing facing, EnumConnectionState state) {
		connectors[facing.ordinal()].state = state;
	}

	public ConnectionConfig getConnectorConfig(EnumFacing facing) {
		return connectors[facing.ordinal()];
	}

	public boolean isSideConnected(EnumFacing facing) {
		return connectors[facing.ordinal()].getState() != EnumConnectionState.DISCONNECTED;
	}

	public void setFilteredItemStack(EnumFacing facing, int slot, ItemStack itemStack) {
		connectors[facing.ordinal()].setItemStack((byte) slot, itemStack);
		
	}
}
