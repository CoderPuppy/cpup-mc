package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTBase

trait Serialization[T, NBT <: NBTBase] {
	def id: String
	def cla: Class[T]

	def nbtClass: Class[NBT]
	def write(data: T): NBT
	def read(nbt: NBT): Either[T, SerializationError]
}

object Serialization {
	def validate[V, T](label: String, get: V => T, expected: T)(v: V): Either[V, SerializationError] = {
		val t = get(v)
		if(t == expected)
			Left(v)
		else
			Right(SerializationError(Symbol(s"bad_${label}") -> List(expected, t)))
	}

	def fromOption[V](v: Option[V]): Either[V, SerializationError] = v.map(Left(_)).getOrElse(Right(SerializationError('none -> List())))
}