package cpup.mc.lib.util

import net.minecraft.entity.Entity
import net.minecraft.util.{MathHelper, MovingObjectPosition, AxisAlignedBB, Vec3}
import java.util.List
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side
import net.minecraftforge.common.IExtendedEntityProperties

object EntityUtil {
	def getExtendedData[D <: IExtendedEntityProperties](e: Entity, name: String, default: => D)(implicit manifest: Manifest[D]) = {
		val genData = e.getExtendedProperties(name)
		genData match {
			case data: D => Some(data)
			case _ if genData == null => Option(default)
			case _ => None
		}
	}

	def getPos(entity: Entity) = {
		var y = entity.posY
		if(FMLCommonHandler.instance.getEffectiveSide == Side.SERVER) {
			y += 1.6200000047683716 // TODO: Figure out why I need this
		}
		entity.worldObj.getWorldVec3Pool.getVecFromPool(entity.posX, y, entity.posZ)
	}

	def getLook(entity: Entity) = {
		val f1 = MathHelper.cos(-entity.rotationYaw * 0.017453292F - Math.PI.asInstanceOf[Float])
		val f2 = MathHelper.sin(-entity.rotationYaw * 0.017453292F - Math.PI.asInstanceOf[Float])
		val f3 = -MathHelper.cos(-entity.rotationPitch * 0.017453292F)
		val f4 = MathHelper.sin(-entity.rotationPitch * 0.017453292F)
		entity.worldObj.getWorldVec3Pool.getVecFromPool((f2 * f3).asInstanceOf[Double], f4.asInstanceOf[Double], (f1 * f3).asInstanceOf[Double])
	}

	def getMOPBlock(entity: Entity, reach: Double) = {
		val pos = getPos(entity)
		val farLook = VectorUtil.getFarLook(pos, getLook(entity), reach)

		entity.worldObj.func_147447_a(pos, farLook, false, false, true)
	}

	def getMOPBoth(entity: Entity, _reach: Double) = {
		var reach = _reach
		var mop = getMOPBlock(entity, reach)
		val pos = getPos(entity)

		if(mop != null) {
			reach = mop.hitVec.distanceTo(pos)
		}

		val look = getLook(entity)
		val farLook = VectorUtil.getFarLook(pos, look, reach)
		var pointedEntity: Entity = null
		var vec33: Vec3 = null
		val f1: Float = 1.0F
		val list: List[_] = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.addCoord(look.xCoord * reach, look.yCoord * reach, look.zCoord * reach).expand(f1.asInstanceOf[Double], f1.asInstanceOf[Double], f1.asInstanceOf[Double]))
		var d2: Double = reach;
		{
			var i: Int = 0
			while(i < list.size) {
				{
					val entity: Entity = list.get(i).asInstanceOf[Entity]
					if(entity.canBeCollidedWith) {
						val f2: Float = entity.getCollisionBorderSize
						val axisalignedbb: AxisAlignedBB = entity.boundingBox.expand(f2.asInstanceOf[Double], f2.asInstanceOf[Double], f2.asInstanceOf[Double])
						val movingobjectposition: MovingObjectPosition = axisalignedbb.calculateIntercept(pos, farLook)
						if(axisalignedbb.isVecInside(pos)) {
							if(0.0D < d2 || d2 == 0.0D) {
								pointedEntity = entity
								vec33 = if(movingobjectposition == null) pos else movingobjectposition.hitVec
								d2 = 0.0D
							}
						}
						else if(movingobjectposition != null) {
							val d3: Double = pos.distanceTo(movingobjectposition.hitVec)
							if(d3 < d2 || d2 == 0.0D) {
								if(entity == entity.ridingEntity && !entity.canRiderInteract) {
									if(d2 == 0.0D) {
										pointedEntity = entity
										vec33 = movingobjectposition.hitVec
									}
								}
								else {
									pointedEntity = entity
									vec33 = movingobjectposition.hitVec
									d2 = d3
								}
							}
						}
					}
				}
				({i += 1; i})
			}
		}
		if(pointedEntity != null && (d2 < reach || mop == null)) {
			mop = new MovingObjectPosition(pointedEntity, vec33)
		}

		mop
	}
}