package cpup.mc.lib.util.serialization

import net.minecraft.nbt.NBTTagCompound

trait Serializable {
	def writeToNBT(nbt: NBTTagCompound)
	def readFromNBT(nbt: NBTTagCompound)
}
