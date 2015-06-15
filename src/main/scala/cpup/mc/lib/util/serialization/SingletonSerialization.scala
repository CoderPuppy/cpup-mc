package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagByte

object SingletonSerialization {
	def register[T](singleton: T, _id: String) {
		SerializationRegistry.registerType(new Serialization[T, NBTTagByte] {
			override def id = _id
			override def cla = singleton.getClass.asInstanceOf[Class[T]]

			override def nbtClass = classOf[NBTTagByte]
			override def write(data: T) = new NBTTagByte(0)
			override def read(nbt: NBTTagByte) = Left(singleton)
		})
	}
}