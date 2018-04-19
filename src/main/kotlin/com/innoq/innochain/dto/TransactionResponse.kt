package com.innoq.innochain.dto

import com.google.gson.GsonBuilder
import com.innoq.innochain.util.ByteArrayToHexAsciiTypeAdapter
import java.util.UUID

data class TransactionResponse(val id: UUID, val timestamp: Long, val payload: String, val confirmed: Boolean) {
	private fun toJson(): String {
		val gson = GsonBuilder()
				.registerTypeHierarchyAdapter(ByteArray::class.java, ByteArrayToHexAsciiTypeAdapter())
				.setPrettyPrinting()
				.create()
		return gson.toJson(this);
	}
		
	override fun toString(): String = toJson()
}