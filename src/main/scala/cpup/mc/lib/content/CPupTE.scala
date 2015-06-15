package cpup.mc.lib.content

import net.minecraft.tileentity.TileEntity
import cpup.mc.lib.{CPupModHolder, CPupModRef, CPupMod}
import net.minecraft.world.World

trait CPupTE[MOD <: CPupMod[_ <: CPupModRef]] extends TileEntity { self: CPupModHolder[MOD] =>
	var world: World = null
	var x: Int = 0
	var y: Int = 0
	var z: Int = 0

	override def validate {
		super.validate
		world = getWorldObj
		x = xCoord
		y = yCoord
		z = zCoord
	}
}
