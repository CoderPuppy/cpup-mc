package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagString
import cpup.mc.lib.mod.CPupLib

object SingletonSerialization extends SerializableType[NBTTagString] {
	def mod = CPupLib

	override def id = s"${mod.ref.modID}:singleton"
	override def cla = classOf[TEntry]

	private var _entries = Map[String, TEntry]()

	override def nbtClass = classOf[NBTTagString]
	override def readFromNBT(nbt: NBTTagString) = _entries(nbt.func_150285_a_)

	trait TEntry extends Serializable[NBTTagString] {
		def id: String

		override def typ = SingletonSerialization
		override def writeToNBT = new NBTTagString(id)
	}

	class Entry(final val id: String) extends TEntry

	def register(entry: TEntry) {
		_entries += ((entry.id, entry))
	}
}