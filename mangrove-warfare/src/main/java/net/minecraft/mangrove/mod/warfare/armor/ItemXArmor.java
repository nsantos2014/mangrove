package net.minecraft.mangrove.mod.warfare.armor;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.warfare.MGWarfareForge;
import net.minecraft.mangrove.mod.warfare.MGWarfareItems;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemXArmor extends ItemArmor{
    /** Holds the 'base' maxDamage that each armorType have. */
    private static final int[] maxDamageArray = new int[] {11, 16, 15, 13};
    private static final String[] CLOTH_OVERLAY_NAMES = new String[] {"leather_helmet_overlay", "leather_chestplate_overlay", "leather_leggings_overlay", "leather_boots_overlay"};
    public static final String[] EMPTY_SLOT_NAMES = new String[] {"empty_armor_slot_helmet", "empty_armor_slot_chestplate", "empty_armor_slot_leggings", "empty_armor_slot_boots"};
    
    /** Stores the armor type: 0 is helmet, 1 is plate, 2 is legs and 3 is boots */
    public final int armorType;
    /** Holds the amount of damage that the armor reduces at full durability. */
    public final int damageReduceAmount;
    /**
     * Used on RenderPlayer to select the correspondent armor to be rendered on the player: 0 is cloth, 1 is chain, 2 is
     * iron, 3 is diamond and 4 is gold.
     */
    public final int renderIndex;
        
    private int maxDamageFactor;
    private int[] damageReductionAmountArray;
    private ArmorMaterial material;
    

    public ItemXArmor(ArmorMaterial material,String name,int renderIndex, int armorType) {
        super(material,renderIndex,armorType);
        this.material=ArmorMaterial.CHAIN;
        this.armorType = armorType;
        this.renderIndex = renderIndex;        
        
        this.maxDamageFactor=64;
        this.damageReductionAmountArray=new int[]{10, 10, 10, 10};
        this.damageReduceAmount =this.damageReductionAmountArray[armorType];
        
        this.setMaxDamage(ItemXArmor.maxDamageArray[armorType] * this.maxDamageFactor);
        this.maxStackSize = 1;
        GameRegistry.registerItem(this, name, MGWarfareForge.ID);
        setUnlocalizedName(name);        
        this.setCreativeTab(CreativeTabs.tabCombat);
        //BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserBehavior);
    }
    
//    @Override
//    public String getUnlocalizedName(ItemStack p_77667_1_) {
//        int i = MathHelper.clamp_int(p_77667_1_.getItemDamage(), 0, 15);
//        return super.getUnlocalizedName(p_77667_1_)+ "." + i;
//    }
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        if (p_82790_2_ > 0)
        {
            return 16777215;
        }
        else
        {
            int j = this.getColor(p_82790_1_);

            if (j < 0)
            {
                j = 16777215;
            }

            return j;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return this.material == ArmorMaterial.LEATHER;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        //return this.material.getEnchantability();
        return 25;
    }

    /**
     * Return the armor material for this armor item.
     */
    public ItemArmor.ArmorMaterial getArmorMaterial()
    {
        return super.getArmorMaterial();
    }

    /**
     * Return whether the specified armor ItemStack has a color.
     */
    public boolean hasColor(ItemStack p_82816_1_)
    {
        return this.material != ArmorMaterial.LEATHER ? false : (!p_82816_1_.hasTagCompound() ? false : (!p_82816_1_.getTagCompound().hasKey("display", 10) ? false : p_82816_1_.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
    }

    /**
     * Return the color for the specified armor ItemStack.
     */
    public int getColor(ItemStack p_82814_1_)
    {
        if (this.material != ArmorMaterial.LEATHER)
        {
            return -1;
        }
        else
        {
            NBTTagCompound nbttagcompound = p_82814_1_.getTagCompound();

            if (nbttagcompound == null)
            {
                return 10511680;
            }
            else
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
                return nbttagcompound1 == null ? 10511680 : (nbttagcompound1.hasKey("color", 3) ? nbttagcompound1.getInteger("color") : 10511680);
            }
        }
    }



    /**
     * Remove the color from the specified armor ItemStack.
     */
    public void removeColor(ItemStack p_82815_1_)
    {
        if (this.material == ArmorMaterial.LEATHER)
        {
            NBTTagCompound nbttagcompound = p_82815_1_.getTagCompound();

            if (nbttagcompound != null)
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

                if (nbttagcompound1.hasKey("color"))
                {
                    nbttagcompound1.removeTag("color");
                }
            }
        }
    }

    public void func_82813_b(ItemStack p_82813_1_, int p_82813_2_)
    {
        if (this.material != ArmorMaterial.CHAIN)
        {
            throw new UnsupportedOperationException("Can\'t dye non-leather!");
        }
        else
        {
            NBTTagCompound nbttagcompound = p_82813_1_.getTagCompound();

            if (nbttagcompound == null)
            {
                nbttagcompound = new NBTTagCompound();
                p_82813_1_.setTagCompound(nbttagcompound);
            }

            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (!nbttagcompound.hasKey("display", 10))
            {
                nbttagcompound.setTag("display", nbttagcompound1);
            }

            nbttagcompound1.setInteger("color", p_82813_2_);
        }
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_)
    {
        return this.material.getRepairItem() == p_82789_2_.getItem() ? true : super.getIsRepairable(p_82789_1_, p_82789_2_);
    }
    

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        int i = EntityLiving.getArmorPosition(p_77659_1_) - 1;
        ItemStack itemstack1 = p_77659_3_.getCurrentArmor(i);

        if (itemstack1 == null)
        {
            p_77659_3_.setCurrentItemOrArmor(i + 1, p_77659_1_.copy());  //Forge: Vanilla bug fix associated with fixed setCurrentItemOrArmor indexs for players.
            p_77659_1_.stackSize = 0;
        }

        return p_77659_1_;
    }
