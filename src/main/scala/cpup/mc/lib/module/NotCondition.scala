package cpup.mc.lib.module

case class NotCondition(condition: TModuleCondition) extends TModuleCondition {
	def canLoad = !condition.canLoad
}

case class NotMessage(msg: CanLoad.Message) extends CanLoad.Message {
	override def toString = s"Not ${'"'}$msg${'"'}"
}