package cpup.mc.lib.module

import cpw.mods.fml.common.Loader

case class ModLoadedCondition(modid: String) extends TModuleCondition {
	override def canLoad = Loader.isModLoaded(modid)
}