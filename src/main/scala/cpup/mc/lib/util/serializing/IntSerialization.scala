package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagInt
import cpup.mc.lib.mod.CPupLib

object IntSerialization extends SerializableType[Int, NBTTagInt] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:int"
	override def cla = classOf[Int]

	override def nbtClass = classOf[NBTTagInt]
	override def writeToNBT(data: Int) = new NBTTagInt(data)
	override def readFromNBT(nbt: NBTTagInt) = nbt.func_150287_d

	SerializationRegistry.registerType(this)
}