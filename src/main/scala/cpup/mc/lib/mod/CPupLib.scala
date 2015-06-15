package cpup.mc.lib.mod

import java.lang.annotation.Annotation

import cpup.lib.module._
import cpup.mc.lib.client.CPupGUIManager
import cpup.mc.lib.intermod.baubles.Baubles
import cpup.mc.lib.mod.content.inspecting.Inspector
import cpup.mc.lib.module.ModLoaded
import cpup.mc.lib.network.CPupNetwork
import cpup.mc.lib.util.TickUtil
import cpup.mc.lib.{CPupMod, inspecting}
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{FMLCommonHandler, Loader, Mod}

@ModuleID(id = Ref.modID)
@Mod(modid = Ref.modID, modLanguage = "scala")
object CPupLib extends CPupMod[TRef] {
	def ref = Ref

	val network = new CPupNetwork[CPupLib.type](this, Map(
		classOf[inspecting.RequestMessage] -> (msg => inspecting.RequestMessage.handler(msg.asInstanceOf[inspecting.RequestMessage])),
		classOf[inspecting.ResponseMessage] -> (msg => inspecting.ResponseMessage.handler(msg.asInstanceOf[inspecting.ResponseMessage]))
	))
	registerLifecycleHandler(network)

	val gui = new CPupGUIManager[CPupLib.type](this, List(Inspector.GUI))

	FMLCommonHandler.instance.bus.register(TickUtil)

	ModuleLoader.canLoadHandlers(classOf[ModLoaded]) = ((spec: ModuleSpec[_], a: ModLoaded) => {
		if(Loader.isModLoaded(a.modid)) {
			List()
		} else {
			List(s"${a.modid} is not loaded")
		}
	}).asInstanceOf[(ModuleSpec[_], Annotation) => List[String]]

	final val baubles = ModuleLoader.forceLoad[Baubles.API](this)

	@EventHandler
	override def preInit(e: FMLPreInitializationEvent) {
		super.preInit(e)

		gui.register
		GameRegistry.registerItem(Inspector, Inspector.name)
	}
}
