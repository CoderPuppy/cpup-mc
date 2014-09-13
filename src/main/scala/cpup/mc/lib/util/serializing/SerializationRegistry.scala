package cpup.mc.lib.util.serializing

import net.minecraft.nbt.{NBTBase, NBTTagCompound}
import scala.collection.mutable
import cpup.mc.lib.mod.CPupLib

object SerializationRegistry {
	private def mod = CPupLib

	private var _types = Map[String, SerializableType[Any, NBTBase]]()
	def types = _types
	private val _classes = new mutable.HashMap[Class[_], SerializableType[Any, NBTBase]]
	def registerType(typ: SerializableType[_ <: Any, _ <: NBTBase]) {
		_types += ((typ.id, typ.asInstanceOf[SerializableType[Any, NBTBase]]))
		_classes(typ.cla) = typ.asInstanceOf[SerializableType[Any, NBTBase]]
	}

	def findType(_cla: Class[_]): SerializableType[Any, NBTBase] = {
		var cla = _cla
		while(cla != null) {
			if(_classes.contains(cla))
				return _classes(cla)
			cla = cla.getSuperclass
		}
		null
	}

	def readFromNBT[T](nbt: NBTTagCompound)(implicit manifest: Manifest[T]): T = {
		val id = nbt.getString("id")
		val data = nbt.getTag("data")
		if(!_types.contains(id)) {
			mod.logger.info("no such type: {} {}", id: Any, _types)
			return null.asInstanceOf[T]
		}
		val typ = _types(id)
		if(!typ.nbtClass.isAssignableFrom(data.getClass)) {
			mod.logger.info("{}: NBT {} isn't assignable from {}", id, typ.nbtClass, data.getClass)
			return null.asInstanceOf[T]
		}
		val res = typ.asInstanceOf[SerializableType[Any, NBTBase]].readFromNBT(data)
		if(res == null) {
			mod.logger.info("{} had an issue deserializing {}", id, data: Any)
			return null.asInstanceOf[T]
		}
		if(!manifest.runtimeClass.isAssignableFrom(res.getClass)) {
			mod.logger.info("{}: data {} isn't assignable from {}", id, manifest.runtimeClass, res.getClass)
			return null.asInstanceOf[T]
		}
		res.asInstanceOf[T]
	}
	def writeToNBT(data: Any): NBTTagCompound = findType(data.getClass) match {
		case typ: SerializableType[Any, NBTBase] =>
			val res = new NBTTagCompound
			res.setString("id", typ.id)
			res.setTag("data", typ.writeToNBT(data))
			res
		case _ => throw new ClassCastException("it's not serializable")
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