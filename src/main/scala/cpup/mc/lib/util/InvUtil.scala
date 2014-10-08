package cpup.mc.lib.util

import net.minecraft.inventory.IInventory

object InvUtil {
	def getItems(inv: IInventory) = (0 to inv.getSizeInventory).map(inv.getStackInSlot).toList
}