package cpup.mc.lib.client.imgui

import cpup.mc.lib.client.imgui.IMGUI.State
import net.minecraft.client.Minecraft

case class Label(var text: String, var color: Int = 0xFFFFFF, var dropShadow: Boolean = false) extends Form with Form.Clickable {
	lazy val fontRenderer = Minecraft.getMinecraft.fontRenderer
	override def render(width: Int, height: Int, in: Boolean, state: State, prevState: State) {
		fontRenderer.drawString(text, 0, 0, if(in) 0xFF0000 else color, dropShadow)
	}

	override def width = fontRenderer.getStringWidth(text)
	override def height = 8
}
