package cpup.mc.lib.util

import net.minecraft.util.Vec3
import net.minecraft.world.World

case class BlockPos(world: World, x: Int, y: Int, z: Int) {
	def toVec3 = Vec3.createVectorHelper(x, y, z)
}
