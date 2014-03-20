package cpup.mc.lib.content

import cpup.mc.lib.{CPupModRef, CPupMod}
import cpup.mc.lib.network.CPupMessage
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraft.block.{ITileEntityProvider, Block}

trait CPupBlockContainer[MOD <: CPupMod[_ <: CPupModRef, _ <: CPupMessage]] extends CPupBlock[MOD] with ITileEntityProvider {
	override def breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int) {
		super.breakBlock(world, x, y, z, block, meta)
		world.removeTileEntity(x, y, z)
	}

	override def onBlockEventReceived(world: World, x: Int, y: Int, z: Int, id: Int, param: Int) = {
		super.onBlockEventReceived(world, x, y, z, id, param)
		val te = world.getTileEntity(x, y, z)
		if(te != null) te.receiveClientEvent(id, param) else false
	}
}