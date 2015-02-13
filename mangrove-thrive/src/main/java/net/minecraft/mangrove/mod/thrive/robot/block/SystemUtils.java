package net.minecraft.mangrove.mod.thrive.robot.block;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.mangrove.mod.thrive.robot.IRobotComponent;
import net.minecraft.mangrove.mod.thrive.robot.IRobotConnection;
import net.minecraft.mangrove.mod.thrive.robot.IRobotControl;
import net.minecraft.mangrove.mod.thrive.robot.IRobotNode;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SystemUtils {    
    public static BlockPos findFirstControl(World world, BlockPos blockPos){
        return findFirstControl(world, blockPos,new HashSet<BlockPos>());
    }
    public static BlockPos findFirstControl(World world, BlockPos blockPos, Set<BlockPos> found){
        if( found==null){
            found=new HashSet<BlockPos>();
        }
        Block current = world.getBlockState(blockPos).getBlock();

        found.add(blockPos);
        
        if( current instanceof IRobotControl){
            return blockPos;
        }
        for( EnumFacing dir:EnumFacing.values()){
            BlockPos p=blockPos.add(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ());
            if( !found.contains(p)){
                Block candidate = world.getBlockState(p).getBlock();
                if( candidate instanceof IRobotControl){
                    return p;
                }
                if( candidate instanceof IRobotComponent){
                    IRobotComponent component=(IRobotComponent)candidate;
                    BlockPos pNew = findFirstControl(world, p, found);
                    if( pNew!=null){
                        return pNew;
                    }
                }
            }
        }
//        for (int i1 = 0; i1 < 6; ++i1){
//            
//            blockPos.add(EnumFacing.offsetsXForSide[i1],)
//            CSPoint3i p=new CSPoint3i();
//            
//            p.x = x + Facing.offsetsXForSide[i1];
//            p.y= y + Facing.offsetsYForSide[i1];
//            p.z = z + Facing.offsetsZForSide[i1];
//            if( !found.contains(p)){
//                Block j1 = world.getBlock(p.x, p.y, p.z);
//                if( j1 instanceof IRobotControl){
//                    return p;
//                }
//                if( j1 instanceof IRobotComponent){
//                    IRobotComponent component=(IRobotComponent)j1;
//                    //found.add(p);
//                    
//                    CSPoint3i pNew = findFirstControl(world, p.x, p.y, p.z, found);                    
//                    if( pNew!=null){
//                        return pNew;
//                    }
//                }
//            }
//        }
        return null;
    }
    public static Set<BlockPos> findAllControl(IBlockAccess world, BlockPos blockPos){
        return findAllControl(world, blockPos,new HashSet<BlockPos>());
    }
    protected static Set<BlockPos> findAllControl(IBlockAccess world, BlockPos blockPos, Set<BlockPos> found){
        if( found==null){
            found=new HashSet<BlockPos>();
        }
        final Set<BlockPos> controlList=new HashSet<BlockPos>();
        
        Block current = world.getBlockState(blockPos).getBlock();
        found.add(blockPos);
        
        if( current instanceof IRobotControl && !controlList.contains(blockPos)){
          controlList.add(blockPos);
        }
        for( EnumFacing dir:EnumFacing.values()){
            BlockPos p=blockPos.add(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ());
            if( !found.contains(p)){
                Block j1 = world.getBlockState(p).getBlock();
                if( j1 instanceof IRobotControl  && !controlList.contains(p)){
                    controlList.add(p);
                }
                if( j1 instanceof IRobotConnection){
                    IRobotComponent component=(IRobotComponent)j1;
                    //found.add(p);
                    controlList.addAll(findAllControl(world, p, found));                    
                }
            }
        }
        
//        for (int i1 = 0; i1 < 6; ++i1){
//            CSPoint3i p=new CSPoint3i();
//            p.x = x + Facing.offsetsXForSide[i1];
//            p.y= y + Facing.offsetsYForSide[i1];
//            p.z = z + Facing.offsetsZForSide[i1];
//            if( !found.contains(p)){
//                Block j1 = world.getBlock(p.x, p.y, p.z);
//                if( j1 instanceof IRobotControl  && !controlList.contains(p)){
//                    controlList.add(p);
//                }
//                if( j1 instanceof IRobotConnection){
//                    IRobotComponent component=(IRobotComponent)j1;
//                    //found.add(p);
//                    controlList.addAll(findAllControl(world, p.x, p.y, p.z, found));                    
//                }
//            }
//        }
        return controlList;
    }
    public static void updateNetwork(World world, BlockPos blockPos){
        updateNetwork(world, blockPos,new HashSet<BlockPos>());
    }
    public static void updateNetwork(World world, BlockPos blockPos, Set<BlockPos> found){
        if( found==null){
            return;
        }       
        
        Block current = world.getBlockState(blockPos).getBlock();        
        
//        if( current instanceof IRobotControl && !found.contains(pThis)){
            found.add(blockPos);
//        }        
        for( EnumFacing dir:EnumFacing.values()){
            BlockPos p=blockPos.add(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ());
            if( !found.contains(p)){
                Block j1 = world.getBlockState(p).getBlock();
                if( j1 instanceof IRobotComponent){
                    IRobotComponent component=(IRobotComponent)j1;
                    //found.add(p);
//                        int meta = world.getBlockMetadata(p);
                    //world.setBlockMetadataWithNotify(p.x, p.y, p.z, meta, 2);
                    component.updateNetwork(world, p);
                    updateNetwork(world, p, found);                    
                } 
            }
        }
//        for (int i1 = 0; i1 < 6; ++i1){
//            CSPoint3i p=new CSPoint3i();
//            p.x = x + Facing.offsetsXForSide[i1];
//            p.y= y + Facing.offsetsYForSide[i1];
//            p.z = z + Facing.offsetsZForSide[i1];
//            if( !found.contains(p)){
//                Block j1 = world.getBlock(p.x, p.y, p.z);
//                if( j1 instanceof IRobotComponent){
//                    IRobotComponent component=(IRobotComponent)j1;
//                    //found.add(p);
//                    int meta = world.getBlockMetadata(p.x, p.y, p.z);
//                    //world.setBlockMetadataWithNotify(p.x, p.y, p.z, meta, 2);
//                    component.updateNetwork(world, p.x, p.y, p.z);
//                    updateNetwork(world, p.x, p.y, p.z, found);                    
//                }
//            }
//        }
    }
    public static <T extends IRobotNode>boolean checkAllNodes(Class<T> nodeClass, World world, BlockPos blockPos) {
        final Set<BlockPos> nodeList=findAllNodes(world, blockPos, new HashSet<BlockPos>());
        for(BlockPos point:nodeList){
            Block current = world.getBlockState(point).getBlock();
            if( !nodeClass.equals(current.getClass()) ){
                return false;
            }
        }
        return true;
    }
    
    protected static Set<BlockPos> findAllNodes(IBlockAccess world, BlockPos blockPos, Set<BlockPos> found){
        if( found==null){
            found=new HashSet<BlockPos>();
        }
        final Set<BlockPos> nodeList=new HashSet<BlockPos>();
        
        Block current = world.getBlockState(blockPos).getBlock();
        
        found.add(blockPos);        
        if( current instanceof IRobotNode && !nodeList.contains(blockPos)){
          nodeList.add(blockPos);
        }
        
        for( EnumFacing dir:EnumFacing.values()){
            BlockPos p=blockPos.add(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ());
            if( !found.contains(p)){
                Block j1 = world.getBlockState(p).getBlock();
                if( j1 instanceof IRobotNode && !nodeList.contains(p)){
                    nodeList.add(p);
                }
                if( j1 instanceof IRobotConnection){
                    IRobotComponent component=(IRobotComponent)j1;
                    //found.add(p);
                    nodeList.addAll(findAllNodes(world, p, found));                    
                }
            }
        }
//        for (int i1 = 0; i1 < 6; ++i1){
//            CSPoint3i p=new CSPoint3i();
//            p.x = x + Facing.offsetsXForSide[i1];
//            p.y= y + Facing.offsetsYForSide[i1];
//            p.z = z + Facing.offsetsZForSide[i1];
//            if( !found.contains(p)){
//                Block j1 = world.getBlock(p.x, p.y, p.z);
//                if( j1 instanceof IRobotNode && !nodeList.contains(p)){
//                    nodeList.add(p);
//                }
//                if( j1 instanceof IRobotConnection){
//                    IRobotComponent component=(IRobotComponent)j1;
//                    //found.add(p);
//                    nodeList.addAll(findAllNodes(world, p.x, p.y, p.z, found));                    
//                }
//            }
//        }
        return nodeList;
    }
    
    
}
