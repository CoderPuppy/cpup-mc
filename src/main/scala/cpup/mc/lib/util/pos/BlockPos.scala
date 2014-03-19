package cpup.mc.lib.util.pos

import net.minecraft.world.World
import net.minecraft.block.Block
import cpup.mc.lib.util.Direction

case class BlockPos(world: World, x: Int, y: Int, z: Int) {
	def setBlock(block: Block) = {
		world.setBlock(x, y, z, block)

		this
	}

	def setMetadata(meta: Int, notify: Int) = {
		world.setBlockMetadataWithNotify(x, y, z, meta, notify)

		this
	}

	def block = world.getBlock(x, y, z)
	def metadata = world.getBlockMetadata(x, y, z)
	def tileEntity = world.getTileEntity(x, y, z)

	def offset(offsetX: Int, offsetY: Int, offsetZ: Int) = BlockPos(world, x + offsetX, y + offsetY, z + offsetZ)
	def offset(dir: Direction, dist: Int = 1): BlockPos = offset(dir.forgeDir.offsetX * dist, dir.forgeDir.offsetY * dist, dir.forgeDir.offsetZ * dist)

	def isAir = block.isAir(world, x, y, z)
	def isReplaceable = block.isReplaceable(world, x, y, z)
}