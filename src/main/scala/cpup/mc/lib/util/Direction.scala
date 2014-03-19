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

	def from(dir: ForgeDirection) = dir match {
		case ForgeDirection.NORTH => North
		case ForgeDirection.EAST => East
		case ForgeDirection.SOUTH => South
		case ForgeDirection.WEST => West
		case _ => null
	}

	def fromSide(side: Int) = from(side match {
		case 2 => ForgeDirection.NORTH
		case 5 => ForgeDirection.EAST
		case 3 => ForgeDirection.SOUTH
		case 4 => ForgeDirection.WEST
		case _ => ForgeDirection.UNKNOWN
	})

	def fromFacing(f: Int) = from(f match {
		case 2 => ForgeDirection.NORTH
		case 3 => ForgeDirection.EAST
		case 0 => ForgeDirection.SOUTH
		case 1 => ForgeDirection.WEST
		case _ => ForgeDirection.UNKNOWN
	})

	def fromYaw(rot: Float) = fromFacing(MathHelper.floor_double((rot / 90f) + 0.5) & 3)
}