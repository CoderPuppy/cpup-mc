package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagDouble
import cpup.mc.lib.mod.CPupLib

object DoubleSerialization extends SerializableType[Double, NBTTagDouble] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:double"
	override def cla = classOf[Double]

	override def nbtClass = classOf[NBTTagDouble]
	override def writeToNBT(data: Double) = new NBTTagDouble(data)
	override def readFromNBT(nbt: NBTTagDouble) = nbt.func_150286_g

	SerializationRegistry.registerType(this)
}