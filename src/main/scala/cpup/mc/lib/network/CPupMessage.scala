package cpup.mc.lib.network

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import cpup.mc.lib.{CPupModRef, CPupMod}
import net.minecraft.network.PacketBuffer

trait CPupMessage[MOD <: CPupMod[_ <: CPupModRef]] {
	def mod: MOD
	def writeTo(buf: PacketBuffer)
}
