package com.innoq.innochain.model

import java.security.MessageDigest
import java.time.Instant
import com.google.gson.GsonBuilder
import com.innoq.innochain.util.ByteArrayToHexAsciiTypeAdapter
import com.innoq.innochain.util.toHexString

class Block(
	val index: Int,
	val previousBlockHash: ByteArray,
	val transaction: Collection<Any> = emptyList()) {
	
	var proof: Long = -1
		private set
	
	val timestamp: Long = Instant.now().epochSecond
	
	init {
		val md: MessageDigest = MessageDigest.getInstance("SHA-256")
		
		var digest: ByteArray = ByteArray(md.digestLength)
		do {
			proof++
			digest = md.digest(toJson().toByteArray(Charsets.UTF_8))
		} while(!matchesRequiredPattern(digest))
	}
	
	private fun toJson(): String {
		val gson = GsonBuilder()
				.registerTypeHierarchyAdapter(ByteArray::class.java, ByteArrayToHexAsciiTypeAdapter())
				.setPrettyPrinting()
				.create()
		return gson.toJson(this);
	}
	
	private fun matchesRequiredPattern(value: ByteArray): Boolean {
		return value.toHexString().startsWith("0000")
	}
		
	override fun toString(): String = toJson()
}