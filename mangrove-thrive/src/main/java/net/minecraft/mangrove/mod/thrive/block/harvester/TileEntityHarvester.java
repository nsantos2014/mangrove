package net.minecraft.mangrove.mod.thrive.block.harvester;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.CoreConstants;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.block.AbstractSidedInventoryTileEntity;
import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.core.inventory.InvUtils;
import net.minecraft.mangrove.core.inventory.Permission;
import net.minecraft.mangrove.core.proxy.FactoryProxy;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.PacketPayload;
import net.minecraft.mangrove.network.PacketUpdate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityHarvester extends AbstractSidedInventoryTileEntity implements ITileUpdatable {

	private static final int[] EMPTY = new int[0];
	
	private EntityBlock head;
	private EntityBlock hTray;
	private EntityBlock vTray;
	
	private double tubeY = Double.NaN;
	private int aimY = 0;
	
	private int transferCooldown = 0;
    
	
    public TileEntityHarvester() {
    	super();
    	
    	inventorySupport.defineSlotRange(0, 5, null, Permission.BOTH, 0,1,2,3,4,5);
//		ensureSize(hopperItemStacks, 4);
	}

    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        //this.transferCooldown = tag.getInteger("TransferCooldown");
        
        aimY = tag.getInteger("aimY");
		tubeY = tag.getFloat("tubeY");
		head.readFromNBT(tag);
    }

    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);

       // tag.setInteger("TransferCooldown", this.transferCooldown);
        
        if (head != null) {
        	tag.setFloat("tubeY", (float) head.posY);
		} else {
			tag.setFloat("tubeY", yCoord);
		}
        head.writeToNBT(tag);
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty(){
        super.markDirty();
    }

    public void updateEntity() {
    	if (head == null) {
    		createTube();
			//return;
		}

		if (head.posY - aimY > 0.01) {
			tubeY = head.posY - 0.01;
			setTubePosition();
			sendNetworkUpdate();
			return;
		}
		this.transferCooldown++;
		
		if(this.transferCooldown % 16 != 0){
			return;
		}
		if (this.transferCooldown % 16 == 0 && this.worldObj != null && !this.worldObj.isRemote){
			plowAndIrrigate();
			this.markDirty();
		}
//				
//        if (this.transferCooldown % 16 == 0 && this.worldObj != null && !this.worldObj.isRemote){
//        	
//            if (!this.isCoolingDown()){
//                this.setTransferCooldown(0);
//                this.func_145887_i();
//                
//                
//            }
//        }
        if (this.transferCooldown % 128==0){
        	if(aimY < yCoord-1){
	        	final Block block = worldObj.getBlock(xCoord, yCoord-1, zCoord);
	        	if( block==Blocks.air){
	        		aimY = yCoord-1;
	        	}
        	}
//        		
//        	for (int y = yCoord-1; y > 0; --y) {
//            	final Block block = worldObj.getBlock(xCoord, y, zCoord);
//            	System.out.println("????? - "+block);
//            	if( block==Blocks.air){
//            		aimY = y+1;
//            	}else{            		
//            		return;
//            	}
//			}
        }
    }

