package cpup.mc.lib.util.serialization

case class SerializationError(errors: (Symbol, List[Any])*) {
	def +(other: SerializationError) = SerializationError(errors ++ other.errors: _*)
	override def toString = errors.map(e => {
		s"${e._1}(${e._2.mkString(", ")}})"
	}).mkString(", ")
}
