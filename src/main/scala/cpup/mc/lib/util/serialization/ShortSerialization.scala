package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagShort
import cpup.mc.lib.mod.CPupLib

object ShortSerialization extends Serialization[Short, NBTTagShort] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:short"
	override def cla = classOf[Short]

	override def nbtClass = classOf[NBTTagShort]
	override def write(data: Short) = new NBTTagShort(data)
	override def read(nbt: NBTTagShort) = Left(nbt.getShort)

	SerializationRegistry.registerType(this)
}