package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.cs.CSPoint3i;
import net.minecraft.mangrove.core.cs.CSPosition3d;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.core.inventory.ITransactor;
import net.minecraft.mangrove.core.inventory.filter.ArrayStackFilter;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.core.utils.WorldUtils;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotControl;
import net.minecraft.mangrove.mod.thrive.robofarmer.block.SystemUtils;
import net.minecraftforge.common.util.ForgeDirection;

public class TileFarmerNode extends AbstractTileRobotNode {
    private final Random field_149933_a = new Random();
    protected CSPosition3i position;
    protected CSPosition3i local;
    protected int it = 0;
    protected CS localCS;

    public TileFarmerNode() {
        super();
        setMaxStep(10);
    }

    @Override
    protected boolean doInit(int serverTick) {
        this.localCS = null;
        this.local = null;
        incStep();
        setupCS();

        for (; this.local.y > -4; this.local.y--) {
            final CSPosition3i worldPos = localCS.toWorld(local);
            final Block block = this.worldObj.getBlock(worldPos.x, worldPos.y, worldPos.z);
            if (this.local.y > -2 && !block.isAir(this.worldObj, worldPos.x, worldPos.y, worldPos.z)) {
                this.local.y = 0;
                return false;
            } else if (this.local.y == -2 && !(block.isAir(this.worldObj, worldPos.x, worldPos.y, worldPos.z) || (block instanceof IGrowable))) {
                this.local.y = 0;
                return false;
            } else if (this.local.y == -3 && block.isAir(this.worldObj, worldPos.x, worldPos.y, worldPos.z)) {
                this.local.y = 0;
                return false;
            }
        }
        this.local.y = -3;
        return true;
    }

    private void setupCS() {
        if (this.localCS == null) {
            this.position = new CSPosition3i(xCoord, yCoord, zCoord, BlockUtils.getForgeDirectionFromMetadata(this.getBlockMetadata()));
            this.localCS = CS.subSystem(this.position);
            // //////////////////////////////////
            this.position = localCS.toWorld(new CSPosition3i(getStep(), 0, 0, position.direction));
            this.localCS = CS.subSystem(this.position);
        }
        // ////////////////////////////////
        if (this.local == null) {
            this.local = new CSPosition3i();
            this.local.direction = position.direction;
        }
    }

