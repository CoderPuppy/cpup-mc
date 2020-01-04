package cpup.mc.lib.network

import java.nio.charset.Charset

import cpup.mc.lib.network.CPupNetwork.Message
import cpup.mc.lib.util.Side
import cpup.mc.lib.{CPupMod, CPupModRef, ModLifecycleHandler}
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext, SimpleNetworkWrapper}
import net.minecraftforge.fml.relauncher.SideOnly
import io.netty.buffer.{ByteBuf, Unpooled}
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.PacketBuffer

// TODO: coordinate the message ids (so both sides don't have to have the exact same setup)

class CPupNetwork[DATA <: AnyRef](val name: String, val data: DATA, val handler: (CPupNetwork[DATA]) => (Context, CPupNetwork.Message) => Any, val messages: Set[Class[_ <: CPupMessage[_ >: DATA <: AnyRef]]])(implicit val manifest: Manifest[DATA]) {
	if(messages.size > 256) {
		throw new ArrayIndexOutOfBoundsException("More than 256 messages for a single network")
	}
	val messageClas = messages.toList

	for(cla <- messageClas) {
		cla.getDeclaredConstructor(classOf[EntityPlayer], classOf[PacketBuffer], manifest.runtimeClass)
	}

	val send = handler(this)

	@SideOnly(Side.CLIENT)
	protected def getClientPlayer = Minecraft.getMinecraft.thePlayer

	def send(ctx: Context, msg: CPupMessage[DATA]) {
		println(s"sending $msg from ${Side.effective} on $name")
		send(ctx, new CPupNetwork.Message(this, msg))
	}

	def handle(player: EntityPlayer, msg: CPupNetwork.Message) = {
		println(s"got $msg on ${Side.effective} on $name")
		msg.construct(this, player).handle(data).map(m => new Message(this, m.asInstanceOf[CPupMessage[AnyRef]]))
	}
}

object CPupNetwork {
	val charset = Charset.forName("UTF-8")

	def simpleNetwork[MOD <: CPupMod[_ <: CPupModRef]](mod: MOD)(network: CPupNetwork[_]) = {
		val _net = new SimpleNetwork[MOD](mod)
		mod.registerLifecycleHandler(_net)
		val handler = new _net.Handler(network)
		mod.registerLifecycleHandler(handler)
		(ctx: Context, msg: Message) => _net.send(ctx, msg)
	}

	class SimpleNetwork[MOD <: CPupMod[_ <: CPupModRef]](mod: MOD) extends ModLifecycleHandler {
		var network: SimpleNetworkWrapper = null
		override def preInit(e: FMLPreInitializationEvent) {
			network = NetworkRegistry.INSTANCE.newSimpleChannel(mod.ref.modID)
		}

		def send(ctx: Context, msg: Message) {
			ctx.send(network, msg)
		}

		class Handler(val cnetwork: CPupNetwork[_]) extends IMessageHandler[Message, Message] with ModLifecycleHandler {
			override def preInit(e: FMLPreInitializationEvent) {
				network.registerMessage[Message, Message](this, classOf[Message], 0, Side.SERVER)
				network.registerMessage[Message, Message](this, classOf[Message], 0, Side.CLIENT)
			}

			override def onMessage(message: CPupNetwork.Message, ctx: MessageContext): CPupNetwork.Message = {
				val player = if(ctx.side.isClient) Minecraft.getMinecraft.thePlayer else ctx.getServerHandler.playerEntity
				cnetwork.handle(player, message).orNull
			}
		}
	}

	class Message(var id: Byte, var data: PacketBuffer = new PacketBuffer(Unpooled.buffer)) extends IMessage {
		def this(network: CPupNetwork[_ <: AnyRef], msg: CPupMessage[_ <: AnyRef]) {
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

		def construct[DATA <: AnyRef](net: CPupNetwork[DATA], player: EntityPlayer): CPupMessage[_ >: DATA <: AnyRef] = {
			net.messageClas(id).getConstructor(classOf[EntityPlayer], classOf[PacketBuffer], net.manifest.runtimeClass).newInstance(player, data, net.data)
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
