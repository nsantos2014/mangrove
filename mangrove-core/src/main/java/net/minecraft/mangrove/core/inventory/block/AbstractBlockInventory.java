package net.minecraft.mangrove.core.inventory.block;

import java.util.Random;

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
//    	dropInventory(worldIn, pos, state);
    	preDestroyBlock(worldIn, pos, state);
    	super.breakBlock(worldIn, pos, state);    	
    }
    
    public static void preDestroyBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof IInventory && !world.isRemote) {
			dropItems(world, (IInventory) tile, pos);
			wipeInventory((IInventory) tile);
		}
		
	}
    public static void dropItems(World world, ItemStack stack, BlockPos pos) {
		if (stack == null || stack.stackSize <= 0) {
			return;
		}

		float f1 = 0.7F;
		double d = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
		double d1 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
		double d2 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
		EntityItem entityitem = new EntityItem(world, pos.getX() + d, pos.getY() + d1, pos.getZ() + d2, stack);
		entityitem.setDefaultPickupDelay();

		world.spawnEntityInWorld(entityitem);
	}

	public static void dropItems(World world, IInventory inv, BlockPos pos) {
		for (int slot = 0; slot < inv.getSizeInventory(); ++slot) {
			ItemStack items = inv.getStackInSlot(slot);

			if (items != null && items.stackSize > 0) {
				dropItems(world, inv.getStackInSlot(slot).copy(), pos);
			}
		}
	}

	public static void wipeInventory(IInventory inv) {
		for (int slot = 0; slot < inv.getSizeInventory(); ++slot) {
			inv.setInventorySlotContents(slot, null);
		}
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

}
