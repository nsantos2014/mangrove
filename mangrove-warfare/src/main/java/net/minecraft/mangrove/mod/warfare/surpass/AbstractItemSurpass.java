package net.minecraft.mangrove.mod.warfare.surpass;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.warfare.MGWarfareForge;
import net.minecraft.mangrove.mod.warfare.rifle.EntityFlintBolt;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Multimap;

/**
 * 
 * surPASS PickAxe Shovel Sword
 */
public abstract class AbstractItemSurpass extends Item {
    private float directDamage=10;
	private float damageModifier;
	private String name;
    
    protected AbstractItemSurpass(String name, float directDamage, float damageModifier) {
        super();
		this.name = name;
		this.damageModifier = damageModifier;
        this.directDamage=directDamage;
        GameRegistry.registerItem(this, name, MGWarfareForge.ID);
        setUnlocalizedName(name);
        setCreativeTab(CreativeTabs.tabCombat);
    }
    
    public String getName() {
		return name;
	}
    
//    @Override
//    public void getSubItems(Item item, CreativeTabs creativeTab, List listOfItems) {
//        listOfItems.add(new ItemStack(item, 1, 0)); // Wood tool
//        listOfItems.add(new ItemStack(item, 1, 1)); // Iron tool
//        listOfItems.add(new ItemStack(item, 1, 2)); // Diamond tool        
//    }
//    
    @Override
    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase target, EntityLivingBase player) {        
        return super.hitEntity(p_77644_1_, target, player);
    }@Override
    public boolean canHarvestBlock(Block blockIn) {
//    	return super.canHarvestBlock(blockIn);
    	return true;
    }
    
//    public boolean func_150897_b(Block p_150897_1_){
//        return true;
//    }
     @Override
    public float getStrVsBlock(ItemStack itemStack, Block block) {
//    	// TODO Auto-generated method stub
//    	return super.getStrVsBlock(stack, block);
    	 float calcDamageInflicted = calcDamageInflicted(itemStack, block);
         return calcDamageInflicted<1.0f?1.0f:calcDamageInflicted>40.0f?40.0f:calcDamageInflicted;
    }
//    public float func_150893_a(ItemStack itemStack, Block block) {
//        float calcDamageInflicted = calcDamageInflicted(itemStack, block);
//        return calcDamageInflicted<1.0f?1.0f:calcDamageInflicted>40.0f?40.0f:calcDamageInflicted;
//    }

    private float calcDamageInflicted(ItemStack itemStack, Block block) {
//        float mult=0.01f;
//        
//        switch(itemStack.getItemDamage()){
//        case 2:
//            mult=1.0f;
//            break;
//        case 1:
//            mult=.5f;
//            break;
//        case 0:
//            mult=.25f;
//            break;
//        }
        final Material material = block.getMaterial();
        if( material==Material.leaves || material==Material.wood){
            return 20.0f*damageModifier;
        }
        if( material==Material.sand || material==Material.snow || material==Material.ice ){
            return 5.0f*damageModifier;
        }
        if(  material== Material.clay){            
            return 10.0f*damageModifier;
        }
        if(  material== Material.ground || material== Material.grass){
            return 5.0f*damageModifier;
        }        
        
        if(material== Material.rock){
            if( block==Blocks.obsidian){
                return 40.0f*damageModifier;
            }
            if( block==Blocks.netherrack){                
                return 30.0f*damageModifier;
            }            
            if( block==Blocks.redstone_ore){                
                return 20.0f*damageModifier;
            }
            if( block==Blocks.iron_ore){                
                return 20.0f*damageModifier;
            }
            if( block==Blocks.gold_ore){                
                return 20.0f*damageModifier;
            }
            if( block==Blocks.coal_ore){                
                return 20.0f*damageModifier;
            }
            if( block==Blocks.lapis_ore){                
                return 20.0f*damageModifier;
            }
            if( block==Blocks.emerald_ore){                
                return 20.0f*damageModifier;
            }
            if( block==Blocks.diamond_ore){                
                return 20.0f*damageModifier;
            }
            if( block==Blocks.quartz_ore){                
                return 20.0f*damageModifier;
            }
            return 10.0f*damageModifier;
        }
        if( material==Material.iron){
            return 10.0f*damageModifier;
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
    
//    /**
//     * Gets an icon index based on an item's damage value
//     */
//    @SideOnly(Side.CLIENT)
//    public IIcon getIconFromDamage(int p_77617_1_) {
//        return this.iconIndex[p_77617_1_];
//    }
//    @Override
//    public String getUnlocalizedName(ItemStack p_77667_1_) {
//        // TODO Auto-generated method stub
//        return getUnlocalizedName()+"."+p_77667_1_.getItemDamage();
//    }
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void registerIcons(IIconRegister iconRegister) {
//        iconIndex[0] = iconRegister.registerIcon("mangrove"+":"+"surpass_0");     
//        iconIndex[1] = iconRegister.registerIcon("mangrove"+":"+"surpass_1");
//        iconIndex[2] = iconRegister.registerIcon("mangrove"+":"+"surpass_2");
//    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit
     * damage.
     */
    public Multimap getAttributeModifiers(ItemStack itemStack) {
        Multimap multimap = super.getAttributeModifiers(itemStack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double) this.directDamage, 0));
        return multimap;
    }
}
