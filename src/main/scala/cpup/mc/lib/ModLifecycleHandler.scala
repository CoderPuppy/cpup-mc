package cpup.mc.lib

import net.minecraftforge.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLPreInitializationEvent, FMLInitializationEvent}

trait ModLifecycleHandler {
	def preInit(e: FMLPreInitializationEvent) {}
	def init(e: FMLInitializationEvent) {}
	def postInit(e: FMLPostInitializationEvent) {}
	def serverStarting(e: FMLServerStartingEvent) {}
}