package net.minecraft.mangrove.mod.thrive.robot.miner;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.cs.CSPosition3d;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.core.inventory.ITransactor;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.robot.entity.AbstractTileRobotNode;
import net.minecraft.mangrove.mod.thrive.robot.entity.Lifecycle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.gson.JsonObject;

public class TileRobotMinerNode extends AbstractTileRobotNode {
    public static final Set<Block> drillableBlocks = new HashSet<Block>();

    private final Random field_149933_a = new Random();
    protected CSPosition3i position;
    protected CSPosition3i local;
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
        for (; depth < yCoord; depth++) {
            this.local.y = -depth;
            final CSPosition3i worldPos = localCS.toWorld(local);
            final Block block = this.worldObj.getBlock(worldPos.x, worldPos.y, worldPos.z);
            if (block.isAir(this.worldObj, worldPos.x, worldPos.y, worldPos.z)) {
                System.out.println("Block Air(" + worldPos.x + "," + worldPos.y + "," + worldPos.z + "): " + block);
                // return false;
            } else if (isWater(block, this.worldObj, worldPos.x, worldPos.y, worldPos.z)) {
                System.out.println("Block Air(" + worldPos.x + "," + worldPos.y + "," + worldPos.z + "): " + block);
                // return false;
            } else {
                if (drillableBlocks.contains(block)) {
                    System.out.println("Block Drillable (" + worldPos.x + "," + worldPos.y + "," + worldPos.z + "): " + block);
                    return true;
                }

                System.out.println("Block Not Drillable (" + worldPos.x + "," + worldPos.y + "," + worldPos.z + "): " + block);
                return false;
            }
        }
        return false;
    }

    private boolean isWater(Block block, World worldObj, int x, int y, int z) {
        return block.getMaterial() == Material.water;
    }

    private void setupCS() {
        if (this.localCS == null) {
            this.position = new CSPosition3i(xCoord, yCoord, zCoord, BlockUtils.getForgeDirectionFromMetadata(this.getBlockMetadata()));
            this.positionCS = CS.subSystem(this.position);
            // //////////////////////////////////
            this.position = positionCS.toWorld(new CSPosition3i(getStep(), -this.depth, 0, position.direction));
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
        this.localCS = null;
        this.local = null;
        setupCS();
        final CSPosition3i worldPos = localCS.toWorld(local);
        final Block block = this.worldObj.getBlock(worldPos.x, worldPos.y, worldPos.z);
        int meta = worldObj.getBlockMetadata(worldPos.x, worldPos.y, worldPos.z);

        if (drillableBlocks.contains(block)) {
            // final ITransactor transactor = null;
            final ITransactor transactor = MGThriveBlocks.robot_kernel.getTransactor(worldObj, controlPosition);
            final ArrayList<ItemStack> drops = block.getDrops(worldObj, worldPos.x, worldPos.y + 1, worldPos.z, meta, 0);

            System.out.println("Block Drilling (" + worldPos.x + "," + worldPos.y + "," + worldPos.z + "): " + block + " drops:" + drops);
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

            worldObj.playAuxSFXAtEntity(null, 2001, worldPos.x, worldPos.y, worldPos.z, Block.getIdFromBlock(block) + (meta << 12));

            if (block.hasTileEntity(meta)) {
                worldObj.removeTileEntity(worldPos.x, worldPos.y, worldPos.z);
            }

            worldObj.setBlockToAir(worldPos.x, worldPos.y, worldPos.z);

            if (transactor != null) {
                for (ItemStack iStack : drops) {
                    ItemStack stack = transactor.add(iStack, ForgeDirection.UP, true);
                    if (stack.stackSize < iStack.stackSize) {
                        iStack.stackSize -= stack.stackSize;
                        spawnDrop(worldPos, iStack);
                    }
                }

                AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(worldPos.x - 2, worldPos.y - 2, worldPos.z - 2, worldPos.x + 3, worldPos.y + 3, worldPos.z + 3);
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
                        ItemStack stack = transactor.add(iStack, ForgeDirection.UP, true);
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

            block.breakBlock(worldObj, worldPos.x, worldPos.y, worldPos.z, block, 0);
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

    private void spawnCropsByDefault(final CSPosition3i worldPos, final List<ItemStack> drops) {
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
        final CSPosition3i x=new CSPosition3i();        
        //this.local.y = -depth;
        final CSPosition3i worldPos = localCS.toWorld(x);
        ForgeDirection direction = position.direction;
        
        final CSPosition3d position = new CSPosition3d(worldPos);
        System.out.println("renderScene:" + clientTick + ":" + getStep() + ": " + position);
        
        worldObj.spawnParticle("happyVillager", position.x + 0.25, position.y + 1.5, position.z + 0.25, 0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle("happyVillager", position.x + 0.25, position.y + 1.5, position.z + 0.75, 0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle("happyVillager", position.x + 0.50, position.y + 1.1, position.z + 0.50, 0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle("happyVillager", position.x + 0.75, position.y + 1.5, position.z + 0.25, 0.5D, 1.0D, 0.5D);
        worldObj.spawnParticle("happyVillager", position.x + 0.75, position.y + 1.5, position.z + 0.75, 0.5D, 1.0D, 0.5D);


        x.x=1;
        
//        x.z=position2.direction.offsetX;        
        final CSPosition3i world2Pos = positionCS.toWorld(x);
        
//        world2Pos.x=direction.getOpposite().offsetX;
//        world2Pos.z=direction.getOpposite().offsetZ; 
        
        final CSPosition3d position2 = new CSPosition3d(world2Pos);
        
//        ForgeDirection rotateAroundYY = position2.direction.getRotation(ForgeDirection.UP);
//        System.out.println("Rotate : "+rotateAroundYY);
        
       
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.25, position2.y + 0.25,position2.z + direction.offsetX*0.25, 1.0D, -0.5D, 0.5D);
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.75, position2.y + 0.25, position2.z + direction.offsetX*0.75, 1.0D, -0.5D, 0.5D);
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.50, position2.y + 0.50, position2.z + direction.offsetX*0.50, 1.0D, -0.5D, 0.5D);
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.25, position2.y + 0.75, position2.z + direction.offsetX*0.25, 1.0D, -0.5D, 0.5D);
//        worldObj.spawnParticle("reddust", position2.x + direction.offsetZ*0.75, position2.y + 0.75, position2.z + direction.offsetX*0.75, 1.0D, -0.5D, 0.5D);

        
        double offsetY = 0.25;
        double offsetX = 0.1;        
        double offsetZ = 0.5;
        
        double dY = 0.0D;
        double dX = 0.0D;        
        double dZ = 0.0D;
        
        switch(direction){
        case NORTH:
            offsetZ = 0.9;            
            offsetX = 0.25;
//            dZ = 1.0D;
            break;
        case SOUTH:
            offsetZ = 0.1;
            offsetX = 0.25;
//            dZ = 1.0D;
            break;
        case EAST:
            offsetZ = 0.25;
            offsetX = 0.1;
//            dX = 1.0D;
            break;
        case WEST:
            offsetZ = 0.25;
            offsetX = 0.9;
//            dX = 1.0D;
            break;
        default:
            break;
        }
                
        switch(direction){
        case NORTH:
        case SOUTH:
            for( int i=1; i< 4; i++){
                for( int j=1; j< 4; j++){
                    worldObj.spawnParticle("reddust", position2.x + i*offsetX, position2.y + j*offsetY, position2.z + offsetZ, dX, dY, dZ);
                }
            }
            break;
        case EAST:
        case WEST:
            for( int i=1; i< 4; i++){
                for( int j=1; j< 4; j++){
                    worldObj.spawnParticle("reddust", position2.x + offsetX, position2.y + j*offsetY, position2.z + i*offsetZ, dX, dY, dZ);
                }
            }
            break;
        default:
            break;
        }
        
        
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
