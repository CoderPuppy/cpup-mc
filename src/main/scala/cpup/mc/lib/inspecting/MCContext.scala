package cpup.mc.lib.inspecting

import cpup.mc.lib.util.Direction
import net.{minecraft => mc}

object MCContext {
	trait World {
		def world: mc.world.World
	}

	trait Side {
		def side: Direction
	}

	trait BlockPos extends World {
		def world: mc.world.World
		def x: Int
		def y: Int
		def z: Int
	}

	trait Player {
		def player: mc.entity.player.EntityPlayer
	}

	trait Entity {
		def entity: mc.entity.Entity
	}
}
