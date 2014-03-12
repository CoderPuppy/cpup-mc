package cpup.mc.lib

import org.apache.logging.log4j.LogManager
import cpup.mc.lib.content.CPupContent
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}

trait CPupMod[REF <: CPupModRef] {
	def ref: REF
	def content: CPupContent[_]

	final val logger = LogManager.getLogger(ref.modID)
	final val network = new CPupNetwork(this)

	@EventHandler
	def preInit(e: FMLPreInitializationEvent) {
		if(content != null) content.preInit(e)
	}

	@EventHandler
	def init(e: FMLInitializationEvent) {
		if(content != null) content.init(e)
		//network.register
	}

	@EventHandler
	def postInit(e: FMLPostInitializationEvent) {
		if(content != null) content.postInit(e)
		//network.finish
	}
}