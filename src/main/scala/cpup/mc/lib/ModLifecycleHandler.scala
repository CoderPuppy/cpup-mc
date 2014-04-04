package cpup.mc.lib

import cpw.mods.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLPreInitializationEvent, FMLInitializationEvent}

trait ModLifecycleHandler {
	def preInit(e: FMLPreInitializationEvent) {}
	def init(e: FMLInitializationEvent) {}
	def postInit(e: FMLPostInitializationEvent) {}
	def serverStarting(e: FMLServerStartingEvent) {}
}