package cpup.mc.lib.network

import cpup.mc.lib.CPupMod

trait BlockMessage[MOD <: CPupMod[_]] extends CPupMessage[MOD] {
	def x: Int
	def y: Int
	def z: Int
}