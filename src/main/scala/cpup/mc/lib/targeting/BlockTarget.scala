package cpup.mc.lib.targeting

import cpup.mc.lib.util.pos.BlockPos
import net.minecraft.entity.Entity
import net.minecraft.block.Block
import net.minecraft.nbt.NBTTagCompound
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer
import cpup.mc.lib.mod.CPupLib
import cpup.mc.lib.inventory.EmptyInventory

case class BlockTarget(pos: BlockPos) extends TTarget {
	override def targetType = BlockTarget

	override def obj = Some(Right(pos))
	override def world = Some(pos.world)
	override def chunkX = Some(pos.chunkX)
	override def chunkZ = Some(pos.chunkZ)
	override def x = Some(pos.x)
	override def y = Some(pos.y)
	override def z = Some(pos.z)

	override def mop = None

	override def owner = null
	override def ownedTargets(typeNoun: TTargetFilter[_ <: Entity, _ <: Block]) = List()

	override def writeToNBT(nbt: NBTTagCompound) {
		nbt.setInteger("dim", pos.world.provider.dimensionId)
		nbt.setInteger("x", pos.x)
		nbt.setInteger("y", pos.y)
		nbt.setInteger("z", pos.z)
	}

	override def activeItems = Array()
	override def inventory = EmptyInventory
}

object BlockTarget extends TTargetType {
	def mod = CPupLib

	override def name = s"${mod.ref.modID}:block"
	override def targetClass = classOf[BlockTarget]
	override def readFromNBT(nbt: NBTTagCompound) = {
		val dim = nbt.getInteger("dim")
		Some(BlockTarget(BlockPos(
			FMLCommonHandler.instance.getEffectiveSide match {
				case Side.CLIENT =>
					val world = Minecraft.getMinecraft.theWorld
					if(world.provider.dimensionId != dim) {
						throw new Exception(s"entity isn't in the same dimension as the player, $dim, ${world.provider.dimensionId}")
					}
					world
				case Side.SERVER =>
					MinecraftServer.getServer.worldServerForDimension(dim)
			},
			nbt.getInteger("x"),
			nbt.getInteger("y"),
			nbt.getInteger("z")
		)))
	}
}