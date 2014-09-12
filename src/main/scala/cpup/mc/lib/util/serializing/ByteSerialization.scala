package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagByte
import cpup.mc.lib.mod.CPupLib

object ByteSerialization extends SerializableType[Byte, NBTTagByte] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:byte"
	override def cla = classOf[Byte]

	override def nbtClass = classOf[NBTTagByte]
	override def writeToNBT(data: Byte) = new NBTTagByte(data)
	override def readFromNBT(nbt: NBTTagByte) = nbt.func_150290_f

	SerializationRegistry.registerType(this)
}