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
	def axis: Direction.Axis
	def dir: Int

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
	sealed trait Axis
	case object X extends Axis
	case object Y extends Axis
	case object Z extends Axis

	case object North extends Direction(ForgeDirection.NORTH) {
		final val side = 2
		final val facing = 2
		final val opposite = South
		override def axis = Z
		override def dir = -1
	}
	case object East extends Direction(ForgeDirection.EAST) {
		final val side = 5
		final val facing = 3
		final val opposite = West
		override def axis = X
		override def dir = 1
	}
	case object South extends Direction(ForgeDirection.SOUTH) {
		final val side = 3
		final val facing = 0
		final val opposite = North
		override def axis = Z
		override def dir = 1
	}
	case object West extends Direction(ForgeDirection.WEST) {
		final val side = 4
		final val facing = 1
		final val opposite = East
		override def axis = X
		override def dir = -1
	}
	case object Up extends Direction(ForgeDirection.UP) {
		final val side = 1
		final val facing = -1
		final val opposite = Down
		override def axis = Y
		override def dir = 1
	}
	case object Down extends Direction(ForgeDirection.DOWN) {
		final val side = 0
		final val facing = -1
		final val opposite = Up
		override def axis = Y
		override def dir = -1
	}

	lazy val valid = Seq(North, East, South, West, Up, Down)

	def from(dir: ForgeDirection) = dir match {
		case ForgeDirection.NORTH => North
		case ForgeDirection.EAST => East
		case ForgeDirection.SOUTH => South
		case ForgeDirection.WEST => West
		case ForgeDirection.UP => Up
		case ForgeDirection.DOWN => Down
		case _ => null
	}

	def from(x: Int, y: Int, z: Int) = (x, y, z) match {
		case ( 0, -1,  0) => Down
		case ( 0,  1,  0) => Up
		case ( 0,  0, -1) => North
		case ( 0,  0,  1) => South
		case (-1,  0,  0) => West
		case ( 1,  0,  0) => East
		case _ => null
	}

	def from(axis: Axis, dir: Int) = (axis, dir) match {
		case (Z, -1) => North
		case (X, 1) => East
		case (Z, 1) => South
		case (X, -1) => West
		case (Y, 1) => Up
		case (Y, -1) => Down
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
