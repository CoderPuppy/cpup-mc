package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTTagCompound
import scala.reflect.runtime.universe.TypeTag

object Serialized {
	def apply(data: Any) = SerializationRegistry.writeToNBT(data)
	def un[T](nbt: NBTTagCompound)(implicit typeTag: TypeTag[T]) = SerializationRegistry.readFromNBT[T](nbt)
}