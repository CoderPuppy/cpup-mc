package cpup.mc.lib.inspecting

import java.util

import scala.collection.{JavaConversions, mutable}

import cpup.mc.lib.mod.CPupLib
import cpup.mc.lib.util.Side

class Request protected[inspecting](val typ: String, val id: List[Data]) {
	val call = (typ, id).hashCode

	protected[inspecting] var handlers = List[(Either[Data, String]) => Unit]()
	var res: Either[Data, String] = Right("requested")
	def refresh {
		Side.effective match {
			case Side.CLIENT =>
				CPupLib.network.sendToServer(new RequestMessage(typ, id))
			case Side.SERVER =>
				update(Registry.get(typ, id: _*))
		}
	}
	refresh

	def on(handler: (Either[Data, String]) => Unit) {
		handlers ::= handler
		handler(res)
	}

	protected[inspecting] def update(neww: Either[Data, String]) {
		res = neww
		for(handler <- handlers) handler(neww)
	}
}

object Request {
	protected[inspecting] var _requests = JavaConversions.mapAsScalaMap(new util.WeakHashMap[Int, Request]())
	def apply(typ: String, _id: Seq[Data]) = {
		val id = _id.toList
		_requests.getOrElseUpdate((typ, id).hashCode, new Request(typ, id))
	}
}
