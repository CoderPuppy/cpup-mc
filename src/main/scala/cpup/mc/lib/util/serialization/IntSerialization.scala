package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagInt
import cpup.mc.lib.mod.CPupLib

object IntSerialization extends Serialization[Int, NBTTagInt] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:int"
	override def cla = classOf[Int]

	override def nbtClass = classOf[NBTTagInt]
	override def write(data: Int) = new NBTTagInt(data)
	override def read(nbt: NBTTagInt) = Left(nbt.func_150287_d)

	SerializationRegistry.registerType(this)
}