//    public boolean func_145887_i() {
//        if (this.worldObj != null && !this.worldObj.isRemote) {
//        	
//        	final List<ItemStack> harvestOutput=calculateHarvestOutput();
//        	if(!harvestOutput.isEmpty()){
//        		InvUtils.packItemStackList(harvestOutput);        	
//        		this.inventorySupport.mergeAll(harvestOutput);
//        		this.markDirty();
//        		
//        	}
//        	
//        	this.setTransferCooldown(80);
//        	
//            return false;
//        }
//        else
//        {
//            return false;
//        }
//    }
    
    private void createTube() {
		if (head == null) {
			head = FactoryProxy.proxy.newPumpTube(worldObj);

			if (!Double.isNaN(tubeY)) {
				head.posY = tubeY;
			} else {
				head.posY = yCoord;
			}

			tubeY = head.posY;

			if (aimY == 0) {
				aimY = yCoord;
			}

			setTubePosition();

			worldObj.spawnEntityInWorld(head);

			if (!worldObj.isRemote) {
				sendNetworkUpdate();
			}
		}
	}

	private void destroyTube() {
		if (head != null) {
			//CoreProxy.proxy.removeEntity(tube);
			worldObj.removeEntity(head);
			head = null;
			tubeY = Double.NaN;
			aimY = 0;
		}
	}
    
	private void plowAndIrrigate() {
		for( int y=-1; y>-7; y--){
			for( int x=-2; x<3; x++){
				for( int z=-2; z<3; z++){
					final Block block = this.worldObj.getBlock(xCoord+x, yCoord+y, zCoord+z);
					int meta = this.worldObj.getBlockMetadata(xCoord+x, yCoord+y, zCoord+z);
					if( block==Blocks.dirt || block==Blocks.grass){
						 final Block block1 = Blocks.farmland;
						 System.out.println("Replace : "+block+":"+block1);
//			             if (!this.worldObj.isRemote){
			                this.worldObj.playSoundEffect((double)((float)(xCoord+x) + 0.5F), (double)((float)(yCoord+y) + 0.5F), (double)((float)(zCoord+z) + 0.5F), block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
			              	this.worldObj.setBlock(xCoord+x, yCoord+y, zCoord+z, block1);	
			               	this.worldObj.setBlockMetadataWithNotify(xCoord+x, yCoord+y, zCoord+z,7, 2);
//			             }            				
					}else if( block==Blocks.farmland && meta <1){
						this.worldObj.playSoundEffect((double)((float)(xCoord+x) + 0.5F), (double)((float)(yCoord+y) + 0.5F), (double)((float)(zCoord+z) + 0.5F), block.stepSound.getStepResourcePath(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
						this.worldObj.setBlockMetadataWithNotify(xCoord+x, yCoord+y, zCoord+z,7, 2);
					}
				}
			}        		
		}
	}
	
//    private List<ItemStack> calculateHarvestOutput() {
//		final List<ItemStack> harvestOutput=new ArrayList<ItemStack>();
//		for( int y=-1; y>-4; y--){
//			for( int x=-2; x<3; x++){
//				
//				for( int z=-2; z<3; z++){
//					Block block = this.worldObj.getBlock(xCoord+x, yCoord+y, zCoord+z);
//					int meta = this.worldObj.getBlockMetadata(xCoord+x, yCoord+y, zCoord+z);
//					if( block==Blocks.wheat && meta>=7){
//						final List<ItemStack> items = block.getDrops(worldObj, xCoord+x, yCoord+y, zCoord+z, meta, 0);
//						harvestOutput.addAll(items);
//						this.worldObj.setBlockMetadataWithNotify(xCoord+x, yCoord+y, zCoord+z, 0, 2);            				
//					}
//					if( block==Blocks.carrots && meta>=7){
//						final List<ItemStack> items = block.getDrops(worldObj, xCoord+x, yCoord+y, zCoord+z, meta, 0);
//						harvestOutput.addAll(items);
//						this.worldObj.setBlockMetadataWithNotify(xCoord+x, yCoord+y, zCoord+z, 0, 2);            				
//					}
//					if( block==Blocks.potatoes && meta>=7){
//						final List<ItemStack> items = block.getDrops(worldObj, xCoord+x, yCoord+y, zCoord+z, meta, 0);
//						harvestOutput.addAll(items);
//						this.worldObj.setBlockMetadataWithNotify(xCoord+x, yCoord+y, zCoord+z, 0, 2);            				
//					}
//				}
//			}        		
//		}
//		return harvestOutput;
//	}
    
    private void merge(final List<ItemStack> from,final List<ItemStack> to) {
    	final Iterator<ItemStack> it = from.iterator();
    	while (it.hasNext()) {
			final ItemStack itemStack = (ItemStack) it.next();
			for( int i=0; i< to.size(); i++){
				ItemStack next = to.get(i);
				if( next==null){
					to.set(i, itemStack);
					break;
				}
				if( next.isItemEqual(itemStack) ){
					if(next.stackSize+itemStack.stackSize <=itemStack.getMaxStackSize()){
						next.stackSize+=itemStack.stackSize;
						break;
					}else{
						itemStack.stackSize=itemStack.getMaxStackSize()-next.stackSize;
						next.stackSize=itemStack.getMaxStackSize();						
					}
				}
			}
		}
    }

	
	@Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
    	int dir = BlockUtils.getDirectionFromMetadata(getBlockMetadata());
    	return dir==side;
    }
    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
    	return false;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int side) {    	
    	int dir = BlockUtils.getDirectionFromMetadata(getBlockMetadata());
    	if(dir==side){
    		return inventorySupport.getSlotArray(side);    	
    	}
    	return EMPTY;
    }
	

    private static boolean func_145892_a(TileEntityHarvester harvester, IInventory inventory, int slot, int side){
        ItemStack itemstack = inventory.getStackInSlot(slot);

        if (itemstack != null && func_145890_b(inventory, itemstack, slot, side))
        {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = func_145889_a(harvester, inventory.decrStackSize(slot, 1), -1);

            if (itemstack2 == null || itemstack2.stackSize == 0)
            {
                inventory.markDirty();
                return true;
            }

            inventory.setInventorySlotContents(slot, itemstack1);
        }

        return false;
    }

    public static boolean func_145898_a(IInventory p_145898_0_, EntityItem p_145898_1_)
    {
        boolean flag = false;

        if (p_145898_1_ == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = p_145898_1_.getEntityItem().copy();
            ItemStack itemstack1 = func_145889_a(p_145898_0_, itemstack, -1);

            if (itemstack1 != null && itemstack1.stackSize != 0)
            {
                p_145898_1_.setEntityItemStack(itemstack1);
            }
            else
            {
                flag = true;
                p_145898_1_.setDead();
            }

            return flag;
        }
    }

    public static ItemStack func_145889_a(IInventory p_145889_0_, ItemStack p_145889_1_, int p_145889_2_)
    {
        if (p_145889_0_ instanceof ISidedInventory && p_145889_2_ > -1)
        {
            ISidedInventory isidedinventory = (ISidedInventory)p_145889_0_;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(p_145889_2_);

            for (int l = 0; l < aint.length && p_145889_1_ != null && p_145889_1_.stackSize > 0; ++l)
            {
                p_145889_1_ = func_145899_c(p_145889_0_, p_145889_1_, aint[l], p_145889_2_);
            }
        }
        else
        {
            int j = p_145889_0_.getSizeInventory();

            for (int k = 0; k < j && p_145889_1_ != null && p_145889_1_.stackSize > 0; ++k)
            {
                p_145889_1_ = func_145899_c(p_145889_0_, p_145889_1_, k, p_145889_2_);
            }
        }

        if (p_145889_1_ != null && p_145889_1_.stackSize == 0)
        {
            p_145889_1_ = null;
        }

        return p_145889_1_;
    }

    private static boolean func_145885_a(IInventory p_145885_0_, ItemStack p_145885_1_, int p_145885_2_, int p_145885_3_)
    {
        return !p_145885_0_.isItemValidForSlot(p_145885_2_, p_145885_1_) ? false : !(p_145885_0_ instanceof ISidedInventory) || ((ISidedInventory)p_145885_0_).canInsertItem(p_145885_2_, p_145885_1_, p_145885_3_);
    }

    private static boolean func_145890_b(IInventory inventory, ItemStack itemStack, int slot, int side)
    {
        return !(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).canExtractItem(slot, itemStack, side);
    }

    private static ItemStack func_145899_c(IInventory inventory, ItemStack itemStack, int slot, int side)
    {
        ItemStack itemstack1 = inventory.getStackInSlot(slot);

        if (func_145885_a(inventory, itemStack, slot, side))
        {
            boolean flag = false;

            if (itemstack1 == null)
            {
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max >= itemStack.stackSize)
                {
                    inventory.setInventorySlotContents(slot, itemStack);
                    itemStack = null;
                }
                else
                {
                    inventory.setInventorySlotContents(slot, itemStack.splitStack(max));
                }
                flag = true;
            }
            else if (func_145894_a(itemstack1, itemStack))
            {
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max > itemstack1.stackSize)
                {
                    int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                    itemStack.stackSize -= l;
                    itemstack1.stackSize += l;
                    flag = l > 0;
                }
            }

            if (flag)
            {
                if (inventory instanceof TileEntityHopper)
                {
                    ((TileEntityHopper)inventory).func_145896_c(8);
                    inventory.markDirty();
                }

                inventory.markDirty();
            }
        }

        return itemStack;
    }

    public static EntityItem func_145897_a(World p_145897_0_, double p_145897_1_, double p_145897_3_, double p_145897_5_)
    {
        List list = p_145897_0_.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(p_145897_1_, p_145897_3_, p_145897_5_, p_145897_1_ + 1.0D, p_145897_3_ + 1.0D, p_145897_5_ + 1.0D), IEntitySelector.selectAnything);
        return list.size() > 0 ? (EntityItem)list.get(0) : null;
    }

    private static boolean func_145894_a(ItemStack p_145894_0_, ItemStack p_145894_1_)
    {
        return p_145894_0_.getItem() != p_145894_1_.getItem() ? false : (p_145894_0_.getItemDamage() != p_145894_1_.getItemDamage() ? false : (p_145894_0_.stackSize > p_145894_0_.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(p_145894_0_, p_145894_1_)));
    }

    /**
     * Gets the world X position for this hopper entity.
     */
    public double getXPos()
    {
        return (double)this.xCoord;
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    public double getYPos()
    {
        return (double)this.yCoord;
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    public double getZPos()
    {
        return (double)this.zCoord;
    }

    public void setTransferCooldown(int tCooldown)
    {
        this.transferCooldown = tCooldown;
    }

    public boolean isCoolingDown()
    {
        return this.transferCooldown > 0;
    }
    
    private void setTubePosition() {
		if (head != null) {
			head.iSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
			head.kSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
			head.jSize = yCoord - head.posY;

			head.setPosition(xCoord + CoreConstants.PIPE_MIN_POS, tubeY, zCoord + CoreConstants.PIPE_MIN_POS);
		}
	}
    @Override
	public void invalidate() {
		super.invalidate();
		destroy();
	}

	@Override
	public void validate() {
		//tileBuffer = null;
		super.validate();
	}

	//@Override
	public void destroy() {
		//tileBuffer = null;
//		pumpLayerQueues.clear();
		destroyTube();
	}
	
	@Override
	public void handleUpdatePacket(PacketUpdate packet) throws IOException {
		PacketPayload payload = packet.payload;
		ByteBuf data = payload.stream;
		aimY = data.readInt();
		tubeY = data.readFloat();
		//powered = data.readBoolean();

		setTubePosition();
	}

	
	public void sendNetworkUpdate() {
		if (worldObj != null && !worldObj.isRemote) {
//			BuildCraftCore.instance.sendToPlayers(getUpdatePacket(), worldObj,
//					xCoord, yCoord, zCoord, DefaultProps.NETWORK_UPDATE_RANGE);
			NetBus.sendToAll(getPacketUpdate());
		}
	}
	
	@Override
	public PacketUpdate getPacketUpdate() {
		return new PacketUpdate(this,getPacketPayload());
	}	
	public PacketPayload getPacketPayload() {
		PacketPayload payload = new PacketPayload(new PacketPayload.StreamWriter() {
			@Override
			public void writeData(ByteBuf buf) {
				buf.writeInt(aimY);
				buf.writeFloat((float) tubeY);
//				buf.writeBoolean(powered);
			}
		});

		return payload;
	}
}
