package net.minecraft.mangrove.mod.house.duct;

import java.io.IOException;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.house.MGHouseForge;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.TileEntityMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

public class AbstractTileEntityDuct extends TileEntity implements IHopper,ITileUpdatable{
    private int maxStacks;
    private ItemStack[] hopperItemStacks;
    private String inventoryName;
    private int transferCooldown;

    public AbstractTileEntityDuct() {
        this.maxStacks = 1;
        this.hopperItemStacks = new ItemStack[this.maxStacks];

        this.transferCooldown = -1;
    }
    
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
        this.hopperItemStacks = new ItemStack[getSizeInventory()];

        if (par1NBTTagCompound.hasKey("CustomName")) {
            this.inventoryName = par1NBTTagCompound.getString("CustomName");
        }

        this.transferCooldown = par1NBTTagCompound.getInteger("TransferCooldown");

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if ((b0 < 0) || (b0 >= this.hopperItemStacks.length))
                continue;
            this.hopperItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.hopperItemStacks.length; ++i) {
            if (this.hopperItemStacks[i] == null)
                continue;
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte) i);
            this.hopperItemStacks[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
        par1NBTTagCompound.setInteger("TransferCooldown",
                this.transferCooldown);

        if (!(func_145818_k_()))
            return;
        par1NBTTagCompound.setString("CustomName", this.inventoryName);
    }

    public int getSizeInventory() {
        return this.hopperItemStacks.length;
    }

    public ItemStack getStackInSlot(int par1) {
        if (par1 >= this.maxStacks)
            return null;
        return this.hopperItemStacks[par1];
    }

    public ItemStack decrStackSize(int par1, int par2) {
        if (this.hopperItemStacks[par1] != null) {
            if (this.hopperItemStacks[par1].stackSize <= par2) {
                ItemStack itemstack = this.hopperItemStacks[par1];
                this.hopperItemStacks[par1] = null;
                return itemstack;
            }

            ItemStack itemstack = this.hopperItemStacks[par1].splitStack(par2);

            if (this.hopperItemStacks[par1].stackSize == 0) {
                this.hopperItemStacks[par1] = null;
            }

            return itemstack;
        }

        return null;
    }

    public ItemStack getStackInSlotOnClosing(int par1) {
        if (this.hopperItemStacks[par1] != null) {
            ItemStack itemstack = this.hopperItemStacks[par1];
            this.hopperItemStacks[par1] = null;
            return itemstack;
        }

        return null;
    }

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        if (par1 >= this.maxStacks)
            return;
        this.hopperItemStacks[par1] = par2ItemStack;

        if ((par2ItemStack == null)
                || (par2ItemStack.stackSize <= getInventoryStackLimit()))
            return;
        par2ItemStack.stackSize = getInventoryStackLimit();
    }
    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.inventoryName : "container.pipe";
    }
    public void setInventoryName(String par1Str) {
        this.inventoryName = par1Str;
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName()
    {
        return this.inventoryName != null && this.inventoryName.length() > 0;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
        return true;
    }

    public void updateEntity() {
         if (this.worldObj != null && !this.worldObj.isRemote){
                --this.transferCooldown;

                if (!isCoolingDown()){
                    setTransferCooldown(0);
                    func_98045_j();
                }
            }               
    }
    
    public boolean func_98045_j() {
        if ((this.worldObj != null && !this.worldObj.isRemote)) {
            final int meta = this.getBlockMetadata();
            if ((!(isCoolingDown())) && (AbstractBlockDuct.getIsBlockNotPoweredFromMetadata(meta))) {
                boolean flag = insertItemToInventory();
                flag = (suckItemsIntoHopper(this,meta)) || (flag);
                
                if (flag) {
                    setTransferCooldown(MGHouseForge.instance.cooldownTime);
                    this.markDirty();
                    return true;
                }
            }

            return false;
        }

        return false;
    }

    private boolean insertItemToInventory() {
        IInventory iinventory = getOutputInventory();

        if (iinventory == null) {
            return false;
        }

        for (int i = 0; i < getSizeInventory(); ++i) {
            if (getStackInSlot(i) == null)
                continue;
            ItemStack itemstack = getStackInSlot(i).copy();
            ItemStack itemstack1 = insertStack(iinventory, decrStackSize(i, 1),Facing.oppositeSide[AbstractBlockDuct.getDirectionFromMetadata(getBlockMetadata())]);

            if ((itemstack1 == null) || (itemstack1.stackSize == 0)) {
                iinventory.markDirty();
                return true;
            }

            setInventorySlotContents(i, itemstack);
        }

        return false;
    }

    public static boolean suckItemsIntoHopper(IHopper hopper, int meta) {
        final int side=BlockUtils.getDirectionFromMetadata(meta);
        byte b0 = (byte) Facing.oppositeSide[side];
        IInventory iinventory = getInventoryAtSide(hopper,b0);

        if (iinventory != null) {
            
            if( iinventory instanceof IHopper){
            }else if ((iinventory instanceof ISidedInventory) && (b0 > -1)) {
                ISidedInventory isidedinventory = (ISidedInventory) iinventory;
                int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);
                
                for (int i = 0; i < aint.length; ++i) {
                    if (func_102012_a(hopper, iinventory, aint[i], side)) {
                        return true;
                    }
                }
            } else {
                int j = iinventory.getSizeInventory();

                for (int k = 0; k < j; ++k) {
                    if (func_102012_a(hopper, iinventory, k, side)) {
                        return true;
                    }
                }
            }
        } else {
            EntityItem entityitem = func_96119_a(hopper.getWorldObj(),
                    hopper.getXPos(),
                    hopper.getYPos() + 1.0D,
                    hopper.getZPos());

            if (entityitem != null) {
                return func_96114_a(hopper, entityitem);
            }
        }

        return false;
    }

    private static boolean func_102012_a(IHopper par0Hopper,IInventory par1IInventory, int slot, int par3) {
        ItemStack itemstack = par1IInventory.getStackInSlot(slot);

        if ((itemstack != null)
                && (canExtractItemFromInventory(par1IInventory, itemstack,
                        slot, par3))) {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = insertStack(par0Hopper,
                    par1IInventory.decrStackSize(slot, 1), -1);

            if ((itemstack2 == null) || (itemstack2.stackSize == 0)) {
                par1IInventory.markDirty();
                return true;
            }

            par1IInventory.setInventorySlotContents(slot, itemstack1);
        }

        return false;
    }

    public static boolean func_96114_a(IInventory par0IInventory,
            EntityItem par1EntityItem) {
        boolean flag = false;

        if (par1EntityItem == null) {
            return false;
        }

        ItemStack itemstack = par1EntityItem.getEntityItem().copy();
        ItemStack itemstack1 = insertStack(par0IInventory, itemstack, -1);

        if ((itemstack1 != null) && (itemstack1.stackSize != 0)) {
            par1EntityItem.setEntityItemStack(itemstack1);
        } else {
            flag = true;
            par1EntityItem.setDead();
        }

        return flag;
    }

    public static ItemStack insertStack(IInventory par0IInventory,
            ItemStack par1ItemStack, int par2) {
        if ((par0IInventory instanceof ISidedInventory) && (par2 > -1)) {
            ISidedInventory isidedinventory = (ISidedInventory) par0IInventory;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(par2);

            for (int j = 0; (j < aint.length) && (par1ItemStack != null)
                    && (par1ItemStack.stackSize > 0); ++j) {
                par1ItemStack = func_102014_c(par0IInventory, par1ItemStack,
                        aint[j], par2);
            }
        } else {
            int k = par0IInventory.getSizeInventory();

            for (int l = 0; (l < k) && (par1ItemStack != null)
                    && (par1ItemStack.stackSize > 0); ++l) {
                par1ItemStack = func_102014_c(par0IInventory, par1ItemStack, l,
                        par2);
            }
        }

        if ((par1ItemStack != null) && (par1ItemStack.stackSize == 0)) {
            par1ItemStack = null;
        }

        return par1ItemStack;
    }

    private static boolean canInsertItemToInventory(IInventory par0IInventory,ItemStack par1ItemStack, int slot, int side) {
        if (par0IInventory instanceof ISidedInventory) {
            ISidedInventory sidedInventory = (ISidedInventory) par0IInventory;
            return sidedInventory.canInsertItem(slot, par1ItemStack, side);
            
        }
        return (par0IInventory.isItemValidForSlot(slot, par1ItemStack));
    }

    private static boolean canExtractItemFromInventory(IInventory par0IInventory, ItemStack par1ItemStack, int slot,
            int side) {
        return ((!(par0IInventory instanceof ISidedInventory)) || (((ISidedInventory) par0IInventory).canExtractItem(slot, par1ItemStack, side)));
    }

    private static ItemStack func_102014_c(IInventory par0IInventory,
            ItemStack par1ItemStack, int slot, int side) {
        ItemStack itemstack1 = par0IInventory.getStackInSlot(slot);

        if (canInsertItemToInventory(par0IInventory, par1ItemStack, slot, side)) {
            boolean flag = false;

            if (itemstack1 == null) {
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(par1ItemStack.getMaxStackSize(), par0IInventory.getInventoryStackLimit());
                if (max >= par1ItemStack.stackSize)
                {
                    par0IInventory.setInventorySlotContents(slot, par1ItemStack);
                    par1ItemStack = null;
                }
                else
                {
                    par0IInventory.setInventorySlotContents(slot, par1ItemStack.splitStack(max));
                }
                flag = true;
//              par0IInventory.func_70299_a(slot, par1ItemStack);
//              par1ItemStack = null;
//              flag = true;
            } else if (areItemStacksEqualItem(itemstack1, par1ItemStack)) {
                
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(par1ItemStack.getMaxStackSize(), par0IInventory.getInventoryStackLimit());
                if (max > itemstack1.stackSize)
                {
                    int l = Math.min(par1ItemStack.stackSize, max - itemstack1.stackSize);
                    par1ItemStack.stackSize -= l;
                    itemstack1.stackSize += l;
                    flag = l > 0;
                }
                
//              int k = par1ItemStack.func_77976_d() - itemstack1.field_77994_a;
//              int l = Math.min(par1ItemStack.field_77994_a, k);
//              par1ItemStack.field_77994_a -= l;
//              itemstack1.field_77994_a += l;
//              flag = l > 0;
            }

            if (flag) {
                if (par0IInventory instanceof AbstractTileEntityDuct) {
                    ((AbstractTileEntityDuct) par0IInventory)
                            .setTransferCooldown(MGHouseForge.instance.cooldownTime);

                    par0IInventory.markDirty();
                }

                par0IInventory.markDirty();
            }
        }

        return par1ItemStack;
    }

    private IInventory getOutputInventory() {
        int i = AbstractBlockDuct.getDirectionFromMetadata(getBlockMetadata());
        return getInventoryAtLocation(getWorldObj(), 
                this.xCoord + Facing.offsetsXForSide[i],
                this.yCoord + Facing.offsetsYForSide[i],
                this.zCoord + Facing.offsetsZForSide[i]
            );
    }

    public static IInventory getInventoryAtSide(IHopper par0Hopper,int side) {
        return getInventoryAtLocation(par0Hopper.getWorldObj(),
                par0Hopper.getXPos()+Facing.offsetsXForSide[side], 
                par0Hopper.getYPos()+Facing.offsetsYForSide[side],
                par0Hopper.getZPos()+Facing.offsetsZForSide[side]
            );
    }

    public static EntityItem func_96119_a(World par0World, double par1,double par3, double par5) {
        List list = par0World.selectEntitiesWithinAABB(
                EntityItem.class,
                AxisAlignedBB.getBoundingBox(par1, par3, par5, par1 + 1.0D, par3 + 1.0D, par5 + 1.0D),
                IEntitySelector.selectAnything);
        return ((list.size() > 0) ? (EntityItem) list.get(0) : null);
    }

    public static IInventory getInventoryAtLocation(World world,
            double xCoord, double yCoord, double zCoord) {
        IInventory iinventory = null;
        int i = MathHelper.floor_double(xCoord);
        int j = MathHelper.floor_double(yCoord);
        int k = MathHelper.floor_double(zCoord);
        TileEntity tileentity = world.getTileEntity(i, j, k);

        if ((tileentity != null) && (tileentity instanceof IInventory)) {
            iinventory = (IInventory) tileentity;

            if (iinventory instanceof TileEntityChest) {
                Block block = world.getBlock(i, j, k);

                if (block instanceof BlockChest) {
                    iinventory = ((BlockChest) block).func_149951_m(world,
                            i, j, k);
                }
            }
        } else{
            Block block = world.getBlock(i, j, k);
            if( block==Blocks.air){
                final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(i-0.5D, j-0.5D, k-0.5D, i+0.5D, j+0.5d, k+0.5d);
                final List list = world.getEntitiesWithinAABB(IInventory.class, boundingBox);
                if(list.size()==1){
                    return (IInventory)list.get(0);
                }
            }
        }

        if (iinventory == null) {
             List list = world.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1.0D, yCoord + 1.0D, zCoord + 1.0D), IEntitySelector.selectInventories);

                if (list != null && list.size() > 0)
                {
                    iinventory = (IInventory)list.get(world.rand.nextInt(list.size()));
                }
