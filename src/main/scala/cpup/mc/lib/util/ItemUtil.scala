package cpup.mc.lib.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

object ItemUtil {
	def withNBT(stack: ItemStack, nbt: NBTTagCompound): ItemStack = {
		val newStack = stack.copy
		newStack.setTagCompound(nbt)
		newStack
	}

	def compound(stack: ItemStack) = {
		var compound = stack.getTagCompound

		if(compound == null) {
			compound = new NBTTagCompound
			stack.setTagCompound(compound)
		}

		compound
	}
}