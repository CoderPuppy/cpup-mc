package cpup.mc.lib.mod

import cpup.mc.lib.CPupMod
import cpw.mods.fml.common.Mod
import cpup.mc.lib.content.CPupContent

@Mod(modid = Ref.modID, modLanguage = "scala")
object CPupLib extends CPupMod[TRef] {
	def ref = Ref
	final val content = null
}