package cpup.mc.lib.content

import cpup.mc.lib.util.Direction
import cpup.mc.lib.{CPupModHolder, CPupModRef, CPupMod}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.block.{ITileEntityProvider, Block}

trait CPupBlockContainer[MOD <: CPupMod[_ <: CPupModRef]] extends CPupBlock[MOD] with ITileEntityProvider { self: CPupModHolder[MOD] =>
	override def breakBlock(world: World, x: Int, y: Int, z: Int, blk: Block, meta: Int) {
		super.breakBlock(world, x, y, z, blk, meta)
		world.removeTileEntity(x, y, z)
	}

	private def getTE(world: IBlockAccess, x: Int, y: Int, z: Int) = world.getTileEntity(x, y, z) match {
		case te: CPupBlockContainer.TE => Some(te)
		case _ => None
	}

	override def onNeighborChange(world: IBlockAccess, x: Int, y: Int, z: Int, changeX: Int, changeY: Int, changeZ: Int) {
		super.onNeighborChange(world, x, y, z, changeX, changeY, changeZ)
		getTE(world, x, y, z).foreach(_.onNeighborChange(Direction.from(changeX - x, changeY - y, changeZ - z)))
	}

	override def onBlockEventReceived(world: World, x: Int, y: Int, z: Int, id: Int, param: Int) = {
		super.onBlockEventReceived(world, x, y, z, id, param)
		val te = world.getTileEntity(x, y, z)
		if(te != null) te.receiveClientEvent(id, param) else false
	}
}
object CPupBlockContainer {
	trait TE {
		def onNeighborChange(dir: Direction) {}
	}
}
