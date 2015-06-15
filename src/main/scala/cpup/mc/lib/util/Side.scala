package cpup.mc.lib.util

import cpw.mods.fml.relauncher
import cpw.mods.fml.common.FMLCommonHandler

object Side {
	final val CLIENT = relauncher.Side.CLIENT
	final val SERVER = relauncher.Side.SERVER
	def effective = FMLCommonHandler.instance.getEffectiveSide
	def code = FMLCommonHandler.instance.getSide

	type T = relauncher.Side
}
