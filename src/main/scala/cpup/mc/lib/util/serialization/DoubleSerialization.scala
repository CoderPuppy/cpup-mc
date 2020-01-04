package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagDouble
import cpup.mc.lib.mod.CPupLib

object DoubleSerialization extends Serialization[Double, NBTTagDouble] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:double"
	override def cla = classOf[Double]

	override def nbtClass = classOf[NBTTagDouble]
	override def write(data: Double) = new NBTTagDouble(data)
	override def read(nbt: NBTTagDouble) = Left(nbt.getDouble)

	SerializationRegistry.registerType(this)
}