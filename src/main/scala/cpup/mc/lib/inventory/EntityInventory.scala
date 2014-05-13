package cpup.mc.lib.inventory

import net.minecraft.entity.{EntityLiving, Entity}
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer

class EntityInventory(val entity: Entity) extends IInventory {
	val inv = if(entity.getLastActiveItems == null) {
		Array.fill[ItemStack](5) { null }
	} else {
		entity.getLastActiveItems
	}

	override def isItemValidForSlot(slot: Int, stack: ItemStack) = EntityLiving.getArmorPosition(stack) == slot

	override def openInventory {}
	override def closeInventory {}

	override def isUseableByPlayer(player: EntityPlayer) = true

	override def markDirty {}

	override def getInventoryStackLimit = 64

	override def hasCustomInventoryName = entity match {
		case e: EntityLiving => e.hasCustomNameTag
		case _ => false
	}
	override def getInventoryName = entity match {
		case e: EntityLiving if e.hasCustomNameTag =>
			e.getCustomNameTag
		case e =>
			e.getCommandSenderName
	}

	override def setInventorySlotContents(slot: Int, stack: ItemStack) {
		inv(slot) = stack
	}

	override def getStackInSlotOnClosing(slot: Int) = {
		val stack = inv(slot)
		inv(slot) = null
		stack
	}

	override def decrStackSize(slot: Int, amt: Int) = {
		if(inv(slot) != null) {
			var stack: ItemStack = null
			if(inv(slot).stackSize <= amt) {
				stack = inv(slot)
				inv(slot) = null
				markDirty
				stack
			}
			else {
				stack = inv(slot).splitStack(amt)
				if(inv(slot).stackSize == 0) {
					inv(slot) = null
				}
				markDirty
				stack
			}
		} else {
			null
		}

	}

	override def getStackInSlot(slot: Int) = inv(slot)

	override def getSizeInventory = inv.size
}