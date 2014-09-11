package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTBase

trait SerializableType[NBT <: NBTBase] {
	def id: String
	def cla: Class[_ <: Serializable[_ <: NBT]]
	def nbtClass: Class[_ <: NBT]

	def readFromNBT(nbt: NBT): Serializable[NBT]
}