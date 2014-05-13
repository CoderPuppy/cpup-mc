package cpup.mc.lib.targeting

import scala.collection.mutable
import net.minecraft.entity.Entity
import net.minecraft.block.Block
import cpup.mc.lib.util.pos.BlockPos

object TargetingRegistry {
	private val entityWrappers = new mutable.HashMap[Class[Entity], Entity => TTarget]
	private val blockWrappers = new mutable.HashMap[Class[Block], (Block, BlockPos) => TTarget]

	def registerEntityTarget[T <: Entity](gCla: Class[T], wrapper: T => TTarget) {
		entityWrappers(gCla.asInstanceOf[Class[Entity]]) = wrapper.asInstanceOf[Entity => TTarget]
	}

	def registerBlockTarget[T <: Block](gCla: Class[T], wrapper: (T, BlockPos) => TTarget) {
		blockWrappers(gCla.asInstanceOf[Class[Block]]) = wrapper.asInstanceOf[(Block, BlockPos) => TTarget]
	}

	def wrapEntity(obj: Entity): Option[TTarget] = {
		var cla = obj.getClass.asInstanceOf[Class[Entity]]
		while(classOf[Entity].isAssignableFrom(cla)) {
			if(entityWrappers.contains(cla)) {
				return Some(entityWrappers(cla)(obj))
			} else if(cla.getSuperclass == null) {
				return None
			} else {
				cla = cla.getSuperclass.asInstanceOf[Class[Entity]]
			}
		}
		return None
	}

	def wrapBlock(pos: BlockPos): Option[TTarget] = {
		val block = pos.block
		var cla = block.getClass.asInstanceOf[Class[Block]]
		while(classOf[Block].isAssignableFrom(cla)) {
			if(blockWrappers.contains(cla)) {
				return Some(blockWrappers(cla)(block, pos))
			} else if(cla.getSuperclass == null) {
				return None
			} else {
				cla = cla.getSuperclass.asInstanceOf[Class[Block]]
			}
		}
		return None
	}
}