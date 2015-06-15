package cpup.mc.lib.network

import java.lang.reflect.Constructor

import cpup.mc.lib.network.CPupNetwork.Message
import cpup.mc.lib.{ModLifecycleHandler, CPupModRef, CPupMod}
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.{FMLPreInitializationEvent, FMLPostInitializationEvent, FMLInitializationEvent}
import java.util
import cpw.mods.fml.common.network.simpleimpl.{MessageContext, IMessage, IMessageHandler, SimpleNetworkWrapper}
import cpw.mods.fml.common.network.{FMLOutboundHandler, NetworkRegistry, FMLEmbeddedChannel}
import cpw.mods.fml.relauncher.{SideOnly, Side}
import io.netty.buffer.{EmptyByteBuf, Unpooled, ByteBuf}
import io.netty.handler.codec.MessageToMessageCodec
import cpw.mods.fml.common.network.internal.FMLProxyPacket
import io.netty.channel.{ChannelHandlerContext, ChannelHandler}
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.{EntityPlayerMP, EntityPlayer}
import cpup.mc.lib.content.CPupBlock
import net.minecraft.network.{PacketBuffer, NetHandlerPlayServer}

// TODO: coordinate the message ids (so both sides don't have to have the exact same setup)

class CPupNetwork[MOD <: CPupMod[_ <: CPupModRef]](val mod: MOD, val messages: Map[Class[_ <: CPupMessage[MOD]], (CPupMessage[MOD]) => Option[CPupMessage[MOD]]]) extends ModLifecycleHandler with IMessageHandler[CPupNetwork.Message, CPupNetwork.Message] {
	if(messages.size > 256) {
		throw new ArrayIndexOutOfBoundsException("More than 256 messages for a single network")
	}

	val messageClas = messages.keys.toList

	for(cla <- messageClas) {
		cla.getDeclaredConstructor(classOf[EntityPlayer], classOf[PacketBuffer])
	}

	var network: SimpleNetworkWrapper = null
	override def preInit(e: FMLPreInitializationEvent) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel(mod.ref.modID)
		network.registerMessage[CPupNetwork.Message, CPupNetwork.Message](this, classOf[CPupNetwork.Message], 0, Side.SERVER)
		network.registerMessage[CPupNetwork.Message, CPupNetwork.Message](this, classOf[CPupNetwork.Message], 0, Side.CLIENT)
	}

	@SideOnly(Side.CLIENT)
	protected def getClientPlayer = Minecraft.getMinecraft.thePlayer

	def sendToServer(msg: CPupMessage[MOD]) = {
		network.sendToServer(new CPupNetwork.Message(this, msg))
		this
	}

	def sendToAll(msg: CPupMessage[MOD]) = {
		network.sendToAll(new CPupNetwork.Message(this, msg))
		this
	}

	def sendTo(player: EntityPlayerMP, msg: CPupMessage[MOD]) = {
		network.sendTo(new CPupNetwork.Message(this, msg), player)
		this
	}

	override def onMessage(message: CPupNetwork.Message, ctx: MessageContext): CPupNetwork.Message = {
		val player = if(ctx.side.isClient) Minecraft.getMinecraft.thePlayer else ctx.getServerHandler.playerEntity
		val msg = message.construct(this, player).asInstanceOf[CPupMessage[MOD]]
		messages(msg.getClass.asInstanceOf[Class[CPupMessage[MOD]]])(msg).map(new CPupNetwork.Message(this, _)).orNull
	}
}

object CPupNetwork {
	class Message(var id: Byte, var data: PacketBuffer = new PacketBuffer(Unpooled.buffer)) extends IMessage {
		def this(network: CPupNetwork[_ <: CPupMod[_ <: CPupModRef]], msg: CPupMessage[_ <: CPupMod[_ <: CPupModRef]]) {
			this({
				val n = network.messageClas.indexOf(msg.getClass)
				(if(n > 127) -n + 127 else n).toByte
			}, {
				val buf = new PacketBuffer(Unpooled.buffer)
				msg.writeTo(buf)
				buf
			})
		}
		def this() { this(0) }

		def construct(net: CPupNetwork[_ <: CPupMod[_ <: CPupModRef]], player: EntityPlayer): CPupMessage[_ <: CPupMod[_ <: CPupModRef]] = {
			net.messageClas(id).getConstructor(classOf[EntityPlayer], classOf[PacketBuffer]).newInstance(player, data)
		}

		override def toBytes(buf: ByteBuf) {
			buf.writeByte(id)
			buf.writeBytes(data)
		}
		override def fromBytes(buf: ByteBuf) {
			id = buf.readByte
			data = new PacketBuffer(buf.slice)
		}
	}
}
