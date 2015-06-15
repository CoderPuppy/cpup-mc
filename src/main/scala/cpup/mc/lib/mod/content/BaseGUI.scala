package cpup.mc.lib.mod.content

import cpup.mc.lib.CPupModHolder
import cpup.mc.lib.client.CPupGUI
import cpup.mc.lib.mod.CPupLib

trait BaseGUI extends CPupGUI[CPupLib.type] with CPupModHolder[CPupLib.type] {
	 def mod = CPupLib
 }
