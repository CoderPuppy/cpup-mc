package cpup.mc.lib.inspecting

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.PacketBuffer

class ResponseMessage(val call: Int, val data: Either[Data, String]) extends BaseMessage {
	def this(player: EntityPlayer, buf: PacketBuffer) = {
		this(
			buf.readInt,
			{
				val len = buf.readInt
				if(len < 0) Left(Registry.readFromByteBuf(buf))
				else Right(buf.readBytes(len).toString(Registry.charset))
			}
		)
	}

	override def writeTo(buf: PacketBuffer) {
		buf.writeInt(call)
		data match {
			case Left(data) =>
				buf.writeInt(-1)
				Registry.writeToByteBuf(data, buf)
			case Right(err) =>
				val bytes = err.getBytes(Registry.charset)
				buf.writeInt(bytes.length)
				buf.writeBytes(bytes)
		}
	}
}

object ResponseMessage {
	def handler(msg: ResponseMessage) = {
		Request._requests.get(msg.call) match {
			case Some(req) => req.update(msg.data)
			case None => println("request disappeared before response", msg.call, msg.data)
		}
		None
	}
}
