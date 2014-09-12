package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagCompound
import cpup.mc.lib.mod.CPupLib
import scala.collection.{JavaConversions, mutable}

object MapSerialization extends SerializableType[Map[String, Any], NBTTagCompound] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:map"
	override def cla = classOf[Map[String, Any]]

	override def nbtClass = classOf[NBTTagCompound]
	override def writeToNBT(data: Map[String, Any]) = {
		val nbt = new NBTTagCompound
		for((k, v) <- data) {
			nbt.setTag(k, Serialized(v))
		}
		nbt
	}
	override def readFromNBT(nbt: NBTTagCompound) = {
		val map = new mutable.HashMap[String, Any]
		JavaConversions.asScalaSet(nbt.func_150296_c).asInstanceOf[Set[String]].foreach((key) => {
			map(key) = Serialized.un(nbt.getCompoundTag(key))
		})
		map.toMap
	}
}