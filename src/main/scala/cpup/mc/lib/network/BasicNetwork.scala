package cpup.mc.lib.network

import cpup.mc.lib.{CPupModRef, CPupMod}

class BasicNetwork[MOD <: CPupMod[_ <: CPupModRef]](val mod: MOD) extends CPupNetwork[MOD]