package cpup.mc.lib.util

import scala.collection.mutable

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object TickUtil {
	val handlers = new mutable.HashMap[(TickEvent.Type, TickEvent.Phase, Side.T), mutable.Queue[() => Unit]]

	def register(typ: TickEvent.Type, phase: TickEvent.Phase, side: Side.T, handler: () => Unit) {
		handlers.getOrElseUpdate((typ, phase, side), mutable.Queue.empty).enqueue(handler)
	}

	@SubscribeEvent
	def tickEvent(e: TickEvent) {
		val key = (e.`type`, e.phase, e.side)
		for(_handlers <- handlers.get(key)) {
			while(_handlers.nonEmpty) {
				_handlers.dequeue().apply()
			}
		}
		handlers.remove(key)
	}

	def fromSide(side: Side.T) = side match {
		case Side.CLIENT => TickEvent.Type.CLIENT
		case Side.SERVER => TickEvent.Type.SERVER
	}
}
