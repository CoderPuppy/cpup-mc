package cpup.mc.lib.content

import net.minecraft.tileentity.TileEntity
import cpup.mc.lib.{CPupModHolder, CPupModRef, CPupMod}
import net.minecraft.world.World

trait CPupTE[MOD <: CPupMod[_ <: CPupModRef]] extends TileEntity { self: CPupModHolder[MOD] =>

}
