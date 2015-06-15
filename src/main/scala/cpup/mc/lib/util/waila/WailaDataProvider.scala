package cpup.mc.lib.util.waila

import java.util

import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

trait WailaDataProvider extends IWailaDataProvider {
	override def getWailaHead(stack: ItemStack, data: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler): util.List[String] = data
	override def getWailaBody(stack: ItemStack, data: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler): util.List[String] = data
	override def getWailaTail(stack: ItemStack, data: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler): util.List[String] = data
	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) = accessor.getStack
	override def getNBTData(player: EntityPlayerMP, te: TileEntity, tag: NBTTagCompound, world: World, x: Int, y: Int, z: Int) = {
		if(te != null)
			te.writeToNBT(tag)
		tag
	}
}
