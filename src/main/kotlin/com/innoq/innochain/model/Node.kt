package com.innoq.innochain.model

import com.google.gson.GsonBuilder
import java.util.*

data class Node(
        val nodeId: UUID,
        val host: String) {
    private fun toJson(): String {
        val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()
        return gson.toJson(this);
    }

    override fun toString() = toJson()
}