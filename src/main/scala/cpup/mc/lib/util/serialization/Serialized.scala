package cpup.mc.lib.util.serialization

import cpup.mc.lib.util.ItemUtil
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import scala.reflect.runtime.universe.TypeTag

object Serialized {
	def apply(data: Any) = SerializationRegistry.write(data)
	def un[T](nbt: NBTTagCompound)(implicit typeTag: TypeTag[T]) = SerializationRegistry.read[T](nbt)
	def un[T](stack: ItemStack)(implicit typeTag: TypeTag[T]): Either[T, SerializationError] = un[T](ItemUtil.compound(stack))
}