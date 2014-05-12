package cpup.mc.lib.targeting

import net.minecraft.entity.Entity
import cpup.mc.lib.mod.CPupLib
import net.minecraft.block.Block
import cpup.mc.lib.util.EntityUtil
import net.minecraft.nbt.NBTTagCompound
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer

case class EntityTarget(entity: Entity) extends TTarget {
	def mod = CPupLib

	override def targetType = EntityTarget

	override def world = Some(entity.worldObj)
	override def chunkX = Some(entity.chunkCoordX)
	override def chunkZ = Some(entity.chunkCoordZ)
	override def x = Some(entity.posX)
	override def y = Some(entity.posY)
	override def z = Some(entity.posZ)

	override def mop = Some(EntityUtil.getMOPBoth(entity, 4))
	override def obj = Some(Left(entity))

	override def ownedTargets(typeNoun: TTargetFilter[_ <: Entity, _ <: Block]) = List()
	override def owner = null // TODO: owner

	override def getActiveItems = entity.getLastActiveItems match {
		case null => Array()
		case items => items
	}
	override def getActiveInventory = ???

	override def writeToNBT(nbt: NBTTagCompound) {
		nbt.setInteger("dim", world.get.provider.dimensionId)
		nbt.setInteger("id", entity.getEntityId)
	}
}

object EntityTarget extends TTargetType {
	def mod = CPupLib

	override def name = s"${mod.ref.modID}:entity"
	override def targetClass = classOf[EntityTarget]
	override def readFromNBT(nbt: NBTTagCompound) = {
		val dim = nbt.getInteger("dim")
		Some(EntityTarget(
			(FMLCommonHandler.instance.getEffectiveSide match {
				case Side.CLIENT =>
					val world = Minecraft.getMinecraft.theWorld
					if(world.provider.dimensionId != dim) {
						throw new Exception(s"entity isn't in the same dimension as the player, $dim, ${world.provider.dimensionId}")
					}
					world
				case Side.SERVER =>
					MinecraftServer.getServer.worldServerForDimension(dim)
				case _ => null
			}).getEntityByID(nbt.getInteger("id"))
		))
	}
}