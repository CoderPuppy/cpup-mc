package cpup.mc.lib

import org.apache.logging.log4j.LogManager
import cpup.mc.lib.content.CPupContent
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.common.config.Configuration
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.Minecraft
import java.io.File

trait CPupMod[REF <: CPupModRef] {
	def ref: REF
	def content: CPupContent[_ <: CPupMod[REF]] = null
	var config = new Configuration(new File(new File(FMLCommonHandler.instance.getSide match {
		case Side.CLIENT => Minecraft.getMinecraft.mcDataDir
		case Side.SERVER => new File(".")
	}, "config"), ref.modID + ".cfg"))

	final val logger = LogManager.getLogger(ref.modID)

	protected var _lifecycleHandlers = List[ModLifecycleHandler]()
	def registerLifecycleHandler(handler: ModLifecycleHandler) = {
		_lifecycleHandlers = _lifecycleHandlers ++ List(handler)
		this
	}

	@EventHandler
	def preInit(e: FMLPreInitializationEvent) {
		config.load

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

		config.save
	}

	@EventHandler
	def serverStarting(e: FMLServerStartingEvent) {
		_lifecycleHandlers.foreach(_.serverStarting(e))
	}
}