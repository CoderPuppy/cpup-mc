package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagString
import cpup.mc.lib.mod.CPupLib

object StringSerialization extends SerializableType[String, NBTTagString] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:string"
	override def cla = classOf[String]

	override def nbtClass = classOf[NBTTagString]
	override def writeToNBT(data: String) = new NBTTagString(data)
	override def readFromNBT(nbt: NBTTagString) = nbt.func_150285_a_

	SerializationRegistry.registerType(this)
}