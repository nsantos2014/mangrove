package net.minecraft.mangrove.mod.warfare.rifle;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlasterRifle extends Item {
    @SideOnly(Side.CLIENT)
    private IIcon iconIndex;
    private float directDamage=10;
//    private Item.ToolMaterial toolMaterial=Item.ToolMaterial.IRON;
//    protected float efficiencyOnProperMaterial = 4.0F;

    public ItemBlasterRifle() {
        super();
        setCreativeTab(CreativeTabs.tabCombat);
        setUnlocalizedName("blasterRifle");
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {     
        return super.onLeftClickEntity(stack, player, entity);
    }
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par3EntityPlayer.capabilities.isCreativeMode || par3EntityPlayer.inventory.consumeInventoryItem(Items.redstone)) {
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            //if (!par2World.isRemote) {
                par2World.spawnEntityInWorld(new EntityBlasterBolt(par2World, par3EntityPlayer));
            //}
        }
        return par1ItemStack;
    }
    @Override
    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase target, EntityLivingBase player) {
        System.out.println("hitEntity:"+target.getHealth()+":"+target);
        return super.hitEntity(p_77644_1_, target, player);
    }
//    @Override
//    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_,float p_77648_9_, float p_77648_10_) {
//        System.out.println("onItemUse"); 
////        return super.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
//        return false;
//    }
    
    public boolean func_150897_b(Block p_150897_1_)
    {
//        System.out.println("func_150897_b");
//        return p_150897_1_ == Blocks.obsidian ? this.toolMaterial.getHarvestLevel() == 3 : (p_150897_1_ != Blocks.diamond_block && p_150897_1_ != Blocks.diamond_ore ? (p_150897_1_ != Blocks.emerald_ore && p_150897_1_ != Blocks.emerald_block ? (p_150897_1_ != Blocks.gold_block && p_150897_1_ != Blocks.gold_ore ? (p_150897_1_ != Blocks.iron_block && p_150897_1_ != Blocks.iron_ore ? (p_150897_1_ != Blocks.lapis_block && p_150897_1_ != Blocks.lapis_ore ? (p_150897_1_ != Blocks.redstone_ore && p_150897_1_ != Blocks.lit_redstone_ore ? (p_150897_1_.getMaterial() == Material.rock ? true : (p_150897_1_.getMaterial() == Material.iron ? true : p_150897_1_.getMaterial() == Material.anvil)) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2);
        return true;
    }

    public float func_150893_a(ItemStack itemStack, Block block) {
        final Material material = block.getMaterial();
        if( material==Material.leaves || material==Material.wood){
            return 20.0f;
        }
        if( material==Material.sand || material==Material.snow || material==Material.ice ){
            return 5.0f;
        }
        if(  material== Material.clay){            
            return 10.0f;
        }
        if(material== Material.rock){
            if( block==Blocks.obsidian){
                return 40.0f;
            }
            if( block==Blocks.netherrack){                
                return 35.0f;
            }
            if( block==Blocks.obsidian){
                return 30.0f;
            }
            if( block==Blocks.redstone_ore){                
                return 20.0f;
            }
            if( block==Blocks.iron_ore){                
                return 20.0f;
            }
            if( block==Blocks.gold_ore){                
                return 20.0f;
            }
            if( block==Blocks.coal_ore){                
                return 20.0f;
            }
            if( block==Blocks.lapis_ore){                
                return 20.0f;
            }
            if( block==Blocks.emerald_ore){                
                return 20.0f;
            }
            if( block==Blocks.diamond_ore){                
                return 20.0f;
            }
            if( block==Blocks.quartz_ore){                
                return 20.0f;
            }
            return 10.0f;
        }
        if( material==Material.iron){
            return 10.0f;
        }
        
//        System.out.println("func_150893_a : "+block);
//        return p_150893_2_.getMaterial() != Material.iron && p_150893_2_.getMaterial() != Material.anvil && p_150893_2_.getMaterial() != Material.rock ? super.func_150893_a(p_150893_1_, p_150893_2_) : this.efficiencyOnProperMaterial;
        return 1.0f;
    }
    @Override
    public boolean itemInteractionForEntity(ItemStack p_111207_1_, EntityPlayer p_111207_2_, EntityLivingBase p_111207_3_) {
        System.out.println("itemInteractionForEntity");
        return super.itemInteractionForEntity(p_111207_1_, p_111207_2_, p_111207_3_);
    }
    
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        System.out.println("addInformation");
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
    }
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
        System.out.println("onBlockStartBreak");
        return super.onBlockStartBreak(itemstack, X, Y, Z, player);
    }
    
    @Override
    public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
        System.out.println("getDigSpeed:"+block.getHarvestLevel(metadata)+":"+metadata+":"+block);
        return super.getDigSpeed(itemstack, block, metadata);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {
        // TODO Auto-generated method stub
        iconIndex = p_94581_1_.registerIcon("mangrove"+":"+"rifle");
        //super.registerIcons(p_94581_1_);
    }
//    @SideOnly(Side.CLIENT)
//    @Override
//    public void registerIcons(IconRegister iconRegister)
//    {
//     iconIndex = iconRegister.registerIcon("YourModId"+":"+"itemName"); 
//     System.out.println(SenitielsSpaceMarineMod.ModId+":"+name);
//    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.iconIndex;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
    
    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.directDamage, 0));
        return multimap;
    }
}
