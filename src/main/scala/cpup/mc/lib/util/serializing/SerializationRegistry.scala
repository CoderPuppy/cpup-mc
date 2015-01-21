package cpup.mc.lib.util.serializing

import cpup.mc.lib.util.ItemUtil
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTBase, NBTTagCompound}
import scala.collection.mutable
import cpup.mc.lib.mod.CPupLib
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe.{TypeTag, runtimeMirror}

object SerializationRegistry {
	private def mod = CPupLib

	private var _types = Map[String, SerializableType[Any, NBTBase]]()
	def types = _types
	private val _classes = new mutable.HashMap[Class[_], SerializableType[Any, NBTBase]]
	def registerType(typ: SerializableType[_ <: Any, _ <: NBTBase]) {
		_types += ((typ.id, typ.asInstanceOf[SerializableType[Any, NBTBase]]))
		_classes(typ.cla) = typ.asInstanceOf[SerializableType[Any, NBTBase]]
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

	def findType(cla: Class[_]): SerializableType[Any, NBTBase] = getClasses(cla).find(_classes.contains).map(_classes(_)).getOrElse(null)

	def readFromNBT[T](nbt: NBTTagCompound)(implicit typeTag: TypeTag[T]): Option[T] = {
		val id = nbt.getString("id")
		val data = nbt.getTag("data")
		if(!_types.contains(id)) {
			if(id != "") mod.logger.info("no such type: {} {}", id: Any, _types)
			return None
		}
		val typ = _types(id)
		if(!typ.nbtClass.isAssignableFrom(data.getClass)) {
			mod.logger.info("{}: NBT {} isn't assignable from {} ({})", id, typ.nbtClass, data, data.getClass)
			return None
		}
		val res = typ.asInstanceOf[SerializableType[Any, NBTBase]].readFromNBT(data)
		if(res == null) {
			mod.logger.info("{} had an issue deserializing {}", id, data: Any)
			return None
		}
		val runtimeClass = runtimeMirror(getClass.getClassLoader).runtimeClass(typeTag.tpe)
		if(!runtimeClass.isAssignableFrom(res.getClass)) {
			mod.logger.info("{}: data {} isn't assignable from {}", id, runtimeClass, res.getClass)
			return None
		}
		Some(res.asInstanceOf[T])
	}
	def writeToNBT(data: Any): NBTTagCompound = findType(data.getClass) match {
		case typ: SerializableType[Any, NBTBase] =>
			val res = new NBTTagCompound
			res.setString("id", typ.id)
			res.setTag("data", typ.writeToNBT(data))
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