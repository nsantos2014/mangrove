package net.minecraft.mangrove.mod.thrive.robofarmer.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.mangrove.core.cs.CSPoint3i;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotComponent;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotControl;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class SystemUtils {    
    public static CSPoint3i findFirstControl(World world, int x, int y, int z){
        return findFirstControl(world, x, y, z,new HashSet<CSPoint3i>());
    }
    public static CSPoint3i findFirstControl(World world, int x, int y, int z, Set<CSPoint3i> found){
        if( found==null){
            found=new HashSet<CSPoint3i>();
        }
        Block current = world.getBlock(x, y, z);
        CSPoint3i pThis = new CSPoint3i(x,y,z);
        found.add(pThis);
        
        if( current instanceof IRobotControl){
            return pThis;
        }
        
        for (int i1 = 0; i1 < 6; ++i1){
            CSPoint3i p=new CSPoint3i();
            p.x = x + Facing.offsetsXForSide[i1];
            p.y= y + Facing.offsetsYForSide[i1];
            p.z = z + Facing.offsetsZForSide[i1];
            if( !found.contains(p)){
                Block j1 = world.getBlock(p.x, p.y, p.z);
                if( j1 instanceof IRobotControl){
                    return p;
                }
                if( j1 instanceof IRobotComponent){
                    IRobotComponent component=(IRobotComponent)j1;
                    //found.add(p);
                    
                    CSPoint3i pNew = findFirstControl(world, p.x, p.y, p.z, found);                    
                    if( pNew!=null){
                        return pNew;
                    }
                }
            }
        }
        return null;
    }
    public static Set<CSPoint3i> findAllControl(World world, int x, int y, int z){
        return findAllControl(world, x, y, z,new HashSet<CSPoint3i>());
    }
    public static Set<CSPoint3i> findAllControl(World world, int x, int y, int z, Set<CSPoint3i> found){
        if( found==null){
            found=new HashSet<CSPoint3i>();
        }
        final Set<CSPoint3i> controlList=new HashSet<CSPoint3i>();
        
        Block current = world.getBlock(x, y, z);
        CSPoint3i pThis = new CSPoint3i(x,y,z);
        found.add(pThis);
        
        if( current instanceof IRobotControl && !controlList.contains(pThis)){
          controlList.add(pThis);
        }
        
        
        for (int i1 = 0; i1 < 6; ++i1){
            CSPoint3i p=new CSPoint3i();
            p.x = x + Facing.offsetsXForSide[i1];
            p.y= y + Facing.offsetsYForSide[i1];
            p.z = z + Facing.offsetsZForSide[i1];
            if( !found.contains(p)){
                Block j1 = world.getBlock(p.x, p.y, p.z);
                if( j1 instanceof IRobotControl  && !controlList.contains(p)){
                    controlList.add(p);
                }
                if( j1 instanceof IRobotComponent){
                    IRobotComponent component=(IRobotComponent)j1;
                    //found.add(p);
                    controlList.addAll(findAllControl(world, p.x, p.y, p.z, found));                    
                }
            }
        }
        return controlList;
    }
    
}
