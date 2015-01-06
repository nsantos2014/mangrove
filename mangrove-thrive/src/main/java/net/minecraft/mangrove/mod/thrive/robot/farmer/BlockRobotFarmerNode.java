package net.minecraft.mangrove.mod.thrive.robot.farmer;

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
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robot.IRobotComponent;
import net.minecraft.mangrove.mod.thrive.robot.IRobotConnection;
import net.minecraft.mangrove.mod.thrive.robot.IRobotNode;
import net.minecraft.mangrove.mod.thrive.robot.block.AbstractBlockNode;
import net.minecraft.mangrove.mod.thrive.robot.block.SystemUtils;
import net.minecraft.mangrove.mod.thrive.robot.entity.TileRobotKernel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRobotFarmerNode extends AbstractBlockNode{


	public BlockRobotFarmerNode() {
		super();
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setBlockName("farmer_node");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileRobotFarmerNode();
	}
}
