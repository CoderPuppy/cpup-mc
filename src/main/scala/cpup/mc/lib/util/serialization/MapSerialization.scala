package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagCompound
import cpup.mc.lib.mod.CPupLib
import scala.collection.{JavaConversions, mutable}

object MapSerialization extends Serialization[Map[String, Any], NBTTagCompound] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:map"
	override def cla = classOf[Map[String, Any]]

	override def nbtClass = classOf[NBTTagCompound]
	override def write(data: Map[String, Any]) = {
		val nbt = new NBTTagCompound
		for((k, v) <- data) {
			nbt.setTag(k, Serialized(v))
		}
		nbt
	}
	override def read(nbt: NBTTagCompound) = {
		val map = new mutable.HashMap[String, Any]
		var error = SerializationError()
		for(key <- JavaConversions.asScalaSet(nbt.getKeySet)) {
			Serialized.un[Any](nbt.getCompoundTag(key)) match {
				case Left(v) => map(key) = v
				case Right(e) => error += e
			}
		}
		if(error.errors.isEmpty)
			Left(map.toMap)
		else
			Right(error)
	}
}