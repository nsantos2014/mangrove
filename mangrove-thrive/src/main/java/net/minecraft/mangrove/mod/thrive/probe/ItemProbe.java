package net.minecraft.mangrove.mod.thrive.probe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemProbe extends ItemMapBase{

	private String name="probe";
	
	public ItemProbe() {
		super();
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(MGThriveForge.ID + "_" + name);
		setCreativeTab(CreativeTabs.tabRedstone);
//		setFull3D();
		
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
//		return super.getItemUseAction(stack);
		return EnumAction.BLOCK;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn,
			EntityPlayer playerIn) {
		System.out.println("onItemRightClick");
		playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn,
			World worldIn, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ) {
		System.out.println("onItemUse");
		return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
	}
	
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 100;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player,	int useRemaining) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean isFirstPersonView = mc.thePlayer.getUniqueID().equals(player.getUniqueID()) &&
				mc.gameSettings.thirdPersonView == 0;
		if(isFirstPersonView) {
			/* ... */
			return new ModelResourceLocation("minecraft:beacon", "inventory"){
				
				
			};
		} else {
			/* ... */
		}
		return null;
	}
}
