package cpup.mc.lib.util

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.{MathHelper, Vec3i}
import net.minecraft.util.EnumFacing.{UP, DOWN, NORTH, SOUTH, WEST, EAST}
import net.minecraft.util.EnumFacing.Axis.{X, Y, Z}
import net.minecraft.util.EnumFacing.AxisDirection.{POSITIVE, NEGATIVE}

object Direction {
	def from(pos: Vec3i) = (pos.getX, pos.getY, pos.getZ) match {
		case ( 0, -1,  0) => DOWN
		case ( 0,  1,  0) => UP
		case ( 0,  0, -1) => NORTH
		case ( 0,  0,  1) => SOUTH
		case (-1,  0,  0) => WEST
		case ( 1,  0,  0) => EAST
	}

	def from(axis: EnumFacing.Axis, dir: EnumFacing.AxisDirection) = (axis, dir) match {
		case (Z, NEGATIVE) => NORTH
		case (X, POSITIVE) => EAST
		case (Z, POSITIVE) => SOUTH
		case (X, NEGATIVE) => WEST
		case (Y, POSITIVE) => UP
		case (Y, NEGATIVE) => DOWN
	}

	def from(side: String) = side match {
		case "up" ⇒ UP
		case "down" ⇒ DOWN
		case "north" ⇒ NORTH
		case "south" ⇒ SOUTH
		case "west" ⇒ WEST
		case "east" ⇒ EAST
	}

	def fromSide(side: Int) = side match {
		case 2 => NORTH
		case 5 => EAST
		case 3 => SOUTH
		case 4 => WEST
		case 1 => UP
		case 0 => DOWN
	}

	def toSide(side: EnumFacing) = side match {
		case NORTH ⇒ 2
		case EAST ⇒ 5
		case SOUTH ⇒ 3
		case WEST ⇒ 4
		case UP ⇒ 1
		case DOWN ⇒ 0
	}

	def fromFacing(f: Int) = f match {
		case 2 => NORTH
		case 3 => EAST
		case 0 => SOUTH
		case 1 => WEST
	}

	def toFacing(f: EnumFacing) = f match {
		case NORTH ⇒ 2
		case EAST ⇒ 3
		case SOUTH ⇒ 0
		case WEST ⇒ 1
	}

	def fromYaw(rot: Float) = fromFacing(MathHelper.floor_double((rot / 90f) + 0.5) & 3)
}
