package cpup.mc.lib.mod

import java.lang.annotation.Annotation

import cpup.lib.module._
import cpup.mc.lib.CPupMod
import cpup.mc.lib.intermod.baubles.Baubles
import cpup.mc.lib.module.ModLoaded
import cpw.mods.fml.common.{Loader, Mod}
import org.apache.logging.log4j.LogManager

@ModuleID(id = Ref.modID)
@Mod(modid = Ref.modID, modLanguage = "scala")
object CPupLib extends CPupMod[TRef] {
	def ref = Ref

	ModuleLoader.canLoadHandlers(classOf[ModLoaded]) = ((a: ModLoaded) => {
		if(Loader.isModLoaded(a.modid)) {
			List()
		} else {
			List(s"${a.modid} is not loaded")
		}
	}).asInstanceOf[(Annotation) => List[String]]

	final val baubles = ModuleLoader.forceLoad[Baubles.API](this)(classOf[Baubles.API])
}
