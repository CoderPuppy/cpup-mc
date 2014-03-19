package cpup.mc.lib.content

import net.minecraft.block.Block
import cpup.mc.lib.{CPupModRef, CPupMod}
import cpup.mc.lib.network.CPupMessage

trait CPupBlock[MOD <: CPupMod[_ <: CPupModRef, _ <: CPupMessage]] extends Block {
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