package net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour;

import net.minecraft.mangrove.core.CoreConstants;
import net.minecraft.mangrove.core.cs.CSPosition3d;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IBehaviour;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MoveEndBehavior extends AbstractBehaviour{
	
	private double boomStep=0.05;
	//private double boomLength = 0.00;
	private double boomAim = 0;
	
	
	//private double jibLength = 0.00;
	private int jibAim = 3;
	private double jibStep=0.05;

	private EntityBlock boom;
	private EntityBlock jib;
	private CSPosition3d local;
	

	
	public MoveEndBehavior(EntityBlock boom,EntityBlock jib) {
		this.boom = boom;
		this.jib = jib;
		this.local=new CSPosition3d();
	}

	@Override
	public void start() {
		boomAim=boom.kSize+1;
		
		this.local.x=CoreConstants.PIPE_MAX_POS;
		this.local.z=CoreConstants.PIPE_MIN_POS;
		this.local.y=CoreConstants.PIPE_MIN_POS;
	}

	@Override
	public void execute() {		
		boom.kSize += boomStep;
		this.local.x+=jibStep;
		final CSPosition3d worldPos = this.localCS.toWorld(this.local);
		jib.rotationYaw=90;
		jib.setPosition(worldPos.x, worldPos.y, worldPos.z);
	}

	@Override
	public boolean hasStopped() {
		return boom.kSize>=boomAim;
	}
	
}
