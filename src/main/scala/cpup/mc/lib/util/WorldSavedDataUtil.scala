package cpup.mc.lib.util

import net.minecraft.world.{World, WorldSavedData}

object WorldSavedDataUtil {
	def get[DATA <: WorldSavedData](world: World, cla: Class[DATA], id: String) = world.loadItemData(cla, id).asInstanceOf[DATA]
}