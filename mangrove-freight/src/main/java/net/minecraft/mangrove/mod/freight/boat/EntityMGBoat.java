package net.minecraft.mangrove.mod.freight.boat;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.mangrove.mod.freight.MGFreightForge;
import net.minecraft.mangrove.mod.freight.MGFreightItems;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMGBoat extends Entity {
	private boolean isBoatEmpty;
	private double speedMultiplier;
	private int boatPosRotationIncrements;
	private double boatX;
	private double boatY;
	private double boatZ;
	private double boatYaw;
	private double boatPitch;
	@SideOnly(Side.CLIENT)
	private double velocityX;
	@SideOnly(Side.CLIENT)
	private double velocityY;
	@SideOnly(Side.CLIENT)
	private double velocityZ;

	public EntityMGBoat(World worldIn) {
		super(worldIn);
		this.isBoatEmpty = true;
		this.speedMultiplier = 0.07D;
		this.preventEntitySpawning = true;
//		this.setSize(2.5F, 1.6F);
		this.setSize(1.5F, 0.6F);
	}

	public EntityMGBoat(World worldIn, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
		this(worldIn);
		this.setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = p_i1705_2_;
		this.prevPosY = p_i1705_4_;
		this.prevPosZ = p_i1705_6_;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(17, new Integer(0));
		this.dataWatcher.addObject(18, new Integer(1));
		this.dataWatcher.addObject(19, new Float(0.0F));
	}

	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.getEntityBoundingBox();
	}

	public AxisAlignedBB getBoundingBox() {
		return this.getEntityBoundingBox();
	}

	public boolean canBePushed() {
		return true;
	}

	public double getMountedYOffset() {
		return (double) this.height * 0.0D - 0.30000001192092896D;
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else if (!this.worldObj.isRemote && !this.isDead) {
			Entity dsEntity = source.getEntity();
			if (this.riddenByEntity != null && this.riddenByEntity == dsEntity && source instanceof EntityDamageSourceIndirect) {
				return false;
			} else {
				this.setForwardDirection(-this.getForwardDirection());
				this.setTimeSinceHit(10);
				this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
				this.setBeenAttacked();
				boolean flag = dsEntity instanceof EntityPlayer && ((EntityPlayer) dsEntity).capabilities.isCreativeMode;

				if (flag || this.getDamageTaken() > 400.0F) {
					if (this.riddenByEntity != null) {
						this.riddenByEntity.mountEntity(this);
					}

					if (!flag) {
						this.dropItemWithOffset(MGFreightItems.itemMGBoat, 1, 0.0F);
					}

					this.setDead();
				}

				return true;
			}
		} else {
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	public void performHurtAnimation() {
		this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
//		this.setDamageTaken(this.getDamageTaken() * 2.0F);
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
		if (p_180426_10_ && this.riddenByEntity != null) {
			this.prevPosX = this.posX = x;
			this.prevPosY = this.posY = y;
			this.prevPosZ = this.posZ = z;
			this.rotationYaw = yaw;
			this.rotationPitch = pitch;
			this.boatPosRotationIncrements = 0;
			this.setPosition(x, y, z);
			this.motionX = this.velocityX = 0.0D;
			this.motionY = this.velocityY = 0.0D;
			this.motionZ = this.velocityZ = 0.0D;
		} else {
			if (this.isBoatEmpty) {
				this.boatPosRotationIncrements = posRotationIncrements + 5;
			} else {
				double d3 = x - this.posX;
				double d4 = y - this.posY;
				double d5 = z - this.posZ;
				double d6 = d3 * d3 + d4 * d4 + d5 * d5;

				if (d6 <= 1.0D) {
					return;
				}

				this.boatPosRotationIncrements = 3;
			}

			this.boatX = x;
			this.boatY = y;
			this.boatZ = z;
			this.boatYaw = (double) yaw;
			this.boatPitch = (double) pitch;
			this.motionX = this.velocityX;
			this.motionY = this.velocityY;
			this.motionZ = this.velocityZ;
		}
	}

	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.velocityX = this.motionX = x;
		this.velocityY = this.motionY = y;
		this.velocityZ = this.motionZ = z;
	}

	public void onUpdate() {
		super.onUpdate();

		if (this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if (this.getDamageTaken() > 0.0F) {
			this.setDamageTaken(this.getDamageTaken() - 1.0F);
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		byte b0 = 5;
		double d0 = 0.0D;

		AxisAlignedBB entityBB = this.getEntityBoundingBox();
		for (int i = 0; i < b0; ++i) {
			double ySize = entityBB.maxY - entityBB.minY;
			double d1 = entityBB.minY + ySize * (double) (i + 0) / (double) b0 - 0.125D;
			double d3 = entityBB.minY + ySize * (double) (i + 1) / (double) b0 - 0.125D;
			AxisAlignedBB axisalignedbb = new AxisAlignedBB(entityBB.minX, d1, entityBB.minZ, entityBB.maxX, d3,entityBB.maxZ);

			if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
				d0 += 1.0D / (double) b0;
			}
		}

		// Magnitude is given by the square root of sum of the x force squared and th z force squared sqrt(x*x + z*z)
		double magnitude = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		double d2;
		double d4;
		int j;

		if (magnitude > 0.2975D) {
			d2 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D);
			d4 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D);

			for (j = 0; (double) j < 1.0D + magnitude * 60.0D; ++j) {
				double d5 = (double) (this.rand.nextFloat() * 2.0F - 1.0F);
				double d6 = (double) (this.rand.nextInt(2) * 2 - 1) * 0.7D;
				double d7;
				double d8;

				if (this.rand.nextBoolean()) {
					d7 = this.posX - d2 * d5 * 0.8D + d4 * d6;
					d8 = this.posZ - d4 * d5 * 0.8D - d2 * d6;
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d7, this.posY - 0.125D, d8, this.motionX, this.motionY, this.motionZ, new int[0]);
				} else {
					d7 = this.posX + d2 + d4 * d5 * 0.7D;
					d8 = this.posZ + d4 - d2 * d5 * 0.7D;
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d7, this.posY - 0.125D, d8, this.motionX, this.motionY, this.motionZ, new int[0]);
				}
			}
		}

		double d10;
		double d11;

		if (this.worldObj.isRemote && this.isBoatEmpty) {
			if (this.boatPosRotationIncrements > 0) {
				d2 = this.posX + (this.boatX - this.posX) / (double) this.boatPosRotationIncrements;
				d4 = this.posY + (this.boatY - this.posY) / (double) this.boatPosRotationIncrements;
				d10 = this.posZ + (this.boatZ - this.posZ) / (double) this.boatPosRotationIncrements;
				d11 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double) this.rotationYaw);
				this.rotationYaw = (float) ((double) this.rotationYaw + d11 / (double) this.boatPosRotationIncrements);
				this.rotationPitch = (float) ((double) this.rotationPitch + (this.boatPitch - (double) this.rotationPitch) / (double) this.boatPosRotationIncrements);
				--this.boatPosRotationIncrements;
				this.setPosition(d2, d4, d10);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else {
				d2 = this.posX + this.motionX;
				d4 = this.posY + this.motionY;
				d10 = this.posZ + this.motionZ;
				this.setPosition(d2, d4, d10);

				if (this.onGround) {
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}

				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}
		} else {
			if (d0 < 1.0D) {
				d2 = d0 * 2.0D - 1.0D;
				this.motionY += 0.03999999910593033D * d2;
			} else {
				if (this.motionY < 0.0D) {
					this.motionY /= 2.0D;
				}

				this.motionY += 0.007000000216066837D;
			}

			if (this.riddenByEntity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) this.riddenByEntity;
				float f = this.riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
				this.motionX += -Math.sin((double) (f * (float) Math.PI / 180.0F)) * this.speedMultiplier * (double) entitylivingbase.moveForward * 0.05000000074505806D;
				this.motionZ += Math.cos((double) (f * (float) Math.PI / 180.0F)) * this.speedMultiplier * (double) entitylivingbase.moveForward * 0.05000000074505806D;
			}

			d2 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

			if (d2 > 0.35D) {
				d4 = 0.35D / d2;
				this.motionX *= d4;
				this.motionZ *= d4;
				d2 = 0.35D;
			}

			if (d2 > magnitude && this.speedMultiplier < 0.35D) {
				this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;

				if (this.speedMultiplier > 0.35D) {
					this.speedMultiplier = 0.35D;
				}
			} else {
				this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;

				if (this.speedMultiplier < 0.07D) {
					this.speedMultiplier = 0.07D;
				}
			}

			int l;

			for (l = 0; l < 4; ++l) {
				int i1 = MathHelper.floor_double(this.posX + ((double) (l % 2) - 0.5D) * 0.8D);
				j = MathHelper.floor_double(this.posZ + ((double) (l / 2) - 0.5D) * 0.8D);

				for (int j1 = 0; j1 < 2; ++j1) {
					int k = MathHelper.floor_double(this.posY) + j1;
					BlockPos blockpos = new BlockPos(i1, k, j);
					Block block = this.worldObj.getBlockState(blockpos).getBlock();

					if (block == Blocks.snow_layer) {
						this.worldObj.setBlockToAir(blockpos);
						this.isCollidedHorizontally = false;
					} else if (block == Blocks.waterlily) {
						this.worldObj.destroyBlock(blockpos, true);
						this.isCollidedHorizontally = false;
					}
				}
			}

			if (this.onGround) {
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);

