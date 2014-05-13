package cpup.mc.lib.module

import cpup.mc.lib.{CPupModRef, CPupMod, ModLifecycleHandler}
import cpw.mods.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}

class Module[MOD <: ModLifecycleHandler](val mod: CPupMod[_ <: CPupModRef], val condition: TModuleCondition, real: => MOD, dummy: => MOD) extends ModLifecycleHandler {
	var name = getClass.getSimpleName.replaceFirst("\\$$", "")

	val canLoad = condition.canLoad
	mod.logger.info(s"[$name] Loading")
	for(msg <- canLoad.messages) {
		mod.logger.info(s"[$name] -- $msg")
	}
	val impl = if(canLoad.toBoolean) {
		try {
			mod.logger.info(s"[$name] Loading real implementation")
			real
		} catch {
			case e: Exception =>
				mod.logger.info(s"[$name] Threw while loading, loading dummy implementation")
				e.printStackTrace
				dummy
		}
	} else {
		mod.logger.info(s"[$name] Condition not met, loading dummy implementation")
		dummy
	}
	mod.logger.info(s"[$name] Loaded")

	def get = impl

	override def preInit(e: FMLPreInitializationEvent)     { impl.preInit(e)        }
	override def init(e: FMLInitializationEvent)           { impl.init(e)           }
	override def postInit(e: FMLPostInitializationEvent)   { impl.postInit(e)       }
	override def serverStarting(e: FMLServerStartingEvent) { impl.serverStarting(e) }
}

object Module {
	def and(conditions: TModuleCondition*) = AndCondition(conditions: _*)
	def or(conditions: TModuleCondition*) = OrCondition(conditions: _*)
	def not(condition: TModuleCondition) = NotCondition(condition)
	def modLoaded(modid: String) = ModLoadedCondition(modid)
}