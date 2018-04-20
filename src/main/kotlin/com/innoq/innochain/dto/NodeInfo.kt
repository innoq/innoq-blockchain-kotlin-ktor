package com.innoq.innochain.dto

import com.innoq.innochain.model.Node
import java.util.*

data class NodeInfo(val node: UUID, val currentBlockHeight: Int, val neighbours : List<Node>)