//    
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if( this==MGWarfareItems.x_helmet){
            player.addPotionEffect(new PotionEffect(Potion.waterBreathing.getId(), 1));
            player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 1));
        }
        super.onArmorTick(world, player, itemStack);
    }
    @Override
    public boolean isFull3D() {
        return true;
    }
//    public static enum XArmorMaterial
//    {
//        CLOTH(5, new int[]{1, 3, 2, 1}, 15),
//        CHAIN(15, new int[]{2, 5, 4, 1}, 12),
//        IRON(15, new int[]{2, 6, 5, 2}, 9),
//        GOLD(7, new int[]{2, 5, 3, 1}, 25),
//        DIAMOND(33, new int[]{3, 8, 6, 3}, 10);
//        /**
//         * Holds the maximum damage factor (each piece multiply this by it's own value) of the material, this is the
//         * item damage (how much can absorb before breaks)
//         */
//        private int maxDamageFactor;
//        /**
//         * Holds the damage reduction (each 1 points is half a shield on gui) of each piece of armor (helmet, plate,
//         * legs and boots)
//         */
//        private int[] damageReductionAmountArray;
//        /** Return the enchantability factor of the material */
//        private int enchantability;
//
//        private static final String __OBFID = "CL_00001768";
//
//        //Added by forge for custom Armor materials.
//        public Item customCraftingMaterial = null;
//
//        private XArmorMaterial(int p_i1827_3_, int[] p_i1827_4_, int p_i1827_5_)
//        {
//            this.maxDamageFactor = p_i1827_3_;
//            this.damageReductionAmountArray = p_i1827_4_;
//            this.enchantability = p_i1827_5_;
//        }
//
//        /**
//         * Returns the durability for a armor slot of for this type.
//         */
//        public int getDurability(int p_78046_1_)
//        {
//            return ItemXArmor.maxDamageArray[p_78046_1_] * this.maxDamageFactor;
//        }
//
//        /**
//         * Return the damage reduction (each 1 point is a half a shield on gui) of the piece index passed (0 = helmet, 1
//         * = plate, 2 = legs and 3 = boots)
//         */
//        public int getDamageReductionAmount(int p_78044_1_)
//        {
//            return this.damageReductionAmountArray[p_78044_1_];
//        }
//
//        /**
//         * Return the enchantability factor of the material.
//         */
//        public int getEnchantability()
//        {
//            return this.enchantability;
//        }
//
//        public Item func_151685_b()
//        {
//            switch (this)
//            {
//                case CLOTH:   return Items.leather;
//                case CHAIN:   return Items.iron_ingot;
//                case GOLD:    return Items.gold_ingot;
//                case IRON:    return Items.iron_ingot;
//                case DIAMOND: return Items.diamond;
//                default:      return customCraftingMaterial;
//            }
//        }
//    }
}
