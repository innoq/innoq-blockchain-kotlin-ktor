package com.innoq.innochain.model

import java.security.MessageDigest
import java.time.Instant
import com.google.gson.GsonBuilder
import com.innoq.innochain.util.ByteArrayToHexAsciiTypeAdapter
import com.innoq.innochain.util.toHexString

data class Block(
	val index: Int,
	val previousBlockHash: ByteArray,
	val transaction: Collection<Transaction>,
	val proof: Long,
	val timestamp: Long) {
	
	private fun toJson(): String {
		val gson = GsonBuilder()
				.registerTypeHierarchyAdapter(ByteArray::class.java, ByteArrayToHexAsciiTypeAdapter())
				.setPrettyPrinting()
				.create()
		return gson.toJson(this);
	}
		
	override fun toString() = toJson()
}