package net.minecraft.mangrove.mod.warfare.rifle;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * surPASS PickAxe Shovel Sword
 */
public class ItemSurpass extends Item {
    @SideOnly(Side.CLIENT)
    private IIcon[] iconIndex=new IIcon[3];
    private float directDamage=10;
    
    public ItemSurpass() {
        super();
        setCreativeTab(CreativeTabs.tabCombat);
        setUnlocalizedName("surpass");
        setHasSubtypes(true);
    }
    
    @Override
    public void getSubItems(Item item, CreativeTabs creativeTab, List listOfItems) {
        listOfItems.add(new ItemStack(item, 1, 0)); // Wood tool
        listOfItems.add(new ItemStack(item, 1, 1)); // Iron tool
        listOfItems.add(new ItemStack(item, 1, 2)); // Diamond tool        
    }
    
    @Override
    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase target, EntityLivingBase player) {
        System.out.println("hitEntity:"+target.getHealth()+":"+target);
        return super.hitEntity(p_77644_1_, target, player);
    }
    public boolean func_150897_b(Block p_150897_1_){
        return true;
    }
    public float func_150893_a(ItemStack itemStack, Block block) {
        float calcDamageInflicted = calcDamageInflicted(itemStack, block);
        return calcDamageInflicted<1.0f?1.0f:calcDamageInflicted>40.0f?40.0f:calcDamageInflicted;
    }

    private float calcDamageInflicted(ItemStack itemStack, Block block) {
        float mult=0.01f;
        
        switch(itemStack.getItemDamage()){
        case 2:
            mult=1.0f;
            break;
        case 1:
            mult=.5f;
            break;
        case 0:
            mult=.25f;
            break;
        }
        final Material material = block.getMaterial();
        if( material==Material.leaves || material==Material.wood){
            return 20.0f*mult;
        }
        if( material==Material.sand || material==Material.snow || material==Material.ice ){
            return 5.0f*mult;
        }
        if(  material== Material.clay){            
            return 10.0f*mult;
        }
        if(  material== Material.ground || material== Material.grass){
            return 5.0f*mult;
        }        
        
        if(material== Material.rock){
            if( block==Blocks.obsidian){
                return 40.0f*mult;
            }
            if( block==Blocks.netherrack){                
                return 30.0f*mult;
            }            
            if( block==Blocks.redstone_ore){                
                return 20.0f*mult;
            }
            if( block==Blocks.iron_ore){                
                return 20.0f*mult;
            }
            if( block==Blocks.gold_ore){                
                return 20.0f*mult;
            }
            if( block==Blocks.coal_ore){                
                return 20.0f*mult;
            }
            if( block==Blocks.lapis_ore){                
                return 20.0f*mult;
            }
            if( block==Blocks.emerald_ore){                
                return 20.0f*mult;
            }
            if( block==Blocks.diamond_ore){                
                return 20.0f*mult;
            }
            if( block==Blocks.quartz_ore){                
                return 20.0f*mult;
            }
            return 10.0f*mult;
        }
        if( material==Material.iron){
            return 10.0f*mult;
        }
        return 1.0f;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack iStack, World world, EntityPlayer player) {
        final int type = iStack.getItemDamage();
        EntityThrowable bolt=null;
        switch (type) {
        case 2:    
//            if (player.capabilities.isCreativeMode || player.inventory.consumeInventoryItem(Items.redstone)) {
//                bolt=new EntityBlastingBolt(world, player);
//                break;
//            }                    
        case 1:                        
        case 0:
            bolt=new EntityFlintBolt(world, player);
            break;
        }
//        System.out.println("Bolt:"+bolt);
        if (bolt!=null) {
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            world.spawnEntityInWorld(bolt);            
        }
        return iStack;
    }
    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        return this.iconIndex[p_77617_1_];
    }
    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        // TODO Auto-generated method stub
        return getUnlocalizedName()+"."+p_77667_1_.getItemDamage();
    }
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        iconIndex[0] = iconRegister.registerIcon("mangrove"+":"+"surpass_0");     
        iconIndex[1] = iconRegister.registerIcon("mangrove"+":"+"surpass_1");
        iconIndex[2] = iconRegister.registerIcon("mangrove"+":"+"surpass_2");
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit
     * damage.
     */
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double) this.directDamage, 0));
        return multimap;
    }
}
