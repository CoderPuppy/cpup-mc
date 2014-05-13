package cpup.mc.lib.module

class FunctionCondition(val name: String, fn: => Boolean) extends TModuleCondition {
	val canLoad = if(fn) {
		CanLoad(true, FunctionMessage(name, true))
	} else {
		CanLoad(false, FunctionMessage(name, false))
	}
}

object FunctionCondition {
	def apply(name: String, fn: => Boolean) = new FunctionCondition(name, fn)
}

case class FunctionMessage(name: String, canLoad: Boolean) extends CanLoad.Message {
	override def toString = s"$name ${if(canLoad) "passed" else "failed"}"
}