package com.innoq.innochain.model

import java.util.*
import kotlin.collections.ArrayList

object BlockChain {

    val id: UUID = UUID.randomUUID()

    val blocks: MutableList<Block> = ArrayList()

}