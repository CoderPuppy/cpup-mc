package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagLong
import cpup.mc.lib.mod.CPupLib

object LongSerialization extends SerializableType[Long, NBTTagLong] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:long"
	override def cla = classOf[Long]

	override def nbtClass = classOf[NBTTagLong]
	override def writeToNBT(data: Long) = new NBTTagLong(data)
	override def readFromNBT(nbt: NBTTagLong) = nbt.func_150287_d

	SerializationRegistry.registerType(this)
}