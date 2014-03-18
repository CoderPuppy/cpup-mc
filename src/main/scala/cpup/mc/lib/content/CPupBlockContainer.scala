package cpup.mc.lib.content

import cpup.mc.lib.{CPupModRef, CPupMod}

trait CPupBlockContainer[MOD <: CPupMod[_ <: CPupModRef]] extends CPupBlock[MOD] {

}