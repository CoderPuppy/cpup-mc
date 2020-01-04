package cpup.mc.lib.inspecting

import java.util

import scala.collection.{JavaConversions, mutable}
import scala.ref.WeakReference
import scala.reflect.runtime._

import cpup.mc.lib.mod.CPupLib
import cpup.mc.lib.network.Context
import cpup.mc.lib.util.Side

class Request protected[inspecting](val typ: String, val id: List[Data]) {
	val call = (typ, id).hashCode

	protected[inspecting] var handlers = List[(Either[Data, String]) => Unit]()
	var res: Either[Data, String] = Right("requested")
	def refresh {
		Side.effective match {
			case Side.CLIENT =>
				CPupLib.network.send(Context.Server, new RequestMessage(typ, id))
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
	protected[inspecting] var _requests = new mutable.Map[Int, Request] {
		val map = mutable.Map.empty[Int, WeakReference[Request]]
		override def get(key: Int) = map.get(key) match {
			case Some(v) => v.get match {
				case v: Some[_] => v
				case None => {
					map -= key
					None
				}
			}
			case None => None
		}

		override def +=(kv: (Int, Request)): this.type = {
			map += kv._1 -> new WeakReference[Request](kv._2)
			this
		}

		override def -=(key: Int): this.type = {
			map -= key
			this
		}

		override def iterator = map.iterator.flatMap { kv => kv._2.get match {
			case Some(v) => Some((kv._1, v))
			case None => {
				map -= kv._1
				None
			}
		} }
	}
	def apply(typ: String, _id: Seq[Data]) = {
		val id = _id.toList
		_requests.getOrElseUpdate((typ, id).hashCode, new Request(typ, id))
	}
}
