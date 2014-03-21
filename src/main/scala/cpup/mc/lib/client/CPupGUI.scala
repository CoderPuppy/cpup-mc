package cpup.mc.lib.client

import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.gui.GuiScreen
import net.minecraft.inventory.Container
import cpup.mc.lib.CPupMod
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import cpup.mc.lib.util.pos.BlockPos

trait CPupGUI[MOD <: CPupMod[_], GUI <: GuiScreen, CONT <: Container] {
	def name: String
	def mod: MOD

	@SideOnly(Side.CLIENT)
	def clientGUI(player: EntityPlayer, pos: BlockPos): GUI

	def container(player: EntityPlayer, pos: BlockPos): CONT
}