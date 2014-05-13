package cpup.mc.lib.mod

import cpup.mc.lib.CPupMod
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.Mod.EventHandler
import cpup.mc.lib.targeting.{BlockTarget, EntityTarget, PlayerTarget, TargetingRegistry}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.Entity
import net.minecraft.block.Block
import cpup.mc.lib.util.pos.BlockPos
import cpup.mc.lib.intermod.baubles.BaublesI

@Mod(modid = Ref.modID, modLanguage = "scala")
object CPupLib extends CPupMod[TRef] {
	def ref = Ref

	registerLifecycleHandler(BaublesI)

	@EventHandler
	override def preInit(e: FMLPreInitializationEvent) {
		super.preInit(e)
		TargetingRegistry.registerEntityTarget(classOf[EntityPlayer], (player: EntityPlayer) => PlayerTarget(player.getCommandSenderName))
		TargetingRegistry.registerEntityTarget(classOf[Entity], (ent: Entity) => EntityTarget(ent))
		TargetingRegistry.registerBlockTarget(classOf[Block], (blk: Block, pos: BlockPos) => BlockTarget(pos))
	}
}