package cpup.mc.lib.inspecting

import java.nio.charset.Charset

import cpup.mc.lib.util.Side
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.PacketBuffer

class RequestMessage(val typ: String, val id: List[Data]) extends BaseMessage {
	def this(player: EntityPlayer, buf: PacketBuffer) = {
		this(
			buf.readBytes(buf.readInt).toString(Registry.charset),
			(0 until buf.readInt).map(v => Registry.readFromByteBuf(buf)).toList
		)
	}

	override def writeTo(buf: PacketBuffer) {
		val bytes = typ.getBytes(Registry.charset)
		buf.writeInt(bytes.length)
		buf.writeBytes(bytes)
		buf.writeInt(id.size)
		for(data <- id) Registry.writeToByteBuf(data, buf)
	}
}

object RequestMessage {
	def handler(msg: RequestMessage) = {
		if(Side.effective == Side.SERVER)
			Some(new ResponseMessage((msg.typ, msg.id).hashCode, Registry.get(msg.typ, msg.id: _*)))
		else
			None
	}
}
