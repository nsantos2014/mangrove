package net.minecraft.mangrove.mod.thrive.robot.miner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.robot.block.AbstractBlockNode;
import net.minecraft.mangrove.mod.thrive.robot.entity.AbstractTileRobotNode;
import net.minecraft.mangrove.mod.thrive.robot.entity.Lifecycle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.google.gson.JsonObject;

public class TileRobotMinerNode extends AbstractTileRobotNode {
    public static final Set<Block> drillableBlocks = new HashSet<Block>();

    private final Random field_149933_a = new Random();
    protected BlockPos position;
    protected BlockPos local;
    protected int it = 0;
    protected CS positionCS;
    protected CS localCS;

    private int minerId;
    private int depth = 0;

    public TileRobotMinerNode(World world) {
        super();
        setMaxStep(9);
        this.minerId = world.rand.nextInt();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        depth = tag.getInteger("depth");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("depth", depth);
    }

    @Override
    public JsonObject getTilePacketData() {
        JsonObject tilePacketData = super.getTilePacketData();
        tilePacketData.addProperty("depth", depth);
        return tilePacketData;
    }

    @Override
    protected void handleServerEvent(JsonObject data) {
        if (data.has("depth")) {
            this.depth = data.get("depth").getAsInt();
        }
    }

    @Override
    protected void handleClientEvent(JsonObject data) {
        if (data.has("depth")) {
            this.depth = data.get("depth").getAsInt();
        }
    }

    @Override
    protected boolean doInit(int serverTick) {
        this.localCS = null;
        this.local = null;
        incStep();
        setupCS();
        for (; depth < pos.getY(); depth++) {
//            this.local.y = -depth;
            this.local.add(0,-depth,0);
            final BlockPos worldPos = localCS.toWorldBlockPos(local);
            final IBlockState blockState = this.worldObj.getBlockState(worldPos);
            final Block block =blockState.getBlock() ;
            if (block.isAir(this.worldObj, worldPos)) {
                System.out.println("Block Air(" + worldPos.getX() + "," + worldPos.getY() + "," + worldPos.getZ() + "): " + block);
                // return false;
            } else if (isWater(block, this.worldObj, worldPos)) {
                System.out.println("Block Air(" + worldPos.getX() + "," + worldPos.getY() + "," + worldPos.getZ() + "): " + block);
                // return false;
            } else {
                if (drillableBlocks.contains(block)) {
                    System.out.println("Block Drillable (" + worldPos.getX() + "," + worldPos.getY() + "," + worldPos.getZ() + "): " + block);
                    return true;
                }

                System.out.println("Block Not Drillable (" + worldPos.getX() + "," + worldPos.getY() + "," + worldPos.getZ() + "): " + block);
                return false;
            }
        }
        return false;
    }

    private boolean isWater(Block block, World worldObj, BlockPos blockPos) {
        return block.getMaterial() == Material.water;
    }

    private void setupCS() {
        EnumFacing facing = (EnumFacing) this.worldObj.getBlockState(this.pos).getValue(AbstractBlockNode.FACING);
        if (this.localCS == null) {
//            this.position = new CSPosition3i(xCoord, yCoord, zCoord, BlockUtils.getForgeDirectionFromMetadata(this.getBlockMetadata()));
            this.positionCS = CS.subSystem(this.pos,facing);
            // //////////////////////////////////
            this.position = this.pos.add(getStep(), -this.depth, 0);
            this.localCS = CS.subSystem(this.position,facing);
        }
        // ////////////////////////////////
        if (this.local == null) {
            this.local = new BlockPos(0,0,0);
        }
    }

