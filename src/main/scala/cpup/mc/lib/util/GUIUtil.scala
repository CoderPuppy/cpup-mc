package cpup.mc.lib.util

import net.minecraft.util.{IIcon, ResourceLocation}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap

@SideOnly(Side.CLIENT)
object GUIUtil {
	def mc = Minecraft.getMinecraft
	def tess = Tessellator.instance

	def drawItemIconAt(icon: IIcon, x: Double, y: Double, z: Double, width: Double, height: Double) {
		GL11.glMatrixMode(5890)
		GL11.glPushMatrix
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
		tess.startDrawingQuads
		tess.addVertexWithUV(x,         y,          z, icon.getMinU, icon.getMinV)
		tess.addVertexWithUV(x,         y + height, z, icon.getMinU, icon.getMaxV)
		tess.addVertexWithUV(x + width, y + height, z, icon.getMaxU, icon.getMaxV)
		tess.addVertexWithUV(x + width, y,          z, icon.getMaxU, icon.getMinV)
		tess.draw
		GL11.glPopMatrix
		GL11.glMatrixMode(5888)
	}

	def drawBlockIconAt(icon: IIcon, x: Double, y: Double, z: Double, width: Double, height: Double) {
		GL11.glMatrixMode(5890)
		GL11.glPushMatrix
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture)
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