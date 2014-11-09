package net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour;

import net.minecraft.mangrove.core.CoordinatesTransform;
import net.minecraft.mangrove.core.CoreConstants;
import net.minecraft.mangrove.core.Position;
import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IBehaviour;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class SetupBehavior extends AbstractBehaviour{
	
	private double boomStep=0.05;
	//private double boomLength = 0.00;
	private double boomAim = CoreConstants.PIPE_MAX_POS;
	
	
	//private double jibLength = 0.00;
	private int jibAim = 3;
	private double jibStep=0.05;

	private EntityBlock boom;
	private EntityBlock jib;	
	
	private final Position boomLocal=new Position();
	private final Position jibLocal=new Position();
	
	public SetupBehavior(EntityBlock boom,EntityBlock jib) {		
		this.boom = boom;
		this.jib = jib;		
	}
	
	@Override
	public void start() {
		boom.kSize=0;
		boom.iSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
		boom.jSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
		
		boomLocal.orientation=position.orientation;
		boomLocal.x=0.01d;
		boomLocal.y=CoreConstants.PIPE_MIN_POS;
		boomLocal.z=CoreConstants.PIPE_MIN_POS;
		
		boom.rotationX=CoordinatesTransform.i.getXRotationAngle(boomLocal.orientation);
		boom.rotationY=CoordinatesTransform.i.getYRotationAngle(boomLocal.orientation);
		boom.rotationZ=CoordinatesTransform.i.getZRotationAngle(boomLocal.orientation);
		
		final Position tBoom = CoordinatesTransform.i.translate(position, boomLocal);
		boom.setPosition(tBoom.x,tBoom.y,tBoom.z);				
		
		
		jib.jSize=0;
		jib.iSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
		jib.kSize = CoreConstants.PIPE_MAX_POS - CoreConstants.PIPE_MIN_POS;
		
		
//		jib.rotationZ=CoordinatesTransform.i.getXRotationAngle(position.orientation);
		
//		jib.rotationY=CoordinatesTransform.i.getYRotationAngle(position.orientation);
//		jib.rotationZ=CoordinatesTransform.i.getZRotationAngle(position.orientation);
		
		jibLocal.orientation=ForgeDirection.DOWN;
		jibLocal.x=CoreConstants.PIPE_MAX_POS;
		jibLocal.y=CoreConstants.PIPE_MIN_POS;
		jibLocal.z=CoreConstants.PIPE_MIN_POS;
		jib.rotationX=0;
		jib.rotationY=CoordinatesTransform.i.getYRotationAngle(boomLocal.orientation);
		jib.rotationZ=0;
		switch(position.orientation){
		case NORTH:
		case SOUTH:
			jib.rotationX=180;
			break;
		case EAST:
		case WEST:
			jib.rotationX=180;
			jib.rotationY+=180;
			break;	
		default:
			break;
		}
		
		final Position tJib = CoordinatesTransform.i.translate(position, jibLocal);
		jib.setPosition(tJib.x, tJib.y, tJib.z);
	}

	@Override
	public void execute() {		
		
		if( boom.kSize<boomAim){
			boom.kSize += boomStep;			
			final Position tBoom = CoordinatesTransform.i.translate(position, boomLocal);
			boom.setPosition(tBoom.x,tBoom.y,tBoom.z);
			
		}else if( jib.jSize<jibAim){
			jib.jSize +=  jibStep*5;		
			final Position t = CoordinatesTransform.i.translate(position, jibLocal);
			jib.setPosition(t.x, t.y, t.z);			
		}
	}

	@Override
	public boolean hasStopped() {
		return boom.kSize>=boomAim && jib.jSize>=jibAim;
	}

	
}
