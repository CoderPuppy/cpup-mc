package cpup.mc.lib.util.pos

import net.minecraft.world.World
import net.minecraft.block.Block
import cpup.mc.lib.util.Direction
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MathHelper

case class BlockPos(world: World, x: Int, y: Int, z: Int) {
	def chunkX = (x - x % 16) / 16
	def chunkZ = (z - z % 16) / 16

	def setBlock(block: Block) = {
		world.setBlock(x, y, z, block)

		this
	}

	def setMetadata(meta: Int, notify: Int) = {
		world.setBlockMetadataWithNotify(x, y, z, meta, notify)

		this
	}

	def setTileEntity(te: TileEntity) = {
		world.setTileEntity(x, y, z, te)

		this
	}

	def block = world.getBlock(x, y, z)
	def metadata = world.getBlockMetadata(x, y, z)
	def tileEntity = world.getTileEntity(x, y, z)
	def tileEntity_=(newTE: TileEntity) = {
		setTileEntity(newTE)
		newTE
	}

	def offset(offsetX: Int, offsetY: Int, offsetZ: Int) = BlockPos(world, x + offsetX, y + offsetY, z + offsetZ)
	def offset(dir: Direction, dist: Int = 1): BlockPos = offset(dir.forgeDir.offsetX * dist, dir.forgeDir.offsetY * dist, dir.forgeDir.offsetZ * dist)

	def isAir = block.isAir(world, x, y, z)
	def isReplaceable = block.isReplaceable(world, x, y, z)

	def tryReplaceWith(block: Block) { tryReplaceWith(block, 0) }
	def tryReplaceWith(block: Block, meta: Int) {
		if(isReplaceable || isAir) {
			setBlock(block)
			setMetadata(meta, 2)
		}
	}

	def scheduleUpdateTick(time: Int) {
		world.scheduleBlockUpdate(x, y, z, block, time)
	}
}