package cpup.mc.lib.client.imgui

import scala.collection.mutable

import cpup.mc.lib.client.imgui.IMGUI.State
import net.minecraft.client.gui.FontRenderer

trait Form {
	def width: Int
	def height: Int
	def render(width: Int, height: Int, in: Boolean, state: State, prevState: State)
	def update(width: Int, height: Int, in: Boolean, state: State, prevState: State) {}
}

object Form {
	trait Clickable extends Form {
	}
}
