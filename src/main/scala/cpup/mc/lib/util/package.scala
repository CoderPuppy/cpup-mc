package cpup.mc.lib.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

object ItemUtil {
	def compound(stack: ItemStack) = {
		var compound = stack.getTagCompound

		if(compound == null) {
			compound = new NBTTagCompound
			stack.setTagCompound(compound)
		}

		compound
	}
}