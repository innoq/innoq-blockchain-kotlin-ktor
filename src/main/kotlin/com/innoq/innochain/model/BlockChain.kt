package com.innoq.innochain.model

import com.innoq.innochain.util.toHexString
import java.security.MessageDigest
import java.time.Instant
import java.util.Queue
import java.util.UUID
import java.util.concurrent.LinkedBlockingQueue
import java.util.Deque
import java.util.ArrayDeque

object BlockChain {

	val id: UUID = UUID.randomUUID()

	private var _blocks: MutableList<Block> = ArrayList()
	val blocks: List<Block> get() = _blocks

	var transactions: Deque<Transaction> = ArrayDeque()

	init {
		mine(1, ByteArray(1),
				listOf(Transaction(UUID.fromString("b3c973e2-db05-4eb5-9668-3e81c7389a6d"), 0, "I am Heribert Innoq")),
				0)
	}

	private fun mine(index: Int, previousBlockHash: ByteArray, transactions: List<Transaction>, timestamp: Long): Block {
		var proof: Long = -1

		var block: Block
		do {
			block = Block(index, previousBlockHash, transactions, proof++, timestamp)
			val digest = calculateDigest(block)
		} while (!matchesRequiredPattern(digest))

		_blocks.add(block)

		return block
	}

	fun mine(): Unit {
		val list: MutableList<Transaction> = ArrayList()
		while(!transactions.isEmpty() && list.size < 5) {
			list.add(transactions.poll())
		}
		
		if(list.isEmpty()) {
			return
		}
		
		mine(_blocks.size + 1, calculateDigest(_blocks.last()), list, Instant.now().epochSecond)
	}

	private fun calculateDigest(block: Block): ByteArray {
		val md: MessageDigest = MessageDigest.getInstance("SHA-256")
		return md.digest(block.toString().toByteArray(Charsets.UTF_8))
	}

	private fun matchesRequiredPattern(value: ByteArray): Boolean {
		return value.toHexString().startsWith("0000")
	}
}