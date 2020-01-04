package cpup.mc.lib.util

import net.minecraft.util.math.{BlockPos ⇒ MCBP}
import net.minecraft.world.World

case class BlockPos(world: World, x: Int, y: Int, z: Int) {
	def mc = new MCBP(x, y, z)
}

object BlockPos {
	type MC = MCBP
}
