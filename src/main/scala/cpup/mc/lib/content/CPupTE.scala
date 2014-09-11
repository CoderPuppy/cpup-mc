package cpup.mc.lib.content

import net.minecraft.tileentity.TileEntity
import cpup.mc.lib.util.pos.BlockPos
import cpup.mc.lib.{CPupModRef, CPupMod}

trait CPupTE[MOD <: CPupMod[_ <: CPupModRef]] extends TileEntity {
	def mod: MOD
	def world = getWorldObj
	def x = xCoord
	def y = yCoord
	def z = zCoord
	def pos = BlockPos(world, x, y, z)
}