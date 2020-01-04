package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagString
import cpup.mc.lib.mod.CPupLib

object StringSerialization extends Serialization[String, NBTTagString] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:string"
	override def cla = classOf[String]

	override def nbtClass = classOf[NBTTagString]
	override def write(data: String) = new NBTTagString(data)
	override def read(nbt: NBTTagString) = Left(nbt.getString)

	SerializationRegistry.registerType(this)
}