package cpup.mc.lib.network

import cpup.mc.lib.network.CPupNetwork.Message
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraft.entity.player.EntityPlayerMP

trait Context {
	def send(network: SimpleNetworkWrapper, msg: Message)
}
object Context {
	case object Server extends Context {
		override def send(network: SimpleNetworkWrapper, msg: Message) {
			network.sendToServer(msg)
		}
	}

	case object AllPlayers extends Context {
		override def send(network: SimpleNetworkWrapper, msg: Message) {
			network.sendToAll(msg)
		}
	}

	case class PlayersAround(dim: Int, x: Double, y: Double, z: Double, range: Double = {
		FMLCommonHandler.instance.getMinecraftServerInstance.getPlayerList.getViewDistance * 16
	}) extends Context {
		override def send(network: SimpleNetworkWrapper, msg: Message) {
			network.sendToAllAround(msg, new NetworkRegistry.TargetPoint(dim, x, y, z, range))
		}
	}

	case class Player(player: EntityPlayerMP) extends Context {
		override def send(network: SimpleNetworkWrapper, msg: Message) {
			network.sendTo(msg, player)
		}
	}

	case class Dimension(dim: Int) extends Context {
		override def send(network: SimpleNetworkWrapper, msg: Message) {
			network.sendToDimension(msg, dim)
		}
	}
}
