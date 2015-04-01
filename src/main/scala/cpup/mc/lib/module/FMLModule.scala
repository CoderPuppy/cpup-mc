package cpup.mc.lib.module

import com.typesafe.config.ConfigFactory
import cpup.lib.module._

@ModuleID(id = "fml")
abstract class TFMLModule {}
object FMLModule extends TFMLModule with TModule[TFMLModule] {
	ModuleLoader.modulesByInst(this) = this
	override final val inst = this
	override lazy val spec = new ModuleSpec[TFMLModule](ModuleLoader.moduleType[TFMLModule](classOf[TFMLModule]), RootModule.spec) {
		override def config = ConfigFactory.load
	}
}
