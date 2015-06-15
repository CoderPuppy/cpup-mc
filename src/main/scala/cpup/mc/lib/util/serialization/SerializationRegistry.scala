package cpup.mc.lib.util.serialization

import cpup.mc.lib.util.ItemUtil
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTBase, NBTTagCompound}
import scala.collection.mutable
import cpup.mc.lib.mod.CPupLib
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.{TypeTag, runtimeMirror}

object SerializationRegistry {
	private def mod = CPupLib

	private val _types = new mutable.HashMap[String, Serialization[Any, NBTBase]]
	def types = _types.toMap
	private val _classes = new mutable.HashMap[Class[_], Serialization[Any, NBTBase]]
	def registerType(typ: Serialization[_ <: Any, _ <: NBTBase]) {
		_types(typ.id) = typ.asInstanceOf[Serialization[Any, NBTBase]]
		_classes(typ.cla) = typ.asInstanceOf[Serialization[Any, NBTBase]]
	}

	private def getClasses(_cla: Class[_]): List[Class[_]] = {
		var cla = _cla
		val classes = new ListBuffer[Class[_]]
		while(cla != null) {
			classes += cla
			classes ++= cla.getInterfaces.flatMap(getClasses)
			cla = cla.getSuperclass
		}
		classes.toList
	}

	def findSerialization(cla: Class[_]): Serialization[Any, NBTBase] = getClasses(cla).find(_classes.contains).map(_classes(_)).getOrElse(null)

	def read[T](nbt: NBTTagCompound)(implicit typeTag: TypeTag[T]): Either[T, SerializationError] = {
		val id = nbt.getString("id")
		val data = nbt.getTag("data")
		if(!_types.contains(id)) {
			if(id != "") mod.logger.info("no such type: {} {}", id: Any, _types)
			return Right(SerializationError('unknown_type -> List(id)))
		}
		val typ = _types(id)
		if(!typ.nbtClass.isAssignableFrom(data.getClass)) {
			mod.logger.info("{}: NBT {} isn't assignable from {} ({})", id, typ.nbtClass, data, data.getClass)
			return Right(SerializationError('incorrect_nbt_type -> List(typ.nbtClass, data.getClass)))
		}
		typ.asInstanceOf[Serialization[Any, NBTBase]].read(data) match {
			case Left(res) => {
				val runtimeClass = runtimeMirror(getClass.getClassLoader).runtimeClass(typeTag.tpe)
				if(!runtimeClass.isAssignableFrom(res.getClass)) {
					mod.logger.info("{}: data {} isn't assignable from {}", id, runtimeClass, res.getClass)
					return Right(SerializationError('bad_return_class -> List(runtimeClass, res.getClass)))
				}
				Left(res.asInstanceOf[T])
			}

			case Right(err) =>
				Right(err)
		}
	}
	def write(data: Any): NBTTagCompound = findSerialization(data.getClass) match {
		case typ: Serialization[Any, NBTBase] =>
			val res = new NBTTagCompound
			res.setString("id", typ.id)
			res.setTag("data", typ.write(data))
			res
		case _ => throw new ClassCastException(s"it's not serializable: ${data.toString}")
	}

	// Load some serializations
	MapSerialization
	SeqSerialization

	StringSerialization

	DoubleSerialization
	ShortSerialization
	FloatSerialization
	LongSerialization
	ByteSerialization
	IntSerialization
}