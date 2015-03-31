package cpup.mc.lib.content

import net.minecraft.tileentity.TileEntity
import cpup.mc.lib.{CPupModRef, CPupMod}

trait CPupTE[MOD <: CPupMod[_ <: CPupModRef]] extends TileEntity {
	def mod: MOD
	lazy val world = getWorldObj
	lazy val x = xCoord
	lazy val y = yCoord
	lazy val z = zCoord
}