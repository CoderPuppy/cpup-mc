package cpup.mc.lib.client.imgui

import scala.collection.mutable

import cpup.mc.lib.client.imgui.IMGUI.State

case class Widget[F <: Form](var form: F, var x: Double = 0, var y: Double = 0, _width: Option[Int] = None, _height: Option[Int] = None) {
	var width = _width.getOrElse(form.width)
	var height = _height.getOrElse(form.height)

	def contains(_x: Double, _y: Double) = {
//		println(_x, _y, x, y, width, height, form, _x >= x, _x <= x + width, _y >= y, _y <= y + height)
		_x >= 0 && _x <= width && _y >= 0 && _y <= height
	}

	protected[imgui] val _clicked = mutable.Set.empty[Int]
	protected[imgui] val _clicks = mutable.Set.empty[Int]

	def clicked(btn: Int) = if(_clicked.contains(btn)) {
	_clicks -= btn
	true
	} else false

	def update(in: Boolean, state: State, prevState: State) {
		for(btn <- _clicked if !state.mouseDown.contains(btn)) {
			_clicked -= btn
			_clicks -= btn
		}
		if(in) {
			for(btn <- state.mouseDown if !_clicked.contains(btn)) {
				_clicks += btn
				_clicked += btn
			}
		}
		form.update(width, height, in, state, prevState)
	}
	def render(in: Boolean, state: State, prevState: State) = form.render(width, height, in, state, prevState)
}
