package cpup.mc.lib

trait CPupModHolder[MOD <: CPupMod[_ <: CPupModRef]] {
	def mod: MOD
}
