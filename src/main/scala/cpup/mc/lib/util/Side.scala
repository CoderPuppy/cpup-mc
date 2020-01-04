package cpup.mc.lib.util

import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher

object Side {
	final val CLIENT = relauncher.Side.CLIENT
	final val SERVER = relauncher.Side.SERVER
	def effective = FMLCommonHandler.instance.getEffectiveSide
	def code = FMLCommonHandler.instance.getSide

	type T = relauncher.Side
}
