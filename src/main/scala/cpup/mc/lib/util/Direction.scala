package cpup.mc.lib.util

import net.minecraft.util.MathHelper
import net.minecraftforge.common.util.ForgeDirection

abstract class Direction(val forgeDir: ForgeDirection) {
	if(forgeDir == ForgeDirection.UNKNOWN) {
		throw new RuntimeException("Unknown isn't a valid direction")
	}

	final val x = forgeDir.offsetX
	final val y = forgeDir.offsetY
	final val z = forgeDir.offsetZ

	def opposite: Direction
	def side: Int
	def facing: Int

	def rotated(axis: Direction, amt: Int = 1) = {
		var res = this
		for(i <- 1 to amt) {
			res = Direction.from(res.forgeDir.getRotation(axis.forgeDir))
		}
		res
	}

	def unrotated(axis: Direction, amt: Int = 1) = rotated(axis.opposite, amt)
}

object Direction {
	case object North extends Direction(ForgeDirection.NORTH) {
		final val side = 2
		final val facing = 2
		final val opposite = South
	}
	case object East extends Direction(ForgeDirection.EAST) {
		final val side = 5
		final val facing = 3
		final val opposite = West
	}
	case object South extends Direction(ForgeDirection.SOUTH) {
		final val side = 3
		final val facing = 0
		final val opposite = North
	}
	case object West extends Direction(ForgeDirection.WEST) {
		final val side = 4
		final val facing = 1
		final val opposite = East
	}
	case object Up extends Direction(ForgeDirection.UP) {
		final val side = 1
		final val facing = -1
		final val opposite = Down
	}
	case object Down extends Direction(ForgeDirection.DOWN) {
		final val side = 0
		final val facing = -1
		final val opposite = Up
	}

	def from(dir: ForgeDirection) = dir match {
		case ForgeDirection.NORTH => North
		case ForgeDirection.EAST => East
		case ForgeDirection.SOUTH => South
		case ForgeDirection.WEST => West
		case ForgeDirection.UP => Up
		case ForgeDirection.DOWN => Down
		case _ => null
	}

	def fromSide(side: Int) = side match {
		case 2 => North
		case 5 => East
		case 3 => South
		case 4 => West
		case 1 => Up
		case 0 => Down
		case _ => null
	}

	def fromFacing(f: Int) = f match {
		case 2 => North
		case 3 => East
		case 0 => South
		case 1 => West
		case _ => null
	}

	def fromYaw(rot: Float) = fromFacing(MathHelper.floor_double((rot / 90f) + 0.5) & 3)
}