package cpup.mc.lib.mod

import cpup.mc.lib.CPupMod
import cpup.mc.lib.intermod.baubles.BaublesI
import cpw.mods.fml.common.Mod

@Mod(modid = Ref.modID, modLanguage = "scala")
object CPupLib extends CPupMod[TRef] {
	def ref = Ref

	loadModule(BaublesI)
}