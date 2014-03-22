package cpup.mc.lib.util

import net.minecraft.util.{IIcon, ResourceLocation}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator

@SideOnly(Side.CLIENT)
object GUIUtil {
	val itemsIcon = new ResourceLocation("textures/atlas/items.png")
	def mc = Minecraft.getMinecraft
	def tess = Tessellator.instance

	def drawItemIconAt(icon: IIcon, x: Int, y: Int, z: Double, width: Int, height: Int) {
		GL11.glMatrixMode(5890)
		GL11.glPushMatrix
		mc.renderEngine.bindTexture(itemsIcon)
		tess.startDrawingQuads
		tess.addVertexWithUV(x,         y,          z, icon.getMinU, icon.getMinV)
		tess.addVertexWithUV(x,         y + height, z, icon.getMinU, icon.getMaxV)
		tess.addVertexWithUV(x + width, y + height, z, icon.getMaxU, icon.getMaxV)
		tess.addVertexWithUV(x + width, y,          z, icon.getMaxU, icon.getMinV)
		tess.draw
		GL11.glPopMatrix
		GL11.glMatrixMode(5888)
	}
}