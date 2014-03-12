package cpup.mc.lib.content

import net.minecraft.block.Block
import cpup.mc.lib.{CPupModRef, CPupMod}

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
}