    @Override
    protected boolean doExecute(int serverTick) {
        this.localCS = null;
        this.local = null;
        setupCS();
        final BlockPos worldPos = localCS.toWorldBlockPos(local);
        IBlockState blockState = this.worldObj.getBlockState(worldPos);
        
        final Block block = blockState.getBlock();
        
        if (drillableBlocks.contains(block)) {
            // final ITransactor transactor = null;
            final ITransactor transactor = MGThriveBlocks.robot_kernel.getTransactor(worldObj, controlPosition);
            final List<ItemStack> drops = block.getDrops(worldObj, worldPos, blockState, 0);

            System.out.println("Block Drilling (" + worldPos.getX() + "," + worldPos.getY() + "," + worldPos.getZ() + "): " + block + " drops:" + drops);
            // worldObj.destroyBlockInWorldPartially(minerId, worldPos.x,
            // worldPos.y, worldPos.z, -1);

            // BlockEvent.BreakEvent breakEvent = new
            // BlockEvent.BreakEvent(worldPos.x, worldPos.y, worldPos.z,
            // worldObj, block, meta,Minecraft.getMinecraft().thePlayer
            // CoreProxy.proxy.getBuildCraftPlayer((WorldServer) world).get());
            // MinecraftForge.EVENT_BUS.post(breakEvent);

            // if (!breakEvent.isCanceled()) {
            // List<ItemStack> stacks =
            // BlockUtils.getItemStackFromBlock((WorldServer) worldObj,
            // worldPos.x, worldPos.y, worldPos.z);
            //
            // if (stacks != null) {
            // spawnCropsByDefault(worldPos,stacks);
            // // for (ItemStack s : stacks) {
            // // if (s != null) {
            // // mineStack(s);
            // // }
            // // }
            // }

//            worldObj.playAuxSFXAtEntity(null, 2001, worldPos.x, worldPos.y, worldPos.z, Block.getIdFromBlock(block) + (meta << 12));

            if (block.hasTileEntity(blockState)) {
                worldObj.removeTileEntity(worldPos);
            }

            worldObj.setBlockToAir(worldPos);

            if (transactor != null) {
                for (ItemStack iStack : drops) {
                    ItemStack stack = transactor.add(iStack, EnumFacing.UP, true);
                    if (stack.stackSize < iStack.stackSize) {
                        iStack.stackSize -= stack.stackSize;
                        spawnDrop(worldPos, iStack);
                    }
                }

                AxisAlignedBB axis = AxisAlignedBB.fromBounds(worldPos.getX() - 2, worldPos.getY() - 2, worldPos.getZ() - 2, worldPos.getX() + 3, worldPos.getY() + 3, worldPos.getZ() + 3);
                List result = worldObj.getEntitiesWithinAABB(EntityItem.class, axis);
                for (int ii = 0; ii < result.size(); ii++) {
                    if (result.get(ii) instanceof EntityItem) {
                        EntityItem entity = (EntityItem) result.get(ii);
                        if (entity.isDead) {
                            continue;
                        }

                        ItemStack iStack = entity.getEntityItem();
                        if (iStack.stackSize <= 0) {
                            continue;
                        }
                        entity.worldObj.removeEntity(entity);
                        ItemStack stack = transactor.add(iStack, EnumFacing.UP, true);
                        if (stack.stackSize < iStack.stackSize) {
                            iStack.stackSize -= stack.stackSize;
                            spawnDrop(worldPos, iStack);
                            break;
                        }
                    }
                }
            } else {
                spawnCropsByDefault(worldPos, drops);
            }

            block.breakBlock(worldObj, worldPos, blockState);
            this.markDirty();
            return true;
        }
        return false;
    }

    // public void mineStack(ItemStack stack) {
    // // First, try to add to a nearby chest
    // stack.stackSize -= Utils.addToRandomInventoryAround(owner.getWorldObj(),
    // owner.xCoord, owner.yCoord, owner.zCoord, stack);
    //
    // // Second, try to add to adjacent pipes
    // if (stack.stackSize > 0) {
    // stack.stackSize -= Utils.addToRandomPipeAround(owner.getWorldObj(),
    // owner.xCoord, owner.yCoord, owner.zCoord, ForgeDirection.UNKNOWN, stack);
    // }
    //
    // // Lastly, throw the object away
    // if (stack.stackSize > 0) {
    // float f = world.rand.nextFloat() * 0.8F + 0.1F;
    // float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
    // float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
    //
    // EntityItem entityitem = new EntityItem(owner.getWorldObj(), owner.xCoord
    // + f, owner.yCoord + f1 + 0.5F, owner.zCoord + f2, stack);
    //
    // entityitem.lifespan = BuildCraftCore.itemLifespan;
    // entityitem.delayBeforeCanPickup = 10;
    //
    // float f3 = 0.05F;
    // entityitem.motionX = (float) world.rand.nextGaussian() * f3;
    // entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 1.0F;
    // entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
    // owner.getWorldObj().spawnEntityInWorld(entityitem);
    // }
    // }

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
        this.depth = 0;
        if (maxStepReached()) {
            setStep(0);
        }
        // System.out.println("doCommit:"+serverTick);
    }

    @Override
    protected void doRollback(int serverTick) {
        // decStep();
        this.localCS = null;
        this.local = null;
        this.depth = 0;
        // setupCS();
        System.out.println("doRollback:" + serverTick);
    }

    @Override
    protected boolean renderScene(int clientTick) {
        this.localCS = null;
        this.local = null;
        setupCS();
//        this.local.y = -depth;
//        final CSPosition3i x=new CSPosition3i();        
        //this.local.y = -depth;
//        final CSPosition3i worldPos = localCS.toWorld(x);
//        ForgeDirection direction = position.direction;
        
        final Vec3 position = new Vec3(this.position.getX(),this.position.getY(),this.position.getZ());
//        System.out.println("renderScene:" + clientTick + ":" + getStep() + ": " + position);
        
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, position.xCoord + 0.25, position.yCoord + 1.5, position.zCoord + 0.25, 0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, position.xCoord + 0.25, position.yCoord + 1.5, position.zCoord + 0.75, 0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, position.xCoord + 0.50, position.yCoord + 1.1, position.zCoord + 0.50, 0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, position.xCoord + 0.75, position.yCoord + 1.5, position.zCoord + 0.25, 0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, position.xCoord + 0.75, position.yCoord + 1.5, position.zCoord + 0.75, 0.5D, 1.0D, 0.5D);


        BlockPos x=new BlockPos(1,0,0);
        
        
