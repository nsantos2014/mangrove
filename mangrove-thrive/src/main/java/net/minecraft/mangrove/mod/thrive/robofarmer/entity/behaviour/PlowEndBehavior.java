package net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IBehaviour;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PlowEndBehavior extends AbstractBehaviour {

	public PlowEndBehavior(EntityBlock head) {
	}

	@Override
	public void start() {
		this.tick=0;
		final Block block1 = Blocks.farmland;
		for (double x = 0.1; x < 1; x += .1) {
			for (double z = 0.1; z < 1; z += .1) {
				worldObj.spawnParticle("spell", position.x + x,
						position.y + 1 + 0.1, position.z + z, 0.5D, 1.0D, 0.5D);
			}
		}
	}

	@Override
	public void execute() {
		this.tick++;

	}

	@Override
	public boolean hasStopped() {
		return tick<80;
	}	

}
