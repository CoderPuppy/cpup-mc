package cpup.mc.lib.util.serializing

import cpup.mc.lib.util.ItemUtil
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import scala.reflect.runtime.universe.TypeTag

object Serialized {
	def apply(data: Any) = SerializationRegistry.writeToNBT(data)
	def un[T](nbt: NBTTagCompound)(implicit typeTag: TypeTag[T]) = SerializationRegistry.readFromNBT[T](nbt)
	def un[T](stack: ItemStack)(implicit typeTag: TypeTag[T]): Option[T] = un[T](ItemUtil.compound(stack))
}