package cpup.mc.lib.content

import net.minecraft.block.Block
import cpup.mc.lib.{CPupModRef, CPupMod}
import cpup.mc.lib.network.{BlockMessage, CPupMessage}
import net.minecraft.creativetab.CreativeTabs

trait CPupBlock[MOD <: CPupMod[_ <: CPupModRef]] extends Block {
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
		setBlockName(mod.ref.modID + ":" + newName)

		this
	}

	def getCreativeTabs = {
		var tabs = Array(getCreativeTabToDisplayOn)
		if(mod.content != null && mod.content.creativeTab != null) {
			tabs ++= Array(mod.content.creativeTab)
		}
		tabs
	}

	def handleMessage(msg: BlockMessage[MOD]) {
		mod.logger.warn("Unhandled message: " + msg.toString)
	}
}