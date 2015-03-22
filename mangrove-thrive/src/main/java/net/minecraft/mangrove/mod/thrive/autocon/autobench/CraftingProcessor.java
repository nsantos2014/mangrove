package net.minecraft.mangrove.mod.thrive.autocon.autobench;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.filter.ArrayStackFilter;
import net.minecraft.mangrove.core.inventory.filter.IStackFilter;
import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.mangrove.core.inventory.transactor.Transactor;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.world.World;

public class CraftingProcessor {
	private final Map<Item, Integer> bom = new HashMap<Item, Integer>();

	private final List<BOMItem> bomData = new ArrayList<BOMItem>();
	private final List<OutletDataConfig> outletData = new ArrayList<OutletDataConfig>();

	public void addInlet(ItemStack itemStack, final List<SearchItem> searchItems) {
		if (itemStack == null || itemStack.stackSize != 1) {
			return;
		}
		BOMItem bom = new BOMItem(itemStack);
		bomData.add(bom);

		final IStackFilter stackFilter = new ArrayStackFilter(itemStack);
		for (SearchItem searchItem : searchItems) {
			bom.inletData.add(new BOMInlet(stackFilter, searchItem));
		}
	}

	public void setOutlet(final ItemStack itemStack,
			final List<SearchItem> searchItems) {
		outletData.clear();
		for (SearchItem searchItem : searchItems) {
			outletData.add(new OutletDataConfig(itemStack, searchItem));
		}
	}

	public void startProcessing() {
		bomData.clear();
		outletData.clear();
	}

	public void endProcessing() {
		bomData.clear();
		outletData.clear();
	}

	public boolean canProcess() {
		if (!outletData.isEmpty() && !bomData.isEmpty()) {
			for (BOMItem bom : bomData) {
				if (bom.inletData.isEmpty()) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public void process(World worldObj) {
		for (OutletDataConfig outlet : outletData) {
			ITransactor outletTransactor = Transactor.getTransactorFor(worldObj
					.getTileEntity(outlet.searchItem.getPos()));
			final ItemStack iStack = outletTransactor.add(outlet.itemStack,
					outlet.searchItem.getFacing().getOpposite(), false);
			if (iStack.stackSize == outlet.itemStack.stackSize) {
				if (consume(worldObj)) {
					outletTransactor.add(iStack, outlet.searchItem.getFacing()
							.getOpposite(), true);
					return;
				}
			}
		}
	}

	private boolean consume(World worldObj) {
		List<InletCommitData> commitDataList=new ArrayList<InletCommitData>();
		for (BOMItem bom : bomData) {
			boolean success=false;
			for (BOMInlet inlet : bom.inletData) {
				ITransactor inletTransactor = Transactor
						.getTransactorFor(worldObj
								.getTileEntity(inlet.searchItem.getPos()));

				final ItemStack iStack = inletTransactor.remove(
						inlet.stackFilter, inlet.searchItem.getFacing()
								.getOpposite(), false);
				if (iStack != null && iStack.stackSize > 0) {
					success=true;
					commitDataList.add(new InletCommitData(inlet, inletTransactor));
					break;
				}
			}
			if( !success){
				return false;
			}
		}
		for (InletCommitData commitData:commitDataList) {
			commitData.commit();
		}
		return true;
	}

	class BOMItem {
		public BOMItem(ItemStack itemStack2) {
			// TODO Auto-generated constructor stub
		}

		ItemStack itemStack;
		final List<BOMInlet> inletData = new ArrayList<BOMInlet>();

	}

	class BOMInlet {
		private final SearchItem searchItem;
		private final IStackFilter stackFilter;

		public BOMInlet(IStackFilter stackFilter, SearchItem searchItem) {
			this.searchItem = searchItem;
			this.stackFilter = stackFilter;
		}

	}

	class OutletDataConfig {
		private final ItemStack itemStack;
		private final SearchItem searchItem;

		public OutletDataConfig(ItemStack itemStack, SearchItem searchItem) {
			this.itemStack = itemStack;
			this.searchItem = searchItem;
		}
	}
	
	class InletCommitData{
		BOMInlet inlet;
		ITransactor transactor;
		public InletCommitData(BOMInlet inlet, ITransactor transactor) {
			this.inlet = inlet;
			this.transactor = transactor;
		}
		public void commit() {
			transactor.remove(
					inlet.stackFilter, inlet.searchItem.getFacing()
							.getOpposite(), true);
			
		}
		
	}

	// class CT {
	//
	// private final ItemStack itemStack;
	// private final SearchItem searchItem;
	// private final IStackFilter stackFilter;
	//
	// public CT(ItemStack itemStack, IStackFilter stackFilter, SearchItem
	// searchItem) {
	// this.itemStack = itemStack;
	// this.stackFilter = stackFilter;
	// this.searchItem = searchItem;
	// }
	//
	// }
}
