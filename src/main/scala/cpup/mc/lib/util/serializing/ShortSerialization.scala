package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagShort
import cpup.mc.lib.mod.CPupLib

object ShortSerialization extends SerializableType[Short, NBTTagShort] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:short"
	override def cla = classOf[Short]

	override def nbtClass = classOf[NBTTagShort]
	override def writeToNBT(data: Short) = new NBTTagShort(data)
	override def readFromNBT(nbt: NBTTagShort) = nbt.func_150289_e

	SerializationRegistry.registerType(this)
}