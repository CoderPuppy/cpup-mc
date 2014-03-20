package cpup.mc.lib.content

import net.minecraft.item.Item
import cpup.mc.lib.{CPupModRef, CPupMod}
import net.minecraft.creativetab.CreativeTabs

trait CPupItem[MOD <: CPupMod[_ <: CPupModRef, _]] extends Item {
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
}