//          List list = par0World.func_94576_a(
//                  (Entity) null,
//                  AxisAlignedBB.getEntitiesWithinAABBExcludingEntity().func_72299_a(par1, par3, par5,
//                          par1 + 1.0D, par3 + 1.0D, par5 + 1.0D),
//                  IEntitySelector.field_96566_b);
//
//          if ((list != null) && (list.size() > 0)) {
//              iinventory = (IInventory) list.get(par0World.field_73012_v
//                      .nextInt(list.size()));
//          }
        }

        return iinventory;
    }

    private static boolean areItemStacksEqualItem(ItemStack par0ItemStack,
            ItemStack par1ItemStack) {
        return par0ItemStack.getItem() != par1ItemStack.getItem() ? 
                    false : 
                        (par0ItemStack.getItemDamage() != par1ItemStack.getItemDamage() ? 
                                false : 
                                    (par0ItemStack.stackSize > par0ItemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(par0ItemStack, par1ItemStack)));
        
//      return ((par0ItemStack.field_77994_a > par0ItemStack.func_77976_d()) ? false
//              : (par0ItemStack.func_77960_j() != par1ItemStack.func_77960_j()) ? false
//                      : (par0ItemStack.func_77973_b() != par1ItemStack
//                              .func_77973_b()) ? false : ItemStack
//                              .func_77970_a(par0ItemStack, par1ItemStack));
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

    public void setTransferCooldown(int par1) {
        this.transferCooldown = par1;
    }

    public boolean isCoolingDown() {
        return (this.transferCooldown > 0);
    }

    public String func_145825_b() {
        return ((func_145818_k_()) ? this.inventoryName
                : "container.hopperduct");
    }

    public boolean func_145818_k_() {
        return ((this.inventoryName != null) && (this.inventoryName.length() > 0));
    }

    public void func_70295_k_() {
    }

    public void func_70305_f() {
    }

//    public void handlePacket(DuctPacket ductPacket) {
//        //ductPacket.direction;
//        final Block block = getWorldObj().getBlock(ductPacket.x, ductPacket.y, ductPacket.z);
//        int l = getWorldObj().getBlockMetadata(ductPacket.x, ductPacket.y, ductPacket.z);
//        
//        int i1 = AbstractBlockDuct.getDirectionFromMetadata(ductPacket.direction);
//                    
//        if (i1 >= 6)
//            i1 = 0;
//        if (i1 < 0)
//            i1 = 5;
//        l &= -8;
//
//        getWorldObj().setBlockMetadataWithNotify(ductPacket.x, ductPacket.y, ductPacket.z, l | i1, 4);
//    }

    public void setDirection(int direction) {        
        updateDirection(direction);
        fireChangeDirectionEvent(direction);        
    }

    private void updateDirection(int direction) {
        final Block block = getWorldObj().getBlock(xCoord, yCoord, zCoord);
        int l = getWorldObj().getBlockMetadata(xCoord, yCoord, zCoord);
        
        int i1 = AbstractBlockDuct.getDirectionFromMetadata(direction);
                    
        if (i1 >= 6)
            i1 = 0;
        if (i1 < 0)
            i1 = 5;
        l &= -8;

        getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, l | i1, 2);
    }

    
    protected final void fireChangeDirectionEvent(int direction) {
        if (this.worldObj.isRemote) {
            final JsonObject evt = JSON.newObject();
            evt.addProperty("FireChangeDirectionEvent", true);
            evt.addProperty("EventSide", "CLIENT");
            evt.addProperty("direction", direction);
            NetBus.sendToServer(new TileEntityMessage(this, evt));
        } 
    }
    
    @Override
    public JsonObject getTilePacketData() {
        final JsonObject data = JSON.newObject();
        return data;
    }

    @Override
    public void handleClientUpdate(JsonObject data) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleServerUpdate(JsonObject data) throws IOException {      
        if (data.has("FireChangeDirectionEvent")) {
            updateDirection(data.get("direction").getAsInt());
        }
    }

}
