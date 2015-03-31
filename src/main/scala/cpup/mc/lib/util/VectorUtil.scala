package cpup.mc.lib.util

import net.minecraft.util.{MathHelper, Vec3}

object VectorUtil {
	def offset(pos: Vec3, look: Vec3, dist: Double) = {
		pos.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist)
	}

	def negate(vec: Vec3) = Vec3.createVectorHelper(-vec.xCoord, -vec.yCoord, -vec.zCoord)

	def offset(origin: Vec3, dir: Direction, amt: Double = 1) = origin.addVector(
		dir.x * amt,
		dir.y * amt,
		dir.z * amt
	)

	def toBlockPos(vec: Vec3) = BlockPos(
		MathHelper.floor_double(vec.xCoord + 0.5),
		MathHelper.floor_double(vec.yCoord + 0.5),
		MathHelper.floor_double(vec.zCoord + 0.5)
	)
}