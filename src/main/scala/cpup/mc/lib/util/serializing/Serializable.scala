package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTBase

trait Serializable[NBT <: NBTBase] {
	def typ: SerializableType[_ >: NBT]

	def writeToNBT: NBT
}