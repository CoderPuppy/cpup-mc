package cpup.mc.lib.client

import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.gui.GuiScreen
import net.minecraft.inventory.Container
import cpup.mc.lib.{CPupModRef, CPupMod}
import net.minecraft.entity.player.EntityPlayer

trait CPupGUI[MOD <: CPupMod[_ <: CPupModRef]] {
	def name: String
	def mod: MOD

	def clientGUI(player: EntityPlayer, world: World, x: Int, y: Int, z: Int): GuiScreen
	def container(player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Container
}
