package cpup.mc.lib.client.imgui

import java.nio.{ByteBuffer, ByteOrder, DoubleBuffer}

import scala.collection.mutable

import cpup.lib.data.matrix.Matrix.Rich
import cpup.lib.data.matrix.{FlatMatrix, Matrix}
import cpup.mc.lib.client.imgui.IMGUI.State
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Matrix4f

class IMGUI {
	protected[imgui] var state = State(0, 0)
	protected[imgui] var prevState: State = state
	def updateMouse(x: Int, y: Int, btn: Int, down: Boolean) {
		state = State(x, y, if(down) state.mouseDown + btn else state.mouseDown - btn, state.keyDown, state.charDown, Minecraft.getSystemTime)
	}
	def updateKeyboard(key: Int, char: Char, down: Boolean, repeat: Boolean) {
		state = State(state.x, state.y, state.mouseDown,
			if(down) state. keyDown + ( key -> (repeat, char)) else state. keyDown - key,
			if(down) state.charDown + (char -> (repeat,  key)) else state.charDown - char,
			Minecraft.getSystemTime)
	}

	val matrixStack = mutable.Stack[Matrix[Double]](FlatMatrix.create[Double](
		1+1+1,
		1,0,0,
		0,1,0,
		0,0,1
	))
	def pushMatrix {
		matrixStack.push(matrixStack.top)
	}
	def popMatrix {
		if(matrixStack.size > 1) matrixStack.pop
	}
	def translate(x: Double, y: Double) {
		matrixStack.push(matrixStack.pop + FlatMatrix.create[Double](
			1+1+1,
			0,0,x,
			0,0,y,
			0,0,0
		))
	}
	def rotate(_rot: Double) {
		val rot = _rot * Math.PI
		val s = Math.sin(rot)
		val c = Math.cos(rot)
		matrixStack.push(matrixStack.pop * FlatMatrix.create[Double](
			 1+1+1,
			 c,s,0,
			-s,c,0,
			 0,0,1
		))
	}

	def invert(t: Matrix[Double]) = {
		val builder = t.canBuild.build[Double](t.width, t.height)
		builder(0, 0) = t(0, 0)
		builder(1, 1) = t(1, 1)
		builder(1, 0) = t(0, 1)
		builder(0, 1) = t(1, 0)
		builder(2, 0) = -t(2, 0)
		builder(2, 1) = -t(2, 1)
		builder.result
	}

	def apply[F <: Form](widget: Widget[F]): Widget[F] = {
		val t = matrixStack.top
		val ut = invert(t)
		val subState = state.transform(ut).offset(-widget.x, -widget.y)
		val subPrevState = prevState.transform(ut).offset(-widget.x, -widget.y)
		val now = widget.contains(subState.x, subState.y)
		widget.update(now, subState, subPrevState)
		GL11.glPushMatrix
		val _buffer = ByteBuffer.allocateDirect(4 * 4 * 8)
		_buffer.order(ByteOrder.nativeOrder)
		val buffer = _buffer.asDoubleBuffer
		def put(x: Int, y: Int, v: Double) {
			buffer.put(x * 4 + y, v)
		}
		for(x <- 0 until 4; y <- 0 until 4) put(x, y, 0)
		put(0, 0, t(0, 0))
		put(1, 1, t(0, 0))
		put(2, 2, t(0, 0))
		put(1, 0, t(0, 1))
		put(0, 1, t(1, 0))
//		put(0, 0, 1)
//		put(1, 1, 1)
//		put(2, 2, 1)
		put(3, 3, 1)
		put(3, 0, t(2, 0))
		put(3, 1, t(2, 1))
//		for(x <- 0 until t.width; y <- 0 until t.height) {
//			put(x, y, t(x, y))
//		}
		GL11.glMultMatrix(buffer)
//		GL11.glTranslated(t(2, 0), t(2, 1), 0)
		GL11.glTranslated(widget.x, widget.y, 0)
		widget.render(now, subState, subPrevState)
		GL11.glPopMatrix
		widget
	}

	def tick {
		prevState = state
	}
}

object IMGUI {
	case class State(x: Double, y: Double, mouseDown: Set[Int] = Set.empty, keyDown: Map[Int, (Boolean, Char)] = Map.empty, charDown: Map[Char, (Boolean, Int)] = Map.empty, time: Long = Minecraft.getSystemTime) {
		def offset(_x: Double, _y: Double) = State(x + _x, y + _y, mouseDown, keyDown, charDown, time)
		def transform(m: Matrix[Double]) = {
			assert(m.width == 3 && m.height == 3, "must be 3x3")
			val vec = m * FlatMatrix.create[Double](1, x, y, 1)
			State(vec(0, 0), vec(0, 1), mouseDown, keyDown, charDown, time)
		}
	}
}
