package net.minecraft.mangrove.mod.house.block.door;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.mangrove.mod.house.MGHouseItems;
import net.minecraft.mangrove.mod.house.proxy.CommonProxy;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBoatDoor extends Block{
	 @SideOnly(Side.CLIENT)
	    private IIcon[] iconsUpper;
	    @SideOnly(Side.CLIENT)
	    private IIcon[] iconsLower;	  

	    public BlockBoatDoor()
	    {
	        super(Material.rock);
	        float f = 0.5F;
	        float f1 = 1.0F;
	        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
	        this.setBlockName("boat_door");
	        this.setBlockTextureName("door_iron");
	        //this.setHardness(30.0f);
	        this.setHarvestLevel("pickaxe", 0);
	        this.setResistance(2000.0F);
	    }

	    /**
	     * Gets the block's texture. Args: side, meta
	     */
	    @SideOnly(Side.CLIENT)
	    public IIcon getIcon(int side, int meta)
	    {
	        return this.iconsLower[0];
	    }

	    @SideOnly(Side.CLIENT)
	    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int meta)
	    {
	        if (meta != 1 && meta != 0)
	        {
	            int i1 = this.func_150012_g(world, x, y, z);
	            int j1 = i1 & 3;
	            boolean flag = (i1 & 4) != 0;
	            boolean flag1 = false;
	            boolean flag2 = (i1 & 8) != 0;

	            if (flag)
	            {
	                if (j1 == 0 && meta == 2)
	                {
	                    flag1 = !flag1;
	                }
	                else if (j1 == 1 && meta == 5)
	                {
	                    flag1 = !flag1;
	                }
	                else if (j1 == 2 && meta == 3)
	                {
	                    flag1 = !flag1;
	                }
	                else if (j1 == 3 && meta == 4)
	                {
	                    flag1 = !flag1;
	                }
	            }
	            else
	            {
	                if (j1 == 0 && meta == 5)
	                {
	                    flag1 = !flag1;
	                }
	                else if (j1 == 1 && meta == 3)
	                {
	                    flag1 = !flag1;
	                }
	                else if (j1 == 2 && meta == 4)
	                {
	                    flag1 = !flag1;
	                }
	                else if (j1 == 3 && meta == 2)
	                {
	                    flag1 = !flag1;
	                }

	                if ((i1 & 16) != 0)
	                {
	                    flag1 = !flag1;
	                }
	            }

	            return flag2 ? this.iconsUpper[flag1?1:0] : this.iconsLower[flag1?1:0];
	        }
	        else
	        {
	            return this.iconsLower[0];
	        }
	    }

	    @SideOnly(Side.CLIENT)
	    public void registerBlockIcons(IIconRegister register)
	    {
	        this.iconsUpper = new IIcon[2];
	        this.iconsLower = new IIcon[2];
	        this.iconsUpper[0] = register.registerIcon(this.getTextureName() + "_upper");
	        this.iconsLower[0] = register.registerIcon(this.getTextureName() + "_lower");
	        //this.iconsLower[0] = new IconFlipped(this.iconsUpper[0], false, true);
	        
	        this.iconsUpper[1] = new IconFlipped(this.iconsUpper[0], true, false);
	        this.iconsLower[1] = new IconFlipped(this.iconsLower[0], true, false);
	    }

	    /**
	     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	     */
	    public boolean isOpaqueCube()
	    {
	        return false;
	    }

	    public boolean getBlocksMovement(IBlockAccess blockAccess, int x, int y, int z)
	    {
	        int l = this.func_150012_g(blockAccess, x, y, z);
	        return (l & 4) != 0;
	    }

	    /**
	     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	     */
	    public boolean renderAsNormalBlock()
	    {
	        return false;
	    }

	    /**
	     * The type of render function that is called for this block
	     */
