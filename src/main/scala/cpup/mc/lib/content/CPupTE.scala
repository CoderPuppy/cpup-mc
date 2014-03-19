package cpup.mc.lib.content

import net.minecraft.tileentity.TileEntity
import cpup.mc.lib.util.pos.BlockPos

trait CPupTE extends TileEntity {
	def world = getWorldObj
	def x = xCoord
	def y = yCoord
	def z = zCoord
	def pos = BlockPos(world, x, y, z)
}