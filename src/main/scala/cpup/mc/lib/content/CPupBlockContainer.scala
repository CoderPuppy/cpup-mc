package cpup.mc.lib.content

import cpup.mc.lib.util.Direction
import cpup.mc.lib.{CPupModHolder, CPupModRef, CPupMod}
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.block.{ITileEntityProvider, Block}

trait CPupBlockContainer[MOD <: CPupMod[_ <: CPupModRef]] extends CPupBlock[MOD] with ITileEntityProvider { self: CPupModHolder[MOD] =>
	override def breakBlock(world: World, pos: BlockPos, state: IBlockState) {
		super.breakBlock(world, pos, state)
		world.removeTileEntity(pos)
	}

	private def getTE(world: IBlockAccess, pos: BlockPos) = world.getTileEntity(pos) match {
		case te: CPupBlockContainer.TE => Some(te)
		case _ => None
	}

	override def onNeighborChange(world: IBlockAccess, pos: BlockPos, change: BlockPos) {
		super.onNeighborChange(world, pos, change)
		getTE(world, pos).foreach(_.onNeighborChange(Direction.from(change.subtract(pos))))
	}

	override def onBlockEventReceived(world: World, pos: BlockPos, state: IBlockState, id: Int, param: Int) = {
		super.onBlockEventReceived(world, pos, state, id, param)
		val te = world.getTileEntity(pos)
		if(te != null) te.receiveClientEvent(id, param) else false
	}
}
object CPupBlockContainer {
	trait TE {
		def onNeighborChange(dir: EnumFacing) {}
	}
}
