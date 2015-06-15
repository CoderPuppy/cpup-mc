package cpup.mc.lib.mod.content.inspecting

import java.util

import scala.collection.mutable

import cpup.mc.lib.client.imgui
import cpup.mc.lib.client.imgui.{Label, Widget}
import cpup.mc.lib.inspecting.Registry.Data
import cpup.mc.lib.inspecting.Request
import cpup.mc.lib.mod.content.{BaseGUI, BaseItem}
import cpup.mc.lib.util.EntityUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.client.gui.{GuiButton, GuiScreen}
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import org.lwjgl.input.{Keyboard, Mouse}
import org.lwjgl.opengl.GL11

object Inspector extends BaseItem {
	name = "inspector"

	override def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float) = {
		mod.gui.open(player, world, x, y, z, GUI)
		true
	}

	object GUI extends BaseGUI {
		override def name = "inspector"

		override def container(player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Cont = null
		class Cont extends Container {
			override def canInteractWith(player: EntityPlayer) = true
		}
		override def clientGUI(player: EntityPlayer, world: World, x: Int, y: Int, z: Int) = {
			new Screen(("minecraft:block:side", List(
				Data.Int(world.provider.dimensionId),
				Data.Int(x), Data.Int(y), Data.Int(z),
				Data.Int(EntityUtil.getMOPBlock(player, Minecraft.getMinecraft.playerController.getBlockReachDistance).sideHit)
			)))
		}
		class Screen(req: (String, List[Data])) extends GuiScreen {
			val path = mutable.Stack[Request](Request(req._1, req._2))

			val upBtn = new GuiButton(0, 5, 5, 20, 20, "^")

			override def actionPerformed(btn: GuiButton) {
				super.actionPerformed(btn)
				btn match {
					case `upBtn` =>
						path.pop
						if(path.isEmpty) {
							mc.displayGuiScreen(null)
							mc.setIngameFocus
						}

					case _ =>
				}
			}

			override def initGui {
				super.initGui
				buttonList.asInstanceOf[util.List[GuiButton]].add(upBtn)
			}

			val gui = new imgui.IMGUI

			override def handleMouseInput {
				super.handleMouseInput
				val mx = Mouse.getEventX * this.width / this.mc.displayWidth
				val my = this.height - Mouse.getEventY * this.height / this.mc.displayHeight - 1
				val btn = Mouse.getEventButton
				val down = Mouse.getEventButtonState
				gui.updateMouse(mx, my, btn, down)
			}

			override def handleKeyboardInput {
				super.handleKeyboardInput
				gui.updateKeyboard(Keyboard.getEventKey, Keyboard.getEventCharacter, Keyboard.getEventKeyState, Keyboard.isRepeatEvent)
			}

			def render(data: Data, x: Int = 0): Int = {
				// TODO: these are only here for tweaking
				val indentSize = 20
				val lineHeight = 10
				data match {
					case tbl: Data.Table => {
						var height = 2
						gui(Widget(Label(s"{ #${tbl.size}"), x = x))
						gui.pushMatrix
						gui.translate(indentSize, lineHeight)
						for(kv <- tbl) {
							val keyWidth = gui(Widget(Label(kv._1 + " = "))).width
							val elHeight = render(kv._2, keyWidth)
							height += elHeight
							gui.translate(0, elHeight * lineHeight)
						}
						gui(Widget(Label("}"), x = -indentSize))
						gui.popMatrix
						height
					}
					case list: Data.List => {
						var height = 2
						gui(Widget(Label(s"[ #${list.size}"), x = x))
						gui.pushMatrix
						gui.translate(indentSize, lineHeight)
						for(el <- list) {
							val elHeight = render(el, 0)
							height += elHeight
							gui.translate(0, elHeight * lineHeight)
						}
						gui(Widget(Label("]"), x = -indentSize))
						gui.popMatrix
						height
					}
					case Data.String(str) =>
						gui(Widget(Label(
							"\"" + str
								.replaceAll("\\\\", "\\\\")
								.replaceAll("\"", "\\\"")
								.replaceAll("\n", "\\n")
								.replaceAll("\r", "\\r")
							+ "\""),
						x = x))
						1
					case v: Data.Primitive =>
						gui(Widget(Label(v.toString), x = x))
						1
					case link: Data.Link => {
						if(gui(Widget(Label(link.toString), x = x)).clicked(0)) {
							path.push(Request(link.typ, link.id))
						}
						1
					}
					case Data.Nil =>
						gui(Widget(Label("nil"), x = x))
						1
				}
			}

			override def drawScreen(mx: Int, my: Int, tick: Float) {
				GL11.glPushMatrix
				GL11.glDisable(GL11.GL_LIGHTING)
				GL11.glDisable(GL11.GL_FOG)
				val tessellator: Tessellator = Tessellator.instance
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
				val f: Float = 32.0F
				GL11.glDisable(GL11.GL_TEXTURE_2D)
				tessellator.startDrawingQuads
				tessellator.setColorOpaque_I(0x888888)
				tessellator.addVertex(0, height, 0)
				tessellator.addVertex(width, height, 0)
				tessellator.addVertex(width, 0, 0)
				tessellator.addVertex(0, 0, 0)
				tessellator.draw
				GL11.glEnable(GL11.GL_TEXTURE_2D)
				super.drawScreen(mx, my, tick)
				val req = path.top
				gui(Widget(Label(s"${req.typ}: ${req.id.mkString(", ")}"), x = 30, y = 10))
				gui.pushMatrix
				gui.translate(5, 30)
//				gui.rotate(.5)
				path.top.res match {
					case Left(data) => render(data)
					case Right(err) => gui(Widget(Label(err, 0xFF0000)))
				}
				gui.popMatrix
				gui.tick
				GL11.glPopMatrix
			}

			override def doesGuiPauseGame = false
		}
	}
}
