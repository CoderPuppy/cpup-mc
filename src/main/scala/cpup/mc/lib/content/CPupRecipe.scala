package cpup.mc.lib.content

import cpup.mc.lib.{CPupModRef, CPupMod}
import net.minecraft.item.crafting.IRecipe

trait CPupRecipe[MOD <: CPupMod[_ <: CPupModRef]] extends IRecipe {
	override def getRecipeOutput = null
}