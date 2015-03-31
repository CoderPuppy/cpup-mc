package cpup.mc.lib.util

import net.minecraft.util.Vec3

case class BlockPos(x: Int, y: Int, z: Int) {
	def toVec3 = Vec3.createVectorHelper(x, y, z)
}
