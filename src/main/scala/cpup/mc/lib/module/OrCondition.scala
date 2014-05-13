package cpup.mc.lib.module

case class OrCondition(conditions: TModuleCondition*) extends TModuleCondition {
	override def canLoad = conditions.foldLeft(CanLoad(true))((res, condition) => {
		res or condition.canLoad
	})
}