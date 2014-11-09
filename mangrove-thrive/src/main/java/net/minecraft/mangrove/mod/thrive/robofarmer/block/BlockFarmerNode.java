package net.minecraft.mangrove.mod.thrive.robofarmer.block;

import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.cs.CSPoint3i;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotComponent;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotConnection;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotNode;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.TileFarmerKernel;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.TileFarmerNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFarmerNode extends BlockContainer implements IRobotNode{
	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon blockIconTop;
	@SideOnly(Side.CLIENT)
	protected IIcon blockIconFront;

	public BlockFarmerNode() {
		super(Material.wood);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setBlockName("farmer_node");
		setBlockTextureName("crafting_table");
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y,
	 * z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x,
			int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
//		System.out.println("BlockAdded:"+world);
		super.onBlockAdded(world, x, y, z);
		updateMetadata(world, x, y, z);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
	    boolean canPlaceBlock = super.canPlaceBlockAt(world, x, y, z);
	    if(canPlaceBlock){
	        final Set<CSPoint3i> controls = SystemUtils.findAllControl(world, x, y, z);
            canPlaceBlock=controls.size()<=1;
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
//			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			break;
		case 1:
			playerOr=ForgeDirection.WEST.getOpposite();
//			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
			break;
		case 2:
			playerOr=ForgeDirection.NORTH.getOpposite();
//			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
			break;
		case 3:
			playerOr=ForgeDirection.EAST.getOpposite();
//			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
			break;	
		}
//		System.out.println("Direction :"+playerOrientation+":"+ForgeDirection.getOrientation(playerOrientation)+":"+playerOr+":"+playerOr.ordinal());
		
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
            if (tile instanceof TileFarmerNode) {
                final TileFarmerNode tileFarmer = (TileFarmerNode) tile;            
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
    
    private void updateMetadata(IBlockAccess par1World, int par2, int par3, int par4) {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        int i1 = BlockUtils.getDirectionFromMetadata(l);
        boolean flag = !(((World) par1World).isBlockIndirectlyGettingPowered(par2, par3, par4));
        boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);

        if (flag == flag1)
            return;
        ((World) par1World).setBlockMetadataWithNotify(par2, par3, par4, i1 | ((flag) ? 0 : 8), 4);
    }
    
    @Override
    public void onBlockPreDestroy(World wordl, int x, int y, int z, int meta) {
        // TODO Auto-generated method stub
        super.onBlockPreDestroy(wordl, x, y, z, meta);
        System.out.println("Block to be destroyed at :"+x+","+y+","+z+" ("+meta+")");
    }
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
//		System.out.println("New Tile:"+world);
		return new TileFarmerNode();
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
		final ForgeDirection dir = ForgeDirection.getOrientation(meta);
		return side == 0 || side == 1
					? this.blockIconTop 
					: (side == dir.ordinal() 
						? this.blockIconFront
					    : this.blockIcon);

		// return side == 1 ? this.blockIconTop : (side == 0 ? Blocks.planks
		// .getBlockTextureFromSide(side)
		// : (side != 2 && side != 4 ? this.blockIcon
		// : this.blockIconFront));
	}
	
}
