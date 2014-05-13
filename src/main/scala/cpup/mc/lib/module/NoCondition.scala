package cpup.mc.lib.module

case class NoCondition(msg: String = "No") extends TModuleCondition {
	override def canLoad = CanLoad(false, NoMessage(msg))
}

case class NoMessage(msg: String) extends CanLoad.Message {
	override def toString = msg
}