package cpup.mc.lib.util.serializing

import net.minecraft.nbt.{NBTTagCompound, NBTBase}

trait Serializable[NBT <: NBTBase] {
	def typ: SerializableType[_ >: NBT]

	def writeToNBT: NBT
}

object Serializable {
	def apply(data: Any) = SerializationRegistry.writeToNBT(data)
	def unapply[T](nbt: NBTTagCompound)(implicit manifest: Manifest[T]) = Option(SerializationRegistry.readFromNBT[T](nbt))
}