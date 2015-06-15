package cpup.mc.lib.network

import cpup.mc.lib.{CPupModRef, CPupMod}

trait BlockMessage[MOD <: CPupMod[ _ <: CPupModRef]] extends CPupMessage[MOD] {
	def x: Int
	def y: Int
	def z: Int
}
