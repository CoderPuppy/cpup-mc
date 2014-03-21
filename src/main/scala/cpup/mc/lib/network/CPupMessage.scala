package cpup.mc.lib.network

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import cpup.mc.lib.CPupMod

trait CPupMessage[MOD <: CPupMod[_]] {
	def mod: MOD
	def writeTo(ctx: ChannelHandlerContext, buf: ByteBuf)
}