package cpup.mc.lib.util

import net.minecraft.world.{World, WorldSavedData}
import cpup.mc.lib.mod.CPupLib

object WorldSavedDataUtil {
	def get[DATA <: WorldSavedData](world: World, cla: Class[DATA], id: String) = world.loadItemData(cla, id).asInstanceOf[DATA]
	def getOrCreate[DATA <: WorldSavedData](world: World, cla: Class[DATA], id: String) = {
		var data = get(world, cla, id)
		if(data == null) {
			try {
				data = cla.getConstructor(classOf[String]).newInstance(id)
				world.setItemData(id, data)
			} catch {
				case e: Exception =>
					CPupLib.logger.error(e)
					CPupLib.logger.error(e.getStackTraceString)
			}
		}
		data
	}
}