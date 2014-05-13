package cpup.mc.lib.module

case class CanLoad(toBoolean: Boolean, messages: CanLoad.Message*) {
	def withResult(bool: Boolean) = CanLoad(bool, messages: _*)
	def addMessages(msgs: CanLoad.Message*) = CanLoad(toBoolean, messages ++ msgs: _*)
	def withMessages(msgs: CanLoad.Message*) = CanLoad(toBoolean, msgs: _*)
	def and(other: CanLoad) = withResult(toBoolean && other.toBoolean).addMessages(other.messages: _*)
	def or(other: CanLoad) = withResult(toBoolean || other.toBoolean).addMessages(other.messages: _*)
	def unary_! = withResult(!toBoolean).withMessages(messages.map(NotMessage): _*)
}

object CanLoad {
	trait Message {}
}