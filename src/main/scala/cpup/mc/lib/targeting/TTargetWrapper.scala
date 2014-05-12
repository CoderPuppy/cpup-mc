package cpup.mc.lib.targeting

import net.minecraft.entity.Entity
import net.minecraft.block.Block
import cpup.mc.lib.inventory.EmptyInventory

trait TTargetWrapper extends TTarget {
	def wrapped: Option[TTarget]

	override def obj = wrapped.flatMap(_.obj)
	override def world = wrapped.flatMap(_.world)
	override def chunkX = wrapped.flatMap(_.chunkX)
	override def chunkZ = wrapped.flatMap(_.chunkZ)
	override def x = wrapped.flatMap(_.x)

	override def y = wrapped.flatMap(_.y)
	override def z = wrapped.flatMap(_.z)

	override def getActiveInventory = wrapped.map(_.getActiveInventory).getOrElse(EmptyInventory)
	override def getActiveItems = wrapped.map(_.getActiveItems).getOrElse(Array())

	override def owner = wrapped.flatMap(_.owner)
	override def ownedTargets(filter: TTargetFilter[_ <: Entity, _ <: Block]) = wrapped.map(_.ownedTargets(filter)).getOrElse(List())

	override def mop = wrapped.flatMap(_.mop)
}