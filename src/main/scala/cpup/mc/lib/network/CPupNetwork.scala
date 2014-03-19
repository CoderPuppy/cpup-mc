package cpup.mc.lib.network

import cpup.mc.lib.{ModLifecycleHandler, CPupModRef, CPupMod}
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent}

trait CPupNetwork[MOD <: CPupMod[_ <: CPupModRef]] extends ModLifecycleHandler {
	def mod: MOD

	def register {

	}

	def finish {

	}

	override def init(e: FMLInitializationEvent) { register }
	override def postInit(e: FMLPostInitializationEvent) { finish }
}