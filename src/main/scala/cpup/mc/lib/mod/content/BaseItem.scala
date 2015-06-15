package cpup.mc.lib.mod.content

import cpup.mc.lib.CPupModHolder
import cpup.mc.lib.content.CPupItem
import cpup.mc.lib.mod.CPupLib

trait BaseItem extends CPupItem with CPupModHolder[CPupLib.type] {
	def mod = CPupLib
}