    @Override
    protected boolean doExecute(int serverTick) {
        setupCS();
        final CSPosition3i worldPos = localCS.toWorld(local);
        final Block block = this.worldObj.getBlock(worldPos.x, worldPos.y, worldPos.z);

        final Block block1 = Blocks.farmland;
        if (block == Blocks.dirt || block == Blocks.grass) {
            this.worldObj.playSoundEffect((double) ((float) (worldPos.x) + 0.5F), (double) ((float) (worldPos.y) + 0.5F), (double) ((float) (worldPos.z) + 0.5F),
                    block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
            this.worldObj.setBlock(worldPos.x, worldPos.y, worldPos.z, block1, 7, 2);
            this.markDirty();
        } else if (block == Blocks.farmland) {
            // Irrigate
            // TODO retrieve water from control and only irrigate if water is
            // available
            this.worldObj.setBlockMetadataWithNotify(worldPos.x, worldPos.y, worldPos.z, 7, 2);
            // TODO check if plating is needed and retrieve seed from control
            if (this.controlPosition != null) {
                final ITransactor transactor = MGThriveBlocks.farmer_kernel.getTransactor(worldObj, controlPosition);
                Block blockCrop = this.worldObj.getBlock(worldPos.x, worldPos.y + 1, worldPos.z);
                if (blockCrop.isAir(this.worldObj, worldPos.x, worldPos.y + 1, worldPos.z)) {
                    final ItemStack iStack = transactor.remove(new ArrayStackFilter(new ItemStack(Items.wheat_seeds,1)), ForgeDirection.UP, false);
                    if( iStack!=null && iStack.stackSize>0) {
                        transactor.remove(new ArrayStackFilter(new ItemStack(Items.wheat_seeds,1)), ForgeDirection.UP, true);
                        worldObj.playSoundEffect((double) ((float) worldPos.x + 0.5F), (double) ((float) worldPos.y + 1 + 0.5F), (double) ((float) worldPos.z + 0.5F),
                                block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);

                        this.worldObj.setBlock(worldPos.x, worldPos.y + 1, worldPos.z, Blocks.wheat, 0, 2);
                        blockCrop = this.worldObj.getBlock(worldPos.x, worldPos.y + 1, worldPos.z);
                    }
                }

                if (blockCrop instanceof IGrowable) {
                    final ItemStack iStack = transactor.remove(new ArrayStackFilter(new ItemStack(Items.dye,1,15)), ForgeDirection.UP, false);
                    if( iStack!=null && iStack.stackSize>0) {
                        transactor.remove(new ArrayStackFilter(new ItemStack(Items.dye,1,15)), ForgeDirection.UP, true);
                        ItemDye.applyBonemeal(iStack, worldObj, worldPos.x, worldPos.y + 1, worldPos.z, Minecraft.getMinecraft().thePlayer);
                    }                    
                }

                if (blockCrop instanceof IGrowable) {
                    int meta = worldObj.getBlockMetadata(worldPos.x, worldPos.y + 1, worldPos.z);
                    if (meta == 7) {

                        final ArrayList<ItemStack> drops = blockCrop.getDrops(worldObj, worldPos.x, worldPos.y + 1, worldPos.z, meta, 0);
                        worldObj.setBlockToAir(worldPos.x, worldPos.y + 1, worldPos.z);
                        // final ITransactor transactor =
                        // MGThriveBlocks.farmer_kernel.getTransactor(worldObj,controlPosition);
                        if (transactor != null) {
                            for (ItemStack iStack : drops) {
                                ItemStack stack = transactor.add(iStack, ForgeDirection.UP, true);
                                if (stack.stackSize < iStack.stackSize) {
                                    iStack.stackSize -= stack.stackSize;
                                    spawnDrop(worldPos, iStack);
                                }
                            }
                        } else {
                            spawnCropsByDefault(worldPos, drops);
                        }
                    }
                }
            }

            this.markDirty();
        }
        return true;
    }

    private void spawnCropsByDefault(final CSPosition3i worldPos, final ArrayList<ItemStack> drops) {
        for (ItemStack iStack : drops) {
            spawnDrop(worldPos, iStack);
        }
    }

    private void spawnDrop(final CSPosition3i worldPos, ItemStack iStack) {
        if (iStack != null) {
            float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
            float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
            float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;

            final EntityItem entityitem = new EntityItem(worldObj, (double) ((float) worldPos.x + f), (double) ((float) worldPos.y + 1 + f1), (double) ((float) worldPos.z + f2),
                    iStack);

            float f3 = 0.05F;
            entityitem.motionX = (double) ((float) this.field_149933_a.nextGaussian() * f3);
            entityitem.motionY = (double) ((float) this.field_149933_a.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = (double) ((float) this.field_149933_a.nextGaussian() * f3);
            worldObj.spawnEntityInWorld(entityitem);
        }
    }

    @Override
    protected void doCommit(int serverTick) {
        this.localCS = null;
        this.local = null;
        if (maxStepReached()) {
            setStep(0);
        }
        // System.out.println("doCommit:"+serverTick);
    }

    @Override
    protected void doRollback(int serverTick) {
        decStep();
        this.localCS = null;
        this.local = null;
        // setupCS();
        // System.out.println("doRollback:"+serverTick);
    }

    @Override
    protected boolean renderScene(int clientTick) {
        this.localCS = null;
        this.local=null;
        setupCS();        
        
        final CSPosition3i worldPos = localCS.toWorld(local);
        final CSPosition3d position = new CSPosition3d(worldPos);
        System.out.println("renderScene:"+clientTick+":"+getStep()+": "+position);
        
        worldObj.spawnParticle("happyVillager",position.x+0.25, position.y-1.5, position.z+0.25,  0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle("happyVillager",position.x+0.25, position.y-1.5, position.z+0.75,  0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle("happyVillager",position.x+0.50, position.y-1.1, position.z+0.50,  0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle("happyVillager",position.x+0.75, position.y-1.5, position.z+0.25,  0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle("happyVillager",position.x+0.75, position.y-1.5, position.z+0.75,  0.5D, 1.0D, 0.5D);
        
       
        return true;
    }

    @Override
    protected boolean renderSceneOut(int clientTick) {
        setupCS();
        // System.out.println("renderSceneOut:"+clientTick);
        return true;
    }

    @Override
    protected boolean renderCooldown(int clientTick) {
        setupCS();
        // System.out.println("renderCooldown:"+clientTick);
        return true;
    }

    @Override
    protected boolean renderFailure(int clientTick) {
        setupCS();
        // System.out.println("renderFailure:"+clientTick);
        return true;
    }

    @Override
    protected int stageDuration(Lifecycle stage2) {
        if (stage2 == Lifecycle.Execute) {
            return 128;
        }
        return 16;
    }

}
