package cpup.mc.lib.util

import scala.collection.mutable

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent

object TickUtil {
	val handlers = new mutable.HashMap[(TickEvent.Type, TickEvent.Phase, Side.T), mutable.Set[() => Unit]] with mutable.MultiMap[(TickEvent.Type, TickEvent.Phase, Side.T), () => Unit]

	def register(typ: TickEvent.Type, phase: TickEvent.Phase, side: Side.T, handler: () => Unit) {
		handlers.addBinding((typ, phase, side), handler)
	}

	@SubscribeEvent
	def tickEvent(e: TickEvent) {
		for {
			_handlers <- handlers.get((e.`type`, e.phase, e.side))
			handler <- _handlers
		} {
			_handlers -= handler
			handler()
		}
	}
}
