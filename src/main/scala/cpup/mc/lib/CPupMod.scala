package cpup.mc.lib

import org.apache.logging.log4j.LogManager
import cpup.mc.lib.content.CPupContent
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import cpup.mc.lib.network.CPupMessage

trait CPupMod[REF <: CPupModRef] {
	def ref: REF
	def content: CPupContent[_ <: CPupMod[REF]] = null

	final val logger = LogManager.getLogger(ref.modID)

	protected var _lifecycleHandlers = List[ModLifecycleHandler]()
	def registerLifecycleHandler(handler: ModLifecycleHandler) = {
		_lifecycleHandlers = _lifecycleHandlers ++ List(handler)
		this
	}

	@EventHandler
	def preInit(e: FMLPreInitializationEvent) {
		if(content != null) content.preInit(e)

		_lifecycleHandlers.foreach(_.preInit(e))
	}

	@EventHandler
	def init(e: FMLInitializationEvent) {
		if(content != null) content.init(e)

		_lifecycleHandlers.foreach(_.init(e))
	}

	@EventHandler
	def postInit(e: FMLPostInitializationEvent) {
		if(content != null) content.postInit(e)

		_lifecycleHandlers.foreach(_.postInit(e))
	}

	@EventHandler
	def serverStarting(e: FMLServerStartingEvent) {
		_lifecycleHandlers.foreach(_.serverStarting(e))
	}
}