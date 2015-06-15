package cpup.mc.lib

trait CPupCommonProxy[MOD <: CPupMod[_ <: CPupModRef]] {
	def mod: MOD
}
