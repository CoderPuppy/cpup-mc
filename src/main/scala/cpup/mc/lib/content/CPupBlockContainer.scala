package cpup.mc.lib.content

import cpup.mc.lib.{CPupModRef, CPupMod}
import cpup.mc.lib.network.CPupMessage

trait CPupBlockContainer[MOD <: CPupMod[_ <: CPupModRef, _ <: CPupMessage]] extends CPupBlock[MOD] {

}