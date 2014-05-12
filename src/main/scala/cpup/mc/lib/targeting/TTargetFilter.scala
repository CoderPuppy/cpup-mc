package cpup.mc.lib.targeting

import net.minecraft.entity.Entity
import net.minecraft.block.Block
import cpup.mc.lib.util.pos.BlockPos

trait TTargetFilter[ENT <: Entity, BLK <: Block] {
	def entityClass: Class[ENT]
	def filterEntity(entity: ENT): Boolean

	def blockClass: Class[BLK]
	def filterBlock(pos: BlockPos): Boolean

	def filter(target: TTarget) = target.obj.forall(_ match {
		case Left(entity) => entityClass.isInstance(entity) && filterEntity(entity.asInstanceOf[ENT])
		case Right(pos) => blockClass.isInstance(pos.block) && filterBlock(pos)
		case _ => false
	})
}