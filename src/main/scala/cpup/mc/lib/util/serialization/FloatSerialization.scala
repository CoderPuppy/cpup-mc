package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagFloat
import cpup.mc.lib.mod.CPupLib

object FloatSerialization extends Serialization[Float, NBTTagFloat] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:float"
	override def cla = classOf[Float]

	override def nbtClass = classOf[NBTTagFloat]
	override def write(data: Float) = new NBTTagFloat(data)
	override def read(nbt: NBTTagFloat) = Left(nbt.getFloat)

	SerializationRegistry.registerType(this)
}