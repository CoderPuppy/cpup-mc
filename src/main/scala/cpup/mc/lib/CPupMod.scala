package cpup.mc.lib

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import cpup.mc.lib.content.CPupContent
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.common.config.Configuration
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.Minecraft
import java.io.File
import cpup.lib.module.Module
import cpup.mc.lib.logging.JCLForSLF4JLogger
import org.slf4j.Logger

trait CPupMod[REF <: CPupModRef] extends Module[CPupMod[REF]] with ModLifecycleHandler {
	def ref: REF
	def content: CPupContent[_ <: CPupMod[REF]] = null
	var config = new Configuration(new File(new File(FMLCommonHandler.instance.getSide match {
		case Side.CLIENT => Minecraft.getMinecraft.mcDataDir
		case Side.SERVER => new File(".")
	}, "config"), ref.modID + ".cfg"))
	config.load

	override final val logger: Logger = new JCLForSLF4JLogger(LogManager.getLogger(ref.modID))

	protected var _lifecycleHandlers = List[ModLifecycleHandler]()
	def registerLifecycleHandler(handler: ModLifecycleHandler) = {
		_lifecycleHandlers = _lifecycleHandlers ++ List(handler)
		this
	}

	def loadModule[M <: Module[_] with ModLifecycleHandler](module: M) = {
		registerLifecycleHandler(module)
		module.load
		this
	}

	def get = this

	@EventHandler
	override def preInit(e: FMLPreInitializationEvent) {
		//config.load

		if(content != null) content.preInit(e)

		_lifecycleHandlers.foreach(_.preInit(e))
	}

	@EventHandler
	override def init(e: FMLInitializationEvent) {
		if(content != null) content.init(e)

		_lifecycleHandlers.foreach(_.init(e))
	}

	@EventHandler
	override def postInit(e: FMLPostInitializationEvent) {
		if(content != null) content.postInit(e)

		_lifecycleHandlers.foreach(_.postInit(e))

		config.save
	}

	@EventHandler
	override def serverStarting(e: FMLServerStartingEvent) {
		_lifecycleHandlers.foreach(_.serverStarting(e))
	}
}