package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityAIFollowGolem extends EntityAIBase {
	private EntityVillager theVillager;
	private EntityIronGolem theGolem;
	private int takeGolemRoseTick;
	private boolean tookGolemRose = false;

	public EntityAIFollowGolem(EntityVillager par1EntityVillager) {
		this.theVillager = par1EntityVillager;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.theVillager.getGrowingAge() >= 0) {
			return false;
		} else if (!this.theVillager.worldObj.isDaytime()) {
			return false;
		} else {
			List var1 = this.theVillager.worldObj.getEntitiesWithinAABB(EntityIronGolem.class,
					this.theVillager.boundingBox.expand(6.0D, 2.0D, 6.0D));

			if (var1.isEmpty()) {
				return false;
			} else {
				Iterator var2 = var1.iterator();

				while (var2.hasNext()) {
					EntityIronGolem var3 = (EntityIronGolem) var2.next();

					if (var3.getHoldRoseTick() > 0) {
						this.theGolem = var3;
						break;
					}
				}

				return this.theGolem != null;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		return this.theGolem.getHoldRoseTick() > 0;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.takeGolemRoseTick = this.theVillager.getRNG().nextInt(320);
		this.tookGolemRose = false;
		this.theGolem.getNavigator().clearPathEntity();
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		this.theGolem = null;
		this.theVillager.getNavigator().clearPathEntity();
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		this.theVillager.getLookHelper().setLookPositionWithEntity(this.theGolem, 30.0F, 30.0F);

		if (this.theGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
			this.theVillager.getNavigator().tryMoveToEntityLiving(this.theGolem, 0.15F);
			this.tookGolemRose = true;
		}

		if (this.tookGolemRose && this.theVillager.getDistanceSqToEntity(this.theGolem) < 4.0D) {
			this.theGolem.setHoldingRose(false);
			this.theVillager.getNavigator().clearPathEntity();
		}
	}
}
