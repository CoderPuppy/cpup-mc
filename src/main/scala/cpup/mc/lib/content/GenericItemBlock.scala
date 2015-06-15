package cpup.mc.lib.content

import cpup.mc.lib.{CPupModRef, CPupMod}
import net.minecraft.block.Block
import net.minecraft.item.ItemBlock

class GenericItemBlock[MOD <: CPupMod[_ <: CPupModRef]](block: Block) extends ItemBlock(block) {
	if(!block.isInstanceOf[CPupBlock[MOD]]) {
		throw new ClassCastException("GenericItemBlock is only for CPupBlocks")
	}

	val cBlock = block.asInstanceOf[CPupBlock[MOD]]

	override def getCreativeTabs = cBlock.getCreativeTabs
}
