package cpup.mc.lib.module

import cpup.mc.lib.ModLifecycleHandler
import cpw.mods.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}

class Module[MOD <: ModLifecycleHandler](val condition: TModuleCondition, enabled: => MOD, disabled: => MOD) extends ModLifecycleHandler {
	val impl = if(condition.canLoad) {
		try {
			enabled
		} catch {
			case e: Exception =>
				disabled
		}
	} else {
		disabled
	}

	def get = impl

	override def preInit(e: FMLPreInitializationEvent)     { impl.preInit(e)        }
	override def init(e: FMLInitializationEvent)           { impl.init(e)           }
	override def postInit(e: FMLPostInitializationEvent)   { impl.postInit(e)       }
	override def serverStarting(e: FMLServerStartingEvent) { impl.serverStarting(e) }
}