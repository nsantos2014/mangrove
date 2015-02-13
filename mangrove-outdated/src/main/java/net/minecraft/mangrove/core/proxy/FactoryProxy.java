/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package net.minecraft.mangrove.core.proxy;

import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.SidedProxy;

public class FactoryProxy {
	@SidedProxy(clientSide = "net.minecraft.mangrove.core.proxy.FactoryProxyClient", serverSide = "net.minecraft.mangrove.core.proxy.FactoryProxy")
	public static FactoryProxy proxy;

	public void initializeTileEntities() {
	}

	public void initializeEntityRenders() {
	}

	public void initializeNEIIntegration() {
	}

	public EntityBlock newPumpTube(World w) {
        return new EntityBlock(w);
    }

	public EntityBlock newDrill(World w, double i, double j, double k,
			double l, double d, double e) {
        return new EntityBlock(w, i, j, k, l, d, e);
    }

	public EntityBlock newDrillHead(World w, double i, double j, double k,
			double l, double d, double e) {
        return new EntityBlock(w, i, j, k, l, d, e);
    }
}
