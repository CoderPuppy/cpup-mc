package cpup.mc.lib.content

import cpup.mc.lib.{CPupModHolder, CPupModRef, CPupMod}
import net.minecraft.world.World
import net.minecraft.block.{ITileEntityProvider, Block}

trait CPupBlockContainer[MOD <: CPupMod[_ <: CPupModRef]] extends CPupBlock[MOD] with ITileEntityProvider { self: CPupModHolder[MOD] =>
	override def breakBlock(world: World, x: Int, y: Int, z: Int, blk: Block, meta: Int) {
		super.breakBlock(world, x, y, z, blk, meta)
		world.removeTileEntity(x, y, z)
	}

	override def onBlockEventReceived(world: World, x: Int, y: Int, z: Int, id: Int, param: Int) = {
		super.onBlockEventReceived(world, x, y, z, id, param)
		val te = world.getTileEntity(x, y, z)
		if(te != null) te.receiveClientEvent(id, param) else false
	}
}