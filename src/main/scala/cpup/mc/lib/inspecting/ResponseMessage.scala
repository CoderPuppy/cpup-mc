package cpup.mc.lib.inspecting

import cpup.mc.lib.mod.CPupLib
import cpup.mc.lib.network.CPupMessage
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.PacketBuffer

class ResponseMessage(val call: Int, val data: Either[Data, String]) extends CPupMessage[AnyRef] {
	def this(player: EntityPlayer, buf: PacketBuffer, data: AnyRef) = {
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

	override def handle(_data: AnyRef) = {
		Request._requests.get(call) match {
			case Some(req) => req.update(data)
			case None => CPupLib.logger.warn("request disappeared before response", call, data)
		}
		None
	}
}
