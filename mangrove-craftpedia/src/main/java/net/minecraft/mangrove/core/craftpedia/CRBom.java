package net.minecraft.mangrove.core.craftpedia;

import java.util.Collection;
import java.util.Map;

import net.minecraft.item.Item;

public class CRBom {

	private Map<Item, CRBomItem> contents;
	private CRPattern crPattern;
	private boolean mirrored;

	public static CRBom of(Map<Item, CRBomItem> resolveBOM, CRPattern crPattern, boolean mirrored) {
	   CRBom bom=new CRBom();
	    bom.contents=resolveBOM;
	    bom.crPattern = crPattern;
	    bom.mirrored = mirrored;
	    
	    return bom;
    }
	
	public Collection<CRBomItem> getContents() {
	    return contents.values();
    }

	public CRPattern getPattern() {
	    return this.crPattern;
    }
	
	public boolean isMirrored(){
		return mirrored;
	}

}
