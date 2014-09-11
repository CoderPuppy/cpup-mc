package cpup.mc.lib.module

import cpup.mc.lib.ModLifecycleHandler
import cpw.mods.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import cpup.lib.module.Module

trait ModModule[I <: ModLifecycleHandler] extends Module[I] with ModLifecycleHandler {
	override def preInit(e: FMLPreInitializationEvent) { get.preInit(e) }
	override def init(e: FMLInitializationEvent) { get.init(e) }
	override def postInit(e: FMLPostInitializationEvent) { get.postInit(e) }

	override def serverStarting(e: FMLServerStartingEvent) { get.serverStarting(e) }
}