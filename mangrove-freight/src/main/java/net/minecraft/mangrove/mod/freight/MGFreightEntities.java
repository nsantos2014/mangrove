package net.minecraft.mangrove.mod.freight;

import net.minecraft.mangrove.mod.freight.boat.EntityMGBoat;
import net.minecraft.mangrove.mod.freight.boat.ItemMGBoat;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MGFreightEntities {

	private static final int ENTITY_MGBOAT_ID = 1;

	public static void preInit() {
		
	}

	public static void init(FMLInitializationEvent event) {
		EntityRegistry.registerModEntity(EntityMGBoat.class, EntityMGBoat.getName(), ENTITY_MGBOAT_ID, MGFreightForge.instance, 80, 3, true);
	}
}