//	    public int getRenderType()
//	    {
//	        return 7;
//	    }
		public int getRenderType() {
			return CommonProxy.boatDoorRendererID;
		}

	    /**
	     * Returns the bounding box of the wired rectangular prism to render.
	     */
	    @SideOnly(Side.CLIENT)
	    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	    {
	        this.setBlockBoundsBasedOnState(world, x, y, z);
	        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	    }

	    /**
	     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	     * cleared to be reused)
	     */
	    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	    {
	        this.setBlockBoundsBasedOnState(world, x, y, z);
	        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	    }

	    /**
	     * Updates the blocks bounds based on its current state. Args: world, x, y, z
	     */
	    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	    {
	        this.func_150011_b(this.func_150012_g(world, x, y, z));
	    }

	    public int func_150013_e(IBlockAccess blockAccess, int x, int y, int z)
	    {
	        return this.func_150012_g(blockAccess, x, y, z) & 3;
	    }

	    public boolean func_150015_f(IBlockAccess blockAccess, int x, int y, int z)
	    {
	        return (this.func_150012_g(blockAccess, x, y, z) & 4) != 0;
	    }

	    private void func_150011_b(int p_150011_1_)
	    {
	        float f = 0.1875F;
	        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
	        int j = p_150011_1_ & 3;
	        boolean flag = (p_150011_1_ & 4) != 0;
	        boolean flag1 = (p_150011_1_ & 16) != 0;

	        if (j == 0)
	        {
	            if (flag)
	            {
	                if (!flag1)
	                {
	                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
	                }
	                else
	                {
	                    this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
	                }
	            }
	            else
	            {
	                this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
	            }
	        }
	        else if (j == 1)
	        {
	            if (flag)
	            {
	                if (!flag1)
	                {
	                    this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	                }
	                else
	                {
	                    this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
	                }
	            }
	            else
	            {
	                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
	            }
	        }
	        else if (j == 2)
	        {
	            if (flag)
	            {
	                if (!flag1)
	                {
	                    this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
	                }
	                else
	                {
	                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
	                }
	            }
	            else
	            {
	                this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	            }
	        }
	        else if (j == 3)
	        {
	            if (flag)
	            {
	                if (!flag1)
	                {
	                    this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
	                }
	                else
	                {
	                    this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	                }
	            }
	            else
	            {
	                this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
	            }
	        }
	    }

	    /**
	     * Called when a player hits the block. Args: world, x, y, z, player
	     */
	    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {}

	    /**
	     * Called upon block activation (right click on the block.)
	     */
	    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	    {
//	        if (this.blockMaterial == Material.iron)
//	        {
//	            return false; //Allow items to interact with the door
//	        }
//	        else
//	        {	    	
	            int i1 = this.func_150012_g(world, x, y, z);
	            int j1 = i1 & 7;
	            j1 ^= 4;
	            int i = i1 & 3;
//	            System.out.print(" Direction :");
	            Block b=null;
	            int x2=x;
	            int y2=y;
	            int z2=z;
	            switch(i){
	            case 0:
	            	if( (i1 & 16)==16){//Left
	            		x2=x+Facing.offsetsXForSide[2];
	            		y2=y;
	            		z2=z+Facing.offsetsZForSide[2];
	            		
	            	}else{//Right
	            		x2=x+Facing.offsetsXForSide[3];
	            		y2=y;
	            		z2=z+Facing.offsetsZForSide[3];
	            	}
//	            	System.out.print("WEST "+Integer.toString(i, 2)+" : "+Integer.toString(4, 2));
	            	break;
	            case 1:
	            	if( (i1 & 16)==16){//Left
	            		x2=x+Facing.offsetsXForSide[5];
	            		y2=y;
	            		z2=z+Facing.offsetsZForSide[5];
	            		
	            	}else{//Right
	            		x2=x+Facing.offsetsXForSide[4];
	            		y2=y;
	            		z2=z+Facing.offsetsZForSide[4];
	            	}
//	            	System.out.print("NORTH "+Integer.toString(i, 2)+" : "+Integer.toString(2, 2));
	            	break;
	            case 2:
	            	if( (i1 & 16)==16){//Left
	            		x2=x+Facing.offsetsXForSide[3];
	            		y2=y;
	            		z2=z+Facing.offsetsZForSide[3];
	            		
	            	}else{//Right
	            		x2=x+Facing.offsetsXForSide[2];
	            		y2=y;
	            		z2=z+Facing.offsetsZForSide[2];
	            	}
	            	
//	            	System.out.print("EAST "+Integer.toString(i, 2)+" : "+Integer.toString(5, 2));
	            	break;
	            case 3:
	            	if( (i1 & 16)==16){//Left
	            		x2=x+Facing.offsetsXForSide[4];
	            		y2=y;
	            		z2=z+Facing.offsetsZForSide[4];
	            		
	            	}else{//Right
	            		x2=x+Facing.offsetsXForSide[5];
	            		y2=y;
	            		z2=z+Facing.offsetsZForSide[5];
	            	}
//	            	System.out.print("SOUTH "+Integer.toString(i, 2)+" : "+Integer.toString(3, 2));
	            	break;
	            }
	            b=world.getBlock(x2, y2, z2);
//	            System.out.print(" : "+b+" : ");
				/*
Direction :SOUTH 11 : 100 Open :Open Y :1 Side :Right
Direction :NORTH 01 : 010 Open :Open Y :1 Side :Left
Direction :EAST  10 : 101 Open :Open Y :1 Side :Left
Direction :WEST  00 : 100 Open :Open Y :1 Side :Left
				 */
//	            System.out.print(" Open :"+(((j1 & 4) >> 2)==0?"Closed":"Open"));
//	            System.out.print(" Y :"+Integer.toBinaryString( (i1 & 8) >>3 ));
//	            System.out.print(" Side :"+(((i1 & 16) >> 4)==0?"Right":"Left"));
	            
//	            System.out.println();
	            

	            if ((i1 & 8) == 0)
	            {
	                world.setBlockMetadataWithNotify(x, y, z, j1, 2);
	                world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
	                if( b!=null && b==this){
	                	world.setBlockMetadataWithNotify(x2, y2, z2, j1, 2);
	 	                world.markBlockRangeForRenderUpdate(x2, y2, z2, x2, y2, z2);
	                }
	            }
	            else
	            {
	                world.setBlockMetadataWithNotify(x, y - 1, z, j1, 2);
	                world.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
	                if( b!=null && b==this){
	                	world.setBlockMetadataWithNotify(x2, y2-1, z2, j1, 2);
	 	                world.markBlockRangeForRenderUpdate(x2, y2-1, z2, x2, y2, z2);
	                }
	            }

	            world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
	            return true;
//	        }
	    }

	    public void func_150014_a(World wordl, int x, int y, int z, boolean p_150014_5_)
	    {
	        int l = this.func_150012_g(wordl, x, y, z);
	        boolean flag1 = (l & 4) != 0;

	        if (flag1 != p_150014_5_)
	        {
	            int i1 = l & 7;
	            i1 ^= 4;

	            if ((l & 8) == 0)
	            {
	                wordl.setBlockMetadataWithNotify(x, y, z, i1, 2);
	                wordl.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
	            }
	            else
	            {
	                wordl.setBlockMetadataWithNotify(x, y - 1, z, i1, 2);
	                wordl.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
	            }

	            wordl.playAuxSFXAtEntity((EntityPlayer)null, 1003, x, y, z, 0);
	        }
	    }

	    /**
	     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	     * their own) Args: x, y, z, neighbor Block
	     */
	    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	    {
	        int l = world.getBlockMetadata(x, y, z);

	        if ((l & 8) == 0)
	        {
	            boolean flag = false;

	            if (world.getBlock(x, y + 1, z) != this)
	            {
	                world.setBlockToAir(x, y, z);
	                flag = true;
	            }

	            if (!doesBlockHaveSolidBottomSurface(world, x, y + 2, z))
	            {
	                world.setBlockToAir(x, y, z);
	                flag = true;

	                if (world.getBlock(x, y + 1, z) == this)
	                {
	                    world.setBlockToAir(x, y + 1, z);
	                }
	            }

	            if (flag)
	            {
	                if (!world.isRemote)
	                {
	                    this.dropBlockAsItem(world, x, y, z, l, 0);
	                }
	            }
	            else
	            {
	                boolean flag1 = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);

	                if ((flag1 || block.canProvidePower()) && block != this)
	                {
	                    this.func_150014_a(world, x, y, z, flag1);
	                }
	            }
	        }
	        else
	        {
	            if (world.getBlock(x, y - 1, z) != this)
	            {
	                world.setBlockToAir(x, y, z);
	            }

	            if (block != this)
	            {
	                this.onNeighborBlockChange(world, x, y - 1, z, block);
	            }
	        }
	    }

	    public Item getItemDropped(int meta, Random p_149650_2_, int p_149650_3_)
	    {
//	        return (meta & 8) != 0 ? null : (this.blockMaterial == Material.iron ? Items.iron_door : Items.wooden_door);
	    	return MGHouseItems.item_boat_door;
	    }

	    /**
	     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
	     * x, y, z, startVec, endVec
	     */
	    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
	    {
	        this.setBlockBoundsBasedOnState(world, x, y, z);
	        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
	    }

	    /**
	     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	     */
	    public boolean canPlaceBlockAt(World world, int x, int y, int z)
	    {
	        boolean b = y >= world.getHeight() - 1;
	        
			boolean solidBottom = doesBlockHaveSolidBottomSurface(world, x, y, z);
			boolean placeable = super.canPlaceBlockAt(world, x, y-1, z) && super.canPlaceBlockAt(world, x, y-2, z);
			return b ? false : solidBottom && placeable;
	    }

	    public static boolean doesBlockHaveSolidBottomSurface(IBlockAccess blockAccess, int x, int y, int z)
	    {
	        Block block = blockAccess.getBlock(x, y, z);
	        return block.isSideSolid(blockAccess, x, y, z, ForgeDirection.DOWN);
	    }
	    
	    /**
	     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
	     * and stop pistons
	     */
	    public int getMobilityFlag()
	    {
	        return 1;
	    }

	    public int func_150012_g(IBlockAccess blockAccess, int x, int y, int z)
	    {
	        int l = blockAccess.getBlockMetadata(x, y, z);
	        boolean flag = (l & 8) != 0;
	        int i1;
	        int j1;

	        if (flag)
	        {
	            i1 = blockAccess.getBlockMetadata(x, y - 1, z);
	            j1 = l;
	        }
	        else
	        {
	            i1 = l;
	            j1 = blockAccess.getBlockMetadata(x, y + 1, z);
	        }

	        boolean flag1 = (j1 & 1) != 0;
	        return i1 & 7 | (flag ? 8 : 0) | (flag1 ? 16 : 0);
	    }

	    /**
	     * Gets an item for the block being called on. Args: world, x, y, z
	     */
	    @SideOnly(Side.CLIENT)
	    public Item getItem(World wordl, int x, int y, int z)
	    {
//	        return this.blockMaterial == Material.iron ? Items.iron_door : Items.wooden_door;
	    	 return MGHouseItems.item_boat_door;
	    }

	    /**
	     * Called when the block is attempted to be harvested
	     */
	    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
	    {
	        if (player.capabilities.isCreativeMode && (meta & 8) != 0 && world.getBlock(x, y - 1, z) == this)
	        {
	            world.setBlockToAir(x, y - 1, z);
	        }
	    }
	    
	    @Override
	    public boolean canHarvestBlock(EntityPlayer player, int meta) {
	        // TODO Auto-generated method stub
	        //return super.canHarvestBlock(player, meta);
	        System.out.println("CanHarvest");
	        return true;
	    }
	    
}
