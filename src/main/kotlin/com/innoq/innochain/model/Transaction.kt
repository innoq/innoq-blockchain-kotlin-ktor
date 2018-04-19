package com.innoq.innochain.model

import com.google.gson.GsonBuilder
import com.innoq.innochain.util.ByteArrayToHexAsciiTypeAdapter
import java.util.UUID

data class Transaction(val id: UUID, val timestamp: Long, val payload: String) {
	private fun toJson(): String {
		val gson = GsonBuilder()
				.registerTypeHierarchyAdapter(ByteArray::class.java, ByteArrayToHexAsciiTypeAdapter())
				.setPrettyPrinting()
				.create()
		return gson.toJson(this);
	}
		
	override fun toString(): String = toJson()
}