//        x.z=position2.direction.offsetX;        
        final BlockPos world2Pos = positionCS.toWorldBlockPos(x);
        
//        world2Pos.x=direction.getOpposite().offsetX;
//        world2Pos.z=direction.getOpposite().offsetZ; 
        
//        final CSPosition3d position2 = new CSPosition3d(world2Pos);
        
//        ForgeDirection rotateAroundYY = position2.direction.getRotation(ForgeDirection.UP);
//        System.out.println("Rotate : "+rotateAroundYY);
        
       
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.25, position2.y + 0.25,position2.z + direction.offsetX*0.25, 1.0D, -0.5D, 0.5D);
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.75, position2.y + 0.25, position2.z + direction.offsetX*0.75, 1.0D, -0.5D, 0.5D);
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.50, position2.y + 0.50, position2.z + direction.offsetX*0.50, 1.0D, -0.5D, 0.5D);
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.25, position2.y + 0.75, position2.z + direction.offsetX*0.25, 1.0D, -0.5D, 0.5D);
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.75, position2.y + 0.75, position2.z + direction.offsetX*0.75, 1.0D, -0.5D, 0.5D);

        //TODO
//        double offsetY = 0.25;
//        double offsetX = 0.1;        
//        double offsetZ = 0.5;
//        
//        double dY = 0.0D;
//        double dX = 0.0D;        
//        double dZ = 0.0D;
//        
//        switch(direction){
//        case NORTH:
//            offsetZ = 0.9;            
//            offsetX = 0.25;
////            dZ = 1.0D;
//            break;
//        case SOUTH:
//            offsetZ = 0.1;
//            offsetX = 0.25;
////            dZ = 1.0D;
//            break;
//        case EAST:
//            offsetZ = 0.25;
//            offsetX = 0.1;
////            dX = 1.0D;
//            break;
//        case WEST:
//            offsetZ = 0.25;
//            offsetX = 0.9;
////            dX = 1.0D;
//            break;
//        default:
//            break;
//        }
//                
//        switch(direction){
//        case NORTH:
//        case SOUTH:
//            for( int i=1; i< 4; i++){
//                for( int j=1; j< 4; j++){
//                    worldObj.spawnParticle("reddust", position2.x + i*offsetX, position2.y + j*offsetY, position2.z + offsetZ, dX, dY, dZ);
//                }
//            }
//            break;
//        case EAST:
//        case WEST:
//            for( int i=1; i< 4; i++){
//                for( int j=1; j< 4; j++){
//                    worldObj.spawnParticle("reddust", position2.x + offsetX, position2.y + j*offsetY, position2.z + i*offsetZ, dX, dY, dZ);
//                }
//            }
//            break;
//        default:
//            break;
//        }
//        
        
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
        System.out.println("renderFailure:" + clientTick);
        return true;
    }

    @Override
    protected int stageDuration(Lifecycle stage2) {
        if (stage2 == Lifecycle.Execute) {
            return 128;
        }
        return 16;
    }

    static {
        drillableBlocks.clear();
        drillableBlocks.add(Blocks.grass);
        drillableBlocks.add(Blocks.farmland);
        drillableBlocks.add(Blocks.obsidian);
        // drillableBlocks.add(Blocks.snow);
        drillableBlocks.add(Blocks.dirt);
        drillableBlocks.add(Blocks.sand);
        drillableBlocks.add(Blocks.gravel);
        drillableBlocks.add(Blocks.sandstone);
        drillableBlocks.add(Blocks.stone);
        drillableBlocks.add(Blocks.clay);
        drillableBlocks.add(Blocks.hardened_clay);
        drillableBlocks.add(Blocks.stained_hardened_clay);

        drillableBlocks.add(Blocks.coal_ore);
        drillableBlocks.add(Blocks.iron_ore);
        drillableBlocks.add(Blocks.gold_ore);
        drillableBlocks.add(Blocks.emerald_ore);
        drillableBlocks.add(Blocks.diamond_ore);
        drillableBlocks.add(Blocks.lapis_ore);
        drillableBlocks.add(Blocks.emerald_ore);
        drillableBlocks.add(Blocks.redstone_ore);
    }

}
