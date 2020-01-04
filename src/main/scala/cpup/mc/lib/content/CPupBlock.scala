package cpup.mc.lib.content

import net.minecraft.block.Block
import cpup.mc.lib.{CPupModHolder, CPupModRef, CPupMod}

trait CPupBlock[MOD <: CPupMod[_ <: CPupModRef]] extends Block { self: CPupModHolder[MOD] =>
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
		setRegistryName(mod.ref.modID, newName)

		this
	}

	def getCreativeTabs = {
		var tabs = Array(getCreativeTabToDisplayOn)
		if(mod.content != null && mod.content.creativeTab != null) {
			tabs ++= Array(mod.content.creativeTab)
		}
		tabs
	}
}
