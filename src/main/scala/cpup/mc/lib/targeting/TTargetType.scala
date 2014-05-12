package cpup.mc.lib.targeting

import net.minecraft.nbt.NBTTagCompound

trait TTargetType {
	def name: String
	def targetClass: Class[_ <: TTarget]
	def readFromNBT(nbt: NBTTagCompound): Option[TTarget]
}