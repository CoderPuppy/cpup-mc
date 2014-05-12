package cpup.mc.lib.targeting

import scala.collection.mutable

object TargetingRegistry {
	private val wrappers = new mutable.HashMap[Class[Any], Any => TTarget]()

	def register[T](wrapper: T => TTarget)(implicit gCla: Class[T]) {
		wrappers(gCla.asInstanceOf[Class[Any]]) = wrapper.asInstanceOf[Any => TTarget]
	}

	def wrap[T](obj: T): Option[TTarget] = {
		var cla: Class[Any] = obj.getClass.asInstanceOf[Class[Any]]
		while(cla.getSuperclass != null) {
			if(wrappers.contains(cla)) {
				return Some(wrappers(cla)(obj))
			} else {
				cla = cla.getSuperclass
			}
		}
		None
	}
}