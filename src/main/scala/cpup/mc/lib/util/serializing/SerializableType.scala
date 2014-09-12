package cpup.mc.lib.util.serializing

import net.minecraft.nbt.NBTBase

trait SerializableType[T, NBT <: NBTBase] {
	def id: String
	def cla: Class[_ <: T]

	def nbtClass: Class[_ <: NBT]
	def writeToNBT(data: T): NBT
	def readFromNBT(nbt: NBT): T
}