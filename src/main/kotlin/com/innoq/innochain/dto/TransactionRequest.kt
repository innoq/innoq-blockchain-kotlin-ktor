package com.innoq.innochain.dto

import com.google.gson.GsonBuilder
import com.innoq.innochain.util.ByteArrayToHexAsciiTypeAdapter

class TransactionRequest(val payload: String) {
		private fun toJson(): String {
		val gson = GsonBuilder()
				.registerTypeHierarchyAdapter(ByteArray::class.java, ByteArrayToHexAsciiTypeAdapter())
				.setPrettyPrinting()
				.create()
		return gson.toJson(this);
	}
		
	override fun toString(): String = toJson()
}