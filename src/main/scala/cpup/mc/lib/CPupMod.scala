package cpup.mc.lib

import java.io.File

import cpup.lib.config.ConfigLoader
import cpup.lib.module.{ModuleLoader, ModuleSpec, TModule}
import cpup.mc.lib.content.CPupContent
import cpup.mc.lib.module.FMLModule
import cpup.mc.lib.util.Side
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent, FMLServerStartingEvent}

trait CPupMod[REF <: CPupModRef] extends TModule[CPupMod[REF]] with ModLifecycleHandler {
	ModuleLoader.modulesByInst(this) = this

	def ref: REF
	def content: CPupContent[_ <: CPupMod[REF]] = null

	override def inst = this
	override def spec = new ModuleSpec[CPupMod[REF]](ModuleLoader.moduleType[CPupMod[REF]](classOf[CPupMod[REF]]), FMLModule.spec) {
		// i really shouldn't do this, but i need t he config before preinit
		override lazy val config = ConfigLoader(new File(new File(Side.effective match {
			case Side.CLIENT => Minecraft.getMinecraft.mcDataDir
			case Side.SERVER => new File(".")
		}, "config"), ref.modID + ".conf"), (name: String) => {
			Option(getClass.getClassLoader.getResourceAsStream(s"assets/${ref.modID}/config/$name.conf"))
		})
		override lazy val id = s"${parent.id}/${ref.modID}"
	}

	// most of the time you'll want to provide it
	def loadModule[T <: AnyRef](implicit manifest: Manifest[T]) = ModuleLoader.load[T](this, provide = true)(manifest).left.map(m => {
		handleModule[T](m)(manifest)
		m
	})

	def forceLoadModule[T <: AnyRef](implicit manifest: Manifest[T]) = {
		val m = ModuleLoader.forceLoad[T](this, provide = true)(manifest)
		handleModule[T](m)(manifest)
		m
	}

	private def handleModule[T <: AnyRef](m: T)(implicit manifest: Manifest[T]) {
		m match {case h: ModLifecycleHandler =>
			registerLifecycleHandler(h)
		case _ =>}
	}

	protected var _lifecycleHandlers = List[ModLifecycleHandler]()
	def registerLifecycleHandler(handler: ModLifecycleHandler) = {
		_lifecycleHandlers = _lifecycleHandlers ++ List(handler)
		this
	}

	def get = this

	@EventHandler
	override def preInit(e: FMLPreInitializationEvent) {
		if(content != null) content.preInit(e)

		_lifecycleHandlers.foreach(handler => {
			handler.preInit(e)
		})
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
	}

	@EventHandler
	override def serverStarting(e: FMLServerStartingEvent) {
		_lifecycleHandlers.foreach(_.serverStarting(e))
	}
}
