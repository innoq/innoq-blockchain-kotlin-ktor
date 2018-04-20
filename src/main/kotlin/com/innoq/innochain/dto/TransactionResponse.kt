package com.innoq.innochain.dto

import java.util.UUID

data class TransactionResponse(val id: UUID, val timestamp: Long, val payload: String, val confirmed: Boolean)