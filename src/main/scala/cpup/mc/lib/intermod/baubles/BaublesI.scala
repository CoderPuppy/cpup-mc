package cpup.mc.lib.intermod.baubles

import cpup.mc.lib.module.{ModModule, ModLoadedCondition}
import cpup.mc.lib.ModLifecycleHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import baubles.api.BaublesApi
import cpup.mc.lib.util.InvUtil
import cpup.lib.module.{MultiModule, Module}
import cpup.mc.lib.mod.CPupLib

trait Interface extends Module[Interface] with ModLifecycleHandler {
	override def parent = Some(BaublesI)
	override def get = this
	def getItems(player: EntityPlayer): List[ItemStack]
}

object Real extends Interface {
	override def canLoad = ModLoadedCondition("Baubles").canLoad

	override def getItems(player: EntityPlayer) = InvUtil.getItems(BaublesApi.getBaubles(player))
}

object Dummy extends Interface {
	override def getItems(player: EntityPlayer) = List()
}

object BaublesI extends MultiModule[Interface](Real, Dummy) with ModModule[Interface] {
	override def parent = Some(CPupLib)
}