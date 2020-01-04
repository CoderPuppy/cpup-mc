package cpup.mc.lib.util

import java.util.List

import net.minecraft.util.math.{RayTraceResult, AxisAlignedBB, Vec3d, MathHelper}
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraft.entity.Entity
import net.minecraft.util._
import net.minecraft.world.World

object EntityUtil {
	def wouldSuffocate(world: World, x: Double, y: Double, z: Double, width: Float, height: Float, eyeHeight: Double): Boolean = {
		for(i <- 0 to 8) {
			val f: Float = (((i >> 0) % 2).asInstanceOf[Float] - 0.5F) * width * 0.8F
			val f1: Float = (((i >> 1) % 2).asInstanceOf[Float] - 0.5F) * 0.1F
			val f2: Float = (((i >> 2) % 2).asInstanceOf[Float] - 0.5F) * width * 0.8F
			val j: Int = MathHelper.floor_double(x + f.asInstanceOf[Double])
			val k: Int = MathHelper.floor_double(y + eyeHeight + f1.asInstanceOf[Double])
			val l: Int = MathHelper.floor_double(z + f2.asInstanceOf[Double])
			if(world.getBlockState(new BlockPos.MC(j, k, l)).isNormalCube) {
				return true
			}
		}
		return false
	}
	def wouldSuffocate(e: Entity, x: Double, y: Double, z: Double): Boolean = {
		wouldSuffocate(e.worldObj, x, y, z, e.width, e.height, e.getEyeHeight)
	}
	def wouldSuffocate(e: Entity): Boolean = wouldSuffocate(e, e.posX, e.posY, e.posZ)

	// TODO
//	def getExtendedData[D <: IExtendedEntityProperties](e: Entity, name: String, default: => D)(implicit manifest: Manifest[D]) = {
//		val genData = e.getExtendedProperties(name)
//		genData match {
//			case data: D => Some(data)
//			case _ if genData == null => {
//				val data = default
//				if(data == null) {
//					None
//				} else {
//					if(e.registerExtendedProperties(name, data) == name) {
//						Some(data)
//					} else {
//						None
//					}
//				}
//			}
//			case _ => None
//		}
//	}

	def getPos(entity: Entity) = {
		var y = entity.posY
		if(Side.effective.isServer) {
			y += 1.6200000047683716 // TODO: Figure out why I need this
		}
		new Vec3d(entity.posX, y, entity.posZ)
	}

	def getLook(entity: Entity) = {
		val f1 = MathHelper.cos(-entity.rotationYaw * 0.017453292F - Math.PI.asInstanceOf[Float])
		val f2 = MathHelper.sin(-entity.rotationYaw * 0.017453292F - Math.PI.asInstanceOf[Float])
		val f3 = -MathHelper.cos(-entity.rotationPitch * 0.017453292F)
		val f4 = MathHelper.sin(-entity.rotationPitch * 0.017453292F)
		new Vec3d((f2 * f3).asInstanceOf[Double], f4.asInstanceOf[Double], (f1 * f3).asInstanceOf[Double])
	}

	def getMOPBlock(entity: Entity, reach: Double) = {
		val pos = getPos(entity)
		val farLook = VectorUtil.offset(pos, getLook(entity), reach)

		entity.worldObj.rayTraceBlocks(pos, farLook, false, true, false)
	}

	// TODO: steal from EntityRenderer.getMouseover
}
