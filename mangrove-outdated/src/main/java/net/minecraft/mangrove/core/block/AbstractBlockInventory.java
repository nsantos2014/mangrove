package net.minecraft.mangrove.core.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractBlockInventory extends BlockContainer{
    
    protected AbstractBlockInventory(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    private final Random field_149933_a = new Random();
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    	super.breakBlock(worldIn, pos, state);
    	dropInventory(worldIn, pos, state);
    }
    
    protected final void dropInventory(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity != null && tileEntity instanceof IInventory){
            IInventory tileEntityInventory = (IInventory)tileEntity;
            for (int i1 = 0; i1 < tileEntityInventory.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tileEntityInventory.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = this.field_149933_a.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(worldIn, (double)((float)pos.getX() + f), (double)((float)pos.getY()+ f1), (double)((float)pos.getZ() + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.field_149933_a.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.field_149933_a.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.field_149933_a.nextGaussian() * f3);
                        worldIn.spawnEntityInWorld(entityitem);
                    }
                }
            }

            worldIn.setBlockState(pos, state);
            
        }
    }
//    @Override
//    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
//        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
//        dropInventory(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
//    }

//    protected final void dropInventory(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_) {
//        TileEntity tileEntity = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
//        if(tileEntity != null && tileEntity instanceof IInventory){
//            IInventory tileEntityInventory = (IInventory)tileEntity;
//            for (int i1 = 0; i1 < tileEntityInventory.getSizeInventory(); ++i1)
//            {
//                ItemStack itemstack = tileEntityInventory.getStackInSlot(i1);
//
//                if (itemstack != null)
//                {
//                    float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
//                    float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
//                    float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
//
//                    while (itemstack.stackSize > 0)
//                    {
//                        int j1 = this.field_149933_a.nextInt(21) + 10;
//
//                        if (j1 > itemstack.stackSize)
//                        {
//                            j1 = itemstack.stackSize;
//                        }
//
//                        itemstack.stackSize -= j1;
//                        EntityItem entityitem = new EntityItem(p_149749_1_, (double)((float)p_149749_2_ + f), (double)((float)p_149749_3_ + f1), (double)((float)p_149749_4_ + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
//
//                        if (itemstack.hasTagCompound())
//                        {
//                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
//                        }
//
//                        float f3 = 0.05F;
//                        entityitem.motionX = (double)((float)this.field_149933_a.nextGaussian() * f3);
//                        entityitem.motionY = (double)((float)this.field_149933_a.nextGaussian() * f3 + 0.2F);
//                        entityitem.motionZ = (double)((float)this.field_149933_a.nextGaussian() * f3);
//                        p_149749_1_.spawnEntityInWorld(entityitem);
//                    }
//                }
//            }
//
//            p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
//        }
//    }
}
