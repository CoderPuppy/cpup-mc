package cpup.mc.lib.inventory

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import cpup.mc.lib.mod.CPupLib
import net.minecraft.util.ChatComponentTranslation

// TODO: abstract over IInventory
object EmptyInventory extends IInventory {
	def mod = CPupLib

	override def openInventory {}
	override def closeInventory {}
	override def markDirty {}

	override def isUseableByPlayer(player: EntityPlayer) = true

	override def isItemValidForSlot(slot: Int, stack: ItemStack) = false

	override def getInventoryStackLimit = 64

	override def hasCustomInventoryName = false
	override def getInventoryName = s"container.${mod.ref.modID}:empty.name"

	override def setInventorySlotContents(slot: Int, stack: ItemStack) {}

	override def getStackInSlotOnClosing(slot: Int) = null

	override def decrStackSize(slot: Int, amt: Int) = null

	override def getStackInSlot(slot: Int) = null

	override def getSizeInventory = 0
}