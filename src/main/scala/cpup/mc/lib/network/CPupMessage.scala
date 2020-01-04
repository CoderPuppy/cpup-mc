package cpup.mc.lib.network

import net.minecraft.network.PacketBuffer

trait CPupMessage[DATA <: AnyRef] {
	// def this(EntityPlayer, PacketBuffer, DATA)
	def writeTo(buf: PacketBuffer)
	def handle(data: DATA): Option[CPupMessage[DATA]]
}
