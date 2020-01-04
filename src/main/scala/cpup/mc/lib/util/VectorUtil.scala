package cpup.mc.lib.util

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.{MathHelper, Vec3d}
import net.minecraft.world.World

object VectorUtil {
	def offset(pos: Vec3d, look: Vec3d, dist: Double) = {
		pos.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist)
	}

	def negate(vec: Vec3d) = new Vec3d(-vec.xCoord, -vec.yCoord, -vec.zCoord)

	def offset(origin: Vec3d, dir: EnumFacing, amt: Double = 1) = origin.addVector(
		dir.getFrontOffsetX * amt,
		dir.getFrontOffsetY * amt,
		dir.getFrontOffsetZ * amt
	)

	def toBlockPos(vec: Vec3d, world: World) = BlockPos(
		world,
		MathHelper.floor_double(vec.xCoord + 0.5),
		MathHelper.floor_double(vec.yCoord + 0.5),
		MathHelper.floor_double(vec.zCoord + 0.5)
	)
}
