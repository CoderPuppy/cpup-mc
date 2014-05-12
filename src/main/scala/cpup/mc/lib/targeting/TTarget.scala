package cpup.mc.lib.targeting

import net.minecraft.world.World
import net.minecraft.entity.Entity
import net.minecraft.block.Block
import cpup.mc.lib.util.pos.BlockPos
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition

trait TTarget {
	def targetType: TTargetType
	def writeToNBT(nbt: NBTTagCompound)

	def mop: Option[MovingObjectPosition]

	def world: Option[World]
	def chunkX: Option[Int]
	def chunkZ: Option[Int]
	def x: Option[Double]
	def y: Option[Double]
	def z: Option[Double]
	def isValid = true
	def obj: Option[Either[Entity, BlockPos]]
	def ownedTargets(filter: TTargetFilter[_ <: Entity, _ <: Block]): List[TTarget]
	def owner: Option[TTarget]

	def getActiveItems: Array[ItemStack]
	def getActiveInventory: IInventory

	def sameObj(other: TTarget) = obj.exists(_ match {
		case Left(entity) =>
			other.obj.exists(_ match {
				case Left(otherEntity) =>
					otherEntity == entity
				case _ => false
			})
		case Right(pos) =>
			other.obj.exists(_ match {
				case Right(otherPos) =>
					otherPos== pos
				case _ => false
			})
		case _ => false
	})
}