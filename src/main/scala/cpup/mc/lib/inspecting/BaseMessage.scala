package cpup.mc.lib.inspecting

import cpup.mc.lib.mod.CPupLib
import cpup.mc.lib.network.CPupMessage

trait BaseMessage extends CPupMessage[CPupLib.type] {
	def mod = CPupLib
}
