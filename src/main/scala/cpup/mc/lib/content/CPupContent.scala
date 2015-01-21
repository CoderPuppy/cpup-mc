package cpup.mc.lib.content

import cpw.mods.fml.common.registry.GameRegistry
import cpup.mc.lib.{CPupModRef, CPupMod}
import scala.collection.mutable
import net.minecraft.item.{ItemStack, ItemBlock}
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.oredict.{ShapelessOreRecipe, ShapedOreRecipe}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.crafting.IRecipe

trait CPupContent[MOD <: CPupMod[_ <: CPupModRef]] extends ContentRegistrar[MOD] {
	final val creativeTab = new CreativeTabs(mod.ref.modID) {
		override def getTabIconItem = null
		override def getIconItemStack: ItemStack = creativeTabItem
	}

	def creativeTabItem: ItemStack = new ItemStack(Blocks.emerald_block)

	protected var _blocks = new mutable.HashMap[String, CPupBlock[MOD]]()
	def blocks = _blocks
	protected var _items = new mutable.HashMap[String, CPupItem]()
	def items = _items

	protected var _postInited = false

	def preInit(e: FMLPreInitializationEvent) {}
	def init(e: FMLInitializationEvent) {}
	def postInit(e: FMLPostInitializationEvent) {
		_postInited = true
	}

	override def registerItem(item: CPupItem) {
		if(item == null)
			throw new RuntimeException("Cannot register null as an item")

		if(item.name == null)
			throw new RuntimeException("No name for item: " + item.getClass.getCanonicalName)

		if(_postInited)
			throw new RuntimeException("Attempt to register an item after initialization")

		items(item.name) = item
		GameRegistry.registerItem(item, item.name, mod.ref.modID)
	}

	override def registerBlock(block: CPupBlock[MOD], item: Class[_ <: ItemBlock], constructorArgs: Object*) {
		if(block == null)
			throw new RuntimeException("Cannot register null as a block")

		if(block.name == null)
			throw new RuntimeException("No name for block: " + block.getClass.getCanonicalName)

		if(_postInited)
		throw new RuntimeException("Attempt to register a block after initialization")

		blocks(block.name) = block
		GameRegistry.registerBlock(block, item, block.name, constructorArgs: _*)
	}
	override def registerTileEntity(cla: Class[_ <: CPupTE[MOD]], id: String) {
		GameRegistry.registerTileEntity(cla, id)
	}

	override def registerRecipe(recipe: IRecipe) {
		GameRegistry.addRecipe(recipe)
	}
}