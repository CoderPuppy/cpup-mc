package cpup.mc.lib.util

import net.minecraft.util.MathHelper
import net.minecraftforge.common.util.ForgeDirection

abstract class Direction(val forgeDir: ForgeDirection) {
	if(forgeDir == ForgeDirection.UNKNOWN) {
		throw new RuntimeException("Unknown isn't a valid direction")
	}

	def opposite: Direction
	def side: Int
	def facing: Int
}

object Direction {
	case object North extends Direction(ForgeDirection.NORTH) {
		def side = 2
		def facing = 2
		def opposite = South
	}
	case object East extends Direction(ForgeDirection.EAST) {
		def side = 5
		def facing = 3
		def opposite = West
	}
	case object South extends Direction(ForgeDirection.SOUTH) {
		def side = 3
		def facing = 0
		def opposite = North
	}
	case object West extends Direction(ForgeDirection.WEST) {
		def side = 4
		def facing = 1
		def opposite = East
	}
	case object Up extends Direction(ForgeDirection.UP) {
		def side = 1
		def facing = -1
		def opposite = Down
	}
	case object Down extends Direction(ForgeDirection.DOWN) {
		def side = 0
		def facing = -1
		def opposite = Up
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