package cpup.mc.lib.module

import cpw.mods.fml.common.Loader

case class ModLoadedCondition(modid: String) extends TModuleCondition {
	override def canLoad = if(Loader.isModLoaded(modid)) {
		CanLoad(true, ModLoadedMessage(modid, true))
	} else {
		CanLoad(false, ModLoadedMessage(modid, false))
	}
}

case class ModLoadedMessage(modid: String, loaded: Boolean) extends CanLoad.Message {
	override def toString = s"$modid ${if(loaded) { "is" } else { "isn't" }} loaded"
}