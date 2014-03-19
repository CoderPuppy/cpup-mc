package cpup.mc.lib.network

import cpup.mc.lib.{ModLifecycleHandler, CPupModRef, CPupMod}
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent}
import java.util
import cpw.mods.fml.common.network.{NetworkRegistry, FMLEmbeddedChannel}
import cpw.mods.fml.relauncher.{SideOnly, Side}
import io.netty.handler.codec.MessageToMessageCodec
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import io.netty.channel.{ChannelHandlerContext, ChannelHandler}
import io.netty.buffer.{ByteBuf, Unpooled}
import java.lang.reflect.Constructor
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.network.NetHandlerPlayServer
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer

@ChannelHandler.Sharable
trait CPupNetwork[MOD <: CPupMod[_ <: CPupModRef, MSG], MSG <: CPupMessage] extends MessageToMessageCodec[FMLProxyPacket, MSG] with ModLifecycleHandler {
	def mod: MOD

	var channels: util.EnumMap[Side, FMLEmbeddedChannel]
	var finished = false
	protected var messages = List[Class[_ <: MSG]]()

	def register {
		channels = NetworkRegistry.INSTANCE.newChannel(mod.ref.modID, this)
	}

	def finish {
		if(finished) {
			return
		}

		finished = true

		messages = messages.sortBy(_.getCanonicalName)
	}

	override def init(e: FMLInitializationEvent) { register }
	override def postInit(e: FMLPostInitializationEvent) { finish }

	def handleMessage(msg: MSG, player: EntityPlayer) {
		if(msg.isInstanceOf[TEMsg]) {

		}
	}

	def register(cla: Class[_ <: MSG]): Boolean = {
		if(finished) {
			throw new Exception("Attempt to register a message after post initialization: " + cla.getCanonicalName)
			return false
		}

		if(messages.size > 256) {
			throw new ArrayIndexOutOfBoundsException("More than 256 messages for a single network")
			return false
		}

		if(messages.contains(cla)) {
			mod.logger.warn("Attempt to reregister message: " + cla.getCanonicalName)
			return false
		}

		if(cla.getConstructors.find((c: Constructor[_]) => {
			val params = c.getParameterTypes
			params.size == 3 && params(0) == classOf[ChannelHandlerContext] && params(1) == classOf[ByteBuf] && params(2) == classOf[EntityPlayer]
		}).isEmpty) {
			throw new NullPointerException("Attempt to register unreadable message type: " + cla.getCanonicalName)
		}

		messages ::= cla

		true
	}

	@Override
	def encode(ctx: ChannelHandlerContext, msg: MSG, out: util.List[Object]) {
		val buffer = Unpooled.buffer
		val cla = msg.getClass
		if(!messages.contains(cla)) {
			throw new NullPointerException("Attempt to send unregistered message: " + cla.getCanonicalName)
		}
		val id = messages.indexOf(cla)
		buffer.writeByte(id)
		msg.writeTo(ctx, buffer)
		val proxyPacket = new FMLProxyPacket(buffer.copy, ctx.channel.attr(NetworkRegistry.FML_CHANNEL).get)
		out.add(proxyPacket)
	}

	@Override
	def decode(ctx: ChannelHandlerContext, proxyMsg: FMLProxyPacket, out: util.List[Object]) {
		val payload = proxyMsg.payload
		val id = payload.readByte
		val cla = messages(id)
		if(cla == null) {
			mod.logger.warn("Unknown message id: " + id)
			return
		}

		val player = FMLCommonHandler.instance.getEffectiveSide match {
			case Side.CLIENT => getClientPlayer
			case Side.SERVER => ctx.channel.attr(NetworkRegistry.NET_HANDLER).get.asInstanceOf[NetHandlerPlayServer].playerEntity
			case _ => null
		}

		val msg = cla.getConstructor(classOf[ChannelHandlerContext], classOf[ByteBuf], classOf[EntityPlayer]).newInstance(ctx, payload.slice, player)

		handleMessage(msg, player)

	}

	@SideOnly(Side.CLIENT)
	protected def getClientPlayer = Minecraft.getMinecraft.thePlayer
}