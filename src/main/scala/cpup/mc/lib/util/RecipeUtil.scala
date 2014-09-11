package cpup.mc.lib.util

import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.{ShapelessOreRecipe, ShapedOreRecipe}

object RecipeUtil {
	def shaped(result: ItemStack, recipe: Array[String], parts: Any*) = new ShapedOreRecipe(
		result,
		(Array(recipe) ++ parts.map(_ match {
			case c: Char => Character.valueOf(c)
			case v: Any => v
		})).toSeq.toArray.asInstanceOf[Array[Object]]: _*
	)

	def shapeless(result: ItemStack, parts: Object*) = new ShapelessOreRecipe(result, parts: _*)
}