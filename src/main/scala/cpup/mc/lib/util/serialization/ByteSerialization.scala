package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagByte
import cpup.mc.lib.mod.CPupLib

object ByteSerialization extends Serialization[Byte, NBTTagByte] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:byte"
	override def cla = classOf[Byte]

	override def nbtClass = classOf[NBTTagByte]
	override def write(data: Byte) = new NBTTagByte(data)
	override def read(nbt: NBTTagByte) = Left(nbt.func_150290_f)

	SerializationRegistry.registerType(this)
}