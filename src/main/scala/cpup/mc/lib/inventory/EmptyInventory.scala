package cpup.mc.lib.inventory

import cpup.mc.lib.mod.CPupLib
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.text.{TextComponentString, TextComponentTranslation}

// TODO: abstract over IInventory
object EmptyInventory extends IInventory {
	def mod = CPupLib

	override def openInventory(player: EntityPlayer) {}
	override def closeInventory(player: EntityPlayer) {}
	override def markDirty {}

	override def isUseableByPlayer(player: EntityPlayer) = true

	override def clear {}
	override def isItemValidForSlot(slot: Int, stack: ItemStack) = false
	override def getInventoryStackLimit = 64
	override def setInventorySlotContents(slot: Int, stack: ItemStack) {}
	override def decrStackSize(slot: Int, amt: Int) = null
	override def getStackInSlot(slot: Int) = null
	override def getSizeInventory = 0
	override def removeStackFromSlot(index: Int) = null

	//	override def getInventoryName = s"container.${mod.ref.modID}:empty.name"

	override def getFieldCount = 0
	override def getField(id: Int) = 0
	override def setField(id: Int, value: Int) {}

	override def hasCustomName = false

	override def getDisplayName = {
		if(this.hasCustomName)
			new TextComponentString(this.getName)
		else
			new TextComponentTranslation(this.getName, new Array[AnyRef](0))
	}
	override def getName = s"container.${mod.ref.modID}:empty.name"
}