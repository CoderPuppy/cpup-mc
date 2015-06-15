package cpup.mc.lib.util

import net.minecraft.nbt.{NBTBase, NBTTagCompound, NBTTagList}
import scala.collection.mutable.ListBuffer

import net.minecraftforge.common.util.Constants.NBT

object NBTUtil {
	def readList(listNBT: NBTTagList) = {
		val list = new ListBuffer[NBTTagCompound]

		for(i <- 0 until listNBT.tagCount) {
			list += listNBT.getCompoundTagAt(i)
		}

		list.toList
	}

	def writeList(list: Seq[NBTBase]) = {
		val listNBT = new NBTTagList

		list.foreach(listNBT.appendTag)

		listNBT
	}

	def compound(nbt: NBTTagCompound, key: String) = {
		if(nbt.hasKey(key, NBT.TAG_COMPOUND)) {
			nbt.getCompoundTag(key)
		} else {
			val c = new NBTTagCompound
			nbt.setTag(key, c)
			c
		}
	}
}