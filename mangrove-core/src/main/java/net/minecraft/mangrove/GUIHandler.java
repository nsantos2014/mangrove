package net.minecraft.mangrove;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.mangrove.core.gui.MGContainer;
import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {
	private Map<Integer, Class<? extends MGContainer>> containerByID=new HashMap<>();
	private Map<Class<?>, Class<? extends MGContainer>> containerByType=new HashMap<>();
	
	private Map<Integer, Class<? extends MGGui>> guiByID=new HashMap<>();
	private Map<Class<?>, Class<? extends MGGui>> guiByType=new HashMap<>();
	
	private Map<Integer, Class<? extends Entity>> entityByID=new HashMap<>();
	
	
	public void registerID(int id,Class<? extends MGContainer> containerClass,Class<? extends MGGui> guiClass){
		this.containerByID.put(id,containerClass);
		this.guiByID.put(id,guiClass);
	}
	public void registerID(int id, Class<? extends MGContainer> containerClass,Class<? extends MGGui> guiClass, Class<? extends Entity> entityClass) {
        registerID(id, containerClass, guiClass);
        entityByID.put(id, entityClass);        
    }
	public void registerClass(Class<?> clazz,Class<? extends MGContainer> containerClass,Class<? extends MGGui> guiClass){
		this.containerByType.put(clazz,containerClass);
		this.guiByType.put(clazz,guiClass);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) {
		if (!world.blockExists(x, y, z)){
			System.out.println("No Block at:("+x+","+y+","+z+")");
			return null;
		}
		final TileEntity tile = world.getTileEntity(x, y, z);// getBlockTileEntity
		if(tile!=null){
			if(containerByID.containsKey(ID)){
				return createContainer(player, tile, containerByID.get(ID));
			}
			for(final Entry<Class<?>,Class<? extends MGContainer>> entry:containerByType.entrySet()){
				if( entry.getKey().isAssignableFrom(tile.getClass())){
					return createContainer(player, tile, entry.getValue());
				}
			}
		}else{
		    Class<? extends Entity> clazz = entityByID.get(ID);
		    if( clazz!=null) {
    		    final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(x-0.5D, y-0.5D, z-0.5D, x+0.5D, y+0.5d, z+0.5d);
    		    final List list = world.getEntitiesWithinAABB(clazz, boundingBox);
    		    if(list.size()==1){
                    return createContainer(player,list.get(0),containerByID.get(ID));
                }
		    }
		}
		return null;
	}
	

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) {
		if (!world.blockExists(x, y, z)){
			System.out.println("No Block at:("+x+","+y+","+z+")");
			return null;
		}
		final TileEntity tile = world.getTileEntity(x, y, z);// getBlockTileEntity
		if(tile!=null){
			if(guiByID.containsKey(ID)){
				return createGui(player, tile, guiByID.get(ID));
			}
			for(final Entry<Class<?>,Class<? extends MGGui>> entry:guiByType.entrySet()){
				if( entry.getKey().isAssignableFrom(tile.getClass())){
					return createGui(player, tile, entry.getValue());
				}
			}
		}else{
		    Class<? extends Entity> clazz = entityByID.get(ID);
		    if( clazz!=null) {
    		    final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(x-0.5D, y-0.5D, z-0.5D, x+0.5D, y+0.5d, z+0.5d);		    
    		    final List list = world.getEntitiesWithinAABB(clazz, boundingBox);
    		    if(list.size()==1){
    		        return createGui(player,list.get(0),guiByID.get(ID));
                }
		    }
		}
		return null;
	}

	private MGContainer createContainer(EntityPlayer player, final Object tile,	final Class<? extends MGContainer> clazz) {
		try {
			for(Constructor<?> c:clazz.getConstructors()){
				if(canConstruct(c,tile.getClass(), InventoryPlayer.class)){
					return (MGContainer) c.newInstance(tile,player.inventory);
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private MGGui createGui(EntityPlayer player, final Object tile,	final Class<? extends MGGui> clazz) {
		try {
			
			for(Constructor<?> c:clazz.getConstructors()){
				if(canConstruct(c, InventoryPlayer.class,tile.getClass())){
					return (MGGui) c.newInstance(player.inventory,tile);
				}
			}
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean canConstruct(Constructor<?> c,Class<?> ...args){
        Class<?>[] paramTypes = c.getParameterTypes(); 
        if(args.length != paramTypes.length){
            return false;
        }

        int i = 0;
        for(Class<?> arg: args){
            if(!paramTypes[i].isAssignableFrom(arg)){
                return false;
            }
            i++;
        }

        return true;
    }
    
}
