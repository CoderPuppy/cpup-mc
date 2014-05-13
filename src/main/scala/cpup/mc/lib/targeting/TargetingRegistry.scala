package cpup.mc.lib.targeting

import scala.collection.mutable

object TargetingRegistry {
	private val wrappers = new mutable.HashMap[Class[Any], Any => TTarget]()

	def register[T](wrapper: T => TTarget)(implicit gCla: Class[T]) {
		wrappers(gCla.asInstanceOf[Class[Any]]) = wrapper.asInstanceOf[Any => TTarget]
	}

	def wrap(obj: Any): Option[TTarget] = {
		var cla = obj.getClass.asInstanceOf[Class[Any]]
		while(true) {
			if(wrappers.contains(cla)) {
				return Some(wrappers(cla)(obj))
			} else if(cla.getSuperclass == null) {
				return None
			} else {
				cla = cla.getSuperclass
			}
		}
		return None
	}
}