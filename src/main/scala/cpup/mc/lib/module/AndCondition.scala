package cpup.mc.lib.module

case class AndCondition(conditions: TModuleCondition*) extends TModuleCondition {
	override def canLoad = conditions.foldLeft(CanLoad(true))((res, condition) => {
		condition.canLoad and res
	})
}