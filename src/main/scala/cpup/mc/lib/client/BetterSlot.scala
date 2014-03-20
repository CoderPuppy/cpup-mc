package cpup.mc.lib.client

import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.ItemStack

class BetterSlot(inv: IInventory, slot: Int, x: Int, y: Int) extends Slot(inv, slot, x, y) {
	override def isItemValid(stack: ItemStack) = inv.isItemValidForSlot(getSlotIndex, stack)
}