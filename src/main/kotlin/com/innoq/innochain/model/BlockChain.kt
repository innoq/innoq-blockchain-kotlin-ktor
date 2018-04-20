package com.innoq.innochain.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.innoq.innochain.dto.NodeInfo
import com.innoq.innochain.util.toHexString
import java.io.Reader
import java.security.MessageDigest
import java.time.Instant
import java.util.ArrayDeque
import java.util.Deque
import java.util.UUID
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.last
import kotlin.collections.listOf

object BlockChain {

    val id: UUID = UUID.randomUUID()
    var port: Int = 0
    private var _neighbours: MutableList<Node> = ArrayList()
    val neighbours: List<Node> get() = _neighbours
    private var _blocks: MutableList<Block> = ArrayList()
    val blocks: List<Block> get() = _blocks

    var transactions: Deque<Transaction> = ArrayDeque()

    private val executor = Executors.newFixedThreadPool(1)

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
        executor.execute(Runnable {

            val list: MutableList<Transaction> = ArrayList()
            while (!transactions.isEmpty() && list.size < 2) {
                list.add(transactions.poll())
            }

            if (!list.isEmpty()) {
                mine(_blocks.size + 1, calculateDigest(_blocks.last()), list, Instant.now().epochSecond)
            }
        })
    }

    private fun calculateDigest(block: Block): ByteArray {
        val md: MessageDigest = MessageDigest.getInstance("SHA-256")
        return md.digest(block.toString().toByteArray(Charsets.UTF_8))
    }

    private fun matchesRequiredPattern(value: ByteArray): Boolean {
        return value.toHexString().startsWith("0000")
    }

    fun nodeInfo(): Node {
        return Node(id, "http://localhost:${port}")
    }

    fun registerNode(host: String): Node {
        class NodeDeserializer : ResponseDeserializable<NodeInfo> {
            override fun deserialize(reader: Reader) = Gson().fromJson(reader, NodeInfo::class.java)
        }
        val (request, response, result) = host.httpGet().responseObject(NodeDeserializer())
        val nodeInfo = result.get()
        val node = Node(nodeInfo.node, host)
        _neighbours.add(node)
        return node
    }
}
