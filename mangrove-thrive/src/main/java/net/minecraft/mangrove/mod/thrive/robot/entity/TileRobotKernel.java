package net.minecraft.mangrove.mod.thrive.robot.entity;

import net.minecraft.block.Block;
import net.minecraft.mangrove.core.inventory.EnumPermission;
import net.minecraft.mangrove.core.inventory.tile.AbstractSidedInventoryTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;

public class TileRobotKernel extends AbstractSidedInventoryTileEntity implements IUpdatePlayerListBox {
	private String name;
	private int tick=0;

	
	public TileRobotKernel() {
		super();
		this.name=null;
		inventorySupport.defineSlotRange(0, 180, null, EnumPermission.BOTH, EnumFacing.DOWN,EnumFacing.UP,EnumFacing.NORTH,EnumFacing.SOUTH,EnumFacing.WEST,EnumFacing.EAST);
	}
	public String getName(){
		if( this.name==null){
			this.name=String.format("Crate (%d,%d,%d)",pos.getX(),pos.getY(),pos.getZ());
		}
		return this.name;
	}
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
//		if( tag.hasKey("sid")){
//		    uuid=UUID.fromString(tag.getString("sid"));
//		}
		
	}
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
//		if( uuid==null){
//		    tag.removeTag("sid");
//		}else{
//		    tag.setString("sid", uuid.toString());
//		}
	}
	
	@Override
	public void update() {
	    tick++;
	    if( !worldObj.isRemote){
	        if( tick % 512==0){
	            Block block = worldObj.getBlockState(pos).getBlock();
	            int powered = worldObj.isBlockIndirectlyGettingPowered(pos);
                System.out.println("Is getting Power? "+powered);
//                if( powered){
//                    SystemUtils.updateNetwork(worldObj, xCoord, yCoord, zCoord);
//                }
	        }
	    }
	        
	}
    public void handlePower() {
        System.out.println("Is getting Power? "+worldObj.getStrongPower(pos));
    }
//    @SideOnly(Side.SERVER)
//    public UUID getSid() {
//        return uuid;
//    }
	
}
