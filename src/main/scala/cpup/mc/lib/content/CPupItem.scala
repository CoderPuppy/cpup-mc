package cpup.mc.lib.content

import net.minecraft.item.{ItemStack, Item}
import cpup.mc.lib.{CPupModRef, CPupMod}
import net.minecraft.entity.player.EntityPlayer
import java.util
import scala.collection.{JavaConversions, mutable}

trait CPupItem[MOD <: CPupMod[_ <: CPupModRef]] extends Item {
	def mod: MOD

	protected var _name: String = null
	def name = _name
	def name_=(newName: String) = {
		setName(newName)
		name
	}
	def setName(newName: String) = {
		if(newName == null) {
			throw new RuntimeException("Cannot set name to null")
		}

		_name = newName
		setUnlocalizedName(mod.ref.modID + ":" + newName)
		setTextureName(mod.ref.modID + ":" + newName)

		this
	}

	override def getCreativeTabs = {
		var tabs = super.getCreativeTabs

		if(mod.content != null && mod.content.creativeTab != null) {
			tabs ++= Array(mod.content.creativeTab)
		}

		tabs
	}

	override def addInformation(stack: ItemStack, player: EntityPlayer, lore: util.List[_], advanced: Boolean) {
		super.addInformation(stack, player, lore, advanced)
		addLore(stack, player, JavaConversions.asScalaBuffer(lore.asInstanceOf[util.List[String]]), advanced)
	}
	def addLore(stack: ItemStack, player: EntityPlayer, lore: mutable.Buffer[String], advanced: Boolean) {}
}