package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagLong
import cpup.mc.lib.mod.CPupLib

object LongSerialization extends Serialization[Long, NBTTagLong] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:long"
	override def cla = classOf[Long]

	override def nbtClass = classOf[NBTTagLong]
	override def write(data: Long) = new NBTTagLong(data)
	override def read(nbt: NBTTagLong) = Left(nbt.getLong)

	SerializationRegistry.registerType(this)
}