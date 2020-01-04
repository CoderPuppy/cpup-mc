package cpup.mc.lib.client

import net.minecraftforge.fml.common.network.{NetworkRegistry, IGuiHandler}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import cpup.mc.lib.{CPupModRef, CPupMod}
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraft.inventory.Container

class CPupGUIManager[MOD <: CPupMod[_ <: CPupModRef]](val mod: MOD, val _guis: Seq[CPupGUI[MOD]]) extends IGuiHandler {
	protected var guis = _guis.toList

//	def register(gui: CPupGUI[MOD]) {
//		if(finished) {
//			throw new Exception("Attempt to register gui after finishing: " + gui.name + " (" + gui.getClass.getCanonicalName + ")")
//		}
//
//		guis ::= gui
//	}

	def open(player: EntityPlayer, world: World, x: Int, y: Int, z: Int, gui: CPupGUI[MOD]) {
		if(!guis.contains(gui)) {
			throw new NullPointerException("Attempt to open unregistered gui: " + gui.name + " (" + gui.getClass.getCanonicalName + ")")
		}

		player.openGui(mod, guis.indexOf(gui), world, x, y, z)
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



//		guis = guis.sortBy(_.name)
	}

	def getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = if(id >= 0 && id < guis.size) {
		guis(id).clientGUI(player, world, x, y, z)
	} else { null }

	def getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = if(id >= 0 && id < guis.size) {
		guis(id).container(player, world, x, y, z)
	} else { null }
}
