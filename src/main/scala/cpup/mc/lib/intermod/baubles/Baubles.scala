package cpup.mc.lib.intermod.baubles

import baubles.api.BaublesApi
import com.typesafe.config.Config
import cpup.lib.module.{ModuleID, MultiModule}
import cpup.mc.lib.inventory.EmptyInventory
import cpup.mc.lib.module.ModLoaded
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import org.slf4j.Logger

object Baubles {
	@ModuleID(id = "baubles")
	@MultiModule(clazzs = Array(
		classOf[Baubles.Real],
		classOf[Baubles.Dummy]
	))
	trait API {
		def getBaubles(player: EntityPlayer): IInventory
	}

	@ModuleID(id = "real")
//	@CanLoad.ClassAvailable(clazz = classOf[baubles.api.BaublesApi])
	@ModLoaded(modid = "Baubles")
	class Real(config: Config, logger: Logger) extends API {
		def getBaubles(player: EntityPlayer) = BaublesApi.getBaubles(player)
	}

	@ModuleID(id = "dummy")
	class Dummy(config: Config, logger: Logger) extends API {
		def getBaubles(player: EntityPlayer) = EmptyInventory
	}
}
