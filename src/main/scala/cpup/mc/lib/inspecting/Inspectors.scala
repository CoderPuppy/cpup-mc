package cpup.mc.lib.inspecting

import cpup.mc.lib.util.{Direction, Side}
import cpw.mods.fml.common.registry.GameData
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer
import net.minecraft.world.World

object Inspectors {
	Registry.register[Block, MCContext.BlockPos]((blk: Block, ctx: MCContext.BlockPos) => {
		val tbl = Data.Table(
			"regID" -> Data.String(GameData.getBlockRegistry.getNameForObject(blk)),
			"unlocalizedName" -> Data.String(blk.getUnlocalizedName),
			"meta" -> Data.Int(ctx.world.getBlockMetadata(ctx.x, ctx.y, ctx.z))
		)
		Some(ctx.world.getTileEntity(ctx.x, ctx.y, ctx.z) match {
			case null => tbl
			case te => tbl + ("te" -> Registry.inspect_(te, ctx))
		})
	})

	Registry.register[Any, MCContext.BlockPos]((obj: Any, ctx: MCContext.BlockPos) => {
		Some(Data.Table(
			"world" -> Data.Link("minecraft:world", Data.Int(ctx.world.provider.dimensionId)),
			"x" -> Data.Int(ctx.x),
			"y" -> Data.Int(ctx.y),
			"z" -> Data.Int(ctx.z)
		))
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
						Registry.inspect_(blk, new Object with MCContext.BlockPos {
							def world = _world
							def x = _x
							def y = _y
							def z = _z
						})
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
						Registry.inspect_(blk, new Object with MCContext.BlockPos with MCContext.Side {
							def world = _world
							def x = _x
							def y = _y
							def z = _z
							def side = Direction.fromSide(_side)
						})
					})
					case None => None
				}
			}
			case _ => None
		}
	})
}
