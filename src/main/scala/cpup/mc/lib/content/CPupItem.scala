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

	override def addInformation(stack: ItemStack, player: EntityPlayer, _lore: util.List[_], advanced: Boolean) {
		super.addInformation(stack, player, _lore, advanced)
		val lore = _lore.asInstanceOf[util.List[String]]
		try {
			addLore(stack, player, JavaConversions.asScalaBuffer(lore), advanced)
		} catch {
			case e: Exception =>
				if(advanced) {
					lore.add(e.getMessage)
					for(frame <- e.getStackTrace) {
						lore.add(s" ${frame.getClassName}.${frame.getMethodName} (${frame.getFileName}:${frame.getLineNumber})")
					}
				}
		}
	}
	def addLore(stack: ItemStack, player: EntityPlayer, lore: mutable.Buffer[String], advanced: Boolean) {}
}