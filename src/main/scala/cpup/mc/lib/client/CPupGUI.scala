package cpup.mc.lib.client

import net.minecraft.world.World
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.gui.GuiScreen
import net.minecraft.inventory.Container
import cpup.mc.lib.CPupMod
import net.minecraft.entity.player.EntityPlayer

trait CPupGUI[MOD <: CPupMod[_], GUI <: GuiScreen, CONT <: Container] {
	def name: String
	def mod: MOD

	@SideOnly(Side.CLIENT)
	def clientGUI(player: EntityPlayer, world: World, x: Int, y: Int, z: Int): GUI

	def container(player: EntityPlayer, world: World, x: Int, y: Int, z: Int): CONT
}