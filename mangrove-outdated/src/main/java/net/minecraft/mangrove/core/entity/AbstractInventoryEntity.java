package net.minecraft.mangrove.core.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.InventorySupport;
import net.minecraft.mangrove.core.inventory.Permission;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class AbstractInventoryEntity extends Entity implements IInventory{
    private final InventorySupport inventorySupport;
    
    public AbstractInventoryEntity(World world) {
        super(world);
        inventorySupport=new InventorySupport(5, "container", false);
        inventorySupport.defineSlotRange(0, 180, null, Permission.BOTH, 0,1,2,3,4,5);
    }

    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        inventorySupport.readFromNBT(tag);
    }

    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        inventorySupport.writeToNBT(tag);        
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory(){
        return this.inventorySupport.getSize();
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slot){
        return this.inventorySupport.getStackInSlot(slot);
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int slot, int size){
        return this.inventorySupport.decrStackSize(slot, size);
    }
    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack=this.inventorySupport.getStackInSlot(slot);
        if( stack!=null){
            this.inventorySupport.setSlotContents(slot, null);
        }
        return stack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack itemStack){
        this.inventorySupport.setSlotContents(slot, itemStack);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName(){
        return inventorySupport.getInventoryName();
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName(){
        return this.inventorySupport.hasCustomInventoryName();
    }

    public void setInventoryName(String inventoryName){
        inventorySupport.setInventoryName(inventoryName);
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit(){
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        //return /*this.worldObj.getTileEntity((int)this.posX, (int)this.posY, (int)this.posZ) != this ? false : player.getDistanceSq((double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D) <= 64.0D;
        return true;
    }

    public void openInventory() {}

    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack itemStack){
        return true;
    }
    
    @Override
    public void markDirty() {
    }
    
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tag) {
        inventorySupport.writeToNBT(tag);   
    }
    

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tag) {
        inventorySupport.readFromNBT(tag);
    }
    
//    public void drops(){
//        for (int i1 = 0; i1 < getSizeInventory(); ++i1)
//        {
//            ItemStack itemstack = getStackInSlot(i1);
//
//            if (itemstack != null)
//            {
//                float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
//                float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
//                float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
//
//                while (itemstack.stackSize > 0)
//                {
//                    int j1 = this.field_149933_a.nextInt(21) + 10;
//
//                    if (j1 > itemstack.stackSize)
//                    {
//                        j1 = itemstack.stackSize;
//                    }
//
//                    itemstack.stackSize -= j1;
//                    EntityItem entityitem = new EntityItem(this.worldObj, (double)((float)posX + f), (double)((float)posY + f1), (double)((float)posZ + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
//
//                    if (itemstack.hasTagCompound())
//                    {
//                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
//                    }
//
//                    float f3 = 0.05F;
//                    entityitem.motionX = (double)((float)this.field_149933_a.nextGaussian() * f3);
//                    entityitem.motionY = (double)((float)this.field_149933_a.nextGaussian() * f3 + 0.2F);
//                    entityitem.motionZ = (double)((float)this.field_149933_a.nextGaussian() * f3);
//                    this.worldObj.spawnEntityInWorld(entityitem);
//                }
//            }
//        }
//    }
}
