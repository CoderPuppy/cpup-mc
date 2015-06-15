package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagList
import cpup.mc.lib.mod.CPupLib
import cpup.mc.lib.util.NBTUtil

object SeqSerialization extends Serialization[Seq[Any], NBTTagList] {
	def mod = CPupLib
	override def id = s"${mod.ref.modID}:seq"
	override def cla = classOf[Seq[Any]]

	override def nbtClass = classOf[NBTTagList]
	override def write(data: Seq[Any]) = NBTUtil.writeList(data.map(Serialized(_)))
	override def read(nbt: NBTTagList) = {
		val (l, e) = NBTUtil.readList(nbt).map(Serialized.un[Any]).foldLeft((List[Any](), SerializationError()))((a, v) => {
			val (l, e) = a
			v match {
				case Left(v) => (l ++ List(v), e)
				case Right(err) => (l, e + err)
			}
		})
		if(e.errors.isEmpty)
			Left(l)
		else
			Right(e)
	}

	SerializationRegistry.registerType(this)
}