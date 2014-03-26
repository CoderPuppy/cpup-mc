package cpup.mc.lib.util

import net.minecraft.util.Vec3

object VectorUtil {
	def getFarLook(pos: Vec3, look: Vec3, dist: Double) = {
		pos.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist)
	}
}