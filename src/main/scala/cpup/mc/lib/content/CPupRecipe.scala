package cpup.mc.lib.content

import cpup.mc.lib.{CPupModRef, CPupMod}
import net.minecraft.item.crafting.IRecipe
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.world.World
import javax.vecmath.Matrix3f

trait CPupRecipe[MOD <: CPupMod[_ <: CPupModRef]] extends IRecipe {
	override def getRecipeOutput = null

//	def width: Int
//	def height: Int
//
//	def test(inv: InventoryCrafting, world: World, ox: Int, oy: Int, data: Matrix3f)
//
//	override def matches(inv: InventoryCrafting, world: World) = {
//		for {
//			ox <- 0 to (2 - width)
//			oy <- 0 to (2 - height)
//		} {
//			val data = new Matrix3f()
//			for {
//				x <- 0 to width
//				y <- 0 to height
//			} {
//				data.setElement(x, y, inv.getStackInRowAndColumn(ox + x, oy + y))
//			}
//		}
//	}
}