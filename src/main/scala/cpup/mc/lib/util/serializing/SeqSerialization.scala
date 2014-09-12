package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagList
import cpup.mc.lib.mod.CPupLib
import cpup.mc.lib.util.NBTUtil

object SeqSerialization extends SerializableType[Seq[Any], NBTTagList] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:list"
	override def cla = classOf[Seq[Any]]

	override def nbtClass = classOf[NBTTagList]
	override def writeToNBT(data: Seq[Any]) = NBTUtil.writeList(data.map(Serialized(_)))
	override def readFromNBT(nbt: NBTTagList) = NBTUtil.readList(nbt).map(Serialized.un[Any])

	SerializationRegistry.registerType(this)
}