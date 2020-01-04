package cpup.mc.lib.inspecting

import cpup.lib.inspecting.Context
import cpup.lib.reflect.ReflectUtil
import cpup.mc.lib.util.{BlockPos, Direction, Side}
import net.minecraft.util.EnumFacing
import net.minecraftforge.fml.common.registry.GameData
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.server.FMLServerHandler

import scala.collection.JavaConversions

object Inspectors {
	Registry.register[Block]((blk: Block, ctx: Context) => {
		for {
			pos <- ctx[BlockPos]()
		} yield {
			val regID = GameData.getBlockRegistry.getNameForObject(blk)
			val state = pos.world.getBlockState(pos.mc)
			val r = JavaConversions.mapAsScalaMap(state.getProperties).map { kv ⇒
				kv._1.getName → Data.Table(
					"label" → Data.String(kv._1.getName(kv._2.asInstanceOf)),
					"value" → Registry.inspect(kv._2, new Context(), ReflectUtil.tpe(kv._1.getValueClass))
				)
			}
			val tbl = Data.Table(
				"regID" -> Data.Table(
					"domain" → Data.String(regID.getResourceDomain),
					"path" → Data.String(regID.getResourcePath)
				),
				"unlocalizedName" -> Data.String(blk.getUnlocalizedName)
//				"state" -> Data.Int(pos.world.getBlockState(pos.mc))
			)
			pos.world.getTileEntity(pos.mc) match {
				case null => tbl
				case te => tbl + ("te" -> Registry.inspect_(te, ctx))
			}
		}
	})

	Registry.register[Any]((obj: Any, ctx: Context) => {
		for {
			pos <- ctx[BlockPos]()
		} yield Data.Table(
			"world" -> Data.Link("minecraft:world", Data.Int(pos.world.provider.getDimension)),
			"x" -> Data.Int(pos.x),
			"y" -> Data.Int(pos.y),
			"z" -> Data.Int(pos.z)
		)
	})

	Registry.register("minecraft:block", args => {
		args match {
			case Seq(dim: Int, _x: Int, _y: Int, _z: Int) => {
				(Side.effective match {
					case Side.CLIENT if Minecraft.getMinecraft.thePlayer.dimension == dim => Some(Minecraft.getMinecraft.theWorld)
					case Side.SERVER => Some(FMLServerHandler.instance.getServer.worldServerForDimension(dim))
					case _ => None
				}) match {
					case Some(_world) => Option(_world.getBlockState(new BlockPos.MC(_x, _y, _z))).map(blk => {
						Registry.inspect_(blk, new Context().withF(BlockPos(_world, _x, _y, _z)))
					})
					case None => None
				}
			}
			case _ => None
		}
	})

	Registry.register("minecraft:block:side", args => {
		args match {
			case Seq(dim: Int, _x: Int, _y: Int, _z: Int, _side: Int) if Direction.fromSide(_side) != null => {
				(Side.effective match {
					case Side.CLIENT if Minecraft.getMinecraft.thePlayer.dimension == dim => Some(Minecraft.getMinecraft.theWorld)
					case Side.SERVER => Some(FMLServerHandler.instance.getServer.worldServerForDimension(dim))
					case _ => None
				}) match {
					case Some(_world) => Option(_world.getBlockState(new BlockPos.MC(_x, _y, _z))).map(blk => {
						Registry.inspect_(blk,
							new Context()
								.withF(BlockPos(_world, _x, _y, _z))
								.withF[EnumFacing](Direction.fromSide(_side), 'side)
						)
					})
					case None => None
				}
			}
			case _ => None
		}
	})
}
