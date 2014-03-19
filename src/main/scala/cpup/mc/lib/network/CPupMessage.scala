package cpup.mc.lib.network

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

trait CPupMessage {
	def writeTo(ctx: ChannelHandlerContext, buf: ByteBuf)
}