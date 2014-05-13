package cpup.mc.lib.targeting

import cpup.mc.lib.mod.CPupLib
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer
import net.minecraft.nbt.NBTTagCompound
import cpup.mc.lib.inventory.EmptyInventory
import net.minecraft.entity.player.EntityPlayer
import cpup.mc.lib.intermod.baubles.BaublesI

case class PlayerTarget(name: String) extends TTarget with TTargetWrapper {
	def mod = CPupLib

	override def targetType = PlayerTarget

	def entity = wrapped.flatMap(_.obj).flatMap(_.left.toOption).map(_.asInstanceOf[EntityPlayer])

	override def isValid = FMLCommonHandler.instance.getEffectiveSide match {
		case Side.CLIENT => Minecraft.getMinecraft.thePlayer.getCommandSenderName == name
		case Side.SERVER => MinecraftServer.getServer.getConfigurationManager.getPlayerForUsername(name) != null
		case _ => false
	}

	override def getActiveItems = entity.map((player) =>
		// TODO: TCon?
		Array(
			player.inventory.armorInventory,
			player.inventory.mainInventory,
			BaublesI.get.getItems(player).toArray
		).flatten
	).getOrElse(Array())
	override def getActiveInventory = entity.map(_.inventory).getOrElse(EmptyInventory)

	override def writeToNBT(nbt: NBTTagCompound) {
		nbt.setString("name", name)
	}

	override def wrapped = (FMLCommonHandler.instance.getEffectiveSide match {
		case Side.CLIENT =>
			val player = Minecraft.getMinecraft.thePlayer
			if(player.getCommandSenderName != name) {
				throw new Exception(s"who is this: $name")
				None
			}
			Some(player)

		case Side.SERVER =>
			Some(MinecraftServer.getServer.getConfigurationManager.getPlayerForUsername(name))

		case _ => None
	}).map(EntityTarget(_))
}

object PlayerTarget extends TTargetType {
	def mod = CPupLib

	override def name = s"${mod.ref.modID}:player"
	override def targetClass = classOf[PlayerTarget]

	override def readFromNBT(nbt: NBTTagCompound) = Some(PlayerTarget(nbt.getString("name")))
}