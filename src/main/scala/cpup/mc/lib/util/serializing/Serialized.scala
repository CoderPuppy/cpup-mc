package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagCompound

object Serialized {
	def apply(data: Any) = SerializationRegistry.writeToNBT(data)
	def unapply[T](nbt: NBTTagCompound)(implicit manifest: Manifest[T]) = Option(SerializationRegistry.readFromNBT[T](nbt))
	def un[T](nbt: NBTTagCompound)(implicit manifest: Manifest[T]) = SerializationRegistry.readFromNBT[T](nbt)
}