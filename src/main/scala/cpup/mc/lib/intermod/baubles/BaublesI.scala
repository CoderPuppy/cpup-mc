package cpup.mc.lib.intermod.baubles

import cpup.mc.lib.module.{ModLoadedCondition, Module}
import cpup.mc.lib.ModLifecycleHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import baubles.api.BaublesApi
import cpup.mc.lib.util.InvUtil
import cpup.mc.lib.mod.CPupLib

trait Interface extends ModLifecycleHandler {
	def getItems(player: EntityPlayer): List[ItemStack]
}

object Real extends Interface {
	def getItems(player: EntityPlayer) = InvUtil.getItems(BaublesApi.getBaubles(player))
}

object Dummy extends Interface {
	def getItems(player: EntityPlayer) = List()
}

object BaublesI extends Module[Interface](CPupLib,
	Dummy,
	Module.Impl(Module.modLoaded("Baubles"), Real)
)