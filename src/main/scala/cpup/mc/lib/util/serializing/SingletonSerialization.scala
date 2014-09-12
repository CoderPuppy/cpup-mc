package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagByte

object SingletonSerialization {
	def register[T](singleton: T, _id: String) {
		SerializationRegistry.registerType(new SerializableType[T, NBTTagByte] {
			override def id = _id
			override def cla = singleton.getClass

			override def nbtClass = classOf[NBTTagByte]
			override def writeToNBT(data: T) = new NBTTagByte(0)
			override def readFromNBT(nbt: NBTTagByte) = singleton
		})
	}
}