package net.minecraft.mangrove.mod.thrive.robot.farmer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.inventory.filter.ArrayStackFilter;
import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.robot.block.AbstractBlockNode;
import net.minecraft.mangrove.mod.thrive.robot.entity.AbstractTileRobotNode;
import net.minecraft.mangrove.mod.thrive.robot.entity.Lifecycle;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class TileRobotFarmerNode extends AbstractTileRobotNode {
    private final Random field_149933_a = new Random();
    protected BlockPos position;
    protected BlockPos local;
    protected int it = 0;
    protected CS localCS;

    public TileRobotFarmerNode() {
        super();
        setMaxStep(10);
    }

    @Override
    protected boolean doInit(int serverTick) {        
        this.localCS = null;
        this.local = null;
        incStep();
        setupCS();

        for (; this.local.getY() > -4; this.local=this.local.down()) {
            final BlockPos worldPos = localCS.toWorldBlockPos(local);
            final Block block = this.worldObj.getBlockState(worldPos).getBlock();
            if (this.local.getY() > -2 && !block.isAir(this.worldObj, worldPos)) {
                this.local=new BlockPos(this.local.getX(),0,this.local.getZ());
                System.out.println("Block Not Air > -2 ("+worldPos.getX()+","+worldPos.getY()+","+worldPos.getZ()+"): "+block);
                return false;
            } else if (this.local.getY() == -2 && !(block.isAir(this.worldObj, worldPos) || (block instanceof IGrowable))) {
                System.out.println("Block Not Air nor growable =-2("+worldPos.getX()+","+worldPos.getY()+","+worldPos.getZ()+"): "+block);
                this.local=new BlockPos(this.local.getX(),0,this.local.getZ());
                return false;
            } else if (this.local.getY() == -3 && block.isAir(this.worldObj, worldPos)) {
                System.out.println("Block Air ("+worldPos.getX()+","+worldPos.getY()+","+worldPos.getZ()+"): "+block);
                this.local=new BlockPos(this.local.getX(),0,this.local.getZ());
                return false;
            }
        }
        this.local=new BlockPos(this.local.getX(),-3,this.local.getZ());
        return true;
    }

    private void setupCS() {
        EnumFacing facing = (EnumFacing) this.worldObj.getBlockState(this.pos).getValue(AbstractBlockNode.FACING);
        if (this.localCS == null) {            
//            this.position = new CSPosition3i(xCoord, yCoord, zCoord, BlockUtils.getForgeDirectionFromMetadata(this.getBlockMetadata()));
            this.localCS = CS.subSystem(this.pos,facing);
            // //////////////////////////////////
//            this.position = localCS.toWorld(new CSPosition3i(getStep(), 0, 0, position.direction));
            this.position=this.pos.add(getStep(), 0,0);
            this.localCS = CS.subSystem(this.position,facing);
        }
        // ////////////////////////////////
        if (this.local == null) {
            this.local = new BlockPos(0,0,0);
//            this.local.direction = position.direction;
        }
    }

    @Override
    protected boolean doExecute(int serverTick) {
        setupCS();
        final BlockPos worldPos = localCS.toWorldBlockPos(local);
        final Block block = this.worldObj.getBlockState(worldPos).getBlock();

        final Block block1 = Blocks.farmland;
        if (block == Blocks.dirt || block == Blocks.grass) {
//            this.worldObj.playSoundEffect((double) ((float) (worldPos.x) + 0.5F), (double) ((float) (worldPos.y) + 0.5F), (double) ((float) (worldPos.z) + 0.5F),
//                    block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
            
            this.worldObj.setBlockState(worldPos, Blocks.farmland.getDefaultState(), 2);
            this.markDirty();
        } else if (block == Blocks.farmland) {
            // Irrigate
            // TODO retrieve water from control and only irrigate if water is
            // available
            IBlockState blockState = this.worldObj.getBlockState(worldPos);
            this.worldObj.setBlockState(worldPos,blockState.withProperty(BlockFarmland.MOISTURE, 7));
//            this.worldObj.setBlockMetadataWithNotify(worldPos.x, worldPos.y, worldPos.z, 7, 2);
            // TODO check if plating is needed and retrieve seed from control
            if (this.controlPosition != null) {
                final ITransactor transactor = MGThriveBlocks.robot_kernel.getTransactor(worldObj, controlPosition);
                Block blockCrop = this.worldObj.getBlockState(worldPos).getBlock();
                if (blockCrop.isAir(this.worldObj, worldPos)) {
                    final ItemStack iStack = transactor.remove(new ArrayStackFilter(new ItemStack(Items.wheat_seeds,1)), EnumFacing.UP, false);
                    if( iStack!=null && iStack.stackSize>0) {
                        transactor.remove(new ArrayStackFilter(new ItemStack(Items.wheat_seeds,1)), EnumFacing.UP, true);
//                        worldObj.playSoundEffect((double) ((float) worldPos.x + 0.5F), (double) ((float) worldPos.y + 1 + 0.5F), (double) ((float) worldPos.z + 0.5F),
//                                block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);

                        this.worldObj.setBlockState(worldPos, Blocks.wheat.getDefaultState(), 3);
                        blockCrop = this.worldObj.getBlockState(worldPos).getBlock();
                    }
                }

                if (blockCrop instanceof IGrowable) {
                    final ItemStack iStack = transactor.remove(new ArrayStackFilter(new ItemStack(Items.dye,1,15)), EnumFacing.UP, false);
                    if( iStack!=null && iStack.stackSize>0) {
                        transactor.remove(new ArrayStackFilter(new ItemStack(Items.dye,1,15)), EnumFacing.UP, true);
                        ItemDye.applyBonemeal(iStack, worldObj, worldPos, Minecraft.getMinecraft().thePlayer);
                    }                    
                }

                if (blockCrop instanceof IGrowable) {
                    IBlockState state = worldObj.getBlockState(worldPos);
                    int value = (Integer) state.getValue(BlockCrops.AGE);
                    if (value== 7) {

                        final List<ItemStack> drops = blockCrop.getDrops(worldObj, worldPos, state, 0);
                        worldObj.setBlockToAir(worldPos);
                        // final ITransactor transactor =
                        // MGThriveBlocks.farmer_kernel.getTransactor(worldObj,controlPosition);
                        if (transactor != null) {
                            for (ItemStack iStack : drops) {
                                ItemStack stack = transactor.add(iStack, EnumFacing.UP, true);
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
            }else{
                
            }

            this.markDirty();
        }
        return true;
    }

    private void spawnCropsByDefault(final BlockPos worldPos, final List<ItemStack> drops) {
        for (ItemStack iStack : drops) {
            spawnDrop(worldPos, iStack);
        }
    }

    private void spawnDrop(final BlockPos worldPos, ItemStack iStack) {
        if (iStack != null) {
            float f = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
            float f1 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
            float f2 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;

            final EntityItem entityitem = new EntityItem(worldObj, (double) ((float) worldPos.getX() + f), (double) ((float) worldPos.getY() + 1 + f1), (double) ((float) worldPos.getZ() + f2),
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
        System.out.println("doRollback:"+serverTick);
    }

    @Override
    protected boolean renderScene(int clientTick) {
        this.localCS = null;
        this.local=null;
        setupCS();        
                
        final BlockPos worldPos = localCS.toWorldBlockPos(local);
//        final CSPosition3d position = new CSPosition3d(worldPos);
        final Vec3 position=new Vec3(worldPos.getX(), worldPos.getY(),worldPos.getZ());
        System.out.println("renderScene:"+clientTick+":"+getStep()+": "+position);
        
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,position.xCoord+0.25, position.yCoord-1.5, position.zCoord+0.25,  0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,position.xCoord+0.25, position.yCoord-1.5, position.zCoord+0.75,  0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,position.xCoord+0.50, position.yCoord-1.1, position.zCoord+0.50,  0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,position.xCoord+0.75, position.yCoord-1.5, position.zCoord+0.25,  0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,position.xCoord+0.75, position.yCoord-1.5, position.zCoord+0.75,  0.5D, 1.0D, 0.5D);
        
       
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
        System.out.println("renderFailure:"+clientTick);
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
