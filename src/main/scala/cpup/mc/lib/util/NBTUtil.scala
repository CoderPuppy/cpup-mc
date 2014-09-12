package cpup.mc.lib.util

import net.minecraft.nbt.{NBTBase, NBTTagCompound, NBTTagList}
import scala.collection.mutable.ListBuffer

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
}