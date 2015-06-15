package cpup.mc.lib.inspecting

import java.nio.charset.Charset
import java.util

import scala.collection.JavaConversions

import cpup.lib.inspecting
import cpup.mc.lib.util.NBTUtil
import io.netty.buffer.ByteBuf

import net.minecraft.nbt._

object Registry extends inspecting.Registry {
	def toNBT(data: Data): NBTBase = data match {
		case tbl: Data.Table => {
			val tag = new NBTTagCompound
			for((k, v) <- tbl) {
				tag.setTag(k, toNBT(v))
			}
			tag
		}
		case list: Data.List => {
			val tag = new NBTTagList
			for(el <- list) {
				tag.appendTag(toNBT(el))
			}
			tag
		}
		case Data.Byte(v) => new NBTTagByte(v)
		case Data.Double(v) => new NBTTagDouble(v)
		case Data.Float(v) => new NBTTagFloat(v)
		case Data.Int(v) => new NBTTagInt(v)
		case Data.Long(v) => new NBTTagLong(v)
		case Data.Short(v) => new NBTTagShort(v)
		case Data.String(v) => new NBTTagString(v)
		case link: Data.Link => toNBT(link.follow.left.get)
		case Data.Nil => null // this'll break NBTTagList
	}
	def fromNBT(nbt: NBTBase): Data = nbt match {
		case v: NBTTagCompound => {
			Data.Table(JavaConversions.asScalaSet(v.func_150296_c.asInstanceOf[util.Set[String]]).map(k => k -> fromNBT(v.getTag(k))).toSeq: _*)
		}
		case v: NBTTagList => Data.List(NBTUtil.readList(v).map(fromNBT): _*)
		case v: NBTTagByteArray => Data.List(v.func_150292_c.map(Data.Byte): _*)
		case v: NBTTagByte => Data.Byte(v.func_150290_f)
		case v: NBTTagDouble => Data.Double(v.func_150286_g)
		case v: NBTTagFloat => Data.Float(v.func_150288_h)
		case v: NBTTagInt => Data.Int(v.func_150287_d)
		case v: NBTTagIntArray => Data.List(v.func_150302_c.map(Data.Int): _*)
		case v: NBTTagLong => Data.Long(v.func_150291_c)
		case v: NBTTagShort => Data.Short(v.func_150289_e)
		case v: NBTTagString => Data.String(v.func_150285_a_)
	}

	val charset = Charset.forName("UTF-16LE")
	val ids = Array(
		Data.Nil.getClass,
		classOf[Data.Byte],
		classOf[Data.Double],
		classOf[Data.Float],
		classOf[Data.Int],
		classOf[Data.Long],
		classOf[Data.Short],
		classOf[Data.String],
		classOf[Data.Table],
		classOf[Data.List],
		classOf[Data.Link]
	)
	def id(data: Data) = ids.indexOf(data.getClass).toByte
	def writeToByteBuf(data: Data, buf: ByteBuf) {
		buf.writeByte(id(data))
		data match {
			case Data.Nil =>
			case Data.Byte(v) => buf.writeByte(v)
			case Data.Double(v) => buf.writeDouble(v)
			case Data.Float(v) => buf.writeFloat(v)
			case Data.Int(v) => buf.writeInt(v)
			case Data.Long(v) => buf.writeLong(v)
			case Data.Short(v) => buf.writeShort(v)
			case Data.String(v) =>
				val bytes = v.getBytes(charset)
				buf.writeInt(bytes.length)
				buf.writeBytes(bytes)
			case tbl: Data.Table =>
				buf.writeInt(tbl.size)
				for(kv <- tbl) {
					val bytes = kv._1.getBytes(charset)
					buf.writeInt(bytes.length)
					buf.writeBytes(bytes)
					writeToByteBuf(kv._2, buf)
				}
			case list: Data.List =>
				buf.writeInt(list.size)
				for(el <- list) {
					writeToByteBuf(el, buf)
				}
			case link: Data.Link =>
				val bytes = link.typ.getBytes(charset)
				buf.writeInt(bytes.length)
				buf.writeBytes(bytes)
				buf.writeInt(link.id.size)
				for(id <- link.id) writeToByteBuf(id, buf)
		}
	}
	def readFromByteBuf(buf: ByteBuf): Data = {
		val cla = ids(buf.readByte)
		if(cla == Data.Nil.getClass) Data.Nil
		else if(cla == classOf[Data.Byte]) Data.Byte(buf.readByte)
		else if(cla == classOf[Data.Double]) Data.Double(buf.readDouble)
		else if(cla == classOf[Data.Float]) Data.Float(buf.readFloat)
		else if(cla == classOf[Data.Int]) Data.Int(buf.readInt)
		else if(cla == classOf[Data.Long]) Data.Long(buf.readLong)
		else if(cla == classOf[Data.Short]) Data.Short(buf.readShort)
		else if(cla == classOf[Data.String]) Data.String(buf.readBytes(buf.readInt).toString(charset))
		else if(cla == classOf[Data.Table]) {
			Data.Table((0 until buf.readInt).map(i => buf.readBytes(buf.readInt).toString(charset) -> readFromByteBuf(buf)): _*)
		} else if(cla == classOf[Data.List]) {
			Data.List((0 until buf.readInt).map(i => readFromByteBuf(buf)): _*)
		} else if(cla == classOf[Data.Link]) {
			Data.Link(buf.readBytes(buf.readInt).toString(charset), (0 until buf.readInt).map(i => readFromByteBuf(buf)): _*)
		} else throw new RuntimeException("bad class")
	}

	Inspectors
}
