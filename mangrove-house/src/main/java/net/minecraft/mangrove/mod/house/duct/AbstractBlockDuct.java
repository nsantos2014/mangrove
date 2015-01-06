package net.minecraft.mangrove.mod.house.duct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.house.MGHouseBlocks;
import net.minecraft.mangrove.mod.house.MGHouseForge;
import net.minecraft.mangrove.mod.house.proxy.CommonProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AbstractBlockDuct extends BlockContainer{
    protected final Random rnd = new Random();

    @SideOnly(Side.CLIENT)
    private static final Map<String,IIcon> ICONS=new HashMap<String, IIcon>();

    public AbstractBlockDuct() {
        super(Material.iron);
        
        setCreativeTab(CreativeTabs.tabRedstone);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(2.0F);
        setResistance(8.0F);        
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2,int par3, int par4) {
        float x1 = 0.25F;
        float x2 = 1.0F - x1;
        float y1 = 0.25F;
        float y2 = 1.0F - y1;
        float z1 = 0.25F;
        float z2 = 1.0F - z1;

        if (isDuctReceivingFrom(par1IBlockAccess, par2 - 1, par3, par4, 5))
            x1 = 0.0F;
        if (isDuctReceivingFrom(par1IBlockAccess, par2 + 1, par3, par4, 4))
            x2 = 1.0F;
        if (isDuctReceivingFrom(par1IBlockAccess, par2, par3, par4 - 1, 3))
            z1 = 0.0F;
        if (isDuctReceivingFrom(par1IBlockAccess, par2, par3, par4 + 1, 2))
            z2 = 1.0F;
        if (isDuctReceivingFrom(par1IBlockAccess, par2, par3 - 1, par4, 1))
            y1 = 0.0F;
        if (isDuctReceivingFrom(par1IBlockAccess, par2, par3 + 1, par4, 0))
            y2 = 1.0F;

        int meta = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        int dir = getDirectionFromMetadata(meta);
        switch (dir) {
        case 0:
            y1 = 0.0F;
            break;
        case 1:
            y2 = 1.0F;
            break;
        case 2:
            z1 = 0.0F;
            break;
        case 3:
            z2 = 1.0F;
            break;
        case 4:
            x1 = 0.0F;
            break;
        case 5:
            x2 = 1.0F;
        }

        setBlockBounds(x1, y1, z1, x2, y2, z2);
    }

    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4,
            AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        float b = 0.25F;
        float b2 = 1.0F - b;
        float e = 0.375F;
        float e2 = 1.0F - e;

        setBlockBounds(b, b, b, b2, b2, b2);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);

        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
            EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
        super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase,
                par6ItemStack);

        if (!(par6ItemStack.hasDisplayName()))
            return;
        AbstractTileEntityDuct tileentityhopperduct = getHopperDuctTile(
                par1World, par2, par3, par4);
        tileentityhopperduct.setInventoryName(par6ItemStack.getDisplayName());
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        updateMetadata(par1World, par2, par3, par4);
    }
    
   

    public abstract TileEntity createNewTileEntity(World var1, int var2);

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4,
            EntityPlayer par5EntityPlayer, int par6, float par7, float par8,
            float par9) {
        if (par1World.isRemote) {
            return true;
        }

        if (par5EntityPlayer.getHeldItem() != null) {
            Item held = par5EntityPlayer.getHeldItem().getItem();

            if (held == Item.getItemFromBlock(this))
                return false;
            if (held == Item.getItemFromBlock(MGHouseBlocks.duct_filter))
                return false;
            if (held == Item.getItemFromBlock(Blocks.hopper))
                return false;

        }

        if ((par5EntityPlayer.getHeldItem() != null)
                && (par5EntityPlayer.getHeldItem().getItem() == Items.stick)) {
            int l = par1World.getBlockMetadata(par2, par3, par4);
            int i1 = getDirectionFromMetadata(l);
            --i1;

            if (i1 >= 6)
                i1 = 0;
            if (i1 < 0)
                i1 = 5;
            l &= -8;

            par1World.setBlockMetadataWithNotify(par2, par3, par4, l | i1, 4);
            
            //System.out.println("Update direction:"+Facing.facings[i1]);

            return true;
        }

        par5EntityPlayer.openGui(MGHouseForge.instance, 0, par1World, par2, par3, par4);

        return true;
    }

    public void onBlockClicked(World par1World, int par2, int par3, int par4,EntityPlayer par5EntityPlayer) {
        if (par1World.isRemote)
            return;

        if ((par5EntityPlayer.getHeldItem() == null) || (par5EntityPlayer.getHeldItem().getItem() != Items.stick))
            return;
        int l = par1World.getBlockMetadata(par2, par3, par4);
        int i1 = getDirectionFromMetadata(l);
        if (par5EntityPlayer.isSneaking())
            --i1;
        else
            ++i1;
        if (i1 >= 6)
            i1 = 0;
        if (i1 < 0)
            i1 = 5;
        l &= -8;

        par1World.setBlockMetadataWithNotify(par2, par3, par4, l | i1, 4);
    }

    public void onNeighborChange(IBlockAccess world, int x, int y, int z,
            int tileX, int tileY, int tileZ) {
        updateMetadata(world, x, y, z);
    }

    private void updateMetadata(IBlockAccess par1World, int par2, int par3,
            int par4) {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        int i1 = getDirectionFromMetadata(l);
        boolean flag = !(((World) par1World).isBlockIndirectlyGettingPowered(par2, par3, par4));
        boolean flag1 = getIsBlockNotPoweredFromMetadata(l);

        if (flag == flag1)
            return;
        ((World) par1World).setBlockMetadataWithNotify(par2, par3, par4, i1
                | ((flag) ? 0 : 8), 4);
    }

    public void breakBlock(World par1World, int par2, int par3, int par4,
            Block par5, int par6) {
        AbstractTileEntityDuct tileentityhopperduct = (AbstractTileEntityDuct) par1World
                .getTileEntity(par2, par3, par4);

        if (tileentityhopperduct != null) {
            for (int j1 = 0; j1 < tileentityhopperduct.getSizeInventory(); ++j1) {
                ItemStack itemstack = tileentityhopperduct.getStackInSlot(j1);

                if (itemstack == null)
                    continue;
                float f = this.rnd.nextFloat() * 0.8F + 0.1F;
                float f1 = this.rnd.nextFloat() * 0.8F + 0.1F;
                float f2 = this.rnd.nextFloat() * 0.8F + 0.1F;

                while (itemstack.stackSize > 0) {
                    int k1 = this.rnd.nextInt(21) + 10;

                    if (k1 > itemstack.stackSize) {
                        k1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= k1;
                    EntityItem entityitem = new EntityItem(par1World, par2 + f,
                            par3 + f1, par4 + f2, new ItemStack(
                                    itemstack.getItem(), k1,
                                    itemstack.getItemDamage()));

                    if (itemstack.hasTagCompound()) {
                        entityitem.getEntityItem().setTagCompound(
                                (NBTTagCompound) itemstack.getTagCompound().copy());
                    }

                    float f3 = 0.05F;
                    entityitem.motionX = ((float) this.rnd
                            .nextGaussian() * f3);
                    entityitem.motionY = ((float) this.rnd
                            .nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = ((float) this.rnd
                            .nextGaussian() * f3);
                    par1World.spawnEntityInWorld(entityitem);
                }

            }

            par1World.func_147453_f(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    public int getRenderType() {
        return CommonProxy.ductRendererID;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public static int getDirectionFromMetadata(int par0) {
        return (par0 & 0x7);
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2,
            int par3, int par4, int par5) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        //System.out.println("Icon side:"+side+" meta:"+meta+"==>"+this);
        int direction=getDirectionFromMetadata(meta);
        if( side==Facing.oppositeSide[direction]){
            return this.ICONS.get("duct_back");
        }
        if( side==direction){
            return this.ICONS.get("duct_front");
        }
        switch (direction) {
        case 0:
            return this.ICONS.get("duct_down");
        case 1:
            return this.ICONS.get("duct_up");
        case 3:
        case 5:
            return this.ICONS.get("duct_right");
        case 2:     
        case 4:
            return this.ICONS.get("duct_left");
        }
        return this.ICONS.get("hopper_outside");
    }

    public static boolean getIsBlockNotPoweredFromMetadata(int par0) {
        return ((par0 & 0x8) != 8);
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4,
            int par5) {
        return Container.calcRedstoneFromInventory(getHopperDuctTile(par1World, par2, par3,
                par4));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
//      this.hopperIcon = par1IconRegister.registerIcon("hopper_outside");
//      this.hopperTopIcon = par1IconRegister.registerIcon("hopper_top");
//      this.hopperInsideIcon = par1IconRegister.registerIcon("hopper_inside");
//      this.hopperIcon = par1IconRegister.registerIcon("glass");
//      this.hopperTopIcon = par1IconRegister.registerIcon("glass");
//      this.hopperInsideIcon = par1IconRegister.registerIcon("glass");
//      this.hopperIcon = par1IconRegister.registerIcon("duct_outside");
//      this.hopperTopIcon = par1IconRegister.registerIcon("duct_top");
//      this.hopperInsideIcon = par1IconRegister.registerIcon("duct_inside");
        
        ICONS.put("hopper_outside",par1IconRegister.registerIcon("hopper_outside"));
        ICONS.put("hopper_top",par1IconRegister.registerIcon("hopper_top"));
        ICONS.put("hopper_inside",par1IconRegister.registerIcon("hopper_inside"));
        
        ICONS.put("duct_outside",par1IconRegister.registerIcon("mangrove:duct_outside"));
        ICONS.put("duct_connect",par1IconRegister.registerIcon("mangrove:duct_outside"));
        ICONS.put("duct_top",par1IconRegister.registerIcon("mangrove:duct_top"));
        ICONS.put("duct_inside",par1IconRegister.registerIcon("mangrove:duct_inside"));
        
        ICONS.put("duct_back",par1IconRegister.registerIcon("mangrove:duct_back"));
        ICONS.put("duct_front",par1IconRegister.registerIcon("mangrove:duct_front"));
        ICONS.put("duct_left",par1IconRegister.registerIcon("mangrove:duct_left"));
        ICONS.put("duct_right",par1IconRegister.registerIcon("mangrove:duct_right"));
        ICONS.put("duct_up",par1IconRegister.registerIcon("mangrove:duct_up"));
        ICONS.put("duct_down",par1IconRegister.registerIcon("mangrove:duct_down"));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(String key) {
        if( ICONS.containsKey(key)){
            return ICONS.get(key);
        }
        return ICONS.get("hopper_outside");
//      return ((par0Str.equals("duct_inside")) ? MyMod.hopperduct.hopperInsideIcon
//              : (par0Str.equals("duct_outside")) ? MyMod.hopperduct.hopperIcon
//                      : null);
    }
    
    @SideOnly(Side.CLIENT)
    public static IIcon getHopperIcon(String key) {
        if( ICONS.containsKey(key)){
            return ICONS.get(key);
        }
        return ICONS.get("hopper_outside");
//      return ((par0Str.equals("duct_inside")) ? MyMod.hopperduct.hopperInsideIcon
//              : (par0Str.equals("duct_outside")) ? MyMod.hopperduct.hopperIcon
//                      : null);
    }

    public static AbstractTileEntityDuct getHopperDuctTile(
            IBlockAccess par0IBlockAccess, int par1, int par2, int par3) {
        return ((AbstractTileEntityDuct) par0IBlockAccess.getTileEntity(par1,
                par2, par3));
    }

    public static boolean isDuctReceivingFrom(IBlockAccess blockaccess, int x,int y, int z, int side) {
        //Block b = blockaccess.getBlock(x, y, z);
        final TileEntity tile = blockaccess.getTileEntity(x, y, z);
        if( tile instanceof IHopper){
            int dir=BlockUtils.getDirectionFromMetadata(tile.getBlockMetadata());
            return dir==side;
        }else if( tile instanceof ISidedInventory){
            ISidedInventory invTile=(ISidedInventory) tile;
            int[] slots = invTile.getAccessibleSlotsFromSide(side);
            if(slots!=null && slots.length>0){
                return true;
            }
        }else if( tile instanceof IInventory){
            //IInventory invTile=(IInventory) tile;
            return true;
        }

        return false;
    }
}
