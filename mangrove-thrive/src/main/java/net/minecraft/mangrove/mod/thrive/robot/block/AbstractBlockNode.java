package net.minecraft.mangrove.mod.thrive.robot.block;

import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.cs.CSPoint3i;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robot.IRobotNode;
import net.minecraft.mangrove.mod.thrive.robot.entity.AbstractTileRobotNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class AbstractBlockNode extends BlockContainer implements IRobotNode{
    @SideOnly(Side.CLIENT)
    protected IIcon blockIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon blockIconTop;
    @SideOnly(Side.CLIENT)
    protected IIcon blockIconFront;
    
       private boolean field_149996_a;

    public AbstractBlockNode() {
        super(Material.wood);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);        
        setBlockTextureName("crafting_table");
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

     /**
     * If this block doesn't render as an ordinary block it will return False
     * (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock() {
        return false;        
    }
    
    public int getRenderType() {
        return CommonProxy.blockFarmerNodeRenderId;
    }
    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether
     * or not to render the shared face of two adjacent blocks and also whether
     * the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {
        return false;
    }
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y,
     * z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x,int y, int z) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
//      System.out.println("BlockAdded:"+world);
        super.onBlockAdded(world, x, y, z);
        updateMetadata(world, x, y, z);
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        boolean canPlaceBlock = super.canPlaceBlockAt(world, x, y, z);
        if(canPlaceBlock){
            final Set<CSPoint3i> controls = SystemUtils.findAllControl(world, x, y, z);
            canPlaceBlock=controls.size()<=1;
            if( canPlaceBlock){
                canPlaceBlock=SystemUtils.checkAllNodes(getClass(),world, x, y, z);
            }
//            canPlaceBlock=false;
//            for (int i1 = 0; !canPlaceBlock && (i1 < 6); ++i1){
//                Block j1 = world.getBlock(x + Facing.offsetsXForSide[i1], y + Facing.offsetsYForSide[i1], z + Facing.offsetsZForSide[i1]);
//                if( j1 instanceof IRobotComponent){
//                    IRobotComponent component=(IRobotComponent)j1;
//                    
//                    canPlaceBlock=true;
//                }
//            }
        }
        return canPlaceBlock;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z,
            EntityLivingBase player, ItemStack iStack) {

        int playerOrientation = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        ForgeDirection playerOr=ForgeDirection.UNKNOWN;
        switch (playerOrientation) {
        case 0:
            playerOr=ForgeDirection.SOUTH.getOpposite();
//          world.setBlockMetadataWithNotify(x, y, z, 2, 2);
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
            break;
        case 1:
            playerOr=ForgeDirection.WEST.getOpposite();
//          world.setBlockMetadataWithNotify(x, y, z, 5, 2);
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
            break;
        case 2:
            playerOr=ForgeDirection.NORTH.getOpposite();
//          world.setBlockMetadataWithNotify(x, y, z, 3, 2);
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
            break;
        case 3:
            playerOr=ForgeDirection.EAST.getOpposite();
//          world.setBlockMetadataWithNotify(x, y, z, 4, 2);
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
            break;  
        }
//      System.out.println("Direction :"+playerOrientation+":"+ForgeDirection.getOrientation(playerOrientation)+":"+playerOr+":"+playerOr.ordinal());
        
    }
    
    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int meta) {
        super.onPostBlockPlaced(world, x, y, z, meta);
        System.out.println("Block placed at :"+x+","+y+","+z+" ("+meta+")");
    }
    @Override
    public void onNeighborBlockChange(World world, int x, int y,int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);
        System.out.println("Block neighbour changed at :"+x+","+y+","+z+" ("+block+")");
        if(block.canProvidePower()){
            final TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof AbstractTileRobotNode) {
                final AbstractTileRobotNode tileFarmer = (AbstractTileRobotNode) tile;            
                if( world.getStrongestIndirectPower(x, y, z)>0){
                    tileFarmer.doStart();
                }else{
                    tileFarmer.doStop();
                }
                //System.out.println("Block neighbour changed at :"+x+","+y+","+z+" ("+block+")");
            }
        }
        updateMetadata(world, x, y, z);
    }
    
    @Override
    public void updateNetwork(IBlockAccess world, int x, int y, int z) {
//        System.out.println("Update network ("+x+","+y+","+z+") "+this);
        updateMetadata(world, x, y, z);
    }
    
    private void updateMetadata(IBlockAccess world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        int i1 = BlockUtils.getDirectionFromMetadata(l);
        boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);
        //boolean flag = !(((World) par1World).isBlockIndirectlyGettingPowered(x, y, z));
        
        CSPoint3i controlPos = SystemUtils.findFirstControl((World) world, x, y, z);
        boolean flag = false;
        if(controlPos==null && flag1){
            System.out.println("Update Metadata: No Control and "+flag1);
            return;
        }else if(controlPos!=null){
            flag = !(((World) world).isBlockIndirectlyGettingPowered(controlPos.x, controlPos.y, controlPos.z));
        }
        if (flag == flag1){
            System.out.println("Update Metadata: Same Value "+flag1);
            return;
        }
        System.out.println("Update Metadata:"+flag);
        ((World) world).setBlockMetadataWithNotify(x, y, z, i1 | ((flag) ? 0 : 8), 2);
    }
    
    @Override
    public void onBlockPreDestroy(World wordl, int x, int y, int z, int meta) {
        // TODO Auto-generated method stub
        super.onBlockPreDestroy(wordl, x, y, z, meta);
        System.out.println("Block to be destroyed at :"+x+","+y+","+z+" ("+meta+")");
    }   

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon("planks_birch");
        this.blockIconTop = register.registerIcon("piston_top_normal");
        this.blockIconFront = register.registerIcon("log_birch_top");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }
}
