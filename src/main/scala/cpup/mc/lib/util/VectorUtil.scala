package cpup.mc.lib.util

import net.minecraft.util.{MathHelper, Vec3}
import net.minecraft.world.World
import cpup.mc.lib.util.pos.BlockPos

object VectorUtil {
	def offset(pos: Vec3, look: Vec3, dist: Double) = {
		pos.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist)
	}

	def negate(vec: Vec3) = vec.myVec3LocalPool.getVecFromPool(-vec.xCoord, -vec.yCoord, -vec.zCoord)

	def get(world: World, x: Double, y: Double, z: Double) = world.getWorldVec3Pool.getVecFromPool(x, y, z)
	def offset(origin: Vec3, dir: Direction, amt: Double = 1) = origin.addVector(
		dir.forgeDir.offsetX * amt,
		dir.forgeDir.offsetY * amt,
		dir.forgeDir.offsetZ * amt
	)

	def toBlockPos(world: World, vec: Vec3) = BlockPos(
		world,
		MathHelper.floor_double(vec.xCoord + 0.5),
		MathHelper.floor_double(vec.yCoord + 0.5),
		MathHelper.floor_double(vec.zCoord + 0.5)
	)
}