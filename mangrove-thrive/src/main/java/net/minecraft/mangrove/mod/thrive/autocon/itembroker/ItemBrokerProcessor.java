package net.minecraft.mangrove.mod.thrive.autocon.itembroker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.filter.ArrayStackFilter;
import net.minecraft.mangrove.core.inventory.filter.IStackFilter;
import net.minecraft.mangrove.core.inventory.filter.StackFilter;
import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.mangrove.core.inventory.transactor.Transactor;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.world.World;

public class ItemBrokerProcessor {
	private final List<CT> inletData=new ArrayList<ItemBrokerProcessor.CT>();
	private final List<CT> outletData=new ArrayList<ItemBrokerProcessor.CT>();
	
	public void addInlet(final ConnectionConfig config,final List<SearchItem> searchItems){
		final IStackFilter stackFilter = new ArrayStackFilter(config.items);
		for( SearchItem searchItem:searchItems){
			inletData.add(new CT(config,stackFilter,searchItem));
		}
	}
	public void addOutlet(final ConnectionConfig config,final List<SearchItem> searchItems){
		final IStackFilter stackFilter = new ArrayStackFilter(config.items);
		for( SearchItem searchItem:searchItems){
			outletData.add(new CT(config,stackFilter,searchItem));
		}
	}
	public void startProcessing() {
		inletData.clear();
		outletData.clear();
	}

	public void endProcessing() {
		inletData.clear();
		outletData.clear();
	}
	
	class CT {

		private final ConnectionConfig config;
		private final SearchItem searchItem;
		private final IStackFilter stackFilter;

		public CT(ConnectionConfig config, IStackFilter stackFilter, SearchItem searchItem) {
			this.config = config;
			this.stackFilter = stackFilter;
			this.searchItem = searchItem;
		}
		
	}

	public boolean canProcess() {
		return !(inletData.isEmpty() || outletData.isEmpty());
	}
	public void process(World worldObj) {
		for(CT inlet: inletData){
			ITransactor inletTransactor = Transactor.getTransactorFor(worldObj.getTileEntity(inlet.searchItem.getPos()));
			final ItemStack iStack = inletTransactor.remove(inlet.stackFilter, inlet.searchItem.getFacing().getOpposite(), false);
			if( iStack!=null && iStack.stackSize>0) {
				if( transfer(worldObj,iStack)){
					inletTransactor.remove(inlet.stackFilter, inlet.searchItem.getFacing().getOpposite(), true);
				}
			}
		}
	}
	private boolean transfer(final World worldObj, final ItemStack iStack) {
		for(CT outlet:outletData){
			ITransactor outletTransactor = Transactor.getTransactorFor(worldObj.getTileEntity(outlet.searchItem.getPos()));
			if(outlet.stackFilter.matches(iStack)){
				final ItemStack iStack2 = outletTransactor.add(iStack, outlet.searchItem.getFacing().getOpposite(), false);
				if (iStack2.stackSize == iStack.stackSize) {
					outletTransactor.add(iStack, outlet.searchItem.getFacing().getOpposite(), true);
					return true;
				}
			}
		}
		return false;
	}
	public void process_2(World worldObj) {
		Iterator<CT> itInlet = inletData.iterator();
		Iterator<CT> itOutlet = outletData.iterator();
		
		CT inletSearch = itInlet.next();
		CT outletSearch = itOutlet.next();
		
		while(true){
			
			ITransactor inletTransactor = Transactor.getTransactorFor(worldObj.getTileEntity(inletSearch.searchItem.getPos()));
			
			final ItemStack iStack = inletTransactor.remove(inletSearch.stackFilter, inletSearch.searchItem.getFacing().getOpposite(), false);
			if( iStack!=null && iStack.stackSize>0) {
				do{
					ITransactor outletTransactor = Transactor.getTransactorFor(worldObj.getTileEntity(outletSearch.searchItem.getPos()));
					if(outletSearch.stackFilter.matches(iStack)){
						final ItemStack iStack2 = outletTransactor.add(iStack, outletSearch.searchItem.getFacing().getOpposite(), true);
						if (iStack2.stackSize < iStack.stackSize) {
							iStack.stackSize-=iStack2.stackSize;
							if( itOutlet.hasNext()){
								outletSearch=itOutlet.next();
							}else{
								return;
							}
						}else{
							iStack.stackSize=0;
						}
					}else{
						if( itOutlet.hasNext()){
							outletSearch=itOutlet.next();
						}else{
							return;
						}
					}
				}while(iStack.stackSize>0);
				
				inletTransactor.remove(inletSearch.stackFilter, inletSearch.searchItem.getFacing().getOpposite(), true);
				return;
			}else{
				if( itInlet.hasNext()){
					inletSearch=itInlet.next();
				}else{
					return;
				}
			}
			
		}
	}


}