//			if (this.isCollidedHorizontally && d9 > 0.2D) {
//				if (!this.worldObj.isRemote && !this.isDead) {
//					this.setDead();
//
//					for (l = 0; l < 3; ++l) {
//						this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
//					}
//
//					for (l = 0; l < 2; ++l) {
//						this.dropItemWithOffset(Items.stick, 1, 0.0F);
//					}
//				}
//			} else {
				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
//			}

			this.rotationPitch = 0.0F;
			d4 = (double) this.rotationYaw;
			d10 = this.prevPosX - this.posX;
			d11 = this.prevPosZ - this.posZ;

			if (d10 * d10 + d11 * d11 > 0.001D) {
				d4 = (double) ((float) (Math.atan2(d11, d10) * 180.0D / Math.PI));
			}

			double d12 = MathHelper.wrapAngleTo180_double(d4 - (double) this.rotationYaw);

			if (d12 > 20.0D) {
				d12 = 20.0D;
			}

			if (d12 < -20.0D) {
				d12 = -20.0D;
			}

			this.rotationYaw = (float) ((double) this.rotationYaw + d12);
			this.setRotation(this.rotationYaw, this.rotationPitch);

			if (!this.worldObj.isRemote) {
				List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, entityBB.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

				if (list != null && !list.isEmpty()) {
					for (int k1 = 0; k1 < list.size(); ++k1) {
						Entity entity = (Entity) list.get(k1);

						if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityMGBoat) {
							entity.applyEntityCollision(this);
						}
					}
				}

				if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
					this.riddenByEntity = null;
				}
			}
		}
	}

	public void updateRiderPosition() {
		if (this.riddenByEntity != null) {
			double d0 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			double d1 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			this.riddenByEntity.setPosition(this.posX + d0, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d1);
		}
	}

	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
	}

	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
	}

	public boolean interactFirst(EntityPlayer playerIn) {
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn) {
			return true;
		} else {
			if (!this.worldObj.isRemote) {
				playerIn.mountEntity(this);
			}

			return true;
		}
	}

	protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
		if (onGroundIn) {
			if (this.fallDistance > 3.0F) {
				this.fall(this.fallDistance, 1.0F);

				if (!this.worldObj.isRemote && !this.isDead) {
					this.setDead();
					int i;

					for (i = 0; i < 3; ++i) {
						this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
					}

					for (i = 0; i < 2; ++i) {
						this.dropItemWithOffset(Items.stick, 1, 0.0F);
					}
				}

				this.fallDistance = 0.0F;
			}
		} else if (this.worldObj.getBlockState((new BlockPos(this)).down()).getBlock().getMaterial() != Material.water && y < 0.0D) {
			this.fallDistance = (float) ((double) this.fallDistance - y);
		}
	}

	public void setDamageTaken(float p_70266_1_) {
		this.dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
	}

	public float getDamageTaken() {
		return this.dataWatcher.getWatchableObjectFloat(19);
	}

	public void setTimeSinceHit(int p_70265_1_) {
		this.dataWatcher.updateObject(17, Integer.valueOf(p_70265_1_));
	}

	public int getTimeSinceHit() {
		return this.dataWatcher.getWatchableObjectInt(17);
	}

	public void setForwardDirection(int p_70269_1_) {
		this.dataWatcher.updateObject(18, Integer.valueOf(p_70269_1_));
	}

	public int getForwardDirection() {
		return this.dataWatcher.getWatchableObjectInt(18);
	}

	@SideOnly(Side.CLIENT)
	public void setIsBoatEmpty(boolean p_70270_1_) {
		this.isBoatEmpty = p_70270_1_;
	}

	public static String getName() {
		return "mgboat";
	}
}
