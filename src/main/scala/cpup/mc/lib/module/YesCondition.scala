package cpup.mc.lib.module

case class YesCondition(msg: String = "Yes") extends TModuleCondition {
	override def canLoad = CanLoad(true, NoMessage(msg))
}

case class YesMessage(msg: String) extends CanLoad.Message {
	override def toString = msg
}