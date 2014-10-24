/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.builders.schematics;

import net.minecraft.nbt.NBTTagCompound;

import buildcraft.api.blueprints.MappingNotFoundException;
import buildcraft.api.blueprints.MappingRegistry;
import buildcraft.api.blueprints.SchematicEntity;
import buildcraft.api.blueprints.SchematicFactory;
import buildcraft.api.blueprints.SchematicRegistry;

public class SchematicFactoryEntity extends SchematicFactory<SchematicEntity> {

	@Override
	protected SchematicEntity loadSchematicFromWorldNBT(NBTTagCompound nbt, MappingRegistry registry)
			throws MappingNotFoundException {
		int entityId = nbt.getInteger("entityId");
		SchematicEntity s = SchematicRegistry.newSchematicEntity(registry.getEntityForId(entityId));

		if (s != null) {
			s.readFromNBT(nbt, registry);
		} else {
			return null;
		}

		return s;
	}

	@Override
	public void saveSchematicToWorldNBT (NBTTagCompound nbt, SchematicEntity object, MappingRegistry registry) {
		super.saveSchematicToWorldNBT(nbt, object, registry);

		nbt.setInteger("entityId", registry.getIdForEntity(object.entity));
		object.writeToNBT(nbt, registry);
	}

}
