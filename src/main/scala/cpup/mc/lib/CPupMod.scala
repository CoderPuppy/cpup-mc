package cpup.mc.lib

import org.apache.logging.log4j.LogManager
import cpup.mc.lib.content.CPupContent
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import cpup.mc.lib.network.{CPupMessage, CPupNetwork}

trait CPupMod[REF <: CPupModRef] {
	def ref: REF
	def content: CPupContent[_ <: CPupMod[REF]] = null

	final val logger = LogManager.getLogger(ref.modID)

	@EventHandler
	def preInit(e: FMLPreInitializationEvent) {
		if(content != null) content.preInit(e)
	}

	@EventHandler
	def init(e: FMLInitializationEvent) {
		if(content != null) content.init(e)
	}

	@EventHandler
	def postInit(e: FMLPostInitializationEvent) {
		if(content != null) content.postInit(e)
	}
}