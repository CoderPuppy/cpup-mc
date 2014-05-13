package cpup.mc.lib.module

import cpup.mc.lib.{CPupModRef, CPupMod, ModLifecycleHandler}
import cpw.mods.fml.common.event.{FMLServerStartingEvent, FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}

class Module[I <: ModLifecycleHandler](val mod: CPupMod[_ <: CPupModRef], dummy: => I, val impls: Module.Impl[_ <: I]*) extends ModLifecycleHandler {
	val name: String = getClass.getSimpleName.replaceFirst("\\$$", "")

	val impl = impls.find((impl) => {
		val default = impl.default
		val configProp = mod.config.get(s"modules.$name", impl.name, default, s"Whether to enable the ${impl.name} implementation for $name (default: $default)")
		val enabled = configProp.getBoolean(default)
		mod.logger.info(s"[$name] Trying ${impl.name}")
		mod.logger.info(s"[$name : ${impl.name}] -- ${if(enabled) "Enabled" else "Disabled"} in the config")
		for(msg <- impl.canLoad.messages) {
			mod.logger.info(s"[$name : ${impl.name}] -- $msg")
		}
		impl.canLoad.toBoolean && enabled && (try {
			impl.impl
			mod.logger.info(s"[$name : ${impl.name}] Loaded")
			true
		} catch {
			case e: Exception =>
				mod.logger.info(s"[$name : ${impl.name}] Threw while loading:")
				e.printStackTrace
				false
		})
	}).map(_.impl).getOrElse({
		mod.logger.info(s"[$name] No implementations are loadable, using dummy implementation")
		dummy
	})

	mod.logger.info(s"[$name] Loaded")

	def get = impl

	override def preInit(e: FMLPreInitializationEvent)     { impl.preInit(e)        }
	override def init(e: FMLInitializationEvent)           { impl.init(e)           }
	override def postInit(e: FMLPostInitializationEvent)   { impl.postInit(e)       }
	override def serverStarting(e: FMLServerStartingEvent) { impl.serverStarting(e) }
}

object Module {
	def and(conditions: TModuleCondition*) = AndCondition(conditions: _*)
	def or(conditions: TModuleCondition*) = OrCondition(conditions: _*)
	def not(condition: TModuleCondition) = NotCondition(condition)
	def modLoaded(modid: String) = ModLoadedCondition(modid)
	def fn(name: String, fn: => Boolean) = FunctionCondition(name, fn)
	def yes(msg: String = "Yes") = YesCondition(msg)
	def no(msg: String = "No") = NoCondition(msg)

	class Impl[I](val condition: TModuleCondition, val default: Boolean, _impl: => I)(implicit val manifest: Manifest[I]) {
		val name = manifest.runtimeClass.getSimpleName.replaceAll("\\$$", "")
		val canLoad = condition.canLoad
		lazy val impl = _impl
	}

	object Impl {
		def apply[I](condition: TModuleCondition, default: Boolean, impl: => I)(implicit manifest: Manifest[I]) = {
			new Impl(condition, default, impl)
		}
	}
}