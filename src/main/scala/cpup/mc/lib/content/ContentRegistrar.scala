package cpup.mc.lib.content

import cpup.mc.lib.{CPupModRef, CPupMod}
import net.minecraft.item.ItemBlock
import net.minecraft.item.crafting.IRecipe

trait ContentRegistrar[MOD <: CPupMod[_ <: CPupModRef]] {
	def mod: MOD

	def registerBlock(block: CPupBlock[MOD]) {
		registerBlock(block, classOf[GenericItemBlock[MOD]])
	}
	def registerBlock(block: CPupBlock[MOD], item: Class[_ <: ItemBlock], args: Object*)
	def registerTileEntity(cla: Class[_ <: CPupTE[MOD]], id: String)

	def registerItem(item: CPupItem[MOD])

	def registerRecipe(recipe: IRecipe)
}