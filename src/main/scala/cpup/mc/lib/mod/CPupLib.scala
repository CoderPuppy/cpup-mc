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
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.common.{FMLCommonHandler, Loader, Mod}

@ModuleID(id = Ref.modID)
@Mod(modid = Ref.modID, modLanguage = "scala")
object CPupLib extends CPupMod[TRef] {
	def ref = Ref

	val network = new CPupNetwork[AnyRef](ref.modID, new AnyRef, CPupNetwork.simpleNetwork[this.type](this), Set(
		classOf[inspecting.RequestMessage],
		classOf[inspecting.ResponseMessage]
	))

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
