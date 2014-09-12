package cpup.mc.lib.util.serializing

import net.minecraft.nbt.{NBTBase, NBTTagCompound}

object SerializationRegistry {
	private var _types = Map[String, SerializableType[_ <: NBTBase]]()
	def types = _types
	def registerType(typ: SerializableType[_ <: NBTBase]) {
		_types += ((typ.id, typ))
	}
	def readFromNBT[T](nbt: NBTTagCompound)(implicit manifest: Manifest[T]): T = {
		val id = nbt.getString("id")
		val data = nbt.getTag("data")
		if(!_types.contains(id)) return null.asInstanceOf[T]
		val typ = _types(id)
		if(!typ.nbtClass.isAssignableFrom(data.getClass)) return null.asInstanceOf[T]
		val res = typ.asInstanceOf[SerializableType[NBTBase]].readFromNBT(data)
		if(!manifest.runtimeClass.isAssignableFrom(res.getClass)) return null.asInstanceOf[T]
		res.asInstanceOf[T]
	}
	def writeToNBT(_data: Any): NBTTagCompound = _data match {
		case data: Serializable[NBTBase] =>
			val id = data.typ.id
			assert(_types.contains(id), s"$id isn't registered")
			assert(_types(id) == data.typ, s"$id is registered to ${_types(id)}, not ${data.typ}")
			val res = new NBTTagCompound
			res.setString("id", data.typ.id)
			res.setTag("data", data.writeToNBT)
			res
		case _ => throw new ClassCastException("it's not serializable")
	}
}