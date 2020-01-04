package cpup.mc.lib.inspecting

import cpup.mc.lib.network.CPupMessage
import cpup.mc.lib.util.Side
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.PacketBuffer

class RequestMessage(val typ: String, val id: List[Data]) extends CPupMessage[AnyRef] {
	def this(player: EntityPlayer, buf: PacketBuffer, data: AnyRef) = {
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

	override def handle(data: AnyRef) = {
		if(Side.effective.isServer)
			Some(new ResponseMessage((typ, id).hashCode, Registry.get(typ, id: _*)))
		else
			None
	}
}
