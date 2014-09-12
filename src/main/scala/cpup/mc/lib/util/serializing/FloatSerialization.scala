package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagFloat
import cpup.mc.lib.mod.CPupLib

object FloatSerialization extends SerializableType[Float, NBTTagFloat] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:float"
	override def cla = classOf[Float]

	override def nbtClass = classOf[NBTTagFloat]
	override def writeToNBT(data: Float) = new NBTTagFloat(data)
	override def readFromNBT(nbt: NBTTagFloat) = nbt.func_150288_h

	SerializationRegistry.registerType(this)
}