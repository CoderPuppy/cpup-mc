package cpup.mc.lib.module

import cpup.mc.lib.{CPupModRef, CPupMod, ModLifecycleHandler}
import cpw.mods.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}

class Module[MOD <: ModLifecycleHandler](val mod: CPupMod[_ <: CPupModRef], val condition: TModuleCondition, enabled: => MOD, disabled: => MOD) extends ModLifecycleHandler {
	val impl = if(condition.canLoad) {
		try {
			mod.logger.info(s"[${getClass.getSimpleName}] Loading")
			val res = enabled
			mod.logger.info(s"[${getClass.getSimpleName}] Loaded")
			res
		} catch {
			case e: Exception =>
				mod.logger.info(s"[${getClass.getSimpleName}] Threw while loading")
				e.printStackTrace
				disabled
		}
	} else {
		mod.logger.info(s"[${getClass.getSimpleName}] Condition not met, not loading")
		disabled
	}

	def get = impl

	override def preInit(e: FMLPreInitializationEvent)     { impl.preInit(e)        }
	override def init(e: FMLInitializationEvent)           { impl.init(e)           }
	override def postInit(e: FMLPostInitializationEvent)   { impl.postInit(e)       }
	override def serverStarting(e: FMLServerStartingEvent) { impl.serverStarting(e) }
}