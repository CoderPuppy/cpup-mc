package cpup.mc.lib.content

import cpup.mc.lib.{CPupMod, CPupModHolder, CPupModRef}
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World

trait CPupRecipe extends IRecipe { self: CPupModHolder[_ <: CPupMod[_ <: CPupModRef]] =>
	override def getRecipeOutput = null
}

object CPupRecipe {
	trait Shaped[IR] extends CPupRecipe { self: CPupModHolder[_ <: CPupMod[_ <: CPupModRef]] =>
		def width: Int
		def height: Int

		override def getRecipeSize = width * height

		def parse(inv: InventoryCrafting, ox: Int, oy: Int, data: Array[Array[ItemStack]]): Option[IR]
		def result(ir: IR): ItemStack

		private def parse(inv: InventoryCrafting): Option[IR] = {
			for {
				ox <- 0 to 2 - width + 1
				oy <- 0 to 2 - height + 1
			} {
				val ox = 0
				val oy = 0
				val data = Array.ofDim[ItemStack](width, height)
				for {
					x <- 0 to width
					y <- 0 to height
				} {
					data(x)(y) = inv.getStackInRowAndColumn(ox + x, oy + y)
				}
				parse(inv, ox, oy, data) match {
					case Some(ir) =>
						return Some(ir)
					case None =>
				}
			}
			None
		}

		override def matches(inv: InventoryCrafting, world: World) = parse(inv).isDefined
		override def getCraftingResult(inv: InventoryCrafting): ItemStack = parse(inv).map(result).get
	}
}