package cpup.mc.lib.client

import cpw.mods.fml.common.network.{NetworkRegistry, IGuiHandler}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import cpup.mc.lib.CPupMod
import net.minecraft.client.gui.GuiScreen
import cpw.mods.fml.relauncher.{Side, SideOnly}
import cpup.mc.lib.util.pos.BlockPos
import net.minecraft.inventory.Container

class CPupGUIManager[MOD <: CPupMod[_], GUI <: CPupGUI[MOD, _ <: GuiScreen, _ <: Container]](val mod: MOD) extends IGuiHandler {
	protected var guis = List[GUI]()

	def register(gui: GUI) {
		if(finished) {
			throw new Exception("Attempt to register gui after finishing: " + gui.name + " (" + gui.getClass.getCanonicalName + ")")
		}

		guis ::= gui
	}

	def open(player: EntityPlayer, pos: BlockPos, gui: GUI) {
		if(!guis.contains(gui)) {
			throw new NullPointerException("Attempt to open unregistered gui: " + gui.name + " (" + gui.getClass.getCanonicalName + ")")
		}

		player.openGui(mod, guis.indexOf(gui), pos.world, pos.x, pos.y, pos.z)
	}

	def register {
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, this)
	}

	protected var _finished = false
	def finished = _finished
	def finish {
		if(_finished) {
			return
		}

		_finished = true

		guis = guis.sortBy(_.name)
	}

	@SideOnly(Side.CLIENT)
	def getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = if(id >= 0 && id < guis.size) {
		guis(id).clientGUI(player, BlockPos(world, x, y, z))
	} else { null }

	def getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = if(id >= 0 && id < guis.size) {
		guis(id).container(player, BlockPos(world, x, y, z))
	} else { null }
}