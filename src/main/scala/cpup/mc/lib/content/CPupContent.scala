package cpup.mc.lib.content

import cpw.mods.fml.common.registry.GameRegistry
import cpup.mc.lib.{CPupModRef, CPupMod}
import scala.collection.mutable
import net.minecraft.item.{Item, ItemStack, ItemBlock}
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.oredict.{ShapelessOreRecipe, ShapedOreRecipe}
import cpup.mc.lib.network.CPupMessage
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.{Blocks, Items}

trait CPupContent[MOD <: CPupMod[_ <: CPupModRef, _ <: CPupMessage]] {
	def mod: MOD

	final val creativeTab = new CreativeTabs(mod.ref.modID) {
		override def getTabIconItem = null
		override def getIconItemStack: ItemStack = creativeTabItem
	}

	def creativeTabItem: ItemStack = new ItemStack(Blocks.emerald_block)

	protected var _blocks = new mutable.HashMap[String, CPupBlock[MOD]]()
	def blocks = _blocks
	protected var _items = new mutable.HashMap[String, CPupItem[MOD]]()
	def items = _items

	protected var _postInited = false

	def preInit(e: FMLPreInitializationEvent) {}
	def init(e: FMLInitializationEvent) {}
	def postInit(e: FMLPostInitializationEvent) {
		_postInited = true
	}

	def registerItem(item: CPupItem[MOD]) {
		if(item == null)
			throw new RuntimeException("Cannot register null as an item")

		if(item.name == null)
			throw new RuntimeException("No name for item: " + item.getClass.getCanonicalName)

		if(_postInited)
			throw new RuntimeException("Attempt to register an item after initialization")

		items(item.name) = item
		GameRegistry.registerItem(item, item.name, mod.ref.modID)
	}

	def registerBlock(block: CPupBlock[MOD]) {
		registerBlock(block, classOf[GenericItemBlock[MOD]])
	}

	def registerBlock(block: CPupBlock[MOD], item: Class[_ <: ItemBlock], constructorArgs: Object*) {
		if(block == null)
			throw new RuntimeException("Cannot register null as a block")

		if(block.name == null)
			throw new RuntimeException("No name for block: " + block.getClass.getCanonicalName)

		if(_postInited)
		throw new RuntimeException("Attempt to register a block after initialization")

		blocks(block.name) = block
		GameRegistry.registerBlock(block, item, block.name, mod.ref.modID, constructorArgs: _*)
	}

	def addRecipe(result: ItemStack, recipe: Array[String], parts: Any*) {
		GameRegistry.addRecipe(new ShapedOreRecipe(
			result,
			(Array(recipe) ++ parts.map(_ match {
				case c: Char => Character.valueOf(c)
				case v: Any => v
			})).toSeq.toArray.asInstanceOf[Array[Object]]: _*
		))
	}

	def addShapelessRecipe(result: ItemStack, parts: Object*) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(
			result,
			parts: _*
		))
	}
}