package cpup.mc.lib.inspecting

import cpup.lib.inspecting.Context
import cpup.mc.lib.util.{BlockPos, Direction, Side}
import cpw.mods.fml.common.registry.GameData
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer

object Inspectors {
	Registry.register[Block]((blk: Block, ctx: Context) => {
		for {
			pos <- ctx[BlockPos]()
		} yield {
			val tbl = Data.Table(
				"regID" -> Data.String(GameData.getBlockRegistry.getNameForObject(blk)),
				"unlocalizedName" -> Data.String(blk.getUnlocalizedName),
				"meta" -> Data.Int(pos.world.getBlockMetadata(pos.x, pos.y, pos.z))
			)
			pos.world.getTileEntity(pos.x, pos.y, pos.z) match {
				case null => tbl
				case te => tbl + ("te" -> Registry.inspect_(te, ctx))
			}
		}
	})

	Registry.register[Any]((obj: Any, ctx: Context) => {
		for {
			pos <- ctx[BlockPos]()
		} yield Data.Table(
			"world" -> Data.Link("minecraft:world", Data.Int(pos.world.provider.dimensionId)),
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
					case Side.SERVER => Some(MinecraftServer.getServer.worldServerForDimension(dim))
					case _ => None
				}) match {
					case Some(_world) => Option(_world.getBlock(_x, _y, _z)).map(blk => {
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
					case Side.SERVER => Some(MinecraftServer.getServer.worldServerForDimension(dim))
					case _ => None
				}) match {
					case Some(_world) => Option(_world.getBlock(_x, _y, _z)).map(blk => {
						Registry.inspect_(blk, new Context().withF(BlockPos(_world, _x, _y, _z)).withF(Direction.fromSide(_side), 'direction))
					})
					case None => None
				}
			}
			case _ => None
		}
	})
}
