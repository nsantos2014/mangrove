package net.minecraft.mangrove.mod.vehicles.mav.i1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.InventorySupport;
import net.minecraft.mangrove.core.inventory.Permission;
import net.minecraft.mangrove.mod.vehicles.MGVehiclesForge;
import net.minecraft.mangrove.mod.vehicles.MGVehiclesItems;
import net.minecraft.mangrove.mod.vehicles.network.KeyboardMessage;
import net.minecraft.mangrove.mod.vehicles.proxy.CommonProxy;
import net.minecraft.mangrove.mod.vehicles.proxy.IKeyControlable;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityVehicleBase extends EntityLiving implements IInventory, IKeyControlable {
    protected static final Set<Item> itemRecover       = new HashSet<Item>();

    static {
        itemRecover.add(Items.stone_axe);
        itemRecover.add(Items.iron_axe);
        itemRecover.add(Items.golden_axe);
        itemRecover.add(Items.diamond_axe);
        itemRecover.add(Items.stone_pickaxe);
        itemRecover.add(Items.iron_pickaxe);
        itemRecover.add(Items.golden_pickaxe);
        itemRecover.add(Items.diamond_pickaxe);
    }

    private static final IAttribute  jumpStrength = (new RangedAttribute("horse.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
//    public int                       field_110278_bp;
//    public int                       field_110279_bq;

    protected boolean                jumping;
    protected boolean                collectNearby     = false;
    protected boolean                digAround     = false;

    private final InventorySupport   inventorySupport;
    /**
     * "The higher this value, the more likely the horse is to be tamed next time a player rides it."
     */
    protected float                  jumpPower;
    private boolean                  field_110294_bI;
    private int                      field_110285_bP;
    private float                    speed=.6f;
    

    public EntityVehicleBase(World par1World) {
        super(par1World);
        this.targetTasks.taskEntries.clear();
        this.tasks.taskEntries.clear();
        this.setSize(0.98F, 0.7F);
        this.yOffset = this.height / 2.0F;
        this.isImmuneToFire = true;
        this.speed=0.1f;
//        this.setChested(false);
        inventorySupport = new InventorySupport(5, "container", false);
        inventorySupport.defineSlotRange(0, 180, null, Permission.BOTH, 0, 1, 2, 3, 4, 5);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(0));
        this.dataWatcher.addObject(19, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(20, Integer.valueOf(0));
        this.dataWatcher.addObject(21, String.valueOf(""));
        this.dataWatcher.addObject(22, Integer.valueOf(0));
    }

    @Override
    public void dismountEntity(Entity entity) {
        // System.out.println("Dismount");
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity par1Entity) {
        return par1Entity.boundingBox;
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox() {
        if (this.riddenByEntity != null) {
            return this.boundingBox.func_111270_a(this.riddenByEntity.boundingBox);
        }
        return this.boundingBox;
    }

    public void setCollectNearby() {
        this.collectNearby = true;
    }
    public void toggleDigAround() {
        this.digAround = !this.digAround ;
        if( this.digAround){
            this.speed=.1f;
        }else{
            this.speed=.6f;
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName() {
        if (this.hasCustomNameTag()) {
            return this.getCustomNameTag();
        } else {
        }
        return "d";
    }

    private boolean getWatchableBoolean(int par1) {
        return (this.dataWatcher.getWatchableObjectInt(16) & par1) != 0;
    }

    private void setWatchableBoolean(int par1, boolean par2) {
        int j = this.dataWatcher.getWatchableObjectInt(16);

        if (par2) {
            this.dataWatcher.updateObject(16, Integer.valueOf(j | par1));
        } else {
            this.dataWatcher.updateObject(16, Integer.valueOf(j & ~par1));
        }
    }

    public boolean isFloating() {
        return getWatchableBoolean(1);
    }

    public void setFloating(boolean b) {
        setWatchableBoolean(1, b);
    }

    @Override
    public double getMountedYOffset() {
        double out;
        out = (double) this.height * 0.0D - 0.30000001192092896D;
        return out;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public void setJumping(boolean par1) {
        this.jumping = par1;
    }

    public boolean allowLeashing() {
        return false;
    }

    public int func_110241_cb() {
        return this.dataWatcher.getWatchableObjectInt(22);
    }

//    /**
//     * 0 = iron, 1 = gold, 2 = diamond
//     */
//    private int getHorseArmorIndex(ItemStack par1ItemStack) {
//        if (par1ItemStack == null) {
//            return 0;
//        } else {
//            Item item = par1ItemStack.getItem();
//            return item == Items.iron_horse_armor ? 1 : (item == Items.golden_horse_armor ? 2 : (item == Items.diamond_horse_armor ? 3 : 0));
//        }
//    }

//    public boolean isEatingHaystack() {
//        return this.getWatchableBoolean(32);
//    }
//
//    public boolean isRearing() {
//        return this.getWatchableBoolean(64);
//    }

    public boolean func_110205_ce() {
        return this.getWatchableBoolean(16);
    }

//    public void func_146086_d(ItemStack p_146086_1_) {
//        this.dataWatcher.updateObject(22, Integer.valueOf(this.getHorseArmorIndex(p_146086_1_)));
//    }

    public void func_110242_l(boolean par1) {
        this.setWatchableBoolean(16, par1);
    }

//    public void setChested(boolean par1) {
//       this.setWatchableBoolean(8, par1);
//    }

    public void pressKey(ID id, EntityPlayer player) {
        if (worldObj.isRemote /* && (key == 6 || key == 8 || key == 9) */) {
//            MyMod.packetPipeline.sendToServer(new VehicleControlPacket(id));
            NetBus.sendToServer(new KeyboardMessage(id));
            return;
        }
//        System.out.println("Called : " + id);
        switch (id) {
        case JUMP: // jump;
            // setJumpPower((int) (this.jumpPower*100.0f));
            setJumpPower(90);
//            setJumping(true);
            break;
        case INVENTORY:
            openGUI(player);
            break;
        case FLOAT:
            // setJumpPower(10);
            // setJumping(true);
            setFloating(!isFloating());
            break;
        case COLLECT:
            // setJumpPower(10);
            // setJumping(true);
            setCollectNearby();
            break;
        case DIG:
            // setJumpPower(10);
            // setJumping(true);
            toggleDigAround();
            break;    
        case INC_SPEED:
            incSpeed();
            break;
        case DEC_SPEED:
            decSpeed();
            break;
         default:
             break;
        }
    }

    @Override
    public boolean canBeSteered() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damageSource, float par2) {
        if (this.isEntityInvulnerable()) {
            System.out.println("Invulnerable");
            return false;
        }
        final Entity dsEntity = damageSource.getEntity();
        if (this.riddenByEntity != null && this.riddenByEntity.equals(dsEntity)) {
            System.out.println("Frendly fire");
            return false;
        } else if (!this.worldObj.isRemote && !this.isDead) {
            if (dsEntity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) dsEntity;
                final ItemStack heldItem = player.getHeldItem();

                final Item item = heldItem == null ? null : heldItem.getItem();

                if ((heldItem != null) && (itemRecover.contains(item))) {
                    this.dropChests();
                    this.func_145778_a(MGVehiclesItems.mav, 1, 0.0F);
                    this.setDead();
                    return true;
                }
            } else {
                System.out.println("1ATTACKED : " + damageSource.damageType + ":" + damageSource.getSourceOfDamage());
                super.attackEntityFrom(damageSource, par2);
            }
        } else {
            System.out.println("2ATTACKED : " + damageSource.damageType + ":" + damageSource.getSourceOfDamage());
            super.attackEntityFrom(damageSource, par2);
        }
        return true;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed() {
        return this.riddenByEntity == null;
    }

    public boolean prepareChunkForSpawn() {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posZ);
        this.worldObj.getBiomeGenForCoords(i, j);
        return true;
    }

    public void dropChests() {
        if (!this.worldObj.isRemote) {
            for (int i1 = 0; i1 < getSizeInventory(); ++i1) {
                ItemStack itemstack = getStackInSlot(i1);

                if (itemstack != null) {
                    float f = this.getRNG().nextFloat() * 0.8F + 0.1F;
                    float f1 = this.getRNG().nextFloat() * 0.8F + 0.1F;
                    float f2 = this.getRNG().nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int j1 = this.getRNG().nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(this.worldObj, (double) ((float) posX + f), (double) ((float) posY + f1), (double) ((float) posZ + f2),
                                new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) this.getRNG().nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) this.getRNG().nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) this.getRNG().nextGaussian() * f3);
                        this.worldObj.spawnEntityInWorld(entityitem);
                    }
                }
            }
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1) {
        System.out.print("Vehicle Fall :" + this.worldObj.isRemote + ": " + par1);
        if (par1 > 1.0F) {
            this.playSound("mob.horse.land", 0.4F, 1.0F);
        }

        int i = MathHelper.ceiling_float_int(par1 * 0.25F - 5.0F);

        if (i > 0) {
            this.attackEntityFrom(DamageSource.fall, (float) i * 10);
            System.out.println(" ouch!!!");
            if (this.riddenByEntity != null) {
                this.riddenByEntity.attackEntityFrom(DamageSource.fall, (float) i / 2);
            }

            Block block = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.2D - (double) this.prevRotationYaw),
                    MathHelper.floor_double(this.posZ));

            if (block.getMaterial() != Material.air) {
                Block.SoundType soundtype = block.stepSound;
                this.worldObj.playSoundAtEntity(this, soundtype.getStepResourcePath(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
        System.out.println();
    }

    /**
     * Called by InventoryBasic.onInventoryChanged() on a array that is never filled.
     */
    public void onInventoryChanged(InventoryBasic par1InventoryBasic) {
        int i = this.func_110241_cb();
//        boolean flag = this.isHorseSaddled();
        // this.func_110232_cE();

        if (this.ticksExisted > 20) {
            if (i == 0 && i != this.func_110241_cb()) {
                this.playSound("mob.horse.armor", 0.5F, 1.0F);
            } else if (i != this.func_110241_cb()) {
                this.playSound("mob.horse.armor", 0.5F, 1.0F);
            }
//            if (!flag && this.isHorseSaddled()) {
//                this.playSound("mob.horse.leather", 0.5F, 1.0F);
//            }
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere() {
        this.prepareChunkForSpawn();
        return false;
    }

    public double getJumpStrength() {
        return this.getEntityAttribute(jumpStrength).getAttributeValue();
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "mob.horse.donkey.death";
    }

    protected Item getDropItem() {
        return MGVehiclesItems.mav;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "mob.horse.donkey.hit";
    }

//    public boolean isHorseSaddled() {
//        return /* this.getHorseWatchableBoolean(4) */true;
//    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound() {
        return "mob.horse.donkey.idle";
    }

    protected String getAngrySoundName() {
        return "mob.horse.donkey.angry";
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
        Block.SoundType soundtype = p_145780_4_.stepSound;

        if (this.worldObj.getBlock(p_145780_1_, p_145780_2_ + 1, p_145780_3_) == Blocks.snow_layer) {
            soundtype = Blocks.snow_layer.stepSound;
        }

        if (!p_145780_4_.getMaterial().isLiquid()) {
            int l = 0/* this.getHorseType() */;

            if (this.riddenByEntity != null && l != 1 && l != 2) {
                ++this.field_110285_bP;

                if (this.field_110285_bP > 5 && this.field_110285_bP % 3 == 0) {
                    this.playSound("mob.horse.gallop", soundtype.getVolume() * 0.15F, soundtype.getPitch());

                    if (l == 0 && this.rand.nextInt(10) == 0) {
                        this.playSound("mob.horse.breathe", soundtype.getVolume() * 0.6F, soundtype.getPitch());
                    }
                } else if (this.field_110285_bP <= 5) {
                    this.playSound("mob.horse.wood", soundtype.getVolume() * 0.15F, soundtype.getPitch());
                }
            } else if (soundtype == Block.soundTypeWood) {
                this.playSound("mob.horse.wood", soundtype.getVolume() * 0.15F, soundtype.getPitch());
            } else {
                this.playSound("mob.horse.soft", soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(jumpStrength);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(53.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22499999403953552D);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk() {
        return 6;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 0.8F;
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval() {
        return 400;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_110239_cn() {
        return true;
    }

    public void openGUI(EntityPlayer player) {
        if (!this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == player) /*
                                                                                                       * && this . isTame ( )
                                                                                                       */) {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY);
            int k = MathHelper.floor_double(this.posZ);
            player.openGui(MGVehiclesForge.instance, CommonProxy.idGuiMAV, this.worldObj, i, j, k);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer) {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
        if (par1EntityPlayer.isSneaking()) {
            this.openGUI(par1EntityPlayer);
            return true;
        } else if (this.riddenByEntity != null) {
            return super.interact(par1EntityPlayer);
        } else {
            if (/* this.func_110253_bW() && */this.riddenByEntity == null) {
                if (itemstack != null && itemstack.interactWithEntity(par1EntityPlayer, this)) {
                    return true;
                } else {
                    this.mountEntity(par1EntityPlayer);
                    return true;
                }
            } else {
                return super.interact(par1EntityPlayer);
            }
        }
    }

    private void mountEntity(EntityPlayer par1EntityPlayer) {
        par1EntityPlayer.rotationYaw = this.rotationYaw;
        par1EntityPlayer.rotationPitch = this.rotationPitch;

        if (!this.worldObj.isRemote) {
            par1EntityPlayer.mountEntity(this);
        }
    }

    public boolean func_110259_cr() {
        return true;
    }

    public boolean func_110229_cs() {
        return false;
    }

    /**
     * Dead and sleeping entities cannot move
     */
    protected boolean isMovementBlocked() {
        return this.riddenByEntity != null;
    }

    public boolean func_110222_cv() {
        return false;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack) {
        return false;
    }

//    private void func_110210_cH() {
//        this.field_110278_bp = 1;
//    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource par1DamageSource) {
        super.onDeath(par1DamageSource);

        if (!this.worldObj.isRemote) {
            this.dropChestItems();
        }
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (this.isEntityAlive() && this.isInsideOfMaterial(Material.water)) {
            if (this.riddenByEntity != null) {
                this.riddenByEntity.setAir(300);
            }
        }
    }

    public void collectItemsNearby() {
        final AxisAlignedBB expand = this.boundingBox.expand(6, 6, 6);

        final List list2 = this.worldObj.getEntitiesWithinAABB(EntityItem.class, expand);
        if (!list2.isEmpty()) {
            for (Object o : list2) {
                if (o instanceof EntityItem) {
                    final EntityItem entityItem = (EntityItem) o;
                    final ItemStack itemStack = entityItem.getEntityItem();
                    if (itemStack != null) {
                        ItemStack result = inventorySupport.mergeStack(itemStack);
                        if (result == null) {
                            entityItem.setDead();
                        } else {
                            entityItem.setEntityItemStack(result);
                        }
                    }
                }
            }
            System.out.println(list2);

        }
    }
    
    public void digBlocksNearby() {
        final int xPos=MathHelper.floor_double(this.posX);
        final int yPos=MathHelper.floor_double(this.posY);
        final int zPos=MathHelper.floor_double(this.posZ);
        
        final int minX = Math.min(xPos+1, xPos-1);
        final int maxX = Math.max(xPos+1, xPos-1);
        
        final int minZ = Math.min(zPos+1, zPos-1);
        final int maxZ = Math.max(zPos+1, zPos-1);
        
        final int minY = yPos;
        final int maxY = yPos+1;
        System.out.println("Dig ");
        int k=0;
        for(int y=maxY; y>=minY; y--) {
            for(int x=minX;x<= maxX;x++){
                for(int z=minZ;z<=maxZ;z++){
                    final Block block = worldObj.getBlock(x,y, z);
                    final int meta= worldObj.getBlockMetadata(x,y, z);
                    if( block==Blocks.sand|| block==Blocks.gravel || block==Blocks.dirt||block==Blocks.grass|| block==Blocks.stained_hardened_clay || block==Blocks.hardened_clay){
                        final ArrayList<ItemStack> dropsList = block.getDrops(worldObj, x,y, z,meta, 0);
                        
                        if (dropsList != null && !dropsList.isEmpty()) {
                            int firstEmptySlot = getSizeInventory();
                            for (ItemStack itemStack : dropsList) {
                                for (int invSlot = 0; invSlot < getSizeInventory(); invSlot++) {
                                    final ItemStack iStack=getStackInSlot(invSlot);
                                    if (iStack == null) {
                                        firstEmptySlot = Math.min(invSlot, firstEmptySlot);
                                    } else if (iStack.isItemEqual(itemStack)) {
                                        int newStackSize = iStack.stackSize+ itemStack.stackSize;
                                        if (newStackSize <= itemStack.getMaxStackSize()) {
                                            iStack.stackSize = newStackSize;
                                            itemStack.stackSize = 0;
//                                            this.inventoryFull = false;
                                            break;
                                        }
                                    }
                                }
                                if (itemStack.stackSize != 0 && firstEmptySlot == getSizeInventory()) { // if item
                                                                                    // stack has
                                                                                    // not bean
                                                                                    // added to
                                                                                    // stack and
                                                                                    // no empty
                                                                                    // slot
                                                                                    // found
                                                                                    // then no
                                                                                    // room to
                                                                                    // item
//                                    this.inventoryFull = true;
                                    toggleDigAround();
                                    // drillMatrix.markBlocked();
                                } else if (itemStack.stackSize != 0) { // if item stack has not
                                                                        // bean added to stack
                                                                        // then set first empty
                                                                        // slot
                                    setInventorySlotContents(firstEmptySlot, itemStack.copy());                         
//                                    this.inventoryFull = false;
                                }
                            }
                
                            // if(this.inventory[2]==null){
                            // this.inventory[2]=dropsList.get(0);
                            // this.onInventoryChanged();
                            //
                            // }else if( this.inventory[2].stackSize==0){
                            // this.inventory[2].stackSize++;
                            // this.onInventoryChanged();
                            //
                            // }
                        }
                        this.worldObj.setBlock(x,y,z, Blocks.air, 0, 3);
                    }
                    
//                    System.out.println("Block: "+(k++)+" ("+x+","+z+"):"+block);
                    
                    
                }
            }
        }
//        
//        final List list2 = this.worldObj.getB(EntityItem.class, expand);
//        if (!list2.isEmpty()) {
//            for (Object o : list2) {
//                if (o instanceof EntityItem) {
//                    final EntityItem entityItem = (EntityItem) o;
//                    final ItemStack itemStack = entityItem.getEntityItem();
//                    if (itemStack != null) {
//                        ItemStack result = inventorySupport.mergeStack(itemStack);
//                        if (result == null) {
//                            entityItem.setDead();
//                        } else {
//                            entityItem.setEntityItemStack(result);
//                        }
//                    }
//                }
//            }
//            System.out.println(list2);
//
//        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(90) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
                if (this.riddenByEntity != null && (this.riddenByEntity instanceof EntityPlayer)) {
                    EntityPlayer player = (EntityPlayer) this.riddenByEntity;
                    player.heal(1.0f);
                }
            }
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();
        if (this.worldObj.isRemote && this.dataWatcher.hasChanges()) {
            this.dataWatcher.func_111144_e();
        }

        if (this.worldObj.isRemote) {
            int px = MathHelper.floor_double(prevPosX);
            int py = MathHelper.floor_double(prevPosY) + 1;
            int pz = MathHelper.floor_double(prevPosZ);
            int x = MathHelper.floor_double(this.posX);
            int y = MathHelper.floor_double(this.posY) + 1;
            int z = MathHelper.floor_double(this.posZ);
            if (px != x || py != y || pz != z) {
//                Block prevBlock = this.worldObj.getBlock(px, py, pz);
//                if (prevBlock == MyMod.light_air) {
//                    this.worldObj.setBlock(px, py, pz, Blocks.air);
//                } else if (prevBlock == MyMod.light_water) {
//                    this.worldObj.setBlock(px, py, pz, Blocks.water);
//                }
//                Block block = this.worldObj.getBlock(x, y, z);
//                if (block == Blocks.air) {
//                    this.worldObj.setBlock(x, y, z, MyMod.light_air);
//                } else if (block == Blocks.water) {
//                    this.worldObj.setBlock(x, y, z, MyMod.light_water);
//                }
            }
        }

        if (collectNearby) {
            collectItemsNearby();
            collectNearby = false;
        }
        
        if (digAround) {
            digBlocksNearby();
//            digAround = false;
        }

    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    private boolean func_110200_cJ() {
        return this.riddenByEntity == null && this.ridingEntity == null 
                && !this.func_110222_cv() && this.getHealth() >= this.getMaxHealth();
    }

    public void setEating(boolean par1) {
        this.setWatchableBoolean(32, par1);
    }

    public void dropChestItems() {
        this.dropChests();
    }

//    private void dropItemsInChest(Entity par1Entity, AnimalChest par2AnimalChest) {
//        if (par2AnimalChest != null && !this.worldObj.isRemote) {
//            for (int i = 0; i < par2AnimalChest.getSizeInventory(); ++i) {
//                ItemStack itemstack = par2AnimalChest.getStackInSlot(i);
//
//                if (itemstack != null) {
//                    this.entityDropItem(itemstack, 0.0F);
//                }
//            }
//        }
//    }

    /**
     * Moves the entity based on the specified heading. Args: strafe, forward
     */
    public void moveEntityWithHeading(float par1, float par2) {

        if (this.riddenByEntity != null) {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            par1 = ((EntityLivingBase) this.riddenByEntity).moveStrafing * 0.5F;
            par2 = ((EntityLivingBase) this.riddenByEntity).moveForward;

            if (par2 <= 0.0F) {
                par2 *= 0.25F;
                this.field_110285_bP = 0;
            }

            if (this.onGround && this.jumpPower == 0.0F /*&& this.isRearing() */&& !this.field_110294_bI) {
                par1 = 0.0F;
                par2 = 0.0F;
            }
//            System.out.println("Jump :" + this.jumpPower+" "+this.isJumping());
            if (this.jumpPower > 0.0F && !this.isJumping()) {
                
                this.motionY = this.getJumpStrength() * (double) this.jumpPower;
//                System.out.println("Jump :" + this.jumpPower+" motion Y:"+this.motionY);
//                if (this.isPotionActive(Potion.jump)) {
//                    this.motionY += (double) ((float) (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
//                }

                this.setJumping(true);
                this.isAirBorne = true;

                if (par2 > 0.0F) {
                    float f2 = MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F);
                    float f3 = MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F);
                    this.motionX += (double) (-0.4F * f2 * this.jumpPower);
                    this.motionZ += (double) (0.4F * f3 * this.jumpPower);
                    this.playSound("mob.horse.jump", 0.4F, 1.0F);
                }

                this.jumpPower = 0.0F;
            }

            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (!this.worldObj.isRemote) {
                this.setAIMoveSpeed((float) this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()*1.6f);
                super.moveEntityWithHeading(par1, par2);
            }

            if (this.onGround) {
                this.jumpPower = 0.0F;
                this.setJumping(false);
            }
//
//            this.prevLimbSwingAmount = this.limbSwingAmount;
//            double d1 = this.posX - this.prevPosX;
//            double d0 = this.posZ - this.prevPosZ;
//            float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;
//
//            if (f4 > 1.0F) {
//                f4 = 1.0F;
//            }
//
//            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
//            this.limbSwing += this.limbSwingAmount;
        } else {
//            System.out.println("Move without ridder :" + par1 + "," + par2 + "  =>" + this.rotationYawHead + ":" + this.rotationYaw);

        }
    }

    protected void updateEntityActionState() {
    }

    @Override
    public float getAIMoveSpeed() {
        return getSpeed();
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public void incSpeed(){
        this.speed*=1.2f;
        this.speed=Math.max(0.1f,this.speed);
        this.speed=Math.min(2.0f,this.speed);
        System.out.println("Speed : "+this.speed);
    }
    public void decSpeed(){
        this.speed*=0.8f;
        this.speed=Math.max(0.1f,this.speed);
        this.speed=Math.min(2.0f,this.speed);
        System.out.println("Speed : "+this.speed);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeEntityToNBT(par1NBTTagCompound);
        inventorySupport.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setFloat("RunSpeed", speed);
//        par1NBTTagCompound.setBoolean("EatingHaystack", this.isEatingHaystack());
//        par1NBTTagCompound.setBoolean("Bred", this.func_110205_ce());

    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readEntityFromNBT(par1NBTTagCompound);
        inventorySupport.readFromNBT(par1NBTTagCompound);
        speed=par1NBTTagCompound.getFloat("RunSpeed");
        this.speed=Math.max(0.1f,this.speed);
        this.speed=Math.min(2.0f,this.speed);
//        this.func_110242_l(par1NBTTagCompound.getBoolean("Bred"));
        IAttributeInstance iattributeinstance = this.getAttributeMap().getAttributeInstanceByName("Speed");
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled() {
        return false;
    }

    @Override
    protected void updateAITasks() {
    }

    @Override
    protected void updateAITick() {
    }

    public void setJumpPower(int par1) {
        if (par1 < 0) {
            par1 = 0;
        } else {
            this.field_110294_bI = true;
        }

        if (par1 >= 90) {
            this.jumpPower = 1.0F;
        } else {
            this.jumpPower = 0.4F + 0.4F * (float) par1 / 90.0F;
        }
    }

    /**
     * "Spawns particles for the horse entity. par1 tells whether to spawn hearts. If it is false, it spawns smoke."
     */
    @SideOnly(Side.CLIENT)
    protected void spawnHorseParticles(boolean par1) {
        String s = par1 ? "heart" : "smoke";

        for (int i = 0; i < 7; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(s, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
                    this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0,
                    d1, d2);
        }
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1) {
        if (par1 == 7) {
            this.spawnHorseParticles(true);
        } else if (par1 == 6) {
            this.spawnHorseParticles(false);
        } else {
            super.handleHealthUpdate(par1);
        }
    }

    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
        } else {
            this.riddenByEntity.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    @Override
    protected int decreaseAirSupply(int airSupply) {
        return airSupply;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder() {
        return false;
    }

//    public static class GroupData implements IEntityLivingData {
//        public int                  field_111107_a;
//        public int                  field_111106_b;
//        private static final String __OBFID = "CL_00001643";
//
//        public GroupData(int par1, int par2) {
//            this.field_111107_a = par1;
//            this.field_111106_b = par2;
//        }
//    }

    /*
     * Inventory Support
     */
    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.inventorySupport.getSize();
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slot) {
        return this.inventorySupport.getStackInSlot(slot);
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a new stack.
     */
    public ItemStack decrStackSize(int slot, int size) {
        return this.inventorySupport.decrStackSize(slot, size);
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem - like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = this.inventorySupport.getStackInSlot(slot);
        if (stack != null) {
            this.inventorySupport.setSlotContents(slot, null);
        }
        return stack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        this.inventorySupport.setSlotContents(slot, itemStack);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName() {
        return inventorySupport.getInventoryName();
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName() {
        return this.inventorySupport.hasCustomInventoryName();
    }

    public void setInventoryName(String inventoryName) {
        inventorySupport.setInventoryName(inventoryName);
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        // return /*this.worldObj.getTileEntity((int)this.posX, (int)this.posY,
        // (int)this.posZ) != this ? false :
        // player.getDistanceSq((double)this.posX + 0.5D, (double)this.posY +
        // 0.5D, (double)this.posZ + 0.5D) <= 64.0D;
        return true;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }

    @Override
    public void markDirty() {
    }
}
