package net.minecraft.mangrove.mod.thrive.feeder;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFeeder extends BlockContainer{
    @SideOnly(Side.CLIENT)
    private IIcon blockIconTop;
    @SideOnly(Side.CLIENT)
    private IIcon blockIconFront;
    private int p_149727_2_;
    
    public BlockFeeder() {
        super(Material.wood);
        setCreativeTab(CreativeTabs.tabRedstone);
        setBlockName("feeder");
        setBlockTextureName("crafting_table");
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(2.0F);
        setResistance(8.0F);
    }
     /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
        return side == 1 ? this.blockIconTop : (side == 0 ? Blocks.planks.getBlockTextureFromSide(side) : (side != 2 && side != 4 ? this.blockIcon : this.blockIconFront));
    }
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register){
        this.blockIcon = register.registerIcon("planks_birch");
        this.blockIconTop = register.registerIcon("piston_top_normal");
        this.blockIconFront = register.registerIcon("planks_birch");
    }
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z);
    }
    
    @Override
    public void onBlockClicked(World world, int x,
            int y, int z, EntityPlayer player) {
    
        super.onBlockClicked(world, x, y, z,player);        
    }
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_){
        super.onBlockActivated(world, x, y, z, player, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
        if (player.isSneaking()) {
            return false;
        }
        if (!world.isRemote){
            player.openGui(MGThriveForge.instance, 0, world,x, y, z);
        }
        return true;
    }
    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {      
        return new TileEntityFeeder();
    }
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2,
            int par3, int par4, int par5) {
        return true;
    }
    
    public void onNeighborChange(IBlockAccess world, int x, int y, int z,
            int tileX, int tileY, int tileZ) {
        updateMetadata(world, x, y, z);
    }

    private void updateMetadata(IBlockAccess par1World, int par2, int par3,
            int par4) {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        int i1 = BlockUtils.getDirectionFromMetadata(l);
        boolean flag = !(((World) par1World).isBlockIndirectlyGettingPowered(par2, par3, par4));
        boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);

        if (flag == flag1)
            return;
        ((World) par1World).setBlockMetadataWithNotify(par2, par3, par4, i1
                | ((flag) ? 0 : 8), 4);
    }
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        updateMetadata(par1World, par2, par3, par4);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2,
            int par3, int par4) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4,
            AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        float f = 0.125F;